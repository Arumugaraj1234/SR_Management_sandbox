package com.vmfg.general.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.interfaces.IDocumentManagementDAO;
import com.vmfg.general.entity.DocumentManagementAccessEntity;
import com.vmfg.general.entity.DocumentManagementEntity;
import com.vmfg.general.entity.FileManagerDownloadEntity;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.rowmapper.DocumentManagementAccessRowMapper;
import com.vmfg.general.rowmapper.DocumentManagementRowMapper;
@Transactional
@Repository
public class DocumentManagementDAO implements IDocumentManagementDAO{
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DocumentManagementEntity> getDocumentManagementDetails(String tENANT_ID, String eNQUIRY_ID,
			String pROJECT_D,String Emp_ID) {
		List<DocumentManagementEntity> list=new ArrayList<>();
		ResponseAsMessage resp = new ResponseAsMessage();

		try {

			String query="SELECT DISTINCT\r\n"
					+ "    (dm.DM_ID),\r\n"
					+ "    dm.DOCUMENT_NAME,\r\n"
					+ "    dm.VERSION,\r\n"
					+ "    fm.FILE_CREATED_BY,\r\n"
					+ "    fuc.DOCUMENT_TYPE_CODE,\r\n"
					+ "    fuc.FU_CODE,\r\n"
					+ "    fuc.DESCRIPTION,\r\n"
					+ "    sm.STG_DESC,\r\n"
					+ "    sm.STG_CODE,\r\n"
					+ "    fm.FILE_CREATED_DATE,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME,dm.REMARKS \r\n"
					+ "FROM\r\n"
					+ "    document_management AS dm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_management_access AS dma ON dm.DM_ID = dma.DM_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    file_manager AS fm ON fm.REFERNCE_ID = dm.DM_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    file_upload_config AS fuc ON fuc.FU_CODE = dm.UPLOAD_DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    stg_master AS sm ON sm.STG_CODE = dm.STAGE_CODE\r\n"
					+ "     INNER JOIN\r\n"
					+ "    employee_mst AS em ON em.EMPLOYEE_ID = fm.FILE_CREATED_BY\r\n"
					+ "    \r\n"
					+ "WHERE\r\n"
					+ "    (dm.ENQUIRY_ID =?\r\n"
					+ "        OR dm.PROJECT_ID =?)\r\n"
					+ "        AND dm.TENANT_ID =?\r\n"
					+ "        AND dm.APPROVED = 1\r\n"
					+ "         AND dma.DEPARTMENT_CODE=(SELECT DEPARTMENT_CODE FROM employee_mst where EMPLOYEE_ID='"+Emp_ID+"')\r\n"
					+ "        AND dm.LATEST_VERSION =1";
			RowMapper<DocumentManagementEntity> rowmapper = new DocumentManagementRowMapper();	  
			list=this.jdbcTemplate.query(query,rowmapper, eNQUIRY_ID,pROJECT_D,tENANT_ID);	 
			
		} catch (Exception ex) {
			logger.error("getDepartmentInfo  method exception-->" + ex);
			resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		logger.debug("getDepartmentInfo  method end");

		return list;
	}

	@Override
	public FileManagerDownloadEntity documentDownloadDocFile(String tENANT_ID, String rEFERNCE_ID ) {
		FileManagerDownloadEntity fdEntity = new FileManagerDownloadEntity();
		logger.info("documentDownloadDocFile DAO method Start");

		try {


			String DocntQ="SELECT \r\n"
					+ "   count(FILE_NAME) as FILE_COUNT \r\n "
					+ "FROM\r\n"
					+ "    file_manager\r\n"
					+ "WHERE\r\n"
					+ "    REFERNCE_ID = ? \r\n"
					+ "        AND TENANT_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(DocntQ,rEFERNCE_ID,tENANT_ID);
			int cnt = Integer.parseInt(result.get("FILE_COUNT").toString());
			if(cnt>0) {
				String fileDocPathQry="select concat(FILE_PATH,'\\\\',FILE_NAME) as FILEPATH from file_manager where "
						+ "    REFERNCE_ID = ? \r\n"
						+ "        AND TENANT_ID = ? ";
				Map<String, Object> results = this.jdbcTemplate.queryForMap(fileDocPathQry,rEFERNCE_ID,tENANT_ID);
				String docpath = results.get("FILEPATH").toString();
				
				String reportPath=docpath;
				
				Map<String, Object> res = this.jdbcTemplate.queryForMap("select FILE_ABSOLUTE_NAME from "
						+ "file_manager where"
						+ "    REFERNCE_ID = ? \r\n"
						+ "        AND TENANT_ID = ? ",rEFERNCE_ID,tENANT_ID);
				String filename = res.get("FILE_ABSOLUTE_NAME").toString();
				
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

	@Override
	public List<DocumentManagementAccessEntity> getdocumentManagementAccessDtl(String tENANT_ID, String dM_ID) {
		List<DocumentManagementAccessEntity> list=new ArrayList<>();
		int id=Integer.parseInt(dM_ID);

		try {

			String query=" SELECT \r\n"
					+ "    dma.ENABLED_DATETIME, d.DEPARTMENT_NAME, d.DEPARTMENT_CODE,dma.DMA_ID\r\n"
					+ "FROM\r\n"
					+ "    document_management_access AS dma\r\n"
					+ "        INNER JOIN\r\n"
					+ "    department AS d ON d.DEPARTMENT_CODE = dma.DEPARTMENT_CODE\r\n"
					+ "WHERE\r\n"
					+ "    dma.DM_ID =?\r\n"
					+ "        AND dma.TENANT_ID =?";
			RowMapper<DocumentManagementAccessEntity> rowmapper = new DocumentManagementAccessRowMapper();	 
			list=this.jdbcTemplate.query(query,rowmapper,id,tENANT_ID);	 

		} catch (Exception ex) {
			logger.error("getdocumentManagementAccessDtl  method exception-->" + ex);
		}
		logger.debug("getdocumentManagementAccessDtl  method end");

		return list;
	}

	@Override
	public ResponseAsMessage insertDocumentManagementAccessDtl(String tENANT_ID, String dM_ID, String dEPT_CODE) {
		ResponseAsMessage resp = new ResponseAsMessage();

		try {
			String query = "insert into document_management_access (DM_ID,DEPARTMENT_CODE,ENABLED_DATETIME,TENANT_ID) values(?,?,NOW(),?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, dM_ID);
					ps.setString(2, dEPT_CODE);
					ps.setString(3, tENANT_ID);

					return ps;
				}

			}, holder);
			int insertRes = holder.getKey().intValue();

			if (insertRes > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successInserted);


			}else {
				resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			}
		} catch (Exception ex) {
			logger.error("insertDocumentManagementAccessDtl DAO method exception-->" + ex);
		}
		logger.debug("insertDocumentManagementAccessDtl DAO method ends");
		return resp ;
	}

	@Override
	public ResponseAsMessage deleteDocumentManagementAccessDtl( String dMA_ID) {
		ResponseAsMessage resp = new ResponseAsMessage();

		try {

			String query="DELETE FROM document_management_access \r\n"
					+ "WHERE\r\n"
					+ "   DMA_ID =?";

			int	deleteId=this.jdbcTemplate.update(query,dMA_ID);	 
			if (deleteId > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successfulDeleted);


			}else {
				resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			}
		} catch (Exception ex) {
			logger.error("deleteDocumentManagementAccessDtl  method exception-->" + ex);
		}


		logger.debug("deleteDocumentManagementAccessDtl DAO method ends");
		return resp ;
	}

	@Override
	public List<DocumentManagementEntity> getDocumentManagementDetailsById(String tENANT_ID, String refId, String stgCode) {
		List<DocumentManagementEntity> list=new ArrayList<>();
		ResponseAsMessage resp = new ResponseAsMessage();

		try {

			String query="SELECT DISTINCT\r\n" + 
					"    (dm.DM_ID),\r\n" + 
					"    dm.DOCUMENT_NAME,\r\n" + 
					"    dm.VERSION,\r\n" + 
					"    fm.FILE_CREATED_BY,\r\n" + 
					"    fuc.DOCUMENT_TYPE_CODE,\r\n" + 
					"    fuc.DESCRIPTION,\r\n" + 
					"    sm.STG_DESC,\r\n" + 
					"    sm.STG_CODE,\r\n" + 
					"    fm.FILE_CREATED_DATE,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME,\r\n" + 
					"    fuc.FU_CODE,fm.FILE_ABSOLUTE_NAME, \r\n" + 
					"     fm.FILE_NAME_EXTN,\r\n" +
					"    CASE WHEN fm.FILE_NAME_EXTN = 'pdf' THEN 1 ELSE 0 END AS IS_PDF \r\n" + 
					"FROM\r\n" + 
					"    document_management AS dm\r\n" + 
					"        INNER JOIN   \r\n" + 
					"    file_manager AS fm ON fm.REFERNCE_ID = dm.DM_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    file_upload_config AS fuc ON fuc.FU_CODE = dm.UPLOAD_DOC_TYPE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    stg_master AS sm ON sm.STG_CODE = dm.STAGE_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst AS em ON em.EMPLOYEE_ID = fm.FILE_CREATED_BY\r\n" + 
					"WHERE\r\n" + 
					"    dm.REFERENCE_ID = ?\r\n" + 
					"        AND dm.TENANT_ID = ?\r\n" + 
					"        AND STG_CODE = ?";
			RowMapper<DocumentManagementEntity> rowmapper = new DocumentManagementRowMapper();	  
			list=this.jdbcTemplate.query(query,rowmapper, refId,tENANT_ID,stgCode);	 
			
		} catch (Exception ex) {
			logger.error("getDepartmentInfo  method exception-->" + ex);
			resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		logger.debug("getDepartmentInfo  method end");

		return list;
	}

	@Override
	public String getDeptByDmaId(String dmaId) {
		String getDeptByDmaId ="";
		try {
			String getDeptByDmaIdStr = "select  DEPARTMENT_CODE from document_management_access where DMA_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getDeptByDmaIdStr,dmaId);
			getDeptByDmaId = resultMap.get("DEPARTMENT_CODE").toString();
		}catch(Exception ex) {
			logger.error("getDeptByDmaId  method exception-->" + ex);	
		}
		return getDeptByDmaId;
	}

	@Override
	public int getDeptCountByDmId(String DmId,String dept) {
		int getDeptByDmId =0;
		try {
			String getDeptByDmIdStr = "select  count(*) as VALUE from document_management_access where DM_ID = ? and DEPARTMENT_CODE = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getDeptByDmIdStr,DmId,dept);
			getDeptByDmId = Integer.parseInt(resultMap.get("VALUE").toString());
		}catch(Exception ex) {
			logger.error("getDeptCountByDmId  method exception-->" + ex);
		}
		return getDeptByDmId;
	}

	@Override
	public String getAccessDeptByDmId(String dmId, String tenantId) {
		
		String getAccessDeptByDmId ="";
		try {
			String getAccessDeptByDmIdStr = "SELECT group_concat(d.DEPARTMENT_NAME) as DEPT_NAME \n"
					+ "		 FROM\n"
					+ "		     document_management_access AS dma\n"
					+ "		         INNER JOIN\n"
					+ "		     department AS d ON d.DEPARTMENT_CODE = dma.DEPARTMENT_CODE\n"
					+ "		 WHERE\n"
					+ "		     dma.DM_ID = ? \n"
					+ "		         AND dma.TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getAccessDeptByDmIdStr,dmId,tenantId);
			getAccessDeptByDmId = resultMap.get("DEPT_NAME").toString();
		}catch(Exception ex) {
			logger.error("getAccessDeptByDmId  method exception-->" + ex);	
		}
		return getAccessDeptByDmId;
		
	}
	
	@Override
	public String getDepartmentDescByDepartmentCode(String department) {
		 
			String getdepartDesc ="";
				try {
					String getdepartDescStr = "select DEPARTMENT_NAME from department where DEPARTMENT_CODE= ? ";
					Map<String, Object> resultMap = jdbcTemplate.queryForMap(getdepartDescStr,department);
					getdepartDesc = resultMap.get("DEPARTMENT_NAME").toString();
				}catch(Exception ex) {
					logger.error("getDepartmentDescByDepartmentCode  method exception-->" + ex);	
				}
				return getdepartDesc;
	}

	@Override
	public String getEmpIdByDeptCode(String department) {
	    StringBuilder empIds = new StringBuilder();
	    try {
	        String sql = "SELECT DISTINCT e.EMPLOYEE_ID, d.DEPARTMENT_NAME " +
	                     "FROM employee_mst AS e " +
	                     "INNER JOIN department AS d ON e.DEPARTMENT_CODE = d.DEPARTMENT_CODE " +
	                     "WHERE d.DEPARTMENT_CODE = ?";

	        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, department);

	        for (Map<String, Object> row : results) {
	            if (empIds.length() > 0) {
	                empIds.append(", ");
	            }
	            empIds.append(row.get("EMPLOYEE_ID").toString());
	        }
	    } catch (Exception ex) {
	        logger.error("getEmpIdByDeptCode method exception --> " + ex);
	    }
	    return empIds.toString();
	}

	@Override
	public ResponseAsMessage deleteDocumentManagementandAccessDtl(String dMA_ID) {
		ResponseAsMessage resp = new ResponseAsMessage();

		try {

			String query="DELETE FROM document_management_access \r\n"
					+ "WHERE\r\n"
					+ "   DM_ID =?";

			int	deleteId=this.jdbcTemplate.update(query,dMA_ID);	 
			if (deleteId > 0) {
				
				String qry="DELETE FROM document_management \r\n"
						+ "WHERE\r\n"
						+ "   DM_ID =?";

				int	delete=this.jdbcTemplate.update(qry,dMA_ID);
				if(delete >0 ) {
					resp.setResponseCode(ResponseMessageMap.responseCodeOk);
					resp.setResponseMessage(ResponseMessageMap.successfulDeleted);
				}else {
					resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				}
			}else {
				resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			}
		} catch (Exception ex) {
			logger.error("deleteDocumentManagementandAccessDtl  method exception-->" + ex);
		}
		return resp;
	}
}
