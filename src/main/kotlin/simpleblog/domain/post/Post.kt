package simpleblog.domain.post

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import simpleblog.domain.AuditingEntity
import simpleblog.domain.member.Member
import simpleblog.domain.member.toDto

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String,
    member: Member
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

    override fun toString(): String {
        return "Post(title='$title', content='$content', member=$member)"
    }

}

fun Post.toDto() =
    PostRes(
        this.id!!,
        this.title,
        this.content,
        this.member.toDto()
    )

