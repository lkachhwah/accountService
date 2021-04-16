package com.mybank.accountservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.db.enums.CurrencyType;
import com.mybank.accountservice.db.enums.TransactionStatus;
import com.mybank.accountservice.db.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
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
	private BigDecimal accountBalance;
}
