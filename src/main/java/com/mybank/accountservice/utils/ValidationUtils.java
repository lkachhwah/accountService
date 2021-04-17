package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.exception.AccountServiceException;

@Component
public class ValidationUtils {
	
	public void validateCreateAccountDetails(AccountDetailDto accountDetailDto)
	{
		if(Objects.nonNull(accountDetailDto))
		{
			if(StringUtils.isEmpty(accountDetailDto.getCountry()))
					{
				      throw new AccountServiceException("Country is missing in request",HttpStatus.BAD_REQUEST);
					}
			if(StringUtils.isEmpty(accountDetailDto.getCustomerId()))
			{
		      throw new AccountServiceException("CustomerId is missing in request",HttpStatus.BAD_REQUEST);
			}
			if(CollectionUtils.isEmpty(accountDetailDto.getCurrencies()))
			{
		      throw new AccountServiceException("Currencies is missing in request",HttpStatus.BAD_REQUEST);
			}
			
			if(StringUtils.isEmpty(accountDetailDto.getBalance()))
			{
		      throw new AccountServiceException("Intial Balance is missing in request",HttpStatus.BAD_REQUEST);
			}
		}
	}

	public void validatePerformTransactionDetails(TrasnsactionDetailDto detailDto) {
		if(Objects.nonNull(detailDto))
		{
			if(Objects.nonNull(detailDto.getAmount()) &&  detailDto.getAmount().compareTo(new BigDecimal(0) )>1 )
					{
				      throw new AccountServiceException("Invalid Amount",HttpStatus.BAD_REQUEST);
					}
			if(StringUtils.isEmpty(detailDto.getAccountId()))
			{
		      throw new AccountServiceException("AccountId is missing in request",HttpStatus.BAD_REQUEST);
			}
			if(Objects.isNull(detailDto.getTransactionCurrency()))
			{
		      throw new AccountServiceException("Currencie is missing in request",HttpStatus.BAD_REQUEST);
			}
			
			if(Objects.isNull(detailDto.getTransactionType()))
			{
		      throw new AccountServiceException("TransactionType is missing in request",HttpStatus.BAD_REQUEST);
			}
			
			if(StringUtils.isEmpty(detailDto.getDescription()))
			{
		      throw new AccountServiceException("Description is missing in request",HttpStatus.BAD_REQUEST);
			}
		}
		
	}

}
