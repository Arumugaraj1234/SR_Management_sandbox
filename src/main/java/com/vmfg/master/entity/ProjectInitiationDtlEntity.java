package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectInitiationDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String piId;
    private String deptCode;
    private String departmentName;
    private String primaryPoc;
    private String masterPoc;
    private String departmentAssigned;
   
}
