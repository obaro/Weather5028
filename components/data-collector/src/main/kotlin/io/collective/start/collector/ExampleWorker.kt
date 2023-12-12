package io.collective.start.collector

import io.collective.data.objects.LocationDataObject
import io.collective.data.objects.WeatherDataObject
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
    private val dbCollector = getTestDbCollector()
    private val API_KEY = "51c3c1ab748d4bc8918134433231112"
    private val weatherAPIUrl = "http://api.weatherapi.com/v1/current.json?aqi=no&key=" + API_KEY


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
//        val locations = Location.all()

        if (locations != null) {
            locations.forEach {
                    location -> getWeatherData(location)
            }
        }
    }

    private fun getWeatherData(location: LocationDataObject) {
        logger.info("getting weather info for " + location.name)

        try {
            val url = URL(weatherAPIUrl + "&q=" + URLEncoder.encode(location.name, StandardCharsets.UTF_8))
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