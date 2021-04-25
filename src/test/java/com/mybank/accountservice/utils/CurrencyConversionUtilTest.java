package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.enums.CurrencyType;

@ExtendWith(SpringExtension.class)
public class CurrencyConversionUtilTest {

	@InjectMocks
	private CurrencyConversionUtil currencyConversionUtil;

	@BeforeEach
	public void init() {
		ReflectionTestUtils.setField(currencyConversionUtil, "usdToEurExchangeRate", 0.83);
		ReflectionTestUtils.setField(currencyConversionUtil, "usdToSekExchangeRate", 8.44);
		ReflectionTestUtils.setField(currencyConversionUtil, "usdToGbpExchangeRate", 0.72);
	}

	@Test
	public void getBalanceDetails() {
		List<BalanceDetailsDto> balances = currencyConversionUtil.getBalanceDetails(Arrays.asList("EUR", "SEK", "GBP"),
				new BigDecimal(10));

		Assertions.assertTrue(3 == balances.size());

	}

	@Test
	public void getUSDValueOfCurrency()
	{
		BigDecimal usdValue= currencyConversionUtil.getUSDValueOfCurrency(new BigDecimal(10), CurrencyType.EUR);
		Assertions.assertNotNull(usdValue);
	}
}
