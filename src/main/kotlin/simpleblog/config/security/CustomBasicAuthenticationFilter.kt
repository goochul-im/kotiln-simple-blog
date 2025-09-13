package simpleblog.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    authenticationManager: AuthenticationManager,
) : BasicAuthenticationFilter(authenticationManager){

    val log = mu.KotlinLogging.logger {}
    private val jwtManager = JwtManager()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {

        log.info { "Basic Authentication Filter" }

        val token = request.getHeader(jwtManager.jwtHeader)?.replace(jwtManager.jwtPrefix, "")?.trim()

        if (token == null){
            log.error { "token is null" }
            chain.doFilter(request, response)
            return
        }

        log.debug { "token: $token" }

        val email = jwtManager.getMemberEmailFromToken(token)
        val role = jwtManager.getMemberRoleFromToken(token)
        val roleForEnum = role.replace("ROLE_","")

        val principalDetails = PrincipalDetails(email = email, role = roleForEnum)
        val authorities = listOf(SimpleGrantedAuthority(role))

        val authentication : Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            null,
            authorities
        )

        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }
}
