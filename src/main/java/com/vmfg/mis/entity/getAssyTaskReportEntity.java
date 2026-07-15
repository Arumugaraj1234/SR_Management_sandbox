package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getAssyTaskReportEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projId;
	private String projCode;
	private String openTask;
	private String pendingTask;
	private String completedTask;
	private String completedPer;
}
