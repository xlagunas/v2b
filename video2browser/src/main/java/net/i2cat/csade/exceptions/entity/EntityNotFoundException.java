package net.i2cat.csade.exceptions.entity;

public class EntityNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Entity doesn't exist in the database";
	}

}
