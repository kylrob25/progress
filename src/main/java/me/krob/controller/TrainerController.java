package me.krob.controller;

import me.krob.model.Trainer;
import me.krob.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping
    public ResponseEntity<Trainer> createTrainer(@RequestBody Trainer trainer) {
        Trainer created = trainerService.create(trainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<Trainer> updateTrainer(@PathVariable String trainerId, @RequestBody Trainer trainer) {
        Trainer updated = trainerService.update(trainerId, trainer);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<?> deleteTrainer(@PathVariable String trainerId) {
        trainerService.delete(trainerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable String trainerId) {
        return trainerService.getTrainerById(trainerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
