package com.demo.rbc.mapper;

import com.demo.rbc.entity.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transaction (tx_id, source_account_no, target_account_no, amount) " +
            "VALUES (#{txId}, #{sourceAccountNo}, #{targetAccountNo}, #{amount})")
    int insert(Transaction transaction);
}