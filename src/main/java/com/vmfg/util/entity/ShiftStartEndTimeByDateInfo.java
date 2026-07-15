package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftStartEndTimeByDateInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nextDate;
	private String shiftStart;
	private String shiftEnd;
	private String shiftMstId;
	private String nextCDate;
	private String shiftMstTypeCode;
	private String shiftDate;
	private String shiftConfigId;
	private String shiftDay;
	
	
	public String getShiftDate() {
		return shiftDate;
	}
	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}
	public String getNextCDate() {
		return nextCDate;
	}
	public void setNextCDate(String nextCDate) {
		this.nextCDate = nextCDate;
	}
	public int getNextDate() {
		return nextDate;
	}
	public void setNextDate(int nextDate) {
		this.nextDate = nextDate;
	}
	public String getShiftStart() {
		return shiftStart;
	}
	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}
	public String getShiftEnd() {
		return shiftEnd;
	}
	public void setShiftEnd(String shiftEnd) {
		this.shiftEnd = shiftEnd;
	}
	public String getShiftMstId() {
		return shiftMstId;
	}
	public void setShiftMstId(String shiftMstId) {
		this.shiftMstId = shiftMstId;
	}
	public String getShiftMstTypeCode() {
		return shiftMstTypeCode;
	}
	public void setShiftMstTypeCode(String shiftMstTypeCode) {
		this.shiftMstTypeCode = shiftMstTypeCode;
	}
	public String getShiftConfigId() {
		return shiftConfigId;
	}
	public void setShiftConfigId(String shiftConfigId) {
		this.shiftConfigId = shiftConfigId;
	}
	public String getShiftDay() {
		return shiftDay;
	}
	public void setShiftDay(String shiftDay) {
		this.shiftDay = shiftDay;
	}
	
}
