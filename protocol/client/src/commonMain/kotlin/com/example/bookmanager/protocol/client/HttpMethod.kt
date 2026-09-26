package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.details.ApiRequestBody
import com.example.bookmanager.protocol.contract.details.ApiResponse
import com.example.bookmanager.protocol.contract.details.ApiResponseDataField
import com.example.bookmanager.protocol.contract.jsonProcessor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlin.reflect.KClass

sealed interface HttpMethod {
    abstract class GET<U : HttpRequestUrl, P : HttpRequestParams, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequest<U, P, O> {
        override suspend fun execute(url: U, params: P, jwt: String?): ApiResponse<O> {
            val response = httpClient.get(urlString = url.buildUrl()) {
                parameters {
                    params.buildParams().forEach(::parameter)
                }
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

    abstract class POST<U : HttpRequestUrl, P : HttpRequestParams, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, P, B, O> {
        override suspend fun execute(url: U, params: P, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.post(urlString = url.buildUrl()) {
                parameters {
                    params.buildParams().forEach(::parameter)
                }
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

    abstract class PUT<U : HttpRequestUrl, P : HttpRequestParams, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, P, B, O> {
        override suspend fun execute(url: U, params: P, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.put(urlString = url.buildUrl()) {
                parameters {
                    params.buildParams().forEach(::parameter)
                }
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

    abstract class DELETE<U : HttpRequestUrl, P : HttpRequestParams, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
    ) : HttpRequestHavingBody<U, P, B, O> {
        override suspend fun execute(url: U, params: P, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.delete(urlString = url.buildUrl()) {
                parameters {
                    params.buildParams().forEach(::parameter)
                }
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
