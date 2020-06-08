package com.coc.payments.bdd;

import com.coc.payments.vo.PaymentRequest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class StepDefinitions {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;
    private String SERVICE_ENDPOINT = "http://localhost:8080/payments/paypal";

    @Given("^a create payment endpoint for the client to call$")
    public void a_create_payment_endpoint() throws Throwable {
        request = RestAssured.given()
            .contentType("application/json");
    }

    @When("^the client calls /payments/paypal with valid details$")
    public void the_client_calls_create_payment() throws Throwable {
        response = request.with()
            .body(new PaymentRequest())
            .when()
            .post(SERVICE_ENDPOINT);
    }

    @Then("^the client receives a valid authentication URL$")
    public void the_client_receives_auth_url() throws Throwable {
        json = response.then()
            .statusCode(200);
        System.out.println(json.toString());
    }
}