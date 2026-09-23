package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.details.EmptyRequestBody
import com.example.bookmanager.protocol.contract.dto.book.RentBookResponse
import io.ktor.client.*

class RentBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<RentBook.RentBookRequestUrl, RentBook.RentBookRequestParams, EmptyRequestBody, RentBookResponse>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class
) {
    data class RentBookRequestUrl(
        val bookId: String,
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$bookId/rent"
    }

    data object RentBookRequestParams : HttpRequestParams
}