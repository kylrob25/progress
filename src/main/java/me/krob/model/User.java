package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
public class User {

    @Id
    private ObjectId _id;

    private String emailAddress;
    private String forename;
    private String surname;

    public User() {

    }

    public User(String emailAddress, String forename, String surname) {
        this.emailAddress = emailAddress;
        this.forename = forename;
        this.surname = surname;
    }
}
