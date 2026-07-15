package com.vmfg.authentication;

import java.io.Serializable;
import java.util.List;

public class UIModuleMst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int	uiModuleMstID	;
	private String	description	;
	private String	materialIcon	;
	private String	displayName	;
//	private String	linkUrl	;
	private int	seqNO	;
	private String	isActive	;
	private String	tenantId	;
	
	
	List<UIScreenMst> uiScreenMstList;
	
	public int getUiModuleMstID() {
		return uiModuleMstID;
	}
	public void setUiModuleMstID(int uiModuleMstID) {
		this.uiModuleMstID = uiModuleMstID;
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
//	public String getLinkUrl() {
//		return linkUrl;
//	}
//	public void setLinkUrl(String linkUrl) {
//		this.linkUrl = linkUrl;
//	}
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
	public List<UIScreenMst> getUiScreenMstList() {
		return uiScreenMstList;
	}
	public void setUiScreenMstList(List<UIScreenMst> uiScreenMstList) {
		this.uiScreenMstList = uiScreenMstList;
	}

	
	
	

}
