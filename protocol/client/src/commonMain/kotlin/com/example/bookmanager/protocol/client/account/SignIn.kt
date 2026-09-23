package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.account.SignInRequest
import com.example.bookmanager.protocol.contract.dto.account.SignInResponse
import io.ktor.client.*

// POST /account/signin: サインインしてJWTを取得
class SignIn internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<SignIn.SignInRequestUrl, SignIn.SignInRequestParam, SignInRequest, SignInResponse>(
    httpClient = httpClient,
    bodyKlazz = SignInRequest::class,
) {
    data object SignInRequestUrl : HttpRequestUrl {
        override fun buildUrl() = "/account/signin"
    }

    data object SignInRequestParam : HttpRequestParams
}