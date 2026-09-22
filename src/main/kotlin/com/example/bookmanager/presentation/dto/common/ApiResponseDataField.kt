package com.example.bookmanager.presentation.dto.common

import kotlinx.serialization.Serializable

/**
 * 成功レスポンス [ApiResponse.Success] の data 部分に入る型のマーカーインターフェース。
 *
 * 画面やクライアントに返すためのシンプルな DTO はこのインターフェースを実装します。
 */
interface ApiResponseDataField

/**
 * ボディに意味のあるデータが無い場合に使う空オブジェクト。
 * 例: 削除が成功した時など。
 */
@Serializable
data object Empty: ApiResponseDataField