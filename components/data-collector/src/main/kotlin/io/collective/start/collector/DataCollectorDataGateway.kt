package io.collective.start.collector

import io.collective.data.dao.Location
import io.collective.data.dao.Locations
import io.collective.data.dao.Weather
import io.collective.database.DatabaseTemplate
import io.collective.data.objects.*
import io.collective.database.DatabaseConfiguration
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

class DataCollectorDataGateway(private val dbTemplate: DatabaseTemplate) {

    fun findAllLocationsNew() : List<LocationDataObject>{
        val names = mutableListOf<LocationDataObject>()
        transaction {
            val locations = Location.all()
            locations.forEach {
                location -> names.add(LocationDataObject(
                    id = location.id.value,
                    name = location.name,
                    country = location.country,
                    timezone = location.timezone
                ))
            }
        }
        return names
    }

    fun saveWeather(weatherData: WeatherDataObject) {
        transaction {
            val weather = Weather.new {
                time_updated_epoch = weatherData.time_updated_epoch
                time_updated = weatherData.time_updated
                condition_text = weatherData.condition_text
                condition_icon = weatherData.condition_icon
                condition_code = weatherData.condition_code
                temp_c = weatherData.temp_c
                wind_kph = weatherData.wind_kph
                feelslike_c = weatherData.feelslike_c
                vis_km = weatherData.vis_km
                location_id = EntityID(weatherData.location_id, Locations)
            }
        }
    }


//    fun findLocationByName(name: String): LocationDataObject? = dbTemplate.queryOne<LocationDataObject>(
//        //language=SQL
//        "select * from locations where name = ?",
//        name
//    ) { it.getObject("confirmation_code", LocationDataObject::class.java) }

    fun findLocationByName(name: String): LocationDataObject? = dbTemplate.queryOne<LocationDataObject>(
        //language=SQL
        "select * from locations where name = ?",
        name
    ) { getLocationDataObject(it) }

    fun findAllLocationNames(): List<String>? = dbTemplate.query<String>(
        //language=SQL
        "select * from locations"
    ) { getLocationNames(it) }

    fun findAllLocations(): List<LocationDataObject>? = dbTemplate.query<LocationDataObject>(
        //language=SQL
        "select * from locations"
    ) { getLocations(it) }

    fun getLocations(rows: ResultSet): List<LocationDataObject> {
        val names = mutableListOf<LocationDataObject>()
        rows.beforeFirst()
        while (rows.next()) {
            names.add(getLocationDataObject(rows))
        }
        return names
    }

    fun getLocationNames(rows: ResultSet): List<String> {
        val names = mutableListOf<String>()
        rows.beforeFirst()
        while (rows.next()) {
            names.add(rows.getString("name"))
        }
        return names
    }

    fun getLocationDataObject(row: ResultSet): LocationDataObject =
        LocationDataObject(
            id = row.getInt("id"),
            name = row.getString("name"),
            country = row.getString("country"),
            timezone = row.getString("timezone")
        )

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


fun getTestDbCollector(): DataCollectorDataGateway {
    val testJdbcUrl = "jdbc:postgresql://localhost:5555/weather_data"
    val testDbUsername = "weather5028"
    val testDbPassword = "weather5028"

    val dbConfig = DatabaseConfiguration(testJdbcUrl, testDbUsername, testDbPassword)
    val dbTemplate = DatabaseTemplate(dbConfig.db)
    return DataCollectorDataGateway(dbTemplate)
}