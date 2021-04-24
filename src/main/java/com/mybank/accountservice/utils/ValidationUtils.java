package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

@Component
public class ValidationUtils {
	
	 

	public void validateCreateAccountDetails(AccountRequestDetails accountRequestDetails) {
		if (Objects.nonNull(accountRequestDetails)) {
			if (StringUtils.isEmpty(accountRequestDetails.getCountry())) {
				throw new AccountServiceException(FailureCode.CD1, HttpStatus.BAD_REQUEST);
			}
			if (StringUtils.isEmpty(accountRequestDetails.getCustomerId())) {
				throw new AccountServiceException(FailureCode.CD2, HttpStatus.BAD_REQUEST);
			}
			if (CollectionUtils.isEmpty(accountRequestDetails.getCurrencies())) {
				throw new AccountServiceException(FailureCode.CD3, HttpStatus.BAD_REQUEST);
			}
			if (!Arrays.asList(CurrencyType.values()).stream().map(currency -> currency.toString())
					.collect(Collectors.toSet()).containsAll(accountRequestDetails.getCurrencies())) {
				throw new AccountServiceException(FailureCode.CD4, HttpStatus.BAD_REQUEST);
			}

			if (StringUtils.isEmpty(accountRequestDetails.getBalance())) {
				throw new AccountServiceException(FailureCode.CD5, HttpStatus.BAD_REQUEST);
			}
			if (accountRequestDetails.getBalance().compareTo(new BigDecimal(0)) <= 0) {
				throw new AccountServiceException(FailureCode.CD6, HttpStatus.BAD_REQUEST);
			}
		}
	}
	

	public void validatePerformTransactionDetails(TransactionRequestDetails transactionRequestDetails) {
		if (Objects.nonNull(transactionRequestDetails)) {
			if (Objects.nonNull(transactionRequestDetails.getAmount()) && transactionRequestDetails.getAmount().compareTo(new BigDecimal(0))<=0) {
				throw new AccountServiceException(FailureCode.CD8, HttpStatus.BAD_REQUEST);
			}
			if (StringUtils.isEmpty(transactionRequestDetails.getAccountId())) {
				throw new AccountServiceException(FailureCode.CD7, HttpStatus.BAD_REQUEST);
			}
			if (StringUtils.isEmpty(transactionRequestDetails.getTransactionCurrency())) {
				throw new AccountServiceException(FailureCode.CD3, HttpStatus.BAD_REQUEST);
			}
			if (!Arrays.asList(CurrencyType.values()).stream().map(currency -> currency.toString())
					.collect(Collectors.toSet()).contains(transactionRequestDetails.getTransactionCurrency())) {
				throw new AccountServiceException(FailureCode.CD4, HttpStatus.BAD_REQUEST);
			}
			
			
			if (StringUtils.isEmpty(transactionRequestDetails.getTransactionType())) {
				throw new AccountServiceException(FailureCode.CD9, HttpStatus.BAD_REQUEST);
			}
			
			if (!Arrays.asList(TransactionType.values()).stream().map(currency -> currency.toString())
					.collect(Collectors.toSet()).contains(transactionRequestDetails.getTransactionType())) {
				throw new AccountServiceException(FailureCode.CD10, HttpStatus.BAD_REQUEST);
			}
			
			if (StringUtils.isEmpty(transactionRequestDetails.getDescription())) {
				throw new AccountServiceException(FailureCode.CD11, HttpStatus.BAD_REQUEST);
			}
			
			
		}

	}

}
