
package com.mybank.accountservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

	@Autowired
	CurrencyConversionUtil currencyConversionUtil;

	@Autowired
	ValidationUtils validationUtils;

	@Autowired
	AccountDetailsMapper accountDetailsMapper;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	CustomerService customerService;

	@Autowired
	TransactionService transactionService;

	@Transactional(rollbackFor=Exception.class)
	public AccountDetailDto createAccount(AccountRequestDetails accountRequestDetails) {
		validationUtils.validateCreateAccountDetails(accountRequestDetails);
		log.info("[createAccount] START for CustomerID:{}", accountRequestDetails.getCustomerId());

		if (!customerService.checkCustomerExist(accountRequestDetails.getCustomerId())) {
			log.error("[createAccount] NOT FOUND for CustomerID:{}", accountRequestDetails.getCustomerId());
			throw new AccountServiceException(FailureCode.CD14, HttpStatus.BAD_REQUEST);
		}

		AccountDetail accountDetail = getNewAcountDetiail(accountRequestDetails);
		log.debug("[createAccount] - INSERT-START for CustomerID:{} Acount Details: {}",
				accountRequestDetails.getCustomerId(), accountDetail.getAccountId());
		accountDetailsMapper.insert(accountDetail);
		log.debug("[createAccount] - INSERT-END for CustomerID:{}", accountRequestDetails.getCustomerId(),
				accountDetail.getAccountId());

		log.debug("[createAccount] - Transaction-START for CustomerID:{} Acount Details: {}",
				accountRequestDetails.getCustomerId(), accountDetail.getAccountId());
		transactionService.intiateOperation(TransactionRequestDetails.builder().accountId(accountDetail.getAccountId())
				.amount(accountRequestDetails.getBalance()).description("Initial Acount opened")
				.transactionCurrency(CurrencyType.USD.toString()).transactionType(TransactionType.IN.toString())
				.build(), 1,null);
		log.debug("[createAccount] - Transaction-END for CustomerID:{} Acount Details: {}",
				accountRequestDetails.getCustomerId(), accountDetail.getAccountId());

		log.info("[createAccount] END for CustomerID:{} acountID:{}", accountRequestDetails.getCustomerId(),
				accountDetail.getAccountId());
		return getAccountDetails(accountDetail.getAccountId());
	}

	private AccountDetail getNewAcountDetiail(AccountRequestDetails accountRequestDetails) {
		AccountDetail accountDetail = commonUtils.getAccountDetailsFromDto(accountRequestDetails);
		accountDetail.setAccountId(commonUtils.getUniqueNumber());
		accountDetail.setVersion(0);
		accountDetail.setOpenedOnDate(new Date());
		accountDetail.setBalance(new BigDecimal(0));
		return accountDetail;
	}

	public AccountDetailDto getAccountDetails(String accountId) {
		log.info("[getAccountDetails] START for accountId:{}", accountId);
		Optional<AccountDetail> accountDetail = accountDetailsMapper.getAccountDetail(accountId);
		if (!accountDetail.isPresent()) {
			log.error("[getAccountDetails] Details not found accountId:{}", accountId);
			throw new AccountServiceException(FailureCode.CD12, HttpStatus.BAD_REQUEST);
		}
		AccountDetailDto accountDetailDto = getAcountDetailsDto(accountDetail);
		log.info("[getAccountDetails] END for accountId:{}", accountId);
		return accountDetailDto;
	}

	private AccountDetailDto getAcountDetailsDto(Optional<AccountDetail> accountDetail) {
		AccountDetail details = accountDetail.get();
		AccountDetailDto accountDetailDto = commonUtils.getAccountDetailsDtoFromAcccountDetail(details);
		List<BalanceDetailsDto> balances = currencyConversionUtil.getBalanceDetails(
				commonUtils.getCurrecnyListFromString(details.getAllowedCurrencies()), accountDetailDto.getBalance());
		accountDetailDto.setBalanceInDifferentCurrency(balances);
		return accountDetailDto;
	}

	public void updateBalance(String accountId,int version, BigDecimal balance) {
		synchronized (this) {
			accountDetailsMapper.updateBalanceForAccount(accountId,version,balance);
		}
	}

}
