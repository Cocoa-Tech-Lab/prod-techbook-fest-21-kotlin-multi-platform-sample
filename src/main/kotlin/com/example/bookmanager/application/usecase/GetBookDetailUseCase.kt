package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.dto.book.Book
import com.example.bookmanager.application.exception.BookNotFound
import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.RentStatus
import com.example.bookmanager.domain.port.BookRepository
import kotlin.time.ExperimentalTime

/**
 * 書籍詳細を取得するユースケース。
 *
 * - 指定IDの書籍を取得し、貸出状態から canRent を導出します。
 * - 見つからない場合は [BookNotFound] を投げます（StatusPages で 404 に変換）。
 */
class GetBookDetailUseCase(
    private val txManager: TransactionManager,
    private val bookRepository: BookRepository
) {
    /**
     * 詳細取得結果。
     * @property book 画面表示用の書籍DTO
     * @property canRent 現在借りられるか
     */
    data class GetBookResult(
        val book: Book,
        val canRent: Boolean
    )

    /**
     * 書籍IDから詳細を取得します。
     * @throws BookNotFound 見つからない場合
     */
    @OptIn(ExperimentalTime::class)
    suspend fun execute(bookId: Int): GetBookResult = txManager.inTransaction {
        bookRepository.findById(BookId(bookId))?.let { (book, rentStatus) ->
            GetBookResult(
                book = Book(
                    id = book.bookId.value,
                    name = book.title,
                    author = book.author,
                    outline = book.outline,
                    isbn = book.isbn.value,
                    publishedAt = book.publishedAt,
                    depositedAt = book.depositedAt,
                ),
                canRent = rentStatus == RentStatus.Returned
            )
        } ?: throw BookNotFound(bookId)
    }
}
