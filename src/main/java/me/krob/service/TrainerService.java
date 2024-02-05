package me.krob.service;

import me.krob.model.Trainer;
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

    public Trainer create(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer update(String trainerId, Trainer trainer) {
        return trainerRepository.findById(trainerId)
                .map(t -> {
                    if (Objects.nonNull(trainer.getForename())) {
                        t.setForename(trainer.getForename());
                    }
                    if(Objects.nonNull(trainer.getSurname())) {
                        t.setSurname(trainer.getSurname());
                    }
                    if (Objects.nonNull(trainer.getEmail())) {
                        t.setEmail(trainer.getEmail());
                    }
                    if (Objects.nonNull(trainer.getCost())) {
                        t.setCost(trainer.getCost());
                    }
                    if (Objects.nonNull(trainer.getLocation())) {
                        t.setLocation(t.getLocation());
                    }
                    if (Objects.nonNull(trainer.getSpecialisation())) {
                        t.setSpecialisation(trainer.getSpecialisation());
                    }
                    if (Objects.nonNull(trainer.getClientIds())) {
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

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(String trainerId) {
        return trainerRepository.findById(trainerId);
    }
}
