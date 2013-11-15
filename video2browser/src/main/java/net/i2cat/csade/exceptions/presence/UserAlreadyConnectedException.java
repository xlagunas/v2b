package net.i2cat.csade.exceptions.presence;

public class UserAlreadyConnectedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		
		return "User already connected";
	}

}
