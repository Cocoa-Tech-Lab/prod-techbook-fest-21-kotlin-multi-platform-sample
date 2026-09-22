package com.example.bookmanager.infra.di

import com.example.bookmanager.infra.config.DataSourceConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import javax.sql.DataSource

/**
 * HikariCP による [DataSource] を提供する Koin モジュール。
 *
 * [DataSourceConfig] の値を用いて JDBC 接続情報を組み立てます。
 */
fun dataSourceModule(
    dataSourceConfig: DataSourceConfig
) = module {
    single<DataSource> {
        HikariDataSource().apply dataSource@{
            this.jdbcUrl = "jdbc:postgresql://${dataSourceConfig.host}:${dataSourceConfig.port}/${dataSourceConfig.dbName}"
            this.username = dataSourceConfig.user
            this.password = dataSourceConfig.password
            this.driverClassName = dataSourceConfig.databaseDriver
        }
    }
}
