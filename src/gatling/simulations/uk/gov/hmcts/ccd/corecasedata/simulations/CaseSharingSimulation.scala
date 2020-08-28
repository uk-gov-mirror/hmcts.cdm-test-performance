package uk.gov.hmcts.ccd.corecasedata.simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils._

import scala.concurrent.duration._

class CaseSharingSimulation extends Simulation  {

  val BaseURL = Environment.baseURL
  val config: Config = ConfigFactory.load()

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080)) //Comment out for VM runs
    .doNotTrackHeader("1")

  val csIterationLarge = 100
  val csIterationSmall = 500

  val CaseSharingLarge = scenario("CCDCSLarge")
    .repeat(1) {
      repeat(1) {
        exec(casesharing.CDSGetRequestLarge) //this step only required once per user to generate the token for case creation
        .exec(casesharing.CaseShareLoginLarge) //this step only required once per user to generate the token for case sharing
      }
        .repeat(csIterationLarge) {
            exec(casesharing.CreateCase)
            .exec(casesharing.CaseSharingPostLarge)
            .exec(WaitforNextIteration.waitforNextIteration)
        }
    }

  val CaseSharingSmall = scenario("CCDCSSmall")
    .repeat(1) {
      repeat(1) {
        exec(casesharing.CDSGetRequestSmall) //this step only required once per user to generate the token for case creation
        .exec(casesharing.CaseShareLoginSmall) //this step only required once per user to generate the token for case sharing
      }
        .repeat(csIterationSmall) {
          exec(casesharing.CreateCase)
          .exec(casesharing.CaseSharingPostSmall)
          .exec(WaitforNextIteration.waitforNextIteration)
        }
    }

  setUp(
    CaseSharingLarge.inject(rampUsers(400) during(30 minutes)),
    CaseSharingSmall.inject(rampUsers(10) during(5 minutes))
  )
    .protocols(httpProtocol)
  //.maxDuration(60 minutes)
}