package simpleblog.config.logout

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import simpleblog.config.security.util.SecurityResponseHandler

@Component
class CustomLogoutSuccessHandler : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        SecurityResponseHandler.sendErrorResponse(response, HttpStatus.OK, "Logout Success")
    }
}
