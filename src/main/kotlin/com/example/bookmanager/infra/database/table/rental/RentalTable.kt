package com.example.bookmanager.infra.database.table.rental

import com.example.bookmanager.infra.database.table.account.AccountTable
import com.example.bookmanager.infra.database.table.book.BookTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

// Exposed v1 のテーブル定義: V1__init.sql の rent テーブルに対応
object RentalTable: UUIDTable(
    name = "rental",
    columnName = "rent_id"
) {
    // 書籍ID への外部キー（RESTRICT / CASCADE）
    val book = reference(
        "book_id",
        BookTable,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.CASCADE
    )
    // 利用者（アカウント）への外部キー（CASCADE / CASCADE）
    val user = reference(
        "user_id",
        AccountTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val rentalAt = timestampWithTimeZone("rental_at")
    val returnedAt = timestampWithTimeZone("returned_at").nullable()
    val returnDeadline = timestampWithTimeZone("return_deadline")

    // メモ: 同一の本を同時に借りられないための部分ユニーク制約
    init {
        uniqueIndex(book, user){
            returnedAt.isNull()
        }
    }
}