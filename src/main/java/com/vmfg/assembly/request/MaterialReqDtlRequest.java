package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReqDtlRequest {

  private String seqNo;
  private String tenantId;
  private String empId;
  private String lastSeq;
  private String hdrId;
  List<MaterialDtlRequest> mrDtlReqList;
	
}
