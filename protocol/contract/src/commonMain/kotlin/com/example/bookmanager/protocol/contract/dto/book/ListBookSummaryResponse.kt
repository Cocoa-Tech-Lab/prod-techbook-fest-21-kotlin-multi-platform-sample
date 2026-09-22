package com.example.bookmanager.protocol.contract.dto.book

import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import kotlinx.serialization.Serializable

/**
 * 書籍一覧向けのサマリ行を表すDTO。
 *
 * @property bookId 書籍ID
 * @property name 書名
 * @property publishedAt 出版日（ISO: yyyy-MM-dd）
 * @property canRent 現在借りられるか
 */
@Serializable
data class BookSummary(
    val bookId: Int,
    val name: String,
    val publishedAt: String,
    val canRent: Boolean,
): ApiResponseDataField

/**
 * 書籍一覧のレスポンスDTO。
 * @property books 一覧に表示する行データ
 */
@Serializable
data class ListBookSummaryResponse(
    val books: List<BookSummary>
): ApiResponseDataField
