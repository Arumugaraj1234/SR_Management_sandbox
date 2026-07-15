package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.entity.DesignWidgetDtlListEntity;
import com.vmfg.mis.entity.GetPlannedProjectEntity;
import com.vmfg.mis.entity.GetTaskCompPerEntity;
import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;
import com.vmfg.mis.entity.getDesignWidgetDtlByCategoryEntity;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;

public interface IDesignMisDAO {

	List<DesignWidgetDtlListEntity> getDesignWidgetDtl(String deptCode, String empId, String tenantId, String month,
			String year, String category,String projectId, String lifespan);

	String getMstPocByDeptCode(String departCode, String tenantId);
	
	List<GetTaskEntryDtlEntity>getPlannedActivity(String year,String month,String assigned,String department,String tenantId, String lifespan);

	List<GetPlannedProjectEntity> getProjList(String pmId,String pmHdrId,String empId,String tenantId);
	
	String getTotalDrawingCount(String pmHdrId,String year ,String month ,String tenantId, String lifespan);
	
	String getCompletionDrawingCount(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	String getDapPlanDate(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	String getManualPlanDate(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	String getDapCompleteDate(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	String getManualCompleteDate(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	List<GetTaskCompPerEntity> getTaskCompPer(String assignedEmp,String deptCode,String tenantId,String year,String month,String pmHdrId);
	
	List<GetTaskCompPerEntity> getTaskCompPerByYear(String assignedEmp,String deptCode ,String startmonth,String endMonth,String tenantId,String pmHdrId);
	
	int getcompletedManualCount(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	int getcompletedDapCount(String pmHdrId,String year ,String month,String tenantId, String lifespan);
	
	List<getDesignWidgetDtlByCategoryEntity> getDesignWidgetDtlByCategory(String deptCode, String empId, String tenantId, String month,
			String year, String category, String projectId);
	
	List<ReportSchedulerEntity>getReportTaskSchForMonth(String month,String year,String tenantId);
	
	List<ReportSchedulerEntity> getReportTaskSchForWeek(String startDate,String endDate,String tenantId);
	
	int getReportTaskCount(String empId,String pmhdrId,String deptCode,String monthYear);
	
	int getReportTaskCountWeek(String empId,String pmhdrId,String deptCode,String startDate,String tenantId);
	
	int insertReportTask(String empId,String pmHdrId,String DepartmentCode,String reportYear,String reportMonth,String monthYear,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId);
	
	int updateReportTask(String empId,String pmHdrId,String DepartmentCode,String monthYear,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId);
	
	int insertReportTaskWeek(String empId,String pmHdrId,String DepartmentCode,String reportYear,String reportMonth,String startDate,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId);
	
	int updateReportTaskWeek(String empId,String pmHdrId,String DepartmentCode,String startDate,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId);
	
	
	List<TaskDtlEntity>getOldTaskMonthPlanned(String yearMonth,String tenantId);
	
	List<TaskDtlEntity>getOldTaskMonthCompleted(String year ,String month,String monthYear,String tenantId);
	
List<TaskDtlEntity>getOldTaskWeekPlanned(String startDate ,String tenantId);
	
	List<TaskDtlEntity>getOldTaskWeekCompleted(String startDate ,String endDate ,String tenantId);

	void updatePlannedTaskMonth(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId);

	void updateCompTaskMonth(TaskDtlEntity taskDtlEntity, String refDate,String tenantId);

	void taskPercentUpdateMonth(String refDate);
	
	void updatePlannedTaskWeek(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId);

	void updateCompTaskWeek(TaskDtlEntity taskDtlEntity, String refDate,String tenantId);

	void taskPercentUpdateWeek(String refDate,String tenantIds);
		
	}
