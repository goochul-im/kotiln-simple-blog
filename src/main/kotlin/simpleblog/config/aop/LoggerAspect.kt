package simpleblog.config.aop

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 전통적인 방식의 프록시 기반 스프링 AOP
 * 코틀린은 뭐가 다른가?
 */

@Component
@Aspect
class LoggerAspect(private val mapper: ObjectMapper) {

    val log = KotlinLogging.logger {}

    @Pointcut("execution(* simpleblog.api.*Controller.*(..))")
    private fun controllerCut() = Unit

    @Before(value = "controllerCut()")
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

    @AfterReturning(pointcut = "controllerCut()", returning = "result")
    fun controllerLogAfter(joinPoint: JoinPoint, result: Any) {


        log.info {
            """
                
            ${joinPoint.signature.name} 
            Method return value : ${mapper.writeValueAsString(result)}
            
            """.trimIndent()


        }

    }



}
