package com.example.bookmanager.infra.database.table.account

import com.example.bookmanager.infra.database.common.PGEnum
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.toJavaInstant


// Exposed v1 のテーブル定義: V1__init.sql の account テーブルに対応
object AccountTable: UuidTable(
    name = "account",
    columnName = "account_id"
) {
    // 表示名（必須 / varchar(32)）
    val name = varchar("name", 32)
    // メールアドレス（必須 / varchar(384) / UNIQUE）
    // 備考: RFC準拠の最大長を考慮。
    val email = varchar("email", 384).uniqueIndex()
    // パスワードハッシュ（必須 / varchar(256)）
    val hashedPassword = varchar("hashed_password", 256)
    // 権限レベル（DBの ACCOUNT_LEVEL ENUM とマッピング）
    // PGEnum を使って Postgres の ENUM 型と相互変換
    val accountLevel = customEnumeration(
        name = "account_level",
        sql = "account_level",
        fromDb = { value -> AccountLevel.valueOf(value as String) },
        toDb = { PGEnum("account_level", it) }
    )
    // 作成日時（timestamptz / default current_timestamp）
    val createdAt = timestampWithTimeZone("created_at").clientDefault {
        Clock.System.now().toJavaInstant().atZone(ZoneOffset.UTC).toOffsetDateTime()
    }
    // 更新日時（timestamptz / default current_timestamp）
    val updatedAt = timestampWithTimeZone("updated_at").clientDefault {
        Clock.System.now().toJavaInstant().atZone(ZoneOffset.UTC).toOffsetDateTime()
    }
}