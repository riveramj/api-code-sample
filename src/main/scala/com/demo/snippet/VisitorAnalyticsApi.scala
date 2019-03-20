package com.demo.snippet

import net.liftweb._
  import common._
  import mapper._
  import http._
    import rest._
  import util._
    import Helpers._
  import json._
    import JsonDSL._

import com.demo.model.{VisitorActivity, EventType}

import java.util.Date

object VisitorAnalyticsApi extends RestHelper with Loggable {
  val events = List("click", "impression")
  val hourInMilliseconds = 3600000

  def checkValidEvent(posisbleEvent: String) = {
    events.contains(posisbleEvent.trim.toLowerCase)
  }

  def checkTimeStamp(possibleTimeStamp: String) = {
    val possibleNumber = tryo(possibleTimeStamp.toLong)

    possibleNumber match {
      case Full(_) => true
      case _ => false
    }
  }

  serve {
    case req @ Req("api" :: "v1" :: "analytics" :: Nil, _, PostRequest) => {
      for {
        rawTimeStamp <- req.param("timestamp").filter(checkTimeStamp(_)) ?~! "`timestamp` should be a number." ~> 400
        timeStamp <- tryo(rawTimeStamp.toLong)
        userId <- req.param("user").filter(_.trim.nonEmpty) ?~! "`user` was not specified." ~> 400
        event <- req.param("event").filter(checkValidEvent(_)) ?~! "`event` must be `click` or `impresion`." ~> 400
      } yield {
        val eventType = {
          if (event == "click")
            EventType.Click
          else
            EventType.Impression
        }

        VisitorActivity.createVisitorActivity(
          userId = userId,
          eventType = eventType,
          timeStamp = new Date(timeStamp)
        )
      }

      PlainTextResponse("", Nil, 204)
    }

    case req @ Req("api" :: "v1" :: "analytics" :: Nil, _, GetRequest) => {
      for {
        rawTimeStamp <- req.param("timestamp").filter(checkTimeStamp(_)) ?~! "`timestamp` should be a number." ~> 400
        timeStamp <- tryo(rawTimeStamp.toLong)
      } yield {
        val startHourBounds = timeStamp - (timeStamp % hourInMilliseconds) - 1
        val endHourBounds = timeStamp - (timeStamp % hourInMilliseconds) + hourInMilliseconds

        val allHourAcitivity = VisitorActivity.findAll(
          By_>(VisitorActivity.timeStamp, new Date(startHourBounds)),
          By_<(VisitorActivity.timeStamp, new Date(endHourBounds))
        )

        val uniqueVisitors = allHourAcitivity.map(_.userId.get).distinct.size
        val visitorEvents = allHourAcitivity.map(_.event.get)
        val clickCount = visitorEvents.filter(_ == EventType.Click).size
        val impressionCount = visitorEvents.size - clickCount

        val hourStatistics = ("unique_visitors" -> uniqueVisitors) ~ ("clicks" -> clickCount) ~ ("impressions" -> impressionCount)

        JsonResponse(
          hourStatistics,
          Nil,
          Nil,
          200
        )
      }
    }
  }
}
