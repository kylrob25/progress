package me.krob.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
@Getter @Setter
public class Client {

    @Id
    private String id;

    @DBRef
    private Trainer trainer;
}
