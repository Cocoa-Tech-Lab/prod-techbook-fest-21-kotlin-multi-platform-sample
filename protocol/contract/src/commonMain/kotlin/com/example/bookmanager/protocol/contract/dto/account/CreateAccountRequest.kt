package com.example.bookmanager.protocol.contract.dto.account

import com.example.bookmanager.protocol.contract.details.ApiRequestBody
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
): ApiRequestBody
