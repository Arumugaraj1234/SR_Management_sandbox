package com.vmfg.quality.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QiCaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String projCode;
	private String projName;
	private String caRaisedBy;
	private String caApprovedBy;
	private String caApprovedOn;
	private String durationTime;
	
	private String qiCaDtlId;
    private String qiHdrId;
    private String pmHdrId;
    private String poId;
    private String poCode;
    private String poDtlId;
    private String indentDtlId;
    private String productCode;
    private String productDescription;
    private String uom;
    private String uomShortDescription;
    private String indentQty;
    private String caType;
    private int qty;
    private String reqReceivedDatetime;
    private int reworkInternal;
    private int reworkVendor;
    private int rejectedInternal;
    private int rejectedExternal;
    private int caQty;
    private int caVendor;
    private int caInternal;
    private String sequenceNo;
    private String sequenceStatus;
    private String documentStatusTypeDescription;
    private String isApproved;
    private String isEditable;
    private String tenantId;
    private String remarks;
    private String vendorName;
    private String qiId;
    private String dmId;
    private String masterPoc;
    private String inspectionQty;
	private List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
}
