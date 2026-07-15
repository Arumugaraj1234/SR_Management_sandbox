package com.vmfg.export.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.export.dao.interfaces.IProjectReportTrackerDAO;
import com.vmfg.export.entity.DocumentFilePathEntity;
import com.vmfg.export.entity.DocumentManagerFileEntity;
import com.vmfg.export.rowmapper.DocumentFilePathRowMapper;
import com.vmfg.general.response.ResponseMessageMap;


@Transactional
@Repository
public class ProjectReportTrackerDAO implements IProjectReportTrackerDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProjectReportTrackerDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getPropValueByTenant(String tenantId, String propertyName) {
		String propertyValue = "";
		try {
			String qty = "select PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,tenantId,propertyName);
			String propValue  = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValue;
	}

	@Override
	public String getCurrentDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	@Override
	public String getOrganizationInfo(String tenantId) {
		String orgNameQry = "select ORG_NAME from organization_info where TENANT_ID = ? limit 1";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(orgNameQry,tenantId);
		String orgName  = resultMap.get("ORG_NAME").toString();
		return orgName;
	}

	@Override
	public String getProjectTrackerPath(String tenantId, String key,String isDestination) {
		String reportPathQry = "SELECT \r\n" + 
				"    path\r\n" + 
				"FROM\r\n" + 
				"    document_path_property\r\n" + 
				"WHERE\r\n" + 
				"    key_value = ? AND tenant_id = ?\r\n" + 
				"        AND is_destination =?";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(reportPathQry,key,tenantId,isDestination);
		String reportPath  = resultMap.get("path").toString();
		return reportPath;
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("getConnection Method Exception " + e);
			e.printStackTrace();
		}
		return connection;

	}

	@Override
	public String getOrganizationLogoPath(String tenantId) {

		String logoPathQry = "select LOGO_PATH from organization_info where TENANT_ID=? limit 1";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(logoPathQry,tenantId);
		String logoPath  = resultMap.get("LOGO_PATH").toString();
		return logoPath;
	}

	@Override
	public String getProjectCode(String tenantId, String projectId) {

		String projectCodeQry = "SELECT \r\n" + "    CASE\r\n"
				+ "        WHEN COUNT(PROJECT_CODE) > 0 THEN PROJECT_CODE\r\n" + "        ELSE '0'\r\n"
				+ "    END AS PROJECT_CODE\r\n" + "FROM\r\n" + "    project_hdr\r\n" + "WHERE\r\n" + "    PM_HDR_ID = ? AND "
				+ "TENANT_ID = ?\r\n" + "LIMIT 1";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(projectCodeQry,projectId,tenantId);
		String projectCode = resultMap.get("PROJECT_CODE").toString();
		return projectCode;
	}
	
	@Override
	public String getPraCode(String tenantId, String praId) {
		
		String praCodeQry= "SELECT \r\n" + "   CASE\r\n"
		+ "      WHEN COUNT(PRA_CODE) >0 THEN PRA_CODE\r\n" + "        ELSE '0'\r\n"
		+ "      END AS PRA_CODE\r\n" +  "FROM\r\n" + "    pra_hdr\r\n" + "WHERE\r\n" + "   PRA_ID = ? AND "	
		+ "TENANT_ID = ?\r\n" + "LIMIT 1";
		
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCodeQry,praId, tenantId);
		String PraCode = resultMap.get("PRA_CODE").toString();
		return PraCode;
	}
	
	@Override
	public String getPoCode(String tenantId, String poId) {

		String projectCodeQry = "select PO_CODE from po_hdr where PO_ID=? and TENANT_ID=?";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(projectCodeQry,poId,tenantId);
		String projectCode = resultMap.get("PO_CODE").toString();
		return projectCode;
	}
	
	
	@Override
	public List<DocumentFilePathEntity> getFileDocumentPath(String tenantId, String poId){
		List<DocumentFilePathEntity> Idlist=new ArrayList<>();
		String refIdQry = "SELECT \r\n" + 
				"    distinct (id.INDENT_DTL_ID)\r\n" + 
				"FROM\r\n" + 
				"    po_hdr ih\r\n" + 
				"        INNER JOIN\r\n" + 
				"    po_dtl id ON ih.PO_ID = id.PO_ID\r\n" + 
				"WHERE ih.PO_ID ='"+poId+"' AND ih.TENANT_ID = '"+tenantId+"' ";
		RowMapper<DocumentFilePathEntity> rowmapper = new DocumentFilePathRowMapper();	  
		Idlist = this.jdbcTemplate.query(refIdQry,rowmapper);	

		return Idlist;
	}
	
	public DocumentManagerFileEntity documentDownloadDocFile(String tENANT_ID, String indentDtlId ) {
		DocumentManagerFileEntity fdEntity = new DocumentManagerFileEntity();
		logger.info("documentDownloadDocFile DAO method Start");

		try {
			int DM = 0;
			String checkCountStr = "select count(*) DM_ID_COUNT from document_management where REFERENCE_ID=?"
					+ " and UPLOAD_DOC_TYPE='FC015' and TENANT_ID=? order by VERSION desc limit 1;";
					Map<String, Object> results = this.jdbcTemplate.queryForMap(checkCountStr,indentDtlId,tENANT_ID);
					int checkCount =  Integer.parseInt(results.get("DM_ID_COUNT").toString());
					
					if(checkCount>0) {
						String DMid = "select  DM_ID AS DM_ID from document_management where REFERENCE_ID=?"
								+ " and UPLOAD_DOC_TYPE='FC015' and TENANT_ID=? order by VERSION desc limit 1;";
						Map<String, Object> result = this.jdbcTemplate.queryForMap(DMid,indentDtlId,tENANT_ID);
			        	 DM = Integer.parseInt(result.get("DM_ID").toString());
								}
			
//			String DMid ="SELECT \r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN COUNT(*) > 0 THEN DM_ID\r\n" + 
//					"        ELSE 0\r\n" + 
//					"    END AS DMID\r\n" + 
//					"FROM\r\n" + 
//					"    document_management\r\n" + 
//					"WHERE\r\n" + 
//					"    REFERENCE_ID = ?\r\n" + 
//					"        AND UPLOAD_DOC_TYPE = 'FC015'\r\n" + 
//					"        AND TENANT_ID = ?\r\n" + 
//					"ORDER BY VERSION DESC\r\n" + 
//					"LIMIT 1;";
//         	Map<String, Object> result = this.jdbcTemplate.queryForMap(DMid,indentDtlId,tENANT_ID);
//        	int DM = Integer.parseInt(result.get("DMID").toString());
         	
         	
         	String DocntQ="SELECT \r\n"
    					+ "   count(FILE_NAME) as COUNT\r\n"
    					+ "FROM\r\n"
    					+ "    file_manager\r\n"
    					+ "WHERE\r\n"
    					+ "    REFERNCE_ID = ?\r\n"
    					+ "        AND TENANT_ID = ?";
    			Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(DocntQ,DM,tENANT_ID);
            	int cnt = Integer.parseInt(resultMap.get("COUNT").toString());
            	
			if(cnt>0) {
				String fileDocPathQry="select concat(FILE_PATH,'\\\\',FILE_NAME) as FILE_PATH from file_manager where "
						+ "    REFERNCE_ID = ?\r\n"
						+ "        AND TENANT_ID = ?";
				Map<String, Object> docpathResult = jdbcTemplate.queryForMap(fileDocPathQry,DM,tENANT_ID);
				String docpath= docpathResult.get("FILE_PATH").toString();
				
				String getFileName="select FILE_ABSOLUTE_NAME from "
						+ "file_manager where"
						+ "    REFERNCE_ID = ?\r\n"
						+ "        AND TENANT_ID = ?";
				Map<String, Object> fileNameResult = jdbcTemplate.queryForMap(getFileName,DM,tENANT_ID);
				String filename= fileNameResult.get("FILE_ABSOLUTE_NAME").toString();
				
				String reportPath=docpath;
				logger.info(reportPath);
				fdEntity.setFileName(filename);
				fdEntity.setFilePath(reportPath);
				fdEntity.setMessageCode(ResponseMessageMap.responseCodeOk);
			}


		}catch(Exception ex) {
			fdEntity.setMessageCode("E0092");
			logger.error("documentDownloadDocFile Method DAO exception---> "+ex );
		}
		return fdEntity;
	}

}
