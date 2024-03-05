package com.masterPool.MasterPoolBiiling.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="table_info")
public class TableInfo {

	@Id
        @GeneratedValue(strategy = GenerationType.AUTO)
	private int tableId;
	private String tableInfo;
	private int pricePerHours;
	private String tableStaus;



	public TableInfo() {
		super();
	}
	
	public TableInfo(int tableId, String tableInfo, int pricePerHours, String tableStaus) {
		super();
		this.tableId = tableId;
		this.tableInfo = tableInfo;
		this.pricePerHours = pricePerHours;
		this.tableStaus = tableStaus;
	}
	
	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public String getTableInfo() {
		return tableInfo;
	}
	public void setTableInfo(String tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	
	public int getPricePerHours() {
		return pricePerHours;
	}

	public void setPricePerHours(int pricePerHours) {
		this.pricePerHours = pricePerHours;
	}

	public String getTableStaus() {
		return tableStaus;
	}

	public void setTableStaus(String tableStaus) {
		this.tableStaus = tableStaus;
	}
}
