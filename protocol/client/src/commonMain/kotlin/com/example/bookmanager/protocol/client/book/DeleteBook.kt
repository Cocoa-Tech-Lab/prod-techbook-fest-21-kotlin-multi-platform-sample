package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.details.Empty
import com.example.bookmanager.protocol.contract.details.EmptyRequestBody
import io.ktor.client.*

class DeleteBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.DELETE<DeleteBook.UpdateBookDetailRequestUrl, DeleteBook.UpdateBookDetailRequestParams, EmptyRequestBody, Empty>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
) {
    data class UpdateBookDetailRequestUrl(
        val bookId: Int
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$bookId"
    }

    data object UpdateBookDetailRequestParams : HttpRequestParams
}