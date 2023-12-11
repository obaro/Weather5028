package io.collective.start.collector

import io.collective.database.createDatasource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseConfiguration(
    private val dbUrl: String,
    private val dbUser: String,
    private val dbPassword: String) {
//        val config = HikariConfig().apply {
//            jdbcUrl = dbUrl
//            username = dbUser
//            password = dbPassword
//        }
//        val ds = HikariDataSource(config)

        private val ds = createDatasource(dbUrl, dbUser, dbPassword)

        val db by lazy {
            Database.connect(ds)
        }
    }