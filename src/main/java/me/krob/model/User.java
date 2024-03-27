package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
public class User {
    @Id
    private String id;

    private String username;
    private String forename;
    private String surname;
    private String email;
    private String password;

    private Set<Role> roles;

    private Set<String> conversationIds = new HashSet<>();
}
