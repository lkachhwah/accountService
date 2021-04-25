package com.mybank.accountservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.dto.TrasnsactionDetailDto;
import com.mybank.accountservice.request.TransactionRequestDetails;
import com.mybank.accountservice.service.TransactionService;
import com.mybank.accountservice.utils.DummyObjectProvider;

@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private TransactionController transactionController;
	@Mock
	private TransactionService transactionService;

	private JacksonTester<TrasnsactionDetailDto> transactionDtoJacksonTester;
	private JacksonTester<List<TrasnsactionDetailDto>> transactionDtoListJacksonTester;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
		ObjectMapper objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);
	}

	@Test
	public void testPerformOperation() throws Exception {
		Mockito.when(transactionService.intiateOperation(Mockito.any(TransactionRequestDetails.class), Mockito.anyInt(),
				Mockito.anyString())).thenReturn(DummyObjectProvider.setupTrasnsactionDetailDto());
		createMethodAndVerifyResponse("/transaction", DummyObjectProvider.addTransactionReqJson);
	}

	@Test
	public void testGetAccount() throws Exception {
		Mockito.when(transactionService.getTrasactionDetails(Mockito.anyString()))
				.thenReturn(DummyObjectProvider.getTransactionDtoList());
		createGetMethodAndVerifyListResponse("/transaction", "TEST_ACCOUNT_ID");
	}

	private void createMethodAndVerifyResponse(String methodUrl, String reqJson) throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(methodUrl).content(reqJson)
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(201, result.getResponse().getStatus());
		
		}

	private void createGetMethodAndVerifyListResponse(String methodUrl, String accountId) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(methodUrl)
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("accountId", accountId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String mockResponse = result.getResponse().getContentAsString();

		List<TrasnsactionDetailDto> trasnsactionDetailDtoList = transactionDtoListJacksonTester
				.parseObject(mockResponse);
		assertEquals("TEST_ACCOUNT_ID", trasnsactionDetailDtoList.get(0).getAccountId());
	}
}
