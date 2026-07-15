package com.vmfg.timesheet.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.quality.dao.interfaces.IQualityInspectionDAO;
import com.vmfg.timesheet.dao.interfaces.ITimeSheetDAO;
import com.vmfg.timesheet.request.DeleteActivityforEmpIdRequest;
import com.vmfg.timesheet.request.DeleteTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrEntity;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrRequest;
import com.vmfg.timesheet.request.GetEmpIDinTimesheetRequest;
import com.vmfg.timesheet.request.GetTimeDtlByHdrRequest;
import com.vmfg.timesheet.request.GetTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.GetTimeSheetDtlRequest;
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
import com.vmfg.timesheet.services.interfaces.ITimeSheetServices;

@Service
public class TimeSheetServices implements ITimeSheetServices {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetServices.class);
	@Autowired
	IQualityInspectionDAO iQualityInspectionDAO;
	@Autowired
	private ITimeSheetDAO iTimeSheetDAO;

	@Override
	public ResponseAsList getEmpIDinTimesheet(GetEmpIDinTimesheetRequest getEmpIDinTimesheetRequest) {
		ResponseAsList list = new ResponseAsList();
		List<GetEmpIDinTimesheetResponse> timeSheetList = new ArrayList<>();
		try {
			String deptAssigned = "";
			String empId = getEmpIDinTimesheetRequest.getEmpId();
			String tenantId = getEmpIDinTimesheetRequest.getTenantId();
			String assignedEmpId = "";
			String pmHdrId = "";

			if (!getEmpIDinTimesheetRequest.getPmHdrId().equalsIgnoreCase("")) {
				pmHdrId = getEmpIDinTimesheetRequest.getPmHdrId();
			} else {
				pmHdrId = "%%";
			}

			String mstPocCheck = iTimeSheetDAO.timesheetProjectInitiationPoc(empId, tenantId);
			if (mstPocCheck.equalsIgnoreCase("1")) {
				String empDepCode = iQualityInspectionDAO.getEmpDepCode(empId,tenantId);
				deptAssigned = iTimeSheetDAO.getEmployeDeptAssigned(empDepCode, tenantId);

			} else {
				assignedEmpId = empId;
			}

			timeSheetList = iTimeSheetDAO.getEmpIDinTimesheet(assignedEmpId, pmHdrId, tenantId, deptAssigned);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getEmpIDinTimesheet Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTimeSheetDtl(GetTimeSheetDtlRequest getTimeSheetDtl) {
		ResponseAsList list = new ResponseAsList();
		List<GetTimeSheetDtlEntity> timeSheetList = new ArrayList<>();
		try {
			String deptAssigned = "";
			String logginEmpId = getTimeSheetDtl.getLogginEmpId();
			String tenantId = getTimeSheetDtl.getTenantId();
			String assignedEmpId = "";
			String pmHdrId = "";

			if (null != getTimeSheetDtl.getPmHdrId() && !getTimeSheetDtl.getPmHdrId().equalsIgnoreCase("")) {
				pmHdrId = getTimeSheetDtl.getPmHdrId();
			} else {
				pmHdrId = "%%";
			}

			if (getTimeSheetDtl.getUnLogginEmpId().equalsIgnoreCase("")) {
				String mstPocCheck = iTimeSheetDAO.timesheetProjectInitiationPoc(logginEmpId, tenantId);
				if (mstPocCheck.equalsIgnoreCase("1")) {
					String empDepCode = iQualityInspectionDAO.getEmpDepCode(logginEmpId,tenantId);
					deptAssigned = iTimeSheetDAO.getEmployeDeptAssigned(empDepCode, tenantId);

				} else {
					assignedEmpId = logginEmpId;
				}
			} else {
				assignedEmpId = getTimeSheetDtl.getUnLogginEmpId();
			}

			timeSheetList = iTimeSheetDAO.getTimeSheetDtl(assignedEmpId, pmHdrId, tenantId,
					getTimeSheetDtl.getFromDate(), getTimeSheetDtl.getToDate(), deptAssigned);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getTimeSheetDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertTimeSheetDtl(InsertTimeSheetRequest insertTimeSheetDtl) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertTimeSheetDtl Service start ");
		int responseHdrId = 0, responseDtlId = 0;
		try {
			String teHdrId = "";
			String tdId = "";

			String teDtlId = "";
			String tdDtlId = "";
			// teHdrId
			if (null != insertTimeSheetDtl.getTeHdrId() && !insertTimeSheetDtl.getTeHdrId().isEmpty()) {
				teHdrId = insertTimeSheetDtl.getTeHdrId();
				insertTimeSheetDtl.setTeHdrId(teHdrId);
			} else {
				teHdrId = "0";
				insertTimeSheetDtl.setTeHdrId(teHdrId);
			}
			// tdId
			if (null != insertTimeSheetDtl.getTdId() && !insertTimeSheetDtl.getTdId().isEmpty()) {
				tdId = insertTimeSheetDtl.getTdId();
				insertTimeSheetDtl.setTdId(tdId);
			} else {
				tdId = "0";
				insertTimeSheetDtl.setTdId(tdId);
			}
			responseHdrId = iTimeSheetDAO.insertTimeSheetHdr(insertTimeSheetDtl);

			if (responseHdrId > 0) {
				for (InsertTimeSheetDtlRequest dtlObj : insertTimeSheetDtl.getTimeSheetDtl()) {
					if (null != dtlObj.getTeDtlId() && !dtlObj.getTeDtlId().isEmpty()) {
						teDtlId = dtlObj.getTeDtlId();
						dtlObj.setTeDtlId(teDtlId);
					} else {
						teDtlId = "0";
						dtlObj.setTeDtlId(teDtlId);
					}

					if (null != dtlObj.getTdDtlId() && !dtlObj.getTdDtlId().isEmpty()) {
						tdDtlId = dtlObj.getTdDtlId();
						dtlObj.setTdDtlId(tdDtlId);
					} else {
						tdDtlId = "0";
						dtlObj.setTdDtlId(tdDtlId);
					}
					
					BigDecimal appSal = iTimeSheetDAO.getSalaryFromId(insertTimeSheetDtl.getEmpId());
					responseDtlId = iTimeSheetDAO.insertTimeSheetDtl(responseHdrId, dtlObj,appSal,insertTimeSheetDtl.getRecordDate(),insertTimeSheetDtl.getEmpId());

				}

			}
			if (responseHdrId > 0 && responseDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertTimeSheetDtl Service end ");
		} catch (Exception ex) {
			logger.error("insertTimeSheetDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList getTimeDtlByHdr(GetTimeDtlByHdrRequest getTimeDtlByHdr) {
		ResponseAsList list = new ResponseAsList();
		List<GetTimeDtlByHdrEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.getTimeDtlByHdr(getTimeDtlByHdr);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getTimeDtlByHdr Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage deleteActivityforEmpId(DeleteActivityforEmpIdRequest deletereq) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("deleteActivityforEmpId Service start ");
		int response = 0;
		try {
			String thdrId = iTimeSheetDAO.gettHdrId(deletereq);

			int count = iTimeSheetDAO.getCount(thdrId);
			if (count == 1) {
				response = iTimeSheetDAO.deleteActivityforEmpId(thdrId, deletereq);
			}else {
				response = iTimeSheetDAO.deleteDtlActivityforEmpId(deletereq);
			}

			if (response > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
			}
			logger.info("deleteActivityforEmpId Service end ");
		} catch (Exception ex) {
			logger.error("deleteActivityforEmpId error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage insertTimeSheetTask(InsertTimeSheetTaskRequest insertTimeSheetTask) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertTimeSheetTask Service start ");
		int responseHdrId = 0, responseDtlId = 0;
		try {

			responseHdrId = iTimeSheetDAO.insertTimeSheetTaskHdr(insertTimeSheetTask);

			if (responseHdrId > 0) {
				for (InsertTimeSheetTaskDtlRequest dtlObj : insertTimeSheetTask.getTimeSheetDtl()) {

					responseDtlId = iTimeSheetDAO.insertTimeSheetTaskDtl(responseHdrId, dtlObj);

				}

			}
			if (responseHdrId > 0 && responseDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertTimeSheetTask Service end ");
		} catch (Exception ex) {
			logger.error("insertTimeSheetTask error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage updateTimeSheetTask(UpdateTimeSheetTaskRequest updateTimeSheetTask) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("updateTimeSheetTask Service start ");
		int responseHdrId = 0;
		try {

			responseHdrId = iTimeSheetDAO.updateTimeSheetTask(updateTimeSheetTask);

			if (responseHdrId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			logger.info("updateTimeSheetTask Service end ");
		} catch (Exception ex) {
			logger.error("updateTimeSheetTask error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList getAllTimeSheetTaskForHdr(GetAllTimeSheetTaskForHdrRequest getTimeDtlByHdr) {
		ResponseAsList list = new ResponseAsList();
		List<GetAllTimeSheetTaskForHdrEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.getAllTimeSheetTaskForHdr(getTimeDtlByHdr);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getAllTimeSheetTaskForHdr Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTimeSheetTaskForHdrandDtl(GetTimeSheetTaskForHdrandDtlRequset getTimeDtlByHdr) {
		ResponseAsList list = new ResponseAsList();
		List<GetTimeSheetTaskForHdrandDtlEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.getTimeSheetTaskForHdrandDtl(getTimeDtlByHdr);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getAllTimeSheetTaskForHdr Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertTimeSheetCategory(InsertTimeSheetCategoryRequest insertTimeSheetCategory) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertTimeSheetCategory Service start ");
		int responseId = 0;
		try {

			responseId = iTimeSheetDAO.insertTimeSheetCategory(insertTimeSheetCategory);

			if (responseId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertTimeSheetCategory Service end ");
		} catch (Exception ex) {
			logger.error("insertTimeSheetCategory error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage updateTimeSheetCategory(UpdateTimeSheetCategoryRequest updateTimeSheetCategory) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("updateTimeSheetCategory Service start ");
		int responseHdrId = 0;
		try {

			responseHdrId = iTimeSheetDAO.updateTimeSheetCategory(updateTimeSheetCategory);

			if (responseHdrId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			logger.info("updateTimeSheetCategory Service end ");
		} catch (Exception ex) {
			logger.error("updateTimeSheetCategory error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList getTimeSheetCategory(GetTimeSheetCategoryRequest getTimeSheetCategory) {
		ResponseAsList list = new ResponseAsList();
		List<GetTimeSheetCategoryEntity> timeSheetList = new ArrayList<>();
		String tcId = "";
		try {
			if (getTimeSheetCategory.getTcId().isEmpty()) {
				tcId = "%%";
				getTimeSheetCategory.setTcId(tcId);
			} else {
				tcId = getTimeSheetCategory.getTcId();
				getTimeSheetCategory.setTcId(tcId);
			}

			timeSheetList = iTimeSheetDAO.getTimeSheetCategory(getTimeSheetCategory);

			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("getTimeSheetCategory Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList timeSheetReport(TimeSheetRequests timeSheetReport) {
		ResponseAsList list = new ResponseAsList();
		List<TimeSheetRequestsEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.timeSheetReport(timeSheetReport);

			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("timeSheetReport Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList timeSheetMonthDtl(TimeSheetRequests timeSheetReport) {
		ResponseAsList list = new ResponseAsList();
		List<TimeSheetMonthDtlEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.timeSheetMonthDtl(timeSheetReport);

			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("timeSheetMonthDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList timeSheetDeptDtl(TimeSheetRequests timeSheetReport) {
		ResponseAsList list = new ResponseAsList();
		List<TimeSheetDeptDtlEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.timeSheetDeptDtl(timeSheetReport);

			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("timeSheetDeptDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage updateTimeSheetTaskHdrGroupName(UpdateTimeSheetTaskHdrGroupName updateTimeSheetTask) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("updateTimeSheetTaskHdrGroupName Service start ");
		int responseHdrId = 0;
		try {

			responseHdrId = iTimeSheetDAO.updateTimeSheetTaskHdrGroupName(updateTimeSheetTask);

			if (responseHdrId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			logger.info("updateTimeSheetTaskHdrGroupName Service end ");
		} catch (Exception ex) {
			logger.error("updateTimeSheetTaskHdrGroupName error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList taskEntryHdrAndDtl(TaskEntryHdrAndDtl timeSheetReport) {
		ResponseAsList list = new ResponseAsList();
		List<TaskEntryHdrAndDtlEntity> timeSheetList = new ArrayList<>();
		try {

			timeSheetList = iTimeSheetDAO.taskEntryHdrAndDtl(timeSheetReport);
			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("taskEntryHdrAndDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage deleteTimeSheetTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("deleteTimeSheetTaskDtl Service start ");
		int response = 0;
		int RemoveTdId = 0;
		try {

			response = iTimeSheetDAO.deleteTimeSheetTaskDtl(deletereq);
			RemoveTdId = iTimeSheetDAO.CheckTDIdTaskDtl(deletereq);
			if (response > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else if (response > 0 && RemoveTdId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
			}
			logger.info("deleteTimeSheetTaskDtl Service end ");
		} catch (Exception ex) {
			logger.error("deleteTimeSheetTaskDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage insertIndividualTimeSheetTaskDtl(InsertIndividualTimeSheetTaskDtl insertTimeSheet) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertIndividualTimeSheetTaskDtl Service start ");
		int responseDtlId = 0;
		try {

			responseDtlId = iTimeSheetDAO.insertIndividualTimeSheetTaskDtl(insertTimeSheet);

			if (responseDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertIndividualTimeSheetTaskDtl Service end ");
		} catch (Exception ex) {
			logger.error("insertIndividualTimeSheetTaskDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList timeSheetReportCategory(TimeSheetRequests timeSheetReportCategory) {
		ResponseAsList list = new ResponseAsList();
		List<TimeSheetRequestsEntity> timeSheetList = new ArrayList<>();
		try {
            String category = iTimeSheetDAO.getPropValueByTenant(timeSheetReportCategory.getTenantId(),"TIMESHEET_CATEOGRY");
			timeSheetList = iTimeSheetDAO.timeSheetReportCategory(timeSheetReportCategory.getFromDate(), timeSheetReportCategory.getToDate(),
					timeSheetReportCategory.getPmHdrId(),timeSheetReportCategory.getTenantId(), category);

			if (timeSheetList.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(timeSheetList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(timeSheetList);
			}
		} catch (Exception ex) {
			logger.error("timeSheetReport Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage timelogremaining(TimeSheetRemainingLogRequest timelogReq) {
		ResponseAsMessage remaintimelog = new ResponseAsMessage() ;
		String remain = "";
		try {
			remain = iTimeSheetDAO.timelogremaining(timelogReq);
			if(!remain.isEmpty()) {
				remaintimelog.setResponseCode(ResponseMessageMap.responseCodeOk);
				remaintimelog.setResponseMessage(ResponseMessageMap.successCreated);
				remaintimelog.setResponseDataMessage(remain);
			}
			else {
				remaintimelog.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				remaintimelog.setResponseMessage(ResponseMessageMap.failToCreateMsg);
				remaintimelog.setResponseDataMessage("");
			}
		}catch (Exception e) {
			logger.error("timelogremaining service method error"+e);
		}
		return remaintimelog;
	}

}
