package com.mybank.accountservice.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.enums.CurrencyType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDetails {
	
	private String country;
	private String customerId;
	private BigDecimal balance; // main balance always be store in USD
	private List<String> currencies;
	
}
