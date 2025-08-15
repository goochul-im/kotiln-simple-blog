package simpleblog.domain.comment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import simpleblog.domain.AuditingEntity
import simpleblog.domain.member.Member
import simpleblog.domain.post.Post

@Entity
@Table(name = "Comment")
class Comment(
    title:String,
    content: String,
    post: Post
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    var post: Post = post
        protected set
}
