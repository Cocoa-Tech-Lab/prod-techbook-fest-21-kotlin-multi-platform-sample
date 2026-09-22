package com.example.bookmanager.presentation.dto.account

import com.example.bookmanager.presentation.dto.common.ApiResponseDataField
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * アカウント登録完了時に返すレスポンスDTO。
 *
 * - クライアントは accountId を保持し、必要に応じてプロフィール表示などに利用します。
 * - センシティブな情報（ハッシュ化パスワード等）は含みません。
 *
 * @property accountId 作成されたアカウントのID
 * @property email 登録したメールアドレス
 * @property name 表示名（現状はメールローカル部等から生成される想定）
 */
@OptIn(ExperimentalUuidApi::class)
@Serializable
data class CreateAccountResponse(
    val accountId: Uuid,
    val email: String,
    val name: String
): ApiResponseDataField
