package com.mybank.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.DummyObjectProvider;
import com.mybank.accountservice.utils.ValidationUtils;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
	
	@InjectMocks
	private AccountService accountService;
	
	@Mock
	private CurrencyConversionUtil currencyConversionUtil;
	@Mock
	private ValidationUtils validationUtils;
	@Mock
	private AccountDetailsMapper accountDetailsMapper;
	@Mock
	private CommonUtils commonUtils;
	@Mock
	private CustomerService customerService;
	@Mock
	private TransactionService transactionService;
	
	@Test
	public void createAccount() {
		Mockito.doNothing().when(validationUtils)
				.validateCreateAccountDetails(Mockito.any(AccountRequestDetails.class));
		Mockito.when(customerService.checkCustomerExist(Mockito.anyString())).thenReturn(true);
		Mockito.when(commonUtils.getUniqueNumber()).thenReturn("TEST_ACCOUNT_ID");
		Mockito.when(commonUtils.getAccountDetailsFromDto(Mockito.any(AccountRequestDetails.class)))
				.thenReturn(DummyObjectProvider.setupAccountDetail());
		Mockito.doNothing().when(accountDetailsMapper).insert(Mockito.any(AccountDetail.class));
		Mockito.when(transactionService.intiateOperation(Mockito.any(TransactionRequestDetails.class)))
				.thenReturn(DummyObjectProvider.setupTrasnsactionDetailDto());
		Mockito.when(currencyConversionUtil.getBalanceDetails(Mockito.anyList(), Mockito.any(BigDecimal.class)))
				.thenReturn(DummyObjectProvider.getListOfBalances());
		Mockito.when(commonUtils.getAccountDetailsDtoFromAcccountDetail(Mockito.any(AccountDetail.class)))
				.thenReturn(DummyObjectProvider.setupAccountDetailDto());
		
		AccountDetailDto response = accountService.createAccount(DummyObjectProvider.setupAccountRequestDetails());
		assertEquals("TEST_ACCOUNT_ID", response.getAccountId());
	}

	@Test
	public void getAccount() {
		Mockito.when(accountDetailsMapper.getAccountDetail(Mockito.anyString()))
				.thenReturn(DummyObjectProvider.setupAccountDetail());
		Mockito.when(commonUtils.getAccountDetailsDtoFromAcccountDetail(Mockito.any(AccountDetail.class)))
				.thenReturn(DummyObjectProvider.setupAccountDetailDto());
		Mockito.when(commonUtils.getCurrecnyListFromString(Mockito.anyString())).thenReturn(Arrays.asList("USD"));
		Mockito.when(currencyConversionUtil.getBalanceDetails(Mockito.anyList(), Mockito.any(BigDecimal.class)))
				.thenReturn(DummyObjectProvider.getListOfBalances());

		AccountDetailDto response = accountService.getAccountDetails("TEST_ACCOUNT_ID");
		assertEquals("TEST_ACCOUNT_ID", response.getAccountId());
	}
}
