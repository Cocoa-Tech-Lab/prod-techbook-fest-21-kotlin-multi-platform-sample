package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.rental.RentalEntity
import com.example.bookmanager.domain.port.RentalRepository
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import kotlin.uuid.Uuid

/**
 * 書籍を貸し出すユースケース。
 *
 * - 同一書籍に未返却の貸出が存在しないことを確認してから貸出レコードを作成します。
 * - 返却期限は「7日後の23:59:59.999999999（UTC）」に揃えます。
 */
class RentBookUseCase(
    private val txManager: TransactionManager,
    private val rentalRepository: RentalRepository,
) {
    /**
     * 入力モデル。
     * @property bookId 書籍ID
     * @property userId 借りるユーザのID
     */
    data class RentBookInput(
        val bookId: Int,
        val userId: Uuid,
    )

    /**
     * 結果モデル。
     * @property rentId 採番された貸出ID
     * @property bookId 書籍ID
     * @property userId ユーザID
     * @property rentalAt 貸出日時
     * @property returnDeadline 返却期限（UTC）
     * @property status 現在の状態（Borrowed/Returned）
     */
    data class RentBookResult(
        val rentId: Uuid,
        val bookId: Int,
        val userId: Uuid,
        val rentalAt: Instant,
        val returnDeadline: Instant,
        val status: String,
    )

    /**
     * 貸出処理を実行します。
     * - 未返却の貸出が既にある場合はエラーを投げます。
     */
    suspend fun execute(input: RentBookInput): RentBookResult = txManager.inTransaction {
        val bid = BookId(input.bookId)
        val uid = AccountId(input.userId)
        // 返却期限: 7日後の23:59:59.999999999 (UTC)
        val dead = Clock.System.now()
            .toJavaInstant()
            .atZone(ZoneOffset.UTC)
            .plusDays(8)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .minusNanos(1)
            .toInstant()
            .toKotlinInstant()

        // 事前チェック: 既に未返却の貸出があるか
        val active = rentalRepository.findActiveByBook(bid)
        require(active == null) { "book already borrowed" }

        val saved = rentalRepository.rent(
            RentalEntity.New(
                bookId = bid,
                userId = uid,
                returnDeadline = dead,
            )
        )

        RentBookResult(
            rentId = saved.id.value,
            bookId = saved.bookId.value,
            userId = saved.userId.value,
            rentalAt = saved.rentalAt,
            returnDeadline = saved.returnDeadline,
            status = saved.status.name,
        )
    }
}
