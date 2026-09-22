package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.port.RentalRepository
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * ユーザのレンタル履歴を一覧取得するユースケース。
 *
 * プレゼン層（Controller）から呼ばれ、トランザクション境界内で
 * Repository を使って集約を取得し、プレゼン向けのシンプルなDTOに変換します。
 */
class ListRentalHistoryUseCase(
    private val txManager: TransactionManager,
    private val rentalRepository: RentalRepository,
) {
    data class ListRentalHistoryInput(
        /** 対象ユーザID */
        val userId: Uuid,
        /** true で未返却（アクティブ）な履歴のみ */
        val activeOnly: Boolean = false,
    )

    data class RentalHistoryItem(
        val rentId: Uuid,
        val bookId: Int,
        val userId: Uuid,
        val status: String,
        val rentalAt: Instant,
        val returnDeadline: Instant,
        val returnedAt: Instant?,
    )

    data class ListRentalHistoryResult(
        val items: List<RentalHistoryItem>
    )

    /**
     * レンタル履歴を取得して Result DTO に詰めて返します。
     */
    suspend fun execute(input: ListRentalHistoryInput): ListRentalHistoryResult = txManager.inTransaction {
        val rentals = rentalRepository.listByUser(
            userId = AccountId(input.userId),
            activeOnly = input.activeOnly
        )
        ListRentalHistoryResult(
            items = rentals.map {
                // ドメインモデル -> 出力DTO への変換
                RentalHistoryItem(
                    rentId = it.id.value,
                    bookId = it.bookId.value,
                    userId = it.userId.value,
                    status = it.status.name,
                    rentalAt = it.rentalAt,
                    returnDeadline = it.returnDeadline,
                    returnedAt = it.returnedAt,
                )
            }
        )
    }
}
