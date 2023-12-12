package io.collective.start

import freemarker.cache.ClassTemplateLoader
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

import io.collective.start.collector.getTestDbCollector

fun Application.module() {
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

            val dbCollector = getTestDbCollector()
            val location = dbCollector.findLocationByName(city)

            call.respond(FreeMarkerContent("viewcity.ftl", mapOf("headers" to headers(),
                "city" to city, "location" to location)))
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
    embeddedServer(Netty, port, watchPaths = listOf("basic-server"), module = { module() }).start()
}
