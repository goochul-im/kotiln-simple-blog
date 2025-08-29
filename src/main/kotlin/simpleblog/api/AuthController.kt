package simpleblog.api

import jakarta.servlet.http.HttpSession
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    val log = KotlinLogging.logger {}

    @PostMapping("/login")
    fun login(session: HttpSession) {

        session.setAttribute("principal", "pass")


    }

}
