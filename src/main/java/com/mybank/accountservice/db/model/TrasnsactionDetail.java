package com.mybank.accountservice.db.model;

import java.io.Serializable;
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
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder

public class TrasnsactionDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6939358267671033448L;
	/**
	 * 
	 */
	
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
	private BigDecimal accountBalanceInUSD;
	private String failureReason;
}
