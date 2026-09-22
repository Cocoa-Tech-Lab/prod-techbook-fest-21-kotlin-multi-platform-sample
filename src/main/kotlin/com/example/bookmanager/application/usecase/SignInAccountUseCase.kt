package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.Email
import com.example.bookmanager.domain.port.AccountRepository
import de.mkammerer.argon2.Argon2Factory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * サインイン（認証）ユースケース。
 *
 * - メールアドレスでアカウントを検索し、Argon2id でパスワード検証を行います。
 * - 成功時にクライアントがトークン発行へ進むための最小情報を返します。
 */
class SignInAccountUseCase(
    private val txManager: TransactionManager,
    private val accountRepository: AccountRepository
) { 
    /**
     * 入力モデル。
     * @property email メールアドレス
     * @property password 平文パスワード
     */
    data class SignInInput(
        val email: String,
        val password: String
    )

    /**
     * 結果モデル。
     * @property accountId 認証に成功したアカウントID
     * @property email メールアドレス
     * @property name 表示名
     * @property level 権限レベル（文字列表現）
     */
    @OptIn(ExperimentalUuidApi::class)
    data class SignInResult(
        val accountId: Uuid, // UUID string
        val email: String,
        val name: String,
        val level: String,
    )

    /**
     * 認証処理を行います。
     * @throws IllegalArgumentException メールアドレスが存在しない、またはパスワードが一致しない場合
     */
    @OptIn(ExperimentalUuidApi::class)
    suspend fun execute(input: SignInInput): SignInResult = txManager.inTransaction {
        val email = Email(input.email.trim())
        val account = accountRepository.findByEmail(email)
            ?: throw IllegalArgumentException("invalid email or password")

        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
        val verified = argon2.verify(
            account.hashedPassword,
            input.password.toCharArray()
        )

        if (!verified) throw IllegalArgumentException("invalid email or password")

        SignInResult(
            accountId = account.id.value,
            email = account.email.value,
            name = account.name,
            level = account.level.name,
        )
    }
}
