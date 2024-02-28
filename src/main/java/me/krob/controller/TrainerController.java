package me.krob.controller;

import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.service.TrainerService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<Trainer> create(@RequestBody Trainer trainer) {
        if (trainer.getUserId() != null & userService.exists(trainer.getUserId()) &&
                !trainerService.existsByUserId(trainer.getUserId())) {
            Trainer created = trainerService.create(trainer);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        return ResponseEntity.notFound().build();
    }

    /*
    @PostMapping
    public ResponseEntity<Trainer> create(@RequestBody User user) {
        if (user.getId() != null & userService.exists(user.getId()) &&
                !trainerService.existsByUserId(user.getId())) {
            Trainer created = trainerService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        return ResponseEntity.notFound().build();
    }*/

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
}
