package com.example.bookmanager.domain.model.common

/**
 * 貸出状態を表す列挙。
 *
 * - Borrowed: 未返却（現在貸出中）
 * - Returned: 返却済み
 *
 * レンタル履歴や書籍一覧の「貸出可否」判定に利用します。
 */
enum class RentStatus {
    /** 未返却（貸出中） */
    Borrowed,
    /** 返却済み */
    Returned
}