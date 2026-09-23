package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.client.account.AccountApi
import com.example.bookmanager.protocol.client.book.BookApi
import io.ktor.client.*
import io.ktor.client.plugins.*

class BookManagerApi(
    baseUrl: String,
    httpClient: HttpClient = createHttpClient()
) {
    private val client = httpClient.config {
        defaultRequest {
            url(baseUrl.trimEnd('/') + "/")
        }
    }

    val account by lazy { AccountApi(client) }
    val book by lazy { BookApi(client) }
}