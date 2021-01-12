package com.baeldung

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class LoadTest extends Simulation {

  val protocol: HttpProtocolBuilder = http.baseUrl("http://localhost:8080/")

  val scn: ScenarioBuilder = scenario("Check concurrentRequests for dummy API")
    .exec(
      http("Get dummy api")
        .get("")
        .check(status.is(200))
	)


  val duringSeconds: Integer = Integer.getInteger("duringSeconds",10)
  val constantUsers: Integer = Integer.getInteger("constantUsers",10)

  setUp(scn.inject(constantConcurrentUsers(constantUsers) during (duringSeconds)).protocols(protocol)).maxDuration(1800)
    .assertions(global.responseTime.max.lt(20000), global.successfulRequests.percent.gt(95))

}
