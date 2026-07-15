package com.vmfg.util.entity;

import java.io.Serializable;

public class ThreePointCheckSheetEmailConfigHdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String preparedByName;
	private String checkedByName;
	private String approvedByName;
	private String preparedBy;
	private String checkedBy;
	private String approvedBy;
	public String getPreparedByName() {
		return preparedByName;
	}
	public void setPreparedByName(String preparedByName) {
		this.preparedByName = preparedByName;
	}
	public String getCheckedByName() {
		return checkedByName;
	}
	public void setCheckedByName(String checkedByName) {
		this.checkedByName = checkedByName;
	}
	public String getApprovedByName() {
		return approvedByName;
	}
	public void setApprovedByName(String approvedByName) {
		this.approvedByName = approvedByName;
	}
	public String getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	

}
