package com.example.bookmanager.presentation.dto.account

import kotlinx.serialization.Serializable

/**
 * サインイン（認証）用のリクエストDTO。
 *
 * @property email 登録済みメールアドレス
 * @property password パスワード（平文、サーバ側で検証）
 */
@Serializable
data class SignInRequest(
    val email: String,
    val password: String,
)
