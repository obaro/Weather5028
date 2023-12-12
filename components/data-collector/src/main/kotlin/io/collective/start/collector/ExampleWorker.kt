package io.collective.start.collector

import io.collective.data.getSystemEnv
import io.collective.data.objects.LocationDataObject
import io.collective.data.objects.WeatherDataObject
import io.collective.database.getDbCollector
import io.collective.workflow.Worker
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ExampleWorker(override val name: String = "data-collector") : Worker<ExampleTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val dbUser = getSystemEnv("DB_USER")
        ?: throw RuntimeException("Please set the DB_USER environment variable")
    private val dbPassword = getSystemEnv("DB_PASS")
        ?: throw RuntimeException("Please set the DB_PASS environment variable")
    private val dbUrl = getSystemEnv("DB_URL")
        ?: throw RuntimeException("Please set the DB_URL environment variable")
    private val dbPort = getSystemEnv("DB_PORT")
        ?: throw RuntimeException("Please set the DB_PORT environment variable")
    private val dbCollector = getDbCollector(dbUser, dbPassword, dbUrl, dbPort)
    private val apiKey = getSystemEnv("WEATHER_API_KEY")
    private val weatherAPIUrl = "http://api.weatherapi.com/v1/current.json?aqi=no&key=$apiKey"


    override fun execute(task: ExampleTask) {
        runBlocking {
            logger.info("starting data collection.")

            // todo - data collection happens here
            getAllWeatherData()

            logger.info("completed data collection.")
        }
    }

    fun getAllWeatherData() {
        val locations = dbCollector.findAllLocationsNew()

        locations.forEach {
                location -> getWeatherData(location)
        }
    }

    private fun getWeatherData(location: LocationDataObject) {
        logger.info("getting weather info for " + location.name)

        try {
            val url = URL(weatherAPIUrl + "&q=" + URLEncoder.encode(location.name, StandardCharsets.UTF_8))
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    val jsonObject = JSONObject(response)

                    val currentObject = jsonObject.getJSONObject("current")
                    val conditionObject = currentObject.getJSONObject("condition")

                    val weatherData = WeatherDataObject(
                        time_updated_epoch = currentObject.getLong("last_updated_epoch"),
                        time_updated = currentObject.getString("last_updated"),
                        condition_text = conditionObject.getString("text"),
                        condition_icon = conditionObject.getString("icon"),
                        condition_code = conditionObject.getInt("code"),
                        temp_c = currentObject.getFloat("temp_c"),
                        wind_kph = currentObject.getFloat("wind_kph"),
                        feelslike_c = currentObject.getFloat("feelslike_c"),
                        vis_km = currentObject.getFloat("vis_km"),
                        location_id = location.id
                    )

                    dbCollector.saveWeather(weatherData)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}