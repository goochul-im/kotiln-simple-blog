package simpleblog.api

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import simpleblog.domain.member.MemberSaveReq
import simpleblog.service.MemberService
import simpleblog.util.value.CmResDto

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/member")
    fun findAllByPage(
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable
    ): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "success", memberService.findAllByPage(pageable))
    }

    @GetMapping("/member/{id}")
    fun findMemberById(@PathVariable id: Long): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find member by id", memberService.findMemberById(id))
    }

    @DeleteMapping("/member/{id}")
    fun deleteMember(@PathVariable id: Long): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK, "delete member by id", memberService.deleteMember(id)
        )
    }

    @PostMapping("/member")
    fun saveMember(
        @RequestBody dto: MemberSaveReq
    ): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "save members", memberService.saveMember(dto))
    }

}
