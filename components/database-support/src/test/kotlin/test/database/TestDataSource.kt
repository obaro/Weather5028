package test.database

import io.collective.database.createDatasource
import javax.sql.DataSource

const val testJdbcUrl = "jdbc:postgresql://localhost:5555/weather_data"
const val testDbUsername = "weather5028"
const val testDbPassword = "weather5028"

fun testDataSource(): DataSource {
    return createDatasource(
        jdbcUrl = testJdbcUrl,
        username = testDbUsername,
        password = testDbPassword
    )
}
