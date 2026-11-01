package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.details.ApiRequestBody
import com.example.bookmanager.protocol.contract.details.ApiResponse
import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import io.ktor.client.*
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

interface HttpRequestUrl {
    fun buildUrl(): String
}

interface HttpRequest<U : HttpRequestUrl, O : ApiResponseDataField> {
    val httpClient: HttpClient
    val responseSerializer: KSerializer<O>

    suspend fun execute(
        url: U,
        jwt: String?,
    ): ApiResponse<O>
}

interface HttpRequestHavingBody<U : HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField> {
    val httpClient: HttpClient
    val bodyKlazz: KClass<B>
    val responseSerializer: KSerializer<O>

    suspend fun execute(
        url: U,
        body: B,
        jwt: String?,
    ): ApiResponse<O>
}
