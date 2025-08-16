package simpleblog.domain.post

import simpleblog.domain.member.Member
import simpleblog.domain.member.MemberRes

data class PostSaveReq(
    val title: String,
    val content: String,
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
