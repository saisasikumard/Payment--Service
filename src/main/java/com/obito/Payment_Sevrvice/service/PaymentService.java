package com.obito.Payment_Sevrvice.service;

import com.obito.Payment_Sevrvice.entity.Payment;
import com.obito.Payment_Sevrvice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    public Payment getById(String paymentId){
        return paymentRepository.findByOrderId(paymentId);
    }
}
