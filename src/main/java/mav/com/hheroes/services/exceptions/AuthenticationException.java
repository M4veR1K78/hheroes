package mav.com.hheroes.services.exceptions;

public class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable e) {
		super(message, e);
	}

}
