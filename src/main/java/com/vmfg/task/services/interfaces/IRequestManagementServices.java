package com.vmfg.task.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.task.entity.GetAllCountByEmpIdEntity;
import com.vmfg.task.request.GetAllCountByEmpId;
import com.vmfg.task.request.GetReqManHdrDtlRequest;
import com.vmfg.task.request.GetRequestCategoryRequest;
import com.vmfg.task.request.GetStatusRemarksDtlRequest;
import com.vmfg.task.request.InsertRMRemarksRequest;
import com.vmfg.task.request.ReqManagementHdr;
import com.vmfg.task.request.UpdateReqStatusRequest;

public interface IRequestManagementServices {

	ResponseAsMessage insertReqManagementHdr(ReqManagementHdr reqManagementHdr);

	ResponseAsList getStatusRemarksDtl(GetStatusRemarksDtlRequest getStatusRemarks);

	ResponseAsList getReqManHdrDtl(GetReqManHdrDtlRequest getReqManHdr);

	ResponseAsMessage insertRMRemarks(InsertRMRemarksRequest insertRMRemarks);

	ResponseAsMessage insertReqStatus(UpdateReqStatusRequest updateReqStatus);

	ResponseAsList getRequestCategory(GetRequestCategoryRequest requestCategory);

	ResponseAsList getReqManHdrAndStatusAndRemarks(GetStatusRemarksDtlRequest getStatusRemarksDtlRequest);

	GetAllCountByEmpIdEntity getAllCounts(GetAllCountByEmpId getAllCountByEmpId);

	ResponseAsList getRequestedToDtl(GetAllCountByEmpId getAllByEmpId);

	ResponseAsList getRequestedByDtl(GetAllCountByEmpId getAllByEmpId);

	ResponseAsList getRequestedByDtlWithIsComplete(GetAllCountByEmpId getAllByEmpId);

	ResponseAsList getRequestToWithAllDepartment(GetAllCountByEmpId getAllByEmpId);

}
