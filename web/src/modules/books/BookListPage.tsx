import React, { useEffect, useMemo, useState } from 'react'
import { booksApi } from '../../lib'
import type { BookSummary, ListBookSummaryResponse } from '../../lib'
import { formatIsoDateYmdToSlash } from '../../lib/format'

export function BookListPage() {
  const [data, setData] = useState<ListBookSummaryResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [query, setQuery] = useState('')

  useEffect(() => {
    let active = true
    setLoading(true)
    setError(null)

    booksApi
      .list()
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
  }, [])

  const filtered: BookSummary[] = useMemo(() => {
    const list = data?.books ?? []
    if (!query.trim()) return list
    const q = query.trim().toLowerCase()
    return list.filter((b) => b.name.toLowerCase().includes(q))
  }, [data, query])

  if (loading) return <p>読み込み中...</p>
  if (error) return <p style={{ color: 'crimson' }}>エラー: {error}</p>

  if (!data) {
    return (
      <div>
        <p style={{ marginBottom: 12 }}>書籍一覧を表示するにはログインしてください。</p>
      </div>
    )
  }

  if (data.books.length === 0)
    return (
      <div>
        <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
          <SearchBox query={query} setQuery={setQuery} />
          <button onClick={() => (location.hash = '/books/new')}>登録</button>
        </div>
        <p>書籍がありません。</p>
      </div>
    )

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
        <SearchBox query={query} setQuery={setQuery} />
        <button onClick={() => (location.hash = '/books/new')}>登録</button>
      </div>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <Th>書籍ID</Th>
            <Th>タイトル</Th>
            <Th>出版日</Th>
            <Th>貸出可</Th>
          </tr>
        </thead>
        <tbody>
          {filtered.map((b) => (
            <tr
              key={b.bookId}
              style={{ borderTop: '1px solid #ddd', cursor: 'pointer' }}
              onClick={() => (location.hash = `/books/${b.bookId}`)}
            >
              <Td>{b.bookId}</Td>
              <Td>{b.name}</Td>
              <Td>{formatIsoDateYmdToSlash(b.publishedAt)}</Td>
              <Td>{b.canRent ? '○' : '×'}</Td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

function SearchBox({ query, setQuery }: { query: string; setQuery: (v: string) => void }) {
  return (
    <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
      <input
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="タイトルで検索"
        style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
      />
    </div>
  )
}

function Th({ children }: { children: React.ReactNode }) {
  return <th style={{ textAlign: 'left', padding: '8px 6px', fontWeight: 600 }}>{children}</th>
}
function Td({ children }: { children: React.ReactNode }) {
  return <td style={{ padding: '8px 6px' }}>{children}</td>
}
