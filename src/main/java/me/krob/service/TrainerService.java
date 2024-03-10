package me.krob.service;

import me.krob.model.Role;
import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.repository.TrainerRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainerService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private TrainerRepository trainerRepository;

    public Trainer create(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer update(String trainerId, Trainer trainer) {
        return trainerRepository.findById(trainerId)
                .map(t -> {
                    if (trainer.getUsername() != null && !Objects.equals(trainer.getUsername(), t.getUsername())) {
                        t.setUsername(trainer.getUsername());
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

    public boolean exists(String trainerId) {
        return trainerRepository.existsById(trainerId);
    }

    public boolean notExistsByUserId(String userId) {
        return !existsByUserId(userId);
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

    /** Client Ids **/

    public void addClientId(String trainerId, String clientId) {
        mongoUtil.addToSet(trainerId, "clientIds", clientId, User.class);
    }

    public void removeClientId(String trainerId, String clientId) {
        mongoUtil.pull(trainerId, "clientIds", clientId, User.class);
    }

    public Optional<Boolean> hasClientId(String trainerId, String clientId) {
        return trainerRepository.findById(trainerId).map(user -> user.getClientIds().contains(clientId));
    }

    public Optional<Set<String>> getClients(String trainerId){
        return trainerRepository.findById(trainerId).map(Trainer::getClientIds);
    }
}
