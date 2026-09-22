package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.dto.book.Book
import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.port.BookRepository
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

/**
 * 書籍更新ユースケース。
 * - ISBN と ID は不変。
 * - 既存を読み出して差分を適用し、更新後の値を返します。
 */
class UpdateBookUseCase(
    private val txManager: TransactionManager,
    private val bookRepository: BookRepository
) {
    /**
     * 書籍更新用の入力モデル。
     * ISBN と ID は不変のため、更新対象に含めません。
     * @property bookId 更新対象のID
     * @property title 書名
     * @property author 著者名
     * @property outline 概要
     * @property publishedAtIsoDate 出版日（kotlinx.datetime.LocalDate）
     */
    data class UpdateBookInput(
        val bookId: Int,
        val title: String,
        val author: String,
        val outline: String,
        // ISO-8601 (yyyy-MM-dd)
        val publishedAtIsoDate: LocalDate,
    )

    /**
     * 更新結果。
     * @property book 更新後の書籍DTO
     */
    data class UpdateBookResult(
        val book: Book,
    )

    /**
     * 書籍の内容を更新します。
     * @throws IllegalStateException 対象が存在しない場合
     */
    @OptIn(ExperimentalTime::class)
    suspend fun execute(input: UpdateBookInput): UpdateBookResult = txManager.inTransaction {
        val current = bookRepository.findById(BookId(input.bookId))
            ?: error("book ${input.bookId} doesn't exist!")
        val updated = bookRepository.update(
            book = current.book.copy(
                title = input.title,
                outline = input.outline,
                author = input.author,
                publishedAt = input.publishedAtIsoDate
            )
        ) ?: error("book ${input.bookId} doesn't exist!")
        UpdateBookResult(
            book = Book(
                id = updated.bookId.value,
                name = updated.title,
                author = updated.author,
                outline = updated.outline,
                isbn = updated.isbn.value,
                publishedAt = updated.publishedAt,
                depositedAt = updated.depositedAt,
            ))
    }
}
