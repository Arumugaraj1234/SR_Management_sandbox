package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsHdrRetrieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String msName;
	private String status;
	private String stageQty;
	private String createdOn;
	private String createdBy;
	private String msHdrId;
	private String pmHdrId;
	// The Staging Group's own UOM label (material_staging_hdr.UOM_CODE), set once when the
	// group was created - NOT a real uom_mst.UOM_CODE foreign key, just the label text as
	// typed/picked at creation time (see AssemblyStagingDAO.insertMsHdr).
	private String uomCode;
}
