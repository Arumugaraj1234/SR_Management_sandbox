package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoHsnEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private String uomCode;
	private String uomDesc;
}
