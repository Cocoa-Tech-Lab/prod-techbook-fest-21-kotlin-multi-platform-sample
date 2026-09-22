package com.example.bookmanager.presentation.dto.common

import kotlinx.serialization.Serializable

/**
 * エラー情報を表す DTO。
 *
 * @property code HTTP ステータスコード相当の数値
 * @property message ユーザやクライアント向けのエラーメッセージ
 */
@Serializable
data class ErrorInfo(
    val code: Int,
    val message: String
)
