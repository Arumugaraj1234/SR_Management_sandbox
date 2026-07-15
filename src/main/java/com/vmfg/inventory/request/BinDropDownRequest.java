package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinDropDownRequest {
    private String tenantId;
    private String pmHdrId;
    private String productId;
}