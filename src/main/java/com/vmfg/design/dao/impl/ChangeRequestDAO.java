package com.vmfg.design.dao.impl;

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
import java.util.stream.Collectors;

import com.vmfg.design.entity.*;
import com.vmfg.design.rowmapper.*;
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

import com.vmfg.design.dao.interfaces.IChangeRequestDAO;
import com.vmfg.design.response.KeyAreaIndentId;
import com.vmfg.design.response.KeySubArea;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class ChangeRequestDAO implements IChangeRequestDAO{
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ChangeRequestHdrEntity> ChangereqHdrList(String pmId, String tenantId) {
		List<ChangeRequestHdrEntity> list = new ArrayList<ChangeRequestHdrEntity>();
		try {
			String crhdrStr = "SELECT \r\n" + 
					"    dp.DEPARTMENT_NAME AS INITIATED_BY_DESC,\r\n" + 
					"    ph.PROJECT_CODE,\r\n" + 
					"    ph.PROJECT_NAME,\r\n" + 
					"    pkm.PK_DESC,\r\n" + 
					"    pskm.PSK_DESC,\r\n" + 
					"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME AS CREATED_BY_DESC,\r\n" + 
					"    em1.EMPLOYEE_FIRSTNAME AS LAST_UPDATED_BY_DESC,\r\n" + 
					"    crh.*,pd.PRODUCT_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    change_request_hdr crh\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr ph ON ph.PM_HDR_ID = crh.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"       project_key_area pk ON pk.PKA_ID = crh.PK_ID INNER JOIN project_key_area_mst pkm ON pkm.PK_ID  = pk.PK_ID \r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area psk ON psk.PKSA_ID = crh.PSK_ID INNER JOIN project_key_sub_area_mst pskm ON pskm.PSK_ID = psk.PSK_ID \r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = crh.TRANSACTION_STATUS\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst em ON em.EMPLOYEE_ID = crh.CREATED_BY\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst em1 ON em1.EMPLOYEE_ID = crh.LAST_UPDATED_BY\r\n" + 
					"        INNER JOIN\r\n" + 
					"    department dp ON dp.DEPARTMENT_CODE = crh.INITIATED_BY INNER JOIN\r\n" + 
					"	product_mst pd on pd.PRODUCT_CODE = crh.PRODUCT_CODE and crh.PM_HDR_ID=pd.PM_HDR_ID \r\n" + 
					"WHERE\r\n" + 
					"    crh.PM_HDR_ID = ? \r\n" + 
					"        AND crh.TENANT_ID =? ";
			list  = this.jdbcTemplate.query(crhdrStr, new ChangeRequestHdrRowMapper(),pmId,tenantId);
		}catch(Exception ex) {
			logger.error("ChangereqHdrList Error" + ex);
		}
		return list;
	}

	@Override
	public List<ChangeRequestDtlEntity> ChangereqDtlList(String ceHdrid,String tenantId) {
		List<ChangeRequestDtlEntity> list = new ArrayList<ChangeRequestDtlEntity>();
		try {
			String crhdrStr = "SELECT \r\n" + 
					"    cr.*,emp.EMPLOYEE_FIRSTNAME\r\n" + 
					"FROM\r\n" + 
					"    change_request_dtl cr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON cr.EMPLOYEE_ID = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    cr.CR_ID = ? and cr.TENANT_ID=? ";
			list  = this.jdbcTemplate.query(crhdrStr, new ChangeRequestDtlRowMapper(),ceHdrid,tenantId);
		}catch(Exception ex) {
			logger.error("ChangereqHdrList Error" + ex);
		}
		return list;
	}

	@Override
	public String designationDesc(String desigCode) {
		String desigDesc ="";
		try {
			String crhdrStr = "select   case when count(*) > 0 then DESIGNATION_NAME  else '' end AS DESIG_NAME from employee_designation where DESIGNATION_CODE= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(crhdrStr,desigCode);
			desigDesc = resultMap.get("DESIG_NAME").toString();
		}catch(Exception ex) {
			logger.error("designationDesc Error" + ex);
		}
		return desigDesc;
	}

	@Override
	public int insertChangeRequestHdr( String deHdId, String pmHdrId, String initiatedBy,
			String crDate, String productCode, String pkId, String pskId, String requestDetail,
			String nextApprovingDesig, String status, String seq, String updateDrawNo, String udateDrawRevNo,
			String createdBy, String lastUpdatedBy, String tenantId ) {
		int insertCRHdr = 0;
//		String crNo, crCode,financialMstId="";
		try {
			
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(crDate, tenantId, "change_request_hdr", "2",
					jdbcTemplate,1,0,null,0);
			
			String insertsalesDtlStr="INSERT INTO `change_request_hdr` ( `TRANSACTION_NO`, `CR_CODE`, `DE_HDR_ID`, `PM_HDR_ID`, `INITIATED_BY`, `CR_DATE`, `PRODUCT_CODE`, `PK_ID`, `PSK_ID`, `REQUEST_DETAILS`, `NEXT_APPROVING_DESIG`, `IS_COMPLETED`, `IS_APPROVED`, `TRANSACTION_STATUS`, `TRANSACTION_STATUS_SEQ`, `UPDATED_DRAWING_NO`, `UPDATED_DRAWING_REV_NO`, `APPROVED_ON`, `CREATED_BY`, `CREATED_ON`, `LAST_UPDATED_BY`, `LAST_UPDATE_ON`, `TENANT_ID`,`FINANCIAL_YEAR_MST_ID`) VALUES (?,?,?,?,?, ?,?,?, ?,?, ?,?,?, ?,?,?,?,?,?,?,?,?, ?,?) ";
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertsalesDtlStr, Statement.RETURN_GENERATED_KEYS);

					ps.setInt(1, gen.getSeq());
					ps.setString(2, gen.getEnquiryCode());
					ps.setString(3, deHdId);
					ps.setString(4, pmHdrId);
					ps.setString(5, initiatedBy);
					ps.setString(6, crDate);
					ps.setString(7, productCode);
					ps.setString(8, pkId);
					ps.setString(9, pskId);
					ps.setString(10, requestDetail);
					ps.setString(11, nextApprovingDesig);
					ps.setString(12, "0");
					ps.setString(13, "0");
					ps.setString(14, status);
					ps.setString(15, seq);
					ps.setString(16, updateDrawNo);
					ps.setString(17, udateDrawRevNo);
					ps.setString(18, CommonMethod.getCurrentDateTime());
					ps.setString(19, createdBy);
					ps.setString(20, CommonMethod.getCurrentDateTime());
					ps.setString(21, lastUpdatedBy);
					ps.setString(22, CommonMethod.getCurrentDateTime());
					ps.setString(23, tenantId);
					ps.setString(24, gen.getFinainceId());
					return ps;
				}

			}, holder);
			insertCRHdr = holder.getKey().intValue();	
		}catch(Exception ex) {
			logger.error("insertChangeRequestHdr Error "+ex);
		}
		return insertCRHdr;
	}

	@Override
	public int updateChangeRequestHdr(String deHdId, String pmHdrId, String initiatedBy, String crDate,
			String productCode, String pkId, String pskId, String requestDetail, String nextApprovingDesig,
			String updateDrawNo, String udateDrawRevNo, String lastUpdatedBy, String tenantId, String crhdrId) {
		int updateCrHdr=0;
		try {
			String updateCrHdrStr="UPDATE `change_request_hdr` SET `DE_HDR_ID`=?, `PM_HDR_ID`=?, `INITIATED_BY`=?, `CR_DATE`=?, `PRODUCT_CODE`=?, `PK_ID`=?, `PSK_ID`=?, `REQUEST_DETAILS`=?, `NEXT_APPROVING_DESIG`=?, `UPDATED_DRAWING_NO`=?, `UPDATED_DRAWING_REV_NO`=?,  `LAST_UPDATED_BY`=?,`LAST_UPDATE_ON`=? WHERE `CR_ID`=? ";
			updateCrHdr = this.jdbcTemplate.update(updateCrHdrStr,deHdId,pmHdrId,initiatedBy,crDate,productCode,pkId,pskId,requestDetail,nextApprovingDesig,updateDrawNo,udateDrawRevNo,lastUpdatedBy,CommonMethod.getCurrentDateTime(),crhdrId);
		}catch(Exception ex) {
			logger.error("updateEnqHdr Error" + ex);
		}
		return updateCrHdr;
	}


	
	@Override
	public String getStatusDescByStatusCode(String docStatusCode, String tenantId) {
		String desc="";
		try {
			String getDesc="select DOCUMENT_STATUS_TYPE_DESCRIPTION from document_status_type_code where DOCUMENT_STATUS_TYPE_CODE= ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getDesc,docStatusCode);
			desc = resultMap.get("DOCUMENT_STATUS_TYPE_DESCRIPTION").toString();
		}catch(Exception ex) {
			logger.error("getStatusDescByStatusCode Error" + ex);
		}
		return desc;
	}

	@Override
	public int insertChangeRequestDtl(String crHdrId, String designedComment, String tenantId, String empId) {
		int insertCrDtl = 0;
		try {
			String insertCrDtlStr = "INSERT INTO `change_request_dtl` (`CR_ID`, `DESIGNER_COMMENTS`, `EMPLOYEE_ID`, `REPORTED_DATETIME`, `TENANT_ID`) VALUES (?, ?, ?, ?, ?)";
			insertCrDtl = jdbcTemplate.update(insertCrDtlStr, crHdrId, designedComment, empId, CommonMethod.getCurrentDateTime(), tenantId);
		} catch (Exception ex) {
			logger.error("insertChangeRequestDtl Error: " + ex.getMessage());
		}
		return insertCrDtl;
	}

	@Override
	public int updateChangeRequestDtl(String crDtlId, String crId, String designerComments, String tenantId, String empId) {
		int updateCrDtl = 0;
		try {
			String updateCrDtlStr = "UPDATE `change_request_dtl` SET `DESIGNER_COMMENTS`=?, `EMPLOYEE_ID`=?, `REPORTED_DATETIME`=?, `TENANT_ID`=? WHERE `CR_DTL_ID`=? AND `CR_ID`=?";
			updateCrDtl = jdbcTemplate.update(updateCrDtlStr, designerComments, empId, CommonMethod.getCurrentDateTime(), tenantId, crDtlId, crId);
		} catch (Exception ex) {
			logger.error("updateChangeRequestDtl Error: " + ex.getMessage());
		}
		return updateCrDtl;
	}

	
	
	@Override
	public int updateChangeReqHdrStatus(String crHdrId,String seq,String seqStatus,String nextApprdesig,String empId) {
		int updateStatus=0;
		try {
			String updateCrDtlStr="UPDATE change_request_hdr SET NEXT_APPROVING_DESIG=?, TRANSACTION_STATUS=?, TRANSACTION_STATUS_SEQ=?, LAST_UPDATED_BY=?, LAST_UPDATE_ON=? WHERE CR_ID=?";
			updateStatus = this.jdbcTemplate.update(updateCrDtlStr,nextApprdesig,seqStatus,seq,empId,CommonMethod.getCurrentDateTime(),crHdrId);
		}catch(Exception ex) {
			logger.error("updateEnqHdr Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int getDmIdVal(String cHdrId, String tenantId) {
		int dmId=0;
		try {
			String getCount="select case when count(*)>0 then DM_ID else 0 end as COUNT from document_management where REFERENCE_ID=? and UPLOAD_DOC_TYPE='FC016' and TENANT_ID=? order by VERSION desc limit 1;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,cHdrId,tenantId);
			dmId = Integer.parseInt(result.get("COUNT").toString());
			
		}catch (Exception ex) {
			logger.error("getIndentDtlIdByProductCode Method Exception --->" + ex);

		}
		return dmId;
	}
	
	
	@Override
	public int updateDocManagementByDmId(String dmId,String fileName) {
		int updateStatus=0;
		try {
			String qry="UPDATE document_management SET DOCUMENT_NAME=? WHERE DM_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,fileName,dmId);
		}catch(Exception ex) {
			logger.error("updateDocManagementByDmId Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateFileByDmId(MultipartFile file, String tenantId, String dmId, String uploadDocType,
			String documentType, String type) {
		int updateStatus=0;
		try {
			
			byte[] bytes = file.getBytes();
			String getFilePath=GetPropertyValue.getPropValue("DOC_SAVED_PATH", tenantId, jdbcTemplate);
			logger.info("file_absolute_name" + file.getName());
			String fileName=CommonMethod.getCurrentDateTimeStamp()+FilenameUtils.getName(file.getOriginalFilename());
			String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // Extension
			
			String file_absolute_name = file.getOriginalFilename();// without extension

			logger.info("file_absolute_name" + file.getOriginalFilename());
			String newFilePath=getFilePath+File.separator+type+File.separator+dmId+File.separator+ documentType+ File.separator +uploadDocType ; //+ File.separator +"file_upload_config";

			Path path = Paths.get(newFilePath+ File.separator + fileName);
			logger.info("path-->" + path);

			File UPLOADED_FOLDER = new File(newFilePath);

			if (!UPLOADED_FOLDER.exists()) {
				UPLOADED_FOLDER.mkdirs();
			}
			Files.write(path, bytes);
			
			String qry = "UPDATE file_manager SET FILE_NAME=?, FILE_NAME_EXTN=?, FILE_ABSOLUTE_NAME=?, FILE_CREATED_DATE=?, FILE_PATH=? WHERE REFERNCE_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, fileName, extension, file_absolute_name,
					CommonMethod.getCurrentDateTime(), newFilePath,dmId);
			
	
		}catch(Exception ex) {
			logger.error("updateEnqHdr Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<KeyAreaIndentId> getKeyAreaDtls(String productCode, String masterId, String tenantId) {
		List<KeyAreaIndentId> list = new ArrayList<KeyAreaIndentId>();
		try {
			String qry = "SELECT \r\n" + 
					"    hdr.PKA_ID, pkam.PK_DESC, hdr.INDENT_ID\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID INNER JOIN  project_key_area_mst pkam ON pkam.PK_ID=pka.PK_ID \r\n" + 
					"WHERE\r\n" + 
					"    PRODUCT_CODE = ?\r\n" + 
					"        AND hdr.TENANT_ID = ? and hdr.MASTER_ID=?\r\n" + 
					"GROUP BY hdr.PKA_ID";
			list = this.jdbcTemplate.query(qry, new KeyAreaIndentIdRowMapper(),productCode,tenantId,masterId);

		} catch (Exception ex) {
			logger.error("getKeyAreaDtls Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<KeySubArea> getSubKeyAreaDtls(String productCode, String masterId, String tenantId,String indentId) {
		List<KeySubArea> list = new ArrayList<KeySubArea>();
		try {
			String qry = "SELECT \r\n" + 
					"    hdr.PKSA_ID, pksam.PSK_DESC  \r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area psk ON hdr.PKSA_ID = psk.PKSA_ID INNER JOIN project_key_sub_area_mst pksam on pksam.PSK_ID=psk.PSK_ID \r\n" + 
					"WHERE\r\n" + 
					"    PRODUCT_CODE =?\r\n" + 
					"        AND hdr.TENANT_ID =?  and hdr.MASTER_ID=? AND hdr.INDENT_ID = ? \r\n" + 
					"GROUP BY hdr.PKSA_ID";
			list = this.jdbcTemplate.query(qry, new DesignSubAreaRowMapper(),productCode,tenantId,masterId,indentId);

		} catch (Exception ex) {
			logger.error("getSubKeyAreaDtls Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int getRevisionNoCount(String productCode, String masterId, String tenantId) {
		int revisionNo=0;
		try {
			String getCount="SELECT \r\n" + 
					"	count(*) AS COUNT \r\n" + 
					"FROM\r\n" + 
					"    change_request_hdr\r\n" + 
					"WHERE\r\n" + 
					"    PRODUCT_CODE = ?\r\n" + 
					"        AND DE_HDR_ID = ?\r\n" + 
					"        AND TENANT_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,productCode,masterId,tenantId);
			int countCheck = Integer.parseInt(result.get("COUNT").toString());
			if(countCheck>0) {
				
				String qry="SELECT \r\n" + 
						"	 UPDATED_DRAWING_REV_NO\r\n" + 
						"FROM\r\n" + 
						"    change_request_hdr\r\n" + 
						"WHERE\r\n" + 
						"    PRODUCT_CODE = ?\r\n" + 
						"        AND DE_HDR_ID = ?\r\n" + 
						"        AND TENANT_ID =?\r\n" + 
						"ORDER BY UPDATED_DRAWING_REV_NO DESC\r\n" + 
						"LIMIT 1;";
				
				Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(qry,productCode,masterId,tenantId);
				revisionNo = Integer.parseInt(resultMap.get("UPDATED_DRAWING_REV_NO").toString());
			}else {
				revisionNo=0;
			}
			
		} catch (Exception ex) {
			logger.error("getRevisionNoCount Method Exception --->" + ex);

		}
		return revisionNo;
	}
	
	@Override 
	public List<ChangeRequestIndentEntity> getChangeRequestByIndentId(String projectId, String tenantId) { 
		// TODO Auto-generated method stub List list = new ArrayList(); 
		List<ChangeRequestIndentEntity> list=null;
		try { String query = "SELECT \r\n" 
		        + " INDENT_ID, INDENT_CODE, TENANT_ID\r\n" 
				+ " FROM \r\n" + " indent_hdr\r\n" 
		        + " WHERE \r\n" + "PROJECT_ID = '"+projectId+"' "
		        + "AND TENANT_ID = '"+tenantId+"';"; 

		               list = this.jdbcTemplate.query(query, new ChangeReqByIndentIdRowMapper()); 
		                 
		    }catch (Exception e) { 
		        logger.error("getChangeRequestByIndentId Dao Error" + e); 
		        } 
		    return list; 
		} 
	
	@Override 
	public List<ChangeRequestIndentDtlEntity> getChangeRequestByIndentDtlId(String indentId, String tenantId) { 
	    // TODO Auto-generated method stub 
	    List<ChangeRequestIndentDtlEntity> list = new ArrayList<ChangeRequestIndentDtlEntity>(); 
	    try { 
	        String query = "SELECT \r\n" 
	                + " INDENT_DTL_ID, PRODUCT_CODE, DESCRIPTION, TENANT_ID\r\n" 
	                + " FROM \r\n" 
	                + " indent_dtl\r\n" 
	                + " WHERE \r\n" 
	                + "INDENT_ID = '"+indentId+"' AND  TENANT_ID = '"+tenantId+"';";     
	                list = this.jdbcTemplate.query(query, new ChangeRequestIndentDtlRowMapper()); 
	                 
	    }catch (Exception e) { 
	        logger.error("getChangeRequestByIndentDtlId Dao Error" + e); 
	        } 
	    return list; 
	}


	@Override
	public IndentPartDetailsEntity getIndentDetailsByIndentCode(Integer indentId) {

		String query = "SELECT hdr.INDENT_ID, hdr.INDENT_CODE, "
				+ "ka.PK_ID AS stationId, ka.PK_DESC AS stationNo, "
				+ "sa.PSK_ID AS subAssyId, sa.PSK_DESC AS subAssyDesc, "
				+ "dtl.PRODUCT_CODE AS partNo, dtl.DESCRIPTION AS partDesc "
				+ "FROM indent_hdr hdr "
				+ "JOIN indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID "
				+ "LEFT JOIN project_key_area_mst ka ON hdr.PKA_ID = ka.PK_ID "
				+ "LEFT JOIN project_key_sub_area_mst sa ON hdr.PKSA_ID = sa.PSK_ID "
				+ "WHERE TRIM(hdr.INDENT_ID) = ?\n";

				List<IndentPartDetailsEntity> result = jdbcTemplate.query(query, new IndentPartDetailsRowMapper(), indentId);

		if (result.isEmpty()) {
			return null;
		}

		IndentPartDetailsEntity grouped = new IndentPartDetailsEntity();
		grouped.setIndentCode(result.get(0).getIndentCode());
		grouped.setIndentId(result.get(0).getIndentId());
		grouped.setStationId(result.get(0).getStationId());
		grouped.setStationNo(result.get(0).getStationNo());
		grouped.setSubAssyId(result.get(0).getSubAssyId());
		grouped.setSubAssyDesc(result.get(0).getSubAssyDesc());

		List<PartEntity> parts = result.stream().map(r -> {
			PartEntity part = new PartEntity();
			part.setPartNo(r.getPartNo());
			part.setPartDesc(r.getPartDesc());
			return part;
		}).collect(Collectors.toList());

		grouped.setParts(parts);

		return grouped;
	}


}
