package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.account.AccountEntity
import com.example.bookmanager.domain.model.account.AccountLevel
import com.example.bookmanager.domain.model.common.Email
import com.example.bookmanager.domain.port.AccountRepository
import de.mkammerer.argon2.Argon2Factory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * アカウントを新規登録するユースケース。
 *
 * - Argon2id でパスワードをハッシュ化し、メールアドレス重複をチェックしてから作成します。
 * - トランザクション境界は本ユースケースが保持します。
 */
class RegisterAccountUseCase(
    private val txManager: TransactionManager,
    private val accountRepository: AccountRepository
) {
    /**
     * 入力モデル。
     * @property email 登録するメールアドレス
     * @property password 平文パスワード（本ユースケース内でハッシュ化）
     */
    data class RegisterAccountInput(
        val email: String,
        val password: String,
    )

    /**
     * 結果モデル。
     * @property accountId 作成されたアカウントのID
     * @property email 登録したメールアドレス
     * @property name 表示名（email のローカル部を初期値として採用）
     */
    @OptIn(ExperimentalUuidApi::class)
    data class RegisterAccountResult(
        val accountId: Uuid,
        val email: String,
        val name: String
    )

    /**
     * アカウントを登録します。
     * - パスワードは Argon2id でハッシュ化します。
     * - 8文字未満のパスワードは拒否します。
     * - 既に同一メールが存在する場合はエラーを投げます。
     */
    @OptIn(ExperimentalUuidApi::class)
    suspend fun execute(input: RegisterAccountInput): RegisterAccountResult{
        // パスワードハッシュ（Argon2id）
        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
        val hash = withContext(Dispatchers.Default){
            argon2.hash(
                2,
                65536,
                3,
                input.password.toCharArray()
            )
        }
        argon2.wipeArray(input.password.toCharArray())

        val result = txManager.inTransaction {
            // 入力の軽微なバリデーション
            val email = Email(input.email.trim())
            require(input.password.length >= 8) { "password too short" }

            // 既存チェック
            val exists = accountRepository.findByEmail(email)
            require(exists == null) { "email already registered" }

            // 表示名はとりあえず email のローカル部を使用
            val displayName = input.email.substringBefore('@').ifBlank { "user" }

            val saved = accountRepository.register(
                AccountEntity.New(
                    name = displayName,
                    email = email,
                    hashedPassword = hash,
                    level = AccountLevel.General
                )
            )

            RegisterAccountResult(
                accountId = saved.id.value,
                email = saved.email.value,
                name = saved.name
            )
        }
        return result
    }
}