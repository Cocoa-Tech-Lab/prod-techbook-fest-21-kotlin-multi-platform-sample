package com.example.bookmanager.presentation.routing

import com.example.bookmanager.domain.model.account.AccountLevel
import com.example.bookmanager.presentation.controller.AdminBookController
import com.example.bookmanager.presentation.controller.BookController
import com.example.bookmanager.presentation.dto.book.CreateBookRequest
import com.example.bookmanager.presentation.dto.book.UpdateBookRequest
import com.example.bookmanager.presentation.plugin.accountLevelAuthorization
import com.example.bookmanager.presentation.security.AppPrincipal
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.Application
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import kotlin.uuid.Uuid

/**
 * 書籍関連のエンドポイントをまとめた Router。
 *
 * 認証必須の利用者向けAPI（一覧/詳細/貸出/返却）と、
 * 管理者権限が必要なAPI（登録/更新/削除）を分けて定義しています。
 */
class BookRouter(
    private val bookController: BookController,
    private val adminBookController: AdminBookController,
): Router{
    /**
     * /books 配下の Resource 階層。
     */
    @Resource("/books")
    class BookRoute{
        /** /books/{bookId} */
        @Resource("{bookId}")
        class BookDetail(
            val parent: BookRoute = BookRoute(),
            val bookId: Int
        ){
            /** /books/{bookId}/rent */
            @Resource("rent")
            class Rent(val parent: BookDetail)
        }
    }

    /** /rental/{rentId}/return - 貸出IDでの返却 */
    @Resource("/rental/{rentId}/return")
    class ReturnRental(val rentId: Uuid)

    override fun installRouting(app: Application){
        // 利用者向けAPI（要認証）
        app.routing {
            authenticate {
                // GET /books: 書籍一覧
                get<BookRoute> {
                    val response = bookController.list()
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }
                // GET /books/{bookId}: 書籍詳細
                get<BookRoute.BookDetail> { req ->
                    val response = bookController.detail(req.bookId)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }

                // POST /books/{bookId}/rent: 書籍を借りる（ログインユーザ）
                post<BookRoute.BookDetail.Rent> { req ->
                    val principal = call.principal<AppPrincipal>()!!
                    val response = bookController.rent(
                        req.parent.bookId,
                        principal.accountId.value
                    )
                    call.respond(
                        status = HttpStatusCode.Created,
                        message = response
                    )
                }

                // POST /rental/{rentId}/return: 借りた本を返す
                post<ReturnRental> { req ->
                    val response = bookController.returnByRentId(req.rentId)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }
            }
        }

        // 管理者向けAPI（要認証 + アカウントレベル=Admin）
        app.routing {
            authenticate {
                install(accountLevelAuthorization){
                    require(AccountLevel.Admin)
                }
                // POST /books: 書籍登録
                post<BookRoute> {
                    val req = call.receive<CreateBookRequest>()
                    val response = adminBookController.register(req)
                    call.respond(
                        status = HttpStatusCode.Created,
                        message = response
                    )
                }
                // PUT /books/{bookId}: 書籍更新
                put<BookRoute.BookDetail> { req ->
                    val body = call.receive<UpdateBookRequest>()
                    val response = adminBookController.update(req.bookId, body)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }
                // DELETE /books/{bookId}: 書籍削除
                delete<BookRoute.BookDetail> { req ->
                    val response = adminBookController.delete(req.bookId)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = response
                    )
                }
            }
        }
    }
}
