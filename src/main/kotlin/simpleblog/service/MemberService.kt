package simpleblog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository
import simpleblog.domain.member.MemberRes
import simpleblog.domain.member.LoginDto
import simpleblog.domain.member.findMembersByPage
import simpleblog.domain.member.toEntity
import simpleblog.exception.MemberNotFoundException

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository

) {

    fun findAll(): List<MemberRes> = memberRepository.findAll().mapNotNull { it?.toDto() }
    // it(member)이 null일 경우는 자동으로 제외

    fun findAllByPage(pageable: Pageable): Page<Member?> = memberRepository.findMembersByPage(pageable)

    @Transactional
    fun saveMember(dto: LoginDto): Member {

        return memberRepository.save(dto.toEntity())

    }

    fun deleteMember(id: Long) {

        return memberRepository.deleteById(id)
    }

    fun findMemberById(id: Long): MemberRes {

        return memberRepository.findById(id).orElseThrow {
            MemberNotFoundException(id.toString())
        }.toDto()
    }

}
