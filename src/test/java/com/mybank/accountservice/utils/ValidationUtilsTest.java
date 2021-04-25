package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

@ExtendWith(SpringExtension.class)
public class ValidationUtilsTest {

	@InjectMocks
	ValidationUtils validationUtils;

	@Test
	public void validateCreateAccountDetailsAllValidationPass() {
		AccountRequestDetails accountRequestDetails = DummyObjectProvider.setupAccountRequestDetails();
		accountRequestDetails.setBalance(new BigDecimal(15.22));
		accountRequestDetails.setCurrencies(Arrays.asList("EUR", "SEK", "USD"));
		validationUtils.validateCreateAccountDetails(accountRequestDetails);

	}

	@Test
	public void validateCreateAccountDetailsAllValidationError() {
		AccountRequestDetails accountRequestDetails = null;
		try {
			validationUtils.validateCreateAccountDetails(accountRequestDetails);
		} catch (AccountServiceException accountServiceException) {
			Assertions.assertTrue(accountServiceException.getCode() == FailureCode.CD17);
			return;
		}
		Assertions.fail();
	}

	@Test
	public void validatePerformTransactionDetailsPassValidation() {
		TransactionRequestDetails trasnsactionDetail = DummyObjectProvider.setupTransactionRequestDetails();
		validationUtils.validatePerformTransactionDetails(trasnsactionDetail);
		
	}

}
