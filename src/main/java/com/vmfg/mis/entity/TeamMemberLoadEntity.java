package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberLoadEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inspCall;
	private String InspOn;
	private String inspQty;

}
