package simpleblog.config.security.loginHandler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import simpleblog.config.security.JwtManager
import simpleblog.config.security.PrincipalDetails

class CustomSuccessfulHandler : AuthenticationSuccessHandler {

    val log = mu.KotlinLogging.logger {}
    val jwtManager = JwtManager()
    val om = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {

        var details = authentication?.principal as PrincipalDetails

        var accessToken = jwtManager.generateAccessToken(om.writeValueAsString(details))

        response?.addHeader(jwtManager.jwtHeader, "${jwtManager.jwtPrefix}$accessToken")
    }
}
