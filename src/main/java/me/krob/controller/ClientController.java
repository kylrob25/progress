package me.krob.controller;

import me.krob.model.Client;
import me.krob.service.ClientService;
import me.krob.service.TrainerService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client) {
        String userId = client.getUserId();

        if (userId != null && userService.exists(userId)
                && !clientService.existsByUserId(userId)) {
            String trainerId = client.getTrainerId();

            if (trainerId != null && trainerService.exists(trainerId)) {
                Client created = clientService.create(client);

                trainerService.hasClientId(trainerId, client.getId()).map(has -> {
                    if (has) {
                        clientService.deleteById(created.getId());
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                    }

                    trainerService.addClientId(trainerId, client.getId());
                    return ResponseEntity.ok(created);
                }).orElseGet(() -> ResponseEntity.badRequest().build());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<Client> getClient(@PathVariable String userId) {
        return clientService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
