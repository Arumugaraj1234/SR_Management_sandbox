package com.vmfg.util.entity;

import java.io.Serializable;

public class RejectionHdrDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private String branchCode;
	private String programCode;

	public String getProductCode() {
		return productCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

}
