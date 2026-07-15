package com.vmfg.design.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentStatusTypeCode implements Serializable {

	private static final long serialVersionUID = 1L;
	private String documentStatusTypeCode;
	private String documentStatusTypeDesc;
	
}
