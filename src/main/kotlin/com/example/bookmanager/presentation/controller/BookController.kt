package com.example.bookmanager.presentation.controller

import com.example.bookmanager.application.usecase.GetBookDetailUseCase
import com.example.bookmanager.application.usecase.ListBookUseCase
import com.example.bookmanager.application.usecase.RentBookUseCase
import com.example.bookmanager.application.usecase.ReturnBookUseCase
import com.example.bookmanager.presentation.dto.book.BookDetailResponse
import com.example.bookmanager.presentation.dto.book.BookSummary
import com.example.bookmanager.presentation.dto.book.ListBookSummaryResponse
import com.example.bookmanager.presentation.dto.book.RentBookResponse
import com.example.bookmanager.presentation.dto.book.ReturnBookResponse
import com.example.bookmanager.presentation.dto.common.ApiResponse.Success
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * 書籍の閲覧・貸出・返却を扱うプレゼン層の Controller。
 *
 * UseCase の入出力を API で返しやすい DTO へ整形して返します。
 */
class BookController(
    private val listBookUseCase: ListBookUseCase,
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val rentBookUseCase: RentBookUseCase,
    private val returnBookUseCase: ReturnBookUseCase,
) {
    /**
     * 書籍一覧を取得します。
     * @return 画面表示用に整形済みの一覧 DTO
     */
    suspend fun list(): Success<ListBookSummaryResponse>{
        val bookList: ListBookUseCase.ListBookResult = listBookUseCase.execute()

        return Success(ListBookSummaryResponse(books = bookList.books.map { (book, canRent) ->
            BookSummary(
                bookId = book.id,
                publishedAt = book.publishedAt.format(LocalDate.Formats.ISO),
                canRent = canRent,
                name = book.name,
            )
        }))
    }

    /**
     * 書籍の詳細を取得します。
     * @param bookId 書籍ID
     */
    @OptIn(ExperimentalTime::class)
    suspend fun detail(bookId: Int): Success<BookDetailResponse> {
        val found = getBookDetailUseCase.execute(bookId)
        return Success(
            BookDetailResponse(
                bookId = found.book.id,
                name = found.book.name,
                outline = found.book.outline,
                publishedAt = found.book.publishedAt.format(LocalDate.Formats.ISO),
                author = found.book.author,
                depositedAt = found.book.depositedAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                isbn = found.book.isbn,
                canRent = found.canRent,
            )
        )
    }

    /**
     * 書籍を借ります。
     * @param bookId 書籍ID
     * @param userId 借りるユーザのID
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun rent(bookId: Int, userId: Uuid): Success<RentBookResponse> {
        val result = rentBookUseCase.execute(
            RentBookUseCase.RentBookInput(
                bookId = bookId,
                userId = userId,
            )
        )
        return Success(
            RentBookResponse(
                rentId = result.rentId,
                bookId = result.bookId,
                userId = result.userId,
                rentalAt = result.rentalAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                returnDeadline = result.returnDeadline.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                status = result.status,
            )
        )
    }

    /**
     * 貸出IDを指定して返却を記録します。
     * @param rentId 貸出ID
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun returnByRentId(rentId: Uuid): Success<ReturnBookResponse> {
        val updated = returnBookUseCase.execute(
            ReturnBookUseCase.ReturnBookInput(rentId = rentId)
        )
        return Success(
            ReturnBookResponse(
                rentId = updated.rentId,
                bookId = updated.bookId,
                userId = updated.userId,
                rentalAt = updated.rentalAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                returnDeadline = updated.returnDeadline.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                returnedAt = updated.returnedAt?.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                status = updated.status,
            )
        )
    }
}