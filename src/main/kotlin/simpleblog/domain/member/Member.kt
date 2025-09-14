package simpleblog.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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

    fun toDto() =
        MemberRes(
            this.id!!,
            this.email,
            this.password,
            this.role,
            createdAt = this.createdAt,
            lastUpdatedAt = this.updatedAt
        )

    override fun toString(): String {
        return "Member(email='$email', password='$password', role=$role, createdAt=$createdAt, updatedAt=$updatedAt)"
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



enum class Role {
    ADMIN,
    USER
}
