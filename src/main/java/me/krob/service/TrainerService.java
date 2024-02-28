package me.krob.service;

import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public Trainer link(String trainerId, User user) {
        return trainerRepository.findById(trainerId)
                .map(t -> {
                    link(t, user);
                    return trainerRepository.save(t);
                }).orElseGet(() -> trainerRepository.save(link(new Trainer(), user)));
    }

    private Trainer link(Trainer trainer, User user) {
        if (user.getForename() != null) {
            trainer.setForename(user.getForename());
        }
        if (user.getSurname() != null) {
            trainer.setSurname(user.getSurname());
        }
        if (user.getUsername() != null) {
            trainer.setUsername(user.getUsername());
        }
        return trainer;
    }

    public Trainer update(String trainerId, Trainer trainer) {
        return trainerRepository.findById(trainerId)
                .map(t -> {
                    if (trainer.getUsername() != null && !Objects.equals(trainer.getUsername(), t.getUsername())) {
                        t.setUsername(trainer.getUsername());
                    }
                    if (trainer.getForename() != null && !Objects.equals(trainer.getForename(), t.getForename())) {
                        t.setForename(trainer.getForename());
                    }
                    if (trainer.getSurname() != null && !Objects.equals(trainer.getSurname(), t.getSurname())) {
                        t.setSurname(trainer.getSurname());
                    }
                    if (trainer.getPictureUrl() != null && !Objects.equals(trainer.getPictureUrl(), t.getPictureUrl())) {
                        t.setPictureUrl(trainer.getPictureUrl());
                    }
                    if (trainer.getCost() != null && !Objects.equals(trainer.getCost(), t.getCost())) {
                        t.setCost(trainer.getCost());
                    }
                    if (trainer.getLocation() != null && !Objects.equals(trainer.getLocation(), t.getLocation())) {
                        t.setLocation(trainer.getLocation());
                    }
                    if (trainer.getSpecialization() != null && !Objects.equals(trainer.getSpecialization(), t.getSpecialization())) {
                        t.setSpecialization(trainer.getSpecialization());
                    }
                    if (trainer.getClientIds() != null && !Arrays.equals(trainer.getClientIds(), t.getClientIds())) {
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

    public void deleteByUserId(String userId) {
        trainerRepository.deleteByUserId(userId);
    }

    public boolean existsByUserId(String userId) {
        return trainerRepository.existsByUserId(userId);
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

    public Optional<Trainer> getByUsername(String trainerId) {
        return trainerRepository.findByUsername(trainerId);
    }
}
