package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "payments")
@Getter
@Setter
public class Payment {
    @Id
    private String id;

    private String trainerId;
    private String clientId;

    private Instant creationDate;
    private Instant completionDate;

    private double amount;

    private String link;
}
