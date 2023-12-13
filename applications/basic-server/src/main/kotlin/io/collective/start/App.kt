package io.collective.start

import freemarker.cache.ClassTemplateLoader
import io.collective.data.getSystemEnv
import io.collective.database.DataCollectorDataGateway
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
    dbCollector: DataCollectorDataGateway) {
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
    val port = System.getenv("PORT")?.toInt() ?: 8888
    val dbUser = System.getenv("DB_USER")
        ?: throw RuntimeException("Please set the DB_USER environment variable")
    val dbPassword = System.getenv("DB_PASS")
        ?: throw RuntimeException("Please set the DB_PASS environment variable")
    val dbUrl = System.getenv("DB_URL")
        ?: throw RuntimeException("Please set the DB_URL environment variable")
    val dbPort = System.getenv("DB_PORT")
        ?: throw RuntimeException("Please set the DB_PORT environment variable")
    val dbCollector = getDbCollector(dbUser, dbPassword, dbUrl, dbPort)
    embeddedServer(Netty, port, watchPaths = listOf("basic-server"),
        module = { module(
            dbCollector
        ) }).start()
}
