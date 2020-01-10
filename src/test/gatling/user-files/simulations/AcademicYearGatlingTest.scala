import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the AcademicYear entity.
 */
class AcademicYearGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "X-XSRF-TOKEN" -> "${xsrf_token}"
    )

    val keycloakHeaders = Map(
        "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Upgrade-Insecure-Requests" -> "1"
    )

    val scn = scenario("Test the AcademicYear entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        .check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authentication")
        .headers(headers_http_authenticated)
        .formParam("username", "admin")
        .formParam("password", "admin")
        .formParam("remember-me", "true")
        .formParam("submit", "Login")
        .check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all academicYears")
            .get("/api/academic-years")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new academicYear")
            .post("/api/academic-years")
            .headers(headers_http_authenticated)
            .body(StringBody("""{
                "id":null
                , "description":"SAMPLE_TEXT"
                , "startDate":"2020-01-01T00:00:00.000Z"
                , "endDate":"2020-01-01T00:00:00.000Z"
                , "comments":"SAMPLE_TEXT"
                , "createdBy":"SAMPLE_TEXT"
                , "createdOn":"2020-01-01T00:00:00.000Z"
                , "updatedBy":"SAMPLE_TEXT"
                , "updatedOn":"2020-01-01T00:00:00.000Z"
                , "status":"SAMPLE_TEXT"
                }""")).asJson
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_academicYear_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created academicYear")
                .get("${new_academicYear_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created academicYear")
            .delete("${new_academicYear_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) during (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
