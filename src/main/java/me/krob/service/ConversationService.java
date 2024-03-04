package me.krob.service;

import me.krob.model.message.Conversation;
import me.krob.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConversationRepository conversationRepository;

    // TODO:
    public Conversation create(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
