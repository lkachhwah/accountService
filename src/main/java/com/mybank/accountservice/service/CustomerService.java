package com.mybank.accountservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;

@Service
public class CustomerService {

	@Autowired
	CustomerDetailsMapper customerDetailsMapper;

	public boolean checkCustomerExist(String customerId) {
		if (customerDetailsMapper.getCustomerDetails(customerId).isPresent())
			return true;
		else
			return false;
	}
}
