package net.i2cat.csade.controllers;

import java.io.IOException;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Error;
import net.i2cat.csade.models.Error.ErrorType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractExceptionController {

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	@ResponseBody protected Error sendEentityNotFound(){
		return new Error(ErrorType.USER, "User not found");
	}
	
	@ExceptionHandler(ExistingEntityException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	@ResponseBody protected Error sendExistingEntity(){
		return new Error(ErrorType.USER, "User not found");
	}
	@ExceptionHandler(IOException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	@ResponseBody protected Error sendIOException(){
		return new Error(ErrorType.USER, "User not found");
	}
}
