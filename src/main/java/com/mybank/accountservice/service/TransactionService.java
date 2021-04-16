package com.mybank.accountservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mybank.accountservice.db.enums.TransactionStatus;
import com.mybank.accountservice.db.enums.TransactionType;
import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;
import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.TrasnsactionDetail;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {
	
	@Autowired
	AccountDetailsMapper accountDetailsMapper;
	
	@Autowired
	CustomerDetailsMapper customerDetailsMapper;
	
	@Autowired
	TransactionDetailsMapper transactionDetailsMapper;
	
	@Transactional
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
	
	

}
