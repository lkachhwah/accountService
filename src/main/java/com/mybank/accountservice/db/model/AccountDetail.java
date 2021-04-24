package com.mybank.accountservice.db.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetail {
	@NonNull
	private String accountId;
	@NonNull
	private String customerId;
	private String country;
	private Date openedOnDate;
	private BigDecimal balance; // main balance always be store in USD
	private String allowedCurrencies;
	@NonNull
	private String stampKey;
}
