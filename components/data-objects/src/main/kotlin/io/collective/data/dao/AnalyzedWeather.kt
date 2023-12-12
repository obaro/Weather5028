package io.collective.data.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AnalyzedWeather(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AnalyzedWeather>(AnalyzedWeatherSnapshots)
    var location_id         by AnalyzedWeatherSnapshots.location_id
    var prev_time_epoch     by AnalyzedWeatherSnapshots.prev_time_epoch
    var prev_time_string    by AnalyzedWeatherSnapshots.prev_time_string
    var current_time_epoch  by AnalyzedWeatherSnapshots.current_time_epoch
    var current_time_string by AnalyzedWeatherSnapshots.current_time_string
    var prev_temp_c         by AnalyzedWeatherSnapshots.prev_temp_c
    var current_temp_c      by AnalyzedWeatherSnapshots.current_temp_c
    var prev_wind_kph       by AnalyzedWeatherSnapshots.prev_wind_kph
    var current_wind_kph    by AnalyzedWeatherSnapshots.current_wind_kph
    var prev_vis_km         by AnalyzedWeatherSnapshots.prev_vis_km
    var current_vis_km      by AnalyzedWeatherSnapshots.current_vis_km

    var location            by Location referencedOn AnalyzedWeatherSnapshots.location_id
}