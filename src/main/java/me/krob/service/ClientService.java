package me.krob.service;

import me.krob.model.Client;
import me.krob.repository.ClientRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private ClientRepository clientRepository;

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
}
