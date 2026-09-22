package com.example.bookmanager.infra.transaction

import com.example.bookmanager.application.port.TransactionManager
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Exposed による TransactionManager 実装。
 *
 * ポイント:
 * - 本プロジェクトでは「トランザクション境界は UseCase」が持つため、UseCase が
 *   [inTransaction] を呼び出して 1 ユースケース = 1 トランザクション を確立します。
 * - 実装は Exposed の transaction(db) を利用し、block が例外を投げた場合はロールバックされます。
 * - 既存トランザクション配下で再度呼ぶと、Exposed のネスト規約に従います（原則同一コネクション上で内側は独立コミットしません）。
 * - I/O はブロッキングになるため、必要に応じて呼び出し側で適切な Dispatcher（例: IO）に切り替えることを検討してください。
 *
 * 備考:
 * - Exposed には newSuspendedTransaction もありますが、ここではシンプルさを優先して
 *   明示的なスレッド切り替えは行っていません。要件に応じて置換可能です。
 */
class ExposedTransactionManager(
    private val db: Database
): TransactionManager {
    override suspend fun <R> inTransaction(block: () -> R): R {
        return transaction(db = db){
            block()
        }
    }
}