package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getAssyDtlTaskReportEntity implements Serializable {

	private static final long serialVersionUID = 1L;
 
	private String projId;
	private String projCode;
	private String pendingTask;
	private String delayTask;
	private String compPer;
	private String openTask;
}
