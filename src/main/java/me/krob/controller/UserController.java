package me.krob.controller;

import me.krob.model.Role;
import me.krob.model.User;
import me.krob.model.auth.AuthResponse;
import me.krob.security.service.UserDetailsImpl;
import me.krob.service.UserService;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody User user) {
        User updated = userService.update(userId, user);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<AuthResponse> delete(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.ok().body(new AuthResponse("Attempted to delete User entity."));
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDetailsImpl> validateToken() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) obj;
            return ResponseEntity.ok(userDetails);
        } catch (Throwable throwable) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable String userId) {
        return userService.getById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        return userService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/roles/{role}/exists")
    public ResponseEntity<?> hasRole(@PathVariable String userId, @PathVariable Role role) {
        return userService.hasRole(userId, role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/roles/{role}")
    public ResponseEntity<AuthResponse> addRole(@PathVariable String userId, @PathVariable Role role) {
        userService.addRole(userId, role);
        return ResponseEntity.ok().body(new AuthResponse("Attempted to add role to User entity."));
    }

    @DeleteMapping("/{userId}/roles/{role}")
    public ResponseEntity<AuthResponse> removeRole(@PathVariable String userId, @PathVariable Role role) {
        userService.removeRole(userId, role);
        return ResponseEntity.ok().body(new AuthResponse("Attempted to remove role from User entity."));
    }

    @PutMapping("/{userId}/conversation")
    public ResponseEntity<AuthResponse> addConversationId(@PathVariable String userId, @RequestBody String conversationId) {
        mongoUtil.addToSet(userId, "conversationIds", conversationId, User.class);
        return ResponseEntity.ok().body(new AuthResponse("Attempted to add conversation ID to the User entity."));
    }

    @DeleteMapping("/{userId}/conversation")
    public ResponseEntity<AuthResponse> clearConversationIds(@PathVariable String userId) {
        mongoUtil.set(userId, "conversationIds", new Set[0], User.class);
        return ResponseEntity.ok().body(new AuthResponse("Attempted to clear conversation IDs from the User entity."));
    }
}
