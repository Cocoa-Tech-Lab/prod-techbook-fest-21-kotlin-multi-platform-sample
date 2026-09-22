package com.example.bookmanager.application.port

/**
 * トランザクション管理のポート（アプリケーション層）。
 *
 * 設計方針:
 * - 「トランザクション境界は UseCase が持つ」ため、UseCase から本インターフェースを介して
 *   トランザクションを開始します（Repository 自体はトランザクションを開始/終了しない）。
 * - これにより、複数の Repository を横断するユースケースでも、1つの一貫したトランザクションにまとめられます。
 * - 実装はインフラ層（例: Exposed, JPA など）に依存させ、ここでは抽象化のみを提供します。
 *
 * 使い方の例（UseCase内）:
 * ```kotlin
 * class SomeUseCase(private val tx: TransactionManager, private val repo: FooRepository) {
 *   suspend fun run() = tx.inTransaction {
 *     val a = repo.load(...)
 *     repo.update(a)
 *     // 必要であれば他のリポジトリ呼び出しも同一トランザクション内で実行
 *   }
 * }
 * ```
 *
 * 注意事項:
 * - block は「トランザクション内で実行したい処理」を表します。
 * - block 内では I/O（DBアクセス）を行うため、呼び出し側（UseCase）は適切に
 *   スレッド/ディスパッチャを考慮してください（実装側で切り替える場合もあります）。
 */
interface TransactionManager {
    /**
     * 新規トランザクションを開始して [block] を実行します。
     * 例外がスローされた場合、実装側の規約に従いロールバックされます。
     */
    suspend fun <R> inTransaction(block: () -> R): R
}