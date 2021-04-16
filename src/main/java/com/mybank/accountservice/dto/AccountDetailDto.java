package com.mybank.accountservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.db.enums.AccountType;
import com.mybank.accountservice.db.enums.CurrencyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailDto {

	private String accountId;
	private String customerId;
	private String country;
	private AccountType accountType;
	private Date openedOnDate;
	private BigDecimal balance; // main balance always be store in USD
	private List<BalanceDetailsDto> balanceInDifferentCurrency; // we convert main balance into different currency for presentation
	
}
