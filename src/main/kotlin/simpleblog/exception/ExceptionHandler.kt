package simpleblog.exception

import jakarta.persistence.NoResultException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    val log = KotlinLogging.logger {}

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "MethodArgumentNotValidException: $e" }

        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)

        return ResponseEntity(of, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.error { "HttpMessageNotReadableException: $e" }

        val response = ErrorResponse(ErrorCode.INVALID_INPUT_VALUE)

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleMethodArgumentNotValidException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.error { "EntityNotFoundException: $e" }

        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)

        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NoResultException::class)
    fun handleMethodArgumentNotValidException(e: NoResultException): ResponseEntity<ErrorResponse> {
        log.error { "NoResultException: $e" }

        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)

        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
