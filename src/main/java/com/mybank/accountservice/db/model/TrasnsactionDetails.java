package com.mybank.accountservice.db.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
public class TrasnsactionDetails {

	
	@NonNull
	private String transactionId;
	
	@NonNull
	private String  customerId;
	@NonNull
	private String accountNumber;
	@NonNull
	private TransactionType transactionType;
	
	private String description;
	
	@NonNull
	private Date trasactionDate;
	
	@NonNull
	private BigDecimal amount;
	
	@NonNull
	private TransactionStatus status;
}
