package me.krob.controller;

import me.krob.model.message.Conversation;
import me.krob.service.ConversationService;
import me.krob.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Conversation> create(@RequestBody Conversation conversation) {
        conversationService.create(conversation);
        return ResponseEntity.ok(conversation);
    }
}
