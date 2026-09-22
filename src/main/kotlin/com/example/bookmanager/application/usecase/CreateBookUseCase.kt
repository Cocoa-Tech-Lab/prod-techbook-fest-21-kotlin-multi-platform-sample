package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.dto.book.Book
import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.book.BookEntity
import com.example.bookmanager.domain.model.common.Isbn
import com.example.bookmanager.domain.port.BookRepository
import kotlinx.datetime.LocalDate

/**
 * 書籍を新規登録するユースケース。
 *
 * 役割:
 * - トランザクション境界内で Repository を呼び出し、ドメインの登録結果をAPI向けDTOに射影します。
 */
class CreateBookUseCase(
    private val txManager: TransactionManager,
    private val bookRepository: BookRepository
) {
    /**
     * 書籍登録用の入力モデル。
     * Controller 層がリクエストDTOをこの型に詰め替えて渡します。
     * @property title 書名
     * @property author 著者名
     * @property outline 概要
     * @property publishedAtIsoDate 出版日（kotlinx.datetime.LocalDate）
     * @property isbn ISBN 文字列（ハイフン任意、UseCase内で [Isbn] に変換）
     */
    data class CreateBookInput(
        val title: String,
        val author: String,
        val outline: String,
        val publishedAtIsoDate: LocalDate,
        val isbn: String,
    )

    /**
     * 登録結果。
     * @property book 登録された書籍のアプリケーションDTO
     */
    data class CreateBookResult(
        val book: Book
    )

    /**
     * 書籍を登録して結果を返します。
     * - すでに存在チェック等は要件次第。現在は Repository 層の制約に委譲しています。
     */
    suspend fun execute(input: CreateBookInput): CreateBookResult = txManager.inTransaction {
        val book = bookRepository.register(
            BookEntity.New(
                title = input.title,
                isbn = Isbn(input.isbn),
                outline = input.outline,
                author = input.author,
                publishedAt = input.publishedAtIsoDate
            )
        )
        CreateBookResult(
            book = Book(
                id = book.bookId.value,
                name = book.title,
                author = book.author,
                outline = book.outline,
                isbn = book.isbn.value,
                publishedAt = book.publishedAt,
                depositedAt = book.depositedAt,
            )
        )
    }
}