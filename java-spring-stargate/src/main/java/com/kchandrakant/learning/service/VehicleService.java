package com.kchandrakant.learning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleService {

    @Value("${stargate.auth-token}")
    private String authToken;

    @Value("${stargate.base-url}")
    private String baseUrl;

    public String getVehicleDetails(String registration) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "?" + "page-size=3&where={whereClause}";
        String whereClause = "{\"registration\":{\"$eq\":\"" + registration + "\"}}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Cassandra-Token", authToken);
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, whereClause);

        return response.getBody();
    }
}
