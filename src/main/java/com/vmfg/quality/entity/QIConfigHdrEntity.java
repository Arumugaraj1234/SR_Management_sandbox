package com.vmfg.quality.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QIConfigHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String qicHdrId;
    private String qicName;
    private String qicCreatedOn;
    private String qicCreatedBy;
    private String inspectionType;
    private String isActive;
    private String tenantId;
    private List<QIConfigDtlEntity> qiConfigDtlList=new ArrayList<QIConfigDtlEntity>();
}
