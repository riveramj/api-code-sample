import sbt._
import Keys._

name := "API Code Sample"

version := "0.1-SNAPSHOT"

organization := "com.demo"

scalaVersion := "2.12.8"

enablePlugins(JettyPlugin)

resolvers ++= Seq(
  "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= {
  val liftVersion = "3.3.0"
  Seq(
    "ch.qos.logback"      %  "logback-classic"    % "1.2.3",
    "org.apache.shiro"    %  "shiro-core"         % "1.2.4",
    "com.h2database"      %  "h2"                  % "1.4.199",
    "org.eclipse.jetty"   %  "jetty-webapp"       % "9.4.8.v20171121" % "container; compile->default",
    "org.scalatest"       %% "scalatest"          % "3.0.5"       % "test",
    "net.liftweb"         %% "lift-common"        % liftVersion % "compile",
    "net.liftweb"         %% "lift-webkit"        % liftVersion % "compile",
    "net.liftweb"         %% "lift-testkit"       % liftVersion % "test",
    "net.liftweb"         %% "lift-mapper"        % liftVersion % "compile"
  )
}
