package com.vmfg.util.entity;

import java.io.Serializable;

public class ProductionOTEmplInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productionManagerApprovedName;
	private String hrManagerApprovedName;
	private String qualityManagerApprovedName;
	private String plantManagerApprovedName;

	private String productionManagerApprovedBY;
	private String hrManagerApprovedBY;
	private String qualityManagerApprovedBY;
	private String plantManagerApprovedBy;

	public String getProductionManagerApprovedName() {
		return productionManagerApprovedName;
	}

	public String getHrManagerApprovedName() {
		return hrManagerApprovedName;
	}

	public String getQualityManagerApprovedName() {
		return qualityManagerApprovedName;
	}

	public String getPlantManagerApprovedName() {
		return plantManagerApprovedName;
	}

	public String getProductionManagerApprovedBY() {
		return productionManagerApprovedBY;
	}

	public String getHrManagerApprovedBY() {
		return hrManagerApprovedBY;
	}

	public String getQualityManagerApprovedBY() {
		return qualityManagerApprovedBY;
	}

	public String getPlantManagerApprovedBy() {
		return plantManagerApprovedBy;
	}

	public void setProductionManagerApprovedName(String productionManagerApprovedName) {
		this.productionManagerApprovedName = productionManagerApprovedName;
	}

	public void setHrManagerApprovedName(String hrManagerApprovedName) {
		this.hrManagerApprovedName = hrManagerApprovedName;
	}

	public void setQualityManagerApprovedName(String qualityManagerApprovedName) {
		this.qualityManagerApprovedName = qualityManagerApprovedName;
	}

	public void setPlantManagerApprovedName(String plantManagerApprovedName) {
		this.plantManagerApprovedName = plantManagerApprovedName;
	}

	public void setProductionManagerApprovedBY(String productionManagerApprovedBY) {
		this.productionManagerApprovedBY = productionManagerApprovedBY;
	}

	public void setHrManagerApprovedBY(String hrManagerApprovedBY) {
		this.hrManagerApprovedBY = hrManagerApprovedBY;
	}

	public void setQualityManagerApprovedBY(String qualityManagerApprovedBY) {
		this.qualityManagerApprovedBY = qualityManagerApprovedBY;
	}

	public void setPlantManagerApprovedBy(String plantManagerApprovedBy) {
		this.plantManagerApprovedBy = plantManagerApprovedBy;
	}

}
