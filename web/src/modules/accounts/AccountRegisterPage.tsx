import React, { useState } from 'react'
import { registerAccount } from '../../lib/api/account'

export function AccountRegisterPage(props: { onSuccess?: (account: { accountId: string; email: string; name: string }) => void; onCancel?: () => void }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [message, setMessage] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    setMessage(null)

    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
      setError('メールアドレスの形式が正しくありません')
      return
    }
    if (password.length < 8) {
      setError('パスワードは8文字以上で入力してください')
      return
    }

    setSubmitting(true)
    try {
      const res = await registerAccount({ email, password })
      if ('data' in res) {
        setMessage('アカウントを登録しました')
        props.onSuccess?.(res.data)
      } else {
        setError(res.errorInfo?.message || '登録に失敗しました')
      }
    } catch (e) {
      setError('通信エラーが発生しました')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form onSubmit={onSubmit} style={{ display: 'grid', gap: 12, maxWidth: 420, margin: '0 auto' }}>
      <label style={{ display: 'grid', gap: 6 }}>
        <span>メールアドレス</span>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
          required
          style={{ padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
        />
      </label>
      <label style={{ display: 'grid', gap: 6 }}>
        <span>パスワード</span>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="8文字以上"
          required
          style={{ padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
        />
      </label>

      {error && (
        <div style={{ color: '#c00', background: '#fee', border: '1px solid #fcc', padding: 8, borderRadius: 4 }}>{error}</div>
      )}
      {message && <div style={{ color: '#060', background: '#efe', border: '1px solid #cfc', padding: 8, borderRadius: 4 }}>{message}</div>}

      <div style={{ display: 'flex', gap: 8, justifyContent: 'center' }}>
        {props.onCancel && (
          <button type="button" onClick={props.onCancel} disabled={submitting}>
            キャンセル
          </button>
        )}
        <button type="submit" disabled={submitting}>
          {submitting ? '登録中...' : '登録する'}
        </button>
      </div>
    </form>
  )
}
