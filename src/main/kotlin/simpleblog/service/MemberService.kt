package simpleblog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository
import simpleblog.domain.member.MemberRes
import simpleblog.domain.member.MemberSaveReq
import simpleblog.domain.member.findMembersByPage
import simpleblog.domain.member.toDto
import simpleblog.domain.member.toEntity

@Service
class MemberService(
    private val memberRepository: MemberRepository

) {

    @Transactional(readOnly = true)
    fun findAll(): List<MemberRes> = memberRepository.findAll().mapNotNull { it?.toDto() }
    // it(member)이 null일 경우는 자동으로 제외

    @Transactional(readOnly = true)
    fun findAllByPage(pageable: Pageable): Page<Member?> = memberRepository.findMembersByPage(pageable)

    @Transactional(readOnly = true)
    fun saveMember(dto: MemberSaveReq): Member {

        return memberRepository.save(dto.toEntity())

    }

    @Transactional(readOnly = true)
    fun deleteMember(id: Long) {

        return memberRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id: Long): MemberRes {

        return memberRepository.findById(id).orElseThrow().toDto()
    }

}
