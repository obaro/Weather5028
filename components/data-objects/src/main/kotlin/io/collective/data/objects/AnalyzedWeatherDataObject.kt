package io.collective.data.objects

class AnalyzedWeatherDataObject (
    val id: Int,
    val location_id: Int,
    val prev_time_epoch: Long,
    val prev_time_string: String,
    val current_time_epoch: Long,
    val current_time_string: String,
    val prev_temp_c: Float,
    val current_temp_c: Float,
    val prev_wind_kph: Float,
    val current_wind_kph: Float,
    val prev_vis_km: Float,
    val current_vis_km: Float
)