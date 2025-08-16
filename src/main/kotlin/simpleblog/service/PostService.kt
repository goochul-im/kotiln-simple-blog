package simpleblog.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.post.Post
import simpleblog.domain.post.PostRepository
import simpleblog.domain.post.PostRes
import simpleblog.domain.post.toDto

@Service
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional(readOnly = true)
    fun findPosts(): List<PostRes> {

        return postRepository.findAll().map { it.toDto() }
    }
}
