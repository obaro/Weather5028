package io.collective.start.analyzer

import io.collective.data.getSystemEnv
import io.collective.data.objects.AnalyzedWeatherDataObject
import io.collective.data.objects.LocationDataObject
import io.collective.database.DataCollectorDataGateway
import io.collective.database.getDbCollector
import io.collective.workflow.Worker
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.time.Instant

class AnalyzeLocation(private val dbCollector: DataCollectorDataGateway) {
    private val logger = LoggerFactory.getLogger(AnalyzeLocation::class.java)

    fun execute(location: String) {
        runBlocking {
            logger.info("starting data analysis for $location.")

            // todo - data analysis happens here
            analyzeLocation(location)

            logger.info("completed data analysis.")
        }
    }

    private fun analyzeLocation(locationId: String) {
        val location = dbCollector.findLocationById(locationId.toInt()) ?: return

        val fromTime = Instant.now().epochSecond - 6200

        logger.info("Processing Data for " + location.name + " with epoch ==> " + fromTime)

        val weatherDataList = dbCollector.findWeatherInRange(location.id, fromTime)
        val prevData = weatherDataList.first()
        val currentData = weatherDataList.last()

        val analyzedObject = AnalyzedWeatherDataObject(
            id = 0,
            location_id = prevData.location_id,
            prev_time_epoch = prevData.time_updated_epoch,
            prev_time_string = prevData.time_updated,
            current_time_epoch = currentData.time_updated_epoch,
            current_time_string = currentData.time_updated,
            prev_temp_c = prevData.temp_c,
            current_temp_c = currentData.temp_c,
            prev_wind_kph = prevData.wind_kph,
            current_wind_kph = currentData.wind_kph,
            prev_vis_km = prevData.vis_km,
            current_vis_km = currentData.vis_km
        )

        dbCollector.saveAnalyzedWeather(analyzedObject)
    }
}