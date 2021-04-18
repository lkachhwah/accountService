package com.mybank.accountservice.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.TransactionStatus;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {
	

	@Autowired
	TransactionDetailsMapper transactionDetailsMapper;
	
	@Autowired
	CommonUtils commonUtils;
	
	@Autowired
	ValidationUtils validationUtils;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	CurrencyConversionUtil currencyConversionUtil;
	
	public TrasnsactionDetailDto intiateOperation(TransactionRequestDetails transactionRequestDetails)
	{
		validationUtils.validatePerformTransactionDetails(transactionRequestDetails);
		AccountDetailDto accountDetailDto = accountService.getAccountDetails(transactionRequestDetails.getAccountId());		
		BigDecimal updatedBalance = accountDetailDto.getBalance();
		TransactionStatus status=null;
		
		TrasnsactionDetail trasnsactionDetail=new TrasnsactionDetail();
		trasnsactionDetail.setTransactionCurrency(CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()));
		trasnsactionDetail.setTransactionType(TransactionType.valueOf(transactionRequestDetails.getTransactionType()));
		trasnsactionDetail.setAccountId(transactionRequestDetails.getAccountId());
		trasnsactionDetail.setDescription(transactionRequestDetails.getDescription());
		trasnsactionDetail.setAmount(transactionRequestDetails.getAmount());
		trasnsactionDetail.setTrasactionDate(new Date());
		trasnsactionDetail.setTransactionId(commonUtils.getUniqueNumber());
		trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(updatedBalance, trasnsactionDetail.getTransactionCurrency()));
		trasnsactionDetail.setCustomerId(accountDetailDto.getCustomerId());
		trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
		trasnsactionDetail.setFailureReason("NA");
		try{
			log.info("Transaction Account details:{}",accountDetailDto);
			preOperationCheck(transactionRequestDetails,accountDetailDto);
			updatedBalance= updatedBalance(transactionRequestDetails,accountDetailDto);
			status=TransactionStatus.SUCCESS;
			trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(updatedBalance, trasnsactionDetail.getTransactionCurrency()));
			trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
		}catch(AccountServiceException e)
		{
			status=TransactionStatus.FAILED;
			trasnsactionDetail.setFailureReason(e.getMessage());
			throw e;
		}
		finally
		{
			if(status==TransactionStatus.SUCCESS){
				accountService.updateBalance(transactionRequestDetails.getAccountId(), updatedBalance);
			}
			trasnsactionDetail.setStatus(status);
			transactionDetailsMapper.insert(trasnsactionDetail);
		}
		return commonUtils.getDTOFromTransactionDetail(trasnsactionDetail);
	}

private BigDecimal updatedBalance(TransactionRequestDetails transactionRequestDetails, AccountDetailDto accountDetailDto) {
		
		BigDecimal balance= accountDetailDto.getBalance();
		
		BigDecimal trasactionAmountInUSD= currencyConversionUtil.getUSDValueOfCurrency(transactionRequestDetails.getAmount(),CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()));
		switch(TransactionType.valueOf(transactionRequestDetails.getTransactionType()))
		{
		case IN:
			balance =balance.add(trasactionAmountInUSD);
			break;
		case OUT:
			log.info("Trasaction amount after Convertion:{}" ,trasactionAmountInUSD);
			balance=balance.subtract(trasactionAmountInUSD);
			break;
		default:
			break;
		}
		
		return balance;
	}

	private void preOperationCheck(TransactionRequestDetails transactionRequestDetails,AccountDetailDto accountDetailDto) {
		BalanceDetailsDto balanceDetailsDto=commonUtils.checkCurrencySupportedByAccount(CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency())
				,accountDetailDto.getBalanceInDifferentCurrency());
		if(TransactionType.OUT== TransactionType.valueOf(transactionRequestDetails.getTransactionType()))
		{
			commonUtils.checkSufficientBalanceToPerformOperation(transactionRequestDetails,balanceDetailsDto);
		}
	}
	
	public List<TrasnsactionDetailDto> getTrasactionDetails(String accountId)
	{
		List<TrasnsactionDetail> trasnsactionDetailsList=transactionDetailsMapper.getTrasnsactionDetailsByAccountId(accountId);
		if(CollectionUtils.isEmpty(trasnsactionDetailsList))
		{
			throw new AccountServiceException("Invalid Account details",HttpStatus.BAD_REQUEST);
		}
		return commonUtils.convertListToTransactionDetailDto(trasnsactionDetailsList);
	}
}
