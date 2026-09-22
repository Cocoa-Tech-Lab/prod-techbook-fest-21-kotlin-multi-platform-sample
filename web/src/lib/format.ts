// Date formatting helpers for UI
// Requirement: display as
// - Date: YYYY/MM/DD
// - DateTime: YYYY/MM/DD HH:mm:ss

function pad2(n: number): string {
  return n < 10 ? `0${n}` : String(n)
}

// Input like 'YYYY-MM-DD' -> 'YYYY/MM/DD'
export function formatIsoDateYmdToSlash(date: string): string {
  // Simple fast path: regex replace when already in YYYY-MM-DD
  const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(date)
  if (m) return `${m[1]}/${m[2]}/${m[3]}`
  // Fallback: try Date parsing and then format in local timezone as YYYY/MM/DD
  const d = new Date(date)
  if (isNaN(d.getTime())) return date
  return `${d.getFullYear()}/${pad2(d.getMonth() + 1)}/${pad2(d.getDate())}`
}

// Input ISO datetime string -> 'YYYY/MM/DD HH:mm:ss'
export function formatIsoDateTimeToSlash(dateTime: string): string {
  const d = new Date(dateTime)
  if (isNaN(d.getTime())) return dateTime
  const hh = pad2(d.getHours())
  const mm = pad2(d.getMinutes())
  const ss = pad2(d.getSeconds())
  return `${d.getFullYear()}/${pad2(d.getMonth() + 1)}/${pad2(d.getDate())} ${hh}:${mm}:${ss}`
}
