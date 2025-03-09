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
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Application Exception",
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				ex.getMessage(),
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.FORBIDDEN.value(),
				"Forbidden",
				"You do not have permission to perform this action",
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized",
				"Invalid credentials",
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized",
				"Invalid or expired token",
				request.getDescription(false)
		);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
	
	
}