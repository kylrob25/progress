package me.krob.service;

import me.krob.model.message.Message;
import me.krob.repository.MessageRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private MessageRepository messageRepository;

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Message> getById(String messageId) {
        return messageRepository.findById(messageId);
    }
}
