package simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import simpleblog.domain.member.MemberRepository

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository
) {

    private val log = mu.KotlinLogging.logger {}

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http.authorizeHttpRequests { auth ->
            auth.requestMatchers("/login", "/auth/**").permitAll()
                .anyRequest().authenticated()
        }
            .cors { }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAt(authenticationFilter(authenticationManager()), BasicAuthenticationFilter::class.java)
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))
                exception.accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
            }

        return http.build()
    }

    class CustomAuthenticationEntryPoint(
        private val objectMapper: ObjectMapper
    ) : AuthenticationEntryPoint {

        private val log = mu.KotlinLogging.logger {}

        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException
        ) {
            log.warn { "인증 실패 (401 Unauthorized): ${authException.message}, 요청 URI: ${request.requestURI}" }

            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"

            val errorResponse = mapOf(
                "status" to HttpStatus.UNAUTHORIZED.value(),
                "error" to "Unauthorized",
                "message" to "인증이 필요합니다. 로그인을 진행해주세요."
            )

            val writer = response.writer
            writer.write(objectMapper.writeValueAsString(errorResponse))
            writer.flush()
        }
    }

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

    @Bean
    fun authenticationFilter(authenticationManager: AuthenticationManager): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(authenticationManager, memberRepository)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun loginFilter(): CustomUsernamePasswordAuthenticationFilter {
        return CustomUsernamePasswordAuthenticationFilter(objectMapper, authenticationManager())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            // 허용할 출처(Origin)를 지정합니다.
            // allowedOrigins = listOf("http://localhost:3000", "https://my-frontend.com")
            // 패턴을 사용하여 유연하게 설정할 수도 있습니다.
            allowedOriginPatterns = listOf("*")

            // 허용할 HTTP 메서드를 지정합니다.
            allowedMethods = listOf("*")

            // 허용할 HTTP 헤더를 지정합니다.
            allowedHeaders = listOf("*") // 모든 헤더 허용

            // 쿠키 등 자격 증명(Credentials)을 허용할지 여부
            allowCredentials = true

            // 브라우저에 노출할 헤더를 지정합니다.
            exposedHeaders = listOf("Authorization", "X-Custom-Header")
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            // 모든 경로에 대해 위에서 정의한 CORS 정책을 적용합니다.
            registerCorsConfiguration("/**", configuration)
        }

        return source
    }

}