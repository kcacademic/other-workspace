package com.sapient.learning.application;

import com.sapient.learning.service.MyKotlinService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.sapient.learning")
@RestController
public class DemoApplication {

	private final MyKotlinService myKotlinService;

	public DemoApplication(MyKotlinService myKotlinService) {
		this.myKotlinService = myKotlinService;
	}

	@GetMapping("/")
	public String home() {
		return myKotlinService.message();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
