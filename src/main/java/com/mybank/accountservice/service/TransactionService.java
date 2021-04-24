package com.mybank.accountservice.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.dto.PublisherDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
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
	
	@Autowired
	EventPublisherService eventPublisherService;
	
	int retryCount=5;
	
	public TrasnsactionDetailDto intiateOperation(TransactionRequestDetails transactionRequestDetails,int count,String tranactionId)
	{
		tranactionId=StringUtils.isEmpty(tranactionId)?commonUtils.getUniqueNumber():tranactionId;
		validationUtils.validatePerformTransactionDetails(transactionRequestDetails);
		log.info("[intiateOperation] -START acountId:{} ,tranactionId:{}",transactionRequestDetails.getAccountId(),tranactionId);
		AccountDetailDto accountDetailDto = accountService.getAccountDetails(transactionRequestDetails.getAccountId());		
		BigDecimal updatedBalance = accountDetailDto.getBalance();
		
		TrasnsactionDetail trasnsactionDetail = getTransactionDetail(transactionRequestDetails, accountDetailDto,updatedBalance);
		trasnsactionDetail.setTransactionId(tranactionId);
		
		try{
			log.info("Transaction Account details:{},,tranactionId:{}",accountDetailDto,tranactionId);
			preOperationCheck(transactionRequestDetails,accountDetailDto);
			updatedBalance= updatedBalance(transactionRequestDetails,accountDetailDto);
			trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(updatedBalance, trasnsactionDetail.getTransactionCurrency()));
			trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
			trasnsactionDetail.setStatus(TransactionStatus.SUCCESS);
			accountService.updateBalanceBasedStampKey(accountDetailDto, updatedBalance);
			log.info("[intiateOperation] -ACOUNT UPDATED acountId:{},,tranactionId:{}",transactionRequestDetails.getAccountId(),tranactionId);
			
		}catch(AccountServiceException e)
		{
			log.error("[intiateOperation] -EXCEPTION acountId:{} , ErrorCode:{} ,ErrorMessage:{} ,tranactionId:{}",transactionRequestDetails.getAccountId(),e.getCode(),e.getMessage(),tranactionId);
			checkAndRetry(transactionRequestDetails, count, trasnsactionDetail, e);
			trasnsactionDetail.setFailureReason(e.getMessage());
			trasnsactionDetail.setStatus(TransactionStatus.FAILED);
			trasnsactionDetail.setAccountBalanceInUSD(accountDetailDto.getBalance());
			trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(accountDetailDto.getBalance(), trasnsactionDetail.getTransactionCurrency()));
			throw e;
		}
		finally
		{
			transactionDetailsMapper.insert(trasnsactionDetail);
			PublisherDto<TrasnsactionDetail> data=new PublisherDto<TrasnsactionDetail>() ;
			data.add(trasnsactionDetail);
			eventPublisherService.asyncMethodWithVoidReturnType(data);
		}
		log.info("[intiateOperation] -END acountId:{} ,TransactionId:{}",trasnsactionDetail.getAccountId(),tranactionId);
			return commonUtils.getDTOFromTransactionDetail(trasnsactionDetail);
	}

	private void checkAndRetry(TransactionRequestDetails transactionRequestDetails, int count,
			TrasnsactionDetail trasnsactionDetail, AccountServiceException e) {
		if(Objects.nonNull(e.getCode())&& FailureCode.CD16==e.getCode() && count<=retryCount)
		{
			log.info("[intiateOperation] -Retry For  acountId:{} , TransactionId:{}",trasnsactionDetail.getAccountId(),trasnsactionDetail.getTransactionId());
			intiateOperation(transactionRequestDetails,count++,trasnsactionDetail.getTransactionId());
		}
	}

	private TrasnsactionDetail getTransactionDetail(TransactionRequestDetails transactionRequestDetails,
			AccountDetailDto accountDetailDto, BigDecimal updatedBalance) {
		TrasnsactionDetail trasnsactionDetail=new TrasnsactionDetail();
		trasnsactionDetail.setTransactionCurrency(CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()));
		trasnsactionDetail.setTransactionType(TransactionType.valueOf(transactionRequestDetails.getTransactionType()));
		trasnsactionDetail.setAccountId(transactionRequestDetails.getAccountId());
		trasnsactionDetail.setDescription(transactionRequestDetails.getDescription());
		trasnsactionDetail.setAmount(transactionRequestDetails.getAmount());
		trasnsactionDetail.setTrasactionDate(new Date());
		trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(updatedBalance, trasnsactionDetail.getTransactionCurrency()));
		trasnsactionDetail.setCustomerId(accountDetailDto.getCustomerId());
		trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
		trasnsactionDetail.setFailureReason("NA");
		return trasnsactionDetail;
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
		log.info("[getTrasactionDetails] -START acountId:{}",accountId);
		Optional<List<TrasnsactionDetail>> trasnsactionDetailsList=transactionDetailsMapper.getTrasnsactionDetailsByAccountId(accountId);
		if(trasnsactionDetailsList.isPresent() && !CollectionUtils.isEmpty(trasnsactionDetailsList.get()))
		{
			log.info("[getTrasactionDetails] -Found acountId:{}",accountId);
			return commonUtils.convertListToTransactionDetailDto(trasnsactionDetailsList.get());
			
		}
		log.error("[getTrasactionDetails] -NOT FOUND acountId:{}",accountId);
		throw new AccountServiceException(FailureCode.CD12,HttpStatus.BAD_REQUEST);
	}
}
