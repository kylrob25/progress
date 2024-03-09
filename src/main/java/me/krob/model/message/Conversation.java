package me.krob.model.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "conversations")
@Getter @Setter
public class Conversation {
    @Id
    private String id;

    private String title;
    private String creatorId;

    private Set<String> participantIds;
    private Set<String> messageIds;

    private String lastMessageId;
}
