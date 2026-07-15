package com.vmfg.export.dao.interfaces;

import java.sql.Connection;
import java.util.List;

import com.vmfg.export.entity.DocumentFilePathEntity;
import com.vmfg.export.entity.DocumentManagerFileEntity;

public interface IProjectReportTrackerDAO {

	String getPropValueByTenant(String tenantId, String propertyName);
	String getOrganizationInfo(String tenantId);
	String getCurrentDateTime();
	String getProjectTrackerPath(String tenantId, String key,String isDestination);
	Connection getConnection();
	String getOrganizationLogoPath(String tenantId);
	String getProjectCode(String tenantId,String projectId);
	String getPoCode(String tenantId, String poId);
	String getPraCode(String tenantId, String praId);
	List<DocumentFilePathEntity> getFileDocumentPath(String tenantId, String poId);
	DocumentManagerFileEntity documentDownloadDocFile(String tenantId, String indentDtlId);
	
}
