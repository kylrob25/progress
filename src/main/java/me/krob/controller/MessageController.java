package me.krob.controller;

import me.krob.model.message.Message;
import me.krob.service.ConversationService;
import me.krob.service.MessageService;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable String messageId) {
        return messageService.getById(messageId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Message message) {
        message.setTimestamp(Instant.now());
        conversationService.addMessageId(message.getConversationId(), message.getId());
        return ResponseEntity.ok(messageService.create(message));
    }
 }
