package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.common.ApiRequestBody
import com.example.bookmanager.protocol.contract.common.ApiResponse
import com.example.bookmanager.protocol.contract.common.ApiResponseDataField
import com.example.bookmanager.protocol.contract.jsonProcessor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

sealed interface HttpMethod {
    abstract class GET<U : HttpRequestUrl, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequest<U, O> {
        override suspend fun execute(url: U, jwt: String?): ApiResponse<O> {
            val response = httpClient.get(urlString = url.buildUrl()) {
                headers {
                    header(HttpHeaders.Authorization, "JWT $jwt")
                }
            }
            return if (response.status.isSuccess()) {
                jsonProcessor.decodeFromString(
                    ApiResponse.Success.serializer(responseSerializer),
                    response.bodyAsText(),
                )
            } else {
                response.body<ApiResponse.Failure>()
            }
        }
    }

    abstract class POST<U : HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, B, O> {
        override suspend fun execute(url: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.post(urlString = url.buildUrl()) {
                headers {
                    header(HttpHeaders.Authorization, "JWT $jwt")
                }
                setBody(body = body, TypeInfo(bodyKlazz))
            }
            return if (response.status.isSuccess()) {
                jsonProcessor.decodeFromString(
                    ApiResponse.Success.serializer(responseSerializer),
                    response.bodyAsText(),
                )
            } else {
                response.body<ApiResponse.Failure>()
            }
        }
    }

    abstract class PUT<U : HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, B, O> {
        override suspend fun execute(url: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.put(urlString = url.buildUrl()) {
                headers {
                    header(HttpHeaders.Authorization, "JWT $jwt")
                }
                setBody(body = body, TypeInfo(bodyKlazz))
            }
            return if (response.status.isSuccess()) {
                jsonProcessor.decodeFromString(
                    ApiResponse.Success.serializer(responseSerializer),
                    response.bodyAsText(),
                )
            } else {
                response.body<ApiResponse.Failure>()
            }
        }
    }

    abstract class DELETE<U : HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, B, O> {
        override suspend fun execute(url: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.delete(urlString = url.buildUrl()) {
                headers {
                    header(HttpHeaders.Authorization, "JWT $jwt")
                }
                setBody(body = body, TypeInfo(bodyKlazz))
            }
            return if (response.status.isSuccess()) {
                jsonProcessor.decodeFromString(
                    ApiResponse.Success.serializer(responseSerializer),
                    response.bodyAsText(),
                )
            } else {
                response.body<ApiResponse.Failure>()
            }
        }
    }
}
