package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.details.ApiRequestBody
import com.example.bookmanager.protocol.contract.details.ApiResponse
import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import io.ktor.client.*
import kotlin.reflect.KClass

interface HttpRequestUrl {
    fun buildUrl(): String
}

interface HttpRequestParams {
    fun buildParams(): Map<String, String> = emptyMap()
}

interface HttpRequest<U : HttpRequestUrl, P : HttpRequestParams, O : ApiResponseDataField> {
    val httpClient: HttpClient

    suspend fun execute(
        url: U,
        params: P,
    ): ApiResponse<O>
}

interface HttpRequestHavingBody<U : HttpRequestUrl, P : HttpRequestParams, B : ApiRequestBody, O : ApiResponseDataField> {
    val httpClient: HttpClient
    val bodyKlazz: KClass<B>

    suspend fun execute(
        url: U,
        params: P,
        body: B
    ): ApiResponse<O>
}