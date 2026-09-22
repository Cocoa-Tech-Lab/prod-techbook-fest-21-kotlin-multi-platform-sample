package com.example.bookmanager.presentation.dto.book

import kotlinx.serialization.Serializable

/**
 * 管理者が既存の書籍情報を更新するためのリクエストDTO。
 *
 * @property title 書名
 * @property author 著者名
 * @property outline 概要（あらすじ等）
 * @property publishedAt 出版日（ISO形式: yyyy-MM-dd）
 */
@Serializable
data class UpdateBookRequest(
    val title: String,
    val author: String,
    val outline: String,
    val publishedAt: String,
)
