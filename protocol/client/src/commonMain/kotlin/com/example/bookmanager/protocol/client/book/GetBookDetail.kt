package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.BookRoute
import com.example.bookmanager.protocol.contract.dto.book.BookDetailResponse
import io.ktor.client.*

class GetBookDetail internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<BookRoute.BookDetail, BookDetailResponse>(
    httpClient = httpClient,
    responseSerializer = BookDetailResponse.serializer(),
    urlSerializer = BookRoute.BookDetail.serializer()
)
