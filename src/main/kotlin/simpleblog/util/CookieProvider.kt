package simpleblog.util

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.ResponseCookie // 이걸 왜 쓰지? https://g.co/gemini/share/ef010cfbc692
import java.util.Optional

object CookieProvider {

    private val log = KotlinLogging.logger {  }

    fun createNullCookie(cookieName: String) {
        TODO()
    }

    fun createCookie(cookieName: String, value: String, maxAge: Long): ResponseCookie {

        val cookie = ResponseCookie.from(cookieName, value)
            .httpOnly(true)
            .secure(false) // http 허용
            .path("/")
            .maxAge(maxAge) // seconds 단위
            .build()

        return cookie
    }

    fun getCookie(request: HttpServletRequest, cookieName: String): Optional<String> {
        val cookieValue = request.cookies.filter { cookie ->
            cookie.name == cookieName
        }.map { it.value }
            .firstOrNull()

        log.info { "cookieValue : $cookieValue" }

        return Optional.ofNullable(cookieValue)
    }

}
