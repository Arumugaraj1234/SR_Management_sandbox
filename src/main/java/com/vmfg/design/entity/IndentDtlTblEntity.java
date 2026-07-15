package com.vmfg.design.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.scm.request.PodtlsForProductEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentDtlTblEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int sNo;
	private String indentDtlId;
	private String indentId;
	private String productCode;
	private String description;
	private String specification;
	private String weight;
	private String material;
	private String qty;
	private String make;
	private String unit;
	private String tenantId;
	private String uomDesc;
	private int dmId;
	private String remarks;
	private String totalQty;
	private String totalVal;
	private String assignTeamEmpName;
	private String assignTeamEmpId;
	private String indentTypeDesc;
	private String indentType;
	private int poCount;
	private String createdBy;
	private String fileNameExtn;
	private String isPdf;
	
	private List<PodtlsForProductEntity> poDtlList=new ArrayList<PodtlsForProductEntity>();

}
