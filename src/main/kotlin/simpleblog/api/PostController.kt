package simpleblog.api

import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import simpleblog.domain.post.PostSaveReq
import simpleblog.service.PostService
import simpleblog.util.value.CmResDto

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping()
    fun findPosts(
        @PageableDefault(size = 10, sort = ["id"]) pageable: Pageable
    ) : CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find Posts", postService.findPosts(pageable))
    }

    @GetMapping("/{id}")
    fun findPostById(@PathVariable id: Long): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find post by id", postService.findPostById(id))
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK, "delete post by id", postService.deletePost(id)
        )
    }

    @PostMapping()
    fun savePost(
        @Valid @RequestBody dto: PostSaveReq
    ): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find all posts", postService.savePost(dto))
    }

}
