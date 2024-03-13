package me.krob.repository;

import me.krob.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRespository extends MongoRepository<String, Payment> {
}
