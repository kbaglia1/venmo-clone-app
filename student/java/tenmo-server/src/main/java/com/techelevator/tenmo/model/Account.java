package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private long id;
    private long userId;
    private BigDecimal balance;

    public Account(){
    }

    public Account(long id, long userId, BigDecimal balance){
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
