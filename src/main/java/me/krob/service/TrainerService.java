package me.krob.service;

import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    public Trainer create(User user) {
        Trainer trainer = new Trainer();
        trainer.setUserId(user.getId());
        return trainerRepository.save(trainer);
    }

    public Trainer update(String trainerId, Trainer trainer) {
        return trainerRepository.findById(trainerId)
                .map(t -> {
                    if (!Objects.equals(trainer.getCost(), t.getCost())) {
                        t.setCost(trainer.getCost());
                    }
                    if (!Objects.equals(trainer.getLocation(), t.getLocation())) {
                        t.setLocation(trainer.getLocation());
                    }
                    if (!Objects.equals(trainer.getSpecialization(), t.getSpecialization())) {
                        t.setSpecialization(trainer.getSpecialization());
                    }
                    if (!Objects.equals(trainer.getClientIds(), t.getClientIds())) {
                        t.setClientIds(trainer.getClientIds());
                    }
                    return trainerRepository.save(t);
                })
                .orElseGet(() -> {
                    trainer.setId(trainerId);
                    return trainerRepository.save(trainer);
                });
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
