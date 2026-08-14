package com.vmfg.assembly.dao.impl;

import java.math.BigDecimal;
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

import com.vmfg.assembly.RowMapper.MsHdrRetrieveRowMapper;
import com.vmfg.assembly.RowMapper.RetrieveForMSowMapper;
import com.vmfg.assembly.RowMapper.RetrieveMSDtlByHdrRowMapper;
import com.vmfg.assembly.dao.interfaces.IAssemblyStagingDAO;
import com.vmfg.assembly.entity.MsHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveForMSEntity;
import com.vmfg.assembly.entity.RetrieveMSDtlByHdrEntity;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.scm.dao.impl.PoDAO;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class AssemblyStagingDAO implements IAssemblyStagingDAO {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyStagingDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IndentUploadDAO indentUploadDao;

	@Autowired
	PoDAO poDAO;

	@Override
	public List<MsHdrRetrieveEntity> msHdrRetrieve(String hdrId, String tenantId) {
		List<MsHdrRetrieveEntity> list1 = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    msh.MS_NAME,msh.STATUS,msh.MS_HDR_ID, msh.QTY,msh.PM_HDR_ID, msh.UOM_CODE, msh.CREATED_ON, em.EMPLOYEE_FIRSTNAME AS CREATED_BY\r\n"
					+ "FROM\r\n" + "    material_staging_hdr msh,\r\n" + "    employee_mst em\r\n" + "WHERE\r\n"
					+ "    msh.CREATED_BY = em.EMPLOYEE_ID\r\n" + "        AND msh.PM_HDR_ID = '" + hdrId + "'\r\n"
					+ "        AND msh.STATUS = 'ACTIVE'\r\n"
					+ "        AND msh.TENANT_ID = '" + tenantId + "'";
			list1 = this.jdbcTemplate.query(qry, new MsHdrRetrieveRowMapper());

		} catch (Exception e) {
			logger.error("msHdrRetrieve Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public List<MsHdrRetrieveEntity> msHdrRetrieveAll(String hdrId, String tenantId) {
		List<MsHdrRetrieveEntity> list1 = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    msh.MS_NAME,msh.STATUS,msh.MS_HDR_ID, msh.QTY,msh.PM_HDR_ID, msh.UOM_CODE, msh.CREATED_ON, em.EMPLOYEE_FIRSTNAME AS CREATED_BY\r\n"
					+ "FROM\r\n" + "    material_staging_hdr msh,\r\n" + "    employee_mst em\r\n" + "WHERE\r\n"
					+ "    msh.CREATED_BY = em.EMPLOYEE_ID\r\n" + "        AND msh.PM_HDR_ID = '" + hdrId + "'\r\n"
					+ "        AND msh.TENANT_ID = '" + tenantId + "'\r\n"
					+ "ORDER BY msh.CREATED_ON DESC";
			list1 = this.jdbcTemplate.query(qry, new MsHdrRetrieveRowMapper());

		} catch (Exception e) {
			logger.error("msHdrRetrieveAll Method Exception --->" + e);
		}
		return list1;
	}
	
	@Override
	public List<MsHdrRetrieveEntity> msHdrRetrieveByHdrId(String hdrId, String tenantId) {
		List<MsHdrRetrieveEntity> list1 = new ArrayList<>();
		try {
			String qry = "SELECT \r\n"
					+ "    msh.MS_NAME,msh.STATUS,msh.MS_HDR_ID, msh.QTY,msh.PM_HDR_ID, msh.UOM_CODE, msh.CREATED_ON, em.EMPLOYEE_FIRSTNAME AS CREATED_BY\r\n"
					+ "FROM\r\n" + "    material_staging_hdr msh,\r\n" + "    employee_mst em\r\n" + "WHERE\r\n"
					+ "    msh.CREATED_BY = em.EMPLOYEE_ID\r\n" + "        AND msh.MS_HDR_ID = '" + hdrId + "'\r\n"
					+ "        AND msh.STATUS = 'ACTIVE'\r\n"
					+ "        AND msh.TENANT_ID = '" + tenantId + "'";
			list1 = this.jdbcTemplate.query(qry, new MsHdrRetrieveRowMapper());

		} catch (Exception e) {
			logger.error("msHdrRetrieve Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public List<RetrieveMSDtlByHdrEntity> retrieveMSDtlByHdr(String hdrId, String tenantId) {
		List<RetrieveMSDtlByHdrEntity> list1 = new ArrayList<>();
		try {
			String qry = "SELECT \r\n" + 
					"    msd.MS_DTL_ID,\r\n" + 
					"    msd.QTY,\r\n" + 
					"    pm.PRODUCT_ID,\r\n" + 
					"    pm.PRODUCT_CODE,\r\n" + 
					"    pm.PRODUCT_DESCRIPTION,\r\n" + 
					"    um.UOM_LONG_DESCRIPTION,\r\n" + 
					"    pkam.PK_DESC AS STATION,\r\n" + 
					"    pksam.PSK_DESC AS SUB_ASSY , pm.SPECIFICATION ,pm.MAKE\r\n" + 
					"FROM\r\n" + 
					"    material_staging_dtl msd\r\n" + 
					"    INNER JOIN product_mst pm ON pm.PRODUCT_ID = msd.PRODUCT_ID\r\n" + 
					"    INNER JOIN uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + 
					"    LEFT JOIN project_key_area pka ON pm.PKA_ID = pka.PKA_ID\r\n" + 
					"    LEFT JOIN project_key_area_mst pkam ON pka.PK_ID = pkam.PK_ID\r\n" + 
					"    LEFT JOIN project_key_sub_area pksa ON pm.PSKA_ID = pksa.PKSA_ID\r\n" + 
					"    LEFT JOIN project_key_sub_area_mst pksam ON pksa.PSK_ID = pksam.PSK_ID\r\n" + 
					"WHERE\r\n" + 
					"    msd.MS_HDR_ID = '"+hdrId+"'\r\n" + 
					"    AND msd.TENANT_ID = '"+ tenantId +"'\r\n" + 
					"ORDER BY \r\n" + 
					"    STATION, \r\n" + 
					"    SUB_ASSY";
			list1 = this.jdbcTemplate.query(qry, new RetrieveMSDtlByHdrRowMapper());

		} catch (Exception e) {
			logger.error("retrieveMSDtlByHdr Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public int checkMsHdrNameExists(String pmHdrId, String msName, String tenantId) {
		int count = 0;
		try {
			String qry = "select count(*) AS MS_NAME_COUNT from material_staging_hdr where PM_HDR_ID = ? and MS_NAME = ? and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId, msName, tenantId);
			count = Integer.parseInt(resultMap.get("MS_NAME_COUNT").toString());
		} catch (Exception ex) {
			logger.error("checkMsHdrNameExists error " + ex);
		}
		return count;
	}

	@Override
	public int insertMsHdr(String pmHdrId, String msName, String stageQty, String tenantId, String createdBy,
			String uom) {
		logger.debug("insertMsHdr   method Start");
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO material_staging_hdr (PM_HDR_ID, MS_NAME, QTY, CREATED_ON, CREATED_BY, TENANT_ID,UOM_CODE) "
					+ "VALUES (?,?,?,NOW(),?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, pmHdrId);
					ps.setString(2, msName);
					ps.setString(3, stageQty);
					ps.setString(4, createdBy);
					ps.setString(5, tenantId);
					ps.setString(6, uom);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

//			String uomCode = indentUploadDao.getUomCodeByUnit(uom, tenantId);
//			if (uomCode.equalsIgnoreCase("0")) {
//				indentUploadDao.createNewUomAndInsert(uom.trim(), tenantId);
//				uomCode = indentUploadDao.getUomCodeByUnit(uom, tenantId);
//			}
//
//			int checkProductCode = indentUploadDao.checkProductCodeInProdMst(msName, tenantId, pmHdrId, "FG");
//			if (checkProductCode == 0) {
//
//				indentUploadDao.insertnewProductInProdMst(msName, pmHdrId, msName, uomCode, tenantId, createdBy, msName,
//						"0", stageQty, uom, "0", msName, null, null, "FG");
//			}
//			CommonMethod.updateProductInvDtl(pmHdrId, msName, "ILC0003", new BigDecimal(stageQty), "add", "ITTC0012",
//					insertRes + "", createdBy, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

		} catch (Exception ex) {
			logger.error("insertMsHdr  method  exception" + ex);
		}
		logger.debug("insertMsHdr   method end");
		return insertRes;
	}

	@Override
	public int insertMsDtl(int responseMsHdrId, String productId, String qty, String tenantId, String pmHdrId,
			String createdBy) {
		logger.debug("insertMsDtl   method Start");
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO material_staging_dtl (MS_HDR_ID, PRODUCT_ID, QTY, TENANT_ID) "
					+ "VALUES (?,?,?,?)";

			insertRes = this.jdbcTemplate.update(insertQ, responseMsHdrId, productId, qty, tenantId);

//			String productCode = poDAO.getProdCodeByprodId(productId);
			CommonMethod.updateProductInvDtl(pmHdrId, productId, "ILC0003", new BigDecimal(qty), "Subraction",
					"ITTC0012", insertRes + "", createdBy, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

		} catch (Exception ex) {
			logger.error("insertMsDtl  method  exception" + ex);
		}
		logger.debug("insertMsDtl   method end");
		return insertRes;
	}

	@Override
	public int cancelMsHdrReq(String hdrId, String tenantId, String empId,String qty,String projectId,String productId) {
		return finalizeMsHdr(hdrId, tenantId, empId, qty, projectId, productId, "CANCELLED");
	}

	@Override
	public int useMsHdrForReturn(String hdrId, String tenantId, String empId, String qty, String projectId,
			String productId) {
		return finalizeMsHdr(hdrId, tenantId, empId, qty, projectId, productId, "USED");
	}

	private int finalizeMsHdr(String hdrId, String tenantId, String empId, String qty, String projectId,
			String productId, String status) {
		logger.debug("finalizeMsHdr   method Start");
		int updateHdr = 0;
		try {

//			String productCode = poDAO.getProdCodeByprodId(productId);
			CommonMethod.updateProductInvDtl(projectId, productId, "ILC0003", new BigDecimal(qty), "Subraction", "ITTC0012",
					hdrId + "", empId, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

			String updateHdrQ = "UPDATE material_staging_hdr SET STATUS = ? WHERE MS_HDR_ID = ? and TENANT_ID=? ";
			updateHdr = this.jdbcTemplate.update(updateHdrQ, status, hdrId, tenantId);

		} catch (Exception ex) {
			logger.error("finalizeMsHdr  method  exception" + ex);
		}
		logger.debug("finalizeMsHdr   method end");
		return updateHdr;
	}

	@Override
	public List<RetrieveForMSEntity> retrieveForMS(String hdrId, String tenantId) {
		List<RetrieveForMSEntity> list1 = new ArrayList<>();
		String INVENTORY_LOCATION_CODE = "ILC0003";
		try {
			String qry = "SELECT \r\n" + "    ipd.PRODUCT_QUANTITY_ON_HAND AS PRODUCT_QUANTITY_ON_HAND,\r\n"
					+ "    pm.PRODUCT_DESCRIPTION,\r\n" + "    pm.PRODUCT_ID,\r\n" + "    pm.PRODUCT_CODE,\r\n"
					+ "    um.UOM_LONG_DESCRIPTION,\r\n" + "    pkam.PK_DESC AS STATION,\r\n"
					+ "    pksam.PSK_DESC AS SUB_ASSY , pm.SPECIFICATION , pm.MAKE \r\n" + "FROM\r\n" + "    product_mst pm\r\n"
					+ "    INNER JOIN inventory_product_dtl ipd ON ipd.PRODUCT_ID = pm.PRODUCT_ID\r\n"
					+ "    INNER JOIN uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n"
					+ "    LEFT JOIN project_key_area pka ON pm.PKA_ID = pka.PKA_ID\r\n"
					+ "    LEFT JOIN project_key_area_mst pkam ON pka.PK_ID = pkam.PK_ID\r\n"
					+ "    LEFT JOIN project_key_sub_area pksa ON pm.PSKA_ID = pksa.PKSA_ID\r\n"
					+ "    LEFT JOIN project_key_sub_area_mst pksam ON pksa.PSK_ID = pksam.PSK_ID\r\n" + "WHERE\r\n"
					+ "    pm.PM_HDR_ID = '" + hdrId + "'\r\n" + "    AND pm.TENANT_ID = '" + tenantId + "'\r\n"
					+ "    AND ipd.INVENTORY_LOCATION_CODE = '" + INVENTORY_LOCATION_CODE + "'\r\n"
					+ "    AND ipd.PRODUCT_QUANTITY_ON_HAND > 0;";

			list1 = this.jdbcTemplate.query(qry, new RetrieveForMSowMapper());

		} catch (Exception e) {
			logger.error("retrieveForMS Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public BigDecimal checkInv(List<MsHdrRetrieveEntity> list, String tenantId) {
		BigDecimal value=BigDecimal.ZERO;
		try {
			String qry = "SELECT \r\n" + "    case when count(*) > 0 then dtl.PRODUCT_QUANTITY_ON_HAND else 0 end as PRODUCT_QUANTITY_ON_HAND\r\n"
					+ "FROM\r\n" + "    inventory_product_dtl dtl,\r\n" + "    product_mst pm\r\n" + "WHERE\r\n"
					+ "    pm.PRODUCT_ID = dtl.PRODUCT_ID\r\n" + "    and pm.PRODUCT_DESCRIPTION = ?"
							+ "" + "    and pm.PM_HDR_ID = ?"
					+ "    and pm.TENANT_ID = ?"
					+ "    and dtl.INVENTORY_LOCATION_CODE = 'ILC0003'";

		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,list.get(0).getMsName(),list.get(0).getPmHdrId(),tenantId);
			value= new BigDecimal(resultMap.get("PRODUCT_QUANTITY_ON_HAND").toString());
			return value;
		} catch (Exception e) {
			logger.error("retrieveForMS Method Exception --->" + e);
			return BigDecimal.ZERO;
		}

	}

	@Override
	public void updateMsDtlInv(String productId, String qty, String projectId, String tenantId, String empId,
			String dtlId) {
		try {

//			String productCode = poDAO.getProdCodeByprodId(productId);
			CommonMethod.updateProductInvDtl(projectId, productId, "ILC0003", new BigDecimal(qty), "", "ITTC0012",
					dtlId + "", empId, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

		} catch (Exception ex) {
			logger.error("updateMsDtlInv  method  exception" + ex);
		}

	}

	@Override
	public String prodIdFromDesc(String msName, String pmHdrId, String tenantId) {
		String prod="";
		try {

			String getQ = "select PRODUCT_ID from product_mst where PM_HDR_ID = ? and PRODUCT_DESCRIPTION = ? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,pmHdrId,msName,tenantId);
			prod = resultMap.get("PRODUCT_ID").toString();

		} catch (Exception ex) {
			logger.error("prodIdFromDesc  method  exception" + ex);
		}return prod;
	}
}
