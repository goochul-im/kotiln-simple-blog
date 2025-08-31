package simpleblog.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import simpleblog.config.security.PrincipalDetails
import simpleblog.domain.member.MemberRepository

@Service
class AuthService(
    private val memberRepo: MemberRepository
) : UserDetailsService {

    val log = mu.KotlinLogging.logger {}

    override fun loadUserByUsername(email: String): UserDetails {
        var findMember = memberRepo.findMemberByEmail(email)
        log.info { "Member found: $findMember" }
        return PrincipalDetails(findMember)
    }
}
