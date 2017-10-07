package mav.com.hheroes.web.exceptions;

public class WebAuthenticationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public WebAuthenticationException(String message) {
		super(message);
	}
	
	public WebAuthenticationException(String message, Throwable e) {
		super(message, e);
	}

}
