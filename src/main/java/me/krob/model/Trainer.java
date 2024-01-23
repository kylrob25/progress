package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "trainers")
@Getter @Setter
public class Trainer extends User {

    @Id
    private String id;

    private List<String> clientIds;
}
