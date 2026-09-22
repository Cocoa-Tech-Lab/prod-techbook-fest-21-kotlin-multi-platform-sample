package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.dto.book.Book
import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.RentStatus
import com.example.bookmanager.domain.port.BookRepository

/**
 * 書籍一覧を取得するユースケース。
 *
 * - ドメインから取得した書籍と貸出状態を、表示用のシンプルなDTOに射影します。
 * - canRent は RentStatus が Returned のときに true となります。
 */
class ListBookUseCase(
    private val txManager: TransactionManager,
    private val bookRepository: BookRepository
) {
    /**
     * 一覧結果。
     * @property books 1行= [Book] と貸出可否のペア
     */
    data class ListBookResult(
        val books: List<Pair<Book, Boolean>>
    )

    /**
     * 書籍一覧を取得します。
     * 返却値の canRent は現在借りられるかどうかを示します。
     */
    suspend fun execute() = txManager.inTransaction {
        bookRepository.findAll().map { (book, rentStatus) ->
            Book(
                id = book.bookId.value,
                name = book.title,
                author = book.author,
                outline = book.outline,
                isbn = book.isbn.value,
                publishedAt = book.publishedAt,
                depositedAt = book.depositedAt,
            ) to (rentStatus == RentStatus.Returned)
        }.let {
            ListBookResult(books = it)
        }
    }
}