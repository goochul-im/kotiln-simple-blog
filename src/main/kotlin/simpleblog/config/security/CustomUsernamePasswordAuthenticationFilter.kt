package simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import simpleblog.domain.member.LoginDto

class CustomUsernamePasswordAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    private val authManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter(authManager){

    private val log = mu.KotlinLogging.logger {}
    private val jwtManager = JwtManger()

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication? {

        log.info { "login 인증 중" }

        lateinit var loginDto:LoginDto

        try {
            loginDto = objectMapper.readValue(request?.inputStream, LoginDto::class.java)
        }catch (e: Exception) {
            log.error { "exception: $e" }
        }

        log.info { "loginDto: $loginDto" }

        var token = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

        return this.authManager.authenticate(token)
    }

}
