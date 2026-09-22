package com.example.bookmanager.domain.port

import com.example.bookmanager.domain.model.common.AccountId
import com.example.bookmanager.domain.model.common.BookId
import com.example.bookmanager.domain.model.common.RentId
import com.example.bookmanager.domain.model.rental.RentalEntity
import kotlin.time.ExperimentalTime

/**
 * 貸出のリポジトリ（ドメインポート）。
 *
 * 役割:
 * - 書籍の貸出・返却・検索に関する最小限の操作を提供します。
 * - トランザクション境界は UseCase が保持し、本インタフェースは副作用の開始/終了を担いません。
 *
 * 注意:
 * - 未返却の判定は「returnedAt が null であること」を意味します（実装依存の列名に関する注釈）。
 * - 競合（同一書籍の重複貸出）は DB 層の部分ユニーク制約で防止します。必要に応じてアプリ層で事前検証してください。
 */
interface RentalRepository {
    /**
     * 書籍を貸し出します。
     *
     * 期待される挙動:
     * - 返却期限を指定して貸出レコードを作成します。
     * - 同一書籍について未返却レコードが既に存在する場合、実装（DB）側で一意制約違反が発生します。
     */
    fun rent(newRental: RentalEntity.New): RentalEntity.Persisted

    /**
     * 返却処理を行います。
     *
     * 期待される挙動:
     * - 指定IDの貸出の returnedAt を現在時刻で更新します。
     * - 既に返却済みであれば何も変更せず、そのままの状態で返します（実装側ポリシーに依存）。
     *
     * @param rentId 貸出ID
     * @return 更新後の貸出。存在しない場合は null
     */
    fun returnById(rentId: RentId): RentalEntity.Persisted?

    /**
     * 指定書籍の「未返却」の貸出を1件取得します。
     *
     * @param bookId 書籍ID
     * @return 見つかった場合は貸出、なければ null
     */
    fun findActiveByBook(bookId: BookId): RentalEntity.Persisted?

    /**
     * 指定ユーザの貸出一覧を取得します。
     *
     * @param userId ユーザID
     * @param activeOnly true の場合は未返却のみ、false の場合は全件
     * @return 貸出一覧（0件の場合は空リスト）
     */
    fun listByUser(userId: AccountId, activeOnly: Boolean = false): List<RentalEntity.Persisted>
}
