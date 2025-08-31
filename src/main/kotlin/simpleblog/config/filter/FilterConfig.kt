package simpleblog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

//    @Bean
    fun registryAuthenticationFilter(): FilterRegistrationBean<MyAuthenticationFilter> {

        var bean = FilterRegistrationBean(MyAuthenticationFilter())

        bean.addUrlPatterns("/member")
        bean.order = 0

        return bean
    }

}
