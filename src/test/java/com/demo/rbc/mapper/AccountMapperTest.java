package com.demo.rbc.mapper;

import com.demo.rbc.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountMapperTest {

    // 通过类名初始化Logger实例
    private static final Logger logger = LoggerFactory.getLogger(AccountMapperTest.class);

    @Autowired
    private AccountMapper accountMapper;

    @Test
    void findByAccountNo() {
        // 准备测试数据（假设数据库已存在 account_no = 'ACCT123' 的记录）
        Account account = accountMapper.findByAccountNo("AC0005913944");
        logger.info("account : {}", account);

        assertNotNull(account);
        assertEquals("AC0005913944", account.getAccountNo());
        assertEquals(0, account.getVersion());
    }

    @Test
    void updateBalanceWithVersion() {
        // 获取测试账户
        Account account = accountMapper.findByAccountNo("AC0005913944");
        int originalVersion = account.getVersion();
        
        // 修改余额
        int result = accountMapper.addBalanceWithVersion("AC0005913944", new BigDecimal(10.00), 0);
        
        assertEquals(1, result);
        // 验证版本号更新
        Account updated = accountMapper.findByAccountNo("AC0005913944");
        assertEquals(originalVersion + 1, updated.getVersion());
    }
}