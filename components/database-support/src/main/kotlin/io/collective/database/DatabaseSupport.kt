package io.collective.database

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import javax.sql.DataSource

fun createDatasource(
    jdbcUrl: String, username: String, password: String,
): DataSource {
    val datasource = HikariDataSource()

    datasource.apply {
        maximumPoolSize = 2
        setJdbcUrl(jdbcUrl)
        setUsername(username)
        setPassword(password)
    }

    val config = ClassicConfiguration()
    config.dataSource = datasource
    val flyway = Flyway(config)
    flyway.migrate()

    return datasource
}