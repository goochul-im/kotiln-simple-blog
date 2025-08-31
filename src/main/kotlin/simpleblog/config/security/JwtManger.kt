package simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.Date

class JwtManger {

    private val log = KotlinLogging.logger {}

    fun generateAccessToken(principal: PrincipalDetails): String {

        var token = JWT.create()
            .withSubject(principal.username)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .withClaim("email", principal.username)
            .withClaim("password", principal.password)
            .sign(Algorithm.HMAC512("secret"))

        return token
    }

}
