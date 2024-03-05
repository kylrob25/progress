package me.krob.controller;

import me.krob.model.User;
import me.krob.model.message.Conversation;
import me.krob.service.ConversationService;
import me.krob.service.MessageService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Conversation> create(@RequestBody Conversation conversation) {
        conversationService.create(conversation);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@PathVariable String conversationId){
        return conversationService.getById(conversationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getConversationsByUser(@PathVariable String userId) {
        return userService.getById(userId)
                .map(User::getConversationIds)
                .map(conversationService::getUserConversations)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
