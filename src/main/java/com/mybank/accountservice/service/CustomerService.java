package com.mybank.accountservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.accountservice.db.mapper.CustomerDetailsMapper;

@Service
public class CustomerService {

	@Autowired
	CustomerDetailsMapper customerDetailsMapper;

	// This method is used to  get whether a paticulat user is present in DB or not
	public boolean checkCustomerExist(String customerId) {
		return customerDetailsMapper.getCustomerDetails(customerId).isPresent();
	}
}
