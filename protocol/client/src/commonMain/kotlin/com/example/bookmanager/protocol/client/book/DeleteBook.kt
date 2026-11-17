package com.example.bookmanager.protocol.client.book

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.route.BookRoute
import com.example.bookmanager.protocol.contract.common.Empty
import com.example.bookmanager.protocol.contract.common.EmptyRequestBody
import io.ktor.client.*

class DeleteBook internal constructor(
    httpClient: HttpClient,
) : HttpMethod.DELETE<BookRoute.BookDetail, EmptyRequestBody, Empty>(
    httpClient = httpClient,
    bodyKlazz = EmptyRequestBody::class,
    responseSerializer = Empty.serializer(),
    urlSerializer = BookRoute.BookDetail.serializer(),
)
