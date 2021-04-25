package com.mybank.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	@PostMapping
	public ResponseEntity<AccountDetailDto> createAccount(@RequestBody AccountRequestDetails accountRequestDetails) {
		AccountDetailDto response = accountService.createAccount(accountRequestDetails);
		response.setBalance(null);
		return new ResponseEntity<AccountDetailDto>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<AccountDetailDto> getAccount(@RequestHeader String accountId) {
		AccountDetailDto response = accountService.getAccountDetails(accountId);
		response.setBalance(null);
		return new ResponseEntity<AccountDetailDto>(response, HttpStatus.OK);
	}

}
