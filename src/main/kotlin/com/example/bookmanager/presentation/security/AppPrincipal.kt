package com.example.bookmanager.presentation.security

import com.example.bookmanager.domain.model.account.AccountLevel
import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.Email
import io.ktor.server.auth.jwt.*

/**
 * 認証済みユーザを表すアプリケーション専用の Principal。
 *
 * Ktor の JWT 認証で検証済みの [JWTPrincipal] から生成し、
 * 以降のハンドラやプラグインでユーザ情報と権限を参照するために使います。
 *
 * - accountId: JWT の subject をアプリ内の型 [AccountId] に変換したもの
 * - accountLevel: 権限レベル（例: Admin, User）
 * - name: 表示名
 * - email: 連絡先メールアドレス
 *
 * 使用例:
 *   val p = call.principal<AppPrincipal>()
 *   if (p?.accountLevel == AccountLevel.Admin) { /* ... */ }
 *
 * @property accountId ユーザの一意なID
 * @property accountLevel アカウントの権限レベル
 * @property name 表示名
 * @property email メールアドレス
 */
data class AppPrincipal(
    val accountId: AccountId,
    val accountLevel: AccountLevel,
    val name: String,
    val email: Email,
){
    /**
     * JWT のクレームから [AppPrincipal] を構築します。
     * - subject -> [AccountId]
     * - level -> [AccountLevel]
     * - name -> 表示名
     * - email -> [Email]
     */
    constructor(source: JWTPrincipal): this(
        accountId = AccountId.parse(source.payload.subject),
        accountLevel = source.payload.getClaim("level").asString().let { AccountLevel.valueOf(it) },
        name = source.payload.getClaim("name").asString(),
        email = Email(source.payload.getClaim("email").asString()),
    )
}