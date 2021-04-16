package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.TrasnsactionDetail;

@Mapper
public interface TransactionDetailsMapper {

	@Select("SELECT * FROM TRANSACTIONDETAILS WHERE customerId = #{customerId}")
	List<TrasnsactionDetail> getTrasnsactionDetailsByCustomerId(@Param("customerId") String customerId);
	
    
	 @Insert("INSERT INTO TRANSACTIONDETAILS(transactionId, customerId, accountNumber,transactionType,description,trasactionDate,amount,status) VALUES "
	    		+ "(#{transactionId}, #{customerId}, #{accountNumber},#{transactionType},#{description},#{trasactionDate},#{amount},#{status})")
		public void  insert(TrasnsactionDetail  trasnsactionDetails);

}
