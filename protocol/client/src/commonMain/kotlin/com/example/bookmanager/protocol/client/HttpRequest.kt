package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.HttpRequestUrl
import com.example.bookmanager.protocol.contract.common.ApiRequestBody
import com.example.bookmanager.protocol.contract.common.ApiResponse
import com.example.bookmanager.protocol.contract.common.ApiResponseDataField
import io.ktor.client.*
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

interface HttpRequest<U : HttpRequestUrl, O : ApiResponseDataField> {
    val httpClient: HttpClient
    val responseSerializer: KSerializer<O>
    val urlSerializer: KSerializer<U>

    suspend fun execute(
        resource: U,
        jwt: String?,
    ): ApiResponse<O>
}

interface HttpRequestHavingBody<U : HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField> {
    val httpClient: HttpClient
    val bodyKlazz: KClass<B>
    val responseSerializer: KSerializer<O>
    val urlSerializer: KSerializer<U>

    suspend fun execute(
        resource: U,
        body: B,
        jwt: String?,
    ): ApiResponse<O>
}
