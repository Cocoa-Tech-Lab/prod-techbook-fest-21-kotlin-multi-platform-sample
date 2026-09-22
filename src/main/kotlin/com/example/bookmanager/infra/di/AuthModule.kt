package com.example.bookmanager.infra.di

import com.example.bookmanager.application.port.TokenIssuer
import com.example.bookmanager.infra.auth.JwtTokenIssuer
import com.example.bookmanager.infra.config.JwtConfig
import org.koin.dsl.module

/**
 * 認証・トークン関連の依存を登録するモジュール。
 *
 * [JwtConfig] を用いた [TokenIssuer] 実装を提供します。
 */
fun authModule(jwtConfig: JwtConfig) = module {
    single<TokenIssuer> {
        JwtTokenIssuer(jwtConfig)
    }
}
