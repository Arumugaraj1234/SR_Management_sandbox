package com.vmfg.sales.dao.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.rowmapper.DocLifeCycleMstTableRowMapper;
import com.vmfg.sales.dao.interfaces.IUploadManagementDAO;
import com.vmfg.sales.entity.ApprovedDocEntity;
import com.vmfg.sales.entity.ChangeRequestHdrInfoEntity;
import com.vmfg.sales.entity.DocumentAppStatusDtlEntity;
import com.vmfg.sales.entity.DocumentManagementTblEntity;
import com.vmfg.sales.entity.FileUploadConfigtblEntity;
import com.vmfg.sales.rowmapper.ApprovedDocRowMapper;
import com.vmfg.sales.rowmapper.ChangeRequestHdrInfoRowMapper;
import com.vmfg.sales.rowmapper.DocumentAppStatusDtlRowMapper;
import com.vmfg.sales.rowmapper.DocumentManagementTblRowMapper;
import com.vmfg.sales.rowmapper.FileUploadConfigtblRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class UploadManagementDAO implements IUploadManagementDAO {
	private static final Logger logger = LoggerFactory.getLogger(UploadManagementDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ApprovedDocEntity> getApprovedDocDtl(String enquiryId, String stageCode, String approved,
			String tenantId,String docType) {
		List<ApprovedDocEntity> list = new ArrayList<ApprovedDocEntity>();
		String getDocList = "";
		try {
			if (approved.equalsIgnoreCase("1")) {
				getDocList = "SELECT \r\n" + "    fc.DOCUMENT_TYPE_CODE,\r\n" + "    mst.DOCUMENT_TYPE_DESCRIPTION,\r\n"
						+ "    dm.DM_ID,\r\n" + "    dm.UPLOAD_DOC_TYPE,\r\n"
						+ "    fc.DESCRIPTION AS UPLOAD_DOCUMENT,\r\n" + "    dm.VERSION,\r\n"
						+ "    emp.EMPLOYEE_FIRSTNAME,\r\n"
						+ "    dm.DOCUMENT_NAME,dm.REMARKS,fim.FILE_CREATED_DATE,dm.REFERENCE_ID,dm.STAGE_CODE,fim.FILE_ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME,"
						+ "( CASE\r\n" + 
						"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
						"        ELSE 0\r\n" + 
						"    END)AS IS_PDF  \r\n"
						+ "    \r\n" + "FROM\r\n" + "    document_management dm\r\n" + "        Left JOIN\r\n"
						+ "    file_upload_config fc ON dm.UPLOAD_DOC_TYPE = fc.FU_CODE\r\n" + "        Left JOIN\r\n"
						+ "    document_type_mst mst ON fc.DOCUMENT_TYPE_CODE = mst.DOCUMENT_TYPE_CODE\r\n"
						+ "        left JOIN\r\n" + "    file_manager fim ON dm.DM_ID = fim.REFERNCE_ID\r\n"
						+ "    left join employee_mst emp on fim.FILE_CREATED_BY=emp.EMPLOYEE_ID\r\n" + "WHERE\r\n"
						+ "    dm.ENQUIRY_ID = ?\r\n" + "        AND dm.APPROVED = ?\r\n"
						+ "        AND dm.TENANT_ID = ?\r\n" + "        AND dm.STAGE_CODE = ? and LATEST_VERSION='1' and DOC_TYPE_CODE=?";

			} else {
				getDocList = "SELECT \r\n" + "    fc.DOCUMENT_TYPE_CODE,\r\n" + "    mst.DOCUMENT_TYPE_DESCRIPTION,\r\n"
						+ "    dm.DM_ID,\r\n" + "    dm.UPLOAD_DOC_TYPE,\r\n"
						+ "    fc.DESCRIPTION AS UPLOAD_DOCUMENT,\r\n" + "    dm.VERSION,\r\n"
						+ "    emp.EMPLOYEE_FIRSTNAME,\r\n" + "    dm.DOCUMENT_NAME,\r\n" + "    dm.REMARKS,\r\n"
						+ "    fim.FILE_CREATED_DATE,dm.REFERENCE_ID,dm.STAGE_CODE,fim.FILE_ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME, "
						+ "( CASE\r\n" + 
						"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
						"        ELSE 0\r\n" + 
						"    END)AS IS_PDF  \r\n" + "FROM\r\n"
						+ "    document_management dm\r\n" + "        LEFT JOIN\r\n"
						+ "    file_upload_config fc ON dm.UPLOAD_DOC_TYPE = fc.FU_CODE\r\n" + "        LEFT JOIN\r\n"
						+ "    document_type_mst mst ON fc.DOCUMENT_TYPE_CODE = mst.DOCUMENT_TYPE_CODE\r\n"
						+ "        LEFT JOIN\r\n" + "    file_manager fim ON dm.DM_ID = fim.REFERNCE_ID\r\n"
						+ "        LEFT JOIN\r\n" + "    employee_mst emp ON fim.FILE_CREATED_BY = emp.EMPLOYEE_ID\r\n"
						+ "WHERE\r\n" + "    ENQUIRY_ID = ? AND APPROVED = ?\r\n" + "        AND dm.TENANT_ID = ?\r\n"
						+ "        AND STAGE_CODE = ?\r\n" + "        AND LATEST_VERSION = '0' and DOC_TYPE_CODE=? \r\n"
						+ "        AND (UPLOAD_DOC_TYPE , dm.VERSION) IN (SELECT \r\n"
						+ "            UPLOAD_DOC_TYPE, MAX(dm.VERSION)\r\n" + "        FROM\r\n"
						+ "            document_management dm where ENQUIRY_ID='" + enquiryId
						+ "'        GROUP BY UPLOAD_DOC_TYPE)\r\n" + "ORDER BY DM_ID DESC;";
			}

			list = this.jdbcTemplate.query(getDocList, new ApprovedDocRowMapper(), enquiryId, approved, tenantId,
					stageCode,docType);

		} catch (Exception ex) {
			logger.error("getApprovedDocDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<DocumentAppStatusDtlEntity> getDocStatusList(String dmId, String tenantId) {
		List<DocumentAppStatusDtlEntity> appList = new ArrayList<DocumentAppStatusDtlEntity>();
		try {

			String getApprovalList = "SELECT \r\n" + "    dtl.DAS_ID,\r\n" + "    dtl.DM_ID,\r\n"
					+ "    dtl.SEQUENCE_NO,\r\n" + "    dtl.SEQUENCE_STATUS,\r\n" + "    dtl.UPDATED_ON,\r\n"
					+ "    mst.EMPLOYEE_FIRSTNAME as UPDATED_BY,\r\n" + "    dtl.TENANT_ID,\r\n"
					+ "    st.DOCUMENT_STATUS_TYPE_DESCRIPTION,dm.VERSION\r\n" + "FROM\r\n"
					+ "    document_app_status_dtl dtl\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst mst ON dtl.UPDATED_BY = mst.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code st ON dtl.SEQUENCE_STATUS = st.DOCUMENT_STATUS_TYPE_CODE INNER JOIN document_management dm on dm.DM_ID = dtl.DM_ID \r\n"
					+ "WHERE\r\n" + "    dtl.DM_ID = ? and dtl.TENANT_ID=?";
			appList = this.jdbcTemplate.query(getApprovalList, new DocumentAppStatusDtlRowMapper(), dmId, tenantId);

		} catch (Exception ex) {
			logger.error("getDocStatusList Error" + ex);
		}
		return appList;
	}

	public List<ApprovedDocEntity> getAllDmId(String tenantId, String documentName, String refId, String stageCode) {
		List<ApprovedDocEntity> list = new ArrayList<ApprovedDocEntity>();
		String getDocList = "";
		try {
			getDocList = "SELECT \r\n" + "    fc.DOCUMENT_TYPE_CODE,\r\n" + "    mst.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dm.DM_ID,\r\n" + "    dm.UPLOAD_DOC_TYPE,\r\n" + "    fc.DESCRIPTION AS UPLOAD_DOCUMENT,\r\n"
					+ "    dm.VERSION,\r\n" + "    emp.EMPLOYEE_FIRSTNAME,\r\n"
					+ "    dm.DOCUMENT_NAME,dm.REMARKS,fim.FILE_CREATED_DATE,dm.REFERENCE_ID,dm.STAGE_CODE,fim.FILE_ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME,"
					+ "( CASE\r\n" + 
					"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END)AS IS_PDF \r\n"
					+ "    \r\n" + "FROM\r\n" + "    document_management dm\r\n" + "        Left JOIN\r\n"
					+ "    file_upload_config fc ON dm.UPLOAD_DOC_TYPE = fc.FU_CODE\r\n" + "        Left JOIN\r\n"
					+ "    document_type_mst mst ON fc.DOCUMENT_TYPE_CODE = mst.DOCUMENT_TYPE_CODE\r\n"
					+ "        left JOIN\r\n" + "    file_manager fim ON dm.DM_ID = fim.REFERNCE_ID\r\n"
					+ "    left join employee_mst emp on fim.FILE_CREATED_BY=emp.EMPLOYEE_ID\r\n" + "WHERE\r\n"
					+ "    dm.UPLOAD_DOC_TYPE = ?\r\n" + "        AND dm.REFERENCE_ID = ?\r\n"
					+ "        AND dm.TENANT_ID = ?\r\n" + "        AND dm.STAGE_CODE = ? order by dm.DM_ID";

			list = this.jdbcTemplate.query(getDocList, new ApprovedDocRowMapper(), documentName, refId, tenantId,
					stageCode);

		} catch (Exception ex) {
			logger.error("getApprovedDocDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<DocumentManagementTblEntity> getDocDlsByDmId(String dmId, String tenantId, String approve ) {
		List<DocumentManagementTblEntity> list = new ArrayList<DocumentManagementTblEntity>();
		try {
			if ( approve.equalsIgnoreCase("0") ) {
				String getDocDtls = "select * from document_management where DM_ID='" + dmId + "'";
				list = this.jdbcTemplate.query(getDocDtls, new DocumentManagementTblRowMapper());
			}else {
				String getDocDtls = "select * from document_management where DM_ID='" + dmId + "' and LATEST_VERSION=1;";
				list = this.jdbcTemplate.query(getDocDtls, new DocumentManagementTblRowMapper());
			} 
				
		} catch (Exception ex) {
			logger.error("getDocDlsByDmId Error" + ex);
		}
		return list;
	}

	@Override
	public List<ApprovedDocEntity> getDocDtlsByCombination(String enquiryId, String projectId, String documentName,
			String refId, String stageCode, String tenantId) {
		List<ApprovedDocEntity> list = new ArrayList<ApprovedDocEntity>();
		try {
			String getDocDtls = "SELECT \r\n" + "    fc.DOCUMENT_TYPE_CODE,\r\n"
					+ "    mst.DOCUMENT_TYPE_DESCRIPTION,\r\n" + "    dm.DM_ID,\r\n" + "    dm.UPLOAD_DOC_TYPE,\r\n"
					+ "    fc.DESCRIPTION AS UPLOAD_DOCUMENT,\r\n" + "    dm.VERSION,\r\n"
					+ "   emp.EMPLOYEE_FIRSTNAME,\r\n" + "    dm.DOCUMENT_NAME,dm.REMARKS,fim.FILE_CREATED_DATE,fim.FILE_ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME,"
					+ "( CASE\r\n" + 
					"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END)AS IS_PDF  \r\n"
					+ "    \r\n" + "FROM\r\n" + "    document_management dm\r\n" + "        left JOIN\r\n"
					+ "    file_upload_config fc ON dm.UPLOAD_DOC_TYPE = fc.DOCUMENT_TYPE_CODE\r\n" + "	left JOIN\r\n"
					+ "    document_type_mst mst ON fc.DOCUMENT_TYPE_CODE = mst.DOCUMENT_TYPE_CODE\r\n"
					+ "        left JOIN\r\n" + "    file_manager fim ON dm.DM_ID = fim.REFERNCE_ID\r\n"
					+ "    left join employee_mst emp on fim.FILE_CREATED_BY=emp.EMPLOYEE_ID\r\n" + "WHERE\r\n"
					+ "    ENQUIRY_ID = ? AND PROJECT_ID = ?\r\n" + "        AND DOCUMENT_NAME = ?\r\n"
					+ "        AND REFERENCE_ID = ?\r\n" + "        AND STAGE_CODE = ?\r\n"
					+ "        AND dm.TENANT_ID=? ;";
			list = this.jdbcTemplate.query(getDocDtls, new ApprovedDocRowMapper(), enquiryId, projectId, documentName,
					refId, stageCode, tenantId);

		} catch (Exception ex) {
			logger.error("getDocDtlsByCombination Error" + ex);
		}
		return list;
	}

	@Override
	public int getLatestVersionbycomb(String tenantId, String documentName, String refId, String stageCode) {
		int version = 0;
		try {
			String getLatestVersion = "SELECT \r\n" + "    VERSION\r\n" + "FROM\r\n" + "    document_management\r\n"
					+ "WHERE\r\n" + "         UPLOAD_DOC_TYPE = ? \r\n"
					+ "        AND REFERENCE_ID = ? \r\n" + "        AND STAGE_CODE =  ? \r\n"  
				    + "        AND TENANT_ID = ? \r\n" + "ORDER BY VERSION DESC\r\n"
					+ "LIMIT 1";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getLatestVersion, documentName, refId, stageCode, tenantId);
			version = Integer.parseInt(resultData.get("VERSION").toString());

		} catch (Exception ex) {
			logger.error("getLatestVersionbycomb Error" + ex);
		}
		return version;
	}

	@Override
	public int getCountByComb(String tenantId, String documentName, String refId, String stageCode) {
		int count = 0;
		try {
			String getCount = "SELECT \r\n" + "    COUNT(*) AS COUNT \r\n" + "FROM\r\n" + "    document_management\r\n"
					+ "WHERE\r\n" + "         UPLOAD_DOC_TYPE = ?\r\n"
					+ "        AND REFERENCE_ID = ?\r\n" + "        AND STAGE_CODE = ?\r\n"  
				    + "        AND TENANT_ID = ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getCount, documentName, refId, stageCode, tenantId);
			count = Integer.parseInt(resultData.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getCountByComb Error" + ex);
		}
		return count;
	}

	@Override
	public List<FileUploadConfigtblEntity> getFileDtlsByDocTypeCode(String docTypeCode, String tenantId) {
		List<FileUploadConfigtblEntity> list = new ArrayList<FileUploadConfigtblEntity>();
		try {
			String getDocDtls = "select * from file_upload_config where DOCUMENT_TYPE_CODE=? and TENANT_ID=?";
			list = this.jdbcTemplate.query(getDocDtls, new FileUploadConfigtblRowMapper(), docTypeCode, tenantId);

		} catch (Exception ex) {
			logger.error("getFileDtlsByDocTypeCode Error" + ex);
		}
		return list;
	}
	public List<FileUploadConfigtblEntity> getFileDtlsByFileUploadCode(String fuCode, String tenantId) {
		List<FileUploadConfigtblEntity> list = new ArrayList<FileUploadConfigtblEntity>();
		try {
			String getDocDtls = "select * from file_upload_config where FU_CODE=? and TENANT_ID=?";
			list = this.jdbcTemplate.query(getDocDtls, new FileUploadConfigtblRowMapper(), fuCode, tenantId);

		} catch (Exception ex) {
			logger.error("getFileDtlsByFileUploadCode Error" + ex);
		}
		return list;
	}

	@Override
	public int getFileDtlCountByDocTypeCode(String docTypeCode, String tenantId) {
		int count = 0;
		try {
			String getDocDtls = "select count(*) as COUNT from file_upload_config where DOCUMENT_TYPE_CODE= ? \r\n" 
					+ " and TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getDocDtls, docTypeCode, tenantId);
			count = Integer.parseInt(resultData.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getFileDtlCountByDocTypeCode Error" + ex);
		}
		return count;

	}

	@Override
	public int insertDocumentDtls(String enquiryId, String projectId, String documentName, String refId,
			String stageCode, String uploadDocType, int version, String tenantId, String remarks, String docApprSeq,
			String lastSeq, String docType) {
		int dmId = 0;
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO document_management ( ENQUIRY_ID, PROJECT_ID, DOCUMENT_NAME, REFERENCE_ID, STAGE_CODE, UPLOAD_DOC_TYPE, VERSION, REMARKS, DOC_APPR_SEQ,TENANT_ID,APPROVED,LATEST_VERSION,DOC_TYPE_CODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, (enquiryId == null || enquiryId.trim().isEmpty()) ? null : enquiryId);
					ps.setString(2, projectId);
					ps.setString(3, documentName);
					ps.setString(4, refId);
					ps.setString(5, stageCode);
					ps.setString(6, uploadDocType);
					ps.setInt(7, version);
					ps.setString(8, remarks);
					ps.setString(9, docApprSeq);
					ps.setString(10, tenantId);
					ps.setString(11, lastSeq);
					ps.setString(12, lastSeq);
					ps.setString(13, docType);
					return ps;
				}
			}, holder);

			dmId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertDocumentDtls Error" + ex);
		}
		return dmId;
	}

	@Override
	public String getDepCodeByEmpId(String empId, String tenantId) {
		String depCode = "";
		try {
			String getDepCode = "select DEPARTMENT_CODE from employee_mst where EMPLOYEE_ID=? \r\n"  
					+ " and TENANT_ID= ?;";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getDepCode, empId, tenantId);
			depCode = resultData.get("DEPARTMENT_CODE").toString();

		} catch (Exception ex) {
			logger.error("getDepCodeByEmpId Error" + ex);
		}
		return depCode;

	}

	@Override
	public int insertDocMagAccessDtl(int newDmId, String depCode, String tenantId) {
		int insertCheck = 0;
		try {
			String insertQry = "INSERT INTO document_management_access (DM_ID, DEPARTMENT_CODE, ENABLED_DATETIME, TENANT_ID) VALUES (?, ?, ?, ?)";
			insertCheck = this.jdbcTemplate.update(insertQry, newDmId, depCode, CommonMethod.getCurrentDateTime(),
					tenantId);

		} catch (Exception ex) {
			logger.error("insertDocMagAccessDtl Error" + ex);
		}
		return insertCheck;

	}

	@Override
	public int insertNewFileDtl(MultipartFile file, String tenantId, int dmId, String uploadDocType, String empId,
			int version, String documentType, String type, String refId) {
		// type can be either Sales or Projects
		// refId can be enquiry id or project id
		int res = 0;
		try {

			byte[] bytes = file.getBytes();
			String getFilePath = GetPropertyValue.getPropValue("DOC_SAVED_PATH", tenantId, jdbcTemplate);
			logger.info("file_absolute_name" + file.getName());
			String fileName = CommonMethod.getCurrentDateTimeStamp()
					+ FilenameUtils.getName(file.getOriginalFilename());
			String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // Extension

			String file_absolute_name = file.getOriginalFilename();// without extension

			logger.info("file_absolute_name" + file.getOriginalFilename());
			String newFilePath = getFilePath + File.separator + type + File.separator + refId + File.separator
					+ documentType + File.separator + uploadDocType; // + File.separator +"file_upload_config";

			Path path = Paths.get(newFilePath + File.separator + fileName);
			logger.info("path-->" + path);

			File UPLOADED_FOLDER = new File(newFilePath);

			if (!UPLOADED_FOLDER.exists()) {
				UPLOADED_FOLDER.mkdirs();
			}
			Files.write(path, bytes);

			String qry = "insert into file_manager(REFERNCE_ID,FILE_NAME,FILE_NAME_EXTN,FILE_ABSOLUTE_NAME,FILE_CREATED_DATE,FILE_CREATED_BY,FILE_PATH,FILE_FORMAT,FILE_CATEGORY_CODE,VERSION,TENANT_ID) values (?,?,?,?,?,?,?,?,?,?,?)";
			res = this.jdbcTemplate.update(qry, dmId, fileName, extension, file_absolute_name,
					CommonMethod.getCurrentDateTime(), empId, newFilePath, "file", uploadDocType, version, tenantId);

		} catch (Exception ex) {
			logger.error("insertNewFileDtl Error" + ex);
		}
		return res;
	}

	@Override
	public List<DocumentStatusMstEntity> getSeqAndStatusByDocTypeCode(String docTypeCode, String tenantId) {
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		try {
			String qry = "select * from document_lifecycle_mst where TENANT_ID='" + tenantId + "' and DOC_TYPE='"
					+ docTypeCode + "' order by CURR_SEQUENCE limit 1";
			list = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
		} catch (Exception ex) {
			logger.error("getSeqAndStatusByDocTypeCode Error" + ex);
		}
		return list;
	}

	@Override
	public String getDocTypeCodeByDocDesc(String docType, String tenantId) {
		String docCode = "";
		try {
			String getDocCode = "select DOCUMENT_TYPE_CODE from document_type_mst where  DOCUMENT_TYPE_DESCRIPTION= ? \r\n"
					  + " and TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getDocCode, docType, tenantId);
			docCode = resultData.get("DOCUMENT_TYPE_CODE").toString();

		} catch (Exception ex) {
			logger.error("getDocTypeCodeByDocDesc Error" + ex);
		}
		return docCode;

	}

	@Override
	public int insertDocAppStatusDtl(int newDmId, String docAppSeq, String tenantId, String seqStatus, String empId) {
		int insertCheck = 0;
		try {
			String insertQry = "INSERT INTO document_app_status_dtl (DM_ID, SEQUENCE_NO, SEQUENCE_STATUS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?)";
			insertCheck = this.jdbcTemplate.update(insertQry, newDmId, docAppSeq, seqStatus, empId,
					CommonMethod.getCurrentDateTime(), tenantId);

		} catch (Exception ex) {
			logger.error("insertDocAppStatusDtl Error" + ex);
		}
		return insertCheck;

	}

	@Override
	public int checkForNextSeq(int currentSeq, String docType, String tenantId) {
		int count = 0;
		try {
			String qry = "select count(*) as COUNT from document_lifecycle_mst where DOC_TYPE= ? and TENANT_ID= ? \r\n"
					 + " and CURR_SEQUENCE > ? order by CURR_SEQUENCE limit 1;";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(qry, docType, tenantId, currentSeq);
			count = Integer.parseInt(resultData.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("checkForNextSeq Error" + ex);
		}
		return count;

	}

	@Override
	public List<DocumentStatusMstEntity> getNextSeqandStatus(int currentSeq, String docType, String tenantId) {
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		try {
			String qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='"
					+ tenantId + "' and CURR_SEQUENCE >'" + currentSeq + "' order by CURR_SEQUENCE limit 1;";
			list = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
		} catch (Exception ex) {
			logger.error("getNextSeqandStatus Error" + ex);
		}
		return list;
	}

	@Override
	public List<DocumentStatusMstEntity> getPrevSeqandStatus(int currentSeq, String docType, String tenantId) {
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		try {
			String qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='"
					+ tenantId + "' and CURR_SEQUENCE <'" + currentSeq + "' order by CURR_SEQUENCE limit 1;";
			list = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
		} catch (Exception ex) {
			logger.error("getPrevSeqandStatus Error" + ex);
		}
		return list;
	}

	@Override
	public String getDesigCodeByEmpId(String empId, String tenantId) {
		String depCode = "";
		try {
			if (!tenantId.isEmpty()) {
				String getDepCode = "select DESIGNATION_CODE from employee_mst where EMPLOYEE_ID= ? \r\n"  
						+ " and TENANT_ID= ?;";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(getDepCode, empId, tenantId);
				depCode = resultData.get("DESIGNATION_CODE").toString();		
				} else {
				String getDepCode = "select DESIGNATION_CODE from employee_mst where EMPLOYEE_ID= ? ";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(getDepCode, empId);
				depCode = resultData.get("DESIGNATION_CODE").toString();
			}
		} catch (Exception ex) {
			logger.error("getDesigCodeByEmpId Error" + ex);
		}
		return depCode;

	}

	@Override
	public int getApprovebtnEnableStatus(String designCode, String tenantId, String docTypeCode, String seq) {
		int status = 0;
		try {
			String getStatus = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN 1\r\n"
					+ "        ELSE 0\r\n" + "    END AS result\r\n" + "FROM\r\n" + "    document_lifecycle_mst\r\n"
					+ "WHERE\r\n" + "    FIND_IN_SET(? , APPR_DESI) > 0\r\n"
					+ "        AND DOC_TYPE = ? \r\n" + "        AND CURR_SEQUENCE = ? " 
					+ "\r\n" + "        AND TENANT_ID = ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getStatus, designCode, docTypeCode, seq, tenantId);
			status = Integer.parseInt(resultData.get("result").toString());
		} catch (Exception ex) {
			logger.error("getApprovebtnEnableStatus Error" + ex);
		}
		return status;

	}

	@Override
	public int getCurrentSeqbyDmId(String dmId, String tenantId) {
		int seq = 0;
		try {
			String getCurrentSeq = "select case when count(*)>0 then DOC_APPR_SEQ else 0 end as count from document_management where DM_ID= ? \r\n"
					 + " and TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getCurrentSeq, dmId, tenantId);
			seq = Integer.parseInt(resultData.get("count").toString());
		} catch (Exception ex) {
			logger.error("getCurrentSeqbyDmId Error" + ex);
		}
		return seq;
	}

	@Override
	public int updateNextSeqInDmTbl(String dmId, String tenantId, String currSequence) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE document_management SET DOC_APPR_SEQ= ? WHERE DM_ID= ?" 
					+ " and TENANT_ID= ? ";
			updateStatus = this.jdbcTemplate.update(qry, currSequence, dmId, tenantId);

		} catch (Exception ex) {
			logger.error("updateNextSeqInDmTbl Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateLatestVersion(String tenantId, String dmId, String uploadDocType, String enqId) {
		int updateStatus = 0;
		try {
			String qry = "update document_management set LATEST_VERSION='0' where UPLOAD_DOC_TYPE= ?" 
					+ " and TENANT_ID= ? AND DM_ID <> ? AND ENQUIRY_ID = ? ";
			updateStatus = this.jdbcTemplate.update(qry, uploadDocType, tenantId, dmId,enqId);

		} catch (Exception ex) {
			logger.error("updateLatestVersion Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateApprAndLatest(String tenantId, String dmId) {
		int updateStatus = 0;
		try {
			String qry = "update document_management set LATEST_VERSION='1' , APPROVED='1' where TENANT_ID= ?" 
					+ " AND DM_ID = ?";
			updateStatus = this.jdbcTemplate.update(qry, tenantId, dmId);

		} catch (Exception ex) {
			logger.error("updateApprAndLatest Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public String getUploadDocTypeByDmId(String dmId, String tenantId) {
		String uploadDocType = "";
		try {
			String getCode = "select UPLOAD_DOC_TYPE from document_management where DM_ID= ? and TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getCode, dmId, tenantId);
			uploadDocType = resultData.get("UPLOAD_DOC_TYPE").toString();
		} catch (Exception ex) {
			logger.error("getUploadDocTypeByDmId Error" + ex);
		}
		return uploadDocType;
	}
	
	@Override
	public String getDocumentNameByDmId(int dmId, String tenantId) {
		String documentName = "";
		try {
			String getDocumentName = "select DOCUMENT_NAME from document_management where DM_ID= ? and TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getDocumentName, dmId, tenantId);
			documentName = resultData.get("DOCUMENT_NAME").toString();
		} catch (Exception ex) {
			logger.error("getDocumentNameByDmId Error" + ex);
		}
		return documentName;
	}

	@Override
	public String getMasterDoc(String depCode, String tenantId,String pmId) {
		String masterDoc = "";
		try {

			String getCode = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN DEPARTMENT_ASSIGNED\r\n"
					+ "        ELSE 'NA'\r\n" + "    END AS DEPARTMENT_ASSIGNED\r\n" + "FROM\r\n"
					+ "    project_wbs_initiation_mst\r\n" + "WHERE\r\n" + "    FIND_IN_SET(?, \r\n"  
					+ " DEPARTMENT_ASSIGNED)\r\n" + "        AND TENANT_ID = ? AND PM_ID = ? \r\n"
					+ "GROUP BY DEPARTMENT_ASSIGNED";

//			String getCode = "select case when count(*) > 0 then DEPARTMENT_ASSIGNED else NA end from project_wbs_initiation_mst where FIND_IN_SET('" + depCode
//					+ "',DEPARTMENT_ASSIGNED) and TENANT_ID='" + tenantId + "'";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getCode, depCode, tenantId, pmId);
			masterDoc = resultData.get("DEPARTMENT_ASSIGNED").toString();
		} catch (Exception ex) {
			logger.error("getMasterDoc Error" + ex);
		}
		return masterDoc;
	}

	@Override
	public String getDocTypeCodeBystgAndpmId(String stgCode, String pmId) {
		String docTypeCode = "";
		try {

			String docTypeCodeStr = "select case when count(*)>0 then DOCUMENT_TYPE_CODE else '' end AS docType from document_type_mst where  STG_CODE =? and PM_ID =? ";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(docTypeCodeStr, stgCode, pmId);
			docTypeCode = resultData.get("docType").toString();
		} catch (Exception ex) {
			logger.error("getDocTypeCodeBystgAndpmId Error" + ex);
		}
		return docTypeCode;
	}

	@Override
	public String getCurrStageByRefId(String masterId) {
		String StageCode = "";
		try {

			String getCode = "select TRANSACTION_STAGE from sales_enq_hdr where SE_ID = ? ";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getCode, masterId);
			StageCode = resultData.get("TRANSACTION_STAGE").toString();
		} catch (Exception ex) {
			logger.error("getCurrStageByRefId Error" + ex);
		}
		return StageCode;
	}

	@Override
	public int getChangeRequestInfoCheck(String sbcHdrId, String tenantId) {
		int changeHdrCountCheck = 0;
		try {
			String changeHdrCountCheckQry = "SELECT \r\n" + "   count(*) as COUNT\r\n" + "FROM\r\n"
					+ "    sales_budget_sheet_hdr hdr\r\n" + "        left JOIN\r\n"
					+ "    sales_budget_change bsChange ON hdr.SB_HDR_ID = bsChange.SB_HDR_ID\r\n" + "WHERE\r\n"
					+ "    hdr.MASTER_ID = ? and hdr.TENANT_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(changeHdrCountCheckQry, sbcHdrId, tenantId);
			changeHdrCountCheck = Integer.parseInt(resultData.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getChangeRequestInfoCheck Error" + ex);
		}
		return changeHdrCountCheck;
	}

	@Override
	public List<ChangeRequestHdrInfoEntity> getChangeRequestInfo(String sbcHdrId, String tenantId) {
		List<ChangeRequestHdrInfoEntity> list = new ArrayList<ChangeRequestHdrInfoEntity>();
		try {
			String qry = "SELECT \r\n" + "    MASTER_ID,\r\n" + "    TOTAL_BUDGET_COST,\r\n"
					+ "    FINAL_SALE_VALUE,\r\n" + "    BUDGET_COST,\r\n" + "    CR_COST,\r\n"
					+ "    bsChange.REMARKS,\r\n" + "    bsChange.CR_VALUE,\r\n" + "    bsChange.CR_DATETIME\r\n, bsChange.SB_HDR_ID,bsChange.SBC_ID\r\n"
					+ "FROM\r\n" + "    sales_budget_sheet_hdr hdr\r\n" + "        LEFT JOIN\r\n"
					+ "    sales_budget_change bsChange ON hdr.SB_HDR_ID = bsChange.SB_HDR_ID\r\n" + "WHERE\r\n"
					+ "    hdr.MASTER_ID = '" + sbcHdrId + "'\r\n" + "        AND hdr.TENANT_ID = '" + tenantId + "'";

			list = this.jdbcTemplate.query(qry, new ChangeRequestHdrInfoRowMapper());

		} catch (Exception ex) {
			logger.error("getChangeRequestInfo Error" + ex);
		}
		return list;
	}

	@Override
	public List<DocumentManagementTblEntity> getAccDocDlsByDmId(String dmId, String tenantId, String approve) {
		List<DocumentManagementTblEntity> list = new ArrayList<DocumentManagementTblEntity>();
		try {
			if ( approve.equalsIgnoreCase("0") ) {
				String getDocDtls = "select * from document_management where DM_ID='" + dmId + "' and LATEST_VERSION=1;";
				list = this.jdbcTemplate.query(getDocDtls, new DocumentManagementTblRowMapper());
			}
				
		} catch (Exception ex) {
			logger.error("getDocDlsByDmId Error" + ex);
		}
		return list;
	}
}