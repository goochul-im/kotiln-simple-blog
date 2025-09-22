package simpleblog.config.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import simpleblog.config.security.util.JwtNameConstant
import simpleblog.config.security.util.SecurityResponseHandler
import simpleblog.exception.InvalidRefreshTokenException
import simpleblog.service.AuthService
import simpleblog.util.CookieProvider

class CustomBasicAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val authService: AuthService
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

        try {
            // 1. 액세스 토큰 검증
            val decodedJWT = jwtManager.validateAccessToken(token)
            setAuthentication(decodedJWT)
        } catch (e: TokenExpiredException) { // 2. 액세스 토큰이 만료되었으면
            log.warn("Access Token has expired. Attempting to refresh...")
            handleTokenRefresh(request, response) // 3. 리프레시 토큰으로 재발급 시도
        } catch (e: Exception) { // 4. 그 외 모든 에러
            log.error("Invalid Token: ${e.message}")
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(request, response)
    }

    private fun setAuthentication(decodedJWT: DecodedJWT) {
        val email = jwtManager.getMemberEmailFromToken(decodedJWT)
        val role = jwtManager.getMemberRoleFromToken(decodedJWT)
        val roleForEnum = role.replace("ROLE_", "")

        val principalDetails = PrincipalDetails(email = email, role = roleForEnum)
        val authorities = listOf(SimpleGrantedAuthority(role))

        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            null,
            authorities
        )

        SecurityContextHolder.getContext().authentication = authentication
    }

    // 토큰 재발급 로직을 별도 메서드로 분리
    private fun handleTokenRefresh(request: HttpServletRequest, response: HttpServletResponse) {
        try {
            // 쿠키에서 리프레시 토큰 가져오기
            val refreshToken = CookieProvider.getCookie(request, JwtNameConstant.REFRESH_TOKEN_NAME)
                .orElseThrow { InvalidRefreshTokenException("Refresh token not found in cookie.") }

            // 새로운 액세스 토큰 발급 (이 과정에서 리프레시 토큰 검증도 함께 수행)
            val newAccessToken = authService.reissueAccessToken(refreshToken)
            val newDecodedJWT = jwtManager.validateAccessToken(newAccessToken)
            setAuthentication(newDecodedJWT)

            response.setHeader(jwtManager.jwtHeader, jwtManager.jwtPrefix + newAccessToken)
            log.info("Successfully reissued access token.")

        } catch (e: InvalidRefreshTokenException) {
            // 리프레시 토큰이 유효하지 않거나 만료된 경우
            log.warn("Failed to refresh token: ${e.message}")
            SecurityContextHolder.clearContext()

            SecurityResponseHandler.sendErrorResponse(
                response,
                HttpStatus.UNAUTHORIZED,
                "Invalid or expired refresh token."
            )

            return
        } catch (e: Exception) {
            // 그 외 재발급 과정에서 발생할 수 있는 서버 오류
            log.error("An unexpected error occurred during token refresh.", e)
            SecurityContextHolder.clearContext()
        }
    }

}
