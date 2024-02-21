package me.krob.service;

import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    public Trainer create(Trainer trainer, User user) {
        trainer.setUserId(user.getId());
        return trainerRepository.save(trainer);
    }

    public Trainer create(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public void delete(String trainerId) {
        trainerRepository.deleteById(trainerId);
    }

    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getById(String trainerId) {
        return trainerRepository.findById(trainerId);
    }

    public Optional<Trainer> getByUserId(String userId) {
        return trainerRepository.findByUserId(userId);
    }
}
