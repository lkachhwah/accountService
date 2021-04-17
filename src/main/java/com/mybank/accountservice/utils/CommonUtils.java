package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import com.mybank.accountservice.exception.AccountServiceException;

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

	public AccountDetail getAccountDetailsFromDto(AccountDetailDto accountDetailDto) {
		AccountDetail accountDetail= objectMapper.convertValue(accountDetailDto, AccountDetail.class);
		accountDetail.setAllowedCurrencies(getStringOfCurrecnyList(accountDetailDto.getCurrencies()));
		return accountDetail;
	}

	public AccountDetailDto getAccountDetailsDtoFromAcccountDetail(AccountDetail accountDetail) {
		return objectMapper.convertValue(accountDetail, AccountDetailDto.class);
	}
	
	public List<CurrencyType> getCurrecnyListFromString(String allowedCurrencies) {
		 return Arrays.asList(allowedCurrencies.split(",")).stream().map(str->CurrencyType.valueOf(str)).collect(Collectors.toList());
	}

	public TrasnsactionDetail getAccountDetailsFromDto(TrasnsactionDetailDto detailDto) {
		return objectMapper.convertValue(detailDto, TrasnsactionDetail.class);
		
	}

	public BalanceDetailsDto checkCurrencySupportedByAccount(CurrencyType transactionCurrency,
			List<BalanceDetailsDto> balanceInDifferentCurrency) {
		Optional<BalanceDetailsDto> balanceDetailFoundt=balanceInDifferentCurrency.stream().filter(balance->balance.getCurrency()==transactionCurrency).findFirst();
		if(balanceDetailFoundt.isPresent())
			return balanceDetailFoundt.get();
		else
			return null;
	}
	
	public void checkSufficientBalanceToPerformOperation(TrasnsactionDetailDto detailDto ,
			BalanceDetailsDto balanceDetailsDto) {
		
		log.info("Balance :{} , transaction :{} ,comparison:{}",balanceDetailsDto.getAmount(),detailDto.getAmount(),balanceDetailsDto.getAmount().compareTo(detailDto.getAmount())>=1);
		if(balanceDetailsDto.getAmount().compareTo(detailDto.getAmount())>=1)
		{
			return;
		}
		throw new AccountServiceException("Insufficient Balance",HttpStatus.BAD_REQUEST);
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
