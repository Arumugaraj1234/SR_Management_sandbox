package com.vmfg.util.entity;

import java.io.Serializable;

public class FlashReportEmailConfigHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maintenanceApprovedByName;
	private String productionApprovedByName;
	private String peApprovedByName;
	private String qualityApprovedByName;
	private String maintenanceApprovedBy;
	private String productionApprovedBy;
	private String peApprovedBy;
	private String qualityApprovedBy;

	public String getMaintenanceApprovedByName() {
		return maintenanceApprovedByName;
	}

	public void setMaintenanceApprovedByName(String maintenanceApprovedByName) {
		this.maintenanceApprovedByName = maintenanceApprovedByName;
	}

	public String getProductionApprovedByName() {
		return productionApprovedByName;
	}

	public void setProductionApprovedByName(String productionApprovedByName) {
		this.productionApprovedByName = productionApprovedByName;
	}

	public String getPeApprovedByName() {
		return peApprovedByName;
	}

	public void setPeApprovedByName(String peApprovedByName) {
		this.peApprovedByName = peApprovedByName;
	}

	public String getQualityApprovedByName() {
		return qualityApprovedByName;
	}

	public void setQualityApprovedByName(String qualityApprovedByName) {
		this.qualityApprovedByName = qualityApprovedByName;
	}

	public String getMaintenanceApprovedBy() {
		return maintenanceApprovedBy;
	}

	public void setMaintenanceApprovedBy(String maintenanceApprovedBy) {
		this.maintenanceApprovedBy = maintenanceApprovedBy;
	}

	public String getProductionApprovedBy() {
		return productionApprovedBy;
	}

	public void setProductionApprovedBy(String productionApprovedBy) {
		this.productionApprovedBy = productionApprovedBy;
	}

	public String getPeApprovedBy() {
		return peApprovedBy;
	}

	public void setPeApprovedBy(String peApprovedBy) {
		this.peApprovedBy = peApprovedBy;
	}

	public String getQualityApprovedBy() {
		return qualityApprovedBy;
	}

	public void setQualityApprovedBy(String qualityApprovedBy) {
		this.qualityApprovedBy = qualityApprovedBy;
	}

}
