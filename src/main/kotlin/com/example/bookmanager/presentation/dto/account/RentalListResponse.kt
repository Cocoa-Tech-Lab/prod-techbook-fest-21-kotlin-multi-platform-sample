package com.example.bookmanager.presentation.dto.account

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/**
 * レンタル履歴の1行分を表すDTO。
 *
 * @property rentId 貸出ID
 * @property bookId 書籍ID
 * @property userId ユーザID
 * @property status 状態（RENTED/RETURNED など）
 * @property rentalAt 貸出日時（ISO 8601 オフセット付き）
 * @property returnDeadline 返却期限（ISO 8601 オフセット付き）
 * @property returnedAt 返却日時（返却済みの場合のみ。ISO 8601 オフセット付き）
 */
@Serializable
data class RentalHistoryItem(
    val rentId: Uuid,
    val bookId: Int,
    val userId: Uuid,
    val status: String,
    val rentalAt: String,
    val returnDeadline: String,
    val returnedAt: String?,
)

/**
 * 自分のレンタル履歴一覧レスポンス。
 * @property items 履歴の配列（新しい順などは呼び出し側の仕様に依存）
 */
@Serializable
data class RentalListResponse(
    val items: List<RentalHistoryItem>
): ApiResponseDataField
