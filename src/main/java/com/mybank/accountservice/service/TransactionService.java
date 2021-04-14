package com.mybank.accountservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;

@Service
public class TransactionService {
	
	@Autowired
	AccountDetailsMapper accountDetailsMapper;
	
	@Autowired
	CustomerDetailsMapper customerDetailsMapper;
	
	

}
