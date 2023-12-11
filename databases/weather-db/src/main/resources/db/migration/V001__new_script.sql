create table locations
(
    id          serial primary key,
    name        varchar not null,
    country     varchar not null,
    timezone    varchar not null
);

create table weather_snapshot
(
    location_id         int,
    time_updated_epoch  int,
    time_updated        varchar,
    temp_c              Numeric,
    condition_text      varchar,
    condition_icon      varchar,
    condition_code      int,
    wind_kph            Numeric,
    feelslike_c         Numeric,
    vis_km              Numeric,
    CONSTRAINT ws_locations
      FOREIGN KEY (location_id)
       REFERENCES locations (id)
);