package me.krob.controller;

import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.service.TrainerService;
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
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<Trainer> create(@RequestBody User user) {
        // TODO: Validate user
        Trainer created = trainerService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<?> delete(@PathVariable String trainerId) {
        trainerService.delete(trainerId);
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
}
