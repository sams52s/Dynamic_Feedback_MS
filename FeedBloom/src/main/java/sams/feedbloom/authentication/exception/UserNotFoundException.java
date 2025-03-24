package sams.feedbloom.authentication.exception;

public final class UserNotFoundException extends BaseCustomAuthException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
