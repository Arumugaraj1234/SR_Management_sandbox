package com.vmfg.finance.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraCancelRequest {
    String praId;
    String poId;
    String potId;
    String tenantId;
}
