package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPlanAndActualEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String planCount;
	private String actualCount;
}
