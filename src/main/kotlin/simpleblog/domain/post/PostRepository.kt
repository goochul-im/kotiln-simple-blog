package simpleblog.domain.post

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import simpleblog.domain.member.Member

interface PostRepository: JpaRepository<Post, Long>, KotlinJdslJpqlExecutor {

}

fun PostRepository.findPosts(pageable: Pageable) : Page<Post?> =
    findPage<Post>(pageable){
        select(entity(Post::class))
           .from(entity(Post::class),
               fetchJoin(Post::member,)
           )
           .orderBy(path(Post::id).asc())
    }


