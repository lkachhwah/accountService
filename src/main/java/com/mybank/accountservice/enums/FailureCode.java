package com.mybank.accountservice.enums;

import lombok.ToString;

@ToString
public enum FailureCode {
	 CD1("Country is missing in request"),
	 CD2("CustomerId is missing in request"),
	 CD3("Currencies is missing in request"),
	 CD4("Invalid Currency"),
	 CD5("Intial Balance is missing  in request"),
	 CD6("Intial Balance is negative  in request"),
	 CD7("AccountId is missing in request"),
	 CD8("Amount is Invalid"),
	 CD9("TransactionType is missing in request"),
	 CD10("TransactionType is Invalid"),
	 CD11("Description is missing in request"),
	 CD12("AccountId is invalid"),
	 CD13("Insufficient Balance"),
	 CD14("CustomerId is does not exist"),
	 CD15("Internal porcessing Error"),
	 CD16("Multiple transaction found please try after sometime");
	 
	 
	 
	
	private String message;
	
	 FailureCode (String message)
	{
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
