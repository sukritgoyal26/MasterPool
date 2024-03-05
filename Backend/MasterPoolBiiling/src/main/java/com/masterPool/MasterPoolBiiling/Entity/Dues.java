package com.masterPool.MasterPoolBiiling.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;


@Entity
@Table(name="dues")
public class Dues {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int billno;
    private String description;
    private String customerName;
    private String customerNumber;
    private int amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String billingBy;
    private String status;

   
   
    public Dues() {
    }

    public Dues(int billno, String description, String customerName, String customerNumber, int amount, Date date, String billingBy, String status) {
        this.billno = billno;
        this.description = description;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.amount = amount;
        this.date = date;
        this.billingBy = billingBy;
        this.status = status;
    }

    public int getBillno() {
        return billno;
    }

    public void setBillno(int billno) {
        this.billno = billno;
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

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBillingBy() {
        return billingBy;
    }

    public void setBillingBy(String billingBy) {
        this.billingBy = billingBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Dues{" + "billno=" + billno + ", description=" + description + ", customerName=" + customerName + ", customerNumber=" + customerNumber + ", amount=" + amount + ", date=" + date + ", billingBy=" + billingBy + ", status=" + status + '}';
    }

    
    
    
   

   
    

    
    
    
}
