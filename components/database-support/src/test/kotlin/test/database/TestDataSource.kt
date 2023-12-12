package test.database

import io.collective.database.createDatasource
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource
import io.mockk.every
import io.mockk.mockk
import java.sql.Connection
import java.sql.DatabaseMetaData

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

fun hikariDataSource(): HikariDataSource {
    val connection: Connection = mockk()
    val databaseMetadata: DatabaseMetaData = mockk()
    val hikariDataSource: HikariDataSource = mockk()
    hikariDataSource
    every { hikariDataSource.connection }.returns(connection)
    every { hikariDataSource.connectionTestQuery }.returns("yeet")
    every { hikariDataSource.metricRegistry }.returns(Any())
    every { hikariDataSource.maximumPoolSize }.returns(2)
    every { hikariDataSource.minimumIdle }.returns(2)
    every { connection.metaData }.returns(databaseMetadata)
    return hikariDataSource
}
