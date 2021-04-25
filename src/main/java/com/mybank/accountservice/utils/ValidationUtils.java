package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

@Component
public class ValidationUtils {

	// Used to Validated AccountRequestDetails for account creation
	public void validateCreateAccountDetails(AccountRequestDetails accountRequestDetails) {
		checkAndThrowExceptionIfMissing(accountRequestDetails, FailureCode.CD17);
		checkAndThrowExceptionIfMissing(accountRequestDetails.getCountry(), FailureCode.CD1);
		checkAndThrowExceptionIfMissing(accountRequestDetails.getCustomerId(), FailureCode.CD2);
		checkAndThrowExceptionIfMissing(accountRequestDetails.getCurrencies(), FailureCode.CD3);
		checkAndThrowExceptionIfMissing(accountRequestDetails.getBalance(), FailureCode.CD5);
		if (!Arrays.asList(CurrencyType.values()).stream().map(currency -> currency.toString())
				.collect(Collectors.toSet()).containsAll(accountRequestDetails.getCurrencies())) {
			throw new AccountServiceException(FailureCode.CD4, HttpStatus.BAD_REQUEST);
		}

		if (accountRequestDetails.getBalance().compareTo(new BigDecimal(0)) <= 0) {
			throw new AccountServiceException(FailureCode.CD6, HttpStatus.BAD_REQUEST);
		}
	}

	private void checkAndThrowExceptionIfMissing(Object balance, FailureCode code) {
		if (Objects.isNull(balance)) {
			throw new AccountServiceException(code, HttpStatus.BAD_REQUEST);
		}

	}

	private void checkAndThrowExceptionIfMissing(List<String> list, FailureCode code) {
		if (CollectionUtils.isEmpty(list)) {
			throw new AccountServiceException(code, HttpStatus.BAD_REQUEST);
		}

	}

	private void checkAndThrowExceptionIfMissing(String value, FailureCode code) {
		if (StringUtils.isEmpty(value)) {
			throw new AccountServiceException(code, HttpStatus.BAD_REQUEST);
		}
	}

	// Used to Validated TransactionRequestDetails for transaction operation perfromance
	public void validatePerformTransactionDetails(TransactionRequestDetails transactionRequestDetails) {
		checkAndThrowExceptionIfMissing(transactionRequestDetails, FailureCode.CD17);
		checkAndThrowExceptionIfMissing(transactionRequestDetails.getAmount(), FailureCode.CD8);
		checkAndThrowExceptionIfMissing(transactionRequestDetails.getAccountId(), FailureCode.CD7);
		checkAndThrowExceptionIfMissing(transactionRequestDetails.getTransactionCurrency(), FailureCode.CD3);
		checkAndThrowExceptionIfMissing(transactionRequestDetails.getTransactionType(), FailureCode.CD9);
		checkAndThrowExceptionIfMissing(transactionRequestDetails.getDescription(), FailureCode.CD11);
		if (!Arrays.asList(CurrencyType.values()).stream().map(currency -> currency.toString())
				.collect(Collectors.toSet()).contains(transactionRequestDetails.getTransactionCurrency())) {
			throw new AccountServiceException(FailureCode.CD4, HttpStatus.BAD_REQUEST);
		}

		if (!Arrays.asList(TransactionType.values()).stream().map(currency -> currency.toString())
				.collect(Collectors.toSet()).contains(transactionRequestDetails.getTransactionType())) {
			throw new AccountServiceException(FailureCode.CD10, HttpStatus.BAD_REQUEST);
		}
	}

}
