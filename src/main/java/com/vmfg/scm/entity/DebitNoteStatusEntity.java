package com.vmfg.scm.entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitNoteStatusEntity {
	
	private static final long serialVersionUID = 1L;
    private String dnId;
	private String seqno;
	private String seqStatus;
	private String seqDesc;
    private String remarks;
    private String updatedBy;
    private String empName;
    private String updatedOn;
    private String tenantId;

}
