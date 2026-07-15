package com.vmfg.util.entity;

import java.io.Serializable;

public class ThreePointInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private String lineDesc;
	private String programDesc;

	public String getProductCode() {
		return productCode;
	}

	public String getLineDesc() {
		return lineDesc;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

}
