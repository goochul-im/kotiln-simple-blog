package simpleblog.config.security

import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import simpleblog.domain.member.Member
import simpleblog.domain.member.Role

class PrincipalDetails : UserDetails {

    private val email: String
    private val role: Role
    private var password: String? = null

    private val log = KotlinLogging.logger {}

    constructor(email: String, role: String) {
        this.email = email
        this.role = Role.valueOf(role.uppercase())
    }

    constructor(member: Member){
        this.email = member.email
        this.role = member.role
        this.password = member.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        log.info { "Role 검증" }
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "ROLE_${this.role}" })
        return collection
    }

    override fun getPassword(): String {
        return this.password ?: ""
    }

    override fun getUsername(): String {
        return this.email
    }

}
