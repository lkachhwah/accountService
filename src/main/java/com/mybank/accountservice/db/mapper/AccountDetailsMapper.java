package com.mybank.accountservice.db.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mybank.accountservice.db.model.AccountDetail;


@Mapper
public interface AccountDetailsMapper {

	@Select("SELECT * FROM ACCOUNTDETAILS WHERE accountId = #{accountId}")
	AccountDetail getAccountDetail(@Param("accountId") String accountId);
	
	
	@Select("SELECT * FROM ACCOUNTDETAILS WHERE customerId = #{customerId}")
	List<AccountDetail> getAccountDetailByCustomerId(@Param("customerId") String customerId);
    
    @Insert("INSERT INTO ACCOUNTDETAILS(accountId, customerId,country,openedOnDate,balance,allowedCurrencies) VALUES "
    		+ "(#{accountId},#{customerId},#{country},#{openedOnDate},#{balance},#{allowedCurrencies})")
	public void  insert(AccountDetail  accountDetail);
    
    
    @Update("Update ACCOUNTDETAILS set balance= #{balance} where accountId=#{accountId}")
    public void updateBalanceForAccount(@Param("accountId") String accountId,@Param("balance") BigDecimal balance);
}
