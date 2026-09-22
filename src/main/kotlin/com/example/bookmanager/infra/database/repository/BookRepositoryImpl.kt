package com.example.bookmanager.infra.database.repository

import com.example.bookmanager.domain.model.book.BookEntity
import com.example.bookmanager.domain.model.book.BookWithStatus
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.Isbn
import com.example.bookmanager.domain.model.common.RentStatus
import com.example.bookmanager.domain.port.BookRepository
import com.example.bookmanager.infra.database.table.book.BookTable
import com.example.bookmanager.infra.database.table.rental.RentalTable
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.updateReturning
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

/**
 * BookRepository の Exposed 実装。
 *
 * 設計メモ:
 * - トランザクション境界は UseCase が保持します（本リポジトリは開始/終了しない）。
 * - 貸出状況は rental テーブルの「未返却行の有無」で判定します。
 *   返却済み行は where で除外し、未返却が存在すれば Borrowed、なければ Returned とします。
 * - 一覧取得では N+1 を避けるため、未返却レンタルの件数をまとめて取得し Map 化して突き合わせています。
 */
class BookRepositoryImpl : BookRepository {
    override fun register(book: BookEntity.New): BookEntity.Persisted {
        return BookTable.insertReturning(
            returning = listOf(
                BookTable.id,
                BookTable.bookName,
                BookTable.bookOutline,
                BookTable.publishedAt,
                BookTable.author,
                BookTable.depositedAt,
                BookTable.isbn
            ),
        ) { row ->
            row[BookTable.bookName] = book.title
            row[BookTable.bookOutline] = book.outline
            row[BookTable.publishedAt] = book.publishedAt
            row[BookTable.author] = book.author
            row[BookTable.isbn] = book.isbn.value
            row[BookTable.depositedAt] = Clock.System.now()
                .toJavaInstant()
                .atZone(ZoneOffset.UTC)
                .toOffsetDateTime()

        }.first().toPersistedBookEntity()
    }

    override fun findAll(): List<BookWithStatus> {
        // 1) まずは書籍の基本情報を取得する SELECT。
        //    - 必要なカラムのみを明示指定（id, name, outline, publishedAt, author, isbn, depositedAt）。
        //    - 並び順は「書籍IDの昇順」。必要に応じて蔵書日などに変更可能。
        val books = BookTable
            .leftJoin(
                otherTable = RentalTable,
                onColumn = { BookTable.id },
                otherColumn = { RentalTable.book },
                additionalConstraint = { RentalTable.returnedAt.isNull() }
            )
            .select(
                BookTable.id,
                BookTable.bookName,
                BookTable.bookOutline,
                BookTable.publishedAt,
                BookTable.author,
                BookTable.isbn,
                BookTable.depositedAt,
                RentalTable.id,
                RentalTable.returnedAt
            )
            .orderBy(BookTable.id to SortOrder.ASC)

        // 3) 取得した書籍一覧に対し、上で作成した Map を使って「貸出状態」を合成する。
        //    - rentals に存在しない場合、貸出中ではない
        //    - rentals に存在する場合、未返却であるため貸出中である
        return books.map {it.toPersistedBookWithSatus() }
    }

    override fun findById(id: BookId): BookWithStatus? {
        // 指定IDの書籍を LEFT JOIN で rental と突き合わせる。
        // - 追加条件: rental.returnedAt IS NULL のみを結合（未返却のみ）。
        //   - 貸出状態管理テーブルにおいて、返却日時がnullの場合にはbookがunique制約の対象となるため、結合先は最大で1行に絞られる
        // - 結合結果に rental の行が存在する場合は Borrowed、存在しない場合は Returned。
        val row = BookTable
            .leftJoin(
                otherTable = RentalTable,
                onColumn = { BookTable.id },
                otherColumn = { RentalTable.book },
                additionalConstraint = { RentalTable.returnedAt.isNull() }
            )
            .select(
                BookTable.id,
                BookTable.bookName,
                BookTable.bookOutline,
                BookTable.publishedAt,
                BookTable.author,
                BookTable.isbn,
                BookTable.depositedAt,
                RentalTable.id,
                RentalTable.returnedAt
            )
            .where(BookTable.id eq id.value)
            .firstOrNull()

        return row?.toPersistedBookWithSatus()
    }

    override fun update(
        book: BookEntity.Persisted
    ): BookEntity.Persisted? {
        val updated = BookTable.updateReturning(
            returning = listOf(
                BookTable.id,
                BookTable.bookName,
                BookTable.bookOutline,
                BookTable.publishedAt,
                BookTable.author,
                BookTable.depositedAt,
                BookTable.isbn
            ),
            where = { BookTable.id eq book.bookId.value }
        ) { row ->
            row[BookTable.bookName] = book.title
            row[BookTable.bookOutline] = book.outline
            row[BookTable.author] = book.author
            row[BookTable.isbn] = book.isbn.value
            row[BookTable.depositedAt] = book.depositedAt.toJavaInstant().atZone(ZoneOffset.UTC).toOffsetDateTime()
            row[BookTable.publishedAt] = book.publishedAt
        }

        return updated.firstOrNull()?.toPersistedBookEntity()
    }

    override fun delete(id: BookId): Int {
        return BookTable.deleteWhere { BookTable.id eq id.value }
    }
}

private fun ResultRow.toPersistedBookEntity(): BookEntity.Persisted {
    return BookEntity.Persisted(
        bookId = BookId(this[BookTable.id].value),
        title = this[BookTable.bookName],
        outline = this[BookTable.bookOutline],
        publishedAt = this[BookTable.publishedAt],
        author = this[BookTable.author],
        depositedAt = this[BookTable.depositedAt].toInstant().toKotlinInstant(),
        isbn = Isbn(this[BookTable.isbn]),
    )
}


private fun ResultRow.toPersistedBookWithSatus(): BookWithStatus {
    return BookWithStatus(
        book = this.toPersistedBookEntity(),
        rentStatus = when{
            this.getOrNull(RentalTable.id) == null -> RentStatus.Returned
            this[RentalTable.returnedAt] == null -> RentStatus.Borrowed
            else -> RentStatus.Returned
        }
    )
}