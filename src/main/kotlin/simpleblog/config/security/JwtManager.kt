package simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import mu.KotlinLogging
import simpleblog.domain.member.Role
import java.util.Date
import java.util.concurrent.TimeUnit

class JwtManager {

    private val log = KotlinLogging.logger {}
    private val secret = "secret"
    private val claimEmail = "email"
    private val claimPrincipal = "principal"
    private val claimRole = "role"
    private val accessTokenExpirationMinutes : Long = 60
    val jwtHeader = "Authorization"
    val jwtPrefix = "Bearer "
    val jwtSubject = "my-token"
    val algorithm: Algorithm = Algorithm.HMAC512(secret)

    fun generateRefreshToken(principal: PrincipalDetails) : String {

    }

    fun generateAccessToken(principal: PrincipalDetails): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(accessTokenExpirationMinutes))

        val role = principal.authorities.first()?.authority

        val token = JWT.create()
            .withSubject(jwtSubject)
            .withIssuedAt(Date())
            .withExpiresAt(expireDate)
            .withClaim(claimEmail, principal.username)
            .withClaim(claimRole, role)
            .withClaim(claimPrincipal, principal.toString())
            .sign(algorithm)

        return token
    }

    fun getMemberEmailFromToken(token: String): String {
        return validatedJwt(token).getClaim(claimEmail).asString()
    }

    fun getMemberRoleFromToken(token: String): String{
        return validatedJwt(token).getClaim(claimRole).asString()
    }

    fun getPrincipalStringByAccessToken(accessToken: String): String{

        val decodedJWT = validatedJwt(accessToken)
        val principalString = decodedJWT.getClaim(claimPrincipal).asString()

        return principalString
    }

    fun validatedJwt(accessToken: String) : DecodedJWT {
        try {
            val verifier: JWTVerifier = JWT.require(algorithm)
                .build()

            val jwt: DecodedJWT = verifier.verify(accessToken)

            return jwt
        } catch (exception: JWTVerificationException) {
            log.error { "exception: $exception" }
            throw RuntimeException("invalid jwt")
        }
    }

}
