package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.*;



import java.math.BigDecimal;

public class AccountDaoTests extends BaseDaoTests {

    private static final Account ACCOUNT1 = new Account(1, 1, new BigDecimal("500.00"));
    private static final Account ACCOUNT2 = new Account(2, 2, new BigDecimal("750.00"));
    private static final Account ACCOUNT3 = new Account(3, 3, new BigDecimal("1000.00"));
    private static final Account ACCOUNT4 = new Account(4, 4, new BigDecimal("250.00"));

    private JdbcAccountDao accountDao;
    private Account testAccount;
    @Before
    public void setup(){
        accountDao = new JdbcAccountDao(dataSource);
    }

    @Test
    public void test_getAccountByUserId(){
        Assert.assertEquals(1,1);

    }

    @Test
    public void test_withdraw(){
        Assert.assertEquals(1,1);
    }

    @Test
    public void test_deposit(){
        Assert.assertEquals(1,1);
    }

    @Test
    public void test_transferFunds(){
        Assert.assertEquals(1,1);
    }
}
