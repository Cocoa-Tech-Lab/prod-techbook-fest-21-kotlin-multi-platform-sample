package com.example.bookmanager.domain.model.common

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// ドメイン共通: 各種IDや値オブジェクト
@JvmInline
value class BookId @OptIn(ExperimentalUuidApi::class) constructor(val value: Int)

@JvmInline
value class AccountId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun parse(text: String): AccountId = AccountId(Uuid.parse(text))
    }
}

@JvmInline
value class RentId @OptIn(ExperimentalUuidApi::class) constructor(val value: Uuid) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun parse(text: String): RentId = RentId(Uuid.parse(text))
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        // 超軽量な構文チェック（RFC準拠ではない）。本格的な検証はアプリ層で行う。
        require('@' in value && value.length <= 384) { "invalid email" }
    }
}

@JvmInline
value class Isbn(val value: String) {
    init {
        // DBは最大17文字（ハイフン含む）を許容
        require(value.isNotBlank() && value.length <= 17) { "invalid isbn length" }
    }
}
