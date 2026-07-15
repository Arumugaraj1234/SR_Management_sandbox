package com.vmfg.authentication;

import java.io.Serializable;

public class UIScreenMst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int	uiModuleMstID	;
	private int uiScreenMstID;
	private String	description	;
	private String	materialIcon	;
	private String	displayName	;
	private String	linkUrl	;
	private int	seqNO	;
	private String	isActive	;
	private String	tenantId	;
	private String subModule;
	public int getUiModuleMstID() {
		return uiModuleMstID;
	}
	public void setUiModuleMstID(int uiModuleMstID) {
		this.uiModuleMstID = uiModuleMstID;
	}
	public int getUiScreenMstID() {
		return uiScreenMstID;
	}
	public void setUiScreenMstID(int uiScreenMstID) {
		this.uiScreenMstID = uiScreenMstID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMaterialIcon() {
		return materialIcon;
	}
	public void setMaterialIcon(String materialIcon) {
		this.materialIcon = materialIcon;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public int getSeqNO() {
		return seqNO;
	}
	public void setSeqNO(int seqNO) {
		this.seqNO = seqNO;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getSubModule() {
		return subModule;
	}
	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	
	

}
