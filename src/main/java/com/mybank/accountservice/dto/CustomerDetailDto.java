package com.mybank.accountservice.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerDetailDto {
	private String customerId;
	private String name;
	private String emailId;
	private List<AccountDetailDto> accounts;
}
