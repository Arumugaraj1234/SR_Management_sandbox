package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustmentTypeDropDownEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String adjustmenttypeId;
	private String adjustmenttypeDesc;

}
