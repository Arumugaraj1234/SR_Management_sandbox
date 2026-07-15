package com.vmfg.scm.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.finance.rowmapper.GrnDtlsRowMapper;
import com.vmfg.scm.entity.*;
import com.vmfg.scm.request.GrnCancelReq;
import com.vmfg.scm.rowmapper.PoCostTypeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.scm.dao.interfaces.IGrnDAO;
import com.vmfg.scm.dao.interfaces.IMaterialInwardDAO;
import com.vmfg.scm.rowmapper.GrnDtlRowMapper;
import com.vmfg.scm.rowmapper.GrnHdrRowMapper;
import com.vmfg.scm.rowmapper.ProductDtlsRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class GrnDAO implements IGrnDAO {
	private static final Logger logger = LoggerFactory.getLogger(GrnDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IMaterialInwardDAO iMaterialInwardDAO;

	@Override
	public List<GrnHdrEntity> getGrnHdrDetails(String projectId, String tenantId,String PoId, String fromDate, String toDate) {
		List<GrnHdrEntity> returnList = new ArrayList<GrnHdrEntity>();
		String date="";
		if(projectId.equalsIgnoreCase("getAll")) {
			projectId="%%";
		}
		    if(fromDate!=null && toDate!=null) {
		    	if(!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
					date = "AND gh.GRN_DATE between '"+fromDate+"' and '"+toDate+"'";
				}	
		    }
			

		try {
			if (PoId != null) {
			    String retQry = "SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN mih.MI_CODE IS NOT NULL THEN mih.MI_CODE\r\n" + 
						"        ELSE 'NA'\r\n" + 
						"    END AS MI_CODE,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN mih.MI_ID IS NOT NULL THEN mih.MI_ID\r\n" + 
						"        ELSE 'NA'\r\n" + 
						"    END AS MI_ID,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN vm.VENDOR_NAME IS NOT NULL THEN vm.VENDOR_NAME\r\n" + 
						"        ELSE 'NA'\r\n" + 
						"    END AS VENDOR_NAME,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN vm.VENDOR_CODE IS NOT NULL THEN vm.VENDOR_CODE\r\n" + 
						"        ELSE 'NA'\r\n" + 
						"    END AS VENDOR_CODE,\r\n" + 
						"    emLastUpdated.EMPLOYEE_FIRSTNAME AS LAST_UPDATED_EMP_NAME,\r\n" + 
						"    emCreatedBy.EMPLOYEE_FIRSTNAME AS CREATED_EMP_NAME,\r\n" + 
						"    gh.GRN_HDR_ID AS GRN_HDR_ID,\r\n" + 
						"    gh.GRN_CODE AS GRN_CODE,\r\n" + 
						"    gh.TRANSACTION_NO AS TRANSACTION_NO,\r\n" + 
						"    gh.FINANCIAL_YEAR_MST_ID AS FINANCIAL_YEAR_MST_ID,\r\n" + 
						"    gh.GRN_DATE AS GRN_DATE,\r\n" +
						"    gh.CREATED_ON AS CREATED_ON,\r\n" + 
						"    gh.CREATED_BY AS CREATED_BY,\r\n" + 
						"    gh.LAST_UPDATED_ON AS LAST_UPDATED_ON,\r\n" + 
						"    gh.LAST_UPDATED_BY AS LAST_UPDATED_BY,phdr.PROJECT_CODE,phdr.PROJECT_NAME,\r\n" + 
						"    INVENTORY_LOCATION_DESCRIPTION, illm.INVENTORY_LOCATION_CODE, pm.PRODUCT_CODE,pm.PRODUCT_DESCRIPTION AS PRODUCT_DESC,mih.INWARD_DATE,po.PO_CODE,gd.RECEIVED_QTY AS GRN_QTY,mih.DC_NO,mih.DC_DATE \r\n" +
						"FROM\r\n" +
						"    grn_hdr AS gh inner join grn_dtl as gd on gh.GRN_HDR_ID = gd.GRN_HDR_ID inner join product_mst pm on pm.PRODUCT_ID = gd.PRODUCT_ID \r\n" +
						"        INNER JOIN\r\n" +
						"    employee_mst AS emLastUpdated ON emLastUpdated.EMPLOYEE_ID = gh.LAST_UPDATED_BY\r\n" +
						"        INNER JOIN\r\n" +
						"    employee_mst AS emCreatedBy ON emCreatedBy.EMPLOYEE_ID = gh.CREATED_BY\r\n" +
						"        INNER JOIN\r\n" +
						"    inventory_location_mst illm ON illm.INVENTORY_LOCATION_CODE = gh.INVENTORY_LOCATION_CODE\r\n" +
						"        LEFT JOIN\r\n" +
						"    material_inward_hdr AS mih ON mih.MI_ID = gh.MI_ID\r\n" +
						"        LEFT JOIN\r\n" +
						"    vendor_mst AS vm ON vm.VENDOR_CODE = mih.VENDOR_CODE\r\n" +
						"        left JOIN\r\n" +
						"    po_hdr po ON po.PO_ID = gh.PO_ID left join project_hdr phdr on phdr.PM_HDR_ID=pm.PM_HDR_ID\r\n" +
						"   #     left JOIN\r\n" +
						"   # indent_hdr inh ON inh.INDENT_ID = po.INDENT_ID\r\n" +
						"WHERE\r\n" +
						"    pm.PM_HDR_ID like ?\r\n" +
						"        AND gh.TENANT_ID = ? AND gh.PO_ID =? AND gh.IS_LATEST=1 ";
				returnList = this.jdbcTemplate.query(retQry, new GrnHdrRowMapper(),projectId,tenantId,PoId);
			}else {
		
			String retQry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN mih.MI_CODE IS NOT NULL THEN mih.MI_CODE\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END AS MI_CODE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN mih.MI_ID IS NOT NULL THEN mih.MI_ID\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END AS MI_ID,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN vm.VENDOR_NAME IS NOT NULL THEN vm.VENDOR_NAME\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END AS VENDOR_NAME,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN vm.VENDOR_CODE IS NOT NULL THEN vm.VENDOR_CODE\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END AS VENDOR_CODE,\r\n" + 
					"    emLastUpdated.EMPLOYEE_FIRSTNAME AS LAST_UPDATED_EMP_NAME,\r\n" + 
					"    emCreatedBy.EMPLOYEE_FIRSTNAME AS CREATED_EMP_NAME,\r\n" + 
					"    gh.GRN_HDR_ID AS GRN_HDR_ID,\r\n" + 
					"    gh.GRN_CODE AS GRN_CODE,\r\n" + 
					"    gh.TRANSACTION_NO AS TRANSACTION_NO,\r\n" + 
					"    gh.FINANCIAL_YEAR_MST_ID AS FINANCIAL_YEAR_MST_ID,\r\n" + 
					"    gh.GRN_DATE AS GRN_DATE,gh.PO_CODE as GRN_PO,\r\n" +
					"    gh.CREATED_ON AS CREATED_ON,\r\n" + 
					"    gh.CREATED_BY AS CREATED_BY,\r\n" + 
					"    gh.LAST_UPDATED_ON AS LAST_UPDATED_ON,\r\n" + 
					"    gh.LAST_UPDATED_BY AS LAST_UPDATED_BY,phdr.PROJECT_CODE,phdr.PROJECT_NAME,\r\n" + 
					"    INVENTORY_LOCATION_DESCRIPTION, illm.INVENTORY_LOCATION_CODE, pm.PRODUCT_CODE,pm.PRODUCT_DESCRIPTION AS PRODUCT_DESC,mih.INWARD_DATE,po.PO_CODE,gd.RECEIVED_QTY AS GRN_QTY,mih.DC_NO,mih.DC_DATE \r\n" +
					"FROM\r\n" + 
					"    grn_hdr AS gh inner join grn_dtl as gd on gh.GRN_HDR_ID = gd.GRN_HDR_ID inner join product_mst pm on pm.PRODUCT_ID = gd.PRODUCT_ID \r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst AS emLastUpdated ON emLastUpdated.EMPLOYEE_ID = gh.LAST_UPDATED_BY\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst AS emCreatedBy ON emCreatedBy.EMPLOYEE_ID = gh.CREATED_BY\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_location_mst illm ON illm.INVENTORY_LOCATION_CODE = gh.INVENTORY_LOCATION_CODE\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    material_inward_hdr AS mih ON mih.MI_ID = gh.MI_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    vendor_mst AS vm ON vm.VENDOR_CODE = mih.VENDOR_CODE\r\n" + 
					"        left JOIN\r\n" + 
					"    po_hdr po ON po.PO_ID = gh.PO_ID left join project_hdr phdr on phdr.PM_HDR_ID=pm.PM_HDR_ID\r\n" + 
					"   #     left JOIN\r\n" + 
					"   # indent_hdr inh ON inh.INDENT_ID = po.INDENT_ID\r\n" + 
					"WHERE\r\n" + 
					"    pm.PM_HDR_ID like ?\r\n" +
					"	 AND gh.IS_LATEST=1\r\n" +
					"        AND gh.TENANT_ID = ? "+date+" ";
			returnList = this.jdbcTemplate.query(retQry, new GrnHdrRowMapper(),projectId,tenantId);
			}
		} catch (Exception ex) {
			logger.error("getGrnHdrDetails error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<GrnDtlEntity> getGrnDtlwithMaterialInwardDtl(String grnHdrId, String tenantId) {
		List<GrnDtlEntity> returnList = new ArrayList<>();
		try {
			String retQry = "SELECT \r\n" + 
					"    midl.MI_DTL_ID,\r\n" + 
					"    midl.PO_DTL_ID,\r\n" + 
					"    idl.INDENT_DTL_ID,\r\n" + 
					"    idl.INDENT_ID,\r\n" +
					"	 pm.PRODUCT_ID,\r\n" +
					"    pm.PRODUCT_CODE,\r\n" + 
					"    pm.PRODUCT_DESCRIPTION,\r\n" + 
					"    pm.SPECIFICATION,\r\n" + 
					"    pm.MAKE,\r\n" + 
					"    pm.QTY,\r\n" + 
					"    pm.PRODUCT_UOM_CODE,\r\n" + 
					"    pm.MATERIAL,\r\n" + 
					"    idl.REMARKS,\r\n" + 
					"    midl.ORDERED_QTY,\r\n" + 
					"    midl.INSPECTED_QTY,\r\n" +   
					"    midl.UOM,\r\n" +
				    "    uom.UOM_LONG_DESCRIPTION,\r\n" + 
					"    midl.MI_ID,\r\n" + 
					"    midl.RECEIVED_QTY AS MATERIAL_INWARD_RECEIVED_QTY,\r\n" + 
					"    gdl.RECEIVED_QTY AS GRN_RECEIVED_QTY,\r\n" + 
					"    gdl.GRN_DTL_ID,\r\n" + 
					"    mhr.PO_CODE,\r\n" + 
					"    mhr.DC_NO,\r\n" + 
					"    mhr.DC_DATE,\r\n" + 
					"    midl.REMARKS as MAT_REMARKS\r\n" +
					"	 FROM\r\n" +
					"    grn_dtl AS gdl\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    material_inward_dtl AS midl ON gdl.MI_DTL_ID = midl.MI_DTL_ID\r\n" +
					"    left JOIN\r\n" + 
					"    material_inward_hdr AS mhr ON mhr.MI_ID = midl.MI_ID\r\n" + 
					"        left JOIN\r\n" + 
					"    product_mst AS pm ON gdl.PRODUCT_ID = pm.PRODUCT_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    indent_dtl idl ON gdl.INDENT_DTL_ID = idl.INDENT_DTL_ID\r\n" + 
					"    left join uom_mst uom ON midl.UOM = uom.UOM_CODE\r\n"+
					"WHERE\r\n" + 
					"    gdl.GRN_HDR_ID = '"+grnHdrId+"'\r\n" +
					" AND IS_LATEST=1\r\n" +
					"        AND gdl.TENANT_ID = '"+tenantId+"'";

			returnList = this.jdbcTemplate.query(retQry, new GrnDtlRowMapper());

		} catch (Exception ex) {
			logger.error("getGrnDtlwithMaterialInwardDtl error---> " + ex);
		}
		return returnList;
	}

	@Override
	public EnquiryCodeWithId insertGrnHdr(String createdBy, String grnDate, String miId, String tenantId,
			String invLocation, String poId) {
		logger.debug("insertGrnHdrAndDtl   method Start");
		EnquiryCodeWithId enqCode = new EnquiryCodeWithId();
		int insertRes = 0;
		try {
			int grnHdrId = iMaterialInwardDAO.getGrnHdr(miId, poId, tenantId);
			if(grnHdrId ==0) {
			
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
					"grn_hdr", "5", jdbcTemplate, 1,1,null,0);
			// insert poHtr
			String insertQ = "INSERT INTO grn_hdr (GRN_CODE, TRANSACTION_NO, FINANCIAL_YEAR_MST_ID, MI_ID,PO_ID, "
					+ "GRN_DATE, CREATED_ON, CREATED_BY, LAST_UPDATED_ON, LAST_UPDATED_BY, TENANT_ID,INVENTORY_LOCATION_CODE) "
					+ "VALUES (?,?,?,?,?,?,NOW(),?,NOW(),?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, gen.getEnquiryCode());
					ps.setString(2, String.valueOf(gen.getSeq()));
					ps.setString(3, gen.getFinainceId());
					ps.setString(4, miId);
					ps.setString(5, poId);
					ps.setString(6, grnDate);
					ps.setString(7, createdBy);
					ps.setString(8, createdBy);
					ps.setString(9, tenantId);
					ps.setString(10, invLocation);
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();
			enqCode.setId(insertRes);
			enqCode.setEnquiryCode(gen.getEnquiryCode());
			}else {
				enqCode.setId(grnHdrId);
				enqCode.setEnquiryCode(iMaterialInwardDAO.getGrnEnqCode(Integer.toString(grnHdrId)));
				
			}
		} catch (Exception ex) {
			logger.error("insertGrnHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertGrnHdrAndDtl   method end");
		return enqCode;
	}
	@Override
	public void updatePoCodeInGrnHdr(int grnHdrId, String poCode) {
		String sql = "UPDATE grn_hdr SET PO_CODE = ? WHERE GRN_HDR_ID = ?";
		jdbcTemplate.update(sql, poCode, grnHdrId);
	}


	@Override
	public int insertGrnDtl(int grnHdrId, String miDtlId, String recivedQty, String tenantId, String poDtlId,
			String indentDtlId, String pmHdrId, String productCode, String invLocation, String createdBy,
			String enquiryCode,String productId) {
		int insertStatus = 0;

		try {
			
			miDtlId = (miDtlId == null || miDtlId.trim().isEmpty() || miDtlId.trim().equalsIgnoreCase("null")) ? "0" : miDtlId;
			poDtlId = (poDtlId == null || poDtlId.trim().isEmpty() || poDtlId.trim().equalsIgnoreCase("null")) ? "0" : poDtlId;
			indentDtlId = (indentDtlId == null || indentDtlId.trim().isEmpty() || indentDtlId.trim().equalsIgnoreCase("null")) ? "0" : indentDtlId;
			productId = (productId == null || productId.trim().isEmpty() || productId.trim().equalsIgnoreCase("null")) ? "0" : productId;
			String qry = "INSERT INTO grn_dtl (GRN_HDR_ID, MI_DTL_ID,PO_DTL_ID,INDENT_DTL_ID, RECEIVED_QTY, TENANT_ID, PRODUCT_ID) VALUES (?,?,?,?,?,?,?)";
			
			
			insertStatus = this.jdbcTemplate.update(qry, grnHdrId, miDtlId, poDtlId, indentDtlId, recivedQty, tenantId,productId);
			

			CommonMethod.updateProductInvDtl(pmHdrId, productId, invLocation, new BigDecimal(recivedQty), "",
					"ITTC0014", enquiryCode, createdBy, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);

		} catch (Exception ex) {
			logger.error("insertGrnDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int insertQualityInspecRequest(String poId, String poCode, String poDtlId, String qtyToBeInspected,
			String qtyInspected, String tenantId, String indentDtlId, String pmHdrId, String reqfrom, String miId,String isRework, String empId) {

		if ("0".equals(isRework) && miId != null && !miId.isEmpty()) {
			String checkQry = "SELECT COUNT(*) FROM quality_inspection_request qir" +
				" WHERE qir.MI_DTL_ID = ? AND qir.IS_REWORK = 0 AND qir.IS_LATEST = 1" +
				" AND NOT EXISTS (SELECT 1 FROM quality_inspection_hdr qih" +
				" WHERE qih.QI_ID = qir.QI_ID AND qih.CANCEL_FLAG = 1 AND qih.IS_LATEST = 1)";
			int existingCount = this.jdbcTemplate.queryForObject(checkQry, Integer.class, miId);
			if (existingCount > 0) {
				logger.warn("Duplicate QI request blocked for MI_DTL_ID: " + miId);
				return 0;
			}
		}

		GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
				"quality_inspection_request", "5", jdbcTemplate, 1,0,null,0);
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO quality_inspection_request ( QI_CODE, TRANSACTION_NO, FINANCIAL_YEAR_MST_ID, PO_ID, PO_CODE, PO_DTL_ID,INDENT_DTL_ID, QTY_TO_BE_INSPECTED, QTY_INSPECTED, TENANT_ID,PM_HDR_ID,INSPECTION_REQUESTED_DATE,REQUEST_FROM,MI_DTL_ID,IS_REWORK,INSPECTION_REQUESTED_BY) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//	insertStatus = this.jdbcTemplate.update(qry, gen.getEnquiryCode(), gen.getSeq(), gen.getFinainceId(), poId,
		//			poCode, poDtlId, indentDtlId, qtyToBeInspected, qtyInspected, tenantId, pmHdrId,
		//			CommonMethod.getCurrentDateTime(), reqfrom, miId,isRework);

			
			  KeyHolder holder = new GeneratedKeyHolder();
			 
			  this.jdbcTemplate.update(new PreparedStatementCreator() {
			  
			  @Override public PreparedStatement createPreparedStatement(Connection con)
			  throws SQLException { 
				  PreparedStatement ps = con.prepareStatement(qry,Statement.RETURN_GENERATED_KEYS);
			  
			          ps.setString(1, gen.getEnquiryCode());
			          ps.setString(2, Integer.toString(gen.getSeq()) ); 
			          ps.setString(3, gen.getFinainceId()); 
			          ps.setString(4, poId); 
			          ps.setString(5, poCode);
			          ps.setString(6, poDtlId); 
			          ps.setString(7, indentDtlId);
			          ps.setString(8, qtyToBeInspected);
			          ps.setString(9, qtyInspected);
			          ps.setString(10, tenantId);
			          ps.setString(11, pmHdrId); 
			          ps.setString(12, CommonMethod.getCurrentDateTime()); 
			          ps.setString(13, reqfrom);
			          ps.setString(14, miId); 
			          ps.setString(15, isRework);
			          ps.setString(16, empId);
			  
			       return ps;
			   }
			  
		  }, holder); 
			  insertStatus = holder.getKey().intValue();
			 
		} catch (Exception ex) {
			logger.error("insertQualityInspecRequest method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public String getPoCodeByPoId(String poId) {
		String getPoCodeByPoId = "";
		try {
			String getPoCodeByPoIdStr = "select PO_CODE from po_hdr where PO_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getPoCodeByPoIdStr,poId);
			getPoCodeByPoId = resultMap.get("PO_CODE").toString();
		} catch (Exception e) {
			logger.error("getPoCodeByPoId method Error" + e);
		}
		return getPoCodeByPoId;
	}

	@Override
	public String getindentDtlBypoDtlId(String poDtlId) {
		String getindentDtlBypoDtlId = "";
		try {
			String getindentDtlBypoDtlIdStr = "select INDENT_DTL_ID from po_dtl where PO_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getindentDtlBypoDtlIdStr,poDtlId);
			getindentDtlBypoDtlId = resultMap.get("INDENT_DTL_ID").toString();
		} catch (Exception e) {
			logger.error("getindentDtlBypoDtlId method Error" + e);
		}
		return getindentDtlBypoDtlId;
	}

	@Override
	public String getMIIndentDtlBypoDtlId(String poDtlId, String indentDtlId, String tenantId) {
		String getMiIndentDtlBypoDtlId = "";
		try {
			String getindentDtlBypoDtlIdStr = "SELECT CASE WHEN COUNT(*)>0 THEN MI_DTL_ID ELSE '' END AS MI_DTL_ID FROM material_inward_dtl WHERE PO_DTL_ID=? and INDENT_DTL_ID= ? and INSPECTED_QTY <= RECEIVED_QTY and TENANT_ID=? ORDER BY MI_DTL_ID DESC LIMIT 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getindentDtlBypoDtlIdStr,poDtlId,indentDtlId,tenantId);
			
			
//			String getQiDtlId = "SELECT CASE WHEN COUNT(*)>0 THEN 1 ELSE 0 END AS HECK FROM quality_inspection_request req \n\r"
//					+ " left join quality_inspection_hdr hdr on hdr.qi_id = req.qi_id  \r\n"
//					+ " WHERE MI_DTL_ID=? and INDENT_DTL_ID= ? and req.TENANT_ID=? and hdr.cancel_flag !=1";
//			Map<String, Object> resultMap3 = jdbcTemplate.queryForMap(getQiDtlId,resultMap.get("MI_DTL_ID").toString(),indentDtlId,tenantId);
//			int getQiChk = Integer.valueOf(resultMap3.get("HECK").toString());
//			
//			if(getQiChk == 0 ) {
				getMiIndentDtlBypoDtlId = resultMap.get("MI_DTL_ID").toString();
//			}
			
		} catch (Exception e) {
			logger.error("getMIIndentDtlBypoDtlId method Error" + e);
		}
		return getMiIndentDtlBypoDtlId;
	}

	
	@Override
	public String getreceivedqtyBypoDtlId(String poDtlId) {
		String getreceivedqtyBypoDtlId = "";
		try {
			String getreceivedqtyBypoDtlIdStr = "select QTY - INSPECTED_QTY as QTY from po_dtl where PO_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getreceivedqtyBypoDtlIdStr,poDtlId);
			getreceivedqtyBypoDtlId = resultMap.get("QTY").toString();

		} catch (Exception e) {
			logger.error("getreceivedqtyBypoDtlId method Error" + e);
		}
		return getreceivedqtyBypoDtlId;
	}

	public String getqtyByMIDtlId(String miDtlId) {
		String getqtyByMIDtlId = "";
		try {
			String getqtyByMIDtlIdStr = "select RECEIVED_QTY - INSPECTED_QTY AS QTY from material_inward_dtl where MI_DTL_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getqtyByMIDtlIdStr,miDtlId);
			getqtyByMIDtlId = resultMap.get("QTY").toString();
		} catch (Exception e) {
			logger.error("getqtyByMIDtlId method Error" + e);
		}
		return getqtyByMIDtlId;
	}

	@Override
	public String getPmHdrIdByPOId(String poHdrId) {
		String getPmHdrIdByPOId = "";
		try {
			String getPmHdrIdByPOIdStr = "SELECT \r\n"
					+ " case when Count(*)>0 then  ihdr.PROJECT_ID else '' end AS PROJECT_ID \r\n" + "FROM\r\n"
					+ "    po_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    indent_hdr ihdr ON hdr.INDENT_ID = ihdr.INDENT_ID\r\n" + "WHERE\r\n" + "    hdr.PO_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getPmHdrIdByPOIdStr,poHdrId);
			getPmHdrIdByPOId = resultMap.get("PROJECT_ID").toString();
		} catch (Exception e) {
			logger.error("getPmHdrIdByPOId method Error" + e);
		}
		return getPmHdrIdByPOId;
	}

	@Override
	public int updateDcDtlReceivedQty(String dcDtlId, String recivedQty) {
		int updateStatus = 0;
		BigDecimal qty = new BigDecimal(recivedQty);
		try {

			String updateqry = "UPDATE dc_dtl SET RECEIVED_QTY=RECEIVED_QTY+? WHERE DC_DTL_ID=?";
			updateStatus = this.jdbcTemplate.update(updateqry,qty,dcDtlId);

		} catch (Exception ex) {
			logger.error("updateDcDtlReceivedQty method Error" + ex);
		}
		return updateStatus;

	}
	@Override
	public int updateDcDtlBin(String dcDtlId, String bin, String productId) {
	    int updateStatus = 0;
	    try {
	    	String updateqry = "UPDATE dc_dtl SET BIN = ?, PRODUCT_ID = ? WHERE DC_DTL_ID = ?";
	    	updateStatus = this.jdbcTemplate.update(updateqry, bin, productId, dcDtlId);
	    } catch (Exception ex) {
	        logger.error("updateDcDtlBin method Error" + ex);
	    }
	    return updateStatus;
	}
//	@Override
//	public int updateDcDtlBin(String dcDtlId, String productId, String bin) {
//	    int updateStatus = 0;
//	    try {
//	        String updateqry;
//
//	        if (productId == null || productId.trim().isEmpty()) {
//	        	logger.info("Updating without productId: dcDtlId=" + dcDtlId + ", bin=" + bin);
//	            updateqry = "UPDATE dc_dtl SET BIN=? WHERE DC_DTL_ID=?";
//	            updateStatus = this.jdbcTemplate.update(updateqry, bin, dcDtlId);
//	        } else {
//	            updateqry = "UPDATE dc_dtl SET BIN=? WHERE DC_DTL_ID=? AND PRODUCT_ID=?";
//	            updateStatus = this.jdbcTemplate.update(updateqry, bin, dcDtlId, productId);
//	        }
//
//	    } catch (Exception ex) {
//	        logger.error("updateDcDtlBin method Error: " + ex);
//	    }
//	    return updateStatus;
//	}
	@Override
	public String getNotifyEmp(String tenantId) {
		// TODO Auto-generated method stub
		String employeeId="";
		try {
			String empQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN GROUP_CONCAT(EMPLOYEE_ID)\r\n" + 
					"        ELSE ''\r\n" + 
					"    END EMPLOYEE_ID\r\n" + 
					"FROM\r\n" + 
					"    employee_mst\r\n" + 
					"WHERE\r\n" + 
					"    DESIGNATION_CODE IN ('DS04' , 'DS11') \r\n"
					+ " and TENANT_ID=?" ;	
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(empQry,tenantId);
			employeeId = resultMap.get("EMPLOYEE_ID").toString();
		}catch(Exception ex) {
			logger.error("getNotifyEmp method Error" + ex);
		}
		return employeeId;
	}

	@Override
	public String getpmHdrId(String poId) {
		// TODO Auto-generated method stub
		String pmHdrId="";
		try {
			String pmQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN pro.PM_HDR_ID\r\n" + 
					"        ELSE ''\r\n" + 
					"    END PM_HDR_ID\r\n" + 
					"FROM\r\n" + 
					"    po_hdr po\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr ind ON po.INDENT_ID = ind.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr pro ON ind.PROJECT_ID = pro.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    po.PO_ID = ?; ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(pmQry,poId);
			pmHdrId = resultMap.get("PM_HDR_ID").toString();
		}catch(Exception ex) {
			logger.error("getpmHdrId method Error" + ex);
		}
		return pmHdrId;
	}

	@Override
	public String getenquiryId(String pmHdrId) {
		// TODO Auto-generated method stub
		String enqId="";
		try {
			String enqQry="select CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN ENQUIRY_ID\r\n" + 
					"        ELSE ''\r\n" + 
					"    END ENQUIRY_ID  from project_hdr where PM_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(enqQry,pmHdrId);
			enqId = resultMap.get("ENQUIRY_ID").toString();
		}catch(Exception ex) {
			logger.error("getenquiryId method Error" + ex);
		}
		return enqId;
	}

	@Override
	public List<ProductDtlsEntity> getProdDtlsByProductCode(String productCode, String pmHdrId, String tenantId) {
		List<ProductDtlsEntity> returnList = new ArrayList<ProductDtlsEntity>();

		try {
		
			String retQry = "SELECT \r\n" + 
					"    PRODUCT_ID, SPECIFICATION, MAKE, PRODUCT_UOM_CODE, MATERIAL,PRODUCT_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    product_mst\r\n" + 
					"WHERE\r\n" + 
					"    PRODUCT_CODE = '"+productCode+"'\r\n" + 
					"        AND PM_HDR_ID = '"+pmHdrId+"'\r\n" + 
					" AND TENANT_ID='"+tenantId+"' LIMIT 1\r\n";
			returnList = this.jdbcTemplate.query(retQry, new ProductDtlsRowMapper());
		} catch (Exception ex) {
			logger.error("getGrnHdrDetails error---> " + ex);
		}
		return returnList;
	}
	@Override
	public List<PoCostTypeEntity> getPoCostType(String isActive, String tenantId){
		List<PoCostTypeEntity> poCostTypeEntityList = new ArrayList<>();
		try{
			String qry = "Select * from po_cost_type where TENANT_ID=? AND IS_ACTIVE = ?";
			poCostTypeEntityList = this.jdbcTemplate.query(qry, new PoCostTypeRowMapper(), tenantId, isActive);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return poCostTypeEntityList;
	}
	@Override
	public String getProductIdByProductCode(String productCode, String pmHdrId, String tenantId) {
		String prodId="";
		try {
			String qry="SELECT \r\n" +
					"    PRODUCT_ID\r\n" +
					"FROM\r\n" +
					"    material_inward_dtl\r\n" +
					"WHERE\r\n" +
					"    MI_DTL_ID = ?\r\n" +
//					"        AND PM_HDR_ID = ?\r\n" +
					"        AND TENANT_ID = ?\r\n" +
					"LIMIT 1;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,productCode,tenantId);
			prodId = resultMap.get("PRODUCT_ID").toString();
		}catch(Exception ex) {
			logger.error("getProductIdByProductCode method Error" + ex);
		}
		return prodId;
	}

	@Override
	public String getQualityHdrId(String pmHdrId, String tenantId) {
		String qHdrId="";
		try {

			String qry = "select case when count(*)>0 then Q_HDR_ID else 0 end as Q_HDR_ID from quality_hdr where PM_HDR_ID= ? and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,tenantId);
			qHdrId = resultMap.get("Q_HDR_ID").toString();

		}catch(Exception ex) {
			logger.error("getQualityHdrId method Error" + ex);
		}
		return qHdrId;
	}

	@Override
	public String grnCancel(GrnCancelReq grnCancelReq){

		try{
			String poCode = grnCancelReq.getPoCode();
			String miId = grnCancelReq.getMiId();
			String tenantId = grnCancelReq.getTenantId();
//			String poIdByPoCode = "select PO_ID from po_hdr where PO_CODE=? AND IS_LATEST = 1 AND TENANT_ID=?";
//			 jdbcTemplate.queryForObject(poIdByPoCode, String.class, poCode, tenantId);

			String poDtlIdQry = "SELECT midtl.PO_DTL_ID, midtl.MI_DTL_ID, midtl.INSPECTED_QTY " +
					"FROM material_inward_hdr mihdr " +
					"INNER JOIN material_inward_dtl midtl ON mihdr.MI_ID = midtl.MI_ID " +
					"WHERE mihdr.MI_ID = ?";
			List<Map<String, Object>> poDtlIds = jdbcTemplate.queryForList(poDtlIdQry, miId);
			for(Map<String, Object> row : poDtlIds) {
				String podtlId = row.get("PO_DTL_ID").toString();
				String miDtlId = row.get("MI_DTL_ID").toString();
				BigDecimal inspectedQty = new BigDecimal(row.get("INSPECTED_QTY").toString());
				String updateRequestQry = "UPDATE quality_inspection_request SET IS_LATEST = 0 WHERE MI_DTL_ID = ? AND TENANT_ID = ?";
				jdbcTemplate.update(updateRequestQry, miDtlId, tenantId);

				String fetchQiIdsQry = "SELECT QI_ID FROM quality_inspection_request WHERE MI_DTL_ID = ? AND TENANT_ID = ?";
				List<String> qiIds = jdbcTemplate.queryForList(fetchQiIdsQry, String.class, miDtlId, tenantId);

//			Update IS_LATEST = 0 in quality_inspection_hdr for each QI_ID
				if (!qiIds.isEmpty()) {
					String inSql = String.join(",", Collections.nCopies(qiIds.size(), "?"));
					String updateHdrQry = "UPDATE quality_inspection_hdr SET IS_LATEST = 0 WHERE QI_ID IN (" + inSql + ") AND TENANT_ID = ?";
					List<Object> params = new ArrayList<>(qiIds);
					params.add(tenantId);

					jdbcTemplate.update(updateHdrQry, params.toArray());
				}

				if(!podtlId.isEmpty()){
				String updateQtyQry = "UPDATE po_dtl " +
						"SET RECEIVED_QTY = RECEIVED_QTY - ?, " +
						"    INSPECTED_QTY = INSPECTED_QTY - ? " +
						"WHERE PO_DTL_ID = ?";
				jdbcTemplate.update(updateQtyQry, inspectedQty, inspectedQty, podtlId);
				}
			}
				String updMIHdrQry = "UPDATE material_inward_hdr set IS_LATEST=0, STATUS='DS155' where MI_ID = ? AND TENANT_ID = ?";
				jdbcTemplate.update(updMIHdrQry, miId, tenantId);
				String updGrnHdrQry = "UPDATE grn_hdr set IS_LATEST = 0, REMARKS=? where MI_ID = ? AND TENANT_ID = ?";
				jdbcTemplate.update(updGrnHdrQry, grnCancelReq.getRemarks(), miId, tenantId);

		} catch (Exception e) {
			logger.error("grnCancel method Error" + e);
		}
		return "Success";
	}

	@Override
	public String removeQtyFromInvPrdDtl(GrnDtlEntity grnDtl, String inventoryLoc, String tenantId){

		try{
			String updateQtyQry = "UPDATE inventory_product_dtl " +
					"SET PRODUCT_QUANTITY_ON_HAND = PRODUCT_QUANTITY_ON_HAND - ? " +
					"WHERE PRODUCT_ID = ? AND INVENTORY_LOCATION_CODE = ? AND TENANT_ID=?";

			int rowsAffected = jdbcTemplate.update(updateQtyQry, grnDtl.getGrnReceivedQty(), grnDtl.getProductId(), inventoryLoc, tenantId);
			if(rowsAffected == 0){
				return "Failed";
			}

		} catch (Exception e) {
			logger.error("removeQtyFromInvPrdDtl method Error" + e);
		}
		return "success";
	}

	@Override
	public boolean checkCountInInvProdDtl(String productId, String inventoryLoc, String tenantId, String prodQty){

		try{
			String qry = "SELECT \n" +
					"    CAST(CASE \n" +
					"        WHEN PRODUCT_QUANTITY_ON_HAND >= ? THEN 1 \n" +
					"        ELSE 0 \n" +
					"    END AS CHAR) AS IS_PRODUCT_AVAILABLE\n" +
					"FROM inventory_product_dtl \n" +
					"WHERE PRODUCT_ID = ? \n" +
					"  AND INVENTORY_LOCATION_CODE = ?;";

			String isProdAvailable = jdbcTemplate.queryForObject(qry, String.class, prodQty, productId, inventoryLoc);
			if(isProdAvailable.equals("1")){
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> getGrnHdrIdByPoId(String poId, String tenantId){
		List<String> grnIds = new ArrayList<>();
		try {
			String qry = "select GRN_HDR_ID from grn_hdr where PO_ID= ? and TENANT_ID = ?";
			grnIds = jdbcTemplate.queryForList(qry, String.class, poId, tenantId);

		} catch (Exception e) {
			logger.error("getGrnHdrIdByPoId method Error" + e);
		}
		return grnIds;
	}

	@Override
	public List<GrnDtlsEntity> getgrnlistForDebitNote(List<String> poDtlIds, String tenantId) {
		// TODO Auto-generated method stub
		List<GrnDtlsEntity> response=new ArrayList<GrnDtlsEntity>();
		try {
			if (poDtlIds == null || poDtlIds.isEmpty()) {
				return response; // return empty list if no IDs provided
			}
			String poDetailIds = poDtlIds.stream().map(id -> "?").collect(Collectors.joining(","));
			String responseQry = "SELECT " +
					"hdr.GRN_CODE, " +
					"hdr.GRN_DATE, " +
					"dtl.RECEIVED_QTY " +
					"FROM grn_hdr hdr " +
					"INNER JOIN grn_dtl dtl ON hdr.GRN_HDR_ID = dtl.GRN_HDR_ID " +
					"INNER JOIN po_dtl podtl ON dtl.PO_DTL_ID = podtl.PO_DTL_ID " +
					"WHERE dtl.PO_DTL_ID IN (" + poDetailIds + ") " +
					"AND hdr.TENANT_ID = ?";
			List<Object> params = new ArrayList<>(poDtlIds);
			params.add(tenantId);

			response = jdbcTemplate.query(responseQry, params.toArray(), new GrnDtlsRowMapper());
		}catch(Exception ex) {
			logger.error("getgrnlist Method Exception "+ex);
		}
		return response;
	}

	public String getProductIdAndProductCode(String productCode, String description, String specification, String unitRate,
			String projectId, String tenantId) {
		String prodId="";
		try {
			String qry="SELECT \r\n" +
					"    PRODUCT_ID\r\n" +
					"FROM\r\n" +
					"    product_mst\r\n" +
					"WHERE\r\n" +
					"    PRODUCT_CODE = ?\r\n" +
					"        AND PRODUCT_DESCRIPTION = ?\r\n" +
					"        AND SPECIFICATION = ?\r\n" +
//					"        AND PRODUCT_COST_PER_UNIT = ?\r\n" +
					"        AND PM_HDR_ID = ?\r\n" +
					"        AND TENANT_ID = ?\r\n" +
					"LIMIT 1;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,productCode,description,specification,projectId,tenantId);
			prodId = resultMap.get("PRODUCT_ID").toString();
		}catch(Exception ex) {
			logger.error("getProductIdByProductCode method Error" + ex);
		}
		return prodId;
	}

}
