package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import me.krob.model.auth.RegisterRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private Role[] roles;
}
