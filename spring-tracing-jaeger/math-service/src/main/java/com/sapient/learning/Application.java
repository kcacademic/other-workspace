package com.sapient.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.learning.client.AdditionService;
import com.sapient.learning.client.MultiplicationService;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	JaegerTracer tracer;

	@GetMapping("/calculate/{number}")
	public int calculate(@PathVariable int number) {

		Span additionSpan = tracer.buildSpan("add").start();
		int x = performAddition(number);
		additionSpan.finish();

		Span multiplicationSpan = tracer.buildSpan("multiply").start();
		int y = performMultiplication(x);
		multiplicationSpan.finish();

		return y;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private int performAddition(int number) {

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:8081/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient.Builder().build())
				.build();

		AdditionService service = retrofit.create(AdditionService.class);
		Call<Integer> callSync = service.add(number);

		Integer x = null;
		try {
			Response<Integer> response = callSync.execute();
			x = response.body();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return x;
	}

	private int performMultiplication(int number) {

		MultiplicationService service = Feign.builder()
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.logger(new Slf4jLogger(MultiplicationService.class))
				.logLevel(Logger.Level.FULL)
				.target(MultiplicationService.class, "http://localhost:8082/");

		Integer x = service.multiply(number);

		return x;
	}

}
