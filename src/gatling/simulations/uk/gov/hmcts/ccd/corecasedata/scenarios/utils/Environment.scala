package uk.gov.hmcts.ccd.corecasedata.scenarios.utils

import uk.gov.hmcts.ccd.corecasedata.scenarios._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object Environment {
  
 //val idamURL = "https://idam-test.dev.ccidam.reform.hmcts.net"
  //val ccdEnvurl = "https://www-ccd.sprod.platform.hmcts.net"
  //val baseURL = "https://gateway-ccd.sprod.platform.hmcts.net"
  
  val idamURL = "https://idam-web-public.perftest.platform.hmcts.net"
  val ccdEnvurl = "https://ccd-case-management-web-perftest.service.core-compute-perftest.internal"
  val baseURL = "https://ccd-api-gateway-web-perftest.service.core-compute-perftest.internal"


 // val baseURL = "https://gateway.ccd.demo.platform.hmcts.net"
  //val idamURL = "https://idam.preprod.ccidam.reform.hmcts.net"
  //val ccdEnvurl = "https://www.ccd.demo.platform.hmcts.net" 
  val minThinkTime = 5
  val maxThinkTime = 12
  val constantthinkTime = 7
  val minWaitForNextIteration = 1
  val maxWaitForNextIteration = 2
  val HttpProtocol = http

  val commonHeader = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> ccdEnvurl)

  val docCommonHeader = Map(
    "Content-Type" -> "application/pdf",
    "Origin" -> ccdEnvurl)

  val idam_header = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Cache-Control" -> "max-age=0",
    "Connection" -> "keep-alive",
    //"Origin" -> idamURL,
    "Upgrade-Insecure-Requests" -> "1")
}