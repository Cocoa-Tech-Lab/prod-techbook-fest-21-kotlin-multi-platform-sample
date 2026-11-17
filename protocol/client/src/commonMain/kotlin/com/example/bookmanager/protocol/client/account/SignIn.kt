package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.dto.account.SignInRequest
import com.example.bookmanager.protocol.contract.dto.account.SignInResponse
import com.example.bookmanager.protocol.contract.route.AccountRoute
import io.ktor.client.*

// POST /account/signin: サインインしてJWTを取得
class SignIn internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<AccountRoute.SignIn, SignInRequest, SignInResponse>(
    httpClient = httpClient,
    bodyKlazz = SignInRequest::class,
    responseSerializer = SignInResponse.serializer(),
    urlSerializer = AccountRoute.SignIn.serializer()
)
