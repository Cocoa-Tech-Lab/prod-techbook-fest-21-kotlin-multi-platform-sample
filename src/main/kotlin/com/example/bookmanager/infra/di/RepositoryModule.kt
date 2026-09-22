package com.example.bookmanager.infra.di

import com.example.bookmanager.domain.port.AccountRepository
import com.example.bookmanager.domain.port.BookRepository
import com.example.bookmanager.domain.port.RentalRepository
import com.example.bookmanager.infra.database.repository.AccountRepositoryImpl
import com.example.bookmanager.infra.database.repository.BookRepositoryImpl
import com.example.bookmanager.infra.database.repository.RentalRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * リポジトリ実装をドメインのポートにバインドする Koin モジュール。
 *
 * インフラ層の実装（Exposed/JDBC）をアプリケーション層のインターフェースに差し込みます。
 */
fun repositoryModule() = module {
    singleOf(::BookRepositoryImpl) bind BookRepository::class
    singleOf(::AccountRepositoryImpl) bind AccountRepository::class
    singleOf(::RentalRepositoryImpl) bind RentalRepository::class
}
