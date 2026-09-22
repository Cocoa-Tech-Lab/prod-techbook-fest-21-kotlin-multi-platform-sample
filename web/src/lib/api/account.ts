import { http, ApiError } from '../http'
import type { RentalListResponse } from '../types/account'
import type { ApiResponse } from '../types/common'

// Types aligned with backend DTOs
export type CreateAccountRequest = { email: string; password: string }
export type CreateAccountResponse = { accountId: string; email: string; name: string }
export type SignInRequest = { email: string; password: string }
export type SignInResponse = { token: string }

export async function registerAccount(
  body: CreateAccountRequest
): Promise<ApiResponse<CreateAccountResponse>> {
  try {
    const data = await http.post<CreateAccountResponse, CreateAccountRequest>('/account', body)
    return { data }
  } catch (e) {
    if (e instanceof ApiError) {
      return { errorInfo: { code: e.code, message: e.message } }
    }
    // Unknown error: rethrow to be handled by caller as network error
    throw e
  }
}

export async function signIn(body: SignInRequest): Promise<SignInResponse> {
  // http.post unwraps the Success envelope and returns data directly
  return http.post<SignInResponse, SignInRequest>('/account/signin', body)
}

export const accountApi = {
  rentalList: () => http.get<RentalListResponse>('/account/rental_list'),
}
