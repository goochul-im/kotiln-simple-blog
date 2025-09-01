package simpleblog.config.security.exceptionHandler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * 인증이 실패했을 때 호출
 */
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val log = mu.KotlinLogging.logger {}

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.warn { "인증 실패 (401 Unauthorized): ${authException.message}, 요청 URI: ${request.requestURI}" }

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val errorResponse = mapOf(
            "status" to HttpStatus.UNAUTHORIZED.value(),
            "error" to "Unauthorized",
            "message" to "인증이 필요합니다. 로그인을 진행해주세요."
        )

        val writer = response.writer
        writer.write(objectMapper.writeValueAsString(errorResponse))
        writer.flush()
    }
}
