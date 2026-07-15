package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocTypeMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String docCode;
	private String docDesc;
}
