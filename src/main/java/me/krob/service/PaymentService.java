package me.krob.service;

import me.krob.model.Payment;
import me.krob.repository.ClientRepository;
import me.krob.repository.PaymentRespository;
import me.krob.repository.TrainerRepository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return paymentRespository.save(payment);
    }

    public Optional<Payment> getById(String paymentId) {
        return paymentRespository.findById(paymentId);
    }
}
