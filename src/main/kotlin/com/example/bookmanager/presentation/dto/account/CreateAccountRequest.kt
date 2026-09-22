package com.example.bookmanager.presentation.dto.account

import kotlinx.serialization.Serializable

/**
 * アカウント登録のためのリクエストDTO。
 *
 * @property email 登録するメールアドレス
 * @property password ログイン用パスワード（平文、サーバ側でハッシュ化）
 */
@Serializable
data class CreateAccountRequest(
    val email: String,
    val password: String,
)
