package com.demo.rbc.mapper;

import com.demo.rbc.entity.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account WHERE account_no = #{accountNo}")
    @Results({
            @Result(property = "accountNo", column = "account_no"),
            @Result(property = "balance", column = "balance"),
            @Result(property = "version", column = "version")
    })
    Account findByAccountNo(String accountNo);
    
    @Update("UPDATE account SET balance = #{balance}, version = version + 1" +
            "WHERE id = #{id} AND version = #{version}")
    int updateBalanceWithVersion(Account account);
}