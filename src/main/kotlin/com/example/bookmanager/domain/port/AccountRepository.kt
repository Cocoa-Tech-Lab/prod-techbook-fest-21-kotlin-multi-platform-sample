package com.example.bookmanager.domain.port

import com.example.bookmanager.domain.model.account.AccountEntity
import com.example.bookmanager.domain.model.common.Email

/**
 * アカウントのリポジトリ（ドメインポート）。
 */
interface AccountRepository {
    /**
     * メールアドレスで検索します。存在しない場合は null を返します。
     */
    fun findByEmail(email: Email): AccountEntity.Persisted?

    /**
     * 新規アカウントを登録します（UUID は実装側で採番）。
     */
    fun register(newAccount: AccountEntity.New): AccountEntity.Persisted
}