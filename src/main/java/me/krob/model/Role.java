package me.krob.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum Role {
    ADMIN("Admin"),
    USER("User")
    ;

    private final String name;
}
