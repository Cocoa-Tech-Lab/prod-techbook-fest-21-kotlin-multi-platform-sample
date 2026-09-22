package com.example.bookmanager.infra.config

import io.ktor.server.plugins.di.annotations.*

/**
 * データソース（JDBC）に関する設定値。
 *
 * Ktor の DI (ktor-server-di) により application.yaml から自動でバインドされます。
 * これらの値は [com.zaxxer.hikari.HikariDataSource] の初期化に使用されます。
 *
 * @property host DBホスト名
 * @property port ポート番号
 * @property dbName データベース名
 * @property user 接続ユーザ
 * @property password 接続パスワード
 * @property databaseDriver JDBCドライバのFQCN（例: org.postgresql.Driver）
 */

data class DataSourceConfig(
    @Property("database.host")
    val host: String,
    @Property("database.port")
    val port: Int,
    @Property("database.dbName")
    val dbName: String,
    @Property("database.user")
    val user: String,
    @Property("database.password")
    val password: String,
    @Property("database.dbDriver")
    val databaseDriver: String,
)