package com.vmfg.assembly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.assembly.RowMapper.MaterialReturnAcceptRowMapper;
import com.vmfg.assembly.RowMapper.MaterialReturnDtlAcceptRowMapper;
import com.vmfg.assembly.RowMapper.MrHdrRetrieveRowMapper;
import com.vmfg.assembly.RowMapper.RetrieveMReturnDtlByHdrRowMapper;
import com.vmfg.assembly.dao.interfaces.IAssemblyReturnDAO;
import com.vmfg.assembly.entity.MaterialReturnAcceptEntity;
import com.vmfg.assembly.entity.MaterialReturnDtlAcceptEntity;
import com.vmfg.assembly.entity.MrHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveMReturnDtlByHdrEntity;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class AssemblyReturnDAO implements IAssemblyReturnDAO {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyStagingDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<MrHdrRetrieveEntity> mrHdrRetrieve(String hdrId, String tenantId) {
		List<MrHdrRetrieveEntity> list1 = new ArrayList<>();
		List<MrHdrRetrieveEntity> list = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    mrh.REMARKS,\r\n"
					+ "    mrh.MRH_ID,\r\n"
					+ "    mrh.IS_CANCELED,\r\n"
					+ "       mrh.IS_COMPLETED,\r\n"
					+ "    mrh.SEQUENCE_NO,\r\n"
					+ "    mrh.CREATED_ON,\r\n"
					+ "     mrh.LAST_UPDATED_DATETIME,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_NAME,\r\n"
					+ "    em.EMPLOYEE_ID,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "FROM\r\n"
					+ "    material_return_hdr mrh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    document_status_type_code dstc\r\n"
					+ "WHERE\r\n"
					+ "    mrh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND mrh.CREATED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND mrh.PM_HDR_ID = '"+hdrId+"'\r\n"
					+ "        AND mrh.TENANT_ID = '"+tenantId+"'";
			list1 = this.jdbcTemplate.query(qry, new MrHdrRetrieveRowMapper());
			for(MrHdrRetrieveEntity count:list1) {
				MrHdrRetrieveEntity obj=new MrHdrRetrieveEntity();
				BeanUtils.copyProperties(count, obj);
				String prodCountQ = "select count(PRODUCT_ID) as COUNT from material_return_dtl where MRH_ID=?";
				
				Map<String, Object> result = this.jdbcTemplate.queryForMap(prodCountQ,count.getMrhId());
				int pCount = Integer.parseInt(result.get("COUNT").toString());
				
				obj.setProductCount(String.valueOf(pCount));
				list.add(obj);
			}


		} catch (Exception e) {
			logger.error("mrHdrRetrieve Method Exception --->" + e);
		}
		return list;
	}

	@Override
	public int insertMaterialReturnHdr(String pmHdrId, String remarks, String tenantId,
			String createdBy,String seqNo, String seqStatus) {
		logger.debug("insertMaterialReturnHdr   method Start");
		String isCanecel = "0";
		String isCompleted = "0";
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO material_return_hdr (PM_HDR_ID, CREATED_ON, CREATED_BY,IS_CANCELED,IS_COMPLETED,SEQUENCE_NO,SEQUENCE_STATUS,REMARKS,LAST_UPDATED_DATETIME,TENANT_ID) "
					+ "VALUES (?,NOW(),?,?,?,?,?,?,NOW(),?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, pmHdrId);
					ps.setString(2, createdBy);
					ps.setString(3, isCanecel);
					ps.setString(4, isCompleted);
					ps.setString(5, seqNo);
					ps.setString(6, seqStatus);
					ps.setString(7, remarks);
					ps.setString(8, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertMaterialReturnHdr  method  exception" + ex);
		}
		logger.debug("insertMaterialReturnHdr   method end");
		return insertRes;
	}

	@Override
	public int insertMaterialReturnDtl(int responseMrHdrId, String productId, String qty, String tenantId) {
		logger.debug("insertMaterialReturnDtl   method Start");
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO material_return_dtl (MRH_ID, PRODUCT_ID, QTY, TENANT_ID) "
					+ "VALUES (?,?,?,?)";

			insertRes = this.jdbcTemplate.update(insertQ, responseMrHdrId, productId, qty, tenantId);

		} catch (Exception ex) {
			logger.error("insertMaterialReturnDtl  method  exception" + ex);
		}
		logger.debug("insertMaterialReturnDtl   method end");
		return insertRes;
	}

	@Override
	public List<RetrieveMReturnDtlByHdrEntity> retrieveMreturnDtlByHdr(String hdrId, String tenantId) {
		List<RetrieveMReturnDtlByHdrEntity> list1 = new ArrayList<>();
		try {
			
			String qry = "SELECT \r\n" + 
					"    mrd.QTY,\r\n" + 
					"    mrd.MRD_ID,\r\n" + 
					"    mrd.IS_APPROVED,\r\n" + 
					"    pm.PRODUCT_ID,\r\n" + 
					"    pm.PRODUCT_CODE,\r\n" + 
					"    pm.PRODUCT_DESCRIPTION,\r\n" + 
					"    um.UOM_LONG_DESCRIPTION,\r\n" + 
					"    pkam.PK_DESC AS STATION,\r\n" + 
					"    pksam.PSK_DESC AS SUB_ASSY\r\n" + 
					"FROM\r\n" + 
					"    material_return_dtl mrd\r\n" + 
					"        INNER JOIN\r\n" + 
					"    product_mst pm ON pm.PRODUCT_ID = mrd.PRODUCT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    project_key_area pka ON pm.PKA_ID = pka.PKA_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    project_key_area_mst pkam ON pka.PK_ID = pkam.PK_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    project_key_sub_area pksa ON pm.PSKA_ID = pksa.PKSA_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    project_key_sub_area_mst pksam ON pksa.PSK_ID = pksam.PSK_ID\r\n" + 
					"WHERE\r\n" + 
					"    mrd.MRH_ID = '"+hdrId+"'\r\n" + 
					"        AND mrd.TENANT_ID = '"+tenantId+"'\r\n" + 
					"ORDER BY STATION , SUB_ASSY;";
			
			
			
			list1 = this.jdbcTemplate.query(qry, new RetrieveMReturnDtlByHdrRowMapper());

		} catch (Exception e) {
			logger.error("retrieveMreturnDtlByHdr Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public int cancelMaterialReturnHdr(String hdrId, String tenantId,String seqStatus,String seqNo) {
		logger.debug("cancelMaterialReturnHdr   method Start");
		int  deleteHdr = 0;
		String isCancel="1";
		String currentDateTime=CommonMethod.getCurrentDateTime();
		try {

			String deleteDtlQ = "UPDATE  material_return_hdr SET SEQUENCE_NO=? ,SEQUENCE_STATUS=? , LAST_UPDATED_DATETIME=? , IS_CANCELED=? WHERE MRH_ID = ?  and TENANT_ID= ? ";
			deleteHdr = this.jdbcTemplate.update(deleteDtlQ,seqNo,seqStatus,currentDateTime,isCancel,hdrId,tenantId);

		} catch (Exception ex) {
			logger.error("cancelMaterialReturnHdr  method  exception" + ex);
		}
		logger.debug("cancelMaterialReturnHdr   method end");
		return deleteHdr;
	}
	
	@Override
	public int approveMaterialReturnHdr(String mrHdrId, String tenantId, String docStatus, String seqNo, String empId) {
		logger.debug("approveMaterialReturnHdr   method Start");
		int  approveHdr = 0;
		String isCompleted="1";
		String currentDateTime=CommonMethod.getCurrentDateTime();
		try {

			String approveDtlQ = "UPDATE  material_return_hdr SET SEQUENCE_NO=? ,SEQUENCE_STATUS=? , LAST_UPDATED_DATETIME=? , IS_COMPLETED=?, LAST_UPDATED_BY=? WHERE MRH_ID = ?  and TENANT_ID= ? ";
			approveHdr = this.jdbcTemplate.update(approveDtlQ,seqNo,docStatus,currentDateTime,isCompleted,empId,mrHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("approveMaterialReturnHdr  method  exception" + ex);
		}
		logger.debug("approveMaterialReturnHdr   method end");
		return approveHdr;
	}
	
	@Override
	public String getSeqandStatus(String lastSeq, String docType, String tenantId) {
		logger.debug("getSeqandStatus   method Start");
		String docStatus = "";
		try {
			
			 String status = "select DOC_STATUS from document_lifecycle_mst WHERE DOC_TYPE = ? AND LAST_SEQ = ? AND TENANT_ID = ?;";
			 Map<String, Object> result = this.jdbcTemplate.queryForMap(status,docType,lastSeq,tenantId);
			 docStatus = result.get("DOC_STATUS").toString();
			 
		}catch(Exception ex) {
			logger.error("getSeqandStatus  method  exception" + ex);
		}
		return docStatus;
	}

	@Override
	public List<MaterialReturnAcceptEntity> materialReturnAccept(String mrhId, String tenantId) {
		List<MaterialReturnAcceptEntity> list1 = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    mrh.PM_HDR_ID, pm.PRODUCT_CODE, mrd.QTY,mrh.CREATED_BY as EMPLOYEE_ID\r\n"
					+ "FROM\r\n"
					+ "    material_return_hdr mrh,\r\n"
					+ "    material_return_dtl mrd,\r\n"
					+ "    product_mst pm\r\n"
					+ "WHERE\r\n"
					+ "    mrh.MRH_ID = mrd.MRH_ID\r\n"
					+ "        AND pm.PRODUCT_ID = mrd.PRODUCT_ID\r\n"
					+ "        AND mrd.MRH_ID = '"+mrhId+"'\r\n"
					+ "        AND mrd.TENANT_ID = '"+tenantId+"'";
			list1 = this.jdbcTemplate.query(qry, new MaterialReturnAcceptRowMapper());

		} catch (Exception e) {
			logger.error("materialReturnAccept Method Exception --->" + e);
		}
		return list1;
	}
	
	@Override
	public List<MaterialReturnDtlAcceptEntity> ApproveMreturnDtls(String dtlId, String tenantId) {
		List<MaterialReturnDtlAcceptEntity> list = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    mrh.PM_HDR_ID, mrh.MRH_ID, pm.PRODUCT_CODE, pm.PRODUCT_ID, mrd.QTY,mrh.CREATED_BY as EMPLOYEE_ID, mrd.TENANT_ID\r\n"
					+ "FROM\r\n"
					+ "    material_return_hdr mrh,\r\n"
					+ "    material_return_dtl mrd,\r\n"
					+ "    product_mst pm\r\n"
					+ "WHERE\r\n"
					+ "    mrh.MRH_ID = mrd.MRH_ID\r\n"
					+ "        AND pm.PRODUCT_ID = mrd.PRODUCT_ID\r\n"
					+ "        AND mrd.MRD_ID = '"+dtlId+"'\r\n"
					+ "        AND mrd.TENANT_ID = '"+tenantId+"'";
			list = this.jdbcTemplate.query(qry, new MaterialReturnDtlAcceptRowMapper());

		} catch (Exception e) {
			logger.error("ApproveMreturnDtls Method Exception --->" + e);
		}
		return list;
	}
	
	@Override
	public int ApproveDtls(String mrDtlId, String tenantId) {
		logger.debug("ApproveDtls method Start");
		int  approveDtl = 0;
		String isApproved="1";

		try {

			String approveDtlQ = "UPDATE  material_return_dtl SET IS_APPROVED = ? WHERE MRD_ID = ?  and TENANT_ID= ? ";
			approveDtl = this.jdbcTemplate.update(approveDtlQ,isApproved,mrDtlId,tenantId);

		} catch (Exception ex) {
			logger.error("ApproveDtls method  exception" + ex);
		}
		logger.debug("ApproveDtls method end");
		return approveDtl;
	}


	@Override
	public int updateStatusInMaterialReturnHdr(String mrhId, String currentSeq, String seqStatus,String isCompleted) {
		logger.debug("updateStatusInMaterialReturnHdr   method Start");
		int  deleteHdr = 0;
		String currentDateTime=CommonMethod.getCurrentDateTime();
		try {

			String deleteDtlQ = "UPDATE  material_return_hdr SET LAST_UPDATED_DATETIME= ?, IS_COMPLETED= ?,SEQUENCE_NO= ?,"
					+ "SEQUENCE_STATUS=?  WHERE MRH_ID = ?"; 

			deleteHdr = this.jdbcTemplate.update(deleteDtlQ,currentDateTime,isCompleted,currentSeq,seqStatus,mrhId);

		} catch (Exception ex) {
			logger.error("updateStatusInMaterialReturnHdr  method  exception" + ex);
		}
		logger.debug("updateStatusInMaterialReturnHdr   method end");
		return deleteHdr;
	}

	@Override
	public int insertReturnRemarks(String mrhId, String referencDoc, String currentSeq, String seqStatus, String remarks,
			String empId, String tenantId) {
		logger.debug("insertReturnRemarks   method Start");
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO material_return_status_dtl (REFERENCE_ID, REFERENCE_DOC, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) "
					+ "VALUES (?,?,?,?,?,?,NOW(),?)";

			insertRes = this.jdbcTemplate.update(insertQ, mrhId, referencDoc, currentSeq, seqStatus,remarks,empId,tenantId);

		} catch (Exception ex) {
			logger.error("insertReturnRemarks  method  exception" + ex);
		}
		logger.debug("insertReturnRemarks   method end");
		return insertRes;
	}

}
