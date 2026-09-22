package com.example.bookmanager.infra.di

import com.example.bookmanager.application.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * アプリケーション層ユースケースを Koin に登録するモジュール。
 *
 * Repository やトランザクション管理などの依存を注入し、
 * 各ユースケースをシングルトンとして提供します。
 */
fun useCaseModule() = module {
    singleOf(::ListBookUseCase)
    singleOf(::GetBookDetailUseCase)
    singleOf(::CreateBookUseCase)
    singleOf(::UpdateBookUseCase)
    singleOf(::DeleteBookUseCase)
    singleOf(::RegisterAccountUseCase)
    singleOf(::SignInAccountUseCase)
    singleOf(::RentBookUseCase)
    singleOf(::ReturnBookUseCase)
    singleOf(::ListRentalHistoryUseCase)
}