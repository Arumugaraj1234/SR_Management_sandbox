package com.vmfg.finance.dao.impl;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.vmfg.finance.dao.interfaces.IPraDAO;
import com.vmfg.finance.entity.GetPraDtlEntity;
import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.finance.entity.PraDtlListEntity;
import com.vmfg.finance.entity.PraDtlsHistoryEntity;
import com.vmfg.finance.entity.PraStatusEntity;
import com.vmfg.finance.request.PraInsertRequest;
import com.vmfg.finance.rowmapper.GetPraDtlRowMapper;
import com.vmfg.finance.rowmapper.GrnDtlsRowMapper;
import com.vmfg.finance.rowmapper.PraDtlListRowMapper;
import com.vmfg.finance.rowmapper.PraDtlsHistoryRowMapper;
import com.vmfg.finance.rowmapper.PraStatusRowMapper;

@Repository
public class PraDAO implements IPraDAO {
	private static final Logger logger = LoggerFactory.getLogger(PraDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getLastPraCode() {
		String praCode="";
		try {
			String qry="SELECT \r\n" + 
					"    CASE \r\n" + 
					"        WHEN COUNT(*) > 0 THEN (SELECT PRA_CODE FROM pra_hdr ORDER BY PRA_ID DESC LIMIT 1)\r\n" + 
					"        ELSE 'PRA001'\r\n" + 
					"    END AS PRA_CODE\r\n" + 
					"FROM \r\n" + 
					"    pra_hdr;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
			praCode = resultMap.get("PRA_CODE").toString();
		}catch(Exception ex) {
			logger.error("getLastPraCode Method Exception "+ex);
		}
		return praCode;
	}

	@Override
	public int InsertPraHdr(PraInsertRequest praInsertRequest,String praCode, String isLast) {
		int praHdrId=0;
		try {

			String insertQ = "INSERT INTO `pra_hdr`\r\n" +
					"(`PRA_CODE`,\r\n" +
					"`PO_ID`,\r\n" +
					"`PO_DATE`,\r\n" +
					"`POT_ID`,\r\n" +
					"`PAYMENT_TERMS`,\r\n" +
					"`PRA_DATE`,\r\n" +
					"`PM_HDR_ID`,\r\n" +
					"`DUE_DATE`,\r\n" +
					"`TYPE_OF_PAYMENT`,\r\n" +
					"`DELIVERY_TYPE`,\r\n" +
					"`VENDOR_CODE`,\r\n" +
					"`GRN_HDR_ID`,\r\n" +
					"`INVOICE_NO`,\r\n" +
					"`INVOICE_DATE`,\r\n" +
					"`ORDER_VALUE`,\r\n" +
					"`INVOICE_VALUE`,\r\n" +
					"`TDS`,\r\n" +
					"`AMOUNT_PAYABLE`,\r\n" +
					"`AMOUNT_DUE`,\r\n" +
					"`RETENTION`,\r\n" +
					"`LD`,\r\n" +
					"`OTHERS`,\r\n" +
					"`STATUS`,\r\n" +
					"`IS_COMPLETED`,\r\n" +
					"`COMPLETED_DATETIME`,\r\n" +
					"`LAST_UPDATED_DATETIME`,\r\n" +
					"`LAST_UPDATED_ON`,\r\n" +
					"`REMARKS`,\r\n" +
					"`TENANT_ID`,`STATUS_CODE`,`PCT_ID`, `IS_LAST`, `TRANSPORT_CHARGE`, `P_F_CHARGE`, `INSURANCE_CHARGE`, `OTHER_CHARGE`, `GST_VALUE`, `IGST_VALUE`)\r\n" +
					"VALUES\r\n" +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, praCode);
					ps.setString(2, praInsertRequest.getPoId());
					ps.setString(3, praInsertRequest.getPoDate());
					ps.setString(4, praInsertRequest.getPotId());
					ps.setString(5, praInsertRequest.getPaymentTerms()+" - "+praInsertRequest.getPercentage()+"%");
					ps.setString(6, praInsertRequest.getPraDate());
					ps.setString(7, praInsertRequest.getPmHdrId());
					ps.setString(8, praInsertRequest.getDueDate());
					ps.setString(9, praInsertRequest.getTypeOfPayment());
					ps.setString(10, praInsertRequest.getDeliveryType());
					ps.setString(11, praInsertRequest.getVendorCode());
					ps.setString(12, praInsertRequest.getGrnHdrId());
					ps.setString(13, praInsertRequest.getInvoiceNumber());
					ps.setString(14, praInsertRequest.getInvoiceDate());
					ps.setString(15, praInsertRequest.getOrderValue());
					ps.setString(16, praInsertRequest.getInvoiceValue());
					ps.setString(17, praInsertRequest.getTds());
					ps.setString(18, praInsertRequest.getAmountPayable());
					ps.setString(19, praInsertRequest.getAmountPayable());
					ps.setString(20, praInsertRequest.getRetention());
					ps.setString(21, praInsertRequest.getLd());
					ps.setString(22, praInsertRequest.getOthers());
					ps.setString(23, praInsertRequest.getStatus());
					ps.setString(24, praInsertRequest.getIsCompleted());
					ps.setString(25, praInsertRequest.getCompletedDateTime());
					ps.setString(26, praInsertRequest.getLastUpdatedDateTime());
					ps.setString(27, praInsertRequest.getLastUpdatedOn());
					ps.setString(28, praInsertRequest.getRemarks());
					ps.setString(29, praInsertRequest.getTenantId());
					ps.setString(30, praInsertRequest.getStatusCode());
					ps.setString(31, praInsertRequest.getPoCostType());
					ps.setString(32, isLast);
					ps.setString(33, praInsertRequest.getTransportValue());
					ps.setString(34, praInsertRequest.getPfValue());
					ps.setString(35, praInsertRequest.getInsuranceValue());
					ps.setString(36, praInsertRequest.getOtherValue());
					ps.setString(37, praInsertRequest.getGst());
					ps.setString(38, praInsertRequest.getIgst());
					return ps;
				}
			}, holder);
			praHdrId = holder.getKey().intValue();

		}catch(Exception ex) {
			logger.error("InsertPraHdr Method Exception "+ex);
		}
		return praHdrId;
	}

//	@Override
//	public int InsertPraDtl(String grnDtlId, int praHdrId) {
//		int res=0;
//		try {
//			String qry="INSERT INTO `pra_dtl`\r\n" + 
//					"(`PRA_ID`,\r\n" + 
//					"`GRN_DTL_ID`)\r\n" + 
//					"VALUES\r\n" + 
//					"(?,?);";
//			res = this.jdbcTemplate.update(qry,praHdrId,grnDtlId);
//		}catch(Exception ex) {
//			logger.error("InsertPraDtl Method Exception "+ex);
//		}
//		return res;
//	}
	
	@Override
	public int InsertPraDtl(String grnDtlId, int praHdrId) {
	    int praDtlId = 0;
	    try {
	        String qry = "INSERT INTO `pra_dtl` (`PRA_ID`, `GRN_DTL_ID`) VALUES (?, ?)";
	        this.jdbcTemplate.update(qry, praHdrId, grnDtlId);

	        String lastInsertedIdQuery = "SELECT LAST_INSERT_ID()";
	        praDtlId = this.jdbcTemplate.queryForObject(lastInsertedIdQuery, Integer.class);
	    } catch (Exception ex) {
	        logger.error("InsertPraDtl Method Exception " + ex);
	    }
	    return praDtlId;
	}



	@Override
	public String grnDtlList(String grnHdrId, String tenantId) {
		String res="";
		try {
			String qry="select GROUP_CONCAT(GRN_DTL_ID) as GRN_DTL_ID from grn_dtl where GRN_HDR_ID in(?) and TENANT_ID = ? ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,grnHdrId,tenantId);
			res = resultMap.get("GRN_DTL_ID").toString();
		}catch(Exception ex) {
			logger.error("grnDtlList Method Exception "+ex);
		}
		return res;
	}

	@Override
	public int updatePraHdr(String invoiceNo, String invoiceDate, String transportValue, String pfValue, String insuranceValue, String otherValue, String reamarks,String tds,String amountPayable,String retention, String ld, String others ,String praId) {
		int res=0;
		try {
			String qry="update pra_hdr set INVOICE_DATE = ?, REMARKS = ? , TDS = ? ,AMOUNT_PAYABLE  = ? , AMOUNT_DUE  = ?, RETENTION = ? ,LD = ?, TRANSPORT_CHARGE = ?, P_F_CHARGE = ?, INSURANCE_CHARGE = ?, OTHER_CHARGE = ?, OTHERS = ?  where PRA_ID = ?";
			res = this.jdbcTemplate.update(qry,invoiceDate,reamarks,tds,amountPayable,amountPayable, retention, ld, transportValue, pfValue, insuranceValue, otherValue, others, praId);
		}catch(Exception ex) {
			logger.error("updatePraHdr Method Exception "+ex);
		}
		return res;
	}
	@Override
	public int updPraAfterPoCancel(String cancelPoId, String newPoId, String tenantId) {
		int res=0;

		try {
			String qry="update pra_hdr set PO_ID=?, IS_LATEST=1 where PO_ID = ? AND TENANT_ID=?";
			res = this.jdbcTemplate.update(qry, newPoId, cancelPoId, tenantId);
		}catch(Exception ex) {
			logger.error("updatePraHdr Method Exception "+ex);
		}
		return res;
	}

	@Override
	public List<GetPraDtlEntity> getPraDtlList(String praId, String tenantId) {
		// TODO Auto-generated method stub
		List<GetPraDtlEntity> list = new ArrayList<GetPraDtlEntity>();
		try {

			String query = "SELECT \n"
			        + "    hdr.*, \n"
			        + "    hdr.RETENTION, hdr.LD, hdr.OTHERS, \n"  
			        + "    pohdr.PO_CODE, pohdr.REVISION, pohdr.REVISION_DATE, \n"
			        + "    vm.VENDOR_NAME, \n"
			        + "    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION, \n"
			        + "    phdr.PROJECT_CODE, phdr.PROJECT_NAME, \n"
			        + "    poh.TOTAL_VALUE, \n"
			        + "    poh.PO_TYPE, \n"
			        + "    hdr.INVOICE_VALUE, \n"
			        + "    SUM(pod.QTY) AS PO_QTY, \n"
			        + "    pohdr.BASIC_TOTAL, \n"
			        + "    pohdr.GST, \n"
			        + "    hdr.DUE_DATE \n"
			        + "FROM \n"
			        + "    pra_hdr hdr \n"
			        + "INNER JOIN po_hdr pohdr ON pohdr.PO_ID = hdr.PO_ID \n"
			        + "INNER JOIN vendor_mst vm ON vm.VENDOR_CODE = hdr.VENDOR_CODE \n"
			        + "INNER JOIN document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = hdr.STATUS_CODE \n"
			        + "INNER JOIN project_hdr phdr ON phdr.PM_HDR_ID = hdr.PM_HDR_ID \n"
			        + "INNER JOIN po_hdr poh ON poh.PO_ID = hdr.PO_ID \n"
			        + "INNER JOIN po_dtl pod ON pod.PO_ID = poh.PO_ID \n"
			        + "WHERE hdr.PRA_ID = ? AND hdr.TENANT_ID = ? \n"
			        + "GROUP BY poh.PO_ID";

			list = this.jdbcTemplate.query(query, new GetPraDtlRowMapper(), praId, tenantId);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getPraDtlList Error" + e);
		}

		return list;
	}

	@Override
	public List<PraStatusEntity> getPraStatusList(String praId){
		List<PraStatusEntity> praStatusEntityList = new ArrayList<>();
		try{
			String qry = "SELECT \n" +
					"    pras.*, \n" +
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION, \n" +
					"    emp.EMPLOYEE_FIRSTNAME\n" +
					"FROM \n" +
					"    pra_status pras\n" +
					"INNER JOIN \n" +
					"    document_status_type_code doc ON pras.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\n" +
					"INNER JOIN \n" +
					"    employee_mst emp ON pras.UPDATED_BY = emp.EMPLOYEE_ID\n" +
					"WHERE \n" +
					"    pras.PRA_ID = ?\n";
			praStatusEntityList = this.jdbcTemplate.query(qry, new PraStatusRowMapper(), praId);
		}
		catch (Exception ex){
			logger.error("getPraStatusList Method Exception --->" + ex);
		}
		return  praStatusEntityList;
	}

	@Override
	public int praCancel(String praId, String tenantId, String empId) {
		int update = 0;
		try {
			String qry1 = "UPDATE pra_hdr SET IS_LATEST = 0 WHERE PRA_ID = ? AND TENANT_ID = ?;";
			update = jdbcTemplate.update(qry1, praId, tenantId);

			String qry2 = "UPDATE po_payment_term ppt "
					+ "JOIN pra_hdr ph ON ppt.POT_ID = ph.POT_ID "
					+ "SET ppt.PENDING_AMOUNT = ppt.PENDING_AMOUNT + ph.AMOUNT_PAYABLE "
					+ "WHERE ph.PRA_ID = ? AND ph.TENANT_ID = ?;";
			jdbcTemplate.update(qry2, praId, tenantId);

			// DC077 lifecycle's cancel step (DSM_ID 3104: DOC_STATUS=DS168 "PRA Cancel", CURR_SEQUENCE=3)
			// Only log history when a real pra_hdr row was actually cancelled (guards against
			// praId values that don't correspond to any PRA, e.g. a caller passing a foreign ID)
			if (update > 0) {
				insertPraStatusDtl(praId, "3", "DS168", tenantId, "Cancelled", empId);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return update;
	}


	@Override
	public List<String> getPraIdsByPoId(String poId, String tenantId){

		try{
			String qry = "SELECT PRA_ID FROM pra_hdr WHERE PO_ID = ? AND TENANT_ID = ?";
			return jdbcTemplate.queryForList(qry, new Object[]{poId, tenantId}, String.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<PraDtlListEntity> getpraSubList(String praId, String tenantId) {
		List<PraDtlListEntity> list = new ArrayList<PraDtlListEntity>();
		try {

			String query = "SELECT \n" +
					"    dtl.*, pm.PRODUCT_DESCRIPTION\n" +
					"FROM\n" +
					"    pra_dtl dtl\n" +
					"    LEFT JOIN pra_hdr hdr ON dtl.PRA_ID = hdr.PRA_ID\n" +
					"    INNER JOIN grn_dtl gdtl ON gdtl.GRN_DTL_ID = dtl.GRN_DTL_ID\n" +
					"    INNER JOIN product_mst pm ON pm.PRODUCT_ID = gdtl.PRODUCT_ID\n" +
					"WHERE\n" +
					"    hdr.PRA_ID = ? AND hdr.TENANT_ID = ?\n";
			list = this.jdbcTemplate.query(query, new PraDtlListRowMapper(), praId, tenantId);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getPraDtlList Error" + e);
		}

		return list;
	}

	@Override
	public List<GetPraDtlEntity> getPraHdrListByPmHdrId(String pmHdrId, String poId, String tenantId) {
		// TODO Auto-generated method stub
				List<GetPraDtlEntity> list = new ArrayList<GetPraDtlEntity>();
				try {

					String query = "SELECT DISTINCT \r\n" + 
							"    hdr.*,\r\n" + 
							"    pohdr.PO_CODE, pohdr.REVISION, pohdr.REVISION_DATE,\r\n" + 
							"    vm.VENDOR_NAME,\r\n" + 
							"    pohdr.GST,\r\n" +                
							"    pohdr.BASIC_TOTAL,\r\n" +
							"    phdr.PROJECT_CODE,\r\n" + 
							"    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,   phdr.PROJECT_NAME,\r\n" + 
							"    poh.TOTAL_VALUE,\r\n" + 
							"    poh.PO_TYPE,\r\n" + 
							"    hdr.INVOICE_VALUE\r\n" + 
							"FROM\r\n" + 
							"    pra_hdr hdr\r\n" + 
							"        INNER JOIN\r\n" + 
							"    po_hdr pohdr ON pohdr.PO_ID = hdr.PO_ID\r\n" + 
							"        INNER JOIN\r\n" + 
							"    vendor_mst vm ON vm.VENDOR_CODE = hdr.VENDOR_CODE\r\n" + 
							"        INNER JOIN\r\n" + 
							"    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = hdr.STATUS_CODE\r\n" + 
							"        INNER JOIN\r\n" + 
							"    project_hdr phdr ON phdr.PM_HDR_ID = hdr.PM_HDR_ID inner join\r\n" + 
							"    po_hdr poh on poh.PO_ID=hdr.PO_ID\r\n" + 
							"    inner join\r\n" + 
							"    po_dtl pod on pod.PO_ID=poh.PO_ID\r\n" + 
							" WHERE\r\n" + 
							"    hdr.PO_ID LIKE '"+poId+"'\r\n" +
							"        AND hdr.PM_HDR_ID LIKE '"+pmHdrId+"'\r\n" +
							"        AND hdr.TENANT_ID = ?";
					list = this.jdbcTemplate.query(query, new GetPraDtlRowMapper(), tenantId);
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("getPraHdrListByPmHdrId Error" + e);
				}

				return list;
	}

	@Override
	public int updatePraHdrSeq(String seq, String seqCode, String islast, String lastCurr,String praId) {
		int res=0;
		try {
			if(islast.equalsIgnoreCase("0")) {
			String qry="UPDATE `pra_hdr` SET `STATUS`= ? , `STATUS_CODE`= ? WHERE `PRA_ID`=? ";
			res = this.jdbcTemplate.update(qry,seq,seqCode,praId);
			}else {
				String qry="UPDATE `pra_hdr` SET `STATUS`= ?, `STATUS_CODE`=?, `IS_COMPLETED`= ? , `COMPLETED_DATETIME`= ? WHERE `PRA_ID`=?  ";
				res = this.jdbcTemplate.update(qry,seq,seqCode,islast,lastCurr,praId);
			}
		}catch(Exception ex) {
			logger.error("updatePraHdrSeq Method Exception "+ex);
		}
		return res;
	}
	
	@Override
	public int getMstId(String ProjectId) {
		int res=0;
		try {
			 String mstIdQry="select case when count(*) > 0 then FE_HDR_ID else 0 end as FE_HDR_ID from finance_hdr where PM_HDR_ID = ?";
			 Map<String, Object> result = this.jdbcTemplate.queryForMap(mstIdQry,ProjectId);
			 int mstId = Integer.parseInt(result.get("FE_HDR_ID").toString());
			 res=mstId;
		}catch(Exception ex) {
			logger.error("getMstId Method Exception "+ex);
		}
		return res;
	}

	@Override
	public int insertPraStatusDtl(String poId, String seqNo, String seqStatusCode, String tenantId, String remarks,
								 String empId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO pra_status ( PRA_ID, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
			insertStatus = this.jdbcTemplate.update(qry, poId, seqNo, seqStatusCode, remarks, empId, tenantId);

		} catch (Exception ex) {
			logger.error("insertPraStatus method Error" + ex);
		}
		return insertStatus;
	}

	public String getPoCostTypeDesc(String praId, String tenantId){
	String poCostTypeDesc = "";
		try{
//			String qry = "select CASE when pct.PCT_DESC is not null then pct.PCT_DESC ELSE 'NA' end AS PCT_DESC from pra_hdr hdr \n" +
//					"    Inner join po_payment_term pot ON hdr.POT_ID = pot.POT_ID \n" +
//					"    Inner join po_cost_type pct \n" +
//					"    ON pct.PCT_ID = pot.PCT_ID where PRA_ID = ?";
			String qry="select  CASE when pct.PCT_DESC is not null then pct.PCT_DESC ELSE 'NA' end AS PCT_DESC \n" +
					"from pra_hdr hdr \n" +
					"INNER JOIN po_cost_type pct ON pct.PCT_ID = hdr.PCT_ID \n" +
					"where PRA_ID =? AND hdr.TENANT_ID=?";
			poCostTypeDesc = this.jdbcTemplate.queryForObject(qry, String.class, praId, tenantId);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("No result found for PRA_ID: " + praId + ", returning 'NA'");
		}
		catch (Exception ex) {
			logger.error("getPoCostTypeDesc method Error" + ex);
		}
	return poCostTypeDesc;
	}
	
	@Override
	public int getProjId(String ProjectId) {
		int res=0;
		try {
			 String projIdQry="SELECT \r\n" + 
				 		"    CASE\r\n" + 
				 		"        WHEN COUNT(*) > 0 THEN PROJECT_CODE\r\n" + 
				 		"        ELSE '0'\r\n" + 
				 		"    END AS result\r\n" + 
				 		"FROM\r\n" + 
				 		"    project_hdr\r\n" + 
				 		"WHERE\r\n" + 
				 		"    PM_HDR_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(projIdQry, ProjectId);
			int projId = Integer.parseInt(result.get("result").toString());
			res=projId;
		}catch(Exception ex) {
			logger.error("getMstId Method Exception "+ex);
		}
		return res;
	}

	@Override
	public List<GrnDtlsEntity> getgrnlist(String grnHdrId, String tenantId) {
		// TODO Auto-generated method stub
		List<GrnDtlsEntity> response=new ArrayList<GrnDtlsEntity>();
		try {
			String responseQry="SELECT \r\n" + 
					"    hdr.GRN_CODE,\r\n" + 
					"    hdr.GRN_DATE,\r\n" + 
					"    dtl.RECEIVED_QTY,\r\n" + 
					"    phdr.INVOICE_DATE,\r\n" + 
					"    phdr.INVOICE_NO\r\n" + 
					"FROM\r\n" + 
					"    grn_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    grn_dtl dtl ON hdr.GRN_HDR_ID = dtl.GRN_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"     pra_dtl pdtl ON pdtl.GRN_DTL_ID = dtl.GRN_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    pra_hdr phdr ON phdr.PRA_ID = pdtl.PRA_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.GRN_HDR_ID = ? and hdr.TENANT_ID=?;\r\n";
			response=this.jdbcTemplate.query(responseQry, new GrnDtlsRowMapper(),grnHdrId,tenantId);
		}catch(Exception ex) {
			logger.error("getgrnlist Method Exception "+ex);
		}
		return response;
	}

	@Override
	public List<PraDtlsHistoryEntity> getprahistory(String praId, String tenantId) {
		// TODO Auto-generated method stub
		List<PraDtlsHistoryEntity> result=new ArrayList<>();
		try {
			String resQry="SELECT \r\n" + 
					"    PRA_CODE,\r\n" + 
					"    INVOICE_VALUE,\r\n" + 
					"    STATUS,\r\n" + 
					"    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    pra_hdr phr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code ds ON ds.DOCUMENT_STATUS_TYPE_CODE = phr.STATUS_CODE\r\n" + 
					"WHERE\r\n" + 
					"    PO_ID = (SELECT \r\n" + 
					"            PO_ID\r\n" + 
					"        FROM\r\n" + 
					"            pra_hdr\r\n" + 
					"        WHERE\r\n" + 
					"            PRA_ID = ?)\r\n" + 
					"        AND PRA_ID != ?;";
			
			result=this.jdbcTemplate.query(resQry, new PraDtlsHistoryRowMapper(),praId,praId);
		}catch(Exception ex) {
			logger.error("getprahistory exception "+ex);
		}
		return result;
	}

	@Override
	public String getPotPendingAmnt(String potId) {
		// TODO Auto-generated method stub
		String pendingAmnt="";
		try {
			pendingAmnt = this.jdbcTemplate.queryForObject("Select PENDING_AMOUNT from po_payment_term where POT_ID = ?;", String.class,potId);

		}catch (Exception e) {
			logger.error("getPotPendingAmnt exception "+e);		}
		return pendingAmnt;
	}

	@Override
	public int updatePotPendingAmnt(String potId,String pending) {
		int update = 0;
		
		try {
			update = this.jdbcTemplate.update("update po_payment_term set PENDING_AMOUNT =? where POT_ID = ?;",pending,potId);
		}catch (Exception e) {
			logger.error("updatePotPendingAmnt exception "+e);	
		}
			
		return update;
	}

	@Override
	public String getDtlId(String praHdrId, String PraDtl) {
		// TODO Auto-generated method stub
		String DtlId="";
		try {
			DtlId = this.jdbcTemplate.queryForObject("SELECT \r\n" + 
					"    gdtl.INDENT_DTL_ID\r\n" + 
					"FROM\r\n" + 
					"    pra_dtl pdtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    grn_dtl gdtl ON pdtl.GRN_DTL_ID = gdtl.GRN_DTL_ID\r\n" + 
					"WHERE\r\n" + 
					"    pdtl.PRA_DTL_ID = ?\r\n" + 
					"    AND pdtl.PRA_ID = ?;", String.class, PraDtl, praHdrId);

		}catch (Exception e) {
			logger.error("getDtlId exception "+e);		
		}
		return DtlId;
	}


	@Override
	public List<String> getIndentDtlId(String praId, String tenantId) {
		List<String> dtlIds = new ArrayList<>();
		try {
			dtlIds = this.jdbcTemplate.queryForList(
					"select INDENT_DTL_ID from pra_hdr phdr inner join po_dtl pdtl \r\n" +
					"        on pdtl.PO_ID = phdr.PO_ID\r\n" +
					"        where phdr.PRA_ID = ? and phdr.TENANT_ID = ?",
					String.class, praId, tenantId);
		} catch (Exception e) {
			logger.error("getIndentDtlId exception " + e);
		}
		return dtlIds;
	}

	@Override
	public String getCountAsCompleted(String indentDtl, String tenantId) {
		// TODO Auto-generated method stub
		String dtlId="";
		try {
			dtlId = this.jdbcTemplate.queryForObject("SELECT \r\n" +
					"    CASE\r\n" +
					"        WHEN SUM(pdtl.RECEIVED_QTY) >= idtl.QTY THEN 0\r\n" +
					"        ELSE 1\r\n" +
					"    END AS COUNT\r\n" +
					"FROM\r\n" +
					"    po_dtl pdtl\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_dtl idtl ON pdtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" +
					"WHERE\r\n" +
					"    pdtl.INDENT_DTL_ID = ?\r\n" +
					"        AND EXISTS (\r\n" +
					"            SELECT 1 FROM po_payment_term\r\n" +
					"            WHERE PO_ID = pdtl.PO_ID AND PENDING_AMOUNT < 1 AND IS_LAST != 2\r\n" +
					"        );", String.class, indentDtl);
			if(dtlId.equalsIgnoreCase("0")) {
				dtlId = this.jdbcTemplate.queryForObject("SELECT \r\n" + 
						"    COUNT(*) as COUNT\r\n" + 
						"FROM\r\n" + 
						"    pra_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl pdtl ON hdr.PO_ID = pdtl.PO_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl idtl ON pdtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_payment_term pterm ON pterm.PO_ID = pdtl.PO_ID AND pterm.IS_LAST != 2\r\n" + 
						"WHERE\r\n" + 
						"    pdtl.INDENT_DTL_ID = ? and \r\n" + 
						"    (hdr.IS_COMPLETED = 0 or hdr.IS_COMPLETED is null)\r\n" + 
						"    and  hdr.TENANT_ID = ?;", String.class, indentDtl, tenantId);
			}
		}catch (Exception e) {
			logger.error("getDtlId exception "+e);		
		}
		return dtlId;
	}

	@Override
	public String getVerCheckByPraId(String praId, String tenantId) {
	    String verCheck = "";
	    try {
	        @SuppressWarnings("deprecation")
			List<String> results = this.jdbcTemplate.query(
	            "SELECT CASE WHEN curr.SEQUENCE_NO < prev.SEQUENCE_NO THEN '1' ELSE '0' END AS IS_VALID " +
	            "FROM " +
	            "  (SELECT SEQUENCE_NO  " +
	            "   FROM pra_status " +
	            "   WHERE PRA_ID = ? AND TENANT_ID = ? " +
	            "   ORDER BY PRA_S_ID DESC " +
	            "   LIMIT 1) curr, " +
	            "  (SELECT SEQUENCE_NO \r\n" + 
	            "	    FROM pra_status \r\n" + 
	            "	   WHERE PRA_ID = ? AND TENANT_ID = ? \r\n" + 
	            "  ORDER BY PRA_S_ID DESC \r\n" + 
	            "  LIMIT 1 OFFSET 1) prev",
	            new Object[]{praId, tenantId, praId, tenantId},
	            (rs, rowNum) -> rs.getString("IS_VALID")
	        );

	        if (!results.isEmpty()) {
	            verCheck = results.get(0);
	        } else {
	            verCheck = "0"; 
	        }
	    } catch (Exception e) {
	        logger.error("getVerCheckByPraId exception " + e);
	    }
	    return verCheck;
	}

}
