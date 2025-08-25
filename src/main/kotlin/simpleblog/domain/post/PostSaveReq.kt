package simpleblog.domain.post

import org.jetbrains.annotations.NotNull
import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRes

data class PostSaveReq(
    @field:NotNull("제목은 비어있을 수 없습니다")
    val title: String?,
    val content: String,
    val memberId: Long
) {
}

fun PostSaveReq.toEntity() = Post(title ?: "", content, Member.createFakeMember(this.memberId))

data class PostRes(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberRes
)
