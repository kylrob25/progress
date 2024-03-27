package me.krob.controller;

import me.krob.model.Role;
import me.krob.model.auth.AuthResponse;
import me.krob.model.client.Client;
import me.krob.model.client.ClientRequest;
import me.krob.model.Trainer;
import me.krob.model.client.ClientUpdate;
import me.krob.service.ClientService;
import me.krob.service.TrainerService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Trainer trainer) {
        String userId = trainer.getUserId();

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Does not contain User ID."));
        }

        if (!userService.exists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Cannot find User entity with that ID."));
        }

        if (trainerService.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("Trainer already exists for this User entity."));
        }

        return userService.getById(userId).map(user -> {
            trainer.setUsername(user.getUsername());
            userService.addRole(userId, Role.TRAINER);
            Trainer created = trainerService.create(trainer);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<?> update(@PathVariable String trainerId, @RequestBody Trainer trainer) {
        if (!trainerService.exists(trainerId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Could not find Trainer entity with that ID."));
        Trainer updated = trainerService.update(trainerId, trainer);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<?> delete(@PathVariable String trainerId) {
        if (!trainerService.exists(trainerId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Could not find Trainer entity with that ID."));
        trainerService.delete(trainerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/userId/{userId}")
    public ResponseEntity<?> deleteByUserId(@PathVariable String userId) {
        if (!trainerService.existsByUserId(userId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Could not find Trainer entity with that User ID."));
        trainerService.deleteByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Trainer> getAll() {
        return trainerService.getAll();
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<?> getById(@PathVariable String trainerId) {
        return trainerService.getById(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Trainer> getByUsername(@PathVariable String username) {
        return trainerService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<Trainer> getByUserId(@PathVariable String userId) {
        return trainerService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{trainerId}/clients/{clientId}/exists")
    public ResponseEntity<?> hasClient(@PathVariable String trainerId, @PathVariable String clientId) {
        return trainerService.hasClientId(trainerId, clientId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Deprecated
    @PutMapping("/{trainerId}/clients")
    public ResponseEntity<Trainer> addClient(@PathVariable String trainerId, @RequestBody String clientId) {
        trainerService.addClientId(trainerId, clientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{trainerId}/clients/{clientId}")
    public ResponseEntity<Trainer> removeClient(@PathVariable String trainerId, @PathVariable String clientId) {
        clientService.deleteById(clientId);
        trainerService.removeClientId(trainerId, clientId);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @GetMapping("/{trainerId}/clientsIds")
    public ResponseEntity<Set<String>> getClientIds(@PathVariable String trainerId) {
        return trainerService.getClients(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{trainerId}/clients")
    public ResponseEntity<Set<Client>> getClients(@PathVariable String trainerId) {
        return trainerService.getClients(trainerId)
                .map(ids -> ids.stream()
                        .map(id -> clientService.getById(id))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{trainerId}/requests/{userId}")
    public ResponseEntity<?> addClientRequest(@PathVariable String trainerId, @PathVariable String userId) {
        if (!trainerService.exists(trainerId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Trainer entity with that ID."));
        if (!userService.exists(userId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID."));
        long updated = trainerService.addRequestId(trainerId, userId);
        if (updated > 0) return ResponseEntity.ok().body(new AuthResponse("Added client request to Trainer entity."));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Failed to add client request to Trainer entity."));
    }

    @PostMapping("/{trainerId}/requests/{userId}")
    public ResponseEntity<?> acceptClientRequest(@PathVariable String trainerId, @PathVariable String userId) {
        return trainerService.getRequestIds(trainerId).map(ids -> {
            if (ids.contains(userId)) {
                return userService.getById(userId).map(user -> {
                    Client client = clientService.create(trainerId, user);

                    long removed = trainerService.removeRequestId(trainerId, userId);
                    if(removed < 1) return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Cannot find request in Trainer entity with that ID."));

                    long added = trainerService.addClientId(trainerId, client.getId());
                    if (added < 1) return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Failed to add Client ID to Trainer entity."));

                    return ResponseEntity.ok().body(new AuthResponse("Added Client ID to Trainer entity."));
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID.")));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find request in Trainer entity."));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Trainer entity with that ID.")));
    }

    @DeleteMapping("/{trainerId}/requests/{userId}")
    public ResponseEntity<?> denyClientRequest(@PathVariable String trainerId, @PathVariable String userId) {
        return trainerService.getRequestIds(trainerId).map(ids -> {
            if (ids.contains(userId)) {
                return userService.getById(userId).map(user -> {
                    long removed = trainerService.removeRequestId(trainerId, userId);
                    if (removed < 1) return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Cannot find request in Trainer entity with that ID."));
                    return ResponseEntity.ok().body(new AuthResponse("Removed request from Trainer entity."));
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find User entity with that ID.")));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find request in Trainer entity."));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Cannot find Trainer entity with that ID.")));
    }

    @Deprecated
    @GetMapping("/{trainerId}/requestIds")
    public ResponseEntity<Set<String>> getClientRequestIds(@PathVariable String trainerId) {
        return trainerService.getRequestIds(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{trainerId}/requests")
    public ResponseEntity<?> getClientRequests(@PathVariable String trainerId) {
        Optional<Set<String>> optionalIds = trainerService.getRequestIds(trainerId);

        if (optionalIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Cannot find Trainer entity with that ID."));
        }

        Set<String> ids = optionalIds.get();
        Set<ClientRequest> clientRequests = ids.stream()
                .map(userService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> new ClientRequest(user.getId(), user.getUsername()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(clientRequests);
    }

    @GetMapping("/{trainerId}/payments")
    public ResponseEntity<Set<String>> getPayments(@PathVariable String trainerId) {
        return trainerService.getPaymentIds(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
