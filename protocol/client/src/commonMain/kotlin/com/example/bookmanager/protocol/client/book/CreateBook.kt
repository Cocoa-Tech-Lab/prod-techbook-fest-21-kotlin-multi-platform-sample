package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.dto.book.CreateBookRequest
import com.example.bookmanager.protocol.contract.dto.book.CreateBookResponse
import com.example.bookmanager.protocol.contract.route.BookRoute
import io.ktor.client.*

class CreateBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<BookRoute, CreateBookRequest, CreateBookResponse>(
    httpClient = httpClient,
    bodyKlazz = CreateBookRequest::class,
    responseSerializer = CreateBookResponse.serializer(),
    urlSerializer = BookRoute.serializer()
)
