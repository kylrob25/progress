package me.krob.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class User {
    private String forename;
    private String surname;
    private String email;
    private String password;
}
