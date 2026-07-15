package com.vmfg.design.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IndentPartDetailsEntity {
    private int indentId;
    private String indentCode;
    private int stationId;
    private String stationNo;
    private int subAssyId;
    private String subAssyDesc;
    private List<PartEntity> parts;

    // transient for row mapping
    private String partNo;
    private String partDesc;

}
