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

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  SetupDb.setup

  def boot {
    Schemifier.schemify(
      true,
      Schemifier.infoF _,
      VisitorActivity
    )


    // where to search snippet
    LiftRules.addToPackages("com.demo")
    
    LiftRules.setSiteMap(Paths.siteMap)

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))    

    // Make a transaction span the whole HTTP request -> uncomment for DB usage
    S.addAround(DB.buildLoanWrapper)

    // set DocType to HTML5
    LiftRules.htmlProperties.default.set((r: Req) =>new Html5Properties(r.userAgent))
  }

  LiftRules.statelessDispatch.append(VisitorAnalyticsApi)
}
