package com.example.bookmanager.presentation.controller

import com.example.bookmanager.application.port.TokenIssuer
import com.example.bookmanager.application.usecase.RegisterAccountUseCase
import com.example.bookmanager.presentation.dto.account.CreateAccountRequest
import com.example.bookmanager.presentation.dto.account.CreateAccountResponse
import com.example.bookmanager.presentation.dto.common.ApiResponse
import com.example.bookmanager.presentation.dto.account.SignInRequest
import com.example.bookmanager.presentation.dto.account.SignInResponse
import kotlin.uuid.ExperimentalUuidApi

import com.example.bookmanager.application.usecase.SignInAccountUseCase
import com.example.bookmanager.application.usecase.ListRentalHistoryUseCase
import com.example.bookmanager.presentation.dto.account.RentalListResponse
import com.example.bookmanager.presentation.dto.account.RentalHistoryItem
import com.example.bookmanager.presentation.dto.common.ApiResponse.Success
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlin.time.ExperimentalTime
import kotlin.uuid.Uuid

/**
 * アカウント登録・サインイン・自分のレンタル履歴取得を扱う Controller。
 *
 * UseCase を呼び出し、その結果をAPIで返しやすいDTOに整形して返します。
 */
class AccountController(
    private val registerAccountUseCase: RegisterAccountUseCase,
    private val signInAccountUseCase: SignInAccountUseCase,
    private val tokenIssuer: TokenIssuer,
    private val listRentalHistoryUseCase: ListRentalHistoryUseCase,
) {
    /**
     * アカウントを新規登録します。
     */
    @OptIn(ExperimentalUuidApi::class)
    suspend fun register(request: CreateAccountRequest): ApiResponse.Success<CreateAccountResponse> {
        val result = registerAccountUseCase.execute(
            RegisterAccountUseCase.RegisterAccountInput(
                email = request.email,
                password = request.password
            )
        )
        return ApiResponse.Success(
            CreateAccountResponse(
                accountId = result.accountId,
                email = result.email,
                name = result.name
            )
        )
    }

    /**
     * サインインしてJWTを発行します。
     */
    @OptIn(ExperimentalUuidApi::class)
    suspend fun signin(request: SignInRequest): ApiResponse.Success<SignInResponse> {
        val result = signInAccountUseCase.execute(
            SignInAccountUseCase.SignInInput(
                email = request.email,
                password = request.password
            )
        )
        val token = tokenIssuer.issue(
            subject = result.accountId.toString(),
            email = result.email,
            name = result.name,
            level = result.level
        )
        return ApiResponse.Success(SignInResponse(token = token))
    }

    /**
     * 自分のレンタル履歴を取得します。
     * @param userId 取得対象のユーザID
     */
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun rentalHistory(userId: Uuid): Success<RentalListResponse> {
        val result = listRentalHistoryUseCase.execute(
            ListRentalHistoryUseCase.ListRentalHistoryInput(
                userId = userId,
                activeOnly = false,
            )
        )
        return Success(
            RentalListResponse(
                items = result.items.map {
                    RentalHistoryItem(
                        rentId = it.rentId,
                        bookId = it.bookId,
                        userId = it.userId,
                        status = it.status,
                        rentalAt = it.rentalAt.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                        returnDeadline = it.returnDeadline.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                        returnedAt = it.returnedAt?.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET),
                    )
                }
            )
        )
    }
}
