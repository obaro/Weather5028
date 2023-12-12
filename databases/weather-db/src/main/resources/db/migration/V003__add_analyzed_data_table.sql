create table analyzedweathersnapshots
(
    id                  serial primary key,
    location_id         int,
    prev_time_epoch     bigint,
    prev_time_string    varchar,
    current_time_epoch  bigint,
    current_time_string varchar,
    prev_temp_c         Numeric,
    current_temp_c      Numeric,
    prev_wind_kph       Numeric,
    current_wind_kph    Numeric,
    prev_vis_km         Numeric,
    current_vis_km      Numeric,
    CONSTRAINT ws_locations
      FOREIGN KEY (location_id)
       REFERENCES locations (id)
);

alter table weathersnapshots
  alter column time_updated_epoch TYPE bigint USING time_updated_epoch::bigint
