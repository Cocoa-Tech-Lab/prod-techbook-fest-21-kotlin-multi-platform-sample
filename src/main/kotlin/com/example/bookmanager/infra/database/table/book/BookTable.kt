package com.example.bookmanager.infra.database.table.book

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.toJavaInstant

// Exposed v1 のテーブル定義: V1__init.sql の book テーブルに対応
object BookTable : IntIdTable(
    name = "book", columnName = "book_id"
) {
    // 書籍名（必須 / varchar(64)）
    val bookName = varchar("book_name", length = 64)
    // あらすじ（必須 / varchar(128)）
    val bookOutline = varchar("book_outline", length = 128)
    // 初版の出版日（date）
    val publishedAt = date("published_at")
    // 筆者名（必須 / varchar(32)）
    val author = varchar("author", length = 32)
    // 蔵書日（timestamptz / default current_timestamp）
    val depositedAt = timestampWithTimeZone("deposited_at").clientDefault {
        Clock.System.now().toJavaInstant().atZone(ZoneOffset.UTC).toOffsetDateTime()
    }
    // ISBN（varchar(17) / UNIQUE）
    val isbn = varchar("isbn", length = 17).uniqueIndex()

    init {
        // author へのインデックス（非ユニーク）
        index(false, author)
    }
}