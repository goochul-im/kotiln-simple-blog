package simpleblog.config.security.exceptionHandler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

/**
 * 인가에 실패했을 때 호출
 * 인증(로그인)은 되었지만, 접근할 권한이 없을 때
 */
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    private val log = mu.KotlinLogging.logger {}

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        log.warn { "접근 거부됨 (403 Forbidden): ${accessDeniedException?.message}, 요청 URI: ${request.requestURI}" }

        // 응답 상태 코드 설정
        response.status = HttpStatus.FORBIDDEN.value()
        // 응답 컨텐츠 타입 및 인코딩 설정
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // 클라이언트에게 보낼 에러 응답 DTO 또는 Map 생성
        val errorResponse = mapOf(
            "status" to HttpStatus.FORBIDDEN.value(),
            "error" to "Forbidden",
            "message" to "해당 리소스에 접근할 수 있는 권한이 없습니다."
        )

        // JSON 형태로 응답 바디에 작성
        val writer = response.writer
        writer.write(objectMapper.writeValueAsString(errorResponse))
        writer.flush()
    }
}
