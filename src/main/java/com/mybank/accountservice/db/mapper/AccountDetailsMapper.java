package com.mybank.accountservice.db.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mybank.accountservice.db.model.AccountDetail;


@Mapper
public interface AccountDetailsMapper {

	@Select("SELECT * FROM ACCOUNTDETAILS WHERE accountId = #{accountId}")
	Optional<AccountDetail> getAccountDetail(@Param("accountId") String accountId);
	
    @Insert("INSERT INTO ACCOUNTDETAILS(accountId, customerId,country,openedOnDate,balance,allowedCurrencies,stampKey) VALUES "
    		+ "(#{accountId},#{customerId},#{country},#{openedOnDate},#{balance},#{allowedCurrencies},#{stampKey})")
	public void  insert(AccountDetail  accountDetail);
    
    
    @Update("Update ACCOUNTDETAILS set balance= #{balance},stampKey= #{stampKey} where accountId=#{accountId}")
    public void updateBalanceForAccount(@Param("accountId") String accountId,@Param("stampKey") String stampKey,@Param("balance") BigDecimal balance);
}
