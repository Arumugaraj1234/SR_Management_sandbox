package com.vmfg.mis.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorTypeCateCountListEntity {
	private List<VendorTypeCateCountEntity> vendorType = new ArrayList<VendorTypeCateCountEntity>(); 
	private List<VendorTypeCateCountEntity> vendorCategory = new ArrayList<VendorTypeCateCountEntity>(); 
}
