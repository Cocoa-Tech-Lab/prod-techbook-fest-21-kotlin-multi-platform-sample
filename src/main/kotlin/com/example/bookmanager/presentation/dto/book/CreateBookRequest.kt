package com.example.bookmanager.presentation.dto.book

import kotlinx.serialization.Serializable

/**
 * 管理者が新しい書籍を登録するためのリクエストDTO。
 *
 * @property title 書名
 * @property author 著者名
 * @property outline 概要（あらすじ等）
 * @property publishedAt 出版日（ISO形式: yyyy-MM-dd）
 * @property isbn ISBNコード（ハイフン有無は任意）
 */
@Serializable
data class CreateBookRequest(
    val title: String,
    val author: String,
    val outline: String,
    val publishedAt: String,
    val isbn: String,
)
