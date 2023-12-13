package io.collective.start

import freemarker.cache.ClassTemplateLoader
import io.collective.data.getSystemEnv
import io.collective.database.getDbCollector
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import java.util.*

fun Application.module(
    dbUser: String,
    dbPassword: String,
    dbUrl: String,
    dbPort: String) {
    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Routing) {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to headers())))
        }

        post("/viewcity") {
            val parameters = call.receiveParameters()
            val city = parameters["city"]?: "London"

            val dbCollector = getDbCollector(dbUser, dbPassword, dbUrl, dbPort)
            val location = dbCollector.findLocationByName(city)

            if(location != null) {
                val analyzedWeather = dbCollector.getLatestAnalysisForLocation(location.id)
                if(analyzedWeather != null) {
                    val range = (analyzedWeather.current_time_epoch - analyzedWeather.prev_time_epoch)/60
                    call.respond(FreeMarkerContent("viewcity.ftl", mapOf("headers" to headers(),
                        "city" to city, "location" to location, "analyzed" to analyzedWeather,
                        "range" to range)))
                }
                else {
                    call.respond(FreeMarkerContent("viewcity.ftl", mapOf("headers" to headers(),
                        "city" to city, "location" to location)))
                }
            }
            else {
                call.respond(
                    FreeMarkerContent(
                        "viewcity.ftl", mapOf(
                            "headers" to headers(),
                            "city" to city
                        )
                    )
                )
            }
        }

        post("/postmvp") {
            val parameters = call.receiveParameters()
            val userInput = parameters["userinput"]?: "This is the input"


            call.respond(FreeMarkerContent("postmvp.ftl", mapOf("headers" to headers(), "userInput" to userInput)))
        }

        static("images") { resources("images") }
        static("style") { resources("style") }
        static("js") { resources("js") }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.headers(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    call.request.headers.entries().forEach { entry ->
        headers[entry.key] = entry.value.joinToString()
    }
    return headers
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = getSystemEnv("PORT")?.toInt() ?: 8888
    val dbUser = getSystemEnv("DB_USER")
        ?: throw RuntimeException("Please set the DB_USER environment variable")
    val dbPassword = getSystemEnv("DB_PASS")
        ?: throw RuntimeException("Please set the DB_PASS environment variable")
    val dbUrl = getSystemEnv("DB_URL")
        ?: throw RuntimeException("Please set the DB_URL environment variable")
    val dbPort = getSystemEnv("DB_PORT")
        ?: throw RuntimeException("Please set the DB_PORT environment variable")
    embeddedServer(Netty, port, watchPaths = listOf("basic-server"),
        module = { module(
            dbUser, dbPassword, dbUrl, dbPort
        ) }).start()
}
