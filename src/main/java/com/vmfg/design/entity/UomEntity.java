package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uomCode;
	private String uomDesc;
}
