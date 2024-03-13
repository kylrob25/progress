package me.krob.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.krob.model.Role;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private final String token, refreshToken, id, username, email;
    private final Role[] roles;
}
