package simpleblog.config.security.loginHandler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import simpleblog.config.security.JwtManager
import simpleblog.config.security.PrincipalDetails
import simpleblog.util.CookieProvider
import java.util.concurrent.TimeUnit

class CustomSuccessfulHandler : AuthenticationSuccessHandler {

    val log = mu.KotlinLogging.logger {}
    val jwtManager = JwtManager()
    val om = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {

        val details = authentication?.principal as PrincipalDetails

        val accessToken = jwtManager.generateAccessToken(details)
        val refreshToken = jwtManager.generateRefreshToken(details)

        val refreshCookie = CookieProvider.createCookie(
            "refreshToken",
            refreshToken,
            TimeUnit.HOURS.toSeconds(jwtManager.refreshTokenExpirationHour)
        )

        response?.addHeader(jwtManager.jwtHeader, "${jwtManager.jwtPrefix}$accessToken")
        response?.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

    }
}
