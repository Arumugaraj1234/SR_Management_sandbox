package com.vmfg.export.services.interfaces;

import java.util.List;

import com.vmfg.assembly.request.MaterialIssueRequest;
import com.vmfg.export.request.DcRequestHdrRequest;
import com.vmfg.export.request.DeliveryChallanRequest;
import com.vmfg.export.request.DtlIdandTenantIdRequest;
import com.vmfg.export.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDeliveryChallanReportService {

	ResponseAsList getDeliveryChallanReportPdf(DeliveryChallanRequest deliveryReq);

	ResponseAsMessage getinsertqcreq(DcRequestHdrRequest deliveryReq);

	ResponseAsList getdcreqhdrdtldtl(MaterialIssueRequest deliveryReq);

	ResponseAsMessage updateDcqty(List<DtlIdandTenantIdRequest> deliveryReq);

	
}
