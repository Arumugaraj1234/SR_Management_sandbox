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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.assembly.RowMapper.GetAssyDtlRowMapper;
import com.vmfg.assembly.RowMapper.MaterialReqDtlRowMapper;
import com.vmfg.assembly.RowMapper.MaterialReqHdrRowMapper;
import com.vmfg.assembly.RowMapper.RetriveFromStockRowMapper;
import com.vmfg.assembly.dao.interfaces.IAssemblyDAO;
import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.assembly.entity.MaterialReqDtlEntity;
import com.vmfg.assembly.entity.MaterialReqHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockEntity;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.design.entity.GetPlanAndActualEntity;
import com.vmfg.design.rowmapper.GetPlanAndActualRowMapper;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class AssemblyDAO implements IAssemblyDAO {

	private static final Logger logger = LoggerFactory.getLogger(AssemblyDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<GetAssyDtlEntity> getAssyDtl(String fromDate, String toDate, String customerName, String assyHdrId,
			String tenantId, String pmId, String empId,String projectId) {
		List<GetAssyDtlEntity> list = new ArrayList<GetAssyDtlEntity>();
		String datediff="";
		if(!fromDate.equalsIgnoreCase("")) {
			datediff = "AND hdr.REQUEST_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		String pro = " and hdr.PM_HDR_ID like '%%' ";
		if(!projectId.equalsIgnoreCase("getall")) {
			pro = " and hdr.PM_HDR_ID = '"+projectId+"'";
		}
		try {
			if (assyHdrId.isEmpty()) {
				String custName = "%" + customerName + "%";
				String assyHdrStr = "SELECT \r\n" + "    hdr.*,phdr.PROJECT_CODE,phdr.ENQUIRY_ID, \r\n"
						+ "    stageDesc.STG_DESC AS STAGE_DESC,\r\n"
						+ "    statusDesc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC\r\n" + "FROM\r\n"
						+ "    assy_hdr hdr\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code statusDesc\r\n" + "        INNER JOIN\r\n"
						+ "    stg_master stageDesc,\r\n" + "    process_assigned_team pat ,  project_hdr phdr \r\n"
						+ "WHERE\r\n" + "    hdr.TRANSACTION_STAGE = stageDesc.STG_CODE\r\n"
						+ "        AND pat.MASTER_ID = hdr.ASSY_HDR_ID AND phdr.PM_HDR_ID = hdr.PM_HDR_ID \r\n"
						+ "        AND pat.ASSIGNED_EMP_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID \r\n"
						+ "        AND hdr.TRANSACTION_STATUS = statusDesc.DOCUMENT_STATUS_TYPE_CODE\r\n"
						+ "        \r\n"+datediff
						+ "        AND hdr.CUSTOMER_NAME LIKE ?\r\n" + "        AND hdr.TENANT_ID = ?\r\n"
						+ "        AND PM_ID = ? AND pat.IS_ACTIVE = 1"+pro ;
				list = this.jdbcTemplate.query(assyHdrStr, new GetAssyDtlRowMapper(), empId, custName,
						tenantId, pmId);
			} else {
				String assyHdrStr = "SELECT \r\n" + "    hdr.*, phdr.PROJECT_CODE ,phdr.ENQUIRY_ID,\r\n"
						+ "    stageDesc.STG_DESC AS STAGE_DESC,\r\n"
						+ "    statusDesc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC\r\n" + "FROM\r\n"
						+ "    assy_hdr hdr\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code statusDesc\r\n" + "        INNER JOIN\r\n"
						+ "    stg_master stageDesc ,project_hdr phdr \r\n" + "WHERE\r\n"
						+ "    hdr.TRANSACTION_STAGE = stageDesc.STG_CODE  AND phdr.PM_HDR_ID = hdr.PM_HDR_ID \r\n"
						+ "        AND hdr.TRANSACTION_STATUS = statusDesc.DOCUMENT_STATUS_TYPE_CODE\r\n"
						+ "        AND hdr.ASSY_HDR_ID = ?\r\n" + "        AND hdr.TENANT_ID = ?;";
				list = this.jdbcTemplate.query(assyHdrStr, new GetAssyDtlRowMapper(), assyHdrId, tenantId);
			}
		} catch (Exception ex) {
			logger.error("ChangereqHdrList Error" + ex);
		}
		return list;
	}

	@Override
	public int getindentcount(String pmId, String isComplete) {
//		Map<String, Object> getindentcount = 0;
		int getindentcount =0;
		String getindentcountStr ="";
		try {
			if (isComplete.equalsIgnoreCase("1")) {
				getindentcountStr = "select count(*) as COUNT from indent_hdr where PROJECT_ID=? and IS_COMPLETED =1 ";
			} else {
				getindentcountStr = "select count(*) as COUNT from indent_hdr where PROJECT_ID=? ";
			}

			Map<String, Object> result = this.jdbcTemplate.queryForMap(getindentcountStr,pmId);
			getindentcount = Integer.parseInt(result.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getindentcount Error" + ex);
		}
		return getindentcount;
	}
	public List<GetPlanAndActualEntity> getIndentAssyPlannedGroupList(String pmId, String isComplete) {
		List<GetPlanAndActualEntity> retrnMsg = new ArrayList<GetPlanAndActualEntity>();
		try {
			String getQ =  "SELECT \r\n" + 
					"    SUM(CASE\n"
					+ "        WHEN hdr.INDENT_ID IS NOT NULL THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) AS PLAN_COUNT,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN hdr.IS_COMPLETED = 1 THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) ACTUAL_COUNT \r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr \r\n" + 
					"WHERE\r\n" + 
					"     PROJECT_ID = '"+pmId+"' ";
			RowMapper<GetPlanAndActualEntity> rowmapper = new GetPlanAndActualRowMapper();
			retrnMsg = this.jdbcTemplate.query(getQ, rowmapper);
		} catch (Exception ex) {
			logger.error("Error with method getIndentAssyPlannedGroupList " + ex.getMessage());
		}
		return retrnMsg;
	}

	@Override
	public int getMaterialReqHdrCount(String pmId, String isComplete) {
		int getMaterialReqHdrCount = 0;
		String getMaterialReqHdrCountStr ="";
		try {
			if (isComplete.equalsIgnoreCase("1")) {
				getMaterialReqHdrCountStr = "select count(*) as COUNT from material_request_hdr where PM_HDR_ID=? and IS_COMPLETED =1 ";
			} else {
				getMaterialReqHdrCountStr = "select count(*) AS COUNT from material_request_hdr where PM_HDR_ID=? ";
			}
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getMaterialReqHdrCountStr,pmId);
			getMaterialReqHdrCount = Integer.parseInt(result.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getMaterialReqHdrCount Error" + ex);
		}
		return getMaterialReqHdrCount;
	}
	
	public List<GetPlanAndActualEntity> getMaterialReqAssyGroupList(String pmId, String isComplete) {
		List<GetPlanAndActualEntity> retrnMsg = new ArrayList<GetPlanAndActualEntity>();
		try {
			String getQ =  "SELECT \r\n" + 
					"    SUM(CASE\n"
					+ "        WHEN hdr.MR_HDR_ID IS NOT NULL THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) AS PLAN_COUNT,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN hdr.IS_COMPLETED = 1 THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) ACTUAL_COUNT \r\n" + 
					"FROM\r\n" + 
					"    material_request_hdr hdr \r\n" + 
					"WHERE\r\n" + 
					"     PM_HDR_ID = '"+pmId+"' and IS_CANCELLED  = 0 ";
			RowMapper<GetPlanAndActualEntity> rowmapper = new GetPlanAndActualRowMapper();
			retrnMsg = this.jdbcTemplate.query(getQ, rowmapper);
		} catch (Exception ex) {
			logger.error("Error with method getMaterialReqAssyGroupList " + ex.getMessage());
		}
		return retrnMsg;
	}

	@Override
	public List<MaterialReqHdrEntity> getMaterialReqHdr(String pmHdrId, String tenantId,String requestType) {
		List<MaterialReqHdrEntity> list = new ArrayList<MaterialReqHdrEntity>();
		List<MaterialReqHdrEntity> list1 = new ArrayList<MaterialReqHdrEntity>();
		int productCount = 0;
		try {
			String qry="";
			// Material issue screen
			if(requestType.equals("1")) {
			 qry = "  SELECT \r\n" + "    mrh.MR_CODE,\r\n" + "    mrh.REQUESTED_ON,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    mrh.REQUESTED_BY,\r\n" + "  mrh.REQUEST_TYPE,\r\n" + "  mrh.IS_COMPLETED,\r\n"
					+ "    mrh.IS_CANCELLED,\r\n" + "    mrh.REQUESTED_FOR AS REASON,\r\n" + "    mrh.MR_HDR_ID\r\n"
					+ "    \r\n" + "    \r\n" + "FROM\r\n" + "    material_request_hdr mrh,\r\n"
					+ "    employee_mst em\r\n" + "    \r\n" + "WHERE\r\n" + "    mrh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND mrh.PM_HDR_ID = '" + pmHdrId + "'\r\n" + "    AND mrh.REQUEST_TYPE='1'\r\n" + "    AND mrh.TENANT_ID = '" + tenantId
					+ "' order by mrh.MR_CODE, IS_COMPLETED , IS_CANCELLED";
			}else if(requestType.equals("0")){
				
				
				 qry = "  SELECT \r\n" + "    mrh.MR_CODE,\r\n" + "    mrh.REQUESTED_ON,\r\n"
							+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    mrh.REQUESTED_BY,\r\n" + "  mrh.REQUEST_TYPE,\r\n" + "  mrh.IS_COMPLETED,\r\n"
							+ "    mrh.IS_CANCELLED,\r\n" + "    mrh.REQUESTED_FOR AS REASON,\r\n" + "    mrh.MR_HDR_ID\r\n"
							+ "    \r\n" + "    \r\n" + "FROM\r\n" + "    material_request_hdr mrh,\r\n"
							+ "    employee_mst em\r\n" + "    \r\n" + "WHERE\r\n" + "    mrh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
							+ "        AND mrh.PM_HDR_ID = '" + pmHdrId + "'\r\n" + "  AND mrh.REQUEST_TYPE='0'\r\n" + "   AND mrh.TENANT_ID = '" + tenantId
							+ "' order by mrh.MR_CODE, IS_COMPLETED , IS_CANCELLED";
			}else {
				// Material Request screen
				 qry = "  SELECT \r\n" + "    mrh.MR_CODE,\r\n" + "    mrh.REQUESTED_ON,\r\n"
							+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    mrh.REQUESTED_BY,\r\n" + "  mrh.REQUEST_TYPE,\r\n" + "  mrh.IS_COMPLETED,\r\n"
							+ "    mrh.IS_CANCELLED,\r\n" + "    mrh.REQUESTED_FOR AS REASON,\r\n" + "    mrh.MR_HDR_ID\r\n"
							+ "    \r\n" + "    \r\n" + "FROM\r\n" + "    material_request_hdr mrh,\r\n"
							+ "    employee_mst em\r\n" + "    \r\n" + "WHERE\r\n" + "    mrh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
							+ "        AND mrh.PM_HDR_ID = '" + pmHdrId + "'\r\n" + "    AND mrh.TENANT_ID = '" + tenantId
							+ "' order by mrh.MR_CODE, IS_COMPLETED , IS_CANCELLED";
			}
			list = this.jdbcTemplate.query(qry, new MaterialReqHdrRowMapper());
			for (MaterialReqHdrEntity materialObj : list) {
				MaterialReqHdrEntity obj = new MaterialReqHdrEntity();
				String countQry = "SELECT \r\n" + "    COUNT(*) AS PRODUCT_COUNT\r\n" + "FROM\r\n"
						+ "    material_request_dtl mrd,\r\n" + "    product_mst pm\r\n" + "WHERE\r\n"
						+ "        mrd.PRODUCT_ID = pm.PRODUCT_ID\r\n" + "        AND mrd.MR_HDR_ID = ?";
				
				Map<String, Object> result = this.jdbcTemplate.queryForMap(countQry,materialObj.getMrHdrId());
				productCount = Integer.parseInt(result.get("PRODUCT_COUNT").toString());
				
				materialObj.setProductCount(String.valueOf(productCount));
				obj.setCancelled(materialObj.getCancelled());
				obj.setCompleted(materialObj.getCompleted());
				obj.setEmpId(materialObj.getEmpId());
				obj.setEmployeeName(materialObj.getEmployeeName());
				obj.setMrCode(materialObj.getMrCode());
				obj.setReason(materialObj.getReason());
				obj.setMrHdrId(materialObj.getMrHdrId());
				obj.setProductCount(materialObj.getProductCount());
				obj.setRequestedOn(materialObj.getRequestedOn());
				obj.setRequestType(materialObj.getRequestType());
				list1.add(obj);
			}

		} catch (Exception e) {
			logger.error("getMaterialReqHdr Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public List<MaterialReqDtlEntity> getMaterialReqDtl(String hdrId, String tenantId) {
		List<MaterialReqDtlEntity> list1 = new ArrayList<MaterialReqDtlEntity>();
		try {
			String qry = "SELECT \n"
					+ "    pkam.PK_DESC AS STATION,\n"
					+ "    pm.PRODUCT_DESCRIPTION,\n"
					+ "    pm.PRODUCT_CODE,\n"
					+ "    um.UOM_LONG_DESCRIPTION,\n"
					+ "    um.UOM_SHORT_DESCRIPTION,\n"
					+ "    mrd.AVAILABLE_QTY,\n"
					+ "    mrd.REQUESTED_QTY,\n"
					+ "    pksam.PSK_DESC AS SUBASSY,\n"
					+ "    ilm.INVENTORY_LOCATION_DESCRIPTION,\n"
					+ "    mrh.IS_CANCELLED,\n"
					+ "    pm.BIN , pm.SPECIFICATION, pm.MAKE, mrh.IS_COMPLETED,mrd.ISSUED_QTY \n"
					+ "FROM\n"
					+ "    material_request_dtl mrd\n"
					+ "        INNER JOIN\n"
					+ "    material_request_hdr mrh ON mrh.MR_HDR_ID = mrd.MR_HDR_ID\n"
					+ "        INNER JOIN\n"
					+ "    product_mst pm ON mrd.PRODUCT_ID = pm.PRODUCT_ID\n"
					+ "        INNER JOIN\n"
					+ "    inventory_location_mst ilm ON ilm.INVENTORY_LOCATION_CODE = mrd.INVENTORY_LOCATION_CODE\n"
					+ "        INNER JOIN\n"
					+ "    uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    project_key_area pka ON pm.PKA_ID = pka.PKA_ID\n"
					+ "        LEFT JOIN\n"
					+ "    project_key_sub_area pksa ON pm.PSKA_ID = pksa.PKSA_ID\n"
					+ "        LEFT JOIN\n"
					+ "    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID\n"
					+ "        LEFT JOIN\n"
					+ "    project_key_sub_area_mst pksam ON pksa.PSK_ID = pksam.PSK_ID\n"
					+ "WHERE\n"
					+ "    mrd.MR_HDR_ID = '"+hdrId+"'\n"
					+ "        AND mrd.TENANT_ID = '"+tenantId+"'";
			list1 = this.jdbcTemplate.query(qry, new MaterialReqDtlRowMapper());

		} catch (Exception e) {
			logger.error("getMaterialReqDtl Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public ResponseAsMessage cancelMiRequestHdr(String hdrId, String tenantId) {
		ResponseAsMessage rm = new ResponseAsMessage();
		String isCancel = "1";
		try {
			String qrySelectCount = "SELECT \r\n" + "    COUNT(*) AS COUNT "
					+ "\r\n" + "FROM\r\n" + "    material_issue_hdr\r\n"
					+ "WHERE\r\n" + "    MR_HDR_ID = ? AND TENANT_ID = ?";
			
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qrySelectCount,hdrId,tenantId);
			int count = Integer.parseInt(result.get("COUNT").toString());
			
			if (count > 0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(ResponseMessageMap.cancelled);
				rm.setResponseMessage(ResponseMessageMap.successMsg);
			} else {
				String updateQry = "UPDATE material_request_hdr SET IS_CANCELLED = ? WHERE  MR_HDR_ID = ? AND TENANT_ID = ?";
				int updateRes = this.jdbcTemplate.update(updateQry,isCancel,hdrId,tenantId);
				if (updateRes > 0) {
					rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					rm.setResponseDataMessage(String.valueOf(updateRes));
					rm.setResponseMessage(ResponseMessageMap.mrCancelled);
				} else {
					rm.setResponseCode(ResponseMessageMap.failToupdateCode);
					rm.setResponseDataMessage(ResponseMessageMap.failMsg);
					rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				}

			}
		} catch (Exception ex) {
			logger.error("cancelMiRequestHdr error " + ex.getMessage());

		}
		return rm;
	}

	@Override
	public List<RetriveFromStockEntity> retriveFromStock(String pmHdrId, String pkaId, String pskaId, String tenantId) {
		List<RetriveFromStockEntity> list1 = new ArrayList<RetriveFromStockEntity>();
		String qry="";
		try {
			if (pkaId.equalsIgnoreCase("getAll")) {
				qry="SELECT \r\n" + 
						"    SUM(ipd.PRODUCT_QUANTITY_ON_HAND) AVAILABLE_QTY,\r\n" + 
						"    pm.PRODUCT_CODE,\r\n" + 
						"    pm.PRODUCT_DESCRIPTION,\r\n" + 
						"    ipd.PRODUCT_ID,\r\n" + 
						"    um.UOM_LONG_DESCRIPTION,\r\n" + 
						"    um.UOM_SHORT_DESCRIPTION,\r\n" + 
						"    pka.PKA_ID,\r\n" + 
						"    pk.PK_DESC,\r\n" + 
						"    pska.PKSA_ID,\r\n" + 
						"    psk.PSK_DESC,\r\n" + 
						"    inv.INVENTORY_LOCATION_DESCRIPTION,\r\n" + 
						"    inv.INVENTORY_LOCATION_CODE,\r\n" + 
						"    pm.SPECIFICATION,\r\n" +
						"	 pm.MAKE\r\n" +
						"FROM\r\n" + 
						"    product_mst pm\r\n" + 
						"        INNER JOIN\r\n" + 
						"    inventory_product_dtl ipd ON pm.PRODUCT_ID = ipd.PRODUCT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    inventory_location_mst inv ON ipd.INVENTORY_LOCATION_CODE = inv.INVENTORY_LOCATION_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    uom_mst um ON um.UOM_CODE = pm.PRODUCT_UOM_CODE\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    project_key_area pka ON pm.PKA_ID = pka.PKA_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    project_key_sub_area pska ON pm.PSKA_ID = pska.PKSA_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    project_key_sub_area_mst psk ON pska.PSK_ID = psk.PSK_ID\r\n" + 
						"WHERE\r\n" + 
						"    pm.PM_HDR_ID = '"+pmHdrId+"'\r\n" + 
						"        AND pm.TENANT_ID = '"+tenantId+"'\r\n" + 
						"        AND (ipd.INVENTORY_LOCATION_CODE = 'ILC0002'\r\n" + 
						"        OR inv.INVENTORY_LOCATION_PARENT_CODE = 'ILC0002')\r\n" + 
						"GROUP BY ipd.INVENTORY_LOCATION_CODE , pm.PRODUCT_ID\r\n" + 
						"HAVING SUM(ipd.PRODUCT_QUANTITY_ON_HAND) > 0;";
				
			} else {
//				pkaAndPskaId = " AND pm.PKA_ID = '" + pkaId + "' AND pm.PSKA_ID = '" + pskaId + "'";
				
				qry = "SELECT \r\n" + 
						"    SUM(ipd.PRODUCT_QUANTITY_ON_HAND) AVAILABLE_QTY,\r\n" + 
						"    pm.PRODUCT_CODE,\r\n" + 
						"    pm.PRODUCT_DESCRIPTION,\r\n" + 
						"    ipd.PRODUCT_ID,\r\n" + 
						"    um.UOM_LONG_DESCRIPTION,\r\n" + 
						"    um.UOM_SHORT_DESCRIPTION,\r\n" + 
						"    pka.PKA_ID,\r\n" + 
						"    pk.PK_DESC,\r\n" + 
						"    pska.PKSA_ID,\r\n" + 
						"    psk.PSK_DESC,\r\n" + 
						"    inv.INVENTORY_LOCATION_DESCRIPTION,\r\n" + 
						"    inv.INVENTORY_LOCATION_CODE,\r\n" +  
						"    pm.SPECIFICATION,\r\n" +
						"	 pm.MAKE\r\n" +
						"FROM\r\n" + 
						"    product_mst pm\r\n" + 
						"        INNER JOIN\r\n" + 
						"    inventory_product_dtl ipd ON pm.PRODUCT_ID = ipd.PRODUCT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    uom_mst um ON um.UOM_CODE = pm.PRODUCT_UOM_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_area pka ON pm.PKA_ID = pka.PKA_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_sub_area pska ON pm.PSKA_ID = pska.PKSA_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_sub_area_mst psk ON pska.PSK_ID = psk.PSK_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    inventory_location_mst inv ON ipd.INVENTORY_LOCATION_CODE = inv.INVENTORY_LOCATION_CODE\r\n" + 
						"WHERE\r\n" + 
						"    pm.PM_HDR_ID = '"+pmHdrId+"' AND pm.PKA_ID = '" + pkaId + "'\r\n" + 
						"        AND pm.PSKA_ID = '" + pskaId + "'\r\n" + 
						"        AND pm.TENANT_ID = '"+tenantId+"'\r\n" + 
						"        AND (ipd.INVENTORY_LOCATION_CODE = 'ILC0002'\r\n" + 
						"        OR inv.INVENTORY_LOCATION_PARENT_CODE = 'ILC0002')\r\n" + 
						"GROUP BY ipd.INVENTORY_LOCATION_CODE , pm.PRODUCT_ID\r\n" + 
						"HAVING SUM(ipd.PRODUCT_QUANTITY_ON_HAND) > 0;";
			}
			
			list1 = this.jdbcTemplate.query(qry, new RetriveFromStockRowMapper());

		} catch (Exception e) {
			logger.error("retriveFromStock Method Exception --->" + e);
		}
		return list1;
	}

	@Override
	public int insertMrHdr(String pmHdrId, String requestedBy, String requestedFor, String tenantId,String requestType) {
		logger.debug("insertMrHdr   method Start");
		int insertRes = 0;
		String cancelled = "0";
		String completed = "0";
		try {

			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
					"material_request_hdr", "5", jdbcTemplate, 1,2,null,0);
			// insert MrHdr
			String insertQ = "INSERT INTO material_request_hdr ( MR_CODE, PM_HDR_ID, REQUESTED_ON, REQUESTED_BY, REQUESTED_FOR, REQUEST_TYPE, TRANSACTION_NO, "
					+ "FINANCIAL_YEAR_MST_ID, IS_COMPLETED, IS_CANCELLED, TENANT_ID) "
					+ "VALUES (?,?,NOW(),?,?,?,?,?,?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, gen.getEnquiryCode());
					ps.setString(2, pmHdrId);
					ps.setString(3, requestedBy);
					ps.setString(4, requestedFor);
					ps.setString(5, requestType);
					ps.setString(6, String.valueOf(gen.getSeq()));
					ps.setString(7, gen.getFinainceId());
					ps.setString(8, completed);
					ps.setString(9, cancelled);
					ps.setString(10, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertMrHdr  method  exception" + ex);
		}
		logger.debug("insertMrHdr   method end");
		return insertRes;
	}

	@Override
	public int insertMrDtl(int responseMrHdrId, String poductId, String requestedQty, String availableQty,
			String tenantId, String inventoryLocationCode) {
		int insertStatus = 0;
		String issuedQty = "0";
		try {
			String qry = "INSERT INTO material_request_dtl (MR_HDR_ID, PRODUCT_ID, REQUESTED_QTY, AVAILABLE_QTY, ISSUED_QTY, TENANT_ID,INVENTORY_LOCATION_CODE) VALUES (?,?,?,?,?,?,?)";
			insertStatus = this.jdbcTemplate.update(qry, responseMrHdrId, poductId, requestedQty, availableQty,
					issuedQty, tenantId, inventoryLocationCode);

		} catch (Exception ex) {
			logger.error("insertMrDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public ResponseAsMessage retriveAssyResp(MaterialReqHdrRequest assyMstRequest) {
		ResponseAsMessage rm = new ResponseAsMessage();

		int res = 0;
		try {

			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN START_MATERIAL_REQUEST IS NOT NULL THEN CAST(START_MATERIAL_REQUEST AS UNSIGNED)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS START_MATERIAL_REQUEST\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr\r\n" + 
					"WHERE\r\n" + 
					"    PM_HDR_ID = ? AND TENANT_ID = ?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,assyMstRequest.getHdrId(),assyMstRequest.getTenantId());
			res = Integer.parseInt(result.get("START_MATERIAL_REQUEST").toString());
			
			if (res == 0 || res == 1) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(String.valueOf(res));
				rm.setResponseMessage(ResponseMessageMap.success);
			} else {
				rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("retriveAssyResp error " + ex.getMessage());

		}
		return rm;
	}

	@Override
	public BigDecimal getActualAvailableQty(String pmHdrId, String tenantId, String productCode, String InventoryCode) {
		BigDecimal getActualAvailableQty = BigDecimal.ZERO;
		
		try {
				String qry = "SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(*) > 0 THEN sum(dtl.REQUESTED_QTY) \r\n" + 
						"        ELSE 0\r\n" + 
						"    END AS REQUESTED_QTY\r\n" + 
						"FROM\r\n" + 
						"    material_request_hdr hdr\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    material_request_dtl dtl ON hdr.MR_HDR_ID = dtl.MR_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = ?\r\n" + 
						"        AND dtl.PRODUCT_ID = ?\r\n" + 
						"        AND INVENTORY_LOCATION_CODE = ? \r\n" +
						"        AND hdr.TENANT_ID = ? and IS_CANCELLED = 0 \r\n" + 
						"ORDER BY MR_DTL_ID DESC\r\n ";
				Map<String, Object> resultmap = this.jdbcTemplate.queryForMap(qry,pmHdrId,productCode,InventoryCode,tenantId);
				System.out.println(resultmap.get("REQUESTED_QTY").toString());
				getActualAvailableQty = new BigDecimal(resultmap.get("REQUESTED_QTY").toString());
				
		} catch (Exception ex) {
			logger.error("getActualAvailableQty method Error" + ex);
		}
		return getActualAvailableQty;
	}
	
	@Override
	public BigDecimal getGrnQty(String pmHdrId, String tenantId, String productCode,String productId, String inventoryCode, String desc, String spec) {
		BigDecimal qty = BigDecimal.ZERO;
		
		try {
				String qry = "SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(*) > 0 THEN SUM(gd.RECEIVED_QTY)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END AS QTY\r\n" + 
						"FROM\r\n" + 
						"    grn_dtl gd\r\n" + 
						"        INNER JOIN\r\n" + 
						"    grn_hdr gh ON gd.GRN_HDR_ID = gh.GRN_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    gh.TENANT_ID = ? AND gh.IS_LATEST=1\r\n" + 
						"        AND gd.PRODUCT_ID = ?\r\n" + 
						"        AND gh.INVENTORY_LOCATION_CODE = ?\r\n " +
						"        or gd.INDENT_DTL_ID IN (SELECT \r\n" + 
						"            id.INDENT_DTL_ID\r\n" + 
						"        FROM\r\n" + 
						"            indent_dtl id\r\n" + 
						"                INNER JOIN\r\n" + 
						"            indent_hdr ih ON id.INDENT_ID = ih.INDENT_ID\r\n" +
						"    INNER JOIN\r\n" + 
						"            grn_dtl grn ON id.INDENT_DTL_ID = grn.INDENT_DTL_ID"+
						"        INNER JOIN\r\n" + 
						"    grn_hdr ghdr ON grn.GRN_HDR_ID = ghdr.GRN_HDR_ID\r\n" +
						"        WHERE\r\n" + 
						"            ih.PROJECT_ID = ? and ghdr.TENANT_ID = ? AND ghdr.IS_LATEST=1 \r\n" + 
						"                AND id.PRODUCT_CODE = ? AND id.DESCRIPTION = ? AND id.SPECIFICATION = ?\r\n" + 
						"                AND ih.TENANT_ID = ?);";
				Map<String, Object> resultmap = this.jdbcTemplate.queryForMap(qry,tenantId,productId,inventoryCode,pmHdrId,tenantId,productCode,
						desc, spec, tenantId);
				System.out.println(resultmap.get("QTY").toString());
				qty = new BigDecimal(resultmap.get("QTY").toString());
				
		} catch (Exception ex) {
			logger.error("getGrnQty method Error" + ex);
		}
		return qty;
	}


	@Override
	public ResponseAsMessage IsStagingStatusForQuality(String hdrId, String tenantId) {
		ResponseAsMessage rs = new ResponseAsMessage();

		int res = 0;
		int cnt = 0;
		try {
            String Chk = " SELECT \r\n" + 
            		"   COUNT(IS_STAGING_STATUS) as IS_STAGING_STATUS\r\n" + 
            		"FROM\r\n" + 
            		"    assy_hdr asy\r\n" + 
            		"        INNER JOIN\r\n" + 
            		"    quality_hdr qh ON asy.PM_HDR_ID = qh.PM_HDR_ID\r\n" + 
            		"WHERE\r\n" + 
            		"    Q_HDR_ID = ?\r\n" + 
            		"        AND asy.TENANT_ID = ?;";
            Map<String,Object> resultMap = jdbcTemplate.queryForMap(Chk, hdrId, tenantId);
            Object val = resultMap.get("IS_STAGING_STATUS");
           
            if(val == null) {
            	res = cnt;
            }else {
			cnt = Integer.parseInt(val.toString());
            }
            
      		if(cnt>0) {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN IS_STAGING_STATUS IS NOT NULL THEN CAST(IS_STAGING_STATUS AS UNSIGNED)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS IS_STAGING_STATUS\r\n" +
					"FROM\r\n" + 
					"    assy_hdr asy\r\n" + 
					"        INNER JOIN\r\n" + 
					"    quality_hdr qh ON asy.PM_HDR_ID = qh.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    Q_HDR_ID = ?\r\n" + 
					"        AND asy.TENANT_ID = ?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,hdrId,tenantId);
			Object value = result.get("IS_STAGING_STATUS");
			res = Integer.parseInt(value.toString());
      		}
			
				rs.setResponseCode(ResponseMessageMap.responseCodeOk);
				rs.setResponseDataMessage(String.valueOf(res));
				rs.setResponseMessage(ResponseMessageMap.success);
			
		} catch (Exception ex) {
			logger.error("IsStagingStatusForQuality error " + ex.getMessage());

		}
		return rs;
	}

	@Override
	public ResponseAsMessage IsStagingStatusForAssy(String hdrId, String tenantId) {
		ResponseAsMessage rs = new ResponseAsMessage();

		int res = 0;
		int cnt = 0;
		try {
			   String Chk = " SELECT \r\n" + 
	            		"   COUNT(IS_STAGING_STATUS) as IS_STAGING_STATUS\r\n" + 
	            		"FROM\r\n" + 
	            		"    assy_hdr\r\n" + 
	            		"WHERE\r\n" + 
	            		"    ASSY_HDR_ID = ?\r\n" + 
	            		"        AND TENANT_ID = ?;";
	            Map<String,Object> resultMap = jdbcTemplate.queryForMap(Chk, hdrId, tenantId);
	            Object val = resultMap.get("IS_STAGING_STATUS");
	           
	            if(val == null) {
	            	res = cnt;
	            }else {
				cnt = Integer.parseInt(val.toString());
	            }
	            
			if(cnt>0) {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN IS_STAGING_STATUS IS NOT NULL THEN CAST(IS_STAGING_STATUS AS UNSIGNED)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS IS_STAGING_STATUS\r\n" +
					"FROM\r\n" + 
					"    assy_hdr\r\n" + 
					"WHERE\r\n" + 
					"    ASSY_HDR_ID = ?\r\n" + 
					"        AND TENANT_ID = ?;";	
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,hdrId,tenantId);
			Object value = result.get("IS_STAGING_STATUS");
			res = Integer.parseInt(value.toString());
			}
				rs.setResponseCode(ResponseMessageMap.responseCodeOk);
				rs.setResponseDataMessage(String.valueOf(res));
				rs.setResponseMessage(ResponseMessageMap.success);
			
		} catch (Exception ex) {
			logger.error("IsStagingStatusForAssy error " + ex.getMessage());

		}
		return rs;
	}

	@Override
	public int updateIsStagingStatus(String hdrId, int isStatus, String tenantId) {
		
		int update = 0;
		try {
			String qry = "update assy_hdr assy INNER JOIN quality_hdr qh on qh.PM_HDR_ID = assy.PM_HDR_ID set IS_STAGING_STATUS = ?"
					+ " where Q_HDR_ID = ? and assy.TENANT_ID = ? ";
			update = this.jdbcTemplate.update(qry, isStatus, hdrId, tenantId);

		} catch (Exception ex) {
			logger.error("updateIsStagingStatus method Error" + ex);
		}
		return update;

	}

	@Override
	public int checkIsStagingStatus(String hdrId, String tenantId) {
	
		int res = 0;
		int cnt = 0;
		try {
			   String Chk = "SELECT \r\n" + 
			   		"    COUNT(asy.PM_HDR_ID) as PM_HDR_ID \r\n" + 
			   		"FROM\r\n" + 
			   		"    assy_hdr asy\r\n" + 
			   		"        INNER JOIN\r\n" + 
			   		"    quality_hdr qh ON asy.PM_HDR_ID = qh.PM_HDR_ID\r\n" + 
			   		"WHERE\r\n" + 
			   		"    Q_HDR_ID = ?\r\n" + 
			   		"        AND asy.TENANT_ID = ?;";
	            Map<String,Object> resultMap = jdbcTemplate.queryForMap(Chk, hdrId, tenantId);
	            cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
	            if(cnt == 0) {
	            	res = 3;
	            }
	            
			if(cnt>0) {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN asy.IS_STAGING_STATUS IS NOT NULL THEN CAST(asy.IS_STAGING_STATUS AS UNSIGNED)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS IS_STAGING_STATUS\r\n" +
					"FROM\r\n" + 
					"    assy_hdr asy inner join quality_hdr qh ON asy.PM_HDR_ID = qh.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    Q_HDR_ID = ?\r\n" + 
					"        AND asy.TENANT_ID = ?;";	
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,hdrId,tenantId);
			Object value = result.get("IS_STAGING_STATUS");
			res = Integer.parseInt(value.toString());
			if(res == 1) {
				res = 0;
			}else {
				res = 1;
			}
			}			
		} catch (Exception ex) {
			logger.error("IsStagingStatusForAssy error " + ex.getMessage());

		}
		return res;
	}

	@Override
	public String getIsInternalOrNot(String pmHdrId) {
		String isInternal = "";
		try {
			String miCodeStr = "select IS_INTERNAL from project_hdr hdr inner join sales_enq_hdr enq \r\n" + 
					"on hdr.ENQUIRY_ID = enq.SE_ID where PM_HDR_ID=? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(miCodeStr,pmHdrId);
			isInternal =  String.valueOf(result.get("IS_INTERNAL"));
		} catch (Exception e) {
			logger.error("getIsInternalOrNot Method Exception --->" + e);
		}
		return isInternal;

	}
}
