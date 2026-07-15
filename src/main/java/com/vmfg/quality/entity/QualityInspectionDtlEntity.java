package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityInspectionDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String qiDtlId;
    private String qiHdrId;
    private int sNo;
    private String serialNumber;
    private String description;
    private String specification;
    private String inspectionMethod;
    private String minimum;
    private String maximum;
    private String average;
    private String inspectionResult;
    private String qicDtlId;
	private String qicHdrId;
    private String inspectionType;
	private String isActive;
    private String tenantId;

}
