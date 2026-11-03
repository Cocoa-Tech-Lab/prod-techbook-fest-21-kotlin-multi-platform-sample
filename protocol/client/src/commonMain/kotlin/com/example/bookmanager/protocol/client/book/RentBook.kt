package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.BookRoute
import com.example.bookmanager.protocol.contract.common.EmptyRequestBody
import com.example.bookmanager.protocol.contract.dto.book.RentBookResponse
import io.ktor.client.*

class RentBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<BookRoute.BookDetail.Rent, EmptyRequestBody, RentBookResponse>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
    responseSerializer = RentBookResponse.serializer(),
    urlSerializer = BookRoute.BookDetail.Rent.serializer()
)