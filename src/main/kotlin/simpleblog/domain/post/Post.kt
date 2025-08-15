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

@Entity
@Table(name = "Post")
class Post(
    title:String,
    content:String,
    member: Member
) : AuditingEntity(){

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set
}
