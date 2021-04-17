package com.mybank.accountservice.dto;

import java.math.BigDecimal;

import com.mybank.accountservice.enums.CurrencyType;

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
public class BalanceDetailsDto{
	private BigDecimal amount;
	private CurrencyType currency;
}
