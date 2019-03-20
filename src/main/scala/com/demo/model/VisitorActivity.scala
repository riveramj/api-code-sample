package com.demo.model

import net.liftweb._
  import mapper._
  import common._
  import util._

import com.demo.util.RandomIdGenerator._

import java.util.Date

class VisitorActivity extends LongKeyedMapper[VisitorActivity] with IdPK with OneToMany[Long, VisitorActivity] {
  def getSingleton = VisitorActivity
  object visitorActivityId extends MappedLong(this) {
    override def dbIndexed_? = true
    override def defaultValue = generateLongId
  }
  object userId extends MappedString(this, 300)
  object event extends MappedEnum(this, EventType)
  object timeStamp extends MappedDateTime(this)
  object createdAt extends MappedDateTime(this) {
    override def defaultValue = new Date()
  }

  def createVisitorActivity(userId: String, eventType: EventType.Value, timeStamp: Date) = {
    VisitorActivity.create
      .userId(userId)
      .event(eventType)
      .timeStamp(timeStamp)
      .saveMe
  }
}

object VisitorActivity extends VisitorActivity with LongKeyedMetaMapper[VisitorActivity]

object EventType extends Enumeration {
  val Click, Impression = Value
}
