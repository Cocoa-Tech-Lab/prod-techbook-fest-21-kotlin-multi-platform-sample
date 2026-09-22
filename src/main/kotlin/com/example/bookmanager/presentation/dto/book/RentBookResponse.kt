package com.example.bookmanager.presentation.dto.book

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/**
 * 書籍の貸出が成功したときに返すレスポンスDTO。
 *
 * @property rentId 付与された貸出ID
 * @property bookId 貸し出した書籍ID
 * @property userId 借りたユーザのID
 * @property rentalAt 貸出日時（ISO 8601 オフセット付き）
 * @property returnDeadline 返却期限（ISO 8601 オフセット付き）
 * @property status 現在のステータス（例: RENTED）
 */
@Serializable
data class RentBookResponse(
    val rentId: Uuid,
    val bookId: Int,
    val userId: Uuid,
    val rentalAt: String,
    val returnDeadline: String,
    val status: String,
): ApiResponseDataField
