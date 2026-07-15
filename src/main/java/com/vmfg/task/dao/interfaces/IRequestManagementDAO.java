package com.vmfg.task.dao.interfaces;

import java.util.List;

import com.vmfg.task.entity.GetAllCountByEmpIdEntity;
import com.vmfg.task.entity.GetRemarksDtlEntity;
import com.vmfg.task.entity.GetReqManHdrDtlEntity;
import com.vmfg.task.entity.GetRequestCategoryEntity;
import com.vmfg.task.entity.GetStatusRemarksDtlEntity;

public interface IRequestManagementDAO {

	int insertReqRemarks(int insertHdr, String remarksBy, String remarks, String tenantId);

	int insertReqManagementHdr(String pmHdrId, String remarks, String reqCategory, String reqClosedDate, String reqDesc,
			String reqName, String requestedBy, String requestedByDept, String requestedTo, String requestedToDept,
			String ticketReporter, String tenantId, String seqNo, String seqStatus, String dueDate);

	int insertReqManStatusDtl(int insertHdr, String seqNo, String seqStatus, String empId, String remarks,
			String tenantId);

	List<GetStatusRemarksDtlEntity> getStatusRemarksDtl(String rqId, String tenantId);

	int insertReqStatus(String empId, String rqId, String seqNo, String seqStatus, String statusRemarks,
			String tenantId);

	List<GetRequestCategoryEntity> getRequestCategory(String tenantId);

	List<GetReqManHdrDtlEntity> getReqManHdrDtl(String pmHdrId, String tenantId);

	List<GetReqManHdrDtlEntity> getReqManHdrAndStatusAndRemarks(String rqId, String tenantId);

	List<GetRemarksDtlEntity> getRemarksDtl(String rqId, String tenantId);

	void updateClosedDateInHdr(String rqId, String closedDate, int isComplete, int isApproved);

	GetAllCountByEmpIdEntity getAllCounts(String empId, String tenantId);

	List<GetReqManHdrDtlEntity> getRequestedToDtl(String empId, String tenantId);

	List<GetReqManHdrDtlEntity> getRequestedByDtl(String empId, String tenantId);

	List<GetReqManHdrDtlEntity> getRequestedByDtlWithIsComplete(String empId, String tenantId);

	List<GetReqManHdrDtlEntity> getRequestToWithAllDepartment(String deptCode, String tenantId);

	String getdepartCode(String empId, String tenantId);

	List<GetReqManHdrDtlEntity> getRequestAssignedTo(String empId, String tenantId, String dept, String fromDate,
			String toDate);

	List<GetReqManHdrDtlEntity> getRequestAssignedFrom(String empId, String tenantId, String dept, String fromDate,
			String toDate);

}
