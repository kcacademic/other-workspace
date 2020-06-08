@Payments
Feature: Process Payments with Paypal as Payment Provider
  @Paypal
  Scenario: Client makes a call to create a new payment
    Given a create payment endpoint for the client to call
    When the client calls /payments/paypal with valid details
    Then the client receives a valid authentication URL