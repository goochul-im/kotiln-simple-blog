package simpleblog.domain.post

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRes

data class PostSaveReq(
    @field:NotBlank(message = "제목은 비어있을 수 없습니다")
    val title: String,
    val content: String,
    @field:NotNull(message = "memberId는 비어있을 수 없습니다")
    val memberId: Long
) {
}

fun PostSaveReq.toEntity() = Post(title, content, Member.createFakeMember(this.memberId))

data class PostRes(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberRes
)