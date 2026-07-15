package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.project.request.DesignWidgetDtlReq;

public interface IAssemblyMisService {

	ResponseAsList getAssyMisWidgetDtl(DesignWidgetDtlReq designWidgetDtl);

	ResponseAsList getTaskCompTimeResp(DesignWidgetDtlReq designWidgetDtl);

	ResponseAsList getPojCompDtl(DesignWidgetDtlReq designWidgetDtl);

	ResponseAsList getAssyTaskReport(DesignWidgetDtlReq designWidgetDtl);

	ResponseAsList getAssyDtlTaskReportResp(DesignWidgetDtlReq designWidgetDtl);

	ResponseAsList getProjectProgressDtls(DesignWidgetDtlReq designWidgetDtl);

}
