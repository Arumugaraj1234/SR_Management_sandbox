package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentHdrDropDownEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indentId;
	private String indentCode;
	private String expectedDeliveryDate;

}
