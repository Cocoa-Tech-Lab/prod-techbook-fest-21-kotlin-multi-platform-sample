package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.account.CreateAccountRequest
import com.example.bookmanager.protocol.contract.dto.account.CreateAccountResponse
import io.ktor.client.*

// POST /account: アカウント登録
class SignUp internal constructor(
    httpClient: HttpClient,
) : HttpMethod.POST<
        SignUp.SignUpRequestUrl,
        SignUp.SignUpRequestParam,
        CreateAccountRequest,
        CreateAccountResponse
        >(
    httpClient = httpClient,
    bodyKlazz = CreateAccountRequest::class,
) {
    data object SignUpRequestUrl : HttpRequestUrl {
        override fun buildUrl() = "/account"
    }

    data object SignUpRequestParam : HttpRequestParams

}