package com.mybank.accountservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class AccountServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;
	private String message;
	 /*
	   * Required when we want to add a custom message when throwing the exception
	   * as throw new AccountServiceException(" Custom Unchecked Exception ");
	   */
	   public AccountServiceException(String message) {
	      // calling super invokes the constructors of all super classes
	      // which helps to create the complete stacktrace.
	      super(message);
	      this.message=message;
	   }
	   /*
	   * Required when we want to wrap the exception generated inside the catch block and rethrow it
	   * as catch(ArrayIndexOutOfBoundsException e) {
	      * throw new AccountServiceException(e);
	   * }
	   */
	   public AccountServiceException(Throwable cause) {
	      // call appropriate parent constructor
	      super(cause);
	   }
	   /*
	   * Required when we want both the above
	   * as catch(ArrayIndexOutOfBoundsException e) {
	      * throw new AccountServiceException(e, "File not found");
	   * }
	   */
	   public AccountServiceException(String message, Throwable throwable) {
	      // call appropriate parent constructor
	      super(message, throwable);
	      this.message=message;
	   }
	   
	   
	   /*
		   * Required when we want to add a custom message when throwing the exception
		   * as throw new AccountServiceException(" Custom Unchecked Exception ") and Want to set Http Status code for caller Service;
		   */
		   public AccountServiceException(String message,HttpStatus httpStatus) {
		      // calling super invokes the constructors of all super classes
		      // which helps to create the complete stacktrace.
		      super(message);
		      this.message=message;
		      this.httpStatus=httpStatus;
		      
		   }
}
