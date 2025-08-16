package simpleblog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository
import simpleblog.domain.member.MemberRes
import simpleblog.domain.member.toDto

@Service
class MemberService (
    private val memberRepository: MemberRepository

){

    @Transactional(readOnly = true)
    fun findAll(): List<MemberRes> = memberRepository.findAll().map { it.toDto() }


}
