package com.example.bookmanager.application.port

/**
 * 認可トークン（例: JWT）を発行するアプリケーション層のポート。
 *
 * インフラ層の実装に依存しないよう、発行に必要な最小限の情報だけを受け取り、
 * シリアライズ済みのトークン文字列を返します。
 */
interface TokenIssuer {
    /**
     * トークンを発行します。
     *
     * 典型的な JWT 実装では、以下のようなクレームに対応します。
     * - subject: 認証主体（ユーザID等）
     * - email: 連絡先メールアドレス
     * - name: 表示名
     * - level: アカウント権限レベル（例: Admin/User）
     *
     * @param subject 認証主体（ユーザIDなど、クライアントが一意に識別できる値）
     * @param email メールアドレス
     * @param name 表示名
     * @param level アカウント権限レベル
     * @return 署名済みのトークン文字列（例: JWT）
     */
    fun issue(
        subject: String,
        email: String,
        name: String,
        level: String,
    ): String
}