package simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import simpleblog.config.security.util.SecurityResponseHandler
import simpleblog.util.value.CmResDto

class CustomLogoutHandler : LogoutHandler{

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

        SecurityResponseHandler.sendErrorResponse(response, HttpStatus.OK, "Logout Success")
    }
}
