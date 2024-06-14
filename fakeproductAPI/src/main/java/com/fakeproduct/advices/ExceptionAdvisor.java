package com.fakeproduct.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fakeproduct.dto.ExceptionDTO;
import com.fakeproduct.exceptions.EmptyListException;
import com.fakeproduct.exceptions.ProductNotFoundException;

@ControllerAdvice
public class ExceptionAdvisor {
	@ExceptionHandler(value = ProductNotFoundException.class)
	public @ResponseBody ExceptionDTO handleProductNotFoundException(Exception exception) {
		return new ExceptionDTO(HttpStatus.NOT_FOUND.value(),exception.getMessage());
	}
	@ExceptionHandler(value=EmptyListException.class)
	public @ResponseBody ExceptionDTO handleEmptyListException(Exception exception) {
		return new ExceptionDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage());
	}

}
