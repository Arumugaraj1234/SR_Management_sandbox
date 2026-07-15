package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectKeyAreaMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyId;
	private String pkId;
	private String keyName;
	private String pkaId;
	private String code;
}
