package simpleblog.domain.member

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, KotlinJdslJpqlExecutor {


}

fun MemberRepository.findMember() =
    findAll{
        select(entity(Member::class))
            .from(entity(Member::class))
            .orderBy(path(Member::id).asc())
    }
