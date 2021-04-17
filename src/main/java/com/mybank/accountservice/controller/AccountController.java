package com.mybank.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@PostMapping
	public AccountDetailDto createAccount(@RequestBody AccountRequestDetails accountRequestDetails)
	{
		AccountDetailDto response=accountService.createAccount(accountRequestDetails);
		response.setBalance(null);
		return response;
	}
	
	@GetMapping
	public AccountDetailDto getAccount(@RequestHeader String accountId)
	{
		AccountDetailDto response=accountService.getAccountDetails(accountId);
		response.setBalance(null);
		return response;
	}
	
}
