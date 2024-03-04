package me.krob.service;

import me.krob.model.message.Conversation;
import me.krob.repository.ConversationRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConversationService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private ConversationRepository conversationRepository;

    /** General **/

    public Conversation create(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    /** Participant IDs **/

    public void addParticipantId(String conversationId, String participantId) {
        mongoUtil.addToSet(conversationId, "participantIds", participantId, Conversation.class);
    }

    public void removeParticipantId(String conversationId, String participantId) {
        mongoUtil.pull(conversationId, "participantIds", participantId, Conversation.class);
    }

    public Optional<Set<String>> getParticipantIds(String conversationId) {
        return conversationRepository.findById(conversationId)
                .map(Conversation::getParticipantIds);
    }

    public Optional<Boolean> hasParticipantId(String conversationId, String participantId) {
        return conversationRepository.findById(conversationId)
                .map(conversation -> conversation.getParticipantIds().contains(participantId));
    }

    /** Message IDs **/

    public void addMessageId(String conversationId, String messageId) {
        mongoUtil.addToSet(conversationId, "messageIds", messageId, Conversation.class);
    }

    public void removeMessageId(String conversationId, String messageId) {
        mongoUtil.pull(conversationId, "messageIds", messageId, Conversation.class);
    }

    public Optional<List<String>> getMessageIds(String conversationId) {
        return conversationRepository.findById(conversationId)
                .map(Conversation::getMessageIds);
    }

    public Optional<Boolean> hasMessageId(String conversationId, String messageId) {
        return conversationRepository.findById(conversationId)
                .map(conversation -> conversation.getMessageIds().contains(messageId));
    }
}