package com.vmfg.timesheet.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.timesheet.request.DeleteActivityforEmpIdRequest;
import com.vmfg.timesheet.request.DeleteTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrRequest;
import com.vmfg.timesheet.request.GetEmpIDinTimesheetRequest;
import com.vmfg.timesheet.request.GetTimeDtlByHdrRequest;
import com.vmfg.timesheet.request.GetTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.GetTimeSheetDtlRequest;
import com.vmfg.timesheet.request.GetTimeSheetTaskForHdrandDtlRequset;
import com.vmfg.timesheet.request.InsertIndividualTimeSheetTaskDtl;
import com.vmfg.timesheet.request.InsertTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.InsertTimeSheetRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskRequest;
import com.vmfg.timesheet.request.TaskEntryHdrAndDtl;
import com.vmfg.timesheet.request.TimeSheetRemainingLogRequest;
import com.vmfg.timesheet.request.TimeSheetRequests;
import com.vmfg.timesheet.request.UpdateTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskHdrGroupName;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskRequest;

public interface ITimeSheetServices {

	ResponseAsList getEmpIDinTimesheet(GetEmpIDinTimesheetRequest getEmpIDinTimesheetRequest);

	ResponseAsList getTimeSheetDtl(GetTimeSheetDtlRequest getTimeSheetDtl);

	ResponseAsMessage insertTimeSheetDtl(InsertTimeSheetRequest insertTimeSheetDtl);

	ResponseAsList getTimeDtlByHdr(GetTimeDtlByHdrRequest getTimeDtlByHdr);

	ResponseAsMessage deleteActivityforEmpId(DeleteActivityforEmpIdRequest deletereq);

	ResponseAsMessage insertTimeSheetTask(InsertTimeSheetTaskRequest insertTimeSheetTask);

	ResponseAsMessage updateTimeSheetTask(UpdateTimeSheetTaskRequest updateTimeSheetTask);

	ResponseAsList getAllTimeSheetTaskForHdr(GetAllTimeSheetTaskForHdrRequest getTimeDtlByHdr);

	ResponseAsList getTimeSheetTaskForHdrandDtl(GetTimeSheetTaskForHdrandDtlRequset getTimeDtlByHdr);

	ResponseAsMessage insertTimeSheetCategory(InsertTimeSheetCategoryRequest insertTimeSheetCategory);

	ResponseAsMessage updateTimeSheetCategory(UpdateTimeSheetCategoryRequest updateTimeSheetCategory);

	ResponseAsList getTimeSheetCategory(GetTimeSheetCategoryRequest getTimeSheetCategory);

	ResponseAsList timeSheetReport(TimeSheetRequests timeSheetReport);

	ResponseAsList timeSheetMonthDtl(TimeSheetRequests timeSheetReport);

	ResponseAsList timeSheetDeptDtl(TimeSheetRequests timeSheetReport);

	ResponseAsMessage updateTimeSheetTaskHdrGroupName(UpdateTimeSheetTaskHdrGroupName updateTimeSheetTask);

	ResponseAsList taskEntryHdrAndDtl(TaskEntryHdrAndDtl timeSheetReport);

	ResponseAsMessage deleteTimeSheetTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq);

	ResponseAsMessage insertIndividualTimeSheetTaskDtl(InsertIndividualTimeSheetTaskDtl insertTimeSheet);

	ResponseAsList timeSheetReportCategory(TimeSheetRequests timeSheetReportCategory);

	ResponseAsMessage timelogremaining(TimeSheetRemainingLogRequest timelogReq);

}
