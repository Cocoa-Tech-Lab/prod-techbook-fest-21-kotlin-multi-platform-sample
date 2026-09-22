package com.example.bookmanager.presentation.routing

import com.example.bookmanager.presentation.controller.AccountController
import com.example.bookmanager.presentation.dto.account.CreateAccountRequest
import com.example.bookmanager.presentation.dto.account.SignInRequest
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import com.example.bookmanager.presentation.security.AppPrincipal

/**
 * アカウント作成・サインイン・レンタル履歴取得を提供する Router。
 */
class AccountRouter(
    private val accountController: AccountController,
): Router{
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

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    override fun installRouting(app: Application) {
        app.routing {
            // POST /account: アカウント登録
            post<AccountRoute> {
                val req = call.receive<CreateAccountRequest>()
                val response = accountController.register(req)
                call.respond(
                    status = HttpStatusCode.Created,
                    message = response
                )
            }

            // POST /account/signin: サインインしてJWTを取得
            post<AccountRoute.SignIn> {
                val req = call.receive<SignInRequest>()
                val response = accountController.signin(req)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = response
                )
            }

            // GET /account/rental_list: 自分のレンタル履歴（要認証）
            authenticate {
                get<AccountRoute.RentalList> {
                    val principal = call.principal<AppPrincipal>()!!
                    val response = accountController.rentalHistory(
                        principal.accountId.value
                    )
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }
            }
        }
    }
}
