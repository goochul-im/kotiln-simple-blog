package simpleblog.config.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging


class MyAuthenticationFilter : Filter {

    val log = KotlinLogging.logger {}

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {

        val servletRequest = request as HttpServletRequest
        val principal: Any? = servletRequest.session.getAttribute("principal") ?: throw RuntimeException("Unauthorized")

        chain?.doFilter(request, response)

    }
}
