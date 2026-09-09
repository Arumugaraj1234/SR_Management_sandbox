package com.vmfg.scm.dao.interfaces;

import java.util.List;

import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentHdrDropDownEntity;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.entity.StationDropDownEntity;
import com.vmfg.scm.request.IndentGrpRetRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;

public interface IIndentManagementDAO {

	List<ProjectDtlsEntity> getIndentProjectDtlsByDate(String fromDate, String toDate, String tenantId);

	List<ProjectDtlsEntity> getCapexIndentProjectDtlsByDate(String fromDate, String toDate, String tenantId);

	List<IndentHdrDtlsEntity> getIndentHdrDtlsByIndentId(String indentId, String tenantId);

	List<IndentHdrDtlsEntity> getAllIndentHdrDtls(String empId, String pmId, String projectId, String tenantId,
			String byProjectId);

	List<IndentHdrDropDownEntity> indentHdrDropDownByProjectCode(String empId, String pmId, String projectID,
			String tenantId);

	List<IndentHdrDropDownEntity> getIndentsExceptPmVerified(String empId, String pmId, String projectId,
			String tenantId);

	List<IndentHdrDropDownEntity> getOnlyScmAcceptedIndents(String empId, String pmId, String projectId,
			String tenantId);

	List<ScmHdrBasedDtlEntity> getScmHdrBasedDtl(ScmHdrBasedDtlRequest scmHdrBasedDtl);

	List<IndentGroupHdrAndDtlEntity> getIndentGrpNewProd(IndentGrpRetRequest indentGrpReq);

	List<IndentHdrDropDownEntity> getOnlyScmVerifiedAndClosedIndents(String empId, String pmId, String projectId,
			String tenantId);

	String getIndentClosedStatus(String indentId);

	List<ProjectDtlsEntity> getIndentProjectDtlsByDateAndIndent(String fromDate, String toDate, String tenantId);

	List<ProjectDtlsEntity> getIndentProjectDtlsByEmployee(String fromDate, String toDate, String tenantId, String empId);

	List<IndentHdrDropDownEntity> getIndentsBasedOnEmployee(String empId, String pmId, String projectId,
			String tenantId);

	List<IndentHdrDropDownEntity> getCapexIndentsBasedOnEmployee(String empId, String pmId, String projectId,
			String tenantId);
	List<IndentHdrDropDownEntity> getOnlyScmVerifiedIndents(String empId, String pmId, String projectId,
			String tenantId);

	int indentVerCheck(String indentId, String tenantId);

	List<IndentHdrDropDownEntity> getIsInternalOneIndents(String empId, String pmId, String projectId, String tenantId);

	// Station-based PJS grouping (NEW-flow). Stations of a project that still have at least one
	// groupable item across their eligible indents, for the Create Indent Group station dropdown.
	List<StationDropDownEntity> getStationsForGrouping(String projectId, String empId, String tenantId, String getIndent);

	// Groupable items (un-allocated qty remaining) from every eligible indent under one station.
	List<IndentGroupHdrAndDtlEntity> getIndentGrpNewProdByStation(IndentGrpRetRequest indentGrpReq);

}
