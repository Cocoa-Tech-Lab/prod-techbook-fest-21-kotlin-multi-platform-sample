package com.example.bookmanager.infra.di

import com.example.bookmanager.presentation.controller.AccountController
import com.example.bookmanager.presentation.controller.AdminBookController
import com.example.bookmanager.presentation.controller.BookController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * プレゼンテーション層の Controller を Koin に登録するモジュール。
 *
 * UseCase やポートを注入した状態で、単一インスタンス（singleton）として提供します。
 */
fun controllerModule() = module {
    singleOf(::BookController)
    singleOf(::AdminBookController)
    singleOf(::AccountController)
}