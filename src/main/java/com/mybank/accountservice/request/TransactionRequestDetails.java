package com.mybank.accountservice.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.TransactionStatus;
import com.mybank.accountservice.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDetails {
	private String accountId;
	private String transactionType;
	private String description;
	private BigDecimal amount;
	private String transactionCurrency;
}
