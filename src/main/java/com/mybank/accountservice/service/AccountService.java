package com.mybank.accountservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.exception.AccountServiceException;

import lombok.extern.slf4j.Slf4j;

import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.ValidationUtils;

@Service
@Slf4j
public class AccountService {
	
	@Autowired
	CurrencyConversionUtil currencyConversionUtil;
	
	@Autowired
	ValidationUtils validationUtils;

	
	
	@Autowired
	AccountDetailsMapper accountDetailsMapper;
	
	@Autowired
	CommonUtils commonUtils;
	
	@Autowired
	CustomerService customerService;

	public AccountDetailDto createAccount(AccountDetailDto accountDetailDto)
	{
		validationUtils.validateCreateAccountDetails(accountDetailDto);
		
		if(!customerService.checkCustomerExist(accountDetailDto.getCustomerId()))
		{
			throw new AccountServiceException("CustomerID Provided does not exist", HttpStatus.BAD_REQUEST);
		}
		
		AccountDetail accountDetail=commonUtils.getAccountDetailsFromDto(accountDetailDto);
		accountDetail.setAccountId(commonUtils.getUniqueNumber());
		accountDetail.setOpenedOnDate(new Date());
		
		log.info("AccountDetails INSERT:{}",accountDetail);
		accountDetailsMapper.insert(accountDetail);
		log.info("AccountDetails INSERT SuccessFull:{}",accountDetail);
		
		List<BalanceDetailsDto>  balances=currencyConversionUtil.getBalanceDetails(accountDetailDto.getCurrencies(), accountDetailDto.getBalance());
		log.info("BalanceDetails:{}",balances);
				
		AccountDetailDto accountDetailDto2=commonUtils.getAccountDetailsDtoFromAcccountDetail(accountDetail); 
		accountDetailDto2.setBalanceInDifferentCurrency(balances);
		
		return accountDetailDto2;
	}
	
	public AccountDetailDto getAccountDetails(String accountId)
	{
		AccountDetail accountDetail= accountDetailsMapper.getAccountDetail(accountId);
		AccountDetailDto accountDetailDto =null; 
		if(Objects.nonNull(accountDetail))
		{
			accountDetailDto=commonUtils.getAccountDetailsDtoFromAcccountDetail(accountDetail); 
			List<BalanceDetailsDto>  balances=currencyConversionUtil.getBalanceDetails(commonUtils.getCurrecnyListFromString(accountDetail.getAllowedCurrencies()),
					accountDetailDto.getBalance());
			accountDetailDto.setBalanceInDifferentCurrency(balances);
		}else
		{
			throw new AccountServiceException("accountId Provided does not exist", HttpStatus.BAD_REQUEST);
		}
		return accountDetailDto;
	}
	
	public void updateBalance(String accountId,BigDecimal balance)
	{
		accountDetailsMapper.updateBalanceForAccount(accountId,balance);
	}
}
