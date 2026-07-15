package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBinEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productId;
	private String bin;

}
