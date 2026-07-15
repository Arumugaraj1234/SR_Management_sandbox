package com.vmfg.util.entity;

import java.io.Serializable;

public class OeeShiftMstInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String shiftTypeCode;
	private String shiftTypeDesc;
	public String getShiftTypeCode() {
		return shiftTypeCode;
	}
	public void setShiftTypeCode(String shiftTypeCode) {
		this.shiftTypeCode = shiftTypeCode;
	}
	public String getShiftTypeDesc() {
		return shiftTypeDesc;
	}
	public void setShiftTypeDesc(String shiftTypeDesc) {
		this.shiftTypeDesc = shiftTypeDesc;
	}
	
}
