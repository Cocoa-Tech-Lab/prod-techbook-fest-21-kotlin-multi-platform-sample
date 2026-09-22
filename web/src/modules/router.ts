import { useEffect, useMemo, useState } from 'react'

export type Route =
  | { name: 'top' } // top page: login/register
  | { name: 'list' } // book list at /book
  | { name: 'detail'; bookId: number }
  | { name: 'edit'; bookId: number }
  | { name: 'create' }
  | { name: 'accountRegister' }
  | { name: 'rentals' }

function parseHash(): Route {
  const h = (globalThis.location?.hash || '').replace(/^#/, '')
  // patterns: '' -> top, '/book' -> list, '/books/123' -> detail, '/books/123/edit' -> edit, '/books/new' -> create
  if (h === '') return { name: 'top' }
  if (h === '/book') return { name: 'list' }
  const mDetail = /^\/books\/(\d+)$/.exec(h)
  if (mDetail) return { name: 'detail', bookId: Number(mDetail[1]) }
  const mEdit = /^\/books\/(\d+)\/edit$/.exec(h)
  if (mEdit) return { name: 'edit', bookId: Number(mEdit[1]) }
  if (h === '/books/new') return { name: 'create' }
  if (h === '/admin/account/register') return { name: 'accountRegister' }
  if (h === '/rentals') return { name: 'rentals' }
  // fallback: send to top
  return { name: 'top' }
}

export function useHashRoute() {
  const [route, setRoute] = useState<Route>(() => parseHash())

  useEffect(() => {
    const onHash = () => setRoute(parseHash())
    window.addEventListener('hashchange', onHash)
    return () => window.removeEventListener('hashchange', onHash)
  }, [])

  const api = useMemo(
    () => ({
      pushList() {
        location.hash = '/book'
      },
      pushDetail(id: number) {
        location.hash = `/books/${id}`
      },
      pushEdit(id: number) {
        location.hash = `/books/${id}/edit`
      },
      pushCreate() {
        location.hash = `/books/new`
      },
      pushAccountRegister() {
        location.hash = `/admin/account/register`
      },
      pushRentals() {
        location.hash = `/rentals`
      },
    }),
    []
  )

  return { route, ...api }
}
