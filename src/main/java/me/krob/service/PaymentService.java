package me.krob.service;

import me.krob.repository.PaymentRespository;
import me.krob.util.MongoTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private MongoTemplateUtil mongoUtil;

    @Autowired
    private PaymentRespository paymentRespository;
}
