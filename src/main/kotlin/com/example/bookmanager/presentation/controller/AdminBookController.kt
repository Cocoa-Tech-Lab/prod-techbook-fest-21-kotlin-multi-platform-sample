package com.example.bookmanager.presentation.controller

import com.example.bookmanager.application.usecase.CreateBookUseCase
import com.example.bookmanager.application.usecase.DeleteBookUseCase
import com.example.bookmanager.application.usecase.UpdateBookUseCase
import com.example.bookmanager.presentation.dto.book.CreateBookRequest
import com.example.bookmanager.presentation.dto.book.CreateBookResponse
import com.example.bookmanager.presentation.dto.book.UpdateBookRequest
import com.example.bookmanager.presentation.dto.book.UpdateBookResponse
import com.example.bookmanager.presentation.dto.common.ApiResponse.Success
import com.example.bookmanager.presentation.dto.common.Empty
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents

/**
 * 管理者向けの書籍登録・更新・削除を扱う Controller。
 */
class AdminBookController(
    private val createBookUseCase: CreateBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) {
    /**
     * 書籍を新規登録します。
     */
    suspend fun register(request: CreateBookRequest): Success<CreateBookResponse> {
        val input = CreateBookUseCase.CreateBookInput(
            title = request.title,
            author = request.author,
            outline = request.outline,
            publishedAtIsoDate = LocalDate.parse(request.publishedAt),
            isbn = request.isbn
        )
        val createBookResult = createBookUseCase.execute(input)
        return Success(CreateBookResponse(
            bookId = createBookResult.book.id,
            name = createBookResult.book.name,
            outline = createBookResult.book.outline,
            publishedAt = createBookResult.book.publishedAt.format(LocalDate.Formats.ISO),
            author = createBookResult.book.author,
            depositedAt = createBookResult.book.depositedAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
            isbn = createBookResult.book.isbn,
        ))
    }

    /**
     * 既存の書籍情報を更新します。
     */
    suspend fun update(bookId: Int, request: UpdateBookRequest): Success<UpdateBookResponse> {
        val input = UpdateBookUseCase.UpdateBookInput(
            bookId = bookId,
            title = request.title,
            author = request.author,
            outline = request.outline,
            publishedAtIsoDate = LocalDate.parse(request.publishedAt)
        )
        val result = updateBookUseCase.execute(input)
        return Success(
            UpdateBookResponse(
                bookId = result.book.id,
                name = result.book.name,
                outline = result.book.outline,
                publishedAt = result.book.publishedAt.format(LocalDate.Formats.ISO),
                author = result.book.author,
                depositedAt = result.book.depositedAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                isbn = result.book.isbn,
            )
        )
    }

    /**
     * 書籍を削除します。
     */
    suspend fun delete(bookId: Int): Success<Empty> {
        deleteBookUseCase.execute(bookId)
        return Success(Empty)
    }
}