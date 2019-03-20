package bootstrap.liftweb

import net.liftweb._
  import util._
  import Helpers._
  import mapper._
  import common._
  import http._

import com.demo.model._
import com.demo.util.Paths
import com.demo.snippet.VisitorAnalyticsApi

object SetupDb {
  def setup = {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:./lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(util.DefaultConnectionIdentifier, vendor)
    }
  }
}
