package com.kchandrakant.learning;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.opencensus.exporter.stats.prometheus.PrometheusStatsCollector;
import io.prometheus.client.exporter.HTTPServer;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException {
		
	    // Create and register the Prometheus exporter
	    PrometheusStatsCollector.createAndRegister();

	    // Run the server as a daemon on address "localhost:8889"
	    @SuppressWarnings("unused")
		HTTPServer server = new HTTPServer("localhost", 8889, true);
		
		SpringApplication.run(Application.class, args);
	}

}
