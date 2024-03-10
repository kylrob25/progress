package me.krob.controller;

import me.krob.model.Role;
import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.service.TrainerService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Trainer trainer) {
        String userId = trainer.getUserId();
        if (userId != null){
            userService.getById(userId).map(user -> {
                trainer.setUsername(user.getUsername());
                return user.getId();
            })
                    .filter(trainerService::notExistsByUserId)
                    .map(s-> ResponseEntity.status(HttpStatus.CREATED).body(trainerService.create(trainer)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<Trainer> update(@PathVariable String trainerId, @RequestBody Trainer trainer) {
        Trainer updated = trainerService.update(trainerId, trainer);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<?> delete(@PathVariable String trainerId) {
        trainerService.delete(trainerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/userId/{userId}")
    public ResponseEntity<?> deleteByUserId(@PathVariable String userId) {
        trainerService.deleteByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Trainer> getAll() {
        return trainerService.getAll();
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getById(@PathVariable String trainerId) {
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

    @PutMapping("/{trainerId}/clients")
    public ResponseEntity<Trainer> addClient(@PathVariable String trainerId, @RequestBody String clientId) {
        trainerService.addClientId(trainerId, clientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{trainerId}/clients/{clientId}")
    public ResponseEntity<Trainer> removeClient(@PathVariable String trainerId, @PathVariable String clientId) {
        trainerService.removeClientId(trainerId, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainerId}/clients/")
    public ResponseEntity<Set<String>> getClients(@PathVariable String trainerId){
        return trainerService.getClients(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
