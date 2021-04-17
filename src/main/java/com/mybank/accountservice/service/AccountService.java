package com.mybank.accountservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

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
	
	@Autowired
	TransactionService transactionService;

	public AccountDetailDto createAccount(AccountRequestDetails accountRequestDetails)
	{
		validationUtils.validateCreateAccountDetails(accountRequestDetails);
		
		if(!customerService.checkCustomerExist(accountRequestDetails.getCustomerId()))
		{
			throw new AccountServiceException("CustomerID Provided does not exist", HttpStatus.BAD_REQUEST);
		}
		
		AccountDetail accountDetail=commonUtils.getAccountDetailsFromDto(accountRequestDetails);
		accountDetail.setAccountId(commonUtils.getUniqueNumber());
		accountDetail.setOpenedOnDate(new Date());
		accountDetail.setBalance(new BigDecimal(0));
		
		log.info("AccountDetails INSERT:{}",accountDetail);
		accountDetailsMapper.insert(accountDetail);
		log.info("AccountDetails INSERT SuccessFull:{}",accountDetail);
		
		transactionService.intiateOperation(TransactionRequestDetails.builder().accountId(accountDetail.getAccountId())
				.amount(accountRequestDetails.getBalance()).description("Initial Acount opened").transactionCurrency(CurrencyType.USD.toString())
				.transactionType(TransactionType.IN.toString()).build());
		
		List<BalanceDetailsDto>  balances=currencyConversionUtil.getBalanceDetails(accountRequestDetails.getCurrencies(), accountRequestDetails.getBalance());
		log.info("BalanceDetails:{}",balances);
				
		AccountDetailDto accountDetailDto=commonUtils.getAccountDetailsDtoFromAcccountDetail(accountDetail); 
		accountDetailDto.setBalanceInDifferentCurrency(balances);
		
		
		
		return accountDetailDto;
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
