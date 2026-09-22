package com.example.bookmanager.application.exception

class BookNotFound(bookId: Int): Throwable("Book(id=$bookId) is not found")