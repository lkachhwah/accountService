package com.mybank.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mybank.accountservice.dto.ErrorResponseDto;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.exception.AccountServiceException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	@ExceptionHandler(value = AccountServiceException.class)
	public ResponseEntity<ErrorResponseDto> exception(AccountServiceException exception) {
		return new ResponseEntity<>(ErrorResponseDto.builder().code(exception.getCode())
				.message(exception.getCode().getMessage()).httpStatus(exception.getHttpStatus()).build(),
				exception.getHttpStatus());
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponseDto> exception(Exception exception) {
		FailureCode code = FailureCode.CD15;
		return new ResponseEntity<>(ErrorResponseDto.builder().code(code).message(exception.getMessage())
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
