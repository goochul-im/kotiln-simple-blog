package simpleblog.config.aop

import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
@Aspect
class LoggerAspect {

    val log = KotlinLogging.logger {}

    @Pointcut("execution(* simpleblog.api.*Controller.*(..))")
    private fun controllerCut() = Unit

    @Before("controllerCut()")
    fun controllerLoggerAdvice(joinPoint: JoinPoint){

        var typeName = joinPoint.signature.declaringTypeName
        val methodname = joinPoint.signature.name

        var request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

        log.info { """
            
            request url: ${request.servletPath}
            type: $typeName
            method: $methodname
            
        """.trimIndent() }

    }

}
