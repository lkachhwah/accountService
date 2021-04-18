package com.mybank.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.PublisherDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.DummyObjectProvider;
import com.mybank.accountservice.utils.ValidationUtils;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

	@InjectMocks
	private TransactionService transactionService;

	@Mock
	private TransactionDetailsMapper transactionDetailsMapper;
	@Mock
	private CommonUtils commonUtils;
	@Mock
	private ValidationUtils validationUtils;
	@Mock
	private AccountService accountService;
	@Mock
	private CurrencyConversionUtil currencyConversionUtil;
	@Mock
	private EventPublisherService eventPublisherService;

	@Test
	public void intiateOperation() {
		Mockito.doNothing().when(validationUtils)
				.validatePerformTransactionDetails(Mockito.any(TransactionRequestDetails.class));
		Mockito.when(accountService.getAccountDetails(Mockito.anyString()))
				.thenReturn(DummyObjectProvider.setupAccountDetailDto());
		Mockito.when(commonUtils.getUniqueNumber()).thenReturn("TEST_TRANSACTION_ID");

		Mockito.when(currencyConversionUtil.getValueByCurrency(Mockito.any(BigDecimal.class),
				Mockito.any(CurrencyType.class))).thenReturn(new BigDecimal(100));
		Mockito.when(currencyConversionUtil.getUSDValueOfCurrency(Mockito.any(BigDecimal.class),
				Mockito.any(CurrencyType.class))).thenReturn(new BigDecimal(100));

		Mockito.doNothing().when(accountService).updateBalance(Mockito.anyString(), Mockito.any(BigDecimal.class));
		Mockito.doNothing().when(transactionDetailsMapper).insert(Mockito.any(TrasnsactionDetail.class));
		Mockito.doNothing().when(eventPublisherService).asyncMethodWithVoidReturnType(Mockito.any(PublisherDto.class));

		Mockito.when(commonUtils.getDTOFromTransactionDetail(Mockito.any(TrasnsactionDetail.class)))
				.thenReturn(DummyObjectProvider.setupTrasnsactionDetailDto());
		
		TrasnsactionDetailDto response = transactionService.intiateOperation(DummyObjectProvider.setupTransactionRequestDetails());
		assertNotNull(response);
		assertEquals("TEST_ACCOUNT_ID", response.getAccountId());
	}
	
	
	@Test
	public void getTrasactionDetails() {
		Mockito.when(transactionDetailsMapper.getTrasnsactionDetailsByAccountId(Mockito.anyString()))
				.thenReturn(DummyObjectProvider.getTransactionList());
		Mockito.when(commonUtils.convertListToTransactionDetailDto(Mockito.anyList()))
				.thenReturn(DummyObjectProvider.getTransactionDtoList());

		List<TrasnsactionDetailDto> response = transactionService.getTrasactionDetails("TEST_ACCOUNT_ID");
		assertNotNull(response);
		assertEquals("TEST_ACCOUNT_ID", response.get(0).getAccountId());
	}
}