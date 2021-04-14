package com.mybank.accountservice.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;
import com.mybank.accountservice.model.AccountDetailDTO;

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

	@GetMapping
	public String account(){
		log.info("Pushing Message");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend("myTestQueueExchange","myTestQueueKey", 
				AccountDetailDTO.builder().description("Saving").accountNumber("123456789").balance(new BigDecimal(999999999.0)).opened(new Date()).openedByBranch("Bajaj Nagar").build());
		
		log.info("FEtching from DATA :{}",accountDetailsMapper.getAccountDetail("CITI123456"));
		log.info("FEtching from LIST DATA :{}",accountDetailsMapper.getAccountDetailByCustomerId("1"));
		
		log.info("FEtching from Customer Details DATA :{}",customerDetailsMapper.getCustomerDetails("1"));
		
	    return "I am working";
	  }
}
