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
  object clicks extends MappedInt(this)
  object impressions extends MappedString(this, 100)
  object createdAt extends MappedDateTime(this) {
    override def defaultValue = new Date()
  }
}

object VisitorActivity extends VisitorActivity with LongKeyedMetaMapper[VisitorActivity]
