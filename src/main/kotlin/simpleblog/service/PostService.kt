package simpleblog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional()
    fun findPosts(pageable: Pageable): Page<Post?> {

        return postRepository.findPosts(pageable)
    }

    @Transactional(readOnly = true)
    fun savePost(dto: PostSaveReq): Post {

        return postRepository.save(dto.toEntity())

    }

    @Transactional(readOnly = true)
    fun deletePost(id: Long) {

        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id: Long): PostRes {

        return postRepository.findById(id).orElseThrow().toDto()
    }
}
