package com.example.bookmanager.core

import com.auth0.jwt.algorithms.Algorithm
import com.example.bookmanager.application.exception.BookNotFound
import com.example.bookmanager.presentation.security.AppPrincipal
import com.example.bookmanager.infra.config.DataSourceConfig
import com.example.bookmanager.infra.config.JwtConfig
import com.example.bookmanager.infra.di.*
import com.example.bookmanager.presentation.dto.common.ApiResponse
import com.example.bookmanager.presentation.dto.common.ErrorInfo
import com.example.bookmanager.presentation.routing.Router
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.di.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.*
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import org.flywaydb.core.Flyway
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.koin
import javax.sql.DataSource

/**
 * アプリケーションのメインモジュール定義。
 * - DI 設定（Koin / Ktor DI）
 * - CORS / JSON シリアライゼーション
 * - JWT 認証
 * - 例外ハンドリング（StatusPages）
 * - Flyway によるDBマイグレーション
 * - Resources + Router の組み立て
 *
 * 技術書典の読者が読みやすいように、処理の意図と流れをコメントで補足しています。
 */

fun Application.m() = module {
    // Ktor DI で DataSourceConfig を提供する最小例（テストやサンプル用）
    val dataSourceConfig: DataSourceConfig by dependencies
}

/**
 * 実行時に呼び出されるエントリポイント。
 * Application.mainModule() をインストールするとサーバの機能が有効になります。
 */
fun Application.mainModule() {
    // 1. Ktor DI で設定クラスを解決可能にする
    dependencies {
        provide(DataSourceConfig::class)
        provide(JwtConfig::class)
    }

    // DI 経由で設定値を取得
    val dataSourceConfig: DataSourceConfig by dependencies
    val jwtConfig: JwtConfig by dependencies

    // 2. Koin モジュールの登録（アプリ層〜プレゼン層の依存解決）
    install(Koin) {
        modules(
            dataSourceModule(dataSourceConfig), // DataSource/HikariCP 等
            authModule(jwtConfig),              // JWT 設定（署名鍵・発行者等）
            dataBaseModule(),                   // Exposed の Database 準備
            transactionModule(),                // トランザクション境界
            repositoryModule(),                 // Repository 実装
            useCaseModule(),                    // UseCase
            controllerModule(),                 // Controller
            routerModule()                      // Router（エンドポイント）
        )
    }

    // 3. CORS 設定（フロントSPAからのアクセスを許可）
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        // 開発用フロントのオリジンのみ許可
        allowOrigins { origin ->
            origin == "http://localhost:5173"
        }
    }

    // 4. JSON シリアライゼーション設定
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // 受信JSONに未知のキーがあっても無視
        })
    }

    // 5. Authentication (JWT)
    install(Authentication) {
        jwt {
            authSchemes("JWT") // Authorization: JWT <token>
            verifier(
                issuer = jwtConfig.issuer,
                audience = jwtConfig.audience,
                algorithm = Algorithm.HMAC256(jwtConfig.secret)
            )
            // トークンが妥当な場合にアプリ独自の Principal を生成
            validate { credential ->
                if (credential.payload.subject.isNullOrBlank()) null
                else AppPrincipal(JWTPrincipal(credential.payload))
            }
            // 未認証時に返す共通レスポンス
            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = ApiResponse.Failure(ErrorInfo(401, "Unauthorized"))
                )
            }
        }
    }

    // 6. 例外・エラーレスポンスの標準化
    install(StatusPages){
        // ドメイン固有エラー: 書籍が見つからない
        exception<BookNotFound> { call, cause ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ApiResponse.Failure(
                    ErrorInfo(
                        404,
                        cause.message ?: "Book Not Found"
                    )
                )
            )
        }

        // 想定外の例外は 500 で返却
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ApiResponse.Failure(
                    ErrorInfo(
                        500,
                        cause.message ?: "Internal Server Error"
                    )
                )
            )
        }

        // ルート未定義など NotFound のハンドリング
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status = status,
                message = ApiResponse.Failure(
                    ErrorInfo(
                        status.value,
                        status.description
                    )
                )
            )
        }
    }

    // 7. アプリ起動時に Flyway で自動マイグレーション
    val dataSource: DataSource by inject()
    Flyway.configure()
        .dataSource(dataSource)
        .cleanDisabled(false)
        .load()
        .migrate()

    // 8. Router を収集して Resources と合わせて Ktor に登録
    val router: List<Router> = koin().getAll<Router>()
    install(Resources)
    router.forEach { it.installRouting(this) }
}
