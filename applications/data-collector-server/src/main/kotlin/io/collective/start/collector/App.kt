package io.collective.start.collector

import io.collective.database.getDbCollector
import io.collective.rabbitsupport.*
import io.collective.workflow.WorkScheduler
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.net.URI
import java.util.*

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("hi!", ContentType.Text.Html)
        }
    }

    val dbUser = System.getenv("DB_USER")
        ?: throw RuntimeException("Please set the DB_USER environment variable")
    val dbPassword = System.getenv("DB_PASS")
        ?: throw RuntimeException("Please set the DB_PASS environment variable")
    val dbUrl = System.getenv("DB_URL")
        ?: throw RuntimeException("Please set the DB_URL environment variable")
    val dbPort = System.getenv("DB_PORT")
        ?: throw RuntimeException("Please set the DB_PORT environment variable")
    val dbCollector = getDbCollector(dbUser, dbPassword, dbUrl, dbPort)
    val apiKey = System.getenv("WEATHER_API_KEY")


    val rabbitString = System.getenv("RABBIT_URL")
        ?: throw RuntimeException("Please set the RABBIT_URL environment variable")
    val connectionFactory = buildConnectionFactory(rabbitString.let(::URI)
        ?: throw RuntimeException("$rabbitString is NOT a valid URI"))

    val registrationNotificationExchange = RabbitExchange(
        name = "collection-exchange",
        type = "direct",
        routingKeyGenerator = { _: String -> "42" },
    )
    val registrationNotificationQueue = RabbitQueue("collection-notification")
    connectionFactory.declareAndBind(exchange = registrationNotificationExchange, queue = registrationNotificationQueue, routingKey = "42")
    val publishFunction = publish(connectionFactory, registrationNotificationExchange)
    val collectorPublisher = CollectorMessagePublisher(publishFunction)

    val scheduler = WorkScheduler<ExampleTask>(ExampleWorkFinder(),
        mutableListOf(ExampleWorker(collectorPublisher, dbCollector, apiKey)), 60)
    scheduler.start()
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8889
    embeddedServer(Netty, port, watchPaths = listOf("data-collector-server"),
        module = {
            module()
        }).start()
}