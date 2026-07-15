package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftMaster implements Serializable {
	private static final long serialVersionUID = 1L;
	private String shiftMasterHdrId;
	private String shiftDays;
	private String shiftStartTime;
	private String shiftEndTime;
	private String shiftActivityTypeCode;
	private String shiftActivityTypeDescription;
	private String activityStartTime;
	private String activityEndTime;
	private String activityDtlId;
	private String shiftMstTypeCode;
	private String shiftMstTypeDesc;
	private String isActive;
	private int shiftDtlCount;
	
	
	
	
	public int getShiftDtlCount() {
		return shiftDtlCount;
	}

	public void setShiftDtlCount(int shiftDtlCount) {
		this.shiftDtlCount = shiftDtlCount;
	}

	public String getShiftActivityTypeCode() {
		return shiftActivityTypeCode;
	}

	public void setShiftActivityTypeCode(String shiftActivityTypeCode) {
		this.shiftActivityTypeCode = shiftActivityTypeCode;
	}

	public String getShiftActivityTypeDescription() {
		return shiftActivityTypeDescription;
	}

	public void setShiftActivityTypeDescription(String shiftActivityTypeDescription) {
		this.shiftActivityTypeDescription = shiftActivityTypeDescription;
	}

	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getShiftMstTypeCode() {
		return shiftMstTypeCode;
	}

	public void setShiftMstTypeCode(String shiftMstTypeCode) {
		this.shiftMstTypeCode = shiftMstTypeCode;
	}

	public String getShiftMstTypeDesc() {
		return shiftMstTypeDesc;
	}

	public void setShiftMstTypeDesc(String shiftMstTypeDesc) {
		this.shiftMstTypeDesc = shiftMstTypeDesc;
	}

	

	public String getActivityDtlId() {
		return activityDtlId;
	}

	public void setActivityDtlId(String activityDtlId) {
		this.activityDtlId = activityDtlId;
	}

	/*public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}*/

	public String getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public String getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	public String getShiftMasterHdrId() {
		return shiftMasterHdrId;
	}

	public void setShiftMasterHdrId(String shiftMasterHdrId) {
		this.shiftMasterHdrId = shiftMasterHdrId;
	}

	public String getShiftDays() {
		return shiftDays;
	}

	public void setShiftDays(String shiftDays) {
		this.shiftDays = shiftDays;
	}

}
