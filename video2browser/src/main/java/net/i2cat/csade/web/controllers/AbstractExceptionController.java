package net.i2cat.csade.web.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Error;
import net.i2cat.csade.models.Error.ErrorType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		return new Error(ErrorType.USER, "User already registered");
	}
	@ExceptionHandler(IOException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	@ResponseBody protected Error sendIOException(){
		return new Error(ErrorType.USER, "User not found");
	}
	
//	 @RequestMapping(method = RequestMethod.OPTIONS, value={"/**"})
//	    public void manageOptions(HttpServletResponse response)
//	    {
//	        System.out.println("Entra al options method");
//	    }
}
