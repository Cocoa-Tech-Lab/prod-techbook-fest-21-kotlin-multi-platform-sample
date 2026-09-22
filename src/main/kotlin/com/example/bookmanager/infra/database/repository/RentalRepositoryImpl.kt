package com.example.bookmanager.infra.database.repository

import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.RentId
import com.example.bookmanager.domain.model.common.RentStatus
import com.example.bookmanager.domain.model.rental.RentalEntity
import com.example.bookmanager.domain.port.RentalRepository
import com.example.bookmanager.infra.database.table.rental.RentalTable
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.*
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

/**
 * RentalRepository の Exposed 実装。
 *
 * 設計メモ:
 * - トランザクション境界は UseCase が保持します（本リポジトリは開始/終了しない）。
 * - 未返却の判定は rental.returned_at IS NULL を用います（部分ユニーク制約もこれを前提に構成）。
 * - Kotlin UUID と Java UUID の相互変換を行います（kotlin.uuid と java.util.UUID の橋渡し）。
 * - 時刻は Kotlin Instant -> Java Instant -> OffsetDateTime(UTC) に変換して永続化します。
 */
class RentalRepositoryImpl : RentalRepository {
    /**
     * 貸出レコードを作成します。重複貸出（未返却の同一 book_id）は DB 側のユニーク制約で弾かれます。
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override fun rent(newRental: RentalEntity.New): RentalEntity.Persisted {
        val inserted = RentalTable.insertReturning(
            returning = listOf(
                RentalTable.id,
                RentalTable.book,
                RentalTable.user,
                RentalTable.rentalAt,
                RentalTable.returnedAt,
                RentalTable.returnDeadline,
            )
        ) { row ->
            // 主にアプリが付与するフィールドを設定（rentalAt は現在時刻・UTCで保存）
            row[RentalTable.book] = newRental.bookId.value
            row[RentalTable.user] = newRental.userId.value.toJavaUuid()
            row[RentalTable.rentalAt] = OffsetDateTime.now(ZoneOffset.UTC)
            // 返却期限は UseCase で決定された値をそのまま保存（UTC へ変換）
            row[RentalTable.returnDeadline] = newRental.returnDeadline.toJavaInstant().atOffset(ZoneOffset.UTC)
        }.first()

        return inserted.toPersistedRentalEntity()
    }

    /**
     * 返却処理。returnedAt を現在時刻で更新します。
     * 見つからない場合は null を返します。
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override fun returnById(rentId: RentId): RentalEntity.Persisted? {
        val updated = RentalTable.updateReturning(
            returning = listOf(
                RentalTable.id,
                RentalTable.book,
                RentalTable.user,
                RentalTable.rentalAt,
                RentalTable.returnedAt,
                RentalTable.returnDeadline,
            ),
            where = { RentalTable.id eq rentId.value.toJavaUuid() }
        ) { row ->
            row[RentalTable.returnedAt] = OffsetDateTime.now(ZoneOffset.UTC)
        }
        return updated.firstOrNull()?.toPersistedRentalEntity()
    }

    /**
     * 指定書籍の未返却の貸出を1件取得します。
     */
    override fun findActiveByBook(bookId: BookId): RentalEntity.Persisted? {
        return RentalTable
            .selectAll()
            .where((RentalTable.book eq bookId.value) and RentalTable.returnedAt.isNull())
            .firstOrNull()
            ?.toPersistedRentalEntity()
    }

    /**
     * ユーザの貸出一覧を取得します。activeOnly=true の場合は未返却のみを返します。
     */
    @OptIn(ExperimentalUuidApi::class)
    override fun listByUser(userId: AccountId, activeOnly: Boolean): List<RentalEntity.Persisted> {
        val condition = if (activeOnly) {
            (RentalTable.user eq userId.value.toJavaUuid()) and RentalTable.returnedAt.isNull()
        } else {
            RentalTable.user eq userId.value.toJavaUuid()
        }
        return RentalTable
            .selectAll()
            .where(condition)
            .map { it.toPersistedRentalEntity() }
    }
}

/**
 * Exposed の ResultRow からドメインの Rental へのマッピング関数。
 * returnedAt の null/非null をもとに RentStatus を導出します。
 */
@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
private fun ResultRow.toPersistedRentalEntity(): RentalEntity.Persisted {
    return RentalEntity.Persisted(
        id = RentId(this[RentalTable.id].value.toKotlinUuid()),
        bookId = BookId(this[RentalTable.book].value),
        userId = AccountId(this[RentalTable.user].value.toKotlinUuid()),
        status = if (this[RentalTable.returnedAt] == null) RentStatus.Borrowed else RentStatus.Returned,
        rentalAt = this[RentalTable.rentalAt].toInstant().toKotlinInstant(),
        returnDeadline = this[RentalTable.returnDeadline].toInstant().toKotlinInstant(),
        returnedAt = this[RentalTable.returnedAt]?.toInstant()?.toKotlinInstant(),
    )
}
