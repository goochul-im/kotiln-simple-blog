package simpleblog.config

import io.github.serpro69.kfaker.Faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRepository
import simpleblog.domain.member.LoginDto
import simpleblog.domain.member.Role
import simpleblog.domain.member.toEntity
import simpleblog.domain.post.Post
import simpleblog.domain.post.PostRepository
import kotlin.random.Random


@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) {

    val faker = Faker()
    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val count = memberRepository.count()
        if (count >= 200) return

        val members = mutableListOf<Member>()

        for (i in 1..100) {
            val member = generateMember()
            log.info { "insert member: $member" }
            members.add(member)
        }
        memberRepository.saveAll(members)


        val posts = mutableListOf<Post>()
        members.forEach { member ->
            for (i in 1..Random.nextInt(1, 5)) {
                val post = generatePost(member)
//                log.info { "insert post: $post" }
                posts.add(post)
            }
        }
        postRepository.saveAll(posts)
    }

    private fun generateMember(): Member =
        LoginDto(
            faker.internet.email(),
            BCryptPasswordEncoder().encode("1234"),
            Role.USER
        ).toEntity()


    private fun generatePost(member: Member): Post =
        Post(
            title = faker.lorem.words(),
            content = faker.lorem.supplemental(),
            member = member
        )

}
