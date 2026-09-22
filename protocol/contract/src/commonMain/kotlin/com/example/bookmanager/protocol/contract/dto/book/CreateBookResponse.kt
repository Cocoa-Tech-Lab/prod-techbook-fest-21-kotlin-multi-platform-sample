package com.example.bookmanager.protocol.contract.dto.book

import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import kotlinx.serialization.Serializable

/**
 * 書籍の新規登録が成功したときに返すレスポンスDTO。
 *
 * @property bookId 作成された書籍のID
 * @property name 書名
 * @property outline 概要
 * @property publishedAt 出版日（ISO: yyyy-MM-dd）
 * @property author 著者名
 * @property depositedAt 登録日時（ISO 8601 オフセット付き）
 * @property isbn ISBN コード
 */
@Serializable
data class CreateBookResponse(
    val bookId: Int,
    val name: String,
    val outline: String,
    val publishedAt: String,
    val author: String,
    val depositedAt: String,
    val isbn: String,
): ApiResponseDataField

