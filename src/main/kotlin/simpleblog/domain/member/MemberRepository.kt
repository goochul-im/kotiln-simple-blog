package simpleblog.domain.member

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, KotlinJdslJpqlExecutor {
    fun findMemberByEmail(email: String): Member

}

fun MemberRepository.findMembersByPage(pageable: Pageable): Page<Member?> =
    findPage(pageable){
        select(entity(Member::class))
            .from(entity(Member::class))
            .orderBy(path(Member::id).asc())
    }

