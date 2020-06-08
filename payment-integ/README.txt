CONFIG SERVER
=============
cd apps\e2e-application\cloud\config-server
mvn spring-boot:run

KAFKA
=====
Apps\kafka_2.11-2.1.0\bin\windows\zookeeper-server-start.bat Apps\kafka_2.11-2.1.0\config\zookeeper.properties
Apps\kafka_2.11-2.1.0\bin\windows\kafka-server-start.bat Apps\kafka_2.11-2.1.0\config\server.properties
Apps\kafka_2.11-2.1.0\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic coc_payments

CASSANDRA
=========
cassandra
cqlsh -u cassandra -p cassandra -k coc_payments

OAUTH MOCK
==========
cd Apps\wiremock-standalone-2.25.1
java -jar wiremock-standalone-2.25.1.jar --port 9999

PAYMENT SERVICE
===============
cd C:\Users\kumchand0\Apps\sts-workspace\Other Projects\payment-integ
mvn clean compile
mvn clean test jacoco:report
mvn spring-boot:run
mvn package
mvn install dockerfile:build
docker run --rm --name=payment-integ -p 8080:8080 coc/payment-integ:latest
docker stop payment-integ
docker-compose up
docker-compose down

TEST SERVICE
============
curl -X POST \
  http://localhost:11008/payments/paypal \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Authorization: Bearer 9e97aff7-d211-42ae-b59a-1f9defa869fc' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 461' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 092e22a5-ee6d-4e49-85ad-6a36ec2596de,4c233a99-5463-4623-82e7-cfa524b024cb' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' \
  -d '{
	"idempotencyKey":"123451",
	"userId":"user",
	"orderId": "12345",
	"paymentProvider":"paypal",
	"paymentMethod":"paypal",
	"intent":"sale",
	"description":"This is a dummy transaction.",
	"amount": {
		"subTotal":"5",
		"shipping":"1",
		"tax":"1",
		"total":"7",
		"currency":"USD"
	},
	"address": {
		"name":"Kumar Chandrakant",
		"line1":"Sapient",
		"line2":"Boulder Av.",
		"city":"NY",
		"postCode":"11011",
		"countryCode":"US",
		"state":"NY",
		"phone":"1111111111"
	}
}'

curl -X POST \
  http://localhost:11008/payments/cybersource \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Authorization: Bearer 9e97aff7-d211-42ae-b59a-1f9defa869fc' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 670' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:11008' \
  -H 'Postman-Token: d5fc38ce-f635-473b-afe3-fdb3b4be9533,2c637f56-8738-49ae-ae37-775e685dadd9' \
  -H 'User-Agent: PostmanRuntime/7.19.0' \
  -H 'cache-control: no-cache' \
  -d '{
	"idempotencyKey":"1324524",
	"orderId": "12345",
	"userId":"user",
	"paymentProvider":"paypal",
	"paymentMethod":"paypal",
	"intent":"sale",
	"description":"This is a dummy transaction.",
	"amount": {
		"subTotal":"5",
		"shipping":"1",
		"tax":"1",
		"total":"7",
		"currency":"USD"
	},
	"address": {
		"name":"Kumar Chandrakant",
		"line1":"Sapient",
		"line2":"Boulder Av.",
		"city":"NY",
		"postCode":"11011",
		"countryCode":"US",
		"state":"NY",
		"phone":"1111111111",
		"email": "kchandrakant@sapient.com"
	},
	"card": {
		"number": "4111111111111111",
		"expirationYear": "2030",
		"expirationMonth": "12",
		"securityCode": "123",
		"saveCard": "true"
	}
}'