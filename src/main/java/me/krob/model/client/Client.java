package me.krob.model.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "clients")
@Getter
@Setter
public class Client {
    @Id
    private String id;

    private String username;
    private String userId;
    private String trainerId;

    // TODO:
    private int weight;
    private int calories;

    private Set<String> paymentIds;
}
