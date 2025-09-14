package simpleblog.config.security.exceptionHandler

import simpleblog.config.security.util.SecurityResponseHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * 인증이 실패했을 때 호출
 */
@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val log = mu.KotlinLogging.logger {}

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.warn { "인증 실패 (401 Unauthorized): ${authException.message}, 요청 URI: ${request.requestURI}" }

        SecurityResponseHandler.sendErrorResponse(
            response,
            HttpStatus.UNAUTHORIZED,
            "인증이 필요합니다. 로그인을 진행해주세요."
        )
    }
}
