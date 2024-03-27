package me.krob.controller;

import me.krob.model.User;
import me.krob.model.auth.AuthResponse;
import me.krob.model.message.Conversation;
import me.krob.model.message.Message;
import me.krob.security.service.UserDetailsImpl;
import me.krob.service.ConversationService;
import me.krob.service.MessageService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> create(@RequestBody Conversation conversation) {
        String conversationId = conversation.getId();

        if (conversationId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Missing Conversation ID."));
        }

        String userId = conversation.getCreatorId();

        if (userId == null || userService.exists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
        }

        userService.getById(userId).ifPresent(user -> {
            String username = user.getUsername();

            conversation.setCreatorId(user.getId());
            conversation.setTitle(username + "'s Conversation");
            conversation.getParticipantNames().add(username);
        });

        Set<String> participantIds = conversation.getParticipantIds();

        if (participantIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("No participants included."));
        }

        for (String participantId: participantIds) {
            if (!userService.exists(participantId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
            }

            long updated = userService.addConversation(participantId, conversationId);
            if (updated < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("One or more participant is already in this Conversation entity."));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(conversationService.create(conversation));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> delete(@PathVariable String conversationId) {
        if (!conversationService.exists(conversationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Conversation entity with that ID."));
        }

        conversationService.getById(conversationId).ifPresent(conversation -> {
            conversation.getMessageIds()
                    .forEach(messageService::deleteById);
            conversation.getParticipantIds()
                    .forEach(userId -> userService.removeConversation(userId, conversationId));
        });

        conversationService.deleteById(conversationId);
        return ResponseEntity.ok().body(new AuthResponse("Deleted Conversation entity."));
    }

    @DeleteMapping("/{conversationId}/leave/{userId}")
    public ResponseEntity<?> leave(@PathVariable String conversationId, String userId) {
        if (conversationId == null || !conversationService.exists(conversationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Conversation entity with that ID."));
        }

        if (userId == null || !userService.exists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
        }

        conversationService.getById(conversationId).ifPresent(conversation -> {
            userService.getById(userId).map(user -> {
                long names = conversationService.removeParticipantName(conversationId, user.getUsername());
                if (names < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to remove participant name from Conversation entity."));

                long ids = conversationService.removeParticipantId(conversationId, user.getId());
                if (ids < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to remove participant ID from Conversation entity."));

                long removed = userService.removeConversation(userId, conversationId);
                if (removed < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to remove participant from Conversion entity."));

                return ResponseEntity.ok(new AuthResponse("Removed participant from Conversation entity."));
            }).orElseGet(() ->  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID.")));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Something went wrong when removing participant from Conversation entity."));
    }

    @PutMapping("/{conversationId}/add/{username}")
    public ResponseEntity<?> addParticipant(@PathVariable String conversationId, @PathVariable String username) {
        if (conversationId == null || !conversationService.exists(conversationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Conversation entity with that ID."));
        }

        if (username == null || !userService.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
        }

        conversationService.getById(conversationId).ifPresent(conversation -> {
            userService.getByUsername(username).map(User::getId).map(userId -> {
                long conv = userService.addConversation(userId, conversationId);
                if (conv < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to add Conversation ID to User entity."));

                long id = conversationService.addParticipantId(conversationId, userId);
                if (id < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to add User ID to Conversation entity."));

                long name = conversationService.addParticipantName(conversationId, username);
                if (name < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to add User username to Conversation entity."));

                return ResponseEntity.ok(new AuthResponse("Added User to Conversation entity."));
            }).orElseGet(() ->  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID.")));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Something went wrong when adding participant to Conversation entity."));
    }


    @GetMapping
    public List<Conversation> getAll() {
        return conversationService.getAll();
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@PathVariable String conversationId) {
        return conversationService.getById(conversationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String conversationId) {
        if (conversationId == null || !conversationService.exists(conversationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Conversation entity with that ID."));
        }

        return conversationService.getById(conversationId)
                .map(Conversation::getMessageIds)
                .map(messageIds -> messageIds.stream()
                        .map(messageService::getById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet())
                )
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getConversationsByUser(@PathVariable String userId) {
        if (userId == null || !userService.exists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
        }

        return userService.getById(userId)
                .map(User::getConversationIds)
                .map(conversationService::getUserConversations)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
