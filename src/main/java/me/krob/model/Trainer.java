package me.krob.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainers")
@Getter @Setter @AllArgsConstructor
public class Trainer extends User {

    @Id
    private String id;

    private List<String> clientIds;

    private String specialisation;
    private String location;
    private Double cost;
}
