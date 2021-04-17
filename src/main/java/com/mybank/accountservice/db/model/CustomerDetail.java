package com.mybank.accountservice.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mybank.accountservice.enums.GenderType;

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
public class CustomerDetail {

	@NonNull
	private String customerId;
	@NonNull
	private String name;
	@NonNull
	private String emailId;
}
