package com.mybank.accountservice.db.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.CustomerDetail;

@Mapper
public interface CustomerDetailsMapper {

	@Select("select * from CustomerDetails  where customerId = #{customerId}")
	Optional<CustomerDetail> getCustomerDetails(@Param("customerId") String customerId);

	@Insert("INSERT INTO CustomerDetails(customerId, name, emailId) VALUES (#{customerId}, #{name}, #{emailId})")
	public void insert(CustomerDetail customerDetails);
}
