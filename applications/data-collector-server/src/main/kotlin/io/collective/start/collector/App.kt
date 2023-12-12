package io.collective.start.collector

import io.collective.workflow.WorkScheduler
import io.collective.data.objects.*
import io.collective.database.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.*
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject


fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("hi!", ContentType.Text.Html)
        }
    }
    val scheduler = WorkScheduler<ExampleTask>(ExampleWorkFinder(), mutableListOf(ExampleWorker()), 30)
    scheduler.start()
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8888
    embeddedServer(Netty, port, watchPaths = listOf("data-collector-server"), module = Application::module).start()

//    getWeatherData()
}

private fun getWeatherData() {
    val dbUser = System.getenv("DB_USER")
        ?: throw RuntimeException("Please set the DB_USER environment variable")
    val dbPassword = System.getenv("DB_PASS")
        ?: throw RuntimeException("Please set the DB_PASS environment variable")
    val dbUrl = System.getenv("DB_URL")
        ?: throw RuntimeException("Please set the DB_URL environment variable")
    val dbPort = System.getenv("DB_PORT")
        ?: throw RuntimeException("Please set the DB_PORT environment variable")

    val testJdbcUrl = "jdbc:postgresql://" + dbUrl + ":" + dbPort + "/weather_data"

    val dbConfig = DatabaseConfiguration(testJdbcUrl, dbUser, dbPassword)
    val dbTemplate = DatabaseTemplate(dbConfig.db)
    val dbCollector = DataCollectorDataGateway(dbTemplate)


    try {
        val url = URL("http://api.weatherapi.com/v1/current.json?key=51c3c1ab748d4bc8918134433231112&q=London&aqi=no")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // Use GET method
            inputStream.bufferedReader().use {
                val response = it.readText()
                val jsonObject = JSONObject(response)

                val currentObject = jsonObject.getJSONObject("current")
                val conditionObject = currentObject.getJSONObject("condition")
                val weatherData = WeatherDataObject(
                    time_updated_epoch = currentObject.getInt("last_updated_epoch"),
                    time_updated = currentObject.getString("last_updated"),
                    condition_text = conditionObject.getString("text"),
                    condition_icon = conditionObject.getString("icon"),
                    condition_code = conditionObject.getInt("code"),
                    temp_c = currentObject.getFloat("temp_c"),
                    wind_kph = currentObject.getFloat("wind_kph"),
                    feelslike_c = currentObject.getFloat("feelslike_c"),
                    vis_km = currentObject.getFloat("vis_km"),
                    location_id = 1
                )

                try {
                    dbCollector.saveWeatherData(1, weatherData)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}