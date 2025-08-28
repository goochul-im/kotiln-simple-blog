package simpleblog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@SpringBootApplication
class SimpleBlogApplication

fun main(args: Array<String>) {
    runApplication<SimpleBlogApplication>(*args)
}

/**
 *  사이드 컨텐츠?
 */
