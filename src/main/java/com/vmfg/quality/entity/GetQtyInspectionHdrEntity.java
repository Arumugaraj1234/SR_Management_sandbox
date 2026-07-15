package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQtyInspectionHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String qiHdrId;
	private String qiId;
	private String pmHdrId;
	private String qualityRefNo;
	private String inspectionQty;
	private String okQty;
	private String caInternal;
	private String caVendor;
	private String revisionDate;
	private String inspectionType;
	private String configName;
	private String reworkInternal;
	private String reworkVendor;
	private String rejectedInternal;
	private String rejectedExternal;
	private String qualityRating;
	private String inspectedBy;
	private String inspectedOn;
	private String sequenceNo;
	private String sequenceStatus;
	private String isCompleted;
	private String tenantId;
	private String totalCa;
	private String totalRework;
	private String totalrejected;
	private String pmHdrDesc;
	private String pmHdrCode;
	private String pmHdrCustomerName;
	private String poCode;
	private String productCode;
	private String uomDesc;
	private String vendorName;
	private int sNo;
	private String description;
	private String empId;
	private String empDesc;
	private String inwardRating;
	private String supplierRating;
	private int inputEnable;
	private int inputQtyEnable;
	private String oldsupplierRating;
	private String customerComplaint;

}
