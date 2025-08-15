package simpleblog.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import simpleblog.service.MemberService

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/member")
    fun findAll() = memberService.findAll()

}
