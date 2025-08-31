package simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.Date

class JwtManger {

    private val log = KotlinLogging.logger {}
    private val secret = "secret"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val expirationTime = 1000 * 60 * 60

    fun generateAccessToken(principal: PrincipalDetails): String {

        var token = JWT.create()
            .withSubject(principal.username)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .withClaim(claimEmail, principal.username)
            .withClaim(claimPassword, principal.password)
            .sign(Algorithm.HMAC512(secret))

        return token
    }

    fun getMemberEmailFromToken(token: String): String {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
            .getClaim(claimEmail).asString()
    }

}
