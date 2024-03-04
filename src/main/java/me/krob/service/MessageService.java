package me.krob.service;

import me.krob.repository.MessageRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private MessageRepository messageRepository;
}
