package me.krob.service;

import com.mongodb.client.result.UpdateResult;
import me.krob.model.Payment;
import me.krob.model.Trainer;
import me.krob.model.User;
import me.krob.repository.TrainerRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    /**
     * Client Ids
     */

    public long addClientId(String trainerId, String clientId) {
        UpdateResult result = mongoUtil.addToSet(trainerId, "clientIds", clientId, Trainer.class);
        return result.getModifiedCount();
    }

    public long removeClientId(String trainerId, String clientId) {
        UpdateResult result = mongoUtil.pull(trainerId, "clientIds", clientId, Trainer.class);
        return result.getModifiedCount();
    }

    public Optional<Boolean> hasClientId(String trainerId, String clientId) {
        return trainerRepository.findById(trainerId).map(trainer -> trainer.getClientIds().contains(clientId));
    }

    public Optional<Set<String>> getClients(String trainerId) {
        return trainerRepository.findById(trainerId).map(Trainer::getClientIds);
    }

    /**
     * Client Request Ids
     */
    public long addRequestId(String trainerId, String userId) {
        UpdateResult result = mongoUtil.addToSet(trainerId, "clientRequestIds", userId, Trainer.class);
        return result.getModifiedCount();
    }

    public long removeRequestId(String trainerId, String userId) {
        UpdateResult result = mongoUtil.pull(trainerId, "clientRequestIds", userId, Trainer.class);
        return result.getModifiedCount();
    }

    public Optional<Boolean> hasRequestId(String trainerId, String userId) {
        return trainerRepository.findById(trainerId).map(trainer -> trainer.getClientRequestIds().contains(userId));
    }

    public Optional<Set<String>> getRequestIds(String trainerId) {
        return trainerRepository.findById(trainerId).map(Trainer::getClientRequestIds);
    }

    /**
     * Payment IDs
     */
    public void addPaymentId(String trainerId, String paymentId) {
        mongoUtil.addToSet(trainerId, "paymentIds", paymentId, Trainer.class);
    }

    public void removePaymentId(String trainerId, String paymentId) {
        mongoUtil.pull(trainerId, "paymentIds", paymentId, Trainer.class);
    }

    public Optional<Set<String>> getPaymentIds(String trainerId) {
        return trainerRepository.findById(trainerId).map(Trainer::getPaymentIds);
    }
}
