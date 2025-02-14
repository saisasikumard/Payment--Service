package com.obito.Payment_Sevrvice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentSevrviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentSevrviceApplication.class, args);
	}

}
