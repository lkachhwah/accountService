package com.mybank.accountservice.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
	private com.mybank.accountservice.enums.FailureCode code;
	private String message;
	private HttpStatus httpStatus;
}
