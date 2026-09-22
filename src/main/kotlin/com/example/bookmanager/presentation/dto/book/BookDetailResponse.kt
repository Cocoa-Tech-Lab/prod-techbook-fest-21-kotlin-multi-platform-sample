package com.example.bookmanager.presentation.dto.book

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

/**
 * 書籍詳細画面向けのレスポンスDTO。
 *
 * 一覧では表示しない詳細情報（概要、在庫可否など）を含みます。
 * @property bookId 書籍ID
 * @property name 書名
 * @property outline 概要
 * @property publishedAt 出版日（ISO: yyyy-MM-dd）
 * @property author 著者名
 * @property depositedAt 登録日時（ISO 8601 オフセット付き）
 * @property isbn ISBN コード
 * @property canRent 現在借りられるか
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class BookDetailResponse(
    val bookId: Int,
    val name: String,
    val outline: String,
    val publishedAt: String,
    val author: String,
    val depositedAt: String,
    val isbn: String,
    val canRent: Boolean,
): ApiResponseDataField
