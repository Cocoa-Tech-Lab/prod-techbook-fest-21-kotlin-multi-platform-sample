package com.example.bookmanager.domain.model.book

import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.Isbn
import com.example.bookmanager.domain.model.common.RentStatus
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface BookEntity {
    val bookId: BookId?
    val title: String
    val isbn: Isbn
    val outline: String
    val author: String
    @OptIn(ExperimentalTime::class)
    val publishedAt: LocalDate

    @OptIn(ExperimentalTime::class)
    data class New(
        override val title: String,
        override val isbn: Isbn,
        override val outline: String,
        override val author: String,
        override val publishedAt: LocalDate
    ): BookEntity{
        override val bookId: BookId? = null
    }


    @OptIn(ExperimentalTime::class)
    data class Persisted(
        override val bookId: BookId,
        override val title: String,
        override val isbn: Isbn,
        override val outline: String,
        override val author: String,
        override val publishedAt: LocalDate,
        val depositedAt: Instant,
    ): BookEntity
}


@OptIn(ExperimentalTime::class)
data class BookWithStatus(
    val book: BookEntity.Persisted,
    val rentStatus: RentStatus
)