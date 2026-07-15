package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;

public interface iReportSchedulerDAO {

	List<ReportSchedulerEntity> getTaskDtl(String frmDate, String toDate,String tenantId);

	int checkReportDtl(String dayStart, String empId, String projId, String tenantID, String deptCode);

	int InsertRecordDtl(String empId, String projId, String deptCode, String year, String month, String dayStart,
			String noPlanned, String noCompleted, String delay, String perCentage, String tenantID, String reportDate);

	int UpdateRecordDtl(String empId, String projId, String deptCode, String year, String month, String dayStart,
			String noPlanned, String noCompleted, String delay, String perCentage, String tenantID, String reportDate);

	String getTenantValue(String tenantId, String propertyValue);
	
	String getOrgTenant();

	String getWeekStartedDate(String dateChk);

	String getPrevDate(String dateChk);

	List<TaskDtlEntity> getOldTaskPlanned(String refDate,String tenantId);

	List<TaskDtlEntity> getOldTaskCompleted(String refDate,String tenantId);

	void updatePlannedTask(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId);

	void updateCompTask(TaskDtlEntity taskDtlEntity, String refDate,String tenantId);

	void taskPercentUpdate(String refDate,String tenantId);

}
