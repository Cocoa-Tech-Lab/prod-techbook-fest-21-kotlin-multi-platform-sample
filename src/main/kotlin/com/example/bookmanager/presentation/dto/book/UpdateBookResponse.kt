package com.example.bookmanager.presentation.dto.book

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookResponse(
    val bookId: Int,
    val name: String,
    val outline: String,
    val publishedAt: String,
    val author: String,
    val depositedAt: String,
    val isbn: String,
): ApiResponseDataField
