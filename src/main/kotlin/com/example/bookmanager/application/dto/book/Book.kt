package com.example.bookmanager.application.dto.book

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

/**
 * クライアントへ返却する書籍情報のDTO。
 *
 * ドメインモデルからプレゼンテーション向けに射影したシンプルなデータ構造です。
 * 画面表示やAPIレスポンスの仕様に沿って必要な情報のみを持ちます。
 *
 * @property id 書籍ID
 * @property name 書名
 * @property author 著者名
 * @property outline 概要（あらすじ等）
 * @property isbn ISBN コード（ハイフン有無は実装依存）
 * @property publishedAt 出版日
 * @property depositedAt 受け入れ（登録）日時
 */
data class Book(
    val id: Int,
    val name: String,
    val author: String,
    val outline: String,
    val isbn: String,
    val publishedAt: LocalDate,
    val depositedAt: Instant,
)