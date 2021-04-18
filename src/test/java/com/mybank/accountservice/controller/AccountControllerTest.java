package com.mybank.accountservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.dto.AccountDetailDto;
import com.mybank.accountservice.request.AccountRequestDetails;
import com.mybank.accountservice.service.AccountService;
import com.mybank.accountservice.utils.DummyObjectProvider;

@ExtendWith(SpringExtension.class)
public class AccountControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private AccountController accountController;
	@Mock
	private AccountService accountService;
	
	private JacksonTester<AccountDetailDto> createAccountDtoJacksonTester;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
		ObjectMapper objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);
	}
	
	@Test
	public void testCreateAccount() throws Exception {
		Mockito.when(accountService.createAccount(Mockito.any(AccountRequestDetails.class)))
				.thenReturn(DummyObjectProvider.setupAccountDetailDto());
		createMethodAndVerifyResponse("/account", DummyObjectProvider.createAccountReqJson);
	}
	
	@Test
	public void testGetAccount() throws Exception {
		Mockito.when(accountService.getAccountDetails(Mockito.anyString()))
				.thenReturn(DummyObjectProvider.setupAccountDetailDto());
		createGetMethodAndVerifyResponse("/account", "TEST_ACCOUNT_ID");
	}
	
	private void createMethodAndVerifyResponse(String methodUrl, String reqJson) throws Exception {
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(methodUrl)
				.content(reqJson)
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String mockResponse = result.getResponse().getContentAsString();
		
		AccountDetailDto accountDetailDto = createAccountDtoJacksonTester.parseObject(mockResponse);
		assertEquals("TEST_ACCOUNT_ID", accountDetailDto.getAccountId());
	}
	
	private void createGetMethodAndVerifyResponse(String methodUrl, String accountId) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(methodUrl)
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("accountId", accountId);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String mockResponse = result.getResponse().getContentAsString();
		
		AccountDetailDto accountDetailDto = createAccountDtoJacksonTester.parseObject(mockResponse);
		assertEquals("TEST_ACCOUNT_ID", accountDetailDto.getAccountId());
	}
}
