package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.dto.account.CreateAccountRequest
import com.example.bookmanager.protocol.contract.dto.account.CreateAccountResponse
import com.example.bookmanager.protocol.contract.route.AccountRoute
import io.ktor.client.*

// POST /account: アカウント登録
class SignUp internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<
    AccountRoute,
    CreateAccountRequest,
    CreateAccountResponse
>(
    httpClient = httpClient,
    bodyKlazz = CreateAccountRequest::class,
    responseSerializer = CreateAccountResponse.serializer(),
    urlSerializer = AccountRoute.serializer()
)
