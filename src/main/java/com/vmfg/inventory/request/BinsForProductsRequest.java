package com.vmfg.inventory.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinsForProductsRequest {
    private String tenantId;
    private String pmHdrId;
    private List<String> productIds;
}
