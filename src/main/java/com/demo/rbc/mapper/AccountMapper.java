package com.demo.rbc.mapper;

import com.demo.rbc.entity.Account;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account WHERE account_no = #{accountNo}")
    @Results({
            @Result(property = "accountNo", column = "account_no"),
            @Result(property = "balance", column = "balance"),
            @Result(property = "version", column = "version")
    })
    Account findByAccountNo(String accountNo);
    
    @Update("UPDATE account SET balance = balance - #{transferAmount}, version = version + 1 " +
            "WHERE account_no = #{accountNo} AND version = #{version}")
    int deductBalanceWithVersion(String accountNo, BigDecimal transferAmount, Integer version);

    @Update("UPDATE account SET balance = balance + #{transferAmount}, version = version + 1 " +
            "WHERE account_no = #{accountNo} AND version = #{version}")
    int addBalanceWithVersion(String accountNo, BigDecimal transferAmount, Integer version);
}