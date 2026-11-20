package com.vmfg.quality.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// QC Inspection Report "Download All": the report CSV exactly as the screen's "Export to CSV" would write
// it (for the rows the user currently sees), plus each row's indent line for its design document.
@Getter
@Setter
public class QiDownloadAllRequest {
	private String tenantId;
	private String pmHdrId;
	private String csv;
	private List<Row> rows;

	@Getter
	@Setter
	public static class Row {
		private String indentDtlId;
		private String productCode;
	}
}
