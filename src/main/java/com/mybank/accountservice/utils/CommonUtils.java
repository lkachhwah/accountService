package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonUtils {

	ObjectMapper objectMapper= new ObjectMapper();
	
	public String getUniqueNumber()
	{
		return String.valueOf(new Date().getTime());
		
	}
	
	public String getStringOfCurrecnyList(List<CurrencyType> list) {
		return list.stream().map(CurrencyType::toString).collect(Collectors.joining(","));
	}

	public AccountDetail getAccountDetailsFromDto(AccountRequestDetails accountRequestDetails) {
		AccountDetail accountDetail= objectMapper.convertValue(accountRequestDetails, AccountDetail.class);
		accountDetail.setAllowedCurrencies(String.join(",", accountRequestDetails.getCurrencies()));
		return accountDetail;
	}

	public AccountDetailDto getAccountDetailsDtoFromAcccountDetail(AccountDetail accountDetail) {
		return objectMapper.convertValue(accountDetail, AccountDetailDto.class);
	}
	
	public List<String> getCurrecnyListFromString(String allowedCurrencies) {
		 return Arrays.asList(allowedCurrencies.split(",")).stream().map(str->str).collect(Collectors.toList());
	}


	public BalanceDetailsDto checkCurrencySupportedByAccount(CurrencyType transactionCurrency,
			List<BalanceDetailsDto> balanceInDifferentCurrency) {
		Optional<BalanceDetailsDto> balanceDetailFoundt=balanceInDifferentCurrency.stream().filter(balance->balance.getCurrency()==transactionCurrency).findFirst();
		if(balanceDetailFoundt.isPresent())
			return balanceDetailFoundt.get();
		else
			return null;
	}
	
	public void checkSufficientBalanceToPerformOperation(TransactionRequestDetails transactionRequestDetails ,
			BalanceDetailsDto balanceDetailsDto) {
		
		log.info("Balance :{} , transaction :{} ,comparison:{}",balanceDetailsDto.getAmount(),transactionRequestDetails.getAmount(),
				balanceDetailsDto.getAmount().compareTo(transactionRequestDetails.getAmount())>=1);
		if(balanceDetailsDto.getAmount().compareTo(transactionRequestDetails.getAmount())>=1)
		{
			return;
		}
		throw new AccountServiceException(FailureCode.CD13,HttpStatus.BAD_REQUEST);
	}

	public TrasnsactionDetailDto getDTOFromTransactionDetail(TrasnsactionDetail trasnsactionDetail) {
		
		return objectMapper.convertValue(trasnsactionDetail, TrasnsactionDetailDto.class);
	}

	public List<TrasnsactionDetailDto> convertListToTransactionDetailDto(List<TrasnsactionDetail> trasnsactionDetailsList) {
	
		return trasnsactionDetailsList.parallelStream()
		.map(trasnsactionDetail->getDTOFromTransactionDetail(trasnsactionDetail))
		.collect(Collectors.toList());
	
	}

	
}
