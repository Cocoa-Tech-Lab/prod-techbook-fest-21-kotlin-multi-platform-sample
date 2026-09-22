package com.example.bookmanager.infra.di

import com.example.bookmanager.presentation.routing.AccountRouter
import com.example.bookmanager.presentation.routing.BookRouter
import com.example.bookmanager.presentation.routing.Router
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * ルータ（エンドポイント群）を Koin に登録するモジュール。
 *
 * Router は [Router] インターフェースで束ね、起動時に Application へ一括登録します。
 */
fun routerModule() = module {
    singleOf(::AccountRouter) bind Router::class
    singleOf(::BookRouter) bind Router::class
}