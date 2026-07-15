package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMaterialIssueDtlRequest {
private String mrDtlId;
private String productId;
private String requestedQty;
private String availableQty;
private String issuedQty;
private String tenantId;
}
