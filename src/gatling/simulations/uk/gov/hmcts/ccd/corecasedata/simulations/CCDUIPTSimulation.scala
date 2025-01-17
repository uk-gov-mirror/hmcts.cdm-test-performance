package uk.gov.hmcts.ccd.corecasedata.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
//import io.gatling.http.Predef._ //comment out for VM runs, only required for proxy
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils._
import scala.concurrent.duration._

class CCDUIPTSimulation extends Simulation  {

  val BaseURL = Environment.baseURL
  val PBiteration = 7 //7
  val SSCSiteration = 12 //12
  val CMCiteration = 7 //7
  val Diviteration = 6 //6
  val Fpliteration = 10 //10
  val Ethositeration = 24 //24
  val LFUiteration = 10 //8

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080)) //Comment out for VM runs
    .doNotTrackHeader("1")

  val CCDProbateScenario = scenario("CCDPB")
    .repeat(1) {
      exec(Browse.Homepage)
      .exec(PBGoR.submitLogin)
      .repeat(PBiteration) {
        exec(PBGoR.PBCreateCase)
        .exec(PBGoR.PBPaymentSuccessful)
        .exec(PBGoR.PBDocUpload)
        .exec(PBGoR.PBStopCase)
        .exec(PBGoR.PBSearchAndView)
        .exec(WaitforNextIteration.waitforNextIteration)
      }
      .exec(Logout.ccdLogout)
  }

  val CCDSSCSScenario = scenario("CCDSSCS")
    .repeat(1) {
     exec(Browse.Homepage)
      .exec(SSCS.SSCSLogin)
      .repeat(SSCSiteration) {
        exec(SSCS.SSCSCreateCase)
        .exec(SSCS.SSCSDocUpload)
        .exec(SSCS.SSCSSearchAndView)
        .exec(WaitforNextIteration.waitforNextIteration)
      }
      .exec(Logout.ccdLogout)
    }

  val CCDCMCScenario = scenario("CCDCMC")
    .repeat(1) {
      exec(Browse.Homepage)
      .exec(CMC.CMCLogin)
      .repeat(CMCiteration) {
        exec(CMC.CMCCreateCase)
        .exec(CMC.CMCStayCase)
        .exec(CMC.CMCWaitingTransfer)
        .exec(CMC.CMCTransfer)
        .exec(CMC.CMCAttachScannedDocs)
        .exec(CMC.CMCSupportUpdate)
        .exec(CMC.CMCSearchAndView)
        .exec(WaitforNextIteration.waitforNextIteration)
      }
      .exec(Logout.ccdLogout)
  }

  val CCDDivScenario = scenario("CCDDIV")
    .repeat(1) {
      exec(Browse.Homepage)
        .exec(DVExcep.submitLogin)
        .repeat(Diviteration) {
          exec(DVExcep.DVCreateCase)
          .exec(DVExcep.DVDocUpload)
          .exec(DVExcep.DVSearchAndView)
          .exec(WaitforNextIteration.waitforNextIteration)
        }
        .exec(Logout.ccdLogout)
    }

  val CCDFPLScenario = scenario("CCDFPL")
    .repeat(1) {
      exec(Browse.Homepage)
        .exec(FPL.FPLLogin)
        .repeat(Fpliteration) {
          exec(FPL.FPLCreateCase)
          .exec(FPL.FPLDocumentUpload)
          .exec(WaitforNextIteration.waitforNextIteration)
        }
        .exec(Logout.ccdLogout)
    }

  val CCDEthosScenario = scenario("CCDEthos")
    .repeat(1) {
      exec(Browse.Homepage)
      .exec(EthosSearchView.submitLogin)
      .repeat(Ethositeration) {
        exec(EthosSearchView.Search)
        .exec(EthosSearchView.OpenCase)
        .exec(WaitforNextIteration.waitforNextIteration)
      }
      .exec(Logout.ccdLogout)
  }

  val CCDLargeFileUpload = scenario("CCDLFU")
      .repeat(1) {
        exec(Browse.Homepage)
        .exec(LargeFileUpload.LFULogin)
        .repeat(LFUiteration) {
          exec(LargeFileUpload.LFU_CreateCase)
          .exec(LargeFileUpload.LFUDocUpload)
          .exec(LargeFileUpload.LFUSearchAndView)
          .exec(WaitforNextIteration.waitforNextIteration)
          }
          .exec(Logout.ccdLogout)
      }

  val UserProfileSearch = scenario("CCDUP")
      .repeat(20) {
        exec(GetUserProfile.SearchJurisdiction)
        .exec(GetUserProfile.SearchAllUsers)
        .exec(WaitforNextIteration.waitforNextIteration)
      }

  val CaseShare = scenario("CCDCS")
    .exec(CaseSharing.CDSGetRequest)
    .exec(CaseSharing.CaseShareRequest)

  val CcdDataStore = scenario("CCDDS") 
    .exec(ccddatastore.CDSGetRequest)

  val DataStoreCreateCase = scenario("CreateCase")
      .exec(ccddatastore.CDSGetRequest)
      .exec(ccddatastore.CreateCaseForCaseSharing)


  setUp(
    //These 5 scenarios required for CCD regression testing
    CCDProbateScenario.inject(rampUsers(150) during (20 minutes)),
    CCDSSCSScenario.inject(rampUsers(150) during (20 minutes)),
    CCDEthosScenario.inject(rampUsers(400) during (20 minutes)),
    CCDCMCScenario.inject(rampUsers(150) during (20 minutes)),
    CCDDivScenario.inject(rampUsers(150) during (20 minutes))

    //UserProfileSearch.inject(rampUsers(10) during(20 minutes))

    //These scenarios left commented out and used for debugging/script testing etc
    //CCDLargeFileUpload.inject(rampUsers(15) during(15 minutes))
    //CCDSSCSScenario.inject(rampUsers(200) during(200 minutes))
    //CCDDivScenario.inject(rampUsers(1) during(1 minutes))
    //CaseShare.inject(rampUsers(1) during(30 minutes))
    //CcdDataStore.inject(rampUsers(1) during(30 minutes))
  )
    .protocols(httpProtocol)
    //.maxDuration(60 minutes)
}