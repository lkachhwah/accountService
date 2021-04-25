package com.mybank.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@PostMapping
	public ResponseEntity<TrasnsactionDetailDto> performOperation(
			@RequestBody TransactionRequestDetails transactionRequestDetails) {
		log.info(" Perform transaction accountId:{}, Type:{}", transactionRequestDetails.getAccountId(),
				transactionRequestDetails.getTransactionType());
		return new ResponseEntity<TrasnsactionDetailDto>(
				transactionService.intiateOperation(transactionRequestDetails, 1, null), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<TrasnsactionDetailDto>> getTransactionDetail(@RequestHeader String accountId) {
		log.info(" Get transactions for accountId:{}", accountId);
		return new ResponseEntity<List<TrasnsactionDetailDto>>(transactionService.getTrasactionDetails(accountId),
				HttpStatus.OK);
	}
}
