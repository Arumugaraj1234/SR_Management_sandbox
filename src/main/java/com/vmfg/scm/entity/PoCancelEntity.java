package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PoCancelEntity {

    private String poId;
    private String poCode;
    private String praAmount;
    private List<PoPaymentTermEntity> poPaymentTerm= new ArrayList<PoPaymentTermEntity>();
}
