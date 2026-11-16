package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.common.Empty
import com.example.bookmanager.protocol.contract.common.EmptyRequestBody
import io.ktor.client.*

class DeleteBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.DELETE<DeleteBook.UpdateBookDetailRequestUrl, EmptyRequestBody, Empty>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
    responseSerializer = Empty.serializer(),
) {
    data class UpdateBookDetailRequestUrl(
        val bookId: Int
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$bookId"
    }
}
