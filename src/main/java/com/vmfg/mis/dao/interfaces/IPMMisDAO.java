package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.response.PMWorkLoadResponse;
import com.vmfg.mis.response.ReportProjectMilestoneResponse;
import com.vmfg.mis.response.ReportProjectTrackerResponse;

public interface IPMMisDAO {

	List<ReportProjectTrackerResponse> getAllPMDtlByDate(String fromDate, String endDate, String tenantId,
			String sbcCode);

	List<ReportProjectTrackerResponse> getPMDtlByPmId(String tenantId, String pmHdrId, String sbcCode);

	List<ReportProjectTrackerResponse> getPMBySBCTypeAll(String fromDate, String endDate, String tenantId,String pmHdrId);

	List<ReportProjectTrackerResponse> getPMBySBCType(String fromDate, String endDate, String tenantId, String sbcCode,String pmHdrId);

	List<ReportProjectMilestoneResponse> getPMMilestoneByMonthYr(String month, String yr, String tenantId,String pmHdrId);

	int MileCountCheck(String department, String pmHdrId, String tenantId);

	String getMileStoneEndDate(String department, String pmHdrId, String tenantId);

	List<ReportProjectTrackerResponse> getPMBySBCTypeAllwithoutDate(String tenantId,String pmHdrId);

	List<ReportProjectTrackerResponse> getPMBySBCTypewithoutDate(String tenantId, String sbcCode,String pmHdrId);

	List<PMWorkLoadResponse> getPMWorkLoad(String tenantId,String pmHdrId);

}
