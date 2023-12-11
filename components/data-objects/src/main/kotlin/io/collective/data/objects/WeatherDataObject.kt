package io.collective.data.objects;

import java.time.LocalDate;


class WeatherDataObject (
        val location_id: Int,
        val time_updated_epoch: Int,
        val time_updated: String,
        val temp_c: Float,
        val condition_text: String,
        val condition_icon: String,
        val condition_code: Int,
        val wind_kph: Float,
        val feelslike_c: Float,
        val vis_km: Float
        )

//class WeatherDataObject {
//    private int location_id;
//    private LocalDate time_updated_epoch;
//    private LocalDate time_updated;
//    private float temp_c;
//    private String condition_text;
//    private String condition_icon;
//    private int condition_code;
//    private float wind_kph;
//    private float feelslike_c;
//    private float vis_km;
//
//    public WeatherDataObject(
//            int location_id,
//            LocalDate time_updated_epoch,
//            LocalDate time_updated,
//            float temp_c,
//            String condition_text,
//            String condition_icon,
//            int condition_code,
//            float wind_kph,
//            float feelslike_c,
//            float vis_km
//    ) {
//        this.condition_code = condition_code;
//        this.condition_text = condition_text;
//        this.condition_icon = condition_icon;
//        this.feelslike_c = feelslike_c;
//        this.temp_c = temp_c;
//        this.location_id = location_id;
//        this.time_updated = time_updated;
//        this.time_updated_epoch = time_updated_epoch;
//        this.vis_km = vis_km;
//        this.wind_kph = wind_kph;
//    }
//
//    public LocalDate getTime_updated() {
//        return time_updated;
//    }
//
//    public LocalDate getTime_updated_epoch() {
//        return time_updated_epoch;
//    }
//
//    public float getFeelslike_c() {
//        return feelslike_c;
//    }
//
//    public float getTemp_c() {
//        return temp_c;
//    }
//
//    public float getVis_km() {
//        return vis_km;
//    }
//
//    public float getWind_kph() {
//        return wind_kph;
//    }
//
//    public int getCondition_code() {
//        return condition_code;
//    }
//
//    public int getLocation_id() {
//        return location_id;
//    }
//
//    public String getCondition_icon() {
//        return condition_icon;
//    }
//
//    public String getCondition_text() {
//        return condition_text;
//    }
//}
//
