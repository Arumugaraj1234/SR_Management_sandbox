package com.vmfg.util.entity;

import java.io.Serializable;

public class ConfigOrganisationInfoEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String  isInventoryEnabled;
	private String  isWMSEnabled;
	public String getIsInventoryEnabled() {
		return isInventoryEnabled;
	}
	public void setIsInventoryEnabled(String isInventoryEnabled) {
		this.isInventoryEnabled = isInventoryEnabled;
	}
	public String getIsWMSEnabled() {
		return isWMSEnabled;
	}
	public void setIsWMSEnabled(String isWMSEnabled) {
		this.isWMSEnabled = isWMSEnabled;
	}
	
	
	
}
