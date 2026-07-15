package com.vmfg.mis.services.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.dao.interfaces.IDepartmentAndEmployeeDAO;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.IDesignMisDAO;
import com.vmfg.mis.dao.interfaces.iReportSchedulerDAO;
import com.vmfg.mis.entity.DesignWidgetDtlListEntity;
import com.vmfg.mis.entity.GetPlannedProjectEntity;
import com.vmfg.mis.entity.GetTaskCompPerEntity;
import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;
import com.vmfg.mis.entity.getDesignWidgetDtlByCategoryEntity;
import com.vmfg.mis.request.DesignMisRequest;
import com.vmfg.mis.request.DesignReportMisRequest;
import com.vmfg.mis.response.DesignWidgetDtlResponse;
import com.vmfg.mis.services.interfaces.IDesignMisService;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.ProjectInitiationMstRequest;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.util.CommonMethod;

@Service
public class DesignMisService implements IDesignMisService {
	private static final Logger logger = LoggerFactory.getLogger(DesignMisService.class);

	@Autowired
	IDesignMisDAO iDesignMisDAO; 
	
	@Autowired
	UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	iReportSchedulerDAO ireportSchedulerDAO;
	
	@Autowired
	IDepartmentAndEmployeeDAO iDepartmentAndEmployeeDAO;
	
	@Override
	public ResponseAsList getDesignWidgetDtl(DesignMisRequest designMisReq) {
		List<DesignWidgetDtlResponse> mainList=new ArrayList<DesignWidgetDtlResponse>();
		DesignWidgetDtlResponse mainObj=new DesignWidgetDtlResponse();
		List<DesignWidgetDtlListEntity> list=new ArrayList<DesignWidgetDtlListEntity>();
		ResponseAsList returnList = new ResponseAsList();
		BigDecimal totalQty=BigDecimal.ZERO;
		String completedQty="";
		logger.debug("getDesignWidgetDtl method Start");
		try {
			ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedEmpId ="";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				
				assignedEmpId = "%%";
			}else {
				assignedEmpId = designMisReq.getEmpId(); 
			}
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			list=iDesignMisDAO.getDesignWidgetDtl(designMisReq.getDeptCode(),assignedEmpId,designMisReq.getTenantId(),month,year,designMisReq.getCategory(),designMisReq.getProjectId(),designMisReq.getLifespan());
			if (list.size() > 0) {
				for(DesignWidgetDtlListEntity obj:list ) {
					totalQty=totalQty.add(new BigDecimal(obj.getQty()));
					if(obj.getCompletedStatus().equalsIgnoreCase("1")) {
						completedQty=obj.getQty();
					}
				}
				mainObj.setTotalQty(String.valueOf(totalQty));
				mainObj.setCompletedQty(String.valueOf(completedQty));
				mainList.add(mainObj);
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
			
		}catch(Exception ex) {
			logger.error("getDesignWidgetDtl method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPlannedProject(DesignMisRequest designMisReq) {
		List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		ResponseAsList returnList = new ResponseAsList();
		logger.debug("getPlannedProject method Start");
		try {
			
			ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedEmpId = "";
			String pmHdrId ="";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				
				assignedEmpId = "%%";
			}else {
				assignedEmpId = designMisReq.getEmpId(); 
			}
			if(!designMisReq.getProjectId().equalsIgnoreCase("getall")) {
				pmHdrId =designMisReq.getProjectId();
			}else {
				pmHdrId = "%%";
			}
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			list = iDesignMisDAO.getProjList(designMisReq.getPmId(), pmHdrId, assignedEmpId,designMisReq.getTenantId());
			for(int q =0;q<list.size();q++) {
				String dapDate=iDesignMisDAO.getDapPlanDate(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
				String manualDate=iDesignMisDAO.getManualPlanDate(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
				String totalDrawing=iDesignMisDAO.getTotalDrawingCount(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
				String completedDrawing=iDesignMisDAO.getCompletionDrawingCount(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
					if(!dapDate.equalsIgnoreCase("0")) {
					//	list.get(q).setDapPlannedDate(dapDate.get(0).getDapPlannedDate());
						list.get(q).setDapPlannedDate(dapDate);
						int dapCompleteCheck = iDesignMisDAO.getcompletedDapCount(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
						if(dapCompleteCheck ==0) {
							String dapActDate=iDesignMisDAO.getDapCompleteDate(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
							if(!dapActDate.equalsIgnoreCase("0")) {
								list.get(q).setDapActualDate(dapActDate);
							}
						}else {
							list.get(q).setDapActualDate("0");
						}
					}
					if(!manualDate.equalsIgnoreCase("0")) {
						//list.get(q).setManualPlannedDate(manualDate.get(0).getDapPlannedDate());
						list.get(q).setManualPlannedDate(manualDate);
						int manualCompleteCheck = iDesignMisDAO.getcompletedManualCount(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
						if(manualCompleteCheck ==0) {
							String manualActDate=iDesignMisDAO.getManualCompleteDate(list.get(q).getProjectId(),year,month,designMisReq.getTenantId(),designMisReq.getLifespan());
							if(manualActDate.equalsIgnoreCase("0")) {
								list.get(q).setManualActualDate(manualActDate);
							}
						}else {
							list.get(q).setManualActualDate("0");
						}
					}
			//	list.get(q).setDapDate(dapDate.get(0).getDapDate());
			//	list.get(q).setManualDate(manualDate.get(0).getManualDate());
			//	list.get(q).setTotalDrawing(totalDrawing.get(0).getTotalDrawing());
			//	list.get(q).setCompletedDrawing(completedDrawing.get(0).getCompletedDrawing());
				list.get(q).setTotalDrawing(totalDrawing);
				list.get(q).setCompletedDrawing(completedDrawing);
			}
		//	list=iDesignMisDAO.getPlannedProject(designMisReq.getDeptCode(),designMisReq.getEmpId(),designMisReq.getTenantId(),month,year,designMisReq.getCategory());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch(Exception ex) {
			logger.error("getPlannedProject method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPlannedActivity(DesignMisRequest designMisReq) {
		List<GetTaskEntryDtlEntity> list=new ArrayList<GetTaskEntryDtlEntity>();
		ResponseAsList returnList = new ResponseAsList();
		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		logger.debug("getPlannedActivity method Start");
		try {
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedTo ="";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				assignedTo = "%%";
			}else {
				assignedTo = designMisReq.getEmpId();
			}
			
			  list = iDesignMisDAO.getPlannedActivity(year, month, assignedTo, designMisReq.getDeptCode(), designMisReq.getTenantId(),designMisReq.getLifespan());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch(Exception ex) {
			logger.error("getPlannedActivity method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getTaskCompPerBymonth(DesignMisRequest designMisReq) {
		List<GetTaskCompPerEntity> list=new ArrayList<GetTaskCompPerEntity>();
		ResponseAsList returnList = new ResponseAsList();
		logger.debug("getPlannedActivity method Start");
		try {
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			String pmHdrId="";
			ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedEmpId = "";
			if(!designMisReq.getProjectId().equalsIgnoreCase("getall")) {
				pmHdrId =designMisReq.getProjectId();
			}else {
				pmHdrId = "%%";
			}
			if(mstPocCheck.equalsIgnoreCase("1")) {
				
				assignedEmpId = "%%";
			}else {
				assignedEmpId = designMisReq.getEmpId(); 
			}
			
			  list = iDesignMisDAO.getTaskCompPer(assignedEmpId, designMisReq.getDeptCode(), designMisReq.getTenantId(), year,month,pmHdrId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch(Exception ex) {
			logger.error("getTaskCompPer method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDesignWidgetDtlByCategory(DesignMisRequest designMisReq) {
		List<getDesignWidgetDtlByCategoryEntity> mainList=new ArrayList<getDesignWidgetDtlByCategoryEntity>();
		ResponseAsList returnList = new ResponseAsList();
		logger.debug("getDesignWidgetDtlByCategory method Start");
		try {
			ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedEmpId ="";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				
				assignedEmpId = "%%";
			}else {
				assignedEmpId = designMisReq.getEmpId(); 
			}
			
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			mainList=iDesignMisDAO.getDesignWidgetDtlByCategory(designMisReq.getDeptCode(),assignedEmpId,designMisReq.getTenantId(),month,year,designMisReq.getCategory(),designMisReq.getProjectId());
			if(mainList.size()>0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
			
		}catch(Exception ex) {
			logger.error("getDesignWidgetDtl method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getTaskCompPerByYear(DesignReportMisRequest designMisReq) {
		List<GetTaskCompPerEntity> list=new ArrayList<GetTaskCompPerEntity>();
		ResponseAsList returnList = new ResponseAsList();
		logger.debug("getPlannedActivity method Start");
		try {
		//	String month=designMisReq.getMonthYear().split("-")[0];
	//		String year=designMisReq.getMonthYear().split("-")[1];
			String pmHdrId ="";
			
			ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
			projectInitiation.setEmpId(designMisReq.getEmpId());
			projectInitiation.setPmId(designMisReq.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,designMisReq.getTenantId());
			String assignedEmpId = "";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				
				assignedEmpId = "%%";
			}else {
				assignedEmpId = designMisReq.getEmpId(); 
			}
			if(!designMisReq.getProjectId().equalsIgnoreCase("getall")) {
				pmHdrId =designMisReq.getProjectId();
			}else {
				pmHdrId = "%%";
			}
			String reqMonth = designMisReq.getMonthYear();
			Date date1=new SimpleDateFormat("MM-yyyy").parse(reqMonth);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);
			cal.add(Calendar.MONTH, -6); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");  
			String strMonth = dateFormat.format(cal.getTime());  
			String month=designMisReq.getMonthYear().split("-")[0];
			String year=designMisReq.getMonthYear().split("-")[1];
			String toMonth = year +"-"+month;
			  list = iDesignMisDAO.getTaskCompPerByYear(assignedEmpId, designMisReq.getDeptCode(),strMonth,toMonth, designMisReq.getTenantId(),pmHdrId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch(Exception ex) {
			logger.error("getTaskCompPer method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updateReportTaskMonth() {

		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			String monthYear =CommonMethod.getCurrentMonthYear(); 
			String tenantAllId = ireportSchedulerDAO.getOrgTenant();
			String[] tenantArrId = tenantAllId.split(",");
			String tenantId;
			for(int q=0;q<tenantArrId.length;q++) {
				tenantId = tenantArrId[q];
			String tenatValue = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_STATUS");
			if (tenatValue.equalsIgnoreCase("OFF")) {

				String from_to_date[] = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_DATE_RANGE")
						.split(Pattern.quote("|"));

				String from_date = from_to_date[0];
				String to_date = from_to_date[1];
				List<String> dateList = new ArrayList<String>();
		//		String start_date = from_to_date[0];
			//	dateList.add(start_date);
		//		String sourceDate = "";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar startcalendar = Calendar.getInstance();
				startcalendar.setTime(sdf.parse(from_date));
				
				Calendar tocalendar = Calendar.getInstance();
				tocalendar.setTime(sdf.parse(to_date));
				
				Calendar tempDate = (Calendar) startcalendar.clone();
		        while (tempDate.before(tocalendar) || tempDate.equals(tocalendar)) {
		            int year = tempDate.get(Calendar.YEAR);
		            int month = tempDate.get(Calendar.MONTH) + 1;
		            String monthStr="";
		            if(month<=9) {
		            	monthStr ="0"+ month;
		            }else {
		            	monthStr = Integer.toString(month);
		            }
		    //        System.out.println(year + "-" + month);
		            dateList.add(year + "-" + monthStr);
		            tempDate.add(Calendar.MONTH, 1); // Move to the next month
		        }
//				do {
//					sourceDate = from_date;
//
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTime(sdf.parse(sourceDate));
//
//					calendar.add(Calendar.DATE, 1);
//					sourceDate = sdf.format(calendar.getTime());
//
//					dateList.add(sourceDate);
//					from_date = sourceDate;
//				} while (!sourceDate.equals(to_date));
				String[] dateRange = new String[dateList.size()];
				
				dateRange = dateList.toArray(dateRange);

				for (int i = 0; i < dateRange.length; i++) {
			//		FrmDate = ireportSchedulerDAO.getWeekStartedDate(dateRange[i]);
			//		ToDate = ireportSchedulerDAO.getPrevDate(dateRange[i]);
			//		this.reportTaskSch(FrmDate, ToDate, returnList);
			//		this.updateOldTask(ToDate);
					this.reportTaskSchForMonth(dateRange[i], returnList,tenantId);
					this.updateOldTaskForMonth(dateRange[i],CommonMethod.getPrevMonth(dateRange[i]),tenantId);
				}

			} else {
				this.reportTaskSchForMonth(monthYear, returnList,tenantId);
				this.updateOldTaskForMonth(monthYear,CommonMethod.getPrevMonth(monthYear),tenantId);
			}
			}
		} catch (Exception ex) {
			logger.error("updateReportTaskMonth service error " + ex);
		}
		return returnList;
	}

	public ResponseAsMessage reportTaskSchForMonth(String currMonth, ResponseAsMessage returnList,String tenantId) {
		List<ReportSchedulerEntity> list = new ArrayList<ReportSchedulerEntity>();
		String month=currMonth.split("-")[1];
		String year=currMonth.split("-")[0];
		list = iDesignMisDAO.getReportTaskSchForMonth(month, year,tenantId);
		if (list.size() > 0) {
			for (ReportSchedulerEntity entity : list) {
				int Cnt = iDesignMisDAO.getReportTaskCount(entity.getEmpId(), entity.getProjId(),
						 entity.getDeptCode(),currMonth);
				if (Cnt == 0) {
					int InsertRec = iDesignMisDAO.insertReportTask(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), entity.getYear(), entity.getMonth(), currMonth,
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),
							entity.getTenantID());
					if (InsertRec > 0) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				} else {
					int UpdateRec = iDesignMisDAO.updateReportTask(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), currMonth,
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),entity.getTenantID());
					if (UpdateRec == 1) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				}
			}
		}
		return returnList;
		}
	
	
	public void updateOldTaskForMonth(String monthYear ,String prevMonthYear,String tenantId) {
		String month=monthYear.split("-")[1];
		String year=monthYear.split("-")[0];
		
	//	String prevMonth=prevMonthYear.split("-")[1];
	//	String prevYear=prevMonthYear.split("-")[0];
	
		List<TaskDtlEntity> getPlannedLi = iDesignMisDAO.getOldTaskMonthPlanned(monthYear,tenantId);
		List<TaskDtlEntity> getCompLi = iDesignMisDAO.getOldTaskMonthCompleted(year,month,monthYear,tenantId);

		for (int h = 0; h < getPlannedLi.size(); h++) {
			iDesignMisDAO.updatePlannedTaskMonth(getPlannedLi.get(h), monthYear,tenantId);
		}

		for (int h = 0; h < getCompLi.size(); h++) {
			iDesignMisDAO.updateCompTaskMonth(getCompLi.get(h), monthYear,tenantId);
		}

		iDesignMisDAO.taskPercentUpdateMonth(monthYear);

	}

	@Override
	public ResponseAsMessage updateReportTaskWeek() {
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			String endDate =CommonMethod.getCurrentDate(); 
			String startDate = ireportSchedulerDAO.getWeekStartedDate(endDate);
			String tenantAllId = ireportSchedulerDAO.getOrgTenant();
			String[] tenantArrId = tenantAllId.split(",");
			String tenantId;
			for(int q=0;q<tenantArrId.length;q++) {
				tenantId = tenantArrId[q];
			String tenatValue = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_STATUS");
			if (tenatValue.equalsIgnoreCase("OFF")) {

				String from_to_date[] = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_DATE_RANGE")
						.split(Pattern.quote("|"));

				String from_date = from_to_date[0];
				String to_date = from_to_date[1];
				List<String> dateList = new ArrayList<String>();
		//		String start_date = from_to_date[0];
		//		dateList.add(start_date);
		//		String sourceDate = "";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar startcalendar = Calendar.getInstance();
				startcalendar.setTime(sdf.parse(from_date));
				
				Calendar tocalendar = Calendar.getInstance();
				tocalendar.setTime(sdf.parse(to_date));
				   Calendar tempDate = (Calendar) startcalendar.clone();

			        // Find the first day of the current week
			        tempDate.set(Calendar.DAY_OF_WEEK, tempDate.getFirstDayOfWeek());

			        // Iterate through each week
			        while (tempDate.before(tocalendar) || tempDate.equals(tocalendar)) {
			            Calendar weekStartDate = (Calendar) tempDate.clone();
			            Calendar weekEndDate = (Calendar) tempDate.clone();

			            // Set the end of the week (add 6 days)
			            weekEndDate.add(Calendar.DAY_OF_MONTH, 6);
			            String startWeekDate = sdf.format(weekStartDate.getTime());
			            String endWeekDate = sdf.format(weekEndDate.getTime());
			            // Print or process the start and end dates of the current week
			       //     System.out.println("Week start: " + formatDate(weekStartDate));
			         //   System.out.println("Week end: " + formatDate(weekEndDate));

			            // Move to the start of the next week
			            dateList.add(startWeekDate + "|" + endWeekDate);
			            tempDate.add(Calendar.DAY_OF_MONTH, 7);
			        }
			//	do {
			//		sourceDate = from_date;

//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTime(sdf.parse(sourceDate));
//
//					calendar.add(Calendar.DATE, 1);
//					sourceDate = sdf.format(calendar.getTime());

				//	dateList.add(sourceDate);
				//	from_date = sourceDate;
			//	} while (!sourceDate.equals(to_date));
				String[] dateRange = new String[dateList.size()];
				dateRange = dateList.toArray(dateRange);

				for (int i = 0; i < dateRange.length; i++) {
			//		FrmDate = ireportSchedulerDAO.getWeekStartedDate(dateRange[i]);
			//		ToDate = ireportSchedulerDAO.getPrevDate(dateRange[i]);
			//		this.reportTaskSch(FrmDate, ToDate, returnList);
			//		this.updateOldTask(ToDate);
					String arrDate[] = dateRange[i].split(Pattern.quote("|"));
					this.reportTaskSchForWeek(arrDate[0],arrDate[1], returnList,tenantId);
					this.updateOldTaskForWeek(arrDate[0],arrDate[1],tenantId ); 
				}

			} else {
				this.reportTaskSchForWeek(startDate,endDate, returnList,tenantId);
				this.updateOldTaskForWeek(startDate,endDate,tenantId );
			//	this.updateOldTaskForMonth(monthYear,CommonMethod.getPrevMonth(monthYear));
			}
			}
		} catch (Exception ex) {
			logger.error("updateReportTaskWeek service error " + ex);
		}
		return returnList;
	}
	
	

	public ResponseAsMessage reportTaskSchForWeek(String startDate,String endDate, ResponseAsMessage returnList,String tenantId) {
		List<ReportSchedulerEntity> list = new ArrayList<ReportSchedulerEntity>();
		list = iDesignMisDAO.getReportTaskSchForWeek(startDate, endDate,tenantId);
		if (list.size() > 0) {
			for (ReportSchedulerEntity entity : list) {
				int Cnt = iDesignMisDAO.getReportTaskCountWeek(entity.getEmpId(), entity.getProjId(),
						 entity.getDeptCode(),startDate,tenantId);
				if (Cnt == 0) {
					int InsertRec = iDesignMisDAO.insertReportTaskWeek(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), entity.getYear(), entity.getMonth(), startDate,
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),
							entity.getTenantID());
					if (InsertRec > 0) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				} else {
					int UpdateRec = iDesignMisDAO.updateReportTaskWeek(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), startDate,
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),entity.getTenantID());
					if (UpdateRec == 1) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				}
			}
		}
		return returnList;
		}
	
	public void updateOldTaskForWeek(String startDate ,String endDate,String tenantId) {
		
		List<TaskDtlEntity> getPlannedLi = iDesignMisDAO.getOldTaskWeekPlanned(startDate,tenantId);
		List<TaskDtlEntity> getCompLi = iDesignMisDAO.getOldTaskWeekCompleted(startDate,endDate,tenantId);

		for (int h = 0; h < getPlannedLi.size(); h++) {
			iDesignMisDAO.updatePlannedTaskWeek(getPlannedLi.get(h), startDate,tenantId);
		}

		for (int h = 0; h < getCompLi.size(); h++) {
			iDesignMisDAO.updateCompTaskWeek(getCompLi.get(h), startDate,tenantId);
		}

		iDesignMisDAO.taskPercentUpdateWeek(startDate,tenantId);

	}

	@Override
	public ResponseAsMessage getTeamMemberEnableCheck(DesignReportMisRequest designMisReq) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			String desig = uploadManagementDAO.getDesigCodeByEmpId(designMisReq.getEmpId(),
					designMisReq.getTenantId());
		String	assignTeam = iDepartmentAndEmployeeDAO.getPrimaryDocFlagVal(desig, designMisReq.getPmId(),designMisReq.getTenantId());
			
		returnList.setResponseDataMessage(assignTeam);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
			
		}catch(Exception ex) {
			logger.error("getTeamMemberEnableCheck method  exception" + ex);
		}
		return returnList;
	}
	
}
