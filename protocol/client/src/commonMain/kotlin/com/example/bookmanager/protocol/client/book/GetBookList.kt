package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.BookRoute
import com.example.bookmanager.protocol.contract.dto.book.ListBookSummaryResponse
import io.ktor.client.*

class GetBookList internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<BookRoute, ListBookSummaryResponse>(
    httpClient = httpClient,
    responseSerializer = ListBookSummaryResponse.serializer(),
    urlSerializer = BookRoute.serializer()
)