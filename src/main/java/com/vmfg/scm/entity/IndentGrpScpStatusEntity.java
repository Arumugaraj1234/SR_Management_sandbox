package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentGrpScpStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String igScpStatusId;
	private String igScpId;
	private String seqNo;
	private String seqStatus;
	private String remarks;
}
