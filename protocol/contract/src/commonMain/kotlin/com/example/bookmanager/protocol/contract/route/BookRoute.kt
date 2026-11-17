package com.example.bookmanager.protocol.contract.route

import com.example.bookmanager.protocol.contract.HttpRequestUrl
import io.ktor.resources.*
import kotlin.uuid.Uuid

/**
 * /books 配下の Resource 階層。
 */
@Resource("/books")
class BookRoute: HttpRequestUrl{
    /** /books/{bookId} */
    @Resource("{bookId}")
    class BookDetail(
        val parent: BookRoute = BookRoute(),
        val bookId: Int
    ): HttpRequestUrl{
        /** /books/{bookId}/rent */
        @Resource("rent")
        class Rent(val parent: BookDetail): HttpRequestUrl{}
    }
}

/** /rental/{rentId}/return - 貸出IDでの返却 */
@Resource("/rental/{rentId}/return")
class ReturnRental(val rentId: Uuid): HttpRequestUrl{}