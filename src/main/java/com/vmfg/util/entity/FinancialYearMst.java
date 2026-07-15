package com.vmfg.util.entity;

import java.io.Serializable;

public class FinancialYearMst implements Serializable{

	private static final long serialVersionUID = 1L;
	private String financialYear;
	private String startDate;
	private String endDate;
	private String financialYearId;
	private String isactive;
	private String currentFY;
	
	
	
	
	public String getCurrentFY() {
		return currentFY;
	}
	public void setCurrentFY(String currentFY) {
		this.currentFY = currentFY;
	}
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFinancialYearId() {
		return financialYearId;
	}
	public void setFinancialYearId(String financialYearId) {
		this.financialYearId = financialYearId;
	}
	
	
}
