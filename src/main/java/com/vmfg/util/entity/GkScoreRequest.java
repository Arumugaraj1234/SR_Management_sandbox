package com.vmfg.util.entity;

import java.util.ArrayList;

public class GkScoreRequest {

	private String lineCode;
	private ArrayList<String> gkScoreType;
	private String tenantId;
	private String shiftId;
	private String gkDate;
	
	public String getGkDate() {
		return gkDate;
	}
	public void setGkDate(String gkDate) {
		this.gkDate = gkDate;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	public ArrayList<String> getGkScoreType() {
		return gkScoreType;
	}
	public void setGkScoreType(ArrayList<String> gkScoreType) {
		this.gkScoreType = gkScoreType;
	}
	
}
