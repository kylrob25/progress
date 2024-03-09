package me.krob.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class TokenResponse {
    private final String token, refreshToken;
}
