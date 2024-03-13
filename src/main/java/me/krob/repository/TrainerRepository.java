package me.krob.repository;

import me.krob.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findByUserId(String userId);

    Optional<Trainer> findByUsername(String username);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
