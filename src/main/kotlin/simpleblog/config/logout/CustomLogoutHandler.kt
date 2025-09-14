package simpleblog.config.logout

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import simpleblog.config.security.util.SecurityResponseHandler

@Component
class CustomLogoutHandler : LogoutHandler {

    private val log = KotlinLogging.logger{}

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        log.info { "logout success" }

        val context = SecurityContextHolder.getContext()
        context.authentication = null
        SecurityContextHolder.clearContext()

    }
}
