package com.example.bookmanager.protocol.contract.route

import com.example.bookmanager.protocol.contract.HttpRequestUrl
import io.ktor.resources.*


@Resource("/account")
class AccountRoute: HttpRequestUrl{
    /** /account/signin: サインイン（トークン発行） */
    @Resource("signin")
    class SignIn(
        val parent: AccountRoute = AccountRoute()
    ): HttpRequestUrl
    /** /account/rental_list: 自分のレンタル履歴（要認証） */
    @Resource("rental_list")
    class RentalList(
        val parent: AccountRoute = AccountRoute()
    ): HttpRequestUrl
}