package simpleblog.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import simpleblog.domain.AuditingEntity

@Entity
@Table(name = "Member")
class Member(
    email: String,
    password: String,
    role: Role


) : AuditingEntity() {

    @Column(name = "name", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set

    override fun toString(): String {
        return "Member(email='$email', password='$password', role=$role)"
    }

    companion object {
        fun createFakeMember(memberId: Long): Member {
            val member = Member(
                "",
                "",
                Role.USER
            )
            member.id = memberId
            return member
        }
    }

}

fun Member.toDto() =
    MemberRes(
        this.id!!,
        this.email,
        this.password,
        this.role
    )

enum class Role {
    ADMIN,
    USER
}
