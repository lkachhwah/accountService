package com.mybank.accountservice.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.TransactionStatus;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;

public class DummyObjectProvider {
	
	private static AccountDetail accountDetail;
	private static AccountDetailDto accountDetailDto;
	private static AccountRequestDetails accountRequestDetails;
	private static TransactionRequestDetails transactionRequestDetails;
	private static TrasnsactionDetailDto trasnsactionDetailDto;
	private static TrasnsactionDetail trasnsactionDetail;

	public static String createAccountReqJson = "{\r\n"
			+ "  \"balance\": 1000,\r\n"
			+ "  \"country\": \"United States\",\r\n"
			+ "  \"currencies\": [\r\n"
			+ "    \"USD\"\r\n"
			+ "  ],\r\n"
			+ "  \"customerId\": \"lkachhwah\"\r\n"
			+ "}";
	
	public static String addTransactionReqJson = "{\r\n"
			+ "  \"accountId\": \"123456789\",\r\n"
			+ "  \"amount\": 10,\r\n"
			+ "  \"description\": \"Credit 10 Dollar\",\r\n"
			+ "  \"transactionCurrency\": \"USD\",\r\n"
			+ "  \"transactionType\": \"IN\"\r\n"
			+ "}";
	
	public static AccountDetailDto setupAccountDetailDto() {
		accountDetailDto = new AccountDetailDto();
		accountDetailDto.setAccountId("TEST_ACCOUNT_ID");
		accountDetailDto.setCountry("United States");
		accountDetailDto.setBalance(new BigDecimal(10));;
		return accountDetailDto;
	}
	
	public static Optional<AccountDetail> setupAccountDetail() {
		accountDetail = new AccountDetail();
		accountDetail.setAccountId("TEST_ACCOUNT_ID");
		accountDetail.setCountry("United States");
		return Optional.of(accountDetail) ;
	}
	
	public static AccountRequestDetails setupAccountRequestDetails() {
		accountRequestDetails = new AccountRequestDetails();
		accountRequestDetails.setCustomerId("lkachhwah");
		accountRequestDetails.setCountry("United States");
		accountRequestDetails.setBalance(new BigDecimal(15.22));
		accountRequestDetails.setCurrencies(Arrays.asList("EUR", "SEK", "USD"));
		return accountRequestDetails;
	}
	
	public static TransactionRequestDetails setupTransactionRequestDetails() {
		transactionRequestDetails = new TransactionRequestDetails();
		transactionRequestDetails.setAccountId("TEST_ACCOUNT_ID");
		transactionRequestDetails.setAmount(new BigDecimal(100));
		transactionRequestDetails.setTransactionCurrency("USD");
		transactionRequestDetails.setTransactionType("IN");
		transactionRequestDetails.setDescription("TestDEscription");
		return transactionRequestDetails;
	}
	
	public static TrasnsactionDetailDto setupTrasnsactionDetailDto() {
		trasnsactionDetailDto = new TrasnsactionDetailDto();
		trasnsactionDetailDto.setAccountId("TEST_ACCOUNT_ID");
		trasnsactionDetailDto.setCustomerId("lkachhwah");
		trasnsactionDetailDto.setDescription("TestDesc");
		trasnsactionDetailDto.setStatus(TransactionStatus.SUCCESS);
		return trasnsactionDetailDto;
	}
	
	public static TrasnsactionDetail setupTrasnsactionDetail() {
		trasnsactionDetail = new TrasnsactionDetail();
		trasnsactionDetail.setAccountId("TEST_ACCOUNT_ID");
		trasnsactionDetail.setCustomerId("lkachhwah");
		return trasnsactionDetail;
	}
	
	public static List<TrasnsactionDetailDto> getTransactionDtoList() {
		List<TrasnsactionDetailDto> trasnsactionDetailDtos = new ArrayList<>();
		trasnsactionDetailDtos.add(setupTrasnsactionDetailDto());
		return trasnsactionDetailDtos;
	}
	
	public static List<TrasnsactionDetail> getTransactionList() {
		List<TrasnsactionDetail> trasnsactionDetailList = new ArrayList<>();
		trasnsactionDetailList.add(setupTrasnsactionDetail());
		return trasnsactionDetailList;
	}
	
	public static List<BalanceDetailsDto>  getListOfBalances() {
		List<BalanceDetailsDto>  balances = new ArrayList<>();
		BalanceDetailsDto usdBalance = new BalanceDetailsDto();
		usdBalance.setAmount(new BigDecimal(100));
		usdBalance.setCurrency(CurrencyType.USD);
		
		balances.add(usdBalance);
		return balances;
	}
}
