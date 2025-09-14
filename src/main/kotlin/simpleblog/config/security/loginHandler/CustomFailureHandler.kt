package simpleblog.config.security.loginHandler

import simpleblog.config.security.util.SecurityResponseHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class CustomFailureHandler : AuthenticationFailureHandler {

    val log = KotlinLogging.logger {}

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException?
    ) {

        log.warn("로그인 실패: ${exception?.message}")

        SecurityResponseHandler.sendErrorResponse(
            response,
            HttpStatus.UNAUTHORIZED,
            exception?.message ?: "인증에 실패했습니다. 아이디나 비밀번호를 확인해주세요."
        )
    }
}
