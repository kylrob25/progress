package me.krob.service;

import me.krob.model.Role;
import me.krob.model.User;
import me.krob.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(String userId, User user) {
        return userRepository.findById(userId)
                .map(t -> {
                    if (user.getUsername() != null && !Objects.equals(user.getUsername(), t.getUsername())) {
                        t.setUsername(user.getUsername());
                    }
                    if (user.getForename() != null && !Objects.equals(user.getForename(), t.getForename())) {
                        t.setForename(user.getForename());
                    }
                    if (user.getSurname() != null && !Objects.equals(user.getSurname(), t.getSurname())) {
                        t.setSurname(user.getSurname());
                    }
                    if (user.getEmail() != null && !Objects.equals(user.getEmail(), t.getEmail())) {
                        t.setEmail(user.getEmail());
                    }
                    if (user.getPassword() != null && !Objects.equals(user.getPassword(), t.getPassword())) {
                        t.setPassword(user.getPassword());
                    }
                    if (user.getRoles() != null && !user.getRoles().equals(t.getRoles())) {
                        t.setRoles(user.getRoles());
                    }
                    return userRepository.save(t);
                })
                .orElseGet(() -> {
                    user.setId(userId);
                    return userRepository.save(user);
                });
    }

    public void addRoleToUser(String userId, Role role) {
        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update().addToSet("roles", role);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public boolean exists(String userId) {
        return userRepository.existsById(userId);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(String userId) {
        return userRepository.findById(userId);
    }
}
