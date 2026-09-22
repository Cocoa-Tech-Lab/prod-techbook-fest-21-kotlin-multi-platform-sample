import { API_BASE_URL } from './config';
import type { ApiFailure, ApiSuccess } from './types/common';
import { getToken, clearToken } from './auth';

export class ApiError extends Error {
  code: number;
  status: number;
  constructor(message: string, options: { code: number; status: number }) {
    super(message);
    this.name = 'ApiError';
    this.code = options.code;
    this.status = options.status;
  }
}

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

export async function request<TData = unknown, TBody = unknown>(
  path: string,
  options: {
    method?: HttpMethod;
    body?: TBody;
    headers?: Record<string, string>;
    signal?: AbortSignal;
  } = {}
): Promise<TData> {
  const url = new URL(path, API_BASE_URL).toString();
  const { method = 'GET', body, headers = {}, signal } = options;

  const authHeader: Record<string, string> = {};
  const token = getToken();
  if (token) authHeader['Authorization'] = `JWT ${token}`;

  const init: RequestInit = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...authHeader,
      ...headers,
    },
    signal,
  };

  if (body !== undefined) {
    (init as { body?: string }).body = JSON.stringify(body);
  }

  const res = await fetch(url, init);
  const contentType = res.headers.get('content-type') || '';
  const isJson = contentType.includes('application/json');
  const payload = isJson ? await res.json() : undefined;

  if (!res.ok) {
    if (res.status === 401) {
      // Clear token and redirect to top
      clearToken();
      try {
        if (location) location.hash = '';
      } catch {}
    }
    // Try to extract backend Failure envelope if present
    let code = res.status;
    let message = res.statusText || 'HTTP Error';
    if (payload && typeof payload === 'object' && 'errorInfo' in payload) {
      const failure = payload as ApiFailure;
      code = failure.errorInfo.code;
      message = failure.errorInfo.message;
    }
    throw new ApiError(message, { code, status: res.status });
  }

  // Expect Success envelope
  if (payload && typeof payload === 'object' && 'data' in payload) {
    return (payload as ApiSuccess<TData>).data;
  }

  // Some endpoints may return empty body on success (unlikely here), fallback
  return payload as TData;
}

export const http = {
  get: <T>(path: string, init?: Omit<Parameters<typeof request<T>>[1], 'method' | 'body'>) =>
    request<T>(path, { ...init, method: 'GET' }),
  post: <T, B = unknown>(path: string, body?: B, init?: Omit<Parameters<typeof request<T, B>>[1], 'method'>) =>
    request<T, B>(path, { ...init, method: 'POST', body }),
  put: <T, B = unknown>(path: string, body?: B, init?: Omit<Parameters<typeof request<T, B>>[1], 'method'>) =>
    request<T, B>(path, { ...init, method: 'PUT', body }),
  delete: <T>(path: string, init?: Omit<Parameters<typeof request<T>>[1], 'method' | 'body'>) =>
    request<T>(path, { ...init, method: 'DELETE' }),
};
