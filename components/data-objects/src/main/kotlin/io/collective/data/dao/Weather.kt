package io.collective.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Weather(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Weather>(WeatherSnapshots)
    var location_id         by WeatherSnapshots.location_id
    var time_updated_epoch  by WeatherSnapshots.time_updated_epoch
    var time_updated        by WeatherSnapshots.time_updated
    var temp_c              by WeatherSnapshots.temp_c
    var condition_text      by WeatherSnapshots.condition_text
    var condition_icon      by WeatherSnapshots.condition_icon
    var condition_code      by WeatherSnapshots.condition_code
    var wind_kph            by WeatherSnapshots.wind_kph
    var feelslike_c         by WeatherSnapshots.feelslike_c
    var vis_km              by WeatherSnapshots.vis_km
}