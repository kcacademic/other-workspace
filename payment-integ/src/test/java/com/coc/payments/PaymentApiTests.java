package com.coc.payments;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.with;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coc.payments.vo.PaymentRequest;
import com.github.tomakehurst.wiremock.WireMockServer;

import io.restassured.RestAssured;

public class PaymentApiTests {

    private static final int PORT = 8084;
    private static WireMockServer wireMockServer = new WireMockServer(PORT);

    @BeforeClass
    public static void before() throws Exception {
        wireMockServer.start();
        configureFor("localhost", PORT);
        RestAssured.port = PORT;
        stubFor(post(urlEqualTo("/payments/paypal")).withRequestBody(containing("{\"userId\":null}"))
            .willReturn(aResponse().withStatus(200)));
    }

    @Test
    public void givenCreatePaymentApi_whenRequestPost_thenOK() {
        with().body(new PaymentRequest())
            .when()
            .request("POST", "/payments/paypal")
            .then()
            .statusCode(200);
    }

}
