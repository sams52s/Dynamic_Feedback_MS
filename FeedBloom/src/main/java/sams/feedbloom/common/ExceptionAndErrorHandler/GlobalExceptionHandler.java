package sams.feedbloom.common.ExceptionAndErrorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import sams.feedbloom.authentication.exception.BaseCustomAuthException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BaseCustomAuthException.class)
	public ResponseEntity<ErrorResponse> handleCustomExceptions(BaseCustomAuthException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                     .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Application Exception", ex.getMessage(), request));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult()
		                               .getFieldErrors()
		                               .stream()
		                               .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacement) -> existing));
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                     .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
		                     .body(buildErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to perform this action", request));
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                     .body(buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid credentials", request));
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                     .body(buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid or expired token", request));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                     .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                     .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error", ex.getMessage(), request));
	}
	
	private ErrorResponse buildErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
		return new ErrorResponse(
				LocalDateTime.now(),
				status.value(),
				error,
				message,
				request.getDescription(false)
		);
	}
}
