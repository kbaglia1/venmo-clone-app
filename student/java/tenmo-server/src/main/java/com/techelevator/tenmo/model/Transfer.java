package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long id;
    private long fromUserId;
    private long toUserId;
    private long fromAccountId;
    private long toAccountId;
    private int typeId;
    private int statusId;
    private BigDecimal amount;

    public Transfer(){}

    public Transfer(long id, long fromUserId, long toUserId, int typeId, int statusId, BigDecimal amount) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.typeId = typeId;
        this.statusId = statusId;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getFromUserId() {
        return fromUserId;
    }
    public void setFromUserId(long userId_from) {
        this.fromUserId = userId_from;
    }
    public long getToUserId() {
        return toUserId;
    }
    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public int getStatusId() {
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
