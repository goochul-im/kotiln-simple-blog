package simpleblog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository

@Service
class MemberService (
    private val memberRepository: MemberRepository

){

    @Transactional(readOnly = true)
    fun findAll(): MutableList<Member> = memberRepository.findAll()



}
