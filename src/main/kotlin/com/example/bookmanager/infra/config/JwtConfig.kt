package com.example.bookmanager.infra.config

import io.ktor.server.plugins.di.annotations.Property

/**
 * JWT 設定。application.yaml から読み込みます。
 */
data class JwtConfig(
    @Property("jwt.secret")
    val secret: String,
    // issuer は application.yaml の jwt.domain を流用
    @Property("jwt.domain")
    val issuer: String,
    @Property("jwt.audience")
    val audience: String,
    @Property("jwt.realm")
    val realm: String,
    @Property("jwt.expiresMinutes")
    val expiresMinutes: Int,
)
