package com.vmfg.scm.dao.impl;

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

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.scm.dao.interfaces.IMaterialInwardDAO;
import com.vmfg.scm.entity.MaterialInwardDtlEntity;
import com.vmfg.scm.entity.MaterialInwardHdrEntity;
import com.vmfg.scm.rowmapper.MaterialInwardDtlRowMapper;
import com.vmfg.scm.rowmapper.MaterialInwardHdrRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class MaterialInwardDAO implements IMaterialInwardDAO {
	private static final Logger logger = LoggerFactory.getLogger(IndentUploadDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<MaterialInwardHdrEntity> getMaterialInwardHdrDtls(String poId, String tenantId, String pmHdrId, String fromDate, String toDate) {
	    List<MaterialInwardHdrEntity> list = new ArrayList<MaterialInwardHdrEntity>();
	    try {
	        String pmhdrStr = "";
	        if (pmHdrId.equalsIgnoreCase("getAll") || pmHdrId.equalsIgnoreCase("")) {
	            pmhdrStr = "mnh.PM_HDR_ID like '%%'";
	        } else {
	            pmhdrStr = "mnh.PM_HDR_ID = '" + pmHdrId + "'";		
	        }

	        String porStr = "";
	        if (poId.equalsIgnoreCase("getAll") || poId.equalsIgnoreCase("")) {
	            porStr = "(mnh.PO_ID like '%%' or mnh.PO_ID is null)";
	        } else {
	            porStr = "mnh.PO_ID = '" + poId + "'";
	        }

	        String qry = "SELECT \r\n" + 
	        		"    phdr.PROJECT_CODE,\r\n" + 
	        		"    phdr.PROJECT_NAME,\r\n" + 
	        		"    mnh.*,\r\n" + 
	        		"    VENDOR_NAME,\r\n" + 
	        		"    emp.EMPLOYEE_FIRSTNAME,\r\n" + 
	        		"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
	        		"    INVENTORY_LOCATION_DESCRIPTION,\r\n" + 
	        		"    grn.GRN_CODE,\r\n" + 
	        		"    grn.GRN_DATE,\r\n" + 
	        		"    CASE WHEN STATUS = 'DS085' THEN 1 ELSE 0 END AS INV_FLAG,\r\n" + 
	        		"(SELECT \r\n" + 
	        		"  CASE \r\n" + 
	        		"    WHEN \r\n" + 
	        		"      (SELECT SUM(RECEIVED_QTY)\r\n" + 
	        		"       FROM material_inward_dtl dtl \r\n" + 
	        		"       INNER JOIN material_inward_hdr hdr ON hdr.MI_ID = dtl.MI_ID\r\n" + 
	        		"       WHERE hdr.MI_ID = mnh.MI_ID)\r\n" + 
	        		"    =\r\n" + 
	        		"      (SELECT SUM(req.QTY_TO_BE_INSPECTED)\r\n" + 
	        		"       FROM material_inward_dtl dtl \r\n" + 
	        		"       INNER JOIN quality_inspection_request req ON req.MI_DTL_ID = dtl.MI_DTL_ID\r\n" + 
	        		"       WHERE dtl.MI_ID = mnh.MI_ID ) - (select (COALESCE(SUM(hdr.NOK_QTY+hdr.REWORK_QTY+hdr.REWORK_INTERNAL+hdr.REWORK_VENDOR+hdr.REJECTED_INTERNAL+hdr.REJECTED_EXTERNAL),0) - COALESCE(SUM(ca.QTY),0)) \r\n" +
	        		"  + COALESCE((select sum(qh.INSPECTION_QTY) from quality_inspection_hdr qh where qh.QI_HDR_ID IN (SELECT hdr2.QI_HDR_ID\r\n" +
	        		"            FROM material_inward_dtl dtl2\r\n" + 
	        		"            INNER JOIN quality_inspection_request req2 ON req2.MI_DTL_ID = dtl2.MI_DTL_ID\r\n" + 
	        		"            LEFT JOIN quality_inspection_hdr hdr2 ON hdr2.QI_ID = req2.QI_ID AND hdr2.IS_COMPLETED = 1\r\n" + 
	        		"            WHERE dtl2.MI_ID = mnh.MI_ID) and CANCEL_FLAG =1),0) \r\n" +
	        		"        from material_inward_dtl dtl INNER JOIN quality_inspection_request req ON req.MI_DTL_ID = dtl.MI_DTL_ID \r\n" + 
	        		"        left join quality_inspection_hdr hdr on hdr.QI_ID = req.QI_ID AND hdr.IS_COMPLETED =1 left join quality_ca_dtl ca on hdr.QI_HDR_ID = ca.QI_HDR_ID and IS_APPROVED=0 where dtl.MI_ID = mnh.MI_ID)\r\n" + 
	        		"    THEN 1\r\n" + 
	        		"    ELSE 2\r\n" + 
	        		"  END) as SEQ_FLAG \r\n" +
	        		"FROM\r\n" + 
	        		"    material_inward_hdr mnh\r\n" + 
	        		"LEFT JOIN vendor_mst ven ON mnh.VENDOR_CODE = ven.VENDOR_CODE\r\n" + 
	        		"LEFT JOIN employee_mst emp ON mnh.CREATED_BY = emp.EMPLOYEE_ID\r\n" + 
	        		"LEFT JOIN document_status_type_code doc ON mnh.STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
	        		"LEFT JOIN inventory_location_mst illm ON illm.INVENTORY_LOCATION_CODE = mnh.INVENTORY_LOCATION_CODE\r\n" + 
	        		"LEFT JOIN grn_hdr grn ON grn.MI_ID = mnh.MI_ID AND grn.IS_LATEST = 1\r\n" + 
	        		"INNER JOIN project_hdr phdr ON mnh.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
	        		"WHERE\r\n" + 
	        		"    "+pmhdrStr+" AND  "+porStr+" \r\n" + 
	        		"    AND mnh.TENANT_ID = ?\r\n" + 
	        		"    AND DATE(mnh.INWARD_DATE) BETWEEN ? AND ?";
	        list = this.jdbcTemplate.query(qry, new MaterialInwardHdrRowMapper(), tenantId, fromDate, toDate);

	    } catch (Exception e) {
	        logger.error("getMaterialInwardHdrDtls Method Exception --->" + e);
	    }
	    return list;
	}

	@Override
	public List<MaterialInwardDtlEntity> getMaterialInwardDtlList(String poId, String tenantId) {
		List<MaterialInwardDtlEntity> list = new ArrayList<MaterialInwardDtlEntity>();
		try {
			String qry = "SELECT \r\n" + "    mi.*,uom.UOM_LONG_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    material_inward_dtl mi\r\n" + "        INNER JOIN\r\n"
					+ "    uom_mst uom ON mi.UOM = uom.UOM_CODE\r\n" + "WHERE\r\n" + "    MI_ID = ? and mi.TENANT_ID=?";
			list = this.jdbcTemplate.query(qry, new MaterialInwardDtlRowMapper(), poId, tenantId);

		} catch (Exception e) {
			logger.error("getMaterialInwardDtlList Method Exception --->" + e);
		}
		return list;
	}
	
	@Override
	public List<MaterialInwardDtlEntity> getMiDtlByPoDtlId(String poDtlId) {
		List<MaterialInwardDtlEntity> list = new ArrayList<MaterialInwardDtlEntity>();
		try {
			String qry = "select * from material_inward_dtl mid " +
						 "INNER JOIN material_inward_hdr mih ON mid.MI_ID = mih.MI_ID " +
						 "where mih.IS_LATEST=1 AND PO_DTL_ID=? AND mih.TENANT_ID='bgrn'";
			list = this.jdbcTemplate.query(qry, new MaterialInwardDtlRowMapper(), poDtlId);

		} catch (Exception e) {
			logger.error("getMiDtlByPoDtlId Method Exception --->" + e);
		}
		return list;
	}

	public List<MaterialInwardDtlEntity> getMaterialInwardDtlListByMiDtlId(String miDtlId, String tenantId) {
		List<MaterialInwardDtlEntity> list = new ArrayList<MaterialInwardDtlEntity>();
		try {
			String qry = "SELECT \r\n" + "    mi.*,uom.UOM_LONG_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    material_inward_dtl mi\r\n" + "        INNER JOIN\r\n"
					+ "    uom_mst uom ON mi.UOM = uom.UOM_CODE\r\n" + "WHERE\r\n"
					+ "    MI_DTL_ID = ? and mi.TENANT_ID=?";
			list = this.jdbcTemplate.query(qry, new MaterialInwardDtlRowMapper(), miDtlId, tenantId);

		} catch (Exception e) {
			logger.error("getMaterialInwardDtlList Method Exception --->" + e);
		}
		return list;
	}

	@Override
	public int insertMaterialInwardHdr(String tenantId, String poId, String poCode, String inwardDate,
			String vendorCode, String dcDate, String noOfParts, String status, String empId, String dcNo,
			String invLocation,String projectCode,String inwardRating,String relationShipRating, String isCompleted ) {
		int miId = 0;
		try {
			// comman method
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
					"material_inward_hdr", "5", jdbcTemplate, 1,1,null,0);

			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO material_inward_hdr (MI_CODE, TRANSACTION_NO,"
						+ " FINANCIAL_YEAR_MST_ID, PO_ID, PO_CODE, DC_ID, DC_CODE, INWARD_DATE, "
						+ "VENDOR_CODE, DC_NO, DC_DATE, NO_OF_PARTS, STATUS, IS_COMPLETED, CREATED_ON, CREATED_BY, "
						+ "LAST_UPDATED_ON, LAST_UPDATED_BY, TENANT_ID,INVENTORY_LOCATION_CODE,PM_HDR_ID,INWARD_RATING,RELATIONSHIP_RATING) VALUES (?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, gen.getEnquiryCode());
					ps.setString(2, String.valueOf(gen.getSeq()));
					ps.setString(3, gen.getFinainceId());
					ps.setString(4, poId);
					ps.setString(5, poCode);
					ps.setString(6, "0");
					ps.setString(7, null);
					ps.setString(8, inwardDate);
					ps.setString(9, vendorCode);
					ps.setString(10, dcNo);
					ps.setString(11, dcDate);
					ps.setString(12, noOfParts);
					ps.setString(13, status);
					ps.setString(14, isCompleted);
					ps.setString(15, CommonMethod.getCurrentDateTime());
					ps.setString(16, empId);
					ps.setString(17, CommonMethod.getCurrentDateTime());
					ps.setString(18, empId);
					ps.setString(19, tenantId);
					ps.setString(20, invLocation);
					ps.setString(21, projectCode);
					ps.setString(22, inwardRating);
					ps.setString(23, relationShipRating);
					return ps;
				}
			}, holder);

			miId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertMaterialInwardHdr method Error" + ex);
		}
		return miId;

	}

	@Override
	public int insertInMiDtl(String miId, String poDtlId, String indentDtlId, String orderedQty, String receivedQty,
			String inspectedQty, String uom, String tenantId, String projectId, String productCode, String miCode,
			String updatedBy, String invLocation, String remarks,String productID) {
		int miDtlId = 0;
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO material_inward_dtl (MI_ID, PO_DTL_ID, INDENT_DTL_ID, ORDERED_QTY, "
						+ "RECEIVED_QTY, INSPECTED_QTY, UOM, TENANT_ID,REMARKS,PRODUCT_ID) VALUES (?, ?, ?, ?,?, ?, ?,?, ?, ?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, miId);
					ps.setString(2, poDtlId);
					ps.setString(3, indentDtlId);
					ps.setString(4, orderedQty);
					ps.setString(5, receivedQty);
					ps.setString(6, inspectedQty);
					ps.setString(7, uom);
					ps.setString(8, tenantId);
					ps.setString(9, remarks);
					ps.setString(10,productID);
					return ps;
				}
			}, holder);

			miDtlId = holder.getKey().intValue();
			BigDecimal inspectQty = new BigDecimal(inspectedQty);
			BigDecimal inwardQty = new BigDecimal(receivedQty);
			if (inwardQty.subtract(inspectQty).compareTo(BigDecimal.ZERO) > 0) {
				CommonMethod.updateProductInvDtl(projectId, productID, invLocation, inwardQty.subtract(inspectQty),
						"", "ITTC0015", miCode, updatedBy, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);
			}
		} catch (Exception ex) {
			logger.error("insertInMiDtl method Error" + ex);
		}
		return miDtlId;

	}

	@Override
	public String getPoQty(String poDtlId) {
		String qty = "";
		try {
			String qry = "select case when QTY is null then 0 else QTY end as QTY from po_dtl where PO_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poDtlId);
			qty = resultMap.get("QTY").toString();
		} catch (Exception e) {
			logger.error("getPoQty method Error" + e);
		}
		return qty;
	}
	

	@Override
	public String getGrnDtlReceivedQty(String poDtlId) {
		String qty = "";
		try {
			String qry = "select case when count(*) >0 then sum(RECEIVED_QTY) else 0 end as QTY from grn_dtl gdtl " +
						 "INNER JOIN grn_hdr ghdr ON ghdr.GRN_HDR_ID = gdtl.GRN_HDR_ID " +
					  	 "where ghdr.IS_LATEST=1 AND PO_DTL_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poDtlId);
			qty = resultMap.get("QTY").toString();
		} catch (Exception e) {
			logger.error("getMIInspectedQty method Error" + e);
		}
		return qty;
	}

	@Override
	public String getPoInspectedQty(String poDtlId) {
		String qty = "";
		try {
			String qry = "SELECT case when INSPECTED_QTY is null then 0 else INSPECTED_QTY end as QTY FROM po_dtl WHERE PO_DTL_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poDtlId);
			qty = resultMap.get("QTY").toString();
		} catch (Exception e) {
			logger.error("getPoInspectedQty method Error" + e);
		}
		return qty;
	}

	@Override
	public int updateReceivedQty(String inwardQty, String poDtlId) {
		int updateStatus = 0;
		try {
			String qry = " UPDATE po_dtl SET RECEIVED_QTY = RECEIVED_QTY + ? WHERE PO_DTL_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, inwardQty, poDtlId);
		} catch (Exception e) {
			logger.error("updateReceivedQty method Error" + e);
		}
		return updateStatus;
	}

	@Override
	public String getMICodeById(String MiId) {
		String getMICodeById = "";
		try {
			String getMICodeByIdStr = "select case when Count(*)>0 then MI_CODE else '' end As Code from material_inward_hdr where MI_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getMICodeByIdStr,MiId);
			getMICodeById = resultMap.get("Code").toString();
		} catch (Exception e) {
			logger.error("updateReceivedQty method Error" + e);
		}
		return getMICodeById;
	}

	@Override
	public String getInvType(String invLocation, String tenantId) {
		String invType = "";
		try {
			String getInTypeQ = "select INVENTORY_LOCATION_TYPE from inventory_location_mst where INVENTORY_LOCATION_CODE=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getInTypeQ,invLocation,tenantId);
			invType = resultMap.get("INVENTORY_LOCATION_TYPE").toString();
		} catch (Exception e) {
			logger.error("getInvType method Error" + e);
		}
		return invType;
	}

	@Override
	public String getInvLocationByType(String getInvType, String tenantId, String invLoc) {
		String invInspLoc = "";
		try {
			String getInTypeQ = "select INVENTORY_LOCATION_CODE from inventory_location_mst where INVENTORY_LOCATION_TYPE = ? and TENANT_ID=? and INVENTORY_LOCATION_CODE !=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getInTypeQ,getInvType,tenantId,invLoc);
			invInspLoc = resultMap.get("INVENTORY_LOCATION_CODE").toString();
		} catch (Exception e) {
			logger.error("getInvType method Error" + e);
		}
		return invInspLoc;
	}

	@Override
	public String getqtyreqcountByMiDtlId(String miDtlId, String tenantId) {
		String getqtyreqcountByMiDtlId = "";
		try {
			String getqtyreqcountByMiDtlIdStr = "select count(*) as count from quality_inspection_request where MI_DTL_ID = ? and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getqtyreqcountByMiDtlIdStr,miDtlId,tenantId);
			getqtyreqcountByMiDtlId = resultMap.get("count").toString();
		} catch (Exception e) {
			logger.error("getqtyreqcountByMiDtlId method Error" + e);
		}
		return getqtyreqcountByMiDtlId;
	}

	@Override
	public int updateMiDtlId(String miDtlId, String poDtlId, String inspectionqty) {
		int updateMiDtlId = 0;
		try {
			if(!inspectionqty.equalsIgnoreCase("0.000")) {
			String updateMiDtlIdStr = "update quality_inspection_request set MI_DTL_ID = ? where PO_DTL_ID = ? and (MI_DTL_ID is null or MI_DTL_ID = '') and QTY_INSPECTED !=0 ";
			updateMiDtlId = this.jdbcTemplate.update(updateMiDtlIdStr, miDtlId, poDtlId);
			}else {
				String updateMiDtlIdStr = "update quality_inspection_request set MI_DTL_ID = ? where PO_DTL_ID = ? and (MI_DTL_ID is null or MI_DTL_ID = '')";
				updateMiDtlId = this.jdbcTemplate.update(updateMiDtlIdStr, miDtlId, poDtlId);
			}
		} catch (Exception e) {
			logger.error("updateMiDtlId method Error" + e);
		}
		return updateMiDtlId;
	}

	@Override
	public void updateBinInprodMst(String productId,String bin) {
		try {
			String qry = "UPDATE product_mst SET BIN=? ,INWARD_DATETIME=? WHERE PRODUCT_ID=?;";
			this.jdbcTemplate.update(qry, bin,CommonMethod.getCurrentDateTime(), productId);
		} catch (Exception e) {
			logger.error("updateBinInprodMst method Error" + e);
		}
	}

	@Override
	public int checkMiQtyStatus(String miDtlId) {
		int status=0 ;
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(RECEIVED_QTY) != SUM(INSPECTED_QTY + NOK_QTY + REWORK_QTY) THEN 0\r\n" + 
					"        ELSE 1\r\n" + 
					"    END as STATUS\r\n" + 
					"FROM\r\n" + 
					"    material_inward_dtl\r\n" + 
					"WHERE\r\n" + 
					"    MI_DTL_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, miDtlId);
			status = Integer.parseInt(resultMap.get("STATUS").toString());
		} catch (Exception e) {
			logger.error("checkMiQtyStatus method Error" + e);
		}
		return status;
	}

	@Override
	public String getBinValue(String prodId) {
		String bin = "";
		try {
			String qry = "select case when BIN is not null then BIN else '' end as BIN from product_mst where PRODUCT_ID=? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,prodId);
			bin = resultMap.get("BIN").toString();
		} catch (Exception e) {
			logger.error("getBinValue method Error" + e);
		}
		return bin;
	}

	@Override
	public String getgrnHdrId(String miId, String invLocation, String poId, String poCode) {
		String id = "";
		try {
			String qry = "select case when count(*)>0 then GRN_HDR_ID else 'NA' end  as ID from grn_hdr where MI_ID=? and INVENTORY_LOCATION_CODE=? and (PO_ID=? or PO_CODE=?);";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,miId,invLocation,poId,poCode);
			id = resultMap.get("ID").toString();
		} catch (Exception e) {
			logger.error("getgrnHdrId method Error" + e);
		}
		return id;
	}

	@Override
	public String getGrnEnqCode(String grnHdrId) {
		String code = "";
		try {
			String qry = "select GRN_CODE from grn_hdr where GRN_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,grnHdrId);
			code = resultMap.get("GRN_CODE").toString();
		} catch (Exception e) {
			logger.error("getGrnEnqCode method Error" + e);
		}
		return code;
	}
	
	@Override
	public int getGrnHdr(String miId,String poId,String tenantId) {
		int grnHdrID = 0;
		try {
			String qry = "select case when count(*) > 0 then GRN_HDR_ID else '0' end AS GRN_HDR_ID from grn_hdr where MI_ID = ? and PO_ID = ? and TENANT_ID = ? limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,miId,poId,tenantId);
			grnHdrID = Integer.parseInt(resultMap.get("GRN_HDR_ID").toString()) ;
		} catch (Exception e) {
			logger.error("getGrnHdr method Error" + e);
		}
		return grnHdrID;
	}

	@Override
	public String getPoCodeByPoId(String tenantId, String poId) {
		String poCode="";
		try {
			
			String qry = "select PO_CODE from po_hdr where PO_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poId,tenantId);
			poCode = resultMap.get("PO_CODE").toString();
		}catch(Exception ex) {
			logger.error("getPoCodeByPoId method Error" + ex);
		}
		
		return poCode;
	}

	@Override
	public int getIndentIdPoId(String tenantId, String poId) {
		int indentId=0;
		try {
			
			String qry = "select INDENT_ID from po_hdr where PO_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poId,tenantId);
			String indentIdString = resultMap.get("INDENT_ID").toString();
			indentId=Integer.parseInt(indentIdString);
			
		}catch(Exception ex) {
			logger.error("getIndentIdPoId method Error" + ex);	
		}
		return indentId;
	}

	@Override
	public int getPmHdrIdByIndentId(String tenantId, int indentId) {
		int pmHdrId=0;
		try {
			
			String qry = "select PROJECT_ID from indent_hdr where INDENT_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId,tenantId);
			String pmHdrIdString = resultMap.get("PROJECT_ID").toString();
			pmHdrId=Integer.parseInt(pmHdrIdString);
			
			
		}catch(Exception ex) {
			logger.error("getPmHdrIdByIndentId method Error" + ex);
		}
		return pmHdrId;
	}

	@Override
	public int getscmHdrIdByPmHdrId(String tenantId, int pmHdrId) {
		int scmHdrId=0;
		try {
			
			String qry = "select SCM_HDR_ID from scm_hdr where PM_HDR_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,tenantId);
			String scmHdrIdString = resultMap.get("SCM_HDR_ID").toString();
			scmHdrId=Integer.parseInt(scmHdrIdString);
			
			
		}catch(Exception ex) {
			logger.error("getscmHdrIdByPmHdrId method Error" + ex);
		}
		return scmHdrId;
	}

}
