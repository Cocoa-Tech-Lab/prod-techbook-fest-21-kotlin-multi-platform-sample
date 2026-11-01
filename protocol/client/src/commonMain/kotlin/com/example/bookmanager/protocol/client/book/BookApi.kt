package com.example.bookmanager.protocol.client.book

import io.ktor.client.*

class BookApi internal constructor(
    httpClient: HttpClient,
) {
    val createBook by lazy { CreateBook(httpClient) }
    val deleteBook by lazy { DeleteBook(httpClient) }
    val updateBook by lazy { UpdateBook(httpClient) }
    val listBook by lazy { GetBookList(httpClient) }
    val getBookDetail by lazy { GetBookDetail(httpClient) }
    val rentBook by lazy { RentBook(httpClient) }
    val returnBook by lazy { ReturnBook(httpClient) }
}