package com.example.bookmanager.protocol.contract.route

import io.ktor.resources.Resource
import kotlin.uuid.Uuid

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