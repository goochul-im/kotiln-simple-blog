package simpleblog.exception

enum class ErrorCode(
    val code: String,
    val message: String
) {

    INVALID_INPUT_VALUE("C001", "Invalid Input Value"),
    ENTITY_NOT_FOUND("C002", "Entity Not Found"),
    ENTITY_ALREADY_EXISTS("C003", "Entity Already Exists"),
    SYSTEM_ERROR("C999", "System Error")

}
