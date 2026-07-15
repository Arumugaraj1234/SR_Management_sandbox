package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftLogHdr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int shiftLogHdrID;
	private String tenantId;
	private String slEmpId;

	public int getShiftLogHdrID() {
		return shiftLogHdrID;
	}

	public void setShiftLogHdrID(int shiftLogHdrID) {
		this.shiftLogHdrID = shiftLogHdrID;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSlEmpId() {
		return slEmpId;
	}

	public void setSlEmpId(String slEmpId) {
		this.slEmpId = slEmpId;
	}

}
