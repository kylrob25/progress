package me.krob.model.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "conversations")
@Getter
@Setter
public class Conversation {
    @Id
    private String id;

    private String title;
    private String creatorId;

    private Set<String> participantIds = new HashSet<>();
    private Set<String> participantNames = new HashSet<>();

    private Set<String> messageIds = new HashSet<>();

    private String lastMessageId;

    public boolean isCreator(String userId) {
        return creatorId.equals(userId);
    }
}
