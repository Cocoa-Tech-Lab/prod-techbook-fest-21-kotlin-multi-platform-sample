package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.common.EmptyRequestBody
import com.example.bookmanager.protocol.contract.dto.book.ReturnBookResponse
import io.ktor.client.*

class ReturnBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<ReturnBook.ReturnBookRequestUrl, EmptyRequestBody, ReturnBookResponse>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
    responseSerializer = ReturnBookResponse.serializer(),
) {
    data class ReturnBookRequestUrl(
        val rentId: String,
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/rental/$rentId/return"
    }
}
