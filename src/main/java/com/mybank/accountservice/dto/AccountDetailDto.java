package com.mybank.accountservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybank.accountservice.enums.CurrencyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetailDto {
	private String accountId;
	private String customerId;
	private String country;
	private Date openedOnDate;
	private BigDecimal balance; // main balance always be store in USD
	private List<BalanceDetailsDto> balanceInDifferentCurrency; // we convert main balance into different currency for presentation
	private List<CurrencyType> currencies;
	private String stampKey;
}
