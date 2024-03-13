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

    /**
     * General
     **/

    public Conversation create(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public void deleteById(String conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    public List<Conversation> getAll() {
        return conversationRepository.findAll();
    }

    public Optional<Conversation> getById(String conversationId) {
        return conversationRepository.findById(conversationId);
    }

    public Set<Conversation> getUserConversations(Set<String> conversationIds) {
        return conversationRepository.findByIdIn(conversationIds);
    }

    /**
     * Participant IDs
     **/

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

    /**
     * Participant Names
     **/

    public void addParticipantName(String conversationId, String participantName) {
        mongoUtil.addToSet(conversationId, "participantNames", participantName, Conversation.class);
    }

    public void removeParticipantName(String conversationId, String participantName) {
        mongoUtil.pull(conversationId, "participantNames", participantName, Conversation.class);
    }

    public Optional<Set<String>> getParticipantNames(String conversationId) {
        return conversationRepository.findById(conversationId)
                .map(Conversation::getParticipantNames);
    }

    public Optional<Boolean> hasParticipantName(String conversationId, String participantName) {
        return conversationRepository.findById(conversationId)
                .map(conversation -> conversation.getParticipantNames().contains(participantName));
    }

    /**
     * Message IDs
     **/

    public void setLastMessageId(String conversationId, String lastMessageId) {
        mongoUtil.set(conversationId, "lastMessageId", lastMessageId, Conversation.class);
    }

    public void addMessageId(String conversationId, String messageId) {
        mongoUtil.addToSet(conversationId, "messageIds", messageId, Conversation.class);
    }

    public void removeMessageId(String conversationId, String messageId) {
        mongoUtil.pull(conversationId, "messageIds", messageId, Conversation.class);
    }

    public Optional<Set<String>> getMessageIds(String conversationId) {
        return conversationRepository.findById(conversationId)
                .map(Conversation::getMessageIds);
    }

    public Optional<Boolean> hasMessageId(String conversationId, String messageId) {
        return conversationRepository.findById(conversationId)
                .map(conversation -> conversation.getMessageIds().contains(messageId));
    }
}
