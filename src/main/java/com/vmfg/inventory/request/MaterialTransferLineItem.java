package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialTransferLineItem {

    private String productId;
    private String productCode;
    private String desc;
    private String spec;
    private String transferQuantity;
    private String fromBin;
    private String toBin;
}
