package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.entity.DesignWidgetDtlEntity;
import com.vmfg.mis.entity.ProjectProgressEntity;
import com.vmfg.mis.entity.TaskCompTimeEntity;
import com.vmfg.mis.entity.getAssyDtlTaskReportEntity;
import com.vmfg.mis.entity.getAssyTaskReportEntity;
import com.vmfg.mis.entity.getPojCompDtlEntity;

public interface IAssemblyMisDAO {

	int assyWidgetDtlDAO(String tenantId, String empId, String deptCode, String pmId, String projId);

	List<TaskCompTimeEntity> getTaskCompTimeDAO(String monthYr, String tenantId, String empId, String deptCode, String pmId, String projId);

	List<TaskCompTimeEntity> getTaskCompTimeDAO1(String monthYr, String tENANTID, String empId, String deptCode,
			String pmId, String projectId);

	List<getPojCompDtlEntity> getPojCompDtlDAO(String tenantId, String empId, String deptCode,
			String pmId, String projId);

	List<getPojCompDtlEntity> getPojCompDtlDAO1(String monthYr, String tENANTID, String empId, String deptCode,
			String pmId, String projectId);

	List<getAssyTaskReportEntity> getAssyTaskReportDAO(String monthYr, String tenantId, String empId, String deptCode,
			String pmId, String projId, String lifespan);

	List<getAssyTaskReportEntity> getAssyTaskReportDAO1(String monthYr, String tENANTID, String empId, String deptCode,
			String pmId, String projectId, String lifespan);

	List<getAssyDtlTaskReportEntity> getAssyDtlTaskReportDAO(String monthYr, String tENANTID, String empId,
			String deptCode, String pmId, String projectId, String lifespan);

	List<getAssyDtlTaskReportEntity> getAssyDtlTaskReportDAO1(String monthYr, String tENANTID, String empId,
			String deptCode, String pmId, String projectId, String lifeSpan);

	List<ProjectProgressEntity> getprojectInProgress(String tENANTID, String empId, String deptCode,
			String pmId, String projectId);

	List<DesignWidgetDtlEntity> assyWidgetDtl(String tenantId, String empId, String deptCode,
			String pmId, String projId);

	int assyWidgetTaskCnt(String tenantId, String empId, String deptCode, String pmId, String projId);

}
