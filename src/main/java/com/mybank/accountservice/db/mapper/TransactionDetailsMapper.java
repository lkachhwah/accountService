package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.TrasnsactionDetails;

@Mapper
public interface TransactionDetailsMapper {

	@Select("SELECT * FROM TRANSACTIONDETAILS WHERE customerId = #{customerId}")
	List<TrasnsactionDetails> getTrasnsactionDetailsByCustomerId(@Param("customerId") String customerId);
}
