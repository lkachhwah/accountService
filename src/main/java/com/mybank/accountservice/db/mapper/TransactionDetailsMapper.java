package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.TrasnsactionDetail;

@Mapper
public interface TransactionDetailsMapper {

	@Select("SELECT * FROM TRANSACTIONDETAILS WHERE accountId = #{accountId}")
	List<TrasnsactionDetail> getTrasnsactionDetailsByAccountId(@Param("accountId") String accountId);
	
	 @Insert("INSERT INTO TRANSACTIONDETAILS(transactionId, customerId, accountId,transactionType,description,trasactionDate,amount,status,transactionCurrency,accountBalance,accountBalanceInUSD) VALUES "
	    		+ "(#{transactionId}, #{customerId},#{accountId},#{transactionType},#{description},#{trasactionDate},#{amount},#{status},#{transactionCurrency},#{accountBalance},#{accountBalanceInUSD})")
		public void  insert(TrasnsactionDetail  trasnsactionDetails);

}
