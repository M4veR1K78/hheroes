package mav.com.hheroes.services.exceptions;

public class ObjectNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException(String message) {
		super(message);
	}
	
	public ObjectNotFoundException(String message, Throwable e) {
		super(message, e);
	}

}
