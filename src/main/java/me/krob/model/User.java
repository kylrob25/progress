package me.krob.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private int id;

    private String forename, surname;
}
