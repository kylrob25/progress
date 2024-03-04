package me.krob.controller;

import jakarta.servlet.http.HttpServletRequest;
import me.krob.model.Role;
import me.krob.model.User;
import me.krob.model.auth.AuthResponse;
import me.krob.model.auth.LoginRequest;
import me.krob.model.auth.LoginResponse;
import me.krob.model.auth.RegisterRequest;
import me.krob.model.token.RefreshToken;
import me.krob.repository.UserRepository;
import me.krob.security.service.UserDetailsImpl;
import me.krob.service.RefreshTokenService;
import me.krob.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String CLEAN_COOKIE_HEADER = "%s; %s";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie tokenCookie = jwtUtils.generateTokenCookie(user);

        RefreshToken refreshToken = refreshTokenService.create(user.getUsername());
        ResponseCookie refreshCookie = jwtUtils.generateRefreshCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new LoginResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles()
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setForename(registerRequest.getForename());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setRoles(Set.of(Role.USER));
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        String refreshToken = jwtUtils.getRefreshFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()){
            return ResponseEntity.badRequest().body(new AuthResponse("Empty refresh token."));
        }

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verify)
                .map(RefreshToken::getUsername)
                .map(username ->{
                    ResponseCookie cookie = jwtUtils.generateTokenCookie(username);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(new AuthResponse("Refreshed token."));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(new AuthResponse("Unknown refresh token.")));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl user){
                refreshTokenService.deleteByUserId(user.getId());
            }
        }

        ResponseCookie cookie = jwtUtils.getCleanCookie();
        ResponseCookie refreshCookie = jwtUtils.getCleanRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, String.format(CLEAN_COOKIE_HEADER, cookie, refreshCookie))
                .body(new AuthResponse("Success."));
    }
}
