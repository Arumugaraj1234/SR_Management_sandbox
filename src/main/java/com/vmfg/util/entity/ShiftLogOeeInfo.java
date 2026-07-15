package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftLogOeeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String shift;
	private String shiftDate;
	private String shiftYearMonth;
	private String shiftYear;
	private String shiftMonth;
	private String lineCode;
	private String shiftTypeCode;
	private String equipmentID;
	
	public String getShiftTypeCode() {
		return shiftTypeCode;
	}
	public void setShiftTypeCode(String shiftTypeCode) {
		this.shiftTypeCode = shiftTypeCode;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getShiftDate() {
		return shiftDate;
	}
	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}
	public String getShiftYearMonth() {
		return shiftYearMonth;
	}
	public void setShiftYearMonth(String shiftYearMonth) {
		this.shiftYearMonth = shiftYearMonth;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getShiftYear() {
		return shiftYear;
	}
	public void setShiftYear(String shiftYear) {
		this.shiftYear = shiftYear;
	}
	public String getShiftMonth() {
		return shiftMonth;
	}
	public void setShiftMonth(String shiftMonth) {
		this.shiftMonth = shiftMonth;
	}
	public String getEquipmentID() {
		return equipmentID;
	}
	public void setEquipmentID(String equipmentID) {
		this.equipmentID = equipmentID;
	}
	
}
