package com.example.bookmanager.protocol.client

import com.example.bookmanager.protocol.contract.HttpRequestUrl
import com.example.bookmanager.protocol.contract.common.ApiRequestBody
import com.example.bookmanager.protocol.contract.common.ApiResponse
import com.example.bookmanager.protocol.contract.common.ApiResponseDataField
import com.example.bookmanager.protocol.contract.jsonProcessor
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.resources.href
import io.ktor.resources.serialization.ResourcesFormat
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

sealed interface HttpMethod {
    abstract class GET<U: HttpRequestUrl, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val responseSerializer: KSerializer<O>,
        override val urlSerializer: KSerializer<U>,
    ) : HttpRequest<U, O>, HttpMethod {
        val resourcesFormat = ResourcesFormat()
        override suspend fun execute(resource: U, jwt: String?): ApiResponse<O> {
            val response = httpClient.get{
                href(
                    resourcesFormat = resourcesFormat,
                    serializer = urlSerializer,
                    resource = resource,
                    urlBuilder = url
                )
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

    abstract class POST<U: HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
        override val urlSerializer: KSerializer<U>,
    ) : HttpRequestHavingBody<U, B, O> {
        val resourcesFormat = ResourcesFormat()
        override suspend fun execute(resource: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.post {
                href(
                    resourcesFormat = resourcesFormat,
                    serializer = urlSerializer,
                    resource = resource,
                    urlBuilder = url
                )
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

    abstract class PUT<U: HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
        override val urlSerializer: KSerializer<U>,
    ) : HttpRequestHavingBody<U, B, O> {
        val resourcesFormat = ResourcesFormat()
        override suspend fun execute(resource: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.put {
                href(
                    resourcesFormat = resourcesFormat,
                    serializer = urlSerializer,
                    resource = resource,
                    urlBuilder = url
                )
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
    abstract class DELETE<U: HttpRequestUrl, B : ApiRequestBody, O : ApiResponseDataField>(
        override val httpClient: HttpClient,
        override val bodyKlazz: KClass<B>,
        override val responseSerializer: KSerializer<O>,
        override val urlSerializer: KSerializer<U>,
    ) : HttpRequestHavingBody<U, B, O> {
        val resourcesFormat = ResourcesFormat()
        override suspend fun execute(resource: U, body: B, jwt: String?): ApiResponse<O> {
            val response = httpClient.delete {
                href(
                    resourcesFormat = resourcesFormat,
                    serializer = urlSerializer,
                    resource = resource,
                    urlBuilder = url
                )
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
