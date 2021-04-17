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
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
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
	
	public TrasnsactionDetailDto intiateOperation(TrasnsactionDetailDto detailDto)
	{
		validationUtils.validatePerformTransactionDetails(detailDto);
		AccountDetailDto accountDetailDto = accountService.getAccountDetails(detailDto.getAccountId());		
		detailDto.setCustomerId(accountDetailDto.getCustomerId());
		
		log.info("Transaction Account details:{}",accountDetailDto);
		preOperationCheck(detailDto,accountDetailDto);
		
		TrasnsactionDetail trasnsactionDetail = performOperation(detailDto,accountDetailDto.getBalance());
		
		return commonUtils.getDTOFromTransactionDetail(trasnsactionDetail);

	}

	private TrasnsactionDetail performOperation(TrasnsactionDetailDto detailDto, BigDecimal balance) {
		
		BigDecimal trasactionAmountInUSD= currencyConversionUtil.getUSDValueOfCurrency(detailDto.getAmount(),detailDto.getTransactionCurrency());
		switch(detailDto.getTransactionType())
		{
		case IN:
			balance =balance.add(currencyConversionUtil.getUSDValueOfCurrency(detailDto.getAmount(),detailDto.getTransactionCurrency()));
			break;
		case OUT:
			log.info("Trasaction amount after Convertion:{}" ,trasactionAmountInUSD);
			balance=balance.subtract(trasactionAmountInUSD);
			break;
		default:
			break;
		}
		TrasnsactionDetail trasnsactionDetail=commonUtils.getAccountDetailsFromDto(detailDto);
		trasnsactionDetail.setTrasactionDate(new Date());
		trasnsactionDetail.setTransactionId(commonUtils.getUniqueNumber());
		trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(balance, trasnsactionDetail.getTransactionCurrency()));
		accountService.updateBalance(detailDto.getAccountId(), balance);
		transactionDetailsMapper.insert(trasnsactionDetail);
		return trasnsactionDetail;
		
	}

	private void preOperationCheck(TrasnsactionDetailDto detailDto,AccountDetailDto accountDetailDto) {
		BalanceDetailsDto balanceDetailsDto=commonUtils.checkCurrencySupportedByAccount(detailDto.getTransactionCurrency(),accountDetailDto.getBalanceInDifferentCurrency());
		if(TransactionType.OUT== detailDto.getTransactionType())
		{
			commonUtils.checkSufficientBalanceToPerformOperation(detailDto,balanceDetailsDto);
		}
		
		 
	}
	
	public List<TrasnsactionDetailDto> getTrasactionDetails(String accountId)
	{
		List<TrasnsactionDetail> trasnsactionDetailsList=transactionDetailsMapper.getTrasnsactionDetailsByCustomerId(accountId);
		if(CollectionUtils.isEmpty(trasnsactionDetailsList))
		{

			throw new AccountServiceException("Invalid Account details",HttpStatus.BAD_REQUEST);
			
		}
		return commonUtils.convertListToTransactionDetailDto(trasnsactionDetailsList);
		
		
	}
	
	
/*	@Transactional
	public TrasnsactionDetail performTransaction(String customerId,String accountNumber, long amount,String description,TransactionType transactionType)
	{
		AccountDetail accountDetail = accountDetailsMapper.getAccountDetail(accountNumber);
		TrasnsactionDetail trasnsactionDetails= TrasnsactionDetail.builder().transactionId(String.valueOf(new Date().getTime())).accountNumber(accountNumber).amount(new BigDecimal(amount)).customerId(customerId).description(description).transactionType(transactionType)
		.trasactionDate(new Date()).status(TransactionStatus.FAILED).build();
		if(Objects.nonNull(accountDetail))
		{
			switch (transactionType) {
			case CREDIT:
				accountDetail.setBalance(accountDetail.getBalance().add(new BigDecimal(amount)));
				trasnsactionDetails.setStatus(TransactionStatus.SUCCESS);
				break;
			case DEBIT:
				if(accountDetail.getBalance().compareTo(new BigDecimal(amount)) >=1)
				{
				log.info("Go ahead with transaction");
				accountDetail.setBalance(accountDetail.getBalance().min(new BigDecimal(amount)));
				trasnsactionDetails.setStatus(TransactionStatus.SUCCESS);
				}else
				{
					log.info("Transaction cannot be procedd as balanc is low");
				}
				break;

			default:
				break;
			}
			accountDetailsMapper.updateAmount(accountDetail);
			transactionDetailsMapper.insert(trasnsactionDetails);
		}
		return trasnsactionDetails;
	}
	*/
	

}
