package me.krob.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "clients")
public class Client {
    @Id
    private String id;

    private String userId;

    private List<String> trainerIds;
}
