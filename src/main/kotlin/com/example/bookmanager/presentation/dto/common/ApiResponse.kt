package com.example.bookmanager.presentation.dto.common

import kotlinx.serialization.Serializable

/**
 * API レスポンスの共通ラッパー（エンベロープ）。
 *
 * - 成功時は [Success] にアプリ固有のデータ型 [ApiResponseDataField] を包んで返します。
 * - 失敗時は [Failure] にエラー情報 [ErrorInfo] を詰めて返します。
 *
 * フロントエンドはこの型に合わせて分岐することで、成功/失敗を一貫して扱えます。
 */
@Serializable
sealed interface ApiResponse {
    /**
     * 正常終了を表すレスポンス。
     * @param T レスポンスボディのデータ型
     * @property data 実際にクライアントへ返すデータ
     */
    @Serializable
    data class Success<T: ApiResponseDataField>(val data: T): ApiResponse

    /**
     * 異常終了（エラー）を表すレスポンス。
     * @property errorInfo ステータスコードやメッセージなどのエラー情報
     */
    @Serializable
    data class Failure(val errorInfo: ErrorInfo): ApiResponse
}