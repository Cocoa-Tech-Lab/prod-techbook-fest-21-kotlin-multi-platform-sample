package com.example.bookmanager.domain.port

import com.example.bookmanager.domain.model.book.BookEntity
import com.example.bookmanager.domain.model.book.BookWithStatus
import com.example.bookmanager.domain.model.common.BookId

/**
 * 書籍のリポジトリ（ドメインポート）。
 *
 * 役割:
 * - 書籍の読み取り用ユースケースに必要な最小限の操作を提供します。
 * - トランザクション境界は UseCase が保持し、呼び出しはその内側で行う想定です。
 *
 * 注意:
 * - ページングや検索条件は今後の要件で拡張予定です（現状は全件取得）。
 * - 実装はインフラ層（Exposed など）に委譲し、ここでは抽象化のみを行います。
 */
interface BookRepository {
    /**
     * 全件を取得します。並び順は実装側の規約（デフォルトはID昇順/蔵書日降順など）に従います。
     * 返却値には書籍の貸出状態（Borrowed/Returned）を含みます。
     */
    fun findAll(): List<BookWithStatus>

    /**
     * 指定IDの書籍を取得します。存在しない場合は [com.example.bookmanager.domain.exception.BookDoesNotFound] を送出します。
     * 貸出状態も合わせて返します。
     */
    fun findById(id: BookId): BookWithStatus?

    /**
     * 書籍を登録します。戻り値は採番されたIDを返します。
     */
    fun register(book: BookEntity.New): BookEntity.Persisted

    /**
     * 書籍情報を更新します（ID/ISBN は更新しません）。存在しない場合は例外を送出します。
     */
    fun update(
        book: BookEntity.Persisted
    ): BookEntity.Persisted?

    /**
     * 書籍を削除します。存在しない場合は例外を送出します。
     */
    fun delete(id: BookId): Int
}
