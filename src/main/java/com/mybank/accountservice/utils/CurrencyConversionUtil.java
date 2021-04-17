package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
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
	
	public BigDecimal getValueByCurrency(BigDecimal balance,CurrencyType currencyType)
	{
		
		BigDecimal value= new BigDecimal(0);
				
		switch(currencyType)
		{ 
		case EUR:
			value= getValue(balance,usdToEurExchangeRate);
			break;
		case SEK:
			value= getValue(balance,usdToSekExchangeRate);
			break;
		case GBP:
			value= getValue(balance,usdToGbpExchangeRate);
			break;
		case USD:
			value= balance;
			break;
		default:
				break;
		}
		return value;
	}
	
	public BigDecimal getValue(BigDecimal balance,double exchangeVale)
	{
		if(balance.compareTo(new BigDecimal(0))==0 )
		{
			return new BigDecimal(0.00);
		}
		else{
			return balance.multiply(new BigDecimal(exchangeVale));
		}
		
	}
	
	
	public List<BalanceDetailsDto> getBalanceDetails(List<CurrencyType> currenciessupportedList,BigDecimal balance)
	{
		if(CollectionUtils.isEmpty(currenciessupportedList))
		{
			return new ArrayList<>();
		}
		
		return currenciessupportedList.stream().map(currency->{
			return BalanceDetailsDto.builder().currency(currency).amount(getValueByCurrency(balance,currency)).build();
		}).collect(Collectors.toList());
		
	}
	
	
	public BigDecimal getUSDValueOfCurrency(BigDecimal balance,CurrencyType currencyType)
	{
		
		BigDecimal value= new BigDecimal(0);
				
		switch(currencyType)
		{ 
		case EUR:
			value= getUSDValue(balance,usdToEurExchangeRate);
			break;
		case SEK:
			value= getUSDValue(balance,usdToSekExchangeRate);
			break;
		case GBP:
			value= getUSDValue(balance,usdToGbpExchangeRate);
			break;
		case USD:
			value= balance;
			break;
		default:
				break;
		}
		return value;
	}
private BigDecimal getUSDValue(BigDecimal balance, double exchangeVale) {
	
	if(balance.compareTo(new BigDecimal(0))==0 )
	{
		return new BigDecimal(0.00);
	}
	else{
		return balance.divide(new BigDecimal(exchangeVale),MathContext.DECIMAL128);
	}
	}

	
}
	
			
			
