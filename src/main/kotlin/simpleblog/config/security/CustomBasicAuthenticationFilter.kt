package simpleblog.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import simpleblog.domain.member.MemberRepository

class CustomBasicAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val memberRepository: MemberRepository
) : BasicAuthenticationFilter(authenticationManager){

    val log = mu.KotlinLogging.logger {}
    private val jwtManager = JwtManger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {

        log.info { "Basic Authentication Filter" }

        val token = request.getHeader(jwtManager.jwtHeader)?.replace(jwtManager.jwtPrefix, "")?.trim()

        if (token == null){
            chain.doFilter(request, response)
            return
        }

        log.debug { "token: $token" }
        val memberEmail = jwtManager.getMemberEmailFromToken(token) ?: throw RuntimeException("Invalid Token")

        val member = memberRepository.findMemberByEmail(memberEmail)
        val principalDetails = PrincipalDetails(member)

        val authentication : Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            null,
            principalDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }
}
