package com.example.bookmanager.infra.database.repository

import com.example.bookmanager.domain.model.account.AccountEntity
import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.Email
import com.example.bookmanager.domain.port.AccountRepository
import com.example.bookmanager.infra.database.table.account.AccountTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.time.toKotlinInstant
import kotlin.uuid.Uuid
import com.example.bookmanager.domain.model.account.AccountLevel as DomainAccountLevel
import com.example.bookmanager.infra.database.table.account.AccountLevel as DbAccountLevel

/**
 * AccountRepository の Exposed 実装。
 *
 * - トランザクション境界は UseCase が保持します。
 * - ドメインの AccountLevel と DB の列挙（infra.database.table.account.AccountLevel）の相互変換を行います。
 */
class AccountRepositoryImpl : AccountRepository {
    override fun findByEmail(email: Email): AccountEntity.Persisted? {
        val row = AccountTable
            .selectAll()
            .where(AccountTable.email eq email.value)
            .firstOrNull()
        return row?.toPersisted()
    }

    override fun register(newAccount: AccountEntity.New): AccountEntity.Persisted {
        val inserted = AccountTable.insertReturning(
            returning = listOf(
                AccountTable.id,
                AccountTable.name,
                AccountTable.email,
                AccountTable.hashedPassword,
                AccountTable.accountLevel,
                AccountTable.createdAt,
                AccountTable.updatedAt
            )
        ) { row ->
            row[AccountTable.id] = Uuid.random()
            row[AccountTable.name] = newAccount.name
            row[AccountTable.email] = newAccount.email.value
            row[AccountTable.hashedPassword] = newAccount.hashedPassword
            row[AccountTable.accountLevel] = when (newAccount.level) {
                DomainAccountLevel.General -> DbAccountLevel.General
                DomainAccountLevel.Admin -> DbAccountLevel.Admin
            }
        }.first()
        return inserted.toPersisted()
    }
}

private fun ResultRow.toPersisted(): AccountEntity.Persisted {
    return AccountEntity.Persisted(
        id = AccountId(this[AccountTable.id].value),
        name = this[AccountTable.name],
        email = Email(this[AccountTable.email]),
        hashedPassword = this[AccountTable.hashedPassword],
        level = when (this[AccountTable.accountLevel]) {
            DbAccountLevel.General -> DomainAccountLevel.General
            DbAccountLevel.Admin -> DomainAccountLevel.Admin
        },
        createdAt = this[AccountTable.createdAt].toInstant().toKotlinInstant(),
        updatedAt = this[AccountTable.updatedAt].toInstant().toKotlinInstant(),
    )
}