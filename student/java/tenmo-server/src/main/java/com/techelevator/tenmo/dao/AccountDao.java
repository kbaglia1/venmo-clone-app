package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;


public interface AccountDao {

    Account getByUserId(long userId);

    void deposit(long toAccountId, BigDecimal transferAmount);

    void withdraw(long fromAccountId, BigDecimal transferAmount);

    boolean transferFunds(BigDecimal transferAmount, long fromAccountId, long toAccountId);

}
