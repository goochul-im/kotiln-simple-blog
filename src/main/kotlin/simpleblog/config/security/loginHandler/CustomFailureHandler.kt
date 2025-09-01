package simpleblog.config.security.loginHandler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class CustomFailureHandler : AuthenticationFailureHandler {

    val log = KotlinLogging.logger {}
    val objectMapper = ObjectMapper()

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException?
    ) {

        log.warn("로그인 실패: ${exception?.message}")

        // 1. 상태 코드를 직접 설정
        response.status = HttpStatus.UNAUTHORIZED.value()
        // 2. 컨텐츠 타입을 JSON으로 설정
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // 3. 에러 메시지를 담은 JSON 본문을 직접 작성
        val errorResponse = mapOf(
            "status" to HttpStatus.UNAUTHORIZED.value(),
            "error" to "Unauthorized",
            "message" to (exception?.message ?: "인증에 실패했습니다. 아이디나 비밀번호를 확인해주세요.")
        )
        response.writer.write(objectMapper.writeValueAsString(errorResponse))

    }
}
