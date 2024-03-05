package com.masterPool.MasterPoolBiiling.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="User")
public class User {
   
	@Id
	private String userId;
	private String sname;
	private String userType;
	private String password;
	
	 public User() {
	}

	public User(String userId, String sname, String userType) {
		super();
		this.userId = userId;
		this.sname = sname;
		this.userType = userType;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	
}
