package me.krob.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class RegisterRequest {
    private final String username, forename, surname, email, password;
}
