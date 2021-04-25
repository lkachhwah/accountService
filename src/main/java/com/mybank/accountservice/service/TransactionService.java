package com.mybank.accountservice.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.TrasnsactionDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.dto.BalanceDetailsDto;
import com.mybank.accountservice.dto.PublisherDto;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.enums.CurrencyType;
import com.mybank.accountservice.enums.FailureCode;
import com.mybank.accountservice.enums.TransactionStatus;
import com.mybank.accountservice.enums.TransactionType;
import com.mybank.accountservice.exception.AccountServiceException;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.utils.CommonUtils;
import com.mybank.accountservice.utils.CurrencyConversionUtil;
import com.mybank.accountservice.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {

	@Autowired
	TransactionDetailsMapper transactionDetailsMapper;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	ValidationUtils validationUtils;

	@Autowired
	AccountService accountService;

	@Autowired
	CurrencyConversionUtil currencyConversionUtil;

	@Autowired
	EventPublisherService eventPublisherService;

	int retryCount = 5;

	// This method is used to perform transaction in existing account, update
	// balance in account and Publish message to reporting service
	public TrasnsactionDetailDto intiateOperation(TransactionRequestDetails transactionRequestDetails, int count,
			String tranactionId) {
		tranactionId = StringUtils.isEmpty(tranactionId) ? commonUtils.getUniqueNumber() : tranactionId;
		validationUtils.validatePerformTransactionDetails(transactionRequestDetails);
		log.info("[intiateOperation] -START acountId:{} ,tranactionId:{}", transactionRequestDetails.getAccountId(),
				tranactionId);
		AccountDetailDto accountDetailDto = accountService.getAccountDetails(transactionRequestDetails.getAccountId());
		BigDecimal updatedBalance = accountDetailDto.getBalance();

		TrasnsactionDetail trasnsactionDetail = getTransactionDetail(transactionRequestDetails, accountDetailDto,
				updatedBalance);
		trasnsactionDetail.setTransactionId(tranactionId);

		try {
			log.info("Transaction Account details:{},,tranactionId:{}", accountDetailDto, tranactionId);
			preOperationCheck(transactionRequestDetails, accountDetailDto);
			updatedBalance = updatedBalance(transactionRequestDetails, accountDetailDto);
			trasnsactionDetail.setAccountBalance(currencyConversionUtil.getValueByCurrency(updatedBalance,
					trasnsactionDetail.getTransactionCurrency()));
			updateDetailsInDB(transactionRequestDetails, tranactionId, accountDetailDto, updatedBalance,
					trasnsactionDetail);
			publishMessage(trasnsactionDetail);

		} catch (AccountServiceException e) {
			log.error(
					"[intiateOperation] -EXCEPTION Check Retry acountId:{} , ErrorCode:{} ,ErrorMessage:{} ,tranactionId:{}",
					transactionRequestDetails.getAccountId(), e.getCode(), e.getMessage(), tranactionId);
			checkAndRetry(transactionRequestDetails, count, trasnsactionDetail, e);
			throw e;
		} catch (Exception e) {
			log.error(
					"[intiateOperation] -EXCEPTION Something Went wrong : acountId:{} ,ErrorMessage:{} ,tranactionId:{}",
					transactionRequestDetails.getAccountId(), e.getMessage(), tranactionId);
			throw new AccountServiceException(FailureCode.CD15, e);
		}
		log.info("[intiateOperation] -END acountId:{} ,TransactionId:{}", trasnsactionDetail.getAccountId(),
				tranactionId);
		return commonUtils.getDTOFromTransactionDetail(trasnsactionDetail);
	}

	// Publish Message to topic
	private void publishMessage(TrasnsactionDetail trasnsactionDetail) {
		PublisherDto<TrasnsactionDetail> data = new PublisherDto<TrasnsactionDetail>();
		data.add(trasnsactionDetail);
		eventPublisherService.asyncMethodWithVoidReturnType(data);
	}

	// Update Data in DB
	@Transactional(rollbackFor = Exception.class)
	private void updateDetailsInDB(TransactionRequestDetails transactionRequestDetails, String tranactionId,
			AccountDetailDto accountDetailDto, BigDecimal updatedBalance, TrasnsactionDetail trasnsactionDetail) {
		trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
		trasnsactionDetail.setStatus(TransactionStatus.SUCCESS);
		accountService.updateBalance(accountDetailDto.getAccountId(), accountDetailDto.getVersion(), updatedBalance);
		log.info("[intiateOperation] -ACOUNT UPDATED acountId:{},,tranactionId:{}",
				transactionRequestDetails.getAccountId(), tranactionId);
		transactionDetailsMapper.insert(trasnsactionDetail);
	}

	// this methode check if transaction failed for a specific error code and
	// retry accordingly
	private void checkAndRetry(TransactionRequestDetails transactionRequestDetails, int count,
			TrasnsactionDetail trasnsactionDetail, AccountServiceException e) {
		if (Objects.nonNull(e.getCode()) && FailureCode.CD16 == e.getCode() && count <= retryCount) {
			log.info("[intiateOperation] -Retry For  acountId:{} , TransactionId:{}", trasnsactionDetail.getAccountId(),
					trasnsactionDetail.getTransactionId());
			intiateOperation(transactionRequestDetails, count++, trasnsactionDetail.getTransactionId());
		}
	}

	// this methode is used to get TransactionDetail object from
	// transactionRequestDetails
	private TrasnsactionDetail getTransactionDetail(TransactionRequestDetails transactionRequestDetails,
			AccountDetailDto accountDetailDto, BigDecimal updatedBalance) {
		TrasnsactionDetail trasnsactionDetail = new TrasnsactionDetail();
		trasnsactionDetail
				.setTransactionCurrency(CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()));
		trasnsactionDetail.setTransactionType(TransactionType.valueOf(transactionRequestDetails.getTransactionType()));
		trasnsactionDetail.setAccountId(transactionRequestDetails.getAccountId());
		trasnsactionDetail.setDescription(transactionRequestDetails.getDescription());
		trasnsactionDetail.setAmount(transactionRequestDetails.getAmount());
		trasnsactionDetail.setTrasactionDate(new Date());
		trasnsactionDetail.setAccountBalance(
				currencyConversionUtil.getValueByCurrency(updatedBalance, trasnsactionDetail.getTransactionCurrency()));
		trasnsactionDetail.setCustomerId(accountDetailDto.getCustomerId());
		trasnsactionDetail.setAccountBalanceInUSD(updatedBalance);
		trasnsactionDetail.setFailureReason("NA");
		return trasnsactionDetail;
	}

	private BigDecimal updatedBalance(TransactionRequestDetails transactionRequestDetails,
			AccountDetailDto accountDetailDto) {

		BigDecimal balance = accountDetailDto.getBalance();

		BigDecimal trasactionAmountInUSD = currencyConversionUtil.getUSDValueOfCurrency(
				transactionRequestDetails.getAmount(),
				CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()));
		switch (TransactionType.valueOf(transactionRequestDetails.getTransactionType())) {
		case IN:
			balance = balance.add(trasactionAmountInUSD);
			break;
		case OUT:
			log.info("Trasaction amount after Convertion:{}", trasactionAmountInUSD);
			balance = balance.subtract(trasactionAmountInUSD);
			break;
		default:
			break;
		}

		return balance;
	}

	private void preOperationCheck(TransactionRequestDetails transactionRequestDetails,
			AccountDetailDto accountDetailDto) {
		BalanceDetailsDto balanceDetailsDto = commonUtils.checkCurrencySupportedByAccount(
				CurrencyType.valueOf(transactionRequestDetails.getTransactionCurrency()),
				accountDetailDto.getBalanceInDifferentCurrency());
		if (TransactionType.OUT == TransactionType.valueOf(transactionRequestDetails.getTransactionType())) {
			commonUtils.checkSufficientBalanceToPerformOperation(transactionRequestDetails, balanceDetailsDto);
		}
	}

	public List<TrasnsactionDetailDto> getTrasactionDetails(String accountId) {
		log.info("[getTrasactionDetails] -START acountId:{}", accountId);
		List<TrasnsactionDetail> trasnsactionDetailsList = transactionDetailsMapper
				.getTrasnsactionDetailsByAccountId(accountId);
		if (CollectionUtils.isEmpty(trasnsactionDetailsList)) {
			log.error("[getTrasactionDetails] -NOT FOUND acountId:{}", accountId);
			throw new AccountServiceException(FailureCode.CD12, HttpStatus.BAD_REQUEST);

		}
		log.info("[getTrasactionDetails] -Found acountId:{}", accountId);
		return commonUtils.convertListToTransactionDetailDto(trasnsactionDetailsList);

	}
}
