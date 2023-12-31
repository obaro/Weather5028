package io.collective.start.analyzer

import com.rabbitmq.client.ConnectionFactory
import io.collective.database.DataCollectorDataGateway
import io.collective.database.getDbCollector
import io.collective.rabbitsupport.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*

class App

val logger = LoggerFactory.getLogger(App::class.java)

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

    val connectionFactory = buildConnectionFactory(
        System.getenv("RABBIT_URL")?.let(::URI)
        ?: throw RuntimeException("Please set the RABBIT_URL environment variable"))
    val registrationNotificationExchange = RabbitExchange(
        name = "collection-exchange",
        type = "direct",
        routingKeyGenerator = { _: String -> "42" },
    )
    val registrationNotificationQueue = RabbitQueue("collection-notification")
    connectionFactory.declareAndBind(exchange = registrationNotificationExchange, queue = registrationNotificationQueue, routingKey = "42")

//    val publishNotification = publish(connectionFactory, registrationNotificationExchange)
    runBlocking {
        listenForMessages(connectionFactory, registrationNotificationQueue, dbCollector)
    }


//    val scheduler = WorkScheduler<ExampleTask>(ExampleWorkFinder(),
//        mutableListOf(ExampleWorker()), 30)
//    scheduler.start()
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8881
    embeddedServer(Netty, port, watchPaths = listOf("data-analyzer-server"),
        module = { module()}).start()
}

suspend fun listenForMessages(
    connectionFactory: ConnectionFactory,
    registrationNotificationQueue: RabbitQueue,
    dbCollector: DataCollectorDataGateway
) {
    val channel = connectionFactory.newConnection().createChannel()

    listen(queue = registrationNotificationQueue, channel = channel) {
        val jsonObject = JSONObject(it)
        val location = jsonObject.getString("location")
        logger.info("Received weather update for $location")
        AnalyzeLocation(dbCollector).execute(location)
    }
}