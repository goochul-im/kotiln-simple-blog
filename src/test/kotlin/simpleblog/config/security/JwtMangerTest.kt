package simpleblog.config.security

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import simpleblog.domain.member.Member
import simpleblog.domain.member.Role

class JwtMangerTest {

    private val jwtManager = JwtManager()

    @Test
    @DisplayName("access token 생성 테스트")
    fun generateAccessTokenTest() {
        // given
        val member = Member("test@gmail.com", "password", Role.USER)
        val principalDetails = PrincipalDetails(member)

        // when
        val accessToken = jwtManager.generateAccessToken(principalDetails)

        // then
        Assertions.assertThat(accessToken).isNotEmpty()
    }

    @Test
    @DisplayName("access token으로 부터 member email 추출 테스트")
    fun getMemberEmailFromTokenTest() {
        // given
        val member = Member("test@gmail.com", "password", Role.USER)
        val principalDetails = PrincipalDetails(member)
        val accessToken = jwtManager.generateAccessToken(principalDetails)

        // when
        val memberEmailFromToken = jwtManager.getMemberEmailFromToken(accessToken)

        // then
        Assertions.assertThat(memberEmailFromToken).isEqualTo(member.email)
    }
}
