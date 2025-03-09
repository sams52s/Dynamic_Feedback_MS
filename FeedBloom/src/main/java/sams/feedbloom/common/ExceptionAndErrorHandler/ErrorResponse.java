package sams.feedbloom.common.ExceptionAndErrorHandler;

import java.time.LocalDateTime;

//record provides an immutable, compact representation.
public record ErrorResponse(
		LocalDateTime timestamp,
		int status,
		String error,
		String message,
		String path
) {}