package com.mybank.accountservice.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.TransactionStatus;
import com.mybank.accountservice.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrasnsactionDetailDto {
	
	
	private String transactionId;
	private String  customerId;
	private String accountId;
	private TransactionType transactionType;
	private String description;
	private Date trasactionDate;
	private BigDecimal amount;
	private TransactionStatus status;
	private CurrencyType transactionCurrency;
	private BigDecimal convertedBalance;
	private String accountBalance;
	private String accountBalanceInUSD;
}
