package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.DesignMisRequest;
import com.vmfg.mis.request.DesignReportMisRequest;

public interface IDesignMisService {

	ResponseAsList getDesignWidgetDtl(DesignMisRequest designMisReq);

	ResponseAsList getPlannedProject(DesignMisRequest designMisReq);

	ResponseAsList getPlannedActivity(DesignMisRequest designMisReq);
	
	ResponseAsList getTaskCompPerBymonth(DesignMisRequest designMisReq);
	
	ResponseAsList getTaskCompPerByYear(DesignReportMisRequest designMisReq);
	
	ResponseAsMessage getTeamMemberEnableCheck(DesignReportMisRequest designMisReq);
	
	ResponseAsList getDesignWidgetDtlByCategory(DesignMisRequest designMisReq);
	
	ResponseAsMessage updateReportTaskMonth();
	ResponseAsMessage updateReportTaskWeek();
	
}
