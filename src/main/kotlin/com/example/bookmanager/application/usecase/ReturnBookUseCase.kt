package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.RentId
import com.example.bookmanager.domain.port.RentalRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * 返却を記録するユースケース。
 *
 * - 指定された貸出IDの returnedAt を現在時刻で更新し、最新状態を返します。
 */
class ReturnBookUseCase(
    private val txManager: TransactionManager,
    private val rentalRepository: RentalRepository,
) {
    /**
     * 入力モデル。
     * @property rentId 返却対象の貸出ID
     */
    @OptIn(ExperimentalUuidApi::class)
    data class ReturnBookInput(
        val rentId: Uuid, // UUID string
    )

    /**
     * 結果モデル。
     * @property rentId 貸出ID
     * @property bookId 書籍ID
     * @property userId ユーザID
     * @property status 現在の状態
     * @property rentalAt 貸出日時
     * @property returnDeadline 返却期限
     * @property returnedAt 返却日時（返却済みなら非null）
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    data class ReturnBookResult(
        val rentId: Uuid,
        val bookId: Int,
        val userId: Uuid,
        val status: String,
        val rentalAt: Instant,
        val returnDeadline: Instant,
        val returnedAt: Instant?,
    )

    /**
     * 返却処理を実施します。
     * @throws IllegalArgumentException 対象の貸出が見つからない場合
     */
    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    suspend fun execute(input: ReturnBookInput): ReturnBookResult = txManager.inTransaction {
        val updated = rentalRepository.returnById(
            rentId = RentId(input.rentId)
        ) ?: throw IllegalArgumentException("rental not found")

        ReturnBookResult(
            rentId = updated.id.value,
            bookId = updated.bookId.value,
            userId = updated.userId.value,
            status = updated.status.name,
            rentalAt = updated.rentalAt,
            returnDeadline = updated.returnDeadline,
            returnedAt = updated.returnedAt,
        )
    }
}
