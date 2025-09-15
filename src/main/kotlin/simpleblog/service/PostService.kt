package simpleblog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simpleblog.domain.post.Post
import simpleblog.domain.post.PostRepository
import simpleblog.domain.post.PostRes
import simpleblog.domain.post.PostSaveReq
import simpleblog.domain.post.findPosts
import simpleblog.domain.post.toDto
import simpleblog.domain.post.toEntity

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository
) {

    @Secured("ROLE_USER")
    fun findPosts(pageable: Pageable): Page<Post?> {

        return postRepository.findPosts(pageable)
    }

    fun savePost(dto: PostSaveReq): Post {

        return postRepository.save(dto.toEntity())

    }

    fun deletePost(id: Long) {

        return postRepository.deleteById(id)
    }

    fun findPostById(id: Long): PostRes {

        return postRepository.findById(id).orElseThrow().toDto()
    }
}
