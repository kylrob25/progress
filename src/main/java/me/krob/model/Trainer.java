package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "trainers")
@Getter
@Setter
public class Trainer {

    @Id
    private String id;

    private String userId;

    private String username;

    private String pictureUrl;

    private Double cost;
    private String location;
    private String specialization;

    private Set<String> clientIds;
}
