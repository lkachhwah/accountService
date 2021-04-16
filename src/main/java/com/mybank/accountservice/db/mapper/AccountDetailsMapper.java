package com.mybank.accountservice.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mybank.accountservice.db.model.AccountDetail;
import com.mybank.accountservice.db.model.CustomerDetail;


@Mapper
public interface AccountDetailsMapper {

	@Select("SELECT * FROM ACCOUNTDETAILS WHERE accountNumber = #{accountNumber}")
	AccountDetail getAccountDetail(@Param("accountNumber") String accountNumber);
	
	
	@Select("SELECT * FROM ACCOUNTDETAILS WHERE customerId = #{customerId}")
	List<AccountDetail> getAccountDetailByCustomerId(@Param("customerId") String customerId);
    
    @Insert("INSERT INTO ACCOUNTDETAILS(accountNumber, description, customerId,accountType,openedByBranch,openedOn,balance) VALUES "
    		+ "(#{accountNumber}, #{description}, #{customerId},#{accountType},#{openedByBranch},#{openedOn},#{balance})")
	public void  insert(AccountDetail  accountDetail);
    
    @Update("Update ACCOUNTDETAILS set balance= #{balance} where accountNumber=#{accountNumber}")
    public void updateAmount(AccountDetail accountDetail);
}
