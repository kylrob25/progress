package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document()
@Getter
@Setter
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private UserType userType;

    public User() {

    }

    public User(String id, String username, String email, UserType userType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
    }
}
