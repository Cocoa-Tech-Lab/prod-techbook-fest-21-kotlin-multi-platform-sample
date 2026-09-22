package com.example.bookmanager.domain.model.account

import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.Email
import kotlin.time.Instant


// アカウントエンティティ。BookEntity と同様に New/Persisted に分岐させる
sealed interface AccountEntity {
    // 未永続化の新規アカウント
    data class New(
        val name: String,
        val email: Email,
        val hashedPassword: String,
        val level: AccountLevel = AccountLevel.General,
    ) : AccountEntity

    // 永続化済みのアカウント
    data class Persisted(
        val id: AccountId,
        val name: String,
        val email: Email,
        val hashedPassword: String,
        val level: AccountLevel,
        val createdAt: Instant, // timestamptz
        val updatedAt: Instant, // timestamptz
    ) : AccountEntity
}

