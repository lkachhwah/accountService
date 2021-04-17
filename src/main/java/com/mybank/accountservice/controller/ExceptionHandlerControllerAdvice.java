package com.mybank.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mybank.accountservice.exception.AccountServiceException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	   @ExceptionHandler(value = AccountServiceException.class)
	   public ResponseEntity<Object> exception(AccountServiceException exception) {
	      return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
	   }
	   
	   
	   @ExceptionHandler(value = HttpMessageNotReadableException.class)
	   public ResponseEntity<Object> exception(HttpMessageNotReadableException exception) {
	      return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	   }
	   
	 
	   @ExceptionHandler(value = Exception.class)
	   public ResponseEntity<Object> exception(Exception exception) {
	      return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	   }

}
