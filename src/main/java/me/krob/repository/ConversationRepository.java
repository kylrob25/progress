package me.krob.repository;

import me.krob.model.message.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Set<Conversation> findByIdIn(Set<String> ids);
}
