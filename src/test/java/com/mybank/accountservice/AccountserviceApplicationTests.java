package com.mybank.accountservice;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mybank.accountservice.AccountserviceApplication;
import com.mybank.accountservice.db.mapper.AccountDetailsMapper;
import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;
import com.mybank.accountservice.db.mapper.TransactionDetailsMapper;
import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.CustomerDetail;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.service.AccountService;
@SpringBootTest(
		webEnvironment=WebEnvironment.RANDOM_PORT,
		classes= AccountserviceApplication.class
	)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application.properties")
class AccountserviceApplicationTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	CustomerDetailsMapper customerDetailsMapper;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	TransactionDetailsMapper transactionDetailsMapper;
	
	@Test
	void contextLoads() throws Exception {
		
		customerDetailsMapper.insert(CustomerDetail.builder().customerId("Lalit").emailId("laitkkac@gmai.com").name("Lk").build());
		AccountDetailDto accountDetailDto=accountService.createAccount(AccountRequestDetails.builder()
				.balance(new BigDecimal(60))
				.country("India")
				.customerId("Lalit")
				.currencies(Arrays.asList("EUR","SEK","USD"))
				.build());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("accountId", accountDetailDto.getAccountId()))
				.andReturn();
		
		System.out.println("Component are integrated :"+result.getResponse().getContentAsString());
		Assertions.assertTrue(200==result.getResponse().getStatus());
	}

}
