package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.details.EmptyRequestBody
import com.example.bookmanager.protocol.contract.dto.book.ReturnBookResponse
import io.ktor.client.*

class ReturnBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<ReturnBook.ReturnBookRequestUrl, ReturnBook.ReturnBookRequestParams, EmptyRequestBody, ReturnBookResponse>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class
) {
    data class ReturnBookRequestUrl(
        val rentId: String,
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$rentId/return"
    }

    data object ReturnBookRequestParams : HttpRequestParams
}