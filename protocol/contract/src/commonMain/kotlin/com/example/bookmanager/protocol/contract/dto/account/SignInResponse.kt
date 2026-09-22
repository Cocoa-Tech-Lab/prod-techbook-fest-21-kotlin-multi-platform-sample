package com.example.bookmanager.protocol.contract.dto.account

import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import kotlinx.serialization.Serializable

/**
 * サインイン成功時に返すレスポンスDTO。
 *
 * フロントエンドは受け取った token を Authorization ヘッダに付与して以降のAPIを呼びます。
 * 例: Authorization: JWT <token>
 *
 * @property token 発行されたJWT
 */
@Serializable
data class SignInResponse(
    val token: String
): ApiResponseDataField
