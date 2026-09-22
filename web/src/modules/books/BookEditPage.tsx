import React, { useEffect, useState } from 'react'
import { booksApi } from '../../lib'
import type { UpdateBookRequest } from '../../lib'

export function BookEditPage({ bookId, onCancel, onSaved }: { bookId: number; onCancel: () => void; onSaved: () => void }) {
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')
  const [outline, setOutline] = useState('')
  const [publishedAt, setPublishedAt] = useState('') // YYYY-MM-DD
  const [saving, setSaving] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)

  useEffect(() => {
    let active = true
    setLoading(true)
    setError(null)
    booksApi
      .detail(bookId)
      .then((d) => {
        if (!active) return
        setTitle(d.name)
        setAuthor(d.author)
        setOutline(d.outline)
        setPublishedAt(d.publishedAt)
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

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    setFormError(null)
    if (!title.trim()) return setFormError('タイトルを入力してください')
    if (!author.trim()) return setFormError('著者を入力してください')
    if (!/^\d{4}-\d{2}-\d{2}$/.test(publishedAt)) return setFormError('出版日はYYYY-MM-DDで入力してください')

    const body: UpdateBookRequest = {
      title: title.trim(),
      author: author.trim(),
      outline: outline,
      publishedAt,
    }
    setSaving(true)
    try {
      await booksApi.update(bookId, body)
      onSaved()
    } catch (e) {
      setFormError(e instanceof Error ? e.message : '更新に失敗しました')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <div style={{ maxWidth: 640, margin: '0 auto' }}>
        <button onClick={onCancel} style={{ marginBottom: 12 }} disabled={saving}>&lt; 戻る</button>
        {loading && <p>読み込み中...</p>}
        {error && <p style={{ color: 'crimson' }}>エラー: {error}</p>}
        {!loading && !error && (
          <form onSubmit={onSubmit} style={{ display: 'grid', gap: 12 }}>
            {formError && <p style={{ color: 'crimson' }}>{formError}</p>}
            <Field label="タイトル">
              <input value={title} onChange={(e) => setTitle(e.target.value)} style={inputStyle} />
            </Field>
            <Field label="著者">
              <input value={author} onChange={(e) => setAuthor(e.target.value)} style={inputStyle} />
            </Field>
            <Field label="出版日">
              <input value={publishedAt} onChange={(e) => setPublishedAt(e.target.value)} placeholder="YYYY-MM-DD" style={inputStyle} />
            </Field>
            <Field label="概要">
              <textarea value={outline} onChange={(e) => setOutline(e.target.value)} rows={6} style={{ ...inputStyle, resize: 'vertical' }} />
            </Field>
            <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
              <button type="button" onClick={onCancel} disabled={saving}>キャンセル</button>
              <button type="submit" disabled={saving}>{saving ? '保存中...' : '保存'}</button>
            </div>
          </form>
        )}
      </div>
    </div>
  )
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: '120px 1fr', gap: 8, alignItems: 'center' }}>
      <div style={{ fontWeight: 600 }}>{label}</div>
      <div>{children}</div>
    </div>
  )
}

const inputStyle: React.CSSProperties = {
  width: '100%',
  padding: 8,
  border: '1px solid #ccc',
  borderRadius: 4,
  background: 'transparent',
}
