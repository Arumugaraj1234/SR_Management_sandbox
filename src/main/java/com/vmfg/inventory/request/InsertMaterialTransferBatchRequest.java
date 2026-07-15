package com.vmfg.inventory.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMaterialTransferBatchRequest {

    private String tenantId;
    private String createdBy;
    private String fromPmHdrId;
    private String toPmHdrId;
    private String fromInventoryLocationCode;
    private String toInventoryLocationCode;
    private String remark;
    private List<MaterialTransferLineItem> items;
}
