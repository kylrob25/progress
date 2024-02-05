package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
@Getter @Setter
public class Client {

    @Id
    private String id;

    private String forename;
    private String surname;
    private String email;
    private String password;

    private String trainerId;
}
