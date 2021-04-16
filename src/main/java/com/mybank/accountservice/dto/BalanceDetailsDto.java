package com.mybank.accountservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.db.enums.AccountType;
import com.mybank.accountservice.db.enums.CurrencyType;
import com.mybank.accountservice.db.model.AccountDetail.AccountDetailBuilder;

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
public class BalanceDetailsDto{
	private BigDecimal amount;
	private CurrencyType currency;
}
