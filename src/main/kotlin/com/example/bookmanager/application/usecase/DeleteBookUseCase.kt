package com.example.bookmanager.application.usecase

import com.example.bookmanager.application.port.TransactionManager
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.port.BookRepository

/**
 * 書籍削除ユースケース。
 *
 * - 指定IDの書籍を削除します。存在しない場合はエラーを投げます。
 * - トランザクション境界は本ユースケースが保持します。
 */
class DeleteBookUseCase(
    private val txManager: TransactionManager,
    private val bookRepository: BookRepository
) {
    /**
     * 書籍を削除します。
     * @param bookId 削除対象の書籍ID
     * @throws IllegalStateException 対象が存在しない場合
     */
    suspend fun execute(bookId: Int) = txManager.inTransaction {
        val deleted = bookRepository.delete(BookId(bookId))
        if (deleted == 0) error("book ${bookId} doesn't exist!")
    }
}
