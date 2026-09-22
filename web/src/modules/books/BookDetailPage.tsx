import React, { useEffect, useState } from 'react'
import { booksApi } from '../../lib'
import type { BookDetailResponse } from '../../lib'

import { formatIsoDateTimeToSlash, formatIsoDateYmdToSlash } from '../../lib/format'

export function BookDetailPage({ bookId, onBack }: { bookId: number; onBack: () => void }) {
  const [data, setData] = useState<BookDetailResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleting, setDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [renting, setRenting] = useState(false)
  const [rentError, setRentError] = useState<string | null>(null)
  const [rentInfo, setRentInfo] = useState<string | null>(null)

  useEffect(() => {
    let active = true
    setLoading(true)
    setError(null)
    booksApi
      .detail(bookId)
      .then((d) => {
        if (!active) return
        setData(d)
      })
      .catch((e: unknown) => {
        if (!active) return
        setError(e instanceof Error ? e.message : 'Failed to load')
      })
      .finally(() => active && setLoading(false))
    return () => {
      active = false
    }
  }, [bookId])

  async function onDelete() {
    setDeleteError(null)
    const ok = window.confirm('この書籍を削除します。よろしいですか？')
    if (!ok) return
    setDeleting(true)
    try {
      await booksApi.delete(bookId)
      onBack()
    } catch (e) {
      setDeleteError(e instanceof Error ? e.message : '削除に失敗しました')
    } finally {
      setDeleting(false)
    }
  }

  async function onRent() {
    setRentError(null)
    setRentInfo(null)
    setRenting(true)
    try {
      const res = await booksApi.rent(bookId)
      setRentInfo(`貸出が完了しました。返却期限: ${formatIsoDateTimeToSlash(res.returnDeadline)}`)
      setData((prev) => (prev ? { ...prev, canRent: false } : prev))
    } catch (e) {
      setRentError(e instanceof Error ? e.message : '貸出に失敗しました')
    } finally {
      setRenting(false)
    }
  }

  return (
    <div>
      <div style={{ maxWidth: 640, margin: '0 auto' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 12 }}>
          <button onClick={onBack} disabled={deleting || renting}>&lt; 戻る</button>
          <div style={{ display: 'flex', gap: 8 }}>
            {data?.canRent && (
              <button
                onClick={onRent}
                disabled={renting}
                style={{ backgroundColor: '#006400', color: 'white' }}
              >
                {renting ? '貸出中...' : '借りる'}
              </button>
            )}
            <button onClick={() => (location.hash = `/books/${bookId}/edit`)} disabled={deleting || renting}>編集</button>
            <button onClick={onDelete} disabled={deleting || renting} style={{ backgroundColor: '#8b0000', color: 'white' }}>{deleting ? '削除中...' : '削除'}</button>
          </div>
        </div>
        {loading && <p>読み込み中...</p>}
        {error && <p style={{ color: 'crimson' }}>エラー: {error}</p>}
        {deleteError && <p style={{ color: 'crimson' }}>エラー: {deleteError}</p>}
        {rentError && <p style={{ color: 'crimson' }}>エラー: {rentError}</p>}
        {rentInfo && <p style={{ color: 'green' }}>{rentInfo}</p>}
        {data && (
          <div style={{ display: 'grid', gap: 8 }}>
            <DetailRow label="書籍ID" value={String(data.bookId)} />
            <DetailRow label="タイトル" value={data.name} />
            <DetailRow label="著者" value={data.author} />
            <DetailRow label="ISBN" value={data.isbn} />
            <DetailRow label="出版日" value={formatIsoDateYmdToSlash(data.publishedAt)} />
            <DetailRow label="蔵書日" value={formatIsoDateTimeToSlash(data.depositedAt)} />
            <DetailRow label="貸出可" value={data.canRent ? '可' : '不可'} />
            <DetailRow label="概要" value={<span style={{ whiteSpace: 'pre-wrap' }}>{data.outline}</span> as unknown as string} />
          </div>
        )}
      </div>
    </div>
  )
}

function DetailRow({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: '120px 1fr', gap: 8 }}>
      <div style={{ fontWeight: 600 }}>{label}</div>
      <div>{value}</div>
    </div>
  )
}
