package simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import simpleblog.config.logout.CustomLogoutHandler
import simpleblog.config.logout.CustomLogoutSuccessHandler
import simpleblog.config.security.exceptionHandler.CustomAccessDeniedHandler
import simpleblog.config.security.exceptionHandler.CustomAuthenticationEntryPoint
import simpleblog.config.security.loginHandler.CustomFailureHandler
import simpleblog.config.security.loginHandler.CustomSuccessfulHandler
import simpleblog.service.AuthService

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val customLogoutSuccessHandler: CustomLogoutSuccessHandler,
    private val customLogoutHandler: CustomLogoutHandler,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val authService: AuthService
) {

    private val log = mu.KotlinLogging.logger {}

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http.authorizeHttpRequests { auth ->
            auth.requestMatchers("/login", "/auth/**", "/error").permitAll()
                .anyRequest().authenticated()
        }
            .cors { }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAt(authenticationFilter(authenticationManager(), authService), BasicAuthenticationFilter::class.java)
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 예외
                exception.accessDeniedHandler(customAccessDeniedHandler) // 인가 예외
            }
            .logout { logout ->
                logout.logoutUrl("/logout")
                    .addLogoutHandler(customLogoutHandler) // 로그아웃 로직
                    .logoutSuccessHandler(customLogoutSuccessHandler) // 로그아웃 후 처리하는 로직 (response)
            }

        return http.build()
    }

    @Bean
    fun customSuccessfulHandler(): CustomSuccessfulHandler {
        return CustomSuccessfulHandler()
    }

    @Bean
    fun customFailureHandler(): CustomFailureHandler {
        return CustomFailureHandler()
    }

    fun authenticationFilter(authenticationManager: AuthenticationManager, authService: AuthService): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(authenticationManager, authService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    fun loginFilter(): CustomUsernamePasswordAuthenticationFilter {
        val filter =
            CustomUsernamePasswordAuthenticationFilter(objectMapper, authenticationManager())
        filter.setAuthenticationFailureHandler(customFailureHandler())
        filter.setAuthenticationSuccessHandler(customSuccessfulHandler())
        return filter
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
