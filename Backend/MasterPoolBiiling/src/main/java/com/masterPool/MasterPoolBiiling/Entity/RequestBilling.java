package com.masterPool.MasterPoolBiiling.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="RequestBilling")
public class RequestBilling {
    
    @Id
    private int tableNo;
    private int minutes;
    private String customerName;
    private String customerNumber;
    private String cashierUserId;
    private String billingMode;

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(String cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public String getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(String billingMode) {
        this.billingMode = billingMode;
    }

   
    
   
}
