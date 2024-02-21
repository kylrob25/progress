package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainers")
@Getter @Setter
public class Trainer {

    @Id
    private String id;

    private String userId;

    private double cost;
    private String location;
    private String specialization;

    private List<String> clientIds;
}
