package com.demo.rbc.mapper;

import cn.hutool.core.util.IdUtil;
import com.demo.rbc.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionMapperTest {

    // 通过类名初始化Logger实例
    private static final Logger logger = LoggerFactory.getLogger(TransactionMapperTest.class);

    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    void insert() {
        Transaction transaction = new Transaction();
        transaction.setTxId(IdUtil.getSnowflakeNextIdStr());
        transaction.setSourceAccountNo("AC0005913944");
        transaction.setTargetAccountNo("AC0035703396");
        transaction.setAmount(new BigDecimal("1.00"));

        logger.info("transaction : {}", transaction);
        int result = transactionMapper.insert(transaction);
        logger.info("result : {}", result);
        assertEquals(1, result);
    }
}