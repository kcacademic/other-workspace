package com.sapient.learning;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sapient.learning.client.AdditionService;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.okhttp3.TracingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	Tracer tracer;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/calculate/{number}")
	public int calculate(@PathVariable int number) {

		int x = performAddition(number);
		int y = performMultiplication(x);

		return combine(x, y);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private int performAddition(int number) {

		OkHttpClient tracingClient = TracingInterceptor.addTracing(new OkHttpClient.Builder(), tracer);
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8081/")
				.addConverterFactory(GsonConverterFactory.create()).client(tracingClient).build();

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

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		int x = restTemplate.exchange("http://localhost:8082/multiply/" + number, HttpMethod.GET, entity, Integer.class)
				.getBody();

		return x;
	}

	private int combine(int num1, int num2) {
		Span span = tracer.buildSpan("combine").start();
		int x = num1 + num2;
		span.finish();
		return x;
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
