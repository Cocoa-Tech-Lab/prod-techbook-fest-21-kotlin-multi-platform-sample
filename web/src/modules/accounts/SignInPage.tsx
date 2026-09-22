import React, { useState } from 'react'
import { signIn } from '../../lib/api/account'
import { setToken } from '../../lib/auth'

export function SignInPage(props: { onSignedIn?: () => void }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
      setError('メールアドレスの形式が正しくありません')
      return
    }
    if (!password) {
      setError('パスワードを入力してください')
      return
    }
    setSubmitting(true)
    try {
      const res = await signIn({ email, password })
      setToken(res.token)
      props.onSignedIn?.()
    } catch (e) {
      setError('メールアドレスまたはパスワードが正しくありません')
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
          placeholder="パスワード"
          required
          style={{ padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
        />
      </label>
      {error && (
        <div style={{ color: '#c00', background: '#fee', border: '1px solid #fcc', padding: 8, borderRadius: 4 }}>{error}</div>
      )}
      <div style={{ display: 'flex', gap: 8, justifyContent: 'center' }}>
        <button type="submit" disabled={submitting}>
          {submitting ? 'ログイン中...' : 'ログイン'}
        </button>
      </div>
    </form>
  )
}
