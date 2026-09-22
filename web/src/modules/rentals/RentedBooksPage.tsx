import React, { useEffect, useState } from 'react'
import { accountApi } from '../../lib/api/account'
import { booksApi } from '../../lib/api/books'
import type { RentalHistoryItem } from '../../lib/types/account'
import { formatIsoDateTimeToSlash } from '../../lib/format'

export function RentedBooksPage() {
  const [items, setItems] = useState<RentalHistoryItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [returningId, setReturningId] = useState<string | null>(null)
  const [actionError, setActionError] = useState<string | null>(null)

  useEffect(() => {
    let active = true
    setLoading(true)
    setError(null)
    accountApi
      .rentalList()
      .then((res) => {
        if (!active) return
        setItems(res.items)
      })
      .catch((e: unknown) => {
        if (!active) return
        setError(e instanceof Error ? e.message : '読み込みに失敗しました')
      })
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [])

  async function onReturn(rentId: string) {
    setActionError(null)
    setReturningId(rentId)
    try {
      await booksApi.returnRental(rentId)
      // この画面は未返却のみの一覧のため、返却後は行を削除
      setItems((prev)=>{
          const idx = prev.findIndex(it => it.rentId === rentId)
          prev[idx].status = 'Returned'
          return prev
      })
    } catch (e) {
      setActionError(e instanceof Error ? e.message : '返却に失敗しました')
    } finally {
      setReturningId(null)
    }
  }

  return (
    <div>
      {loading && <p>読み込み中...</p>}
      {error && <p style={{ color: 'crimson' }}>エラー: {error}</p>}
      {actionError && <p style={{ color: 'crimson' }}>エラー: {actionError}</p>}
      {!loading && !error && (
        <div style={{ display: 'grid', gap: 8 }}>
          {items.length === 0 && <p>現在、借りている書籍はありません。</p>}
          {items.length > 0 && (
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr>
                  <th style={th}>レンタルID</th>
                  <th style={th}>書籍ID</th>
                  <th style={th}>貸出日時</th>
                  <th style={th}>返却期限</th>
                  <th style={th}>操作</th>
                </tr>
              </thead>
              <tbody>
                {items.map((it) => (
                  <tr key={it.rentId}>
                    <td style={td}>{it.rentId}</td>
                    <td style={td}>{it.bookId}</td>
                    <td style={td}>{formatIsoDateTimeToSlash(it.rentalAt)}</td>
                    <td style={td}>{formatIsoDateTimeToSlash(it.returnDeadline)}</td>
                    <td style={td}>
                        {
                            it.status !== 'Returned' && (
                                <button
                                    onClick={() => onReturn(it.rentId)}
                                    disabled={returningId === it.rentId}
                                    style={{ backgroundColor: '#333', color: 'white' }}
                                >
                                    {returningId === it.rentId ? '返却中...' : '返却'}
                                </button>
                            )
                        }
                        {
                            it.status === 'Returned' && (
                                <p>返却済</p>
                            )
                        }
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  )
}

const th: React.CSSProperties = { textAlign: 'left', borderBottom: '1px solid #ddd', padding: 8 }
const td: React.CSSProperties = { borderBottom: '1px solid #eee', padding: 8 }
