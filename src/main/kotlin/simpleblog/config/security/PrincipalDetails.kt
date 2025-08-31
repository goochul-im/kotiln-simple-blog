package simpleblog.config.security

import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import simpleblog.domain.member.Member

class PrincipalDetails(
    member: Member
) : UserDetails {

    var member: Member = member
        private set

    private val log = KotlinLogging.logger {}

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        log.info { "Role 검증" }
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "ROLE" + member.role })
        return collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

}
