package com.example.bookmanager.infra.di

import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import javax.sql.DataSource

/**
 * Exposed の Database を初期化して DI で提供するモジュール。
 *
 * すでに他モジュールで提供されている [DataSource] を使って [Database.connect] を実行します。
 */
fun dataBaseModule() = module {
    single {
        val dataSource: DataSource = get()
        Database.connect(dataSource)
    }
}