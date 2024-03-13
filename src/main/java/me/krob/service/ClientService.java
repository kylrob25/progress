package me.krob.service;

import me.krob.model.Client;
import me.krob.model.Payment;
import me.krob.repository.ClientRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ClientService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private ClientRepository clientRepository;

    public Client create(String trainerId, String userId) {
        Client client = new Client();
        client.setUserId(userId);
        client.setTrainerId(trainerId);
        return clientRepository.save(client);
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(String clientId) {
        clientRepository.deleteById(clientId);
    }

    public boolean existsByUserId(String userId) {
        return clientRepository.existsByUserId(userId);
    }

    public Optional<Client> getById(String clientId) {
        return clientRepository.findById(clientId);
    }

    public Optional<Client> getByUserId(String userId) {
        return clientRepository.findByUserId(userId);
    }

    public void updateWeight(String clientId, int weight) {
        mongoUtil.set(clientId, "weight", weight, Client.class);
    }

    public void updateCalories(String clientId, int calories) {
        mongoUtil.set(clientId, "calories", calories, Client.class);
    }

    /**
     * Payment IDs
     */
    public void addPaymentId(String clientId, String paymentId) {
        mongoUtil.addToSet(clientId, "paymentIds", paymentId, Client.class);
    }

    public void removePaymentId(String clientId, String paymentId) {
        mongoUtil.pull(clientId, "paymentIds", paymentId, Client.class);
    }

    public Optional<Set<String>> getPaymentIds(String clientId) {
        return clientRepository.findById(clientId).map(Client::getPaymentIds);
    }
}
