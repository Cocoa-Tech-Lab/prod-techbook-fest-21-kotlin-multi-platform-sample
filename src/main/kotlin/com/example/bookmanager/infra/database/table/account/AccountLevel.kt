package com.example.bookmanager.infra.database.table.account

// アカウントの権限レベル。
// DB 側の ENUM 型 ACCOUNT_LEVEL と 1:1 で対応します。
// 値の追加・変更は Flyway のマイグレーション（ALTER TYPE ... ADD VALUE）で行うこと。
enum class AccountLevel {
    // 一般ユーザー
    General,
    // 管理者
    Admin
}