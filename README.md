# Kotlin ktor starter

An extension of the [Kotlin ktor starter](https://www.appcontinuum.io/) sample that fetches data from a REST API and stores it in a DB.


## Getting Started

## Running the sample

1.  Run docker-compose. This would start a postgres database in docker that can be connected to using the url localhost and port 555
    ```bash
    ./docker-compose up
    ```

1.  If you are using a separate database from that provided with docker, run the create-database.sh script to create the necessary databases.
    ```bash
    ./databases/init-scripts/create-database.sh
    ```

1.  You may need to make the script executable first
    ```bash
    chmod +x ./databases/init-scripts/create-database.sh
    ```

1.  Set the environment variables. These values are typical if using the docker database
    ```bash
    export DB_USER=weather5028
    export DB_PASS=weather5028
    export DB_URL=localhost
    export DB_PORT=5555
    ```

1.  Run the data-collector-server. This connects to the [Weather Api](http://www.weatherapi.com), fetches the current weather info for London and stores it in the database
    ```bash
    ./gradlew a:data-collector-server:run
    ```

1.  Connect to the database using pgAdmin and observe the new entry in the weather_snapshot table
    ```bash
    ./gradlew a:data-collector-server:run
    ```
