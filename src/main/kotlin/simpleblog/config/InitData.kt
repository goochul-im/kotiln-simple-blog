package simpleblog.config

import io.github.serpro69.kfaker.Faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository
import simpleblog.domain.member.MemberSaveReq
import simpleblog.domain.member.Role
import simpleblog.domain.member.toEntity
import simpleblog.service.MemberService


@Configuration
class InitData(
    private val memberRepository: MemberRepository
) {

    val faker = Faker()
    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val members = mutableListOf<Member>()

        for (i in 1..100){
            val member = generateMember()
            log.info { "insert $member" }
            members.add(member)
        }

        memberRepository.saveAll(members)
    }

    private fun generateMember(): Member =
        MemberSaveReq(
            faker.internet.email(),
            "1234",
            Role.USER
        ).toEntity()

}
