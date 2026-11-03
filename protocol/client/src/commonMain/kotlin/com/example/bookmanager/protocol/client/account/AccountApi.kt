package com.example.bookmanager.protocol.client.account

import io.ktor.client.*

class AccountApi internal constructor(
    httpClient: HttpClient,
) {
    val signIn by lazy { SignIn(httpClient) }
    val signUp by lazy { SignUp(httpClient) }
    val rentalList by lazy { RentalList(httpClient) }
}