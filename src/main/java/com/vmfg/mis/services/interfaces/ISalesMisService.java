package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.mis.request.MisFromDateToDateRequest;
//import com.vmfg.scm.entity.ProjectHdrDtlEntity;

public interface ISalesMisService {

	ResponseAsList getSalesOrderDetails(MisFromDateToDateRequest misFromDateToDateReq);
	
	ResponseAsList getSalesStageDtl(MisFromDateToDateRequest misFromDateToDateReq);
	
	ResponseAsList getCustomerOrderDtl(MisFromDateToDateRequest misFromDateToDateReq);
	
	ResponseAsList getSalesConvRatio(MisFromDateToDateRequest misFromDateToDateReq);
	
	ResponseAsList getSalesContDtl(MisFromDateToDateRequest misFromDateToDateReq);

	ResponseAsList getSalesContProjects(MisFromDateToDateRequest misFromDateToDateRequest);
	
    ResponseAsList getSalesDeptEmployees(MisFromDateToDateRequest misFromDateToDateRequest);
	
}
