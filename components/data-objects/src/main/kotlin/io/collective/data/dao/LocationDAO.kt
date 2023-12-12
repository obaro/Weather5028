package io.collective.data.dao

import org.jetbrains.exposed.dao.id.IntIdTable

object Locations : IntIdTable() {
        val name = varchar("name", 50)
        val country = varchar("country", 50)
        val timezone = varchar("timezone", 50)
}

object WeatherSnapshots : IntIdTable() {
        val location_id = reference("location_id", Locations)
        val time_updated_epoch = long("time_updated_epoch")
        val time_updated = varchar("time_updated", 50)
        val temp_c = float("temp_c")
        val condition_text = varchar("condition_text", 50)
        val condition_icon = varchar("condition_icon", 50)
        val condition_code = integer("condition_code")
        val wind_kph = float("wind_kph")
        val feelslike_c = float("feelslike_c")
        val vis_km = float("vis_km")
}

object AnalyzedWeatherSnapshots: IntIdTable() {
        val location_id = reference("location_id", Locations)
        val prev_time_epoch = long("prev_time_epoch")
        val prev_time_string = varchar("prev_time_string", 50)
        val current_time_epoch = long("current_time_epoch")
        val current_time_string = varchar("current_time_string", 50)
        val prev_temp_c = float("prev_temp_c")
        val current_temp_c = float("current_temp_c")
        val prev_wind_kph = float("prev_wind_kph")
        val current_wind_kph = float("current_wind_kph")
        val prev_vis_km = float("prev_vis_km")
        val current_vis_km = float("current_vis_km")
}