package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DesignWidgetDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String projCnt;
    private String avgTasktime;
    private String avgProjtime;
}
