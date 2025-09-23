package simpleblog.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import simpleblog.config.security.JwtManager
import simpleblog.config.security.PrincipalDetails
import simpleblog.domain.member.MemberRepository

@Service
class AuthService(
    private val memberRepo: MemberRepository,
    private val jwtManager: JwtManager
) : UserDetailsService {

    val log = mu.KotlinLogging.logger {}

    override fun loadUserByUsername(email: String): UserDetails {
        var findMember = memberRepo.findMemberByEmail(email)
        log.info { "Member found: $findMember" }
        return PrincipalDetails(findMember)
    }

    fun reissueAccessToken(refreshToken: String): String {
        val decodedJWT = jwtManager.validateRefreshToken(refreshToken)
        val email = jwtManager.getMemberEmailFromToken(decodedJWT)
        val role = jwtManager.getMemberRoleFromToken(decodedJWT)
        val roleForEnum = role.replace("ROLE_", "")

        val principal = PrincipalDetails(email, roleForEnum)

        return jwtManager.generateAccessToken(principal)
    }
}
