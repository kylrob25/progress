package me.krob.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainers")
@Getter @Setter @AllArgsConstructor
public class Trainer {

    @Id
    private String id;

    private String forename;
    private String surname;
    private String email;
    private String password;

    private List<String> clientIds;

    private String specialisation;
    private String location;
    private double cost;
}
