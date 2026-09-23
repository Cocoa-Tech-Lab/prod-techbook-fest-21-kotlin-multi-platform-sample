package com.example.bookmanager.protocol.contract.route

import io.ktor.resources.Resource


@Resource("/account")
class AccountRoute{
    /** /account/signin: サインイン（トークン発行） */
    @Resource("signin")
    class SignIn(
        val parent: AccountRoute = AccountRoute()
    )
    /** /account/rental_list: 自分のレンタル履歴（要認証） */
    @Resource("rental_list")
    class RentalList(
        val parent: AccountRoute = AccountRoute()
    )
}