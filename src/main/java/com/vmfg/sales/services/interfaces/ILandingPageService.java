package com.vmfg.sales.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.sales.request.GetEnqDtlbySlaveIdRequest;

public interface ILandingPageService {

	ResponseAsList getEnqDtlbyDate(GetEnqDtlbyDateRequest getEnqDtlbyDateRequest);
	ResponseAsList getEnqDtlbySlaveId(GetEnqDtlbySlaveIdRequest getEnqDtlbySlaveIdReq);
}

