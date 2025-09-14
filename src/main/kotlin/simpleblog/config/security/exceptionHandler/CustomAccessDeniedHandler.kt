package simpleblog.config.security.exceptionHandler

import simpleblog.config.security.util.SecurityResponseHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

/**
 * 인가에 실패했을 때 호출
 * 인증(로그인)은 되었지만, 접근할 권한이 없을 때
 */
class CustomAccessDeniedHandler : AccessDeniedHandler {

    private val log = mu.KotlinLogging.logger {}

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        log.warn { "접근 거부됨 (403 Forbidden): ${accessDeniedException?.message}, 요청 URI: ${request.requestURI}" }

        SecurityResponseHandler.sendErrorResponse(
            response,
            HttpStatus.FORBIDDEN,
            "해당 리소스에 접근할 수 있는 권한이 없습니다."
        )
    }
}
