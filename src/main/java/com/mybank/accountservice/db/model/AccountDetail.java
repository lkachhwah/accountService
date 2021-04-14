package com.mybank.accountservice.db.model;



import java.math.BigDecimal;
import java.util.Date;

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
public class AccountDetail {
	@NonNull
	private String accountNumber;
	private String description;
	@NonNull
	private String customerId;
	private AccountType accountType;
	private Date openedOn;
	private String openedByBranch;
	private BigDecimal balance;

}
