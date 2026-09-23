package com.example.bookmanager.protocol.client.account

import com.example.bookmanager.protocol.client.HttpMethod
import com.example.bookmanager.protocol.client.HttpRequestParams
import com.example.bookmanager.protocol.client.HttpRequestUrl
import com.example.bookmanager.protocol.contract.dto.account.RentalListResponse
import io.ktor.client.*

// GET /account/rental_list: 自分のレンタル履歴（要認証）
class RentalList internal constructor(
    httpClient: HttpClient,
) : HttpMethod.GET<RentalList.RentalListUrl, RentalList.RentalListParams, RentalListResponse>(
    httpClient,
) {
    data object RentalListUrl : HttpRequestUrl {
        override fun buildUrl(): String = "/account/rental_list"
    }

    data object RentalListParams : HttpRequestParams
}