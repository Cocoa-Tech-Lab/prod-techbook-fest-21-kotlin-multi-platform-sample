package com.example.bookmanager.protocol.contract.dto.book

import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

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
