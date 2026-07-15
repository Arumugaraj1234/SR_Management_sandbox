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

import com.vmfg.assembly.RowMapper.MaterialIssueDtlRowMapper;
import com.vmfg.assembly.RowMapper.MaterialIssueHdrRowMapper;
import com.vmfg.assembly.RowMapper.RetriveFromStockIssueRowMapper;
import com.vmfg.assembly.dao.interfaces.IAssemblyIssueDAO;
import com.vmfg.assembly.entity.MaterialIssueDtlEntity;
import com.vmfg.assembly.entity.MaterialIssueHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockIssueEntity;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class AssemblyIssueDAO implements IAssemblyIssueDAO {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyIssueDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insertMaterialIssueHdr(String pmHdrId, String mrHdrId, String issuedBy, String remarks,
			String tenantId) {
		logger.debug("insertMaterialIssueHdr   method Start");
		int insertRes = 0;
		try {

			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
					"material_issue_hdr", "4", jdbcTemplate, 1,0,null,0);
			// insert MiHdr
			String insertQ = "INSERT INTO material_issue_hdr (MI_CODE, TRANSACTION_NO, FINANCIAL_YEAR_MST_ID, PM_HDR_ID, MI_DATE, "
					+ "MR_HDR_ID, ISSUED_BY, ISSUED_ON, REMARKS, TENANT_ID) " + "VALUES (?,?,?,?,?,?,?,NOW(),?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, gen.getEnquiryCode());
					ps.setString(2, String.valueOf(gen.getSeq()));
					ps.setString(3, gen.getFinainceId());
					ps.setString(4, pmHdrId);
					ps.setString(5, CommonMethod.getCurrentDate());
					ps.setString(6, mrHdrId);
					ps.setString(7, issuedBy);
					ps.setString(8, remarks);
					ps.setString(9, tenantId);
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertMaterialIssueHdr  method  exception" + ex);
		}
		logger.debug("insertMaterialIssueHdr   method end");
		return insertRes;
	}

	@Override
	public int insertMaterialIssueDtl(int responseMiHdrId, String mrDtlId, String productId, String requestedQty,
			String availableQty, String issuedQty, String tenantId, String productCode, String projectId, String miCode,
			String updateBy,String invLocation) {
		int insertStatus = 0;
		try {

			String qry = "INSERT INTO material_issue_dtl (MI_HDR_ID, MR_DTL_ID, PRODUCT_ID,"
					+ " REQUESTED_QTY, AVAILABLE_QTY, ISSUED_QTY, TENANT_ID) VALUES (?,?,?,?,?,?,?)";
			insertStatus = this.jdbcTemplate.update(qry, responseMiHdrId, mrDtlId, productId, requestedQty,
					availableQty, issuedQty, tenantId);
			CommonMethod.updateProductInvDtl(projectId, productId, "ILC0003", new BigDecimal(issuedQty), "",
					"ITTC0016", miCode, updateBy, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

			CommonMethod.updateProductInvDtl(projectId, productId, invLocation, new BigDecimal(issuedQty),
					"Subraction", "ITTC0016", miCode, updateBy, CommonMethod.getCurrentDateTime(), tenantId,
					jdbcTemplate);
		} catch (Exception ex) {
			logger.error("insertMaterialIssueDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int updateMRDtlIssueQty(String mrDtlId,String issuedQty ) {
		int updateStatus = 0;
		try {
			String updateQry = "update material_request_dtl set ISSUED_QTY=ISSUED_QTY + ? where MR_DTL_ID=? ";
			updateStatus=this.jdbcTemplate.update(updateQry,issuedQty,mrDtlId);

		} catch (Exception ex) {
			logger.error("updateMRDtlIssueQty method Error" + ex);
		}
		return updateStatus;
	}
	
	@Override
	public int getMRCompletedStatus(String mrHdrId) {
		int count = 0;
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END result\r\n" + 
					"FROM\r\n" + 
					"    material_request_dtl\r\n" + 
					"WHERE\r\n" + 
					"    (REQUESTED_QTY - ISSUED_QTY) > 0\r\n" + 
					"        AND MR_HDR_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,mrHdrId);
			count = Integer.parseInt(result.get("result").toString());
		} catch (Exception ex) {
			logger.error("getMRCompletedStatus method Error" + ex);
		}
		return count;
	}

	@Override
	public int updateMrHdrCompletedStatus(String mrHdrId,String empId ) {
		int updateStatus = 0;
		try {
			String updateQry = "UPDATE material_request_hdr SET IS_COMPLETED=1, LAST_UPDATED_BY=? , LAST_UPDATED_ON=? WHERE MR_HDR_ID=?;";
			updateStatus=this.jdbcTemplate.update(updateQry,empId,CommonMethod.getCurrentDateTime(),mrHdrId);

		} catch (Exception ex) {
			logger.error("updateMrHdrCompletedStatus method Error" + ex);
		}
		return updateStatus;
	}
	
	
	@Override
	public List<MaterialIssueHdrEntity> getMaterialIssueHdr(String hdrId, String tenantId, String productId) {
		List<MaterialIssueHdrEntity> list = new ArrayList<MaterialIssueHdrEntity>();
		List<MaterialIssueHdrEntity> list1 = new ArrayList<MaterialIssueHdrEntity>();
		int productCount = 0;
		try {
			String pmHdrTd="", qry="";
			if(!hdrId.equalsIgnoreCase("") && !hdrId.equalsIgnoreCase("getAll")) {
				pmHdrTd="AND mih.PM_HDR_ID = '" + hdrId + "'";
			}
			
			if(!productId.equalsIgnoreCase("") && !productId.equalsIgnoreCase(null)) {
				qry = "SELECT     mih.MI_CODE,    mih.MI_DATE,    mih.ISSUED_ON, mrh.IS_COMPLETED,\r\n" + 
						"					    mrh.MR_CODE,    mih.MI_HDR_ID,   em.EMPLOYEE_FIRSTNAME as ISSUED_BY,\r\n" + 
						"					    mih.MR_HDR_ID,    mrh.REQUESTED_ON,  phdr.PROJECT_CODE, em2.EMPLOYEE_FIRSTNAME as REQUESTED_BY  \r\n" + 
						"					  FROM  material_issue_hdr mih,    employee_mst em, project_hdr phdr, material_issue_dtl mdtl,\r\n" + 
						"					    material_request_hdr mrh,  employee_mst em2 WHERE mrh.REQUESTED_BY = em2.EMPLOYEE_ID and mih.ISSUED_BY = em.EMPLOYEE_ID and\r\n" + 
						"					    mrh.MR_HDR_ID=mih.MR_HDR_ID and phdr.PM_HDR_ID=mih.PM_HDR_ID and mdtl.MI_HDR_ID=mih.MI_HDR_ID\r\n" + 
						"                        "+pmHdrTd+"   and PRODUCT_ID = '"+productId+"'\r\n" + 
						"					        AND mih.TENANT_ID = '"+tenantId+"'" ;
			}else {
				 qry = " SELECT \r\n" + "    mih.MI_CODE,\r\n" + "    mih.MI_DATE,\r\n" + "    mih.ISSUED_ON, mrh.IS_COMPLETED,\r\n"
						+ "    mrh.MR_CODE,\r\n" + "    mih.MI_HDR_ID,\r\n" + "   em.EMPLOYEE_FIRSTNAME as ISSUED_BY,\r\n"
						+ "    mih.MR_HDR_ID,\r\n" + "    mrh.REQUESTED_ON,  phdr.PROJECT_CODE, em2.EMPLOYEE_FIRSTNAME as REQUESTED_BY\r\n"  
						+ "  FROM\r\n  material_issue_hdr mih,\r\n" + "    employee_mst em, project_hdr phdr,\r\n"
						+ "    material_request_hdr mrh,  employee_mst em2\r\n" + "WHERE\r\n" + "mrh.REQUESTED_BY = em2.EMPLOYEE_ID and mih.ISSUED_BY = em.EMPLOYEE_ID and\r\n"
						+ "    mrh.MR_HDR_ID=mih.MR_HDR_ID and phdr.PM_HDR_ID=mih.PM_HDR_ID\r\n" + " "+pmHdrTd+"    \r\n"
						+ "        AND mih.TENANT_ID = '" + tenantId + "'";
			}
			
			list = this.jdbcTemplate.query(qry, new MaterialIssueHdrRowMapper());
			for (MaterialIssueHdrEntity materialObj : list) {
				MaterialIssueHdrEntity obj = new MaterialIssueHdrEntity();
				String countQry = "SELECT \r\n" + "    COUNT(*) AS COUNT\r\n" + "FROM\r\n" + "    material_issue_dtl midl,\r\n"
						+ "    product_mst pm\r\n" + "WHERE\r\n" + "    midl.PRODUCT_ID = pm.PRODUCT_ID\r\n"
						+ "        AND midl.MI_HDR_ID = ? and midl.TENANT_ID = '" + tenantId + "'";
				Map<String, Object> result = this.jdbcTemplate.queryForMap(countQry,materialObj.getMiHdrId());
				productCount = Integer.parseInt(result.get("COUNT").toString());
				
				obj.setProductCount(String.valueOf(productCount));
				obj.setIssuedBy(materialObj.getIssuedBy());
				obj.setProjCode(materialObj.getProjCode());
				obj.setIssuedOn(materialObj.getIssuedOn());
				obj.setMiCode(materialObj.getMiCode());
				obj.setMiDate(materialObj.getMiDate());
				obj.setMiHdrId(materialObj.getMiHdrId());
				obj.setMrCode(materialObj.getMrCode());
				obj.setMrHdrId(materialObj.getMrHdrId());
				obj.setRequestedOn(materialObj.getRequestedOn());
				obj.setIsCompleted(materialObj.getIsCompleted());
				obj.setRequestedBy(materialObj.getRequestedBy());
				list1.add(obj);
			}

		} catch (Exception e) {
			logger.error("getMaterialIssueHdr Method Exception --->" + e);
		}
		return list;
	}

	@Override
	public List<MaterialIssueDtlEntity> getMaterialIssueDtl(String hdrId, String tenantId) {
		List<MaterialIssueDtlEntity> list1 = new ArrayList<MaterialIssueDtlEntity>();
		try {
			String qry = "SELECT \r\n" + "    pm.PRODUCT_DESCRIPTION,\r\n" + "    pm.PRODUCT_CODE,\r\n"
					+ "    um.UOM_LONG_DESCRIPTION,\r\n" + "    um.UOM_SHORT_DESCRIPTION,\r\n"
					+ "    midl.AVAILABLE_QTY,\r\n" + "    midl.REQUESTED_QTY,\r\n" + "    midl.ISSUED_QTY\r\n"
					+ "  \r\n" + "FROM\r\n" + "    material_issue_hdr mih,\r\n" + "    material_issue_dtl midl,\r\n"
					+ "    product_mst pm,\r\n" + "    uom_mst um\r\n" + "    \r\n" + "WHERE\r\n"
					+ "    mih.MI_HDR_ID = midl.MI_HDR_ID\r\n" + "        AND midl.PRODUCT_ID = pm.PRODUCT_ID\r\n"
					+ "        AND pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + "        AND mih.MI_HDR_ID = '" + hdrId
					+ "'\r\n" + " AND midl.TENANT_ID = '" + tenantId + "'  AND mih.TENANT_ID = '" + tenantId + "'";
			list1 = this.jdbcTemplate.query(qry, new MaterialIssueDtlRowMapper());

		} catch (Exception e) {
			logger.error("getMaterialIssueDtl Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public List<RetriveFromStockIssueEntity> retriveFromIssueStock(String hdrId, String tenantId) {
		List<RetriveFromStockIssueEntity> list1 = new ArrayList<RetriveFromStockIssueEntity>();
		String INVENTORY_LOCATION_CODE = "ILC0002";
		try {
//			String qry = "SELECT \r\n" + "    SUM(ipd.PRODUCT_QUANTITY_ON_HAND) AS INVENTORY_QTY,\r\n"
//					+ "    mrd.REQUESTED_QTY - mrd.ISSUED_QTY as REQUIRED_QTY,\r\n" + "    pm.PRODUCT_CODE,\r\n"
//					+ "    pm.PRODUCT_DESCRIPTION,\r\n" + "    um.UOM_LONG_DESCRIPTION,\r\n"
//					+ "    um.UOM_SHORT_DESCRIPTION,\r\n" + "    mrd.PRODUCT_ID,\r\n" + "    mrd.MR_DTL_ID ,pm.BIN\r\n"
//					+ "FROM\r\n" + "    material_request_dtl mrd,\r\n" + "    product_mst pm,\r\n"
//					+ "    inventory_product_dtl ipd,\r\n" + "    uom_mst um\r\n" + "WHERE\r\n"
//					+ "    mrd.PRODUCT_ID = pm.PRODUCT_ID\r\n   and ipd.PRODUCT_ID = pm.PRODUCT_ID "
//					+ "        AND um.UOM_CODE = pm.PRODUCT_UOM_CODE\r\n" + "        AND mrd.MR_HDR_ID = '" + hdrId
//					+ "'\r\n" + "        AND ipd.INVENTORY_LOCATION_CODE = '" + INVENTORY_LOCATION_CODE + "'\r\n"
//					+ "        AND mrd.TENANT_ID = '" + tenantId + "' and REQUESTED_QTY >0\r\n"
//					+ "GROUP BY mrd.PRODUCT_ID";
			
			String qry ="SELECT \r\n" + 
					"    inv.PRODUCT_QUANTITY_ON_HAND AS INVENTORY_QTY,\r\n" + 
					"    dtl.REQUESTED_QTY - dtl.ISSUED_QTY AS REQUIRED_QTY,\r\n" + 
					"    dtl.PRODUCT_ID,\r\n" + 
					"    dtl.MR_DTL_ID,\r\n" + 
					"    mst.BIN,\r\n" + 
					"    mst.PRODUCT_CODE,\r\n" + 
					"    mst.PRODUCT_DESCRIPTION,\r\n" + 
					"    uo.UOM_LONG_DESCRIPTION,\r\n" + 
					"    uo.UOM_SHORT_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    material_request_dtl dtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    product_mst mst ON mst.PRODUCT_ID = dtl.PRODUCT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_product_dtl inv ON inv.PRODUCT_ID = dtl.PRODUCT_ID\r\n" + 
					"        AND inv.INVENTORY_LOCATION_CODE = dtl.INVENTORY_LOCATION_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    uom_mst uo ON uo.UOM_CODE = mst.PRODUCT_UOM_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_location_mst invmst ON invmst.INVENTORY_LOCATION_CODE = inv.INVENTORY_LOCATION_CODE\r\n" + 
					"WHERE\r\n" + 
					"    dtl.MR_HDR_ID = ?\r\n" + 
					"        AND dtl.TENANT_ID = ?\r\n" + 
					"        AND dtl.REQUESTED_QTY - dtl.ISSUED_QTY  > 0\r\n" + 
					"        AND (inv.INVENTORY_LOCATION_CODE = ?\r\n" + 
					"        OR invmst.INVENTORY_LOCATION_PARENT_CODE = ?) \r\n" +
				    " GROUP BY inv.INVENTORY_LOCATION_CODE , mst.PRODUCT_ID \r\n" + 
					"            HAVING SUM(inv.PRODUCT_QUANTITY_ON_HAND) > 0";
			
			list1 = this.jdbcTemplate.query(qry, new RetriveFromStockIssueRowMapper(),hdrId,tenantId,INVENTORY_LOCATION_CODE,INVENTORY_LOCATION_CODE);

		} catch (Exception e) {
			logger.error("retriveFromIssueStock Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public String getMtlIusseCode(String miCode) {
		String mtlIssCode = "";
		try {
			String miCodeStr = "select case when Count(*)>0 then MI_CODE else '' end As CODE from material_issue_hdr where MI_HDR_ID =?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(miCodeStr,miCode);
			mtlIssCode =  (String) result.get("CODE");
		} catch (Exception e) {
			logger.error("getMtlIusseCode Method Exception --->" + e);
		}
		return mtlIssCode;

	}

	@Override
	public String getInventoryLoctionCodeByMrDtlId(String mrDtlId) {
		String inventoryLocationCode = "";
		try {
			String miCodeStr = "select INVENTORY_LOCATION_CODE AS INVENTORY_LOCATION_CODE from material_request_dtl where MR_DTL_ID= ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(miCodeStr,mrDtlId);
			inventoryLocationCode =  (String) result.get("INVENTORY_LOCATION_CODE");
		}catch(Exception ex) {
			logger.error("getInventoryLoctionCodeByMrDtlId Method Exception --->" + ex);
		}
		return inventoryLocationCode;
	}

}
