package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.book.ListBookSummaryResponse
import io.ktor.client.*

class GetBookList internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<GetBookList.GetBookListRequestUrl, GetBookList.GetBookListRequestParams, ListBookSummaryResponse>(
    httpClient = httpClient,
) {
    data object GetBookListRequestUrl : HttpRequestUrl {
        override fun buildUrl(): String = "/books"
    }

    data object GetBookListRequestParams : HttpRequestParams
}