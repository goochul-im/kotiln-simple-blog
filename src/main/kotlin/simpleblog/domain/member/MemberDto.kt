package simpleblog.domain.member

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * dto <=> entity 간의 맵핑할 때, 스타일이 2개 있음
 * 1. 각 dto, entity에 책임 할당 (내가 아는 방식)
 * 2. entityMapper를 만들어서 책임 할당
 */

data class LoginDto(
    @field:NotNull(message = "email must not be null")
    val email: String,
    val password: String,
    val role: Role
){
}

// 확장 함수
// 실제 클래스의 멤버처럼 사용할 수 있음
// DTO -> Entity로 변환
fun LoginDto.toEntity() = Member(email, password, role)

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,
    val createdAt : LocalDateTime?,
    val lastUpdatedAt : LocalDateTime?
)
