package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.book.UpdateBookRequest
import com.example.bookmanager.protocol.contract.dto.book.UpdateBookResponse
import io.ktor.client.*

class UpdateBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.PUT<UpdateBook.UpdateBookDetailRequestUrl, UpdateBook.UpdateBookDetailRequestParams, UpdateBookRequest, UpdateBookResponse>(
    httpClient = httpClient,
    bodyKlazz = UpdateBookRequest::class,
    responseSerializer = UpdateBookResponse.serializer(),
) {
    data class UpdateBookDetailRequestUrl(
        val bookId: Int
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$bookId"
    }

    data object UpdateBookDetailRequestParams : HttpRequestParams
}
