package com.mybank.accountservice.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerDetails {

	@NonNull
	private String customerId;
	@NonNull
	private String name;
	@NonNull
	private String emailId;
	private Date dob;
	private GenderType gender;
	private List<AccountDetail> accounts;
	private List<TrasnsactionDetails> transactions ;
}
