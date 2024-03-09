package me.krob.controller;

import me.krob.model.message.Message;
import me.krob.security.service.UserDetailsImpl;
import me.krob.service.ConversationService;
import me.krob.service.MessageService;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.logging.Logger;

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

        Message m = messageService.create(message);
        conversationService.addMessageId(m.getConversationId(), m.getId());
        conversationService.setLastMessageId(m.getConversationId(), m.getId());
        return ResponseEntity.ok(m);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> delete(@PathVariable String messageId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return messageService.getById(messageId)
                .filter(message -> message.getSenderId().equals(userDetails.getId()))
                .map(message -> {
            messageService.deleteById(messageId);
            conversationService.removeMessageId(message.getConversationId(), messageId);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
 }
