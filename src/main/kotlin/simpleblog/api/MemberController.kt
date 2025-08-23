package simpleblog.api

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import simpleblog.service.MemberService
import simpleblog.util.value.CmResDto

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/member")
    fun findAllByPage(
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable
    ) : CmResDto<*> {
        return CmResDto(HttpStatus.OK, "success", memberService.findAllByPage(pageable))
    }

}
