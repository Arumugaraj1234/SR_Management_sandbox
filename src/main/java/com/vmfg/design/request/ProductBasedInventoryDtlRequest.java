package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBasedInventoryDtlRequest {
private String productId;
private String pmHdrId;
private String tenantId;
}
