package com.example.bookmanager.infra.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.bookmanager.application.port.TokenIssuer
import com.example.bookmanager.infra.config.JwtConfig
import java.util.Date
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * JWT を発行する [TokenIssuer] の実装。
 *
 * - 署名方式: HMAC-SHA256
 * - iss/aud/sub/iat/exp と、アプリ固有の email/name/level クレームを付与します。
 * - 有効期限は [JwtConfig.expiresMinutes] に基づきます。
 */
class JwtTokenIssuer(
    private val config: JwtConfig
): TokenIssuer {
    // 遅延初期化で署名アルゴリズムを準備
    private val algorithm: Algorithm by lazy { Algorithm.HMAC256(config.secret) }

    /**
     * JWT を発行します。
     * @param subject JWT の subject（ユーザIDなど）
     * @param email クレーム email
     * @param name クレーム name
     * @param level クレーム level（権限）
     * @return 署名済みJWT文字列
     */
    override fun issue(
        subject: String,
        email: String,
        name: String,
        level: String,
    ): String {
        val now = Instant.now()
        val expiresAt = now.plus(config.expiresMinutes.toLong(), ChronoUnit.MINUTES)
        return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withSubject(subject)
            .withClaim("email", email)
            .withClaim("name", name)
            .withClaim("level", level)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expiresAt))
            .sign(algorithm)
    }
}
