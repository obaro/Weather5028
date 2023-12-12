package io.collective.data.objects;

import java.time.LocalDate;


class WeatherDataObject (
        val location_id: Int,
        val time_updated_epoch: Long,
        val time_updated: String,
        val temp_c: Float,
        val condition_text: String,
        val condition_icon: String,
        val condition_code: Int,
        val wind_kph: Float,
        val feelslike_c: Float,
        val vis_km: Float
        )
