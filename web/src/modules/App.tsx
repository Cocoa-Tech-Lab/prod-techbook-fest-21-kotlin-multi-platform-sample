import { useEffect, useState } from 'react'
import { BookListPage } from './books/BookListPage'
import { BookDetailPage } from './books/BookDetailPage'
import { BookEditPage } from './books/BookEditPage'
import { BookCreatePage } from './books/BookCreatePage'
import { useHashRoute } from './router'
import { RentedBooksPage } from './rentals/RentedBooksPage'
import { AccountRegisterPage } from './accounts/AccountRegisterPage'
import { SignInPage } from './accounts/SignInPage'
import { clearToken, getToken } from '../lib/auth'

export function App() {
  const { route, pushList, pushDetail, pushAccountRegister, pushRentals } = useHashRoute()
  const [token, setTokenState] = useState<string | null>(() => getToken())

  useEffect(() => {
    // Track sessionStorage changes from 401 handler or other tabs
    const onStorage = (e: StorageEvent) => {
      if (e.storageArea === sessionStorage && e.key === 'jwtToken') {
        setTokenState(getToken())
      }
    }
    window.addEventListener('storage', onStorage)
    return () => window.removeEventListener('storage', onStorage)
  }, [])

  function onLogout() {
    clearToken()
    setTokenState(null)
    // Navigate to login (top) after logout
    location.hash = ''
  }

  const isAuthed = !!token

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 16 }}>
      <div style={{ width: '100%', maxWidth: 960 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
          {/*<button onClick={pushList}>トップ</button>*/}
          <div style={{ display: 'flex', gap: 8 }}>
              <button onClick={pushList}>書籍一覧</button>
              <button onClick={pushRentals}>貸出履歴</button>
            {isAuthed && (
              <>
                <button onClick={onLogout}>ログアウト</button>
              </>
            )}
          </div>
        </div>
        {route.name === 'top' && (
          <>
            <h1 style={{ textAlign: 'center' }}>ログイン</h1>
            <SignInPage onSignedIn={() => { setTokenState(getToken()); pushList(); }} />
            <div style={{ marginTop: 16, textAlign: 'center' }}>
              <button onClick={pushAccountRegister}>アカウントをお持ちでない方はこちら（新規作成）</button>
            </div>
          </>
        )}
        {route.name === 'list' && (
          <>
            <h1 style={{ textAlign: 'center' }}>書籍一覧</h1>
            <BookListPage />
          </>
        )}
        {route.name === 'rentals' && (
          <>
            <h1 style={{ textAlign: 'center' }}>貸出履歴</h1>
            <RentedBooksPage />
          </>
        )}
        {route.name === 'detail' && (
          <>
            <h1 style={{ textAlign: 'center' }}>書籍詳細</h1>
            <BookDetailPage bookId={route.bookId} onBack={pushList} />
          </>
        )}
        {route.name === 'edit' && (
          <>
            <h1 style={{ textAlign: 'center' }}>書籍編集</h1>
            <BookEditPage bookId={route.bookId} onCancel={() => pushDetail(route.bookId)} onSaved={() => pushDetail(route.bookId)} />
          </>
        )}
        {route.name === 'create' && (
          <>
            <h1 style={{ textAlign: 'center' }}>書籍登録</h1>
            <BookCreatePage onCancel={pushList} onSaved={(c) => (c?.bookId ? pushDetail(c.bookId) : pushList())} />
          </>
        )}
        {route.name === 'accountRegister' && (
          <>
            <h1 style={{ textAlign: 'center' }}>アカウント登録</h1>
            <AccountRegisterPage onCancel={pushList} />
          </>
        )}
      </div>
    </div>
  )
}
