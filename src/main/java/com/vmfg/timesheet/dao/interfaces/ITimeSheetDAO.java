package com.vmfg.timesheet.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.timesheet.request.DeleteActivityforEmpIdRequest;
import com.vmfg.timesheet.request.DeleteTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrEntity;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrRequest;
import com.vmfg.timesheet.request.GetTimeDtlByHdrRequest;
import com.vmfg.timesheet.request.GetTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.GetTimeSheetTaskForHdrandDtlRequset;
import com.vmfg.timesheet.request.InsertIndividualTimeSheetTaskDtl;
import com.vmfg.timesheet.request.InsertTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.InsertTimeSheetDtlRequest;
import com.vmfg.timesheet.request.InsertTimeSheetRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskRequest;
import com.vmfg.timesheet.request.TaskEntryHdrAndDtl;
import com.vmfg.timesheet.request.TimeSheetRemainingLogRequest;
import com.vmfg.timesheet.request.TimeSheetRequests;
import com.vmfg.timesheet.request.UpdateTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskHdrGroupName;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskRequest;
import com.vmfg.timesheet.response.GetEmpIDinTimesheetResponse;
import com.vmfg.timesheet.response.GetTimeDtlByHdrEntity;
import com.vmfg.timesheet.response.GetTimeSheetCategoryEntity;
import com.vmfg.timesheet.response.GetTimeSheetDtlEntity;
import com.vmfg.timesheet.response.GetTimeSheetTaskForHdrandDtlEntity;
import com.vmfg.timesheet.response.TaskEntryHdrAndDtlEntity;
import com.vmfg.timesheet.response.TimeSheetDeptDtlEntity;
import com.vmfg.timesheet.response.TimeSheetMonthDtlEntity;
import com.vmfg.timesheet.response.TimeSheetRequestsEntity;

public interface ITimeSheetDAO {

	String timesheetProjectInitiationPoc(String empId, String tenantId);

	List<GetEmpIDinTimesheetResponse> getEmpIDinTimesheet(String empId, String pmHdrId, String tenantId,
			String deptAssigned);

	List<GetTimeSheetDtlEntity> getTimeSheetDtl(String assignedEmpId, String pmHdrId, String tenantId, String fromdate,
			String todate, String deptAssigned);

	int insertTimeSheetHdr(InsertTimeSheetRequest insertTimeSheetDtl);

	int insertTimeSheetDtl(int responseHdrId, InsertTimeSheetDtlRequest dtlObj, BigDecimal appSal,String reqDate,String empId);

	List<GetTimeDtlByHdrEntity> getTimeDtlByHdr(GetTimeDtlByHdrRequest getTimeDtlByHdr);

	int deleteActivityforEmpId(String thdrId, DeleteActivityforEmpIdRequest deletereq);

	String gettHdrId(DeleteActivityforEmpIdRequest deletereq);

	int getCount(String thdrId);

	int insertTimeSheetTaskHdr(InsertTimeSheetTaskRequest insertTimeSheetTask);

	int insertTimeSheetTaskDtl(int responseHdrId, InsertTimeSheetTaskDtlRequest dtlObj);

	int updateTimeSheetTask(UpdateTimeSheetTaskRequest updateTimeSheetTask);

	List<GetAllTimeSheetTaskForHdrEntity> getAllTimeSheetTaskForHdr(GetAllTimeSheetTaskForHdrRequest getTimeDtlByHdr);

	List<GetTimeSheetTaskForHdrandDtlEntity> getTimeSheetTaskForHdrandDtl(
			GetTimeSheetTaskForHdrandDtlRequset getTimeDtlByHdr);

	int insertTimeSheetCategory(InsertTimeSheetCategoryRequest insertTimeSheetCategory);

	int updateTimeSheetCategory(UpdateTimeSheetCategoryRequest updateTimeSheetCategory);

	List<GetTimeSheetCategoryEntity> getTimeSheetCategory(GetTimeSheetCategoryRequest getTimeSheetCategory);

	List<TimeSheetRequestsEntity> timeSheetReport(TimeSheetRequests timeSheetReport);

	List<TimeSheetMonthDtlEntity> timeSheetMonthDtl(TimeSheetRequests timeSheetReport);

	List<TimeSheetDeptDtlEntity> timeSheetDeptDtl(TimeSheetRequests timeSheetReport);

	int updateTimeSheetTaskHdrGroupName(UpdateTimeSheetTaskHdrGroupName updateTimeSheetTask);

	List<TaskEntryHdrAndDtlEntity> taskEntryHdrAndDtl(TaskEntryHdrAndDtl timeSheetReport);

	int deleteTimeSheetTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq);

	int insertIndividualTimeSheetTaskDtl(InsertIndividualTimeSheetTaskDtl insertTimeSheet);

	String getEmployeDeptAssigned(String empDepCode, String tenantId);

	int CheckTDIdTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq);

	int deleteDtlActivityforEmpId(DeleteActivityforEmpIdRequest deletereq);

	BigDecimal getSalaryFromId(String empId);

	String getPropValueByTenant(String tenantId, String propertyName);

	List<TimeSheetRequestsEntity> timeSheetReportCategory(String fromDate, String toDate, String pmHdrId,
			String tenantId, String category);

	String timelogremaining(TimeSheetRemainingLogRequest timelogReq);

}
