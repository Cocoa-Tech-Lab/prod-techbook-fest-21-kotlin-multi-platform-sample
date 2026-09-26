package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.jsonProcessor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

actual fun createHttpClient() = HttpClient(OkHttp) {
    install(Resources)
    install(ContentNegotiation) {
        json(json = jsonProcessor)
    }

    install(ContentEncoding) {
        deflate(1.0F)
        gzip(0.9F)
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}
