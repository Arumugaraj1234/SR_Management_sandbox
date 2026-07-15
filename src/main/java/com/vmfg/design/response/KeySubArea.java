package com.vmfg.design.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeySubArea implements Serializable {

	private static final long serialVersionUID = 1L;
	private String keyId;
	private String keyName;
}
