package com.obito.Payment_Sevrvice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obito.Payment_Sevrvice.dto.Order;
import com.obito.Payment_Sevrvice.dto.UserEntity;
import com.obito.Payment_Sevrvice.entity.Payment;
import com.obito.Payment_Sevrvice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static com.obito.Payment_Sevrvice.consumer.OrderProcessingConsumer.USER_URL;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    RestTemplate restTemplate;
    public Payment getById(String paymentId){
        return paymentRepository.findByOrderId(paymentId);
    }

    public String process(Order order) throws JsonProcessingException {

        //order from kafka
        System.out.println("inside payment service");


        //build payment Request
//        Payment payment=Payment.builder()
//                .amount(order.getPrice())
//                .orderId(order.getOrderId())
//                .userId(order.getUserId())
//                .paymentStatus(order.getPaymentMode())
//                .paidDate(new Date())
//                .build();
        Payment payment=new Payment();
        payment.setPaymode(order.getPaymentMode());
        payment.setAmount(order.getPrice());
        payment.setPaidDate(new Date());
        payment.setUserId(order.getUserId());
        payment.setOrderId(order.getOrderId());



        if(payment.getPaymode().equals("COD")){
            payment.setPaymentStatus("PENDING");
        }
        //check with available balance in user db for amount
        else{
            UserEntity user=restTemplate.getForObject("http://USER-SERVICE/user/get?id="+payment.getUserId(), UserEntity.class);
            if(user.getAmount()< payment.getAmount()){
                throw new RuntimeException("Unsufficient Balance");
            }
            else {
                payment.setPaymentStatus("PAID");

                restTemplate.put(USER_URL + "/update/" + payment.getUserId() + "/" + payment.getAmount(), null);

            }
        }
        paymentRepository.save(payment);
        //git branch test

        return "Success";
    }
}
