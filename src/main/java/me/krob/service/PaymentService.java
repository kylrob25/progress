package me.krob.service;

import me.krob.model.Payment;
import me.krob.repository.ClientRepository;
import me.krob.repository.PaymentRespository;
import me.krob.repository.TrainerRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private PaymentRespository paymentRespository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Payment create(Payment payment) {
        payment.setCreationDate(Instant.now());
        return paymentRespository.save(payment);
    }

    public Optional<Payment> getById(String paymentId) {
        return paymentRespository.findById(paymentId);
    }

    public Optional<Boolean> isComplete(String paymentId) {
        return paymentRespository.findById(paymentId).map(payment -> {
            if (payment.getCompletionDate() != null) {
                return Instant.now().isAfter(payment.getCompletionDate());
            }
            return false;
        });
    }

    public void completePayment(String paymentId) {
        mongoUtil.set(paymentId, "completionDate", Instant.now(), Payment.class);
    }

    public void updateAmount(String paymentId, double amount) {
        mongoUtil.set(paymentId, "amount", amount, Payment.class);
    }

    public void updateLink(String paymentId, String link) {
        mongoUtil.set(paymentId, "link", link, Payment.class);
    }
}
