package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.ReturnRental
import com.example.bookmanager.protocol.contract.details.EmptyRequestBody
import com.example.bookmanager.protocol.contract.dto.book.ReturnBookResponse
import io.ktor.client.*

class ReturnBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<ReturnRental, EmptyRequestBody, ReturnBookResponse>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
    responseSerializer = ReturnBookResponse.serializer(),
    urlSerializer = ReturnRental.serializer()
)