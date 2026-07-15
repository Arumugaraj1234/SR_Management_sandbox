package com.vmfg.mis.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.project.entity.IndentHdrEntity;
import com.vmfg.project.entity.ProjectHdrEntity;
import com.vmfg.project.entity.ReInspectionVendorMasterEntity;

public interface iReportSchedulerVIIDAO {
	List<ProjectHdrEntity> getProjects(String tenantId);

	List<IndentHdrEntity> getIndentsByProjectId(String pmHdrId);

	int reportProjectTrackerCountCheck(String pmHdrId, String sbcCode);

	int reportProjectTrackerInsert(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String completedDateTime,
			String lastUpdatedDateTime, String tenantId);

	int reportProjectTrackerUpdate(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String completedDateTime,
			String lastUpdatedDateTime, String tenantId, int hdrId);

	BigDecimal reportGetSaleContribution(String pmHdrId);

	int reportUpdateSaleValue(String pmHdrId, BigDecimal saleContribution, BigDecimal saleValue);

	BigDecimal getMatrialCost(String pmHdrId, String sbcCode);

	int updateMatrialCost(String pmHdrId, String sbcCode, BigDecimal materialCost);

	int reportProjectTrackerInsert1(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String completedDateTime, String lastUpdatedDateTime,
			String tenantId);

	int reportProjectTrackerUpdate1(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String lastUpdatedDateTime, String tenantId,
			int hdrId);

	BigDecimal getMatrialCostCount(String pmHdrId, String sbcCode);

	int updateHumanCost(String pmHdrId, String sbcCode, BigDecimal humanCost);

	BigDecimal getHumanCost(String pmHdrId);

	BigDecimal reportGetSaleValue(String enquiryId);

	List<ReInspectionVendorMasterEntity> getVendorList();

	String getNextApprovingSequence(String tenantId, String pmId);

}
