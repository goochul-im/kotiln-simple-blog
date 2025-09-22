package simpleblog.exception

class InvalidRefreshTokenException(string: String) : BusinessException(string, ErrorCode.INVALID_JWT) {



}
