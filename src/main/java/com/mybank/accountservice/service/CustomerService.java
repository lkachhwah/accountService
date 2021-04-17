package com.mybank.accountservice.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;

@Service
public class CustomerService {

	@Autowired
	CustomerDetailsMapper customerDetailsMapper;

	public boolean checkCustomerExist(String customerId) {
		if (Objects.nonNull(customerDetailsMapper.getCustomerDetails(customerId)))
			return true;
		else
			return false;
	}
}
