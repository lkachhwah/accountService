package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.enums.CurrencyType;

@Component
public class CurrencyConversionUtil {

	@Value("${accountservice.exchange.rate.usd.to.eur:0.83}")
	private double usdToEurExchangeRate;

	@Value("${accountservice.exchange.rate.usd.to.sek:8.44}")
	private double usdToSekExchangeRate;

	@Value("${accountservice.exchange.rate.usd.to.gbp:0.72}")
	private double usdToGbpExchangeRate;

	public BigDecimal getValueByCurrency(BigDecimal balance, CurrencyType currencyType) {

		BigDecimal value = new BigDecimal(0);

		switch (currencyType) {
		case EUR:
			value = getValue(balance, usdToEurExchangeRate);
			break;
		case SEK:
			value = getValue(balance, usdToSekExchangeRate);
			break;
		case GBP:
			value = getValue(balance, usdToGbpExchangeRate);
			break;
		case USD:
			value = balance;
			break;
		default:
			break;
		}
		return setValueWithTwoPrecision(value);
	}

	public BigDecimal getValue(BigDecimal balance, double exchangeVale) {
		if (balance.compareTo(new BigDecimal(0)) == 0) {
			return setValueWithTwoPrecision(new BigDecimal(0.00));
		} else {
			return setValueWithTwoPrecision(balance.multiply(new BigDecimal(exchangeVale)));
		}

	}

	public List<BalanceDetailsDto> getBalanceDetails(List<String> currenciessupportedList, BigDecimal balance) {
		if (CollectionUtils.isEmpty(currenciessupportedList)) {
			return Collections.EMPTY_LIST;
		}
		return currenciessupportedList.stream()
				.map(currencyType -> BalanceDetailsDto.builder().currency(CurrencyType.valueOf(currencyType))
						.amount(getValueByCurrency(balance, CurrencyType.valueOf(currencyType))).build())
				.collect(Collectors.toList());
	}

	public BigDecimal getUSDValueOfCurrency(BigDecimal balance, CurrencyType currencyType) {

		BigDecimal value = new BigDecimal(0);

		switch (currencyType) {
		case EUR:
			value = getUSDValue(balance, usdToEurExchangeRate);
			break;
		case SEK:
			value = getUSDValue(balance, usdToSekExchangeRate);
			break;
		case GBP:
			value = getUSDValue(balance, usdToGbpExchangeRate);
			break;
		case USD:
			value = balance;
			break;
		default:
			break;
		}
		return setValueWithTwoPrecision(value);
	}

	private BigDecimal getUSDValue(BigDecimal balance, double exchangeVale) {

		if (balance.compareTo(new BigDecimal(0)) == 0) {
			return setValueWithTwoPrecision(new BigDecimal(0.00));
		} else {
			return setValueWithTwoPrecision(balance.divide(new BigDecimal(exchangeVale), MathContext.DECIMAL128));
		}
	}

	public BigDecimal setValueWithTwoPrecision(BigDecimal value) {
		if (Objects.isNull(value)) {
			return new BigDecimal(0.00);
		}
		return value.setScale(2, RoundingMode.UP);
	}

}
