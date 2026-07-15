package com.vmfg.general.services.interfaces;

import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.project.request.ProjectHdrRequest;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;

public interface ITileViewService {

	ResponseAsList getSaleTileView(GetEnqDtlbyDateRequest getEnqDtlbyDateReq);
	ResponseAsList getDesignTitleView(DesignRequest designReq);
	ResponseAsList getProjectTitleView(ProjectHdrRequest tenReq);
	ResponseAsList getSCMTitleView(ScmHdrBasedDtlRequest scmHdrBasedDtl);
	ResponseAsList getFinanceTitleView(DesignRequest designReq);
	ResponseAsList getAssyTitleView(GetAssyDtlRequest getAssyDtlReq);
	ResponseAsList getQualityView(GetQtyDtlRequest getQtyDtlReq);
}
