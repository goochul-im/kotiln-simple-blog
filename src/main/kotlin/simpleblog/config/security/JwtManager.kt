package simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.stereotype.Component
import simpleblog.util.CookieProvider
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

@Component
class JwtManager {

    private val log = KotlinLogging.logger {}
    private val accessTokenSecretKey = "secret"
    private val refreshTokenSecretKey = "refreshSecret"
    private val claimEmail = "email"
    private val claimPrincipal = "principal"
    private val claimRole = "role"
    private val accessTokenExpirationMinutes : Long = 1
    val refreshTokenExpirationHour : Long = 24
    val jwtHeader = "Authorization"
    val jwtPrefix = "Bearer "
    val jwtSubject = "my-token"
    val algorithm: Algorithm = Algorithm.HMAC512(accessTokenSecretKey)

    fun generateRefreshToken(principal: PrincipalDetails): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(refreshTokenExpirationHour))

        val role = principal.authorities.first()?.authority

        val token = generateToken(expireDate, principal, role, refreshTokenSecretKey)

        return token
    }

    fun generateAccessToken(principal: PrincipalDetails): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(accessTokenExpirationMinutes))

        val role = principal.authorities.first()?.authority

        val token = generateToken(expireDate, principal, role, accessTokenSecretKey)

        return token
    }

    fun getMemberEmailFromToken(token: DecodedJWT): String {
        return token.getClaim(claimEmail).asString()
    }

    fun getMemberRoleFromToken(token: DecodedJWT): String{
        return token.getClaim(claimRole).asString()
    }

    fun validateAccessToken(accessToken: String) : DecodedJWT {
        try {
            val verifier: JWTVerifier = JWT.require(algorithm).build()
            return verifier.verify(accessToken)
        } catch (exception: TokenExpiredException) {
            log.error { "Access token expired: $exception" }
            throw exception
        }
        catch (exception: JWTVerificationException) {
            log.error { "Invalid access token: $exception" }
            throw RuntimeException("Invalid access token")
        }
    }

    fun validateRefreshToken(refreshToken: String): DecodedJWT {
        try {
            val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(refreshTokenSecretKey)).build()
            return verifier.verify(refreshToken)
        } catch (exception: JWTVerificationException) {
            log.error { "Invalid refresh token: $exception" }
            throw RuntimeException("Invalid refresh token")
        }
    }

    private fun generateToken(
        expireDate: Date,
        principal: PrincipalDetails,
        role: String?,
        secretKey: String
    ): String = JWT.create()
        .withSubject(jwtSubject)
        .withIssuedAt(Date())
        .withExpiresAt(expireDate)
        .withClaim(claimEmail, principal.username)
        .withClaim(claimRole, role)
        .withClaim(claimPrincipal, principal.toString())
        .sign(Algorithm.HMAC512(secretKey))

}
