package com.obito.Payment_Sevrvice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.obito.Payment_Sevrvice.dto.Order;
import com.obito.Payment_Sevrvice.entity.Payment;
import com.obito.Payment_Sevrvice.repository.PaymentRepository;
import com.obito.Payment_Sevrvice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;
    @GetMapping("/get/{id}")
    public Payment getByorderId(@PathVariable("id") String orderId){
        return paymentRepository.findByOrderId(orderId);
    }
    @PostMapping("/process")
    public String process(@RequestBody Order order) throws JsonProcessingException {
        return paymentService.process(order);
    }
}

