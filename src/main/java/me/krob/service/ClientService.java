package me.krob.service;

import com.mongodb.client.result.UpdateResult;
import me.krob.model.client.Client;
import me.krob.model.User;
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

    public Client create(String trainerId, User user) {
        Client client = new Client();
        client.setUsername(user.getUsername());
        client.setUserId(user.getId());
        client.setTrainerId(trainerId);
        return clientRepository.save(client);
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(String clientId) {
        clientRepository.deleteById(clientId);
    }

    public boolean exists(String clientId) {
        return clientRepository.existsById(clientId);
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

    public long updateWeight(String clientId, int weight) {
        UpdateResult result = mongoUtil.set(clientId, "weight", weight, Client.class);
        return result.getModifiedCount();
    }

    public long updateCalories(String clientId, int calories) {
        UpdateResult result = mongoUtil.set(clientId, "calories", calories, Client.class);
        return result.getModifiedCount();
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
