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

}

enum class Role {
    ADMIN,
    USER
}
