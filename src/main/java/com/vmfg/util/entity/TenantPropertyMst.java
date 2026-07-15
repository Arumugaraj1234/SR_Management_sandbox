package com.vmfg.util.entity;

import java.io.Serializable;

public class TenantPropertyMst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String propertyName;
	private String propertyValue;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	

}
