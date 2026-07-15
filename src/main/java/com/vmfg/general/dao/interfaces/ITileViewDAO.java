package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.finance.entity.FinanceHdrEntity;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.sales.entity.SalesEnqDtlEntity;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;

public interface ITileViewDAO {

	List<String> getDistinctStatus(String tableName);

	List<SalesEnqDtlEntity> saleEnqList(String status,String fromDate, String toDate, String customerName,String tenantId,String empId,String tentativeVal,String isexpectedDate);

	List<DesignHdr> designTitleViewList(String status,String fromDate,String toDate,String customerName,String tenantId,String projectId,String processId,String empId);
	
	List<ProjectHdr> projectTitleViewList(String tenantId, String custName, String fromDate, String toDate, String projectID,
			String empId, String pmId,String status);

	List<ScmHdrBasedDtlEntity>scmTitleViewList(ScmHdrBasedDtlRequest scmHdrBasedDtl,String status);

	List<FinanceHdrEntity> financeTitleViewList(String fromDate, String toDate, String customer, String processId, String empId,
			String tenantID, String financeId,String projectId,String status);
	
	List<GetAssyDtlEntity> assyTitleViewList(String fromDate, String toDate, String custName, String assyId, String tenantID,
			String pmId, String empId,String projectId,String status);

	List<GetQtyDtlEntity> getQtyDtltile(String qHdrId, String empId, String fromDate, String toDate, String tenantId,
			String customerName, String pmId, String projectId, String statusDesc);
}
