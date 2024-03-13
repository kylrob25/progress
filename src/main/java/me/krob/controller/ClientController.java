package me.krob.controller;

import me.krob.model.Client;
import me.krob.model.Payment;
import me.krob.service.ClientService;
import me.krob.service.PaymentService;
import me.krob.service.TrainerService;
import me.krob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    @Autowired
    private PaymentService paymentService;

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

    @PutMapping("/{clientId}/payments")
    public ResponseEntity<Payment> createPayment(@PathVariable String clientId, @RequestBody Payment payment) {
        return clientService.getById(clientId).map(client -> {
            Payment created = paymentService.create(payment);
            trainerService.addPaymentId(created.getTrainerId(), created.getId());
            clientService.addPaymentId(clientId, created.getId());
            return ResponseEntity.ok(created);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{clientId}/payments/{paymentId}")
    public ResponseEntity<?> completePayment(@PathVariable String clientId, @PathVariable String paymentId) {
        // TODO: Handle payments that are already completed
        return clientService.getById(clientId).map(client -> {
                if (client.getPaymentIds().contains(paymentId)) {
                    paymentService.completePayment(paymentId);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.notFound().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{clientId}/payments")
    public ResponseEntity<Set<String>> getPayments(@PathVariable String clientId) {
        return clientService.getById(clientId)
                .map(Client::getPaymentIds)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
