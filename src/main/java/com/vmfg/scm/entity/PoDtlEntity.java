package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.design.entity.IndentDtlTblEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String poDtlId;
	private String poId;
	private String indentDtlId;
	private String hsnCode;
	private String poGst;
	private String qty;
	private String uomCode;
	private String deliveryDate;
	private String currencyType;
	private String unitRateFx;
	private String unitRate;
	private String totalValueFx;
	private String totalValue;
	private String productCode;
	private String inspectedQty;
	private String receivedQty;
	private String qtyInspectReqCount;
	private String nOkCount;
	private String reWorkCount;
	private String serviceNo;
	private String materialDesc;
	private String qcRequestedQty;
	private String pendingQty; // pending qty for qc request
	private String inwardQty;
	List<IndentDtlTblEntity> indentDtlList;

}
