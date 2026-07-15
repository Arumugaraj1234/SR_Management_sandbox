package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentGrpScpVenPtEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String igScpVpt;
	private String igScpId;
	private String level;
	private String term;
	private String percentage;
	private String remarks;
}
