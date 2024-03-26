package me.krob.model.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
@Getter
@Setter
public class Message {

    @Id
    private String id;

    private String conversationId;
    private String senderId;
    private String sender;
    private String text;
    private Instant timestamp;
}
