package com.masterPool.MasterPoolBiiling.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="customer")
public class CustomerInfo {
    
     @Id
     private String customerMobileNo;  
     private String customerName;
     private int maxCreditLimit;
     


     
    public CustomerInfo(String customerMobileNo, String customerName, int maxCreditLimit) {
		super();
		this.customerMobileNo = customerMobileNo;
		this.customerName = customerName;
		this.maxCreditLimit = maxCreditLimit;
	}
    
    


	public int getMaxCreditLimit() {
		return maxCreditLimit;
	}




	public void setMaxCreditLimit(int maxCreditLimit) {
		this.maxCreditLimit = maxCreditLimit;
	}




	public CustomerInfo() {
    }

 
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }
}
