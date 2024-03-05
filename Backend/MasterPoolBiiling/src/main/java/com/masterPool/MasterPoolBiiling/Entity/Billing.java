package com.masterPool.MasterPoolBiiling.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.*;

@Entity
@Table(name = "Billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int billNo;
    private String description;
    private String customerName;
    private String customerNumer;
    @Temporal(TemporalType.DATE)
    private Date date;
    private int totalTimePlayed;
    private int billAmount;
    private String billingBy;
    private String billingMode;
    private String billType;
    private String status;

    public Billing(int billNo, String description, String customerName, String customerNumer, Date date, int totalTimePlayed, int billAmount, String billingBy, String billingMode, String billType, String status) {
        this.billNo = billNo;
        this.description = description;
        this.customerName = customerName;
        this.customerNumer = customerNumer;
        this.date = date;
        this.totalTimePlayed = totalTimePlayed;
        this.billAmount = billAmount;
        this.billingBy = billingBy;
        this.billingMode = billingMode;
        this.billType = billType;
        this.status = status;
    }

    public Billing() {
        super();
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumer() {
        return customerNumer;
    }

    public void setCustomerNumer(String customerNumer) {
        this.customerNumer = customerNumer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalTimePlayed() {
        return totalTimePlayed;
    }

    public void setTotalTimePlayed(int totalTimePlayed) {
        this.totalTimePlayed = totalTimePlayed;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(int billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillingBy() {
        return billingBy;
    }

    public void setBillingBy(String billingBy) {
        this.billingBy = billingBy;
    }

    public String getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(String billingMode) {
        this.billingMode = billingMode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
}
