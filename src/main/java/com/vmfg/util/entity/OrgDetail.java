package com.vmfg.util.entity;

import java.io.Serializable;

public class OrgDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private String organizationCode;
	private String organaizationName;
	private String logoPath;
	private String isActive;
	private String tenantId;
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getOrganaizationName() {
		return organaizationName;
	}
	public void setOrganaizationName(String organaizationName) {
		this.organaizationName = organaizationName;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
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
	 
}
