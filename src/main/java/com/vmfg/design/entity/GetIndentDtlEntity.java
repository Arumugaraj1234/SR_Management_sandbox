package com.vmfg.design.entity;

import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class GetIndentDtlEntity {
	private int approveBtnEnable;
	private String targetValue;
	private String allocatedValue;
	private String avilablevalue;
	private String seq;
	private String poId;
	private int isFlag;
	List<IndentDtlTblEntity> dtlList;
	List<DocumentStatusMstEntity> docLifeCycleMstList;
	
}
