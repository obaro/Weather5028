package io.collective.database

import io.collective.data.dao.*
import io.collective.data.objects.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

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

    fun findLocationByName(city: String) : LocationDataObject? {
        var locationObject: LocationDataObject? = null
        transaction {
            val location = Location.find { Locations.name eq city }.firstOrNull()
            if (location != null) {
                locationObject = LocationDataObject(
                    id = location.id.value,
                    name = location.name,
                    country = location.country,
                    timezone = location.timezone
                )
            }
        }
        return locationObject
    }

    fun saveWeather(weatherData: WeatherDataObject) {
        transaction {
            val existing = Weather.find{
                (WeatherSnapshots.time_updated_epoch eq weatherData.time_updated_epoch) and
                (WeatherSnapshots.location_id eq weatherData.location_id)
            }.firstOrNull()
            if(existing != null)
                return@transaction
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

//    fun findWeatherInRange(city: Int, rangeInHours: Int) : List<WeatherDataObject> {
//        val minimumTime = Instant.now().epochSecond - (rangeInHours * 3600)
//        val weatherList = mutableListOf<WeatherDataObject>()
//        transaction {
//            val weatherVals = Weather.find {
//                WeatherSnapshots.location_id eq city
//                WeatherSnapshots.time_updated_epoch greater  minimumTime
//            }
//            weatherList.add(mapWeatherToDataObject(weatherVals.first()))
//            weatherList.add(mapWeatherToDataObject(weatherVals.last()))
//        }
//        return weatherList
//    }

    fun findWeatherInRange(city: Int, fromTime: Long) : List<WeatherDataObject> {
        val weatherList = mutableListOf<WeatherDataObject>()
        transaction {
            val weatherVals = Weather.find {
                (WeatherSnapshots.location_id eq city) and
                        (WeatherSnapshots.time_updated_epoch greater fromTime)
            }
            weatherList.add(mapWeatherToDataObject(weatherVals.first()))
            weatherList.add(mapWeatherToDataObject(weatherVals.last()))
        }
        return weatherList
    }

    fun getLatestAnalysisForLocation(id: Int) : AnalyzedWeatherDataObject? {
        var analyzedObject: AnalyzedWeatherDataObject? = null
        transaction {
            val analyzedWeather = AnalyzedWeather.find {
                AnalyzedWeatherSnapshots.location_id eq id
            }.sortedByDescending { it.id }.firstOrNull()
            analyzedObject = analyzedWeather?.let { mapAnalyzedWeatherToDataObject(it) }
        }
        return analyzedObject
    }

    fun saveAnalyzedWeather(analyzedObject: AnalyzedWeatherDataObject) {
        transaction {
            val existing = AnalyzedWeather.find{
                (AnalyzedWeatherSnapshots.prev_time_epoch eq analyzedObject.prev_time_epoch) and
                        (AnalyzedWeatherSnapshots.current_time_epoch eq analyzedObject.current_time_epoch)
            }.firstOrNull()
            if(existing != null)
                return@transaction
            val analyzedWeather = AnalyzedWeather.new {
                location_id = EntityID(analyzedObject.location_id, Locations)
                prev_time_epoch = analyzedObject.prev_time_epoch
                prev_time_string = analyzedObject.prev_time_string
                current_time_epoch = analyzedObject.current_time_epoch
                current_time_string = analyzedObject.current_time_string
                prev_temp_c = analyzedObject.prev_temp_c
                current_temp_c = analyzedObject.current_temp_c
                prev_wind_kph = analyzedObject.prev_wind_kph
                current_wind_kph = analyzedObject.current_wind_kph
                prev_vis_km = analyzedObject.prev_vis_km
                current_vis_km = analyzedObject.current_vis_km
            }
        }
    }

    private fun mapWeatherToDataObject(weather: Weather) : WeatherDataObject{
        return WeatherDataObject(
            time_updated_epoch = weather.time_updated_epoch,
            time_updated = weather.time_updated,
            condition_text = weather.condition_text,
            condition_icon = weather.condition_icon,
            condition_code = weather.condition_code,
            temp_c = weather.temp_c,
            wind_kph = weather.wind_kph,
            feelslike_c = weather.feelslike_c,
            vis_km = weather.vis_km,
            location_id = weather.location_id.value)
    }

    private fun mapAnalyzedWeatherToDataObject(analyzedWeather: AnalyzedWeather) : AnalyzedWeatherDataObject {
        return AnalyzedWeatherDataObject(
            id = analyzedWeather.id.value,
            location_id = analyzedWeather.location_id.value,
            prev_time_epoch = analyzedWeather.prev_time_epoch,
            prev_time_string = analyzedWeather.prev_time_string,
            current_time_epoch = analyzedWeather.current_time_epoch,
            current_time_string = analyzedWeather.current_time_string,
            prev_temp_c = analyzedWeather.prev_temp_c,
            current_temp_c = analyzedWeather.current_temp_c,
            prev_wind_kph = analyzedWeather.prev_wind_kph,
            current_wind_kph = analyzedWeather.current_wind_kph,
            prev_vis_km = analyzedWeather.prev_vis_km,
            current_vis_km = analyzedWeather.current_vis_km
        )
    }


//    fun findLocationByName(name: String): LocationDataObject? = dbTemplate.queryOne<LocationDataObject>(
//        //language=SQL
//        "select * from locations where name = ?",
//        name
//    ) { it.getObject("confirmation_code", LocationDataObject::class.java) }

//    fun findLocationByName(name: String): LocationDataObject? = dbTemplate.queryOne<LocationDataObject>(
//        //language=SQL
//        "select * from locations where name = ?",
//        name
//    ) { getLocationDataObject(it) }
//
//    fun findAllLocationNames(): List<String>? = dbTemplate.query<String>(
//        //language=SQL
//        "select * from locations"
//    ) { getLocationNames(it) }
//
//    fun findAllLocations(): List<LocationDataObject>? = dbTemplate.query<LocationDataObject>(
//        //language=SQL
//        "select * from locations"
//    ) { getLocations(it) }
//
//    fun getLocations(rows: ResultSet): List<LocationDataObject> {
//        val names = mutableListOf<LocationDataObject>()
//        rows.beforeFirst()
//        while (rows.next()) {
//            names.add(getLocationDataObject(rows))
//        }
//        return names
//    }
//
//    fun getLocationNames(rows: ResultSet): List<String> {
//        val names = mutableListOf<String>()
//        rows.beforeFirst()
//        while (rows.next()) {
//            names.add(rows.getString("name"))
//        }
//        return names
//    }
//
//    fun getLocationDataObject(row: ResultSet): LocationDataObject =
//        LocationDataObject(
//            id = row.getInt("id"),
//            name = row.getString("name"),
//            country = row.getString("country"),
//            timezone = row.getString("timezone")
//        )
//
//    fun saveWeatherData(locationId: Int, weatherData: WeatherDataObject) = dbTemplate.execute(
//        //language=SQL
//        "insert into weather_snapshot (location_id, time_updated_epoch, time_updated, temp_c, " +
//                "condition_text, condition_icon, condition_code, wind_kph, feelslike_c, vis_km) values " +
//                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//        weatherData.location_id, weatherData.time_updated_epoch, weatherData.time_updated,
//        weatherData.temp_c, weatherData.condition_text, weatherData.condition_icon, weatherData.condition_code,
//        weatherData.wind_kph, weatherData.feelslike_c, weatherData.vis_km
//    )
}

fun getDbCollector(
    dbUser: String,
    dbPassword: String,
    dbUrl: String,
    dbPort: String) : DataCollectorDataGateway {
    val testJdbcUrl = "jdbc:postgresql://$dbUrl:$dbPort/weather_data"

    val dbConfig = DatabaseConfiguration(testJdbcUrl, dbUser, dbPassword)
    val dbTemplate = DatabaseTemplate(dbConfig.db)
    return DataCollectorDataGateway(dbTemplate)
}

//fun getTestDbCollector(): DataCollectorDataGateway {
//    val testJdbcUrl = "jdbc:postgresql://localhost:5555/weather_data"
//    val testDbUsername = "weather5028"
//    val testDbPassword = "weather5028"
//
//    val dbConfig = DatabaseConfiguration(testJdbcUrl, testDbUsername, testDbPassword)
//    val dbTemplate = DatabaseTemplate(dbConfig.db)
//    return DataCollectorDataGateway(dbTemplate)
//}