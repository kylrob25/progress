package me.krob.service;

import me.krob.model.User;
import me.krob.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(String userId, User user) {
        return userRepository.findById(userId)
                .map(t -> {
                    if (!Objects.equals(user.getForename(), t.getForename())) {
                        t.setForename(user.getForename());
                    }
                    if (!Objects.equals(user.getSurname(), t.getSurname())) {
                        t.setSurname(user.getSurname());
                    }
                    if (!Objects.equals(user.getEmail(), t.getEmail())) {
                        t.setEmail(user.getEmail());
                    }
                    return userRepository.save(t);
                })
                .orElseGet(() -> {
                    user.setId(userId);
                    return userRepository.save(user);
                });
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(String userId) {
        return userRepository.findById(userId);
    }
}
