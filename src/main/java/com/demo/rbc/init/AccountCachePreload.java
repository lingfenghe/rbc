package com.demo.rbc.init;

import com.demo.rbc.mapper.AccountMapper;
import com.demo.rbc.service.AccountService;
import com.demo.rbc.service.impl.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountCachePreload implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AccountCachePreload.class);

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("run account cache preload at appplication start");
        accountService.preloadtRecentUpdatedAccountsToCache();
    }
}
