package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.book.CreateBookRequest
import com.example.bookmanager.protocol.contract.dto.book.CreateBookResponse
import io.ktor.client.*

class CreateBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<CreateBook.CreateBookListRequestUrl, CreateBook.CreateBookListRequestParams, CreateBookRequest, CreateBookResponse>(
    httpClient = httpClient,
    bodyKlazz = CreateBookRequest::class,
) {
    data object CreateBookListRequestUrl : HttpRequestUrl {
        override fun buildUrl(): String = "/books"
    }

    data object CreateBookListRequestParams : HttpRequestParams
}