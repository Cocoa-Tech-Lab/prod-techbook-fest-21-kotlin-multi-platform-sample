package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.BookRoute
import com.example.bookmanager.protocol.contract.dto.book.UpdateBookRequest
import com.example.bookmanager.protocol.contract.dto.book.UpdateBookResponse
import io.ktor.client.*

class UpdateBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.PUT<BookRoute.BookDetail, UpdateBookRequest, UpdateBookResponse>(
    httpClient = httpClient,
    bodyKlazz = UpdateBookRequest::class,
    responseSerializer = UpdateBookResponse.serializer(),
    urlSerializer = BookRoute.BookDetail.serializer()
)