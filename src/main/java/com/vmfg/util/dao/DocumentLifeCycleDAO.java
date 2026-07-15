package com.vmfg.util.dao;

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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.entity.DocLifeCycleLogRequest;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.entity.DocGroupTypeEntity;
import com.vmfg.util.entity.DocGroupTypeRowMapper;
import com.vmfg.util.entity.DocLifeCycleListEntity;
import com.vmfg.util.entity.DocLifeCycleListRowMapper;
import com.vmfg.util.entity.DocLifeCycleMstLogEntity;
import com.vmfg.util.entity.DocLifeCycleMstLogRowMapper;
import com.vmfg.util.entity.DocLifecycleVersionEntity;
import com.vmfg.util.entity.DocLifecycleVersionRowMapper;
import com.vmfg.util.entity.DocumentTypeEntity;
import com.vmfg.util.entity.DocumentTypeRowMapper;
import com.vmfg.util.entity.EmployeeDesignationEntity;
import com.vmfg.util.entity.EmployeeDesignationRowMapper;
@Transactional
@Repository
public class DocumentLifeCycleDAO implements IDocumentLifeCycleDAO {
	private static final Logger logger = LoggerFactory.getLogger(DocumentLifeCycleDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DocumentTypeEntity> getDocTypes(String processCode, String tenantId) {
		// TODO Auto-generated method stub
		List<DocumentTypeEntity> list=new ArrayList<DocumentTypeEntity>();
		try {
			String doctypeQry="SELECT \r\n" + 
					"    distinct(doc.DOCUMENT_TYPE_CODE), doc.DOCUMENT_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_type_mst doc on mst.DOC_TYPE=doc.DOCUMENT_TYPE_CODE\r\n" + 
					"WHERE\r\n" + 
					"    mst.PROCESS_CODE ='"+processCode+"'\r\n" + 
					"        AND mst.TENANT_ID = '"+tenantId+"';";
			list=this.jdbcTemplate.query(doctypeQry, new DocumentTypeRowMapper());
		}catch(Exception ex) {
			logger.error("getDocTypes error "+ex);
		}
		return list;
	}

	@Override
	public List<DocGroupTypeEntity> getDocGroups(String processCode, String tenantId,String docType) {
		List<DocGroupTypeEntity> docGroups=new ArrayList<DocGroupTypeEntity>();
		try {
			String qry="";
			if(!docType.equalsIgnoreCase("DC018")) {
				qry="SELECT DISTINCT\r\n" + 
						"    (DOC_GROUP), DOC_GROUP AS INDENT_TYPE_DESC\r\n" + 
						"FROM\r\n" + 
						"    document_lifecycle_mst\r\n" + 
						"WHERE\r\n" + 
						"    PROCESS_CODE = '"+processCode+"'\r\n" + 
						"        AND DOC_TYPE = '"+docType+"'\r\n" + 
						"        AND TENANT_ID = '"+tenantId+"'\r\n" + 
						"        AND DOC_GROUP IS NOT NULL;";
				
			}else {
				qry="SELECT DISTINCT\r\n" + 
						"    (dm.DOC_GROUP), im.INDENT_TYPE_DESC\r\n" + 
						"FROM\r\n" + 
						"    document_lifecycle_mst dm\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_type_mst im ON dm.DOC_GROUP = im.INDENT_TYPE_CODE\r\n" + 
						"WHERE\r\n" + 
						"    PROCESS_CODE = '"+processCode+"' AND DOC_TYPE = '"+docType+"'\r\n" + 
						"        AND dm.TENANT_ID = '"+tenantId+"'\r\n" + 
						"        AND DOC_GROUP IS NOT NULL;";
			}
			
			docGroups=this.jdbcTemplate.query(qry,new DocGroupTypeRowMapper());
		}catch(Exception ex) {
			logger.error("getDocGroups error "+ex);
		}
		return docGroups;
	}
	
	@Override
	public List<DocumentTypeEntity> getDocStatusTypes(PmHdrIdAndTenantIdRequest tenanttreq) {
		// TODO Auto-generated method stub
		List<DocumentTypeEntity> list=new ArrayList<DocumentTypeEntity>();
		try {
			String doctypeQry="SELECT \r\n" + 
					"    DOCUMENT_STATUS_TYPE_CODE AS DOCUMENT_TYPE_CODE,\r\n" + 
					"    DOCUMENT_STATUS_TYPE_DESCRIPTION AS DOCUMENT_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dtl ON dtl.DOCUMENT_STATUS_TYPE_CODE = hdr.DOC_STATUS\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tenanttreq.getTenantId()+"'\r\n" + 
					"        AND hdr.PROCESS_CODE = '"+tenanttreq.getPmHdrId()+"' group by dtl.DOCUMENT_STATUS_TYPE_CODE;";
			list=this.jdbcTemplate.query(doctypeQry, new DocumentTypeRowMapper());
		}catch(Exception ex) {
			logger.error("getDocTypes error "+ex);
		}
		return list;
	}

	@Override
	public List<DocLifeCycleListEntity> getDocTypesDataList(DocLifeCycleLogRequest docReq) {
		// TODO Auto-generated method stub
		List<DocLifeCycleListEntity> list=new ArrayList<DocLifeCycleListEntity>();
		try {
			String listQry="SELECT \r\n" + 
					"    @a:=@a+1 serial_number, DSM_ID,\r\n" + 
					"    DOC_TYPE,\r\n" + 
					"    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n" + 
					"    PROCESS_CODE,\r\n" + 
					"    DOC_STATUS,\r\n" + 
					"    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
					"    CURR_SEQUENCE,\r\n" + 
					"    APPR_DESI,\r\n" + 
					"    LAST_SEQ,\r\n" + 
					"    CANCEL_SEQ,\r\n" + 
					"    NEXT_SEQ,\r\n" + 
					"    SEQ_BATCH,\r\n" + 
					"    dlm.IS_EDITABLE,dlm.IS_ACTIVE,\r\n" + 
					"    dlm.TENANT_ID\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst dlm\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_type_mst dtm ON dtm.DOCUMENT_TYPE_CODE = dlm.DOC_TYPE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = dlm.DOC_STATUS, (SELECT @a:= 0) AS a\r\n" + 
					"WHERE\r\n" + 
					"    dlm.DOC_TYPE = '"+docReq.getDocType()+"'\r\n" + 
					"        AND dlm.PROCESS_CODE = '"+docReq.getProcessCode()+"'\r\n" + 
					"        AND dlm.DOC_GROUP = '"+docReq.getDocGroup()+"'\r\n" + 
					"        AND dlm.TENANT_ID = '"+docReq.getTenantId()+"'\r\n" + 
					"ORDER BY CURR_SEQUENCE+1;";
			list=this.jdbcTemplate.query(listQry, new DocLifeCycleListRowMapper());
		}catch(Exception ex) {
			logger.error("getDocTypesDataList error "+ex);
		}
		return list;
	}

	@Override
	public String getStatusCode(String statusDesc,String tenantId) {
		String statusCode = "";
		try {
			String maxDocQry="SELECT \r\n" + 
					"    DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"FROM\r\n" + 
					"    document_status_type_code\r\n" + 
					"WHERE\r\n" + 
					"    DOCUMENT_STATUS_TYPE_DESCRIPTION LIKE ?\r\n" + 
					"        AND TENANT_ID = ?" + 
					"LIMIT 1;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(maxDocQry,statusDesc,tenantId);
			 statusCode = resultMap.get("DOCUMENT_STATUS_TYPE_CODE").toString();
		} catch (Exception ex) {
			logger.error("getStatusCode Error " + ex);
		}
		return statusCode;
	}
	
	@Override
	public int insertDocList(DocLifeCycleMstLogEntity docLifeCycleListEntity) {
		int insertRes = 0;
		try {
			String docStatuscode=docLifeCycleListEntity.getDocStatus();
			if(docStatuscode ==null || docStatuscode.isEmpty() ) {
				String maxDocQry="select DOCUMENT_STATUS_TYPE_CODE  from document_status_type_code order by DOCUMENT_STATUS_TYPE_CODE desc limit 1;\r\n";
				//String maxDocStatusCode=this.jdbcTemplate.queryForObject(maxDocQry, String.class);
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(maxDocQry);
				String maxDocStatusCode = resultMap.get("DOCUMENT_STATUS_TYPE_CODE").toString();
				docStatuscode=CommonMethod.getDocStatusCode(maxDocStatusCode);
				
				String insDocStsCdQry="INSERT INTO `document_status_type_code`\r\n" + 
						"(`DOCUMENT_STATUS_TYPE_CODE`,\r\n" + 
						"`DOCUMENT_STATUS_TYPE_DESCRIPTION`,\r\n" + 
						"`IS_ACTIVE`,\r\n" + 
						"`TENANT_ID`)\r\n" + 
						"VALUES\r\n" + 
						"(?,?,?,?);";
				this.jdbcTemplate.update(insDocStsCdQry,docStatuscode,docLifeCycleListEntity.getDocStatusDesc(),
						1,docLifeCycleListEntity.getTenantId());
				
			}
			
			final String docStatuscodeF=docStatuscode;

			String insertDocLifeCycle = "INSERT INTO document_lifecycle_mst (DOC_TYPE, PROCESS_CODE,DOC_STATUS, DOC_GROUP, CURR_SEQUENCE, APPR_DESI, IS_ACTIVE, LAST_SEQ, CANCEL_SEQ, NEXT_SEQ, SEQ_BATCH, IS_EDITABLE, TENANT_ID) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertDocLifeCycle, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, docLifeCycleListEntity.getDocType());
					ps.setString(2, docLifeCycleListEntity.getProcessCode());
					ps.setString(3, docStatuscodeF);
					ps.setString(4, docLifeCycleListEntity.getDocGroup());
					ps.setString(5, docLifeCycleListEntity.getCurSeq());
					ps.setString(6, docLifeCycleListEntity.getApprDesiCode());
					ps.setInt(7, docLifeCycleListEntity.getIsActive());
					ps.setString(8, docLifeCycleListEntity.getLastSeq());
					ps.setString(9, docLifeCycleListEntity.getCancelSeq());
					ps.setString(10, docLifeCycleListEntity.getNextSeq());
					ps.setString(11, docLifeCycleListEntity.getSeqBatch());
					ps.setString(12, docLifeCycleListEntity.getIsEditable());
					ps.setString(13, docLifeCycleListEntity.getTenantId());
					return ps;
				}
			}, holder);
			insertRes = holder.getKey().intValue();
			if(insertRes>0) {
				insertRes=1;
			}else {
				insertRes=0;
			}
		} catch (Exception ex) {
			logger.error("insertDocList error " + ex);
		}
		return insertRes;
	}

	@Override
	public int updateDocList(DocLifeCycleListEntity docLifeCycleListEntity) {
		// TODO Auto-generated method stub
		int updateSeq=0;
		try {
			String updQry="update document_lifecycle_mst set DOC_TYPE=?,DOC_STATUS=?,"
					+ "CURR_SEQUENCE=?,APPR_DESI=?,IS_EDITABLE=?,NEXT_SEQ=?,SEQ_BATCH=?,"
					+ "CANCEL_SEQ=?,LAST_SEQ=? where DSM_ID=? and TENANT_ID=?\r\n" ;
			updateSeq=this.jdbcTemplate.update(updQry,docLifeCycleListEntity.getDocType(),
					docLifeCycleListEntity.getDocStatus(),docLifeCycleListEntity.getCurSeq(),docLifeCycleListEntity.getApprDesiCode(),
					docLifeCycleListEntity.getIsEditable(),docLifeCycleListEntity.getNextSeq(),docLifeCycleListEntity.getSeqBatch(),
					docLifeCycleListEntity.getCancelSeq(),docLifeCycleListEntity.getLastSeq(),docLifeCycleListEntity.getDsmId(),docLifeCycleListEntity.getTenantId());
			
		}catch(Exception ex) {
			logger.error("updateDocList error " + ex);

		}
		return updateSeq;
		
	}

	@Override
	public int deleteDocList(PmHdrIdAndTenantIdRequest docList) {
		// TODO Auto-generated method stub
		int deleteCheck=0;
		try {
			String DeleteQry="delete from document_lifecycle_mst where DSM_ID=? and TENANT_ID=?;";
			deleteCheck=this.jdbcTemplate.update(DeleteQry,docList.getPmHdrId(),docList.getTenantId());
		}catch(Exception ex) {
			logger.error("deleteDocList error "+ex);
		}
		return deleteCheck;
	}

	@Override
	public List<EmployeeDesignationEntity> getEmpDesigList(String tenantID) {
		// TODO Auto-generated method stub
		List<EmployeeDesignationEntity> list = new ArrayList<EmployeeDesignationEntity>();

		try {
			String desigQry = "select DESIGNATION_CODE,DESIGNATION_NAME from employee_designation"
					+ " where TENANT_ID='" + tenantID + "';";
			list = this.jdbcTemplate.query(desigQry, new EmployeeDesignationRowMapper());
		} catch (Exception ex) {
			logger.error("getEmpDesigList error " + ex);

		}
		return list;
	}

	@Override
	public List<EmployeeDesignationEntity> getPmIdList(String tenantID) {
		// TODO Auto-generated method stub
		List<EmployeeDesignationEntity> list = new ArrayList<EmployeeDesignationEntity>();

		try {
			String desigQry = "select PROCESS_NAME as DESIGNATION_NAME,PM_ID as DESIGNATION_CODE from process_mst ";
			list = this.jdbcTemplate.query(desigQry, new EmployeeDesignationRowMapper());
		} catch (Exception ex) {
			logger.error("getEmpDesigList error " + ex);

		}
		return list;
	}


	@Override
	public List<DocLifeCycleMstLogEntity> getLifeCylceMstVersionDtls(String docGroup, String processCode,
			String docType, String tenantId,String version) {
		List<DocLifeCycleMstLogEntity> list=new ArrayList<DocLifeCycleMstLogEntity>();
		String docGrpCol;
		try {
			if(docGroup==null || docGroup.isEmpty()) {
				docGrpCol="";
			}else {
				docGrpCol="AND mst.DOC_GROUP ='"+docGroup+"'";
			}
			String listQry="SELECT \r\n" + 
					"    DSM_LOG_ID,\r\n" + 
					"    DOC_TYPE,\r\n" + 
					"    DOCUMENT_TYPE_DESCRIPTION,\r\n" + 
					"    PROCESS_CODE,\r\n" + 
					"    DOC_STATUS,\r\n" + 
					"    DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
					"    DOC_GROUP,\r\n" + 
					"    CURR_SEQUENCE,\r\n" + 
					"    APPR_DEPT,\r\n" + 
					"    APPR_DESI,\r\n" + 
					"    mst.IS_ACTIVE,\r\n" + 
					"    IS_NOTIFY,\r\n" + 
					"    LAST_SEQ,\r\n" + 
					"    CANCEL_SEQ,\r\n" + 
					"    NEXT_SEQ,\r\n" + 
					"    SEQ_BATCH,\r\n" + 
					"    IS_EDITABLE,\r\n" + 
					"    MASTER_COMPLETED,\r\n" + 
					"    VERSION,\r\n" + 
					"    VERSION_DATETIME,\r\n" + 
					"    emp.EMPLOYEE_FIRSTNAME,mst.TENANT_ID\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst_log mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_type_mst dtm ON dtm.DOCUMENT_TYPE_CODE = mst.DOC_TYPE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = mst.DOC_STATUS\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON mst.UPDATED_BY = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    mst.PROCESS_CODE = '"+processCode+"'\r\n" + docGrpCol+
					"        AND mst.DOC_TYPE = '"+docType+"'\r\n" + 
					"        AND mst.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND VERSION = '"+version+"'\r\n" + 
					"ORDER BY CURR_SEQUENCE;";
			list=this.jdbcTemplate.query(listQry, new DocLifeCycleMstLogRowMapper());
		}catch(Exception ex) {
			logger.error("getDocLifecycleLogDtls error "+ex);
		}
		return list;
	}
//
	@Override
	public List<String> getDesignationDesc(String designationCode) {
		List<String> deisgn=new ArrayList<String>();
		try {
			String qry="SELECT \r\n" + 
					"    DESIGNATION_NAME\r\n" + 
					"FROM\r\n" + 
					"    employee_designation\r\n" + 
					"WHERE\r\n" + 
					"    FIND_IN_SET(DESIGNATION_CODE, '"+designationCode+"');" ;
			deisgn=this.jdbcTemplate.queryForList(qry,String.class);
			
		}catch(Exception ex) {
			logger.error("getDesignationDesc error " + ex);

		}
		return deisgn;
		
	}
	
	@Override
	public List<DocLifecycleVersionEntity> getVersionList(String docGroup, String processCode,
			String docType, String tenantId) {
		List<DocLifecycleVersionEntity> list=new ArrayList<DocLifecycleVersionEntity>();
		String docGrpCol;
		try {
			if(docGroup==null || docGroup.isEmpty()) {
				docGrpCol="";
			}else {
				docGrpCol = "AND mst.DOC_GROUP ='" + docGroup + "'";
			}
			String listQry="SELECT \r\n" + 
					"    VERSION,\r\n" + 
					"    VERSION_DATETIME,\r\n" + 
					"    emp.EMPLOYEE_FIRSTNAME as UPDATED_BY\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst_log mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON mst.UPDATED_BY = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    mst.PROCESS_CODE = '"+processCode+"'\r\n" + 
					"        AND mst.DOC_TYPE = '"+docType+"'\r\n" + docGrpCol + 
					"        AND mst.TENANT_ID = '"+tenantId+"'\r\n" + 
					"GROUP BY VERSION";
			list=this.jdbcTemplate.query(listQry, new DocLifecycleVersionRowMapper());
		}catch(Exception ex) {
			logger.error("getVersionList error "+ex);
		}
		return list;
	}


	@Override
	public int insertDocLifecycleMstLog(DocLifeCycleMstLogEntity docLifeCycleListEntity,String version) {
		int updateSeq=0;
		try {
			String updQry="INSERT INTO document_lifecycle_mst_log (DOC_TYPE, PROCESS_CODE, DOC_STATUS, DOC_GROUP, CURR_SEQUENCE, APPR_DESI, IS_ACTIVE, IS_NOTIFY, LAST_SEQ, CANCEL_SEQ, NEXT_SEQ, SEQ_BATCH, IS_EDITABLE, VERSION, VERSION_DATETIME, TENANT_ID,UPDATED_BY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);" ;
			updateSeq=this.jdbcTemplate.update(updQry,docLifeCycleListEntity.getDocType(),docLifeCycleListEntity.getProcessCode(),
					docLifeCycleListEntity.getDocStatus(),docLifeCycleListEntity.getDocGroup(),docLifeCycleListEntity.getCurSeq(),
					docLifeCycleListEntity.getApprDesiCode(),docLifeCycleListEntity.getIsActive(),null,
					docLifeCycleListEntity.getLastSeq(),docLifeCycleListEntity.getCancelSeq(),docLifeCycleListEntity.getNextSeq(),
					docLifeCycleListEntity.getSeqBatch(),docLifeCycleListEntity.getIsEditable(),version,CommonMethod.getCurrentDateTime(),
					docLifeCycleListEntity.getTenantId(),docLifeCycleListEntity.getEmpId());
			
		}catch(Exception ex) {
			logger.error("insertDocLifecycleMstLog error " + ex);

		}
		return updateSeq;
		
	}
	
	@Override
	public String getDocVersion(String docGroup, String processCode,String docType, String tenantId ) {
		String version="";
		try {
			if(docGroup==null || docGroup.isEmpty()) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(VERSION) > 0 THEN MAX(VERSION) + 1\r\n" + 
						"        ELSE 1\r\n" + 
						"    END AS VERSION\r\n" + 
						"FROM\r\n" + 
						"    document_lifecycle_mst_log\r\n" + 
						"WHERE\r\n" + 
						"    PROCESS_CODE = ?\r\n" + 
						"        AND DOC_TYPE = ?\r\n" + 
						"        AND TENANT_ID = ?;" ;
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,processCode,docType,tenantId);
				version = resultMap.get("VERSION").toString();
				
			}else {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(VERSION) > 0 THEN MAX(VERSION) + 1\r\n" + 
						"        ELSE 1\r\n" + 
						"    END AS VERSION\r\n" + 
						"FROM\r\n" + 
						"    document_lifecycle_mst_log\r\n" + 
						"WHERE\r\n" + 
						"    PROCESS_CODE = ? AND DOC_GROUP =?\r\n" + 
						"        AND DOC_TYPE = ?\r\n" + 
						"        AND TENANT_ID = ?;" ;
				
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,processCode,docGroup,docType,tenantId);
				version = resultMap.get("VERSION").toString();
			}
			
		}catch(Exception ex) {
			logger.error("getDocVersion error " + ex);

		}
		return version;
		
	}
	
	@Override
	public int deleteDocLifecycleMstList(String docGroup, String processCode,String docType, String tenantId) {
		int deleteCheck=0;
		try {
			if(!docGroup.equalsIgnoreCase(null) && !docGroup.isEmpty()) {
//				String DeleteQry="delete from document_lifecycle_mst where PROCESS_CODE = ? AND DOC_TYPE = ? AND TENANT_ID = ?";
//				deleteCheck=this.jdbcTemplate.update(DeleteQry,processCode,docType,tenantId);
//				
//			}else {
				String DeleteQry="delete from document_lifecycle_mst where PROCESS_CODE = ? AND DOC_GROUP =? AND DOC_TYPE = ? AND TENANT_ID = ?";
				deleteCheck=this.jdbcTemplate.update(DeleteQry,processCode,docGroup,docType,tenantId);
			}

		}catch(Exception ex) {
			logger.error("deleteDocLifecycleMstList error "+ex);
		}
		return deleteCheck;
	}
	
	@Override
	public int getDoclifecycleCount(String docGroup, String processCode,String docType, String tenantId) {
		int count=0;
		try {
			if(docGroup==null || docGroup.isEmpty()) {
				String qry="select count(*) as COUNT from document_lifecycle_mst where PROCESS_CODE = ? AND DOC_TYPE = ? AND TENANT_ID = ?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, processCode,docType,tenantId);
				count = Integer.parseInt(resultMap.get("COUNT").toString());
			}else {
				String qry="select count(*) as COUNT   from document_lifecycle_mst where PROCESS_CODE = ? AND DOC_GROUP = ? AND DOC_TYPE = ? AND TENANT_ID = ?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, processCode,docGroup,docType,tenantId);
				count = Integer.parseInt(resultMap.get("COUNT").toString());
			}
		
		}catch(Exception ex) {
			logger.error("getDoclifecycleCount error "+ex);
		}
		return count;
	}

	

	

}
