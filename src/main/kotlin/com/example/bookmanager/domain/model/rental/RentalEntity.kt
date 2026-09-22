package com.example.bookmanager.domain.model.rental

import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.RentId
import com.example.bookmanager.domain.model.common.RentStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// 貸出エンティティ。DBの rent テーブルに対応。新規作成と永続化済みで型を分岐
sealed interface RentalEntity {
    // 未永続化の新規貸出リクエスト
    @OptIn(ExperimentalTime::class)
    data class New(
        val bookId: BookId,
        val userId: AccountId,
        val returnDeadline: Instant,
    ) : RentalEntity

    // 永続化済みの貸出
    @OptIn(ExperimentalTime::class)
    data class Persisted(
        val id: RentId,
        val bookId: BookId,
        val userId: AccountId,
        val status: RentStatus,
        val rentalAt: Instant,
        val returnDeadline: Instant,
        val returnedAt: Instant?,
    ) : RentalEntity
}

