package com.demo.snippet

import org.scalatest._
  import concurrent._

import net.liftweb._
  import util._
  import Helpers._
  import mapper._
  import common._
  import http._
  import mockweb._
    import MockWeb._
  import mocks._
  import json._
    import Extraction._
    import JsonDSL._

import com.demo.model._

import java.util.Date

class VisitorAnalyticsApiSpec extends FunSpec with MustMatchers with BeforeAndAfterAll {

  override def beforeAll() {
    bootstrap.liftweb.SetupDb.setup

    VisitorActivity.createVisitorActivity(
      userId="some user id",
      timeStamp=new Date(1553105213150L),
      eventType=EventType.Click
    )
  }

  override def afterAll() {
    VisitorActivity.findAll().map(_.delete_!)
  }

  private def runApiRequest(url: String, requestModifier: (MockHttpServletRequest)=>Any = (req)=>false)(responseHandler: (Box[LiftResponse])=>Any) = {
    val mockReq = new MockHttpServletRequest("http://localhost:8080" + url)
      requestModifier(mockReq)

      testS(mockReq) {
        testReq(mockReq) {req =>
          responseHandler(VisitorAnalyticsApi(req)())
        }
      }
  }

  describe("GET /api/v1/analytics") {
    it("should return visitor statistics info") {
      runApiRequest("/api/v1/analytics", _.parameters = List("timestamp" -> "1553105213150")) { response =>
        response match {
          case Full(JsonResponse(json, _, _, code)) =>
            code must equal (200)

            implicit val formats = DefaultFormats
            val jvalue = parse(json.toJsCmd)
            val uniqueVisitors = (jvalue \ "unique_visitors").extract[Int]
            val clicks = (jvalue \ "clicks").extract[Int]
            val impressions = (jvalue \ "impressions").extract[Int]
            
            uniqueVisitors must equal (1)
            clicks must equal (1)
            impressions must equal (0)

          case somethingUnexpected => fail(somethingUnexpected.toString)
        }
      }
    }
  }
}
