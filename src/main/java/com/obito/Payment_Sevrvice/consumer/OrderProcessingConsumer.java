package com.obito.Payment_Sevrvice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obito.Payment_Sevrvice.dto.Order;
import com.obito.Payment_Sevrvice.dto.UserEntity;
import com.obito.Payment_Sevrvice.entity.Payment;
import com.obito.Payment_Sevrvice.repository.PaymentRepository;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@Builder
public class OrderProcessingConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingConsumer.class);

    public static final String USER_URL = "http://USER-SERVICE/user";
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    RestTemplate restTemplate;

    //@KafkaListener(topics = "ORDER_PAYMENT_TOPIC1")
    public String processOrder(String orderJsonString) throws JsonProcessingException {
        log.info("Received order for processing: {}", orderJsonString);
        //order from kafka
        System.out.println("inside payment service");
        Order order=new ObjectMapper().readValue(orderJsonString, Order.class);

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