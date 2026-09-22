package com.example.bookmanager.protocol.contract.dto.book

import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
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
