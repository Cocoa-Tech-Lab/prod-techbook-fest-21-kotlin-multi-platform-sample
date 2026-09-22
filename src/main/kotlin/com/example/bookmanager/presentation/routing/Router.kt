package com.example.bookmanager.presentation.routing

import io.ktor.server.application.*

/**
 * ルーティング定義をモジュール化するためのインターフェース。
 *
 * 実装ごとにエンドポイント群（Account, Book など）を分割し、
 * Application 起動時にまとめて登録します。
 */
interface Router {
    /**
     * この Router が管理するルートを Ktor Application に登録します。
     */
    fun installRouting(app: Application)
}