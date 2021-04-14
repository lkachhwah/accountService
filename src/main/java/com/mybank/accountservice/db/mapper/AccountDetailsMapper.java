package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.AccountDetail;


@Mapper
public interface AccountDetailsMapper {

	@Select("SELECT * FROM ACCOUNTDETAILS WHERE accountNumber = #{accountNumber}")
	AccountDetail getAccountDetail(@Param("accountNumber") String accountNumber);
	
	
	@Select("SELECT * FROM ACCOUNTDETAILS WHERE customerId = #{customerId}")
	List<AccountDetail> getAccountDetailByCustomerId(@Param("customerId") String customerId);
}
