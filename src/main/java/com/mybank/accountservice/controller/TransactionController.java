package com.mybank.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@PostMapping
	public TrasnsactionDetailDto performOperation(@RequestBody TransactionRequestDetails transactionRequestDetails) {
		return transactionService.intiateOperation(transactionRequestDetails, 1,null);
	}

	@GetMapping
	public List<TrasnsactionDetailDto> getTransactionDetail(@RequestHeader String accountId) {
		return transactionService.getTrasactionDetails(accountId);
	}
}
