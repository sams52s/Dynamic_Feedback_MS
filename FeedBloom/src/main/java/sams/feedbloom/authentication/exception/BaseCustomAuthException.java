package sams.feedbloom.authentication.exception;

//A sealed hierarchy ensures that only specific exceptions extend
public sealed class BaseCustomAuthException extends RuntimeException
		permits EmailAlreadyExistsException, UserNotFoundException {
	
	public BaseCustomAuthException(String message) {
		super(message);
	}
}
