package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.book.BookDetailResponse
import io.ktor.client.*

class GetBookDetail internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<GetBookDetail.GetBookDetailRequestUrl, GetBookDetail.GetBookDetailRequestParams, BookDetailResponse>(
    httpClient = httpClient,
) {
    data class GetBookDetailRequestUrl(
        val bookId: Int
    ) : HttpRequestUrl {
        override fun buildUrl(): String = "/books/$bookId"
    }

    data object GetBookDetailRequestParams : HttpRequestParams
}