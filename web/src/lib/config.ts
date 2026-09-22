// Configuration for API base URL
// Reads from Vite env variable VITE_API_BASE_URL; fallback to localhost:8080
export const API_BASE_URL: string =
  (typeof import.meta !== 'undefined' && (import.meta as unknown as { env?: Record<string, string | undefined> }).env?.VITE_API_BASE_URL) ||
  ((globalThis as unknown as { process?: { env?: Record<string, string | undefined> } }).process?.env?.VITE_API_BASE_URL) ||
  'http://localhost:8080';
