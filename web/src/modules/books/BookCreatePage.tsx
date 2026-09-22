import React, { useState } from 'react'
import { booksApi } from '../../lib'
import type { CreateBookRequest } from '../../lib'

export function BookCreatePage({ onCancel, onSaved }: { onCancel: () => void; onSaved: (created?: { bookId: number }) => void }) {
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')
  const [outline, setOutline] = useState('')
  const [publishedAt, setPublishedAt] = useState('') // YYYY-MM-DD
  const [isbn, setIsbn] = useState('')
  const [saving, setSaving] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    setFormError(null)
    if (!title.trim()) return setFormError('タイトルを入力してください')
    if (!author.trim()) return setFormError('著者を入力してください')
    if (!/^[0-9-]{10,20}$/.test(isbn.trim())) return setFormError('ISBNを正しく入力してください（ハイフン可）')
    if (!/^\d{4}-\d{2}-\d{2}$/.test(publishedAt)) return setFormError('出版日はYYYY-MM-DDで入力してください')

    const body: CreateBookRequest = {
      title: title.trim(),
      author: author.trim(),
      outline,
      publishedAt,
      isbn: isbn.trim(),
    }
    setSaving(true)
    try {
      const res = await booksApi.create(body)
      // Navigate to detail of the created book before returning to list
      onSaved({ bookId: res.bookId })
    } catch (e) {
      setFormError(e instanceof Error ? e.message : '登録に失敗しました')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <div style={{ maxWidth: 640, margin: '0 auto' }}>
        <button onClick={onCancel} style={{ marginBottom: 12 }} disabled={saving}>&lt; 戻る</button>
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
          <Field label="ISBN">
            <input value={isbn} onChange={(e) => setIsbn(e.target.value)} placeholder="例: 978-4-xx" style={inputStyle} />
          </Field>
          <Field label="概要">
            <textarea value={outline} onChange={(e) => setOutline(e.target.value)} rows={6} style={{ ...inputStyle, resize: 'vertical' }} />
          </Field>
          <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
            <button type="button" onClick={onCancel} disabled={saving}>キャンセル</button>
            <button type="submit" disabled={saving}>{saving ? '登録中...' : '登録'}</button>
          </div>
        </form>
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
