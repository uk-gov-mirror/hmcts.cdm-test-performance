package uk.gov.hmcts.ccd.corecasedata.scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils._

object EthosSearchView {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val CCDEnvurl = Environment.ccdEnvurl
  val CommonHeader = Environment.commonHeader
  val idam_header = Environment.idam_header
  val feedUserData = csv("EthosUserData.csv").circular
  val feedEthosSearchData = csv("EthosSearchData.csv").random
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val caseActivityRepeat = 3

  val headers_0 = Map(
    "Access-Control-Request-Headers" -> "content-type",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "no-cors")

  val headers_1 = Map(
		"Origin" -> CCDEnvurl,
    "Accept" -> "application/json",
		"Pragma" -> "no-cache",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site")

  val headers_2 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors")

  val headers_6 = Map(
    "Access-Control-Request-Headers" -> "content-type,experimental",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "no-cors")

  val headers_7 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_8 = Map(
    "Access-Control-Request-Headers" -> "content-type,experimental",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site")

  val headers_19 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "none",
    "Upgrade-Insecure-Requests" -> "1")

  val submitLogin = group("ET_Login") {

    exec(http("ET_020_005_Login")
      .post(IdamURL + "/login?response_type=code&client_id=ccd_gateway&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .disableFollowRedirect
      .headers(idam_header)
      .formParam("username", "${EthosUserName}")
      .formParam("password", "${EthosUserPassword}")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "false")
      .formParam("_csrf", "${csrf}")
      .check(headerRegex("Location", "(?<=code=)(.*)&client").saveAs("authCode"))
      .check(status.in(200, 302)))
      //.exitHereIfFailed

      .exec(http("ET_020_010_Login")
        .get(CCDEnvurl + "/config")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_015_Login")
        .options(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_020_Login")
        .get(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_025_Login")
        .get(CCDEnvurl + "/config")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_030_Login")
        .options(BaseURL + "/data/caseworkers/:uid/profile"))
      //.exitHereIfFailed

      .exec(http("ET_020_035_Login")
        .get(BaseURL + "/data/caseworkers/:uid/profile")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_040_Login")
        .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types?access=read")
        .headers(CommonHeader))

      .exec(http("ET_020_045_Login")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types?access=read")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_050_Login")
        .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/work-basket-inputs"))
      //.exitHereIfFailed

      .exec(http("ET_020_055_Login")
        .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/cases?view=WORKBASKET&state=TODO&page=1"))
      //.exitHereIfFailed

      .exec(http("ET_020_060_Login")
        .options(BaseURL + "/data/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/cases/pagination_metadata?state=TODO"))
      //.exitHereIfFailed

      .exec(http("ET_020_065_Login")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/work-basket-inputs")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_070_Login")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/cases?view=WORKBASKET&state=TODO&page=1")
        .headers(CommonHeader))
      //.exitHereIfFailed

      .exec(http("ET_020_075_Login")
        .get(BaseURL + "/data/caseworkers/:uid/jurisdictions/${EthosJurisdiction}/case-types/${EthosCaseType}/cases/pagination_metadata?state=TODO")
        .headers(CommonHeader))
    //.exitHereIfFailed
  }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  val Search =

//    exec(http("ET_030_005_SearchCases")
//      .options("/aggregated/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/${EthosCaseType}/cases?view=WORKBASKET&page=1")
//      .headers(headers_0))

    // .exec(http("ET_030_010_SearchCases")
    //   .options("/data/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/${EthosCaseType}/cases/pagination_metadata")
    //   .headers(headers_0))

    exec(http("ET_030_005_SearchCases")
      .get("/data/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/${EthosCaseType}/cases/pagination_metadata?case.receiptDate=2019-09-26")
      .headers(headers_2))

    .exec(http("ET_030_010_SearchCases")
      .get("/aggregated/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/${EthosCaseType}/cases?view=WORKBASKET&page=1&case.receiptDate=2019-09-26")
      .headers(headers_2)
      .check(jsonPath("$..case_id").findAll.optional.saveAs("caseNumbers"))
    )

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  val OpenCase =

    exec {
      session =>
        println(session("caseNumbers").as[String])
        session
    }

    .foreach("${caseNumbers}","caseNumber") {
      exec(http("ET_040_OpenCase")
        .get("/data/internal/cases/${caseNumber}")
        .headers(headers_7))

        .repeat(caseActivityRepeat) {
          exec(http("Ethos_CaseActivity")
            .get("/activity/cases/${caseNumber}/activity")
            .headers(headers_2))

            .pause(2)
        }
    }
}