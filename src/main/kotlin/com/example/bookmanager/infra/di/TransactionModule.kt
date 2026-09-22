package com.example.bookmanager.infra.di

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.infra.transaction.ExposedTransactionManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * トランザクション境界を提供するモジュール。
 *
 * Exposed を用いた実装 [ExposedTransactionManager] をアプリケーションのポート
 * [TransactionManager] にバインドして提供します。
 */
fun transactionModule() = module {
    singleOf(::ExposedTransactionManager) bind TransactionManager::class
}