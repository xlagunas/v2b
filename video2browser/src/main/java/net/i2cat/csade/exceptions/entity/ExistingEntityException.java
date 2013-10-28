package net.i2cat.csade.exceptions.entity;

public class ExistingEntityException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Entity already exists in the database";
	}

}
