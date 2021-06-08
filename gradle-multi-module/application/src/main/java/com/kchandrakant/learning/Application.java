package com.kchandrakant.learning;

import com.kchandrakant.learning.service.MyKotlinService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.sapient.learning")
@RestController
public class Application {

	private final MyKotlinService myKotlinService;

	public Application(MyKotlinService myKotlinService) {
		this.myKotlinService = myKotlinService;
	}

	@GetMapping("/")
	public String home() {
		return myKotlinService.message();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
