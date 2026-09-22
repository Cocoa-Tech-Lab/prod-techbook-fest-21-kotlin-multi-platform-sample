package com.example.bookmanager.presentation.plugin

import com.example.bookmanager.domain.model.account.AccountLevel
import com.example.bookmanager.presentation.security.AppPrincipal
import com.example.bookmanager.presentation.dto.common.ApiResponse
import com.example.bookmanager.presentation.dto.common.ErrorInfo
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlin.collections.plusAssign

/**
 * アカウントレベル（権限）でルートを制御するための Route スコーププラグイン。
 *
 * 使用例:
 * authenticate {
 *   install(accountLevelAuthorization) {
 *     require(AccountLevel.Admin)
 *   }
 *   // ここに Admin のみ許可したいルートを定義
 * }
 */
class AccountLevelAuthorizePluginConfig{
    // 許可したいアカウントレベル（複数可）
    internal val required = linkedSetOf<AccountLevel>()
    fun require(vararg level: AccountLevel) { required += level }
}

// ルートにスコープして適用するプラグイン定義
val accountLevelAuthorization = createRouteScopedPlugin(
    name = "AuthorizePlugin",
    ::AccountLevelAuthorizePluginConfig
){
    val config = pluginConfig
    // すでに authenticate によって認証チェックが走った直後のフェーズで評価する
    on(AuthenticationChecked) { call ->
        // Principal が無い＝未認証
        val p = call.principal<AppPrincipal>() ?: run {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = ApiResponse.Failure(ErrorInfo(401, "Unauthorized"))
            )
            return@on
        }
        // 要求される権限に含まれていればアクセス許可
        if (config.required.contains(p.accountLevel)) return@on
        // それ以外は 403
        call.respond(
            status = HttpStatusCode.Forbidden,
            message = ApiResponse.Failure(ErrorInfo(403, "Forbidden"))
        )
        return@on
    }
}