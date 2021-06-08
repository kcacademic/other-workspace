package com.kchandrakant.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sapient.payments")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
