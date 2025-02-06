package com.obito.Payment_Sevrvice.controller;

import com.obito.Payment_Sevrvice.entity.Payment;
import com.obito.Payment_Sevrvice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;
    public Payment getByorderId(String orderId){
        return paymentRepository.findByOrderId(orderId);
    }
}
