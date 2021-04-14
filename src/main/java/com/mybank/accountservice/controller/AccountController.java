package com.mybank.accountservice.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;
import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.AccountType;
import com.mybank.accountservice.db.model.CustomerDetails;
import com.mybank.accountservice.db.model.GenderType;
import com.mybank.accountservice.db.model.TransactionStatus;
import com.mybank.accountservice.db.model.TransactionType;
import com.mybank.accountservice.db.model.TrasnsactionDetails;
import com.mybank.accountservice.model.AccountDetailDTO;
import com.mybank.accountservice.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	AccountDetailsMapper accountDetailsMapper;
	
	@Autowired
    CustomerDetailsMapper customerDetailsMapper;
	
	@Autowired
	TransactionDetailsMapper transactionDetailsMapper;
	
	@Autowired
	TransactionService  transactionService;

	@GetMapping
	public String account(){
		log.info("Pushing Message");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend("myTestQueueExchange","myTestQueueKey", 
				AccountDetailDTO.builder().description("Saving").accountNumber("123456789").balance(new BigDecimal(999999999.0)).opened(new Date()).openedByBranch("Bajaj Nagar").build());
		
		log.info("FEtching from DATA :{}",accountDetailsMapper.getAccountDetail("CITI123456"));
		log.info("FEtching from LIST DATA :{}",accountDetailsMapper.getAccountDetailByCustomerId("1"));
		
		log.info("FEtching from Customer Details DATA :{}",customerDetailsMapper.getCustomerDetails("1"));
		
		
		log.info("INSERT  Customer Details" );
		customerDetailsMapper.insert(CustomerDetails.builder().customerId("5").dob(new Date()).gender(GenderType.MALE).name("Dipti").emailId("dipt@gmail.com").build());
		
		log.info("INSERT  Account Details" );
		accountDetailsMapper.insert(AccountDetail.builder().accountNumber("7777777").accountType(AccountType.INDIVIDUAL).balance(new BigDecimal(89899)).customerId("5")
				.description("Salary Credited").openedByBranch("Bajaj Nagar").openedOn(new Date()).build());
		
		
		log.info("INSERT  Transaction Details" );
		transactionDetailsMapper.insert(TrasnsactionDetails.builder().accountNumber("7777777").transactionType(TransactionType.CREDIT)
				.status(TransactionStatus.SUCCESS).description("Salary Credited").amount(new BigDecimal(89899)).customerId("5")
				.description("Salary Credited").transactionId("5").trasactionDate(new Date()).build());
		
		
	    return "I am working";
	  }
	
	@GetMapping("/transaction")
	public TrasnsactionDetails perfromTransaction(@RequestHeader String customerId,@RequestHeader String accountNumber, @RequestHeader long amount,
			@RequestHeader String description,@RequestHeader TransactionType transactionType ){
		log.info("Trasaction Started");
	    return transactionService.performTransaction(customerId, accountNumber, amount, description, transactionType);
	  }
	
}
