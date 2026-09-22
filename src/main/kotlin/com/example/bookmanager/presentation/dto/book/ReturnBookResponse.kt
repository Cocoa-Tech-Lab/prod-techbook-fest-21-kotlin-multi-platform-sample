package com.example.bookmanager.presentation.dto.book

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class ReturnBookResponse(
    val rentId: Uuid,
    val bookId: Int,
    val userId: Uuid,
    val rentalAt: String,
    val returnDeadline: String,
    val returnedAt: String?,
    val status: String,
): ApiResponseDataField
