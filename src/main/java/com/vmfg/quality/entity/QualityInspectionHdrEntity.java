package com.vmfg.quality.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityInspectionHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String qiHdrId;
    private String qiId;
    private String pmHdrId;
    private String vendorCode;
    private String qualityRefNo;
    private String drawingNo;
    private String inspectionQty;
    private String revisionDate;
    private String inspectionType;
    private String configName;
    private String directlyAccepted;
    private String caInternal;
    private String caVendor;
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
    private String nrFlag;
    private String tenantId;
    private String statusDesc;
    private String isEditable;
    private String empId;
    private String totalOkQty;
    private String okRemarks;
    private String totalNokQty; 
    private String nokRemarks;
    private String totalRejcInt;
    private String rejectIntRemarks;
    private String totalRejcExt;
    private String rejectExtRemarks;
    private String totalReworkInt;
    private String reworkIntRemarks;
    private String totalReworkVen;
    private String reworkVenRemarks;
    

    
    private String cancelFlag;
	private String qicHdrId;
    private String qicName;
    private String qicCreatedOn;
    private String qicCreatedBy;
    private String isActive;
    private String pmId;
    private String qHdrId;
    private String poId;
    private List<QualityInspectionDtlEntity> dtlList=new ArrayList<QualityInspectionDtlEntity>();
    List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
}
