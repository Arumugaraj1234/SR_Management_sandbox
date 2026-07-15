package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetPoDtlsByDate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String poId;
	private String poCode;
	private String vendorName;
	private String vendorCode;
	

}
