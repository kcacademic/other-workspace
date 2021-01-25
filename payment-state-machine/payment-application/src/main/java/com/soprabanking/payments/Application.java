package com.soprabanking.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.soprabanking.payments")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
