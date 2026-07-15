package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.design.entity.IndentDtlTblEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MaterialInwardDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String miDtlId;
	private String miId;
	private String poDtlId;
	private String indentDtlId;
	private String orderedQty;
	private String receivedQty;
	private String inspectedQty;
	private String reworkQty;
	private String nokQty;
	private String uom;
	private String uomDesc;
	private String tenantId;
	private String inwardQty;
	private String inspectQty;
	private String qtyInspectReqCount;
	private String productId;
	private String pendingQty;  // pending Qty to raise qc request
	private List<IndentDtlTblEntity> indentDtlList =null;

}
