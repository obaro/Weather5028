package io.collective.start.collector

import io.collective.database.DatabaseTemplate
import io.collective.data.objects.*
import java.util.*

class DataCollectorDataGateway(private val dbTemplate: DatabaseTemplate) {
    fun findLocationByName(name: String): LocationDataObject? = dbTemplate.queryOne<LocationDataObject>(
        //language=SQL
        "select * from locations where name = ?",
        name
    ) { it.getObject("confirmation_code", LocationDataObject::class.java) }

    fun saveWeatherData(locationId: Int, weatherData: WeatherDataObject) = dbTemplate.execute(
        //language=SQL
        "insert into weather_snapshot (location_id, time_updated_epoch, time_updated, temp_c, " +
                "condition_text, condition_icon, condition_code, wind_kph, feelslike_c, vis_km) values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        weatherData.location_id, weatherData.time_updated_epoch, weatherData.time_updated,
        weatherData.temp_c, weatherData.condition_text, weatherData.condition_icon, weatherData.condition_code,
        weatherData.wind_kph, weatherData.feelslike_c, weatherData.vis_km
    )
}
