package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorCategoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String vendorCategory;
    private String tenantId;
    private String vendorId;
}

