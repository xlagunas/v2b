package net.i2cat.csade.models;

public class Error {
	public enum ErrorType {USER, RELATIONSHIP}
	
	private ErrorType errorType;
	private String description;
	
	public Error(ErrorType type, String description){
		this.errorType = type;
		this.description = description;
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}
	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
