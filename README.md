![](https://media.giphy.com/media/UFGj6EYw5JhMQ/giphy.gif)
# API Demo
Code sample for Narrative.io

REST API that has two functions:

`POST /v1/api/analytics?timestamp={millis_since_epoch}&user={user_id}&event={click|impression}`

which creates a `VisitorActivity` record that records the time stamp, user id, and event type.

`GET /v1/api/analytics?timestamp={millis_since_epoch}`

which gets all user statistics from the hour of given timestamp.

## Running Instructions:

1. Install `sbt`. Info can be found here if needed: https://www.scala-sbt.org/1.0/docs/Setup.html
2. Clone this repo.
3. Start `sbt` in this repo.
4. Run `jetty:start`

To run the spec test:

1. Start `sbt` in the repo.
2. run `test`

## Solution Overview
My solution is a simple REST app that is written with Scala and Lift. The API code can [be found here](https://github.com/riveramj/api-code-sample/blob/master/src/test/scala/com/demo/snippet/VisitorAnalyticsApi.scala) and the database model [can be found here](https://github.com/riveramj/api-code-sample/blob/master/src/main/scala/com/demo/model/VisitorActivity.scala).
