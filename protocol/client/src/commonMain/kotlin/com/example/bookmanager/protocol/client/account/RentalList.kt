package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.contract.dto.account.RentalListResponse
import com.example.bookmanager.protocol.contract.route.AccountRoute
import io.ktor.client.*

// GET /account/rental_list: 自分のレンタル履歴（要認証）
class RentalList internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<AccountRoute.RentalList, RentalListResponse>(
    httpClient = httpClient,
    responseSerializer = RentalListResponse.serializer(),
    urlSerializer = AccountRoute.RentalList.serializer(),
)
