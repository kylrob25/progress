package me.krob.repository;

import me.krob.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findByUserId(String userId);
}
