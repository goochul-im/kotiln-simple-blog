package simpleblog.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import simpleblog.service.PostService
import simpleblog.util.value.CmResDto

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts() : CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find Posts", postService.findPosts())
    }

}
