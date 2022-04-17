package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private long id;
    private String fromUsername;
    private String toUsername;
    private int typeId;
    private int statusId;
    private BigDecimal amount;

    public TransferDTO(){}

    public TransferDTO(long id, String fromUsername, String toUsername, int typeId, int statusId, BigDecimal amount) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
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
    public String getFromUsername() {
        return fromUsername;
    }
    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }
    public String getToUsername() {
        return toUsername;
    }
    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
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
