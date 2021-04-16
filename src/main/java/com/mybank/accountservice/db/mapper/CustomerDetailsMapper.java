package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.CustomerDetail;

@Mapper
public interface CustomerDetailsMapper {


	@Select("select * from CustomerDetails  where customerId = #{customerId}")
	@Results({
		@Result(property = "customerId", column ="customerId"),
        @Result(property = "name", column = "name"),
        @Result(property = "emailId", column= "emailId"),
        @Result(property = "accounts", column = "customerId",javaType= List.class ,
            		many = @Many(select = "com.mybank.accountservice.db.mapper.AccountDetailsMapper.getAccountDetailByCustomerId")),
		@Result(property = "transactions", column = "customerId",javaType= List.class ,
		many = @Many(select = "com.mybank.accountservice.db.mapper.TransactionDetailsMapper.getTrasnsactionDetailsByCustomerId"))
		
	})
	CustomerDetail getCustomerDetails(@Param("customerId") String customerId);
	
	@Insert("INSERT INTO CustomerDetails(customerId, name, emailId,gender,dob) VALUES (#{customerId}, #{name}, #{emailId},#{gender},#{dob})")
	public void  insert(CustomerDetail  customerDetails);
}
