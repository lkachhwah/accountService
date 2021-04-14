package com.mybank.accountservice.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailDTO {

	private String accountNumber;
	private String customerId;
	private String description;
	private Date opened;
	private String openedByBranch;
	private BigDecimal balance;
	
}
