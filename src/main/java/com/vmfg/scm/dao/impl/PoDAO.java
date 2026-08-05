package com.vmfg.scm.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.inventory.rowmapper.ProductDropDownRowMapper;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.master.rowmapper.VendorDtlsRowMapper;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.quality.RowMapper.RetrieveQualitInspectionRowMapper;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.entity.AddressDtlByDcTypeEntity;
import com.vmfg.scm.entity.BillingDetailEntity;
import com.vmfg.scm.entity.DcDtlEntity;
import com.vmfg.scm.entity.DcHdrEntity;
import com.vmfg.scm.entity.DebitNoteEntity;
import com.vmfg.scm.entity.GetDCTypeDtlEntity;
import com.vmfg.scm.entity.GetPoDtlsByDate;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.entity.GetProductUnitCostEntity;
import com.vmfg.scm.entity.IndentGrpHdrIdEntity;
import com.vmfg.scm.entity.LocationMstEntity;
import com.vmfg.scm.entity.PoDescMstEntity;
import com.vmfg.scm.entity.PoDispatchDocEntity;
import com.vmfg.scm.entity.PoDtlEntity;
import com.vmfg.scm.entity.PoHsnEntity;
import com.vmfg.scm.entity.PoInstoreDtlEntity;
import com.vmfg.scm.entity.PoPaymentTermEntity;
import com.vmfg.scm.entity.PoStatusEntity;
import com.vmfg.scm.request.GetDcDtlByDcIdRequest;
import com.vmfg.scm.request.PoTypeUpdateReq;
import com.vmfg.scm.rowmapper.AddressDtlByDcTypeRowMapper;
import com.vmfg.scm.rowmapper.BillingDetailRowMapper;
import com.vmfg.scm.rowmapper.DcDtlRowMapper;
import com.vmfg.scm.rowmapper.DcHdrRowMapper;
import com.vmfg.scm.rowmapper.DebitNoteReasonRowMapper;
import com.vmfg.scm.rowmapper.GetDCTypeDtlRowmapper;
import com.vmfg.scm.rowmapper.GetPoDtlsByDateRowMapper;
import com.vmfg.scm.rowmapper.GetProductUnitCostRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpHdrIdRowMapper;
import com.vmfg.scm.rowmapper.LocationMstRowMapper;
import com.vmfg.scm.rowmapper.PoDescMstRowMapper;
import com.vmfg.scm.rowmapper.PoDispatchDocRowMapper;
import com.vmfg.scm.rowmapper.PoDtlRowMapper;
import com.vmfg.scm.rowmapper.PoHdrRowMapper;
import com.vmfg.scm.rowmapper.PoHsnRowMapper;
import com.vmfg.scm.rowmapper.PoInstoreDtlRowMapper;
import com.vmfg.scm.rowmapper.PoPaymentTermRowMapper;
import com.vmfg.scm.rowmapper.PoStatusRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class PoDAO implements IPoDAO {
	private static final Logger logger = LoggerFactory.getLogger(PoDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IIndentUploadDAO iIndentUploadDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Override
	public List<IndentGrpHdrIdEntity> getIndentGrpHdrIdList(String hdrId, String tenantId) {
		List<IndentGrpHdrIdEntity> returnList = new ArrayList<IndentGrpHdrIdEntity>();
		try {
			String retQry = "SELECT \r\n" + "    igd.IG_HDR_ID\r\n" + "FROM\r\n" + "    indent_dtl id\r\n"
					+ "        INNER JOIN\r\n" + "    indent_grp_dtl igd ON id.INDENT_DTL_ID = igd.INDENT_DTL_ID\r\n"
					+ "WHERE\r\n" + "    INDENT_ID = '" + hdrId + "' and igd.TENANT_ID='" + tenantId + "'\r\n"
					+ "GROUP BY igd.IG_HDR_ID";

			returnList = this.jdbcTemplate.query(retQry, new IndentGrpHdrIdRowMapper());

		} catch (Exception ex) {
			logger.error("getIndentGrpHdrIdList error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<IndentGrpHdrIdEntity> getAllPoHdrByIndentId(String projectId, String tenantId) {
		List<IndentGrpHdrIdEntity> returnList = new ArrayList<IndentGrpHdrIdEntity>();
		try {
			String retQry = "SELECT \r\n" + "    igd.IG_HDR_ID\r\n" + "FROM\r\n" + "    indent_grp_dtl igd\r\n"
					+ "        INNER JOIN\r\n" + "    indent_dtl id ON igd.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n"
					+ "        INNER JOIN\r\n" + "    indent_hdr ih ON id.INDENT_ID = ih.INDENT_ID\r\n" + "WHERE\r\n"
					+ "    ih.PROJECT_ID = '" + projectId + "'\r\n" + "        AND igd.TENANT_ID = '" + tenantId
					+ "'\r\n" + "GROUP BY igd.IG_HDR_ID";

			returnList = this.jdbcTemplate.query(retQry, new IndentGrpHdrIdRowMapper());

		} catch (Exception ex) {
			logger.error("getAllPoHdrByIndentId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<GetPoDtlsEntity> getPoHdrListByIgHdrId(String igHdrId, String tenantId) {
		List<GetPoDtlsEntity> returnList = new ArrayList<GetPoDtlsEntity>();
		try {
//			String qry = "select po.PO_TYPE from po_hdr po \n" +
//					"    INNER JOIN indent_grp_scs igs ON po.IG_SCS_ID = igs.IG_SCS_ID\n" +
//					"    WHERE igs.IG_HDR_ID = ? AND po.TENANT_ID = ?";
//			int po_type = this.jdbcTemplate.queryForObject(qry, new Object[]{igHdrId,tenantId}, Integer.class);
//			if(po_type==2){ //If PO is import type then retrieve the exchange rate final total
//				String qryForImport = "SELECT \n" +
//						"    grpven.L1_FINAL_SUB_TOTAL_FX as TOTAL_VALUE,\n" +
//						"    po.*,\n" +
//						"    ih.INDENT_CODE\n" +
//						"FROM \n" +
//						"    po_hdr po\n" +
//						"    INNER JOIN indent_grp_scs igs ON po.IG_SCS_ID = igs.IG_SCS_ID\n" +
//						"    INNER JOIN indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = po.IG_SCS_ID\n" +
//						"    INNER JOIN document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = po.SEQUENCE_STATUS\n" +
//						"    INNER JOIN indent_hdr ih ON po.INDENT_ID = ih.INDENT_ID\n" +
//						"WHERE \n" +
//						"    igs.IG_HDR_ID = ? \n" +
//						"    AND po.TENANT_ID = ?";
//				returnList = this.jdbcTemplate.query(qryForImport, new PoHdrRowMapper(), igHdrId, tenantId);
//			}
//
//			else {
			String retQry = "SELECT \r\n" + "    *,ih.INDENT_CODE, igs.IG_HDR_ID AS IG_HDR_ID\r\n" + "FROM\r\n" + "    po_hdr po\r\n"
					+ "        INNER JOIN\r\n" + "    indent_grp_scs igs ON po.IG_SCS_ID = igs.IG_SCS_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code AS dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = po.SEQUENCE_STATUS\r\n"
					+ "        INNER JOIN\r\n" + "    indent_hdr ih ON po.INDENT_ID = ih.INDENT_ID\r\n" + "WHERE\r\n"
					+ "    igs.IG_HDR_ID = ? AND po.TENANT_ID = ?";

			returnList = this.jdbcTemplate.query(retQry, new PoHdrRowMapper(), igHdrId, tenantId);
//			}

		} catch (Exception ex) {
			logger.error("getPoHdrListByIgHdrId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<DebitNoteEntity> getDebitNoteReason(String isActive, String tenantId){
		List<DebitNoteEntity> debitNoteEntityList = new ArrayList<>();
		try{
			String qry = "Select * from debit_note_reason where TENANT_ID=? AND IS_ACTIVE = ?";
			debitNoteEntityList = this.jdbcTemplate.query(qry, new DebitNoteReasonRowMapper(), tenantId, isActive);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return debitNoteEntityList;
	}

	@Override
	public List<GetPoDtlsEntity> getPoDtlsByPoId(String hdrId, String tenantId) {
		List<GetPoDtlsEntity> returnList = new ArrayList<GetPoDtlsEntity>();
		try {
			String retQry = "select * from po_hdr where PO_ID=? and TENANT_ID=?";

			returnList = this.jdbcTemplate.query(retQry, new PoHdrRowMapper(), hdrId, tenantId);

		} catch (Exception ex) {
			logger.error("getPoDtlsByPoId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<GetPoDtlsEntity> getPoDtlsByPoDtlId(String poDtlId) {
		List<GetPoDtlsEntity> returnList = new ArrayList<GetPoDtlsEntity>();
		try {
			String retQry = "select * from po_hdr hdr inner join po_dtl dtl on hdr.PO_ID=dtl.PO_ID where dtl.PO_DTL_ID=?";

			returnList = this.jdbcTemplate.query(retQry, new PoHdrRowMapper(), poDtlId);

		} catch (Exception ex) {
			logger.error("getPoDtlsByPoDtlId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDtlEntity> getPoDtlList(String poId) {
		List<PoDtlEntity> returnList = new ArrayList<PoDtlEntity>();
		try {
			String retQry = "SELECT * from po_dtl where PO_ID=?";

			returnList = this.jdbcTemplate.query(retQry, new PoDtlRowMapper(), poId);

		} catch (Exception ex) {
			logger.error("getPoDtlList error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDtlEntity> getPoDtlListByPoDtlId(String poDtlId) {
		List<PoDtlEntity> returnList = new ArrayList<PoDtlEntity>();
		try {
			String retQry = "SELECT * from po_dtl where PO_DTL_ID=?";

			returnList = this.jdbcTemplate.query(retQry, new PoDtlRowMapper(), poDtlId);

		} catch (Exception ex) {
			logger.error("getPoDtlsByPoDtlId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoPaymentTermEntity> getPoPaymentTermList(String poId) {
		List<PoPaymentTermEntity> returnList = new ArrayList<PoPaymentTermEntity>();
		try {
			String retQry = "SELECT * from po_payment_term where PO_ID=? AND IS_LAST != 2;";

			returnList = this.jdbcTemplate.query(retQry, new PoPaymentTermRowMapper(), poId);

		} catch (Exception ex) {
			logger.error("getPoPaymentTermList error---> " + ex);
		}
		return returnList;
	}

	@Override
	public String updatePoPaymentTerm(String potId, String paidAmount, String tenantId){
		int updPoPmntStatus = 0;
		try{
			double number = Double.parseDouble(paidAmount);
			String amount = String.format("%.2f", number);
			String qry = "UPDATE po_payment_term SET PENDING_AMOUNT = ? where POT_ID=?";
			updPoPmntStatus = jdbcTemplate.update(qry, amount, potId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(updPoPmntStatus==1){
			return "success";
		}
		return "update Failed";
	}


	public List<PoPaymentTermEntity> getPoPaymentTermListByPotId(String potId) {
		List<PoPaymentTermEntity> returnList = new ArrayList<PoPaymentTermEntity>();
		try {
			String retQry = "SELECT * from po_payment_term where POT_ID=?;";

			returnList = this.jdbcTemplate.query(retQry, new PoPaymentTermRowMapper(), potId);

		} catch (Exception ex) {
			logger.error("getPoPaymentTermListByPotId error---> " + ex);
		}
		return returnList;
	}
	
	@Override
	public String getMiDtlList(String poDtlId, String tenantId) {
		String  inwardQty="";
		try {
			String getCount = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN RECEIVED_QTY > 0  THEN CASE WHEN RECEIVED_QTY != INSPECTED_QTY THEN SUM(RECEIVED_QTY-INSPECTED_QTY)\r\n" + 
					"        ELSE RECEIVED_QTY END ELSE 0\r\n" + 
					"    END AS INSPECTED_QTY\r\n" + 
					"FROM\r\n" + 
					"    material_inward_dtl\r\n" + 
					"WHERE\r\n" + 
					"    PO_DTL_ID = ? AND TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,poDtlId,tenantId);
			inwardQty = resultMap.get("INSPECTED_QTY").toString();

		} catch (Exception ex) {
			logger.error("getMiDtlList Method Exception --->" + ex);
		}
		return inwardQty;
	}
	
	@Override
	public List<PoDispatchDocEntity> getPoDispatchDocList(String poId) {
		List<PoDispatchDocEntity> returnList = new ArrayList<PoDispatchDocEntity>();
		try {
			String retQry = "SELECT * from po_despatch_doc where PO_ID=?;";

			returnList = this.jdbcTemplate.query(retQry, new PoDispatchDocRowMapper(), poId);

		} catch (Exception ex) {
			logger.error("getPoDispatchDocList error---> " + ex);
		}
		return returnList;
	}
	public String getPoRevisionCodeByIgScsId(String igScsId) {
		String  poCode="";
		try {
			String getCount = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN PO_CODE \r\n" + 
					"        ELSE 0\r\n" + 
					"    END as COUNT\r\n" + 
					"FROM\r\n" + 
					"    po_hdr\r\n" + 
					"WHERE\r\n" + 
					"    IG_SCS_ID = ? AND SEQUENCE_NO = 3\r\n" + 
					"ORDER BY REVISION DESC limit 1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igScsId);
			poCode = resultMap.get("COUNT").toString();

		} catch (Exception ex) {
			logger.error("getPoRevisionCodeByIgScsId Method Exception --->" + ex);

		}
		return poCode;
	}
	
	public String getPoOldDateByIgScsId(String igScsId) {
		String  poDate="";
		try {
			String getCount = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN DATE  \r\n" + 
					"        ELSE 0 \r\n" + 
					"    END AS PO_DATE \r\n" + 
					"FROM\r\n" + 
					"    po_hdr\r\n" + 
					"WHERE\r\n" + 
					"    IG_SCS_ID = ? AND SEQUENCE_NO = 3\r\n" + 
					"ORDER BY REVISION DESC limit 1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igScsId);
			poDate = resultMap.get("PO_DATE").toString();

		} catch (Exception ex) {
			logger.error("getPoOldDateByIgScsId Method Exception --->" + ex);

		}
		return poDate;
	}
	@Override
	public int insertPoHdrDtl(GetPoDtlsEntity insertPoDtlsEntity) {
		logger.debug("insertPoHdrDtl   method Start");
		int insertRes = 0;
		try {

			insertPoDtlsEntity.setIsApproved("0");
			String projectCode = projectDAO.getProjCodeByProjId(insertPoDtlsEntity.getProjectId(),
					insertPoDtlsEntity.getTenantId());
			// comman method
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(),
					insertPoDtlsEntity.getTenantId(), "po_hdr", "5", jdbcTemplate, 1, 1, projectCode,0);


String poCode =  "";
String poDate= CommonMethod.getCurrentDate();
			if(insertPoDtlsEntity.getRevision().equalsIgnoreCase("0")) {
			 poCode = gen.getEnquiryCode();
			}else {
				poCode = getPoRevisionCodeByIgScsId(insertPoDtlsEntity.getIgScpId());
				
				   if(poCode.equalsIgnoreCase("0")) {
					   poCode = gen.getEnquiryCode();
				   }
				 poDate =getPoOldDateByIgScsId(insertPoDtlsEntity.getIgScpId());
				 	if(poDate.equalsIgnoreCase("0")) {
				 		poDate = CommonMethod.getCurrentDate();
				 	}
			}
			final String poCodeFinal  =poCode;
			final String poDateFinal  =poDate;
			insertPoDtlsEntity.setTransactionNo(String.valueOf(gen.getSeq()));
			insertPoDtlsEntity.setFinancialYearMstId(gen.getFinainceId());

				String vendorTypeStr ="select case when GST_TYPE  is null then '3' else GST_TYPE end AS GST_TYPE from vendor_mst where VENDOR_CODE = ? ";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(vendorTypeStr,insertPoDtlsEntity.getVendorCode());
				String vendorType = resultMap.get("GST_TYPE").toString();
//			String divisioncheck ="Select count(*) from po_division  where PD_DESC ='"+insertPoDtlsEntity.getDivision()+"' and IS_ACTIVE =1";
//			String dispatchModecheck ="select count(*) from po_mode_of_dispatch where MOD_DESC ='"+insertPoDtlsEntity.getDispatchMode()+"' and IS_ACTIVE =1";
//			String transitInsuranceCheck = "select count(*) from po_transit_insurance where PTI_DESC = '"+insertPoDtlsEntity.getTransitInsurance()+"' and IS_ACTIVE =1";
//			String inspectScopeCheck="select count(*) from po_inspect_scope where IS_DESC ='"+insertPoDtlsEntity.getInspectionScope()+"' and IS_ACTIVE =1";
//		
//			int division = this.jdbcTemplate.queryForObject(divisioncheck, int.class);
//			int dispatchMode = this.jdbcTemplate.queryForObject(dispatchModecheck, int.class);
//			int transitInsurance = this.jdbcTemplate.queryForObject(transitInsuranceCheck, int.class);
//			int inspectScope = this.jdbcTemplate.queryForObject(inspectScopeCheck, int.class);
//			
//	if(division == 0) {
//				String insertqty = "INSERT INTO `po_division` (`PD_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
//		this.jdbcTemplate.update(insertqty,insertPoDtlsEntity.getDivision(),insertPoDtlsEntity.getTenantId());
//		}
//	if(dispatchMode == 0) {
//		String insertqty = "INSERT INTO `po_mode_of_dispatch` (`MOD_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
//		this.jdbcTemplate.update(insertqty,insertPoDtlsEntity.getDispatchMode(),insertPoDtlsEntity.getTenantId());
//		}
//	if(transitInsurance == 0) {
//		String insertqty = "INSERT INTO `po_transit_insurance` (`PTI_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
//		this.jdbcTemplate.update(insertqty,insertPoDtlsEntity.getTransitInsurance(),insertPoDtlsEntity.getTenantId());
//		}
//	if(inspectScope == 0) {
//		String insertqty = "INSERT INTO `po_inspect_scope` (`IS_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
//		this.jdbcTemplate.update(insertqty,insertPoDtlsEntity.getInspectionScope(),insertPoDtlsEntity.getTenantId());
//		
//		}
//	String divisionId = this.jdbcTemplate.queryForObject("Select PD_ID from po_division  where PD_DESC ='"+insertPoDtlsEntity.getDivision()+"' and IS_ACTIVE =1", String.class);
//	String dispatchModeId = this.jdbcTemplate.queryForObject("select MOD_ID from po_mode_of_dispatch where MOD_DESC ='"+insertPoDtlsEntity.getDispatchMode()+"' and IS_ACTIVE =1", String.class);
//	String transitInsuranceId = this.jdbcTemplate.queryForObject("select PTI_ID from po_transit_insurance where PTI_DESC = '"+insertPoDtlsEntity.getTransitInsurance()+"' and IS_ACTIVE =1", String.class);
//	String inspectScopeId = this.jdbcTemplate.queryForObject("select IS_ID from po_inspect_scope where IS_DESC ='"+insertPoDtlsEntity.getInspectionScope()+"' and IS_ACTIVE =1", String.class);
//	
			// insert poHtr
			String insertQ = "INSERT INTO po_hdr (IG_SCS_ID, TRANSACTION_NO, FINANCIAL_YEAR_MST_ID, PO_TYPE, PO_CODE, "
					+ "BILLING_NAME, BILLING_ADDRESS_LINE, BILLING_CITY, BILLING_PINCODE, BILLING_STATE, "
					+ "BILLING_COUNT, BILLING_GST, VENDOR_NAME, VENDOR_ADDRESS_LINE, VENDOR_CITY, VENDOR_PINCODE, "
					+ "VENDOR_STATE, VENDOR_COUNT, VENDOR_GST, DELIVERY_NAME, DELIVERY_ADDRESS_LINE, DELIVERY_CITY, "
					+ "DELIVERY_PINCODE, DELIVERY_STATE, DELIVERY_COUNT, DELIVERY_GST, DELIVERY_CONTACT, DIVISION, "
					+ "ORDER_NO, DATE, REVISION, REVISION_DATE, REF_DATE, DELIVERY_TERMS, LIQ_DAMAGES, GUARANTEE, "
					+ "WARRENTY, DISPATCH_MODE, TRANSIT_INSURANCE, INSPECTION_SCOPE, MISC, PORT_OF_DEST, REMARKS, "
					+ "DISCOUNT, P_F, FRIEGHT, OTHERS, BASIC_TOTAL, GST, CESS, TOTAL_VALUE, FRIEGHT_REMARKS, "
					+ "P_F_REMARKS, PO_T_C, DWGS, QAP, GTC, DOC_CHARGES, INSPECTION_CHARGES, INSURANCE_VALUE, "
					+ "TESTING_CHARGES, CTC, TDC, TDS, TENANT_ID, SEQUENCE_NO, SEQUENCE_STATUS, VENDOR_CODE,LAST_UPDATED_DATETIME,"
					+ "LAST_UPDATED_BY,IS_APPROVED,INDENT_ID,DELIVERY_DATE,BILLING_CONTACT,VENDOR_CONTACT,REF_NO,TRANSPORT_CHARGES,"
					+ "UNIT_INI_BASIC_TOTAL,EXTN_INI_BASIC_TOTAL,IGST,VEN_CODE,BASIC_TOTAL_FX,GST_FX,TOTAL_VALUE_FX, TRANSPORT_CHARGES_FX, P_F_FX) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, insertPoDtlsEntity.getIgScpId());
					ps.setString(2, insertPoDtlsEntity.getTransactionNo());
					ps.setString(3, insertPoDtlsEntity.getFinancialYearMstId());
					ps.setString(4, insertPoDtlsEntity.getPoType());
					ps.setString(5, poCodeFinal);
					ps.setString(6, insertPoDtlsEntity.getBillingName());
					ps.setString(7, insertPoDtlsEntity.getBillingAddressLine());
					ps.setString(8, insertPoDtlsEntity.getBillingCity());
					ps.setString(9, insertPoDtlsEntity.getBillingPincode());
					ps.setString(10, insertPoDtlsEntity.getBillingState());
					ps.setString(11, insertPoDtlsEntity.getBillingCount());
					ps.setString(12, insertPoDtlsEntity.getBillingGst());
					ps.setString(13, insertPoDtlsEntity.getVendorName());
					ps.setString(14, insertPoDtlsEntity.getVendorAddressLine());
					ps.setString(15, insertPoDtlsEntity.getVendorCity());
					ps.setString(16, insertPoDtlsEntity.getVendorPincode());
					ps.setString(17, insertPoDtlsEntity.getVendorState());
					ps.setString(18, insertPoDtlsEntity.getVendorCount());
					ps.setString(19, insertPoDtlsEntity.getVendorGst());
					ps.setString(20, insertPoDtlsEntity.getDeliveryName());
					ps.setString(21, insertPoDtlsEntity.getDeliveryAddressLine());
					ps.setString(22, insertPoDtlsEntity.getDeliveryCity());
					ps.setString(23, insertPoDtlsEntity.getDeliveryPincode());
					ps.setString(24, insertPoDtlsEntity.getDeliveryState());
					ps.setString(25, insertPoDtlsEntity.getDeliveryCount());
					ps.setString(26, insertPoDtlsEntity.getDeliveryGst());
					ps.setString(27, insertPoDtlsEntity.getDeliveryContact());
					ps.setString(28, insertPoDtlsEntity.getDivision());
					ps.setString(29, poCodeFinal);
					ps.setString(30, poDateFinal); // date
					ps.setString(31, insertPoDtlsEntity.getRevision()); // revNo
					ps.setString(32, CommonMethod.getCurrentDate()); // revDate
					ps.setString(33, insertPoDtlsEntity.getRefDate()); // refDate
					ps.setString(34, insertPoDtlsEntity.getDeliveryTerms());
					ps.setString(35, insertPoDtlsEntity.getLiqDamages());
					ps.setString(36, insertPoDtlsEntity.getGuarantee());
					ps.setString(37, insertPoDtlsEntity.getWarrenty());
					ps.setString(38, insertPoDtlsEntity.getDispatchMode());
					ps.setString(39, insertPoDtlsEntity.getTransitInsurance());
					ps.setString(40, insertPoDtlsEntity.getInspectionScope());
					ps.setString(41, insertPoDtlsEntity.getMisc());
					ps.setString(42, insertPoDtlsEntity.getPortOfDest());
					ps.setString(43, insertPoDtlsEntity.getRemarks());
					ps.setString(44, insertPoDtlsEntity.getDiscount() != null ? insertPoDtlsEntity.getDiscount() : "0");
					ps.setString(45, insertPoDtlsEntity.getPF() != null ? insertPoDtlsEntity.getPF() : "0");
					ps.setString(46, insertPoDtlsEntity.getFrieght() != null ? insertPoDtlsEntity.getFrieght() : "0");
					ps.setString(47, insertPoDtlsEntity.getOthers() != null ? insertPoDtlsEntity.getOthers() : "0");
					ps.setString(48,
							insertPoDtlsEntity.getBasicTotal() != null ? insertPoDtlsEntity.getBasicTotal() : "0");
					if(!vendorType.equalsIgnoreCase("2")) {
						ps.setString(49, insertPoDtlsEntity.getGst() != null ? insertPoDtlsEntity.getGst() : "0");
					}else {
						ps.setString(49,  "0");
					}
					ps.setString(50, insertPoDtlsEntity.getCess() != null ? insertPoDtlsEntity.getCess() : "0");

					ps.setString(51,
							insertPoDtlsEntity.getTotalValue() != null ? insertPoDtlsEntity.getTotalValue() : "0");
					ps.setString(52, insertPoDtlsEntity.getFrieghtRemarks());
					ps.setString(53, insertPoDtlsEntity.getPFRemarks());
					ps.setString(54, insertPoDtlsEntity.getPoTC() != "" ? insertPoDtlsEntity.getPoTC() : null);
					ps.setString(55, insertPoDtlsEntity.getDwgs() != "" ? insertPoDtlsEntity.getDwgs() : null);
					ps.setString(56, insertPoDtlsEntity.getQap() != null ? insertPoDtlsEntity.getQap() : "0");
					ps.setString(57, insertPoDtlsEntity.getGtc() != null ? insertPoDtlsEntity.getGtc() : "0");
					ps.setString(58,
							insertPoDtlsEntity.getDocCharges() != null ? insertPoDtlsEntity.getDocCharges() : "0");
					ps.setString(59,
							insertPoDtlsEntity.getInspectionCharges() != null
									? insertPoDtlsEntity.getInspectionCharges()
									: "0");
					ps.setString(60,
							insertPoDtlsEntity.getInsuranceValue() != null ? insertPoDtlsEntity.getInsuranceValue()
									: "0");
					ps.setString(61,
							insertPoDtlsEntity.getTestingCharges() != null ? insertPoDtlsEntity.getTestingCharges()
									: "0");
					ps.setString(62, insertPoDtlsEntity.getCtc() != null ? insertPoDtlsEntity.getCtc() : "0");
					ps.setString(63, insertPoDtlsEntity.getTdc() != null ? insertPoDtlsEntity.getTdc() : "0");
					ps.setString(64, insertPoDtlsEntity.getTds() != null ? insertPoDtlsEntity.getTds() : "0");
					ps.setString(65, insertPoDtlsEntity.getTenantId());
					ps.setString(66, "1");
					ps.setString(67, insertPoDtlsEntity.getSequenceStatus());
					ps.setString(68, insertPoDtlsEntity.getVendorCode());
					ps.setString(69, insertPoDtlsEntity.getEmpId());
					ps.setString(70, insertPoDtlsEntity.getIsApproved());
					ps.setString(71, insertPoDtlsEntity.getIndentID());
					ps.setString(72, insertPoDtlsEntity.getDeliveryDate());
					ps.setString(73, insertPoDtlsEntity.getBillingContactNo());
					ps.setString(74, insertPoDtlsEntity.getVendorContactNo());
					ps.setString(75, insertPoDtlsEntity.getRefNo());
					ps.setString(76, insertPoDtlsEntity.getTransportCharges());
					ps.setString(77, insertPoDtlsEntity.getIntialunitPrice());
					ps.setString(78, insertPoDtlsEntity.getIntialExtendedPrice());
					if(vendorType.equalsIgnoreCase("2")) {
						ps.setString(79, insertPoDtlsEntity.getGst() != null ? insertPoDtlsEntity.getGst() : "0");
					}else {
						ps.setString(79,  "0");
					}
					ps.setString(80, insertPoDtlsEntity.getVenCode());
					ps.setString(81,insertPoDtlsEntity.getBasicTotalFx());
					ps.setString(82,insertPoDtlsEntity.getGstFx());
					ps.setString(83,insertPoDtlsEntity.getTotalValueFx());
					ps.setString(84, insertPoDtlsEntity.getTransportChargesFx());
					ps.setString(85, insertPoDtlsEntity.getPFFX());
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertPoHdrDtl  method  exception" + ex);
		}
		logger.debug("insertPoHdrDtl   method end");
		return insertRes;
	}

	@Override
	public int insertPoStatusDtl(String poId, String seqNo, String seqStatusCode, String tenantId, String remarks,
			String empId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO po_status ( PO_ID, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
			insertStatus = this.jdbcTemplate.update(qry, poId, seqNo, seqStatusCode, remarks, empId, tenantId);

		} catch (Exception ex) {
			logger.error("insertInScpStatus method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int updatePoHdrDtl(GetPoDtlsEntity updatePoDtls) {
		int updateStatus = 0;
		try {

			String divisioncheck = "Select count(*) as COUNT from po_division  where PD_DESC =? and IS_ACTIVE =1 AND TENANT_ID = ? ";
			String dispatchModecheck = "select count(*) as COUNT from po_mode_of_dispatch where MOD_DESC = ? and IS_ACTIVE =1 AND TENANT_ID = ? ";
			String transitInsuranceCheck = "select count(*) as COUNT from po_transit_insurance where PTI_DESC = ? and IS_ACTIVE =1 AND TENANT_ID = ? ";
			String inspectScopeCheck = "select count(*) as COUNT from po_inspect_scope where IS_DESC =? and IS_ACTIVE =1 AND TENANT_ID = ? ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(divisioncheck,updatePoDtls.getDivision(),updatePoDtls.getTenantId());
			int division = Integer.parseInt(resultMap.get("COUNT").toString());
			
			Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(dispatchModecheck,updatePoDtls.getDispatchMode(),updatePoDtls.getTenantId());
			int dispatchMode = Integer.parseInt(resultMap1.get("COUNT").toString());
			
			Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(transitInsuranceCheck,updatePoDtls.getTransitInsurance(),updatePoDtls.getTenantId());
			int transitInsurance = Integer.parseInt(resultMap2.get("COUNT").toString());
			
			Map<String, Object> resultMap3 = jdbcTemplate.queryForMap(inspectScopeCheck,updatePoDtls.getInspectionScope(),updatePoDtls.getTenantId());
			int inspectScope = Integer.parseInt(resultMap3.get("COUNT").toString());

			if (division == 0) {
				String insertqty = "INSERT INTO `po_division` (`PD_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
				this.jdbcTemplate.update(insertqty, updatePoDtls.getDivision(), updatePoDtls.getTenantId());
			}
			if (dispatchMode == 0) {
				String insertqty = "INSERT INTO `po_mode_of_dispatch` (`MOD_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
				this.jdbcTemplate.update(insertqty, updatePoDtls.getDispatchMode(), updatePoDtls.getTenantId());
			}
			if (transitInsurance == 0 &&  updatePoDtls.getTransitInsurance()!=(null)) {
				String insertqty = "INSERT INTO `po_transit_insurance` (`PTI_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
				this.jdbcTemplate.update(insertqty, updatePoDtls.getTransitInsurance(), updatePoDtls.getTenantId());
			}
			if (inspectScope == 0) {
				String insertqty = "INSERT INTO `po_inspect_scope` (`IS_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,'1',?) ";
				this.jdbcTemplate.update(insertqty, updatePoDtls.getInspectionScope(), updatePoDtls.getTenantId());

			}
			Map<String,Object> map  = this.jdbcTemplate.queryForMap("Select PD_ID from po_division  where PD_DESC =? and IS_ACTIVE =1 and TENANT_ID = ?",updatePoDtls.getDivision(),updatePoDtls.getTenantId());
			String divisionId = map.get("PD_ID").toString();
			
			Map<String,Object> map1 = this.jdbcTemplate.queryForMap("select MOD_ID from po_mode_of_dispatch where MOD_DESC =? and IS_ACTIVE =1 and TENANT_ID = ?", updatePoDtls.getDispatchMode(),updatePoDtls.getTenantId());
			String dispatchModeId = map1.get("MOD_ID").toString();
			
			String transitInsuranceId = "";
			if(updatePoDtls.getTransitInsurance()!=null) {
				Map<String,Object> map2  = this.jdbcTemplate.queryForMap("select PTI_ID from po_transit_insurance where PTI_DESC = ? and IS_ACTIVE =1 and TENANT_ID = ?", updatePoDtls.getTransitInsurance(),updatePoDtls.getTenantId());
				 transitInsuranceId = map2.get("PTI_ID").toString();
			}
			
			Map<String,Object> map3 = this.jdbcTemplate.queryForMap("select IS_ID from po_inspect_scope where IS_DESC =? and IS_ACTIVE =1 and TENANT_ID = ?", updatePoDtls.getInspectionScope(),updatePoDtls.getTenantId());
			String inspectScopeId = map3.get("IS_ID").toString();

			String qry = "UPDATE po_hdr SET  PO_CODE = ?, PO_TYPE = ?, BILLING_NAME = ?, BILLING_ADDRESS_LINE = ?, BILLING_CITY = ?,"
					+ " BILLING_PINCODE = ?, BILLING_STATE = ?, BILLING_COUNT = ?, BILLING_GST = ?, VENDOR_NAME = ?, VENDOR_ADDRESS_LINE = ?,"
					+ " VENDOR_CITY = ?, VENDOR_PINCODE = ?, VENDOR_STATE = ?, VENDOR_COUNT = ?, VENDOR_GST = ?, DELIVERY_NAME = ?, DELIVERY_ADDRESS_LINE = ?,"
					+ " DELIVERY_CITY = ?, DELIVERY_PINCODE = ?, DELIVERY_STATE = ?, DELIVERY_COUNT = ?, DELIVERY_GST = ?, DELIVERY_CONTACT = ?, DIVISION = ?,"
					+ "  REVISION = ?, REVISION_DATE = ?, REF_DATE = ?, DELIVERY_TERMS = ?, LIQ_DAMAGES = ?, GUARANTEE = ?, WARRENTY = ?, DISPATCH_MODE = ?,"
					+ " TRANSIT_INSURANCE = ?, INSPECTION_SCOPE = ?, MISC = ?, PORT_OF_DEST = ?, REMARKS = ?, DISCOUNT = ?, P_F = ?, FRIEGHT = ?, OTHERS = ?, BASIC_TOTAL = ?, GST = ?,"
					+ " CESS = ?, TOTAL_VALUE = ?, FRIEGHT_REMARKS = ?, P_F_REMARKS = ?, PO_T_C = ?, DWGS = ?, QAP = ?, GTC = ?, DOC_CHARGES = ?, INSPECTION_CHARGES = ?, INSURANCE_VALUE = ?,"
					+ " TESTING_CHARGES = ?, CTC = ?, TDC = ?, TDS = ?, VENDOR_CODE = ?,DATE=?,BILLING_CONTACT = ?,VENDOR_CONTACT = ?,AMOUNT_IN_WORDS = ?, RAN_DIV_COMMTE=?, PAN=?, PRICE_BASIS=?,IGST=?,TERMINAL_TAX=?,REF_NO = ?,TRANSPORT_CHARGES =?,VEN_CODE = ? WHERE PO_ID = ?";
			updateStatus = this.jdbcTemplate.update(qry, updatePoDtls.getPoCode(), updatePoDtls.getPoType(),
					updatePoDtls.getBillingName(), updatePoDtls.getBillingAddressLine(), updatePoDtls.getBillingCity(),
					updatePoDtls.getBillingPincode(), updatePoDtls.getBillingState(), updatePoDtls.getBillingCount(),
					updatePoDtls.getBillingGst(), updatePoDtls.getVendorName(), updatePoDtls.getVendorAddressLine(),
					updatePoDtls.getVendorCity(), updatePoDtls.getVendorPincode(), updatePoDtls.getVendorState(),
					updatePoDtls.getVendorCount(), updatePoDtls.getVendorGst(), updatePoDtls.getDeliveryName(),
					updatePoDtls.getDeliveryAddressLine(), updatePoDtls.getDeliveryCity(),
					updatePoDtls.getDeliveryPincode(), updatePoDtls.getDeliveryState(), updatePoDtls.getDeliveryCount(),
					updatePoDtls.getDeliveryGst(), updatePoDtls.getDeliveryContact(), divisionId,
					updatePoDtls.getRevision(), updatePoDtls.getRevisionDate(), updatePoDtls.getRefDate(),
					updatePoDtls.getDeliveryTerms(), updatePoDtls.getLiqDamages(), updatePoDtls.getGuarantee(),
					updatePoDtls.getWarrenty(), dispatchModeId, transitInsuranceId, inspectScopeId,
					updatePoDtls.getMisc(), updatePoDtls.getPortOfDest(), updatePoDtls.getRemarks(),
					updatePoDtls.getDiscount(), updatePoDtls.getPF(), updatePoDtls.getFrieght(),
					updatePoDtls.getOthers(), updatePoDtls.getBasicTotal(), updatePoDtls.getGst(),
					updatePoDtls.getCess(), updatePoDtls.getTotalValue(), updatePoDtls.getFrieghtRemarks(),
					updatePoDtls.getPFRemarks(), updatePoDtls.getPoTC(), updatePoDtls.getDwgs(), updatePoDtls.getQap(),
					updatePoDtls.getGtc(), updatePoDtls.getDocCharges(), updatePoDtls.getInspectionCharges(),
					updatePoDtls.getInsuranceValue(), updatePoDtls.getTestingCharges(), updatePoDtls.getCtc(),
					updatePoDtls.getTdc(), updatePoDtls.getTds(), updatePoDtls.getVendorCode(), updatePoDtls.getDate(),
					updatePoDtls.getBillingContactNo(), updatePoDtls.getVendorContactNo(),
					updatePoDtls.getAmountinwords(), updatePoDtls.getRanDivCommte(), updatePoDtls.getPan(),
					updatePoDtls.getPriceBasis(), updatePoDtls.getIgst(), updatePoDtls.getTerminalTax(),updatePoDtls.getRefNo(),updatePoDtls.getTransportCharges(),updatePoDtls.getVenCode(),updatePoDtls.getPoId());
		} catch (Exception ex) {
			logger.error("updatePoHdrDtl method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int insertpoDispatchDocDtl(PoDispatchDocEntity poDispatch, String poHdrId) {
		int insertStatus = 0;
		try {
			if (poDispatch.getPodId() == null || poDispatch.getPodId().isEmpty()) {
				String insertqQry = "INSERT INTO po_despatch_doc ( PO_ID, INVOICE_NO, PKG_LIST, AWB_BL, TEST_REPORTS, CERTIFICATE_OF_ORIGIN, O_M_MANUAL, "
						+ "INSURANCE_WARRENTY_CERT, INSPECTION_REPORT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStatus = this.jdbcTemplate.update(insertqQry, poHdrId, poDispatch.getInvoiceNo(),
						poDispatch.getPkgList(), poDispatch.getAwbBl(), poDispatch.getTestReports(),
						poDispatch.getCertificateOfOrigin(), poDispatch.getoMManual(),
						poDispatch.getInsuranceWarrentyCert(), poDispatch.getInspectionReport());
			} else {

				String updateQry = "UPDATE po_despatch_doc SET \r\n" + "    PO_ID = ?, \r\n"
						+ "    INVOICE_NO = ?, \r\n" + "    PKG_LIST = ?, \r\n" + "    AWB_BL = ?, \r\n"
						+ "    TEST_REPORTS = ?, \r\n" + "    CERTIFICATE_OF_ORIGIN = ?, \r\n"
						+ "    O_M_MANUAL = ?, \r\n" + "    INSURANCE_WARRENTY_CERT = ?, \r\n"
						+ "    INSPECTION_REPORT = ?\r\n" + "WHERE \r\n" + "    POD_ID = ?";
				insertStatus = this.jdbcTemplate.update(updateQry, poDispatch.getPoId(), poDispatch.getInvoiceNo(),
						poDispatch.getPkgList(), poDispatch.getAwbBl(), poDispatch.getTestReports(),
						poDispatch.getCertificateOfOrigin(), poDispatch.getoMManual(),
						poDispatch.getInsuranceWarrentyCert(), poDispatch.getInspectionReport(), poDispatch.getPodId());
			}

		} catch (Exception ex) {
			logger.error("insertpoDispatchDocDtl method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int insertpoPaymentTermDtl(PoPaymentTermEntity poPaymentTerm, String poHdrId,int flag) {
		int insertStatus = 0;
		try {
            String poValue = this.jdbcTemplate.queryForObject(
                    "SELECT COALESCE(BASIC_TOTAL,0) " +
                            "FROM po_hdr WHERE PO_ID = ?",
                    String.class,
                    poHdrId
            );
//			String poValue  = this.jdbcTemplate.queryForObject("Select TOTAL_VALUE from po_hdr where PO_ID = ?", String.class,poHdrId);
            BigDecimal totalAmount = (poValue != null) ? new BigDecimal(poValue) : BigDecimal.ZERO;


            String percentage = poPaymentTerm.getPercentage();
			BigDecimal percent = new BigDecimal(percentage);
			
//			BigDecimal amt = totalAmount.divide(percent).multiply(new BigDecimal(100));
			//BigDecimal pendAmt = totalAmount.subtract(amt);
			BigDecimal amt = totalAmount.multiply(percent).divide(new BigDecimal(100));
			amt = amt.setScale(0, RoundingMode.HALF_UP);

			if(flag == 1) {

				String poValueForLast = this.jdbcTemplate.queryForObject(
						"SELECT COALESCE(P_F,0) + COALESCE(TRANSPORT_CHARGES,0) + COALESCE(INSURANCE_VALUE,0) + COALESCE(OTHERS,0) " +
								"FROM po_hdr WHERE PO_ID = ?",
						String.class,
						poHdrId
				);
//			String poValue  = this.jdbcTemplate.queryForObject("Select TOTAL_VALUE from po_hdr where PO_ID = ?", String.class,poHdrId);
				BigDecimal totalAmountForLast = (poValueForLast != null) ? new BigDecimal(poValueForLast) : BigDecimal.ZERO;

				BigDecimal gstValues  = this.jdbcTemplate.queryForObject("Select CASE WHEN GST+IGST > 0 THEN GST+IGST ELSE 0 END AS GST_VALUES from po_hdr where PO_ID = ?", BigDecimal.class,poHdrId);
				amt = amt.add(totalAmountForLast);
				amt = amt.add(gstValues);
			}
			
			String insertqQry = "INSERT INTO po_payment_term ( PO_ID, TERM, PERCENTAGE,PAYMENT_AMOUNT,PENDING_AMOUNT, REMARKS, IS_LAST) VALUES (?, ?, ?, ?, ?, ?, ?)";
			insertStatus = this.jdbcTemplate.update(insertqQry, poHdrId, poPaymentTerm.getTerm(),
					poPaymentTerm.getPercentage(),amt,amt ,poPaymentTerm.getRemarks(),flag);

		} catch (Exception ex) {
			logger.error("insertpoPaymentTermDtl method Error" + ex);
		}
		return insertStatus;

	}
	@Override
	public int insertpoDtl(PoDtlEntity poDtl, String poHdrId) {
		int insertStatus = 0;

		if (poDtl == null) {
			logger.error("insertpoDtl failed: poDtl is null");
			return insertStatus;
		}

		if (poHdrId == null || poHdrId.trim().isEmpty()) {
			logger.error("insertpoDtl failed: poHdrId is null or empty");
			return insertStatus;
		}

		try {
			if (poDtl.getPoDtlId() == null || poDtl.getPoDtlId().isEmpty()) {
				String insertQry = "INSERT INTO po_dtl (PO_ID, HSN_CODE, PO_GST, QTY, UOM_CODE, DELIVERY_DATE, CURRENCY_TYPE, UNIT_RATE_FX, UNITE_RATE, TOTAL_VALUE_FX, TOTAL_VALUE, INDENT_DTL_ID, MATERIAL_DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				insertStatus = this.jdbcTemplate.update(insertQry,
						poHdrId,
						safeString(poDtl.getHsnCode()),
						safeDouble(poDtl.getPoGst()),
						safeDouble(poDtl.getQty()),
						safeString(poDtl.getUomCode()),
						poDtl.getDeliveryDate(),
						safeString(poDtl.getCurrencyType()),
						safeDouble(poDtl.getUnitRateFx()),
						safeDouble(poDtl.getUnitRate()),
						safeDouble(poDtl.getTotalValueFx()),
						safeDouble(poDtl.getTotalValue()),
						safeString(poDtl.getIndentDtlId()),
						safeString(poDtl.getMaterialDesc()));
			} else {
				String updateQry = "UPDATE po_dtl SET PO_ID=?, HSN_CODE=?, PO_GST=?, QTY=?, UOM_CODE=?, DELIVERY_DATE=?, CURRENCY_TYPE=?, UNIT_RATE_FX=?, UNITE_RATE=?, TOTAL_VALUE_FX=?, TOTAL_VALUE=?, INDENT_DTL_ID=?, SERVICE_NUMBER=?, MATERIAL_DESCRIPTION=? WHERE PO_DTL_ID=?";

				insertStatus = this.jdbcTemplate.update(updateQry,
						safeString(poDtl.getPoId()),
						safeString(poDtl.getHsnCode()),
						safeDouble(poDtl.getPoGst()),
						safeDouble(poDtl.getQty()),
						safeString(poDtl.getUomCode()),
						poDtl.getDeliveryDate(),
						safeString(poDtl.getCurrencyType()),
						safeDouble(poDtl.getUnitRateFx()),
						safeDouble(poDtl.getUnitRate()),
						safeDouble(poDtl.getTotalValueFx()),
						safeDouble(poDtl.getTotalValue()),
						safeString(poDtl.getIndentDtlId()),
						safeString(poDtl.getServiceNo()),
						safeString(poDtl.getMaterialDesc()),
						safeString(poDtl.getPoDtlId()));
			}
		} catch (Exception ex) {
			logger.error("insertpoDtl method error: ", ex);
		}

		return insertStatus;
	}

	// Utility Methods
	private String safeString(String input) {
		return (input != null) ? input : "";
	}

	private Double safeDouble(Object input) {
		try {
			if (input instanceof Number) return ((Number) input).doubleValue();
			if (input instanceof String) {
				String str = ((String) input).trim();
				return !str.isEmpty() ? Double.parseDouble(str) : 0.0;
			}
		} catch (Exception e) {
			logger.warn("Invalid number format: " + input);
		}
		return 0.0;
	}



	@Override
	public int updatePoSeqAndStatus(String poId, String currentseq, String docStatus, String isLatest,
			String isApproved, String empId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE po_hdr SET SEQUENCE_NO=?, SEQUENCE_STATUS=?, IS_LATEST=?,LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=?,IS_APPROVED=? WHERE PO_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, currentseq, docStatus, isLatest,
					CommonMethod.getCurrentDateTime(), empId,isApproved, poId);

		} catch (Exception ex) {
			logger.error("updatePoSeqAndStatus method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public void updatePoApproved(String poId, String isApproved) {

		try {
			String qry = "UPDATE po_hdr SET IS_APPROVED=? WHERE PO_ID=?";
			this.jdbcTemplate.update(qry, isApproved,poId);

		} catch (Exception ex) {
			logger.error("updatePoApproved method Error" + ex);
		}

	}

	@Override
	public String getPoType(String vendorCode) {
		String poType = "";
		try {
			String query = "select PO_TYPE from vendor_mst where VENDOR_CODE=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,vendorCode);
			poType = resultMap.get("PO_TYPE").toString();
		} catch (Exception e) {
			logger.error("getPoType method Error" + e);
		}
		return poType;
	}

	@Override
	public String getIgScsIdByPoId(String poId) {
		String igScsId = "";
		try {
			String query = "select IG_SCS_ID from po_hdr where PO_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,poId);
			igScsId = resultMap.get("IG_SCS_ID").toString();
		} catch (Exception e) {
			logger.error("getIgScsIdByPoId method Error" + e);
		}
		return igScsId;
	}

	@Override
	public String getIndentId(String igScpId) {
		String indentId = "";
		try {
			String query = "select INDENT_ID from indent_grp_scs where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,igScpId);
			indentId = resultMap.get("INDENT_ID").toString();
		} catch (Exception e) {
			logger.error("getIndentId method Error" + e);
		}
		return indentId;
	}
	@Override
	public String getPJSCreatedBY(String igScpId) {
		String indentId = "";
		try {
			String query = "select case when CREATED_BY is null then '' else CREATED_BY end AS CREATED_BY from indent_grp_scs where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,igScpId);
			indentId = resultMap.get("CREATED_BY").toString();
		} catch (Exception e) {
			logger.error("getPJSCreatedBY method Error" + e);
		}
		return indentId;
	}

	@Override
	public String getProdCodeByIndentDtlId(String indentDtlId) {
		String prodCode = "";
		try {
			String query = "select PRODUCT_CODE from indent_dtl where INDENT_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,indentDtlId);
			prodCode = resultMap.get("PRODUCT_CODE").toString();
		} catch (Exception e) {
			logger.error("getProdCodeByIndentDtlId method Error" + e);
		}
		return prodCode;
	}

	public String getProdCodeByprodId(String prodid) {
		String prodCode = "";
		try {
			String query = "select PRODUCT_CODE from product_mst where PRODUCT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,prodid);
			prodCode = resultMap.get("PRODUCT_CODE").toString();
		} catch (Exception e) {
			logger.error("getProdCodeByIndentDtlId method Error" + e);
		}
		return prodCode;
	}

	public String getPmHdrIdByIndentDtlId(String indentDtlId) {
		String prodCode = "";
		try {
			String query = "select PROJECT_ID from indent_dtl dtl inner join indent_hdr hdr on hdr.INDENT_ID = dtl.INDENT_ID where INDENT_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,indentDtlId);
			prodCode = resultMap.get("PROJECT_ID").toString();
		} catch (Exception e) {
			logger.error("getPmHdrIdByIndentDtlId method Error" + e);
		}
		return prodCode;
	}

	@Override
	public List<BillingDetailEntity> getBillingDetails(String tenantId) {
		List<BillingDetailEntity> list = null;
		try {
			String qry = "SELECT \r\n" + "    org.ORG_CODE,\r\n" + "    org.ORG_NAME,\r\n" + "    lm.LOCATION_ID,\r\n"
					+ "    LOCATION_REFERENCENAME,\r\n" + "    LOCATION_ADDRESSLINE,\r\n" + "    LOCATION_CITY,\r\n"
					+ "    LOCATION_STATE,\r\n" + "    LOCATION_COUNTRY_CODE,\r\n"
					+ "    LOCATION_PINCODE,CONTACT_NO,lm.GST_NUMBER AS GST_NUMBER \r\n" + "FROM\r\n" + "    organization_info org\r\n"
					+ "        INNER JOIN\r\n" + "    organization_location_dtl lm ON org.ORG_CODE = lm.ORG_CODE\r\n"
					+ "WHERE\r\n" + "    org.TENANT_ID = '" + tenantId + "';";
			list = this.jdbcTemplate.query(qry, new BillingDetailRowMapper());
		} catch (Exception e) {
			logger.error("getBillingDetails method Error" + e);
		}
		return list;
	}

	@Override
	public List<VendorMstEntity> getVendorMstDtls(String vendorCode, String tenantId) {
		List<VendorMstEntity> list = null;
		try {
			String qry = "select * from vendor_mst where VENDOR_CODE='" + vendorCode + "' and TENANT_ID='" + tenantId
					+ "' ";
			list = this.jdbcTemplate.query(qry, new VendorDtlsRowMapper());
		} catch (Exception e) {
			logger.error("getVendorMstDtls method Error" + e);
		}
		return list;
	}

	@Override
	public List<LocationMstEntity> getVendorLocDtls(String locationId, String tenantId) {
		List<LocationMstEntity> list = null;
		try {
			String qry = "select * from location_mst where LOCATION_ID='" + locationId + "' and TENANT_ID='" + tenantId
					+ "'";
			list = this.jdbcTemplate.query(qry, new LocationMstRowMapper());
		} catch (Exception e) {
			logger.error("getVendorLocDtls method Error" + e);
		}
		return list;
	}

	@Override
	public int getLatestRevByPoId(String poId) {
		int revision = 0;
		try {
			String qry = "select case when REVISION is not null then REVISION else 0 end as REVISION from po_hdr where PO_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poId);
			revision = Integer.parseInt(resultMap.get("REVISION").toString());
		} catch (Exception e) {
			logger.error("getVendorLocDtls method Error" + e);
		}
		return revision;
	}

	@Override
	public List<PoStatusEntity> getPoStatusList(String poId) {
		List<PoStatusEntity> list = null;
		try {
			String qry = "SELECT \r\n" + "    po.*,\r\n" + "    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    emp.EMPLOYEE_FIRSTNAME\r\n" + "FROM\r\n" + "    po_status po\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code doc ON po.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        INNER JOIN\r\n" + "    employee_mst emp ON po.UPDATED_BY = emp.EMPLOYEE_ID\r\n"
					+ "WHERE\r\n" + "    po.PO_ID = ?";
			list = this.jdbcTemplate.query(qry, new PoStatusRowMapper(), poId);
		} catch (Exception e) {
			logger.error("getVendorLocDtls method Error" + e);
		}
		return list;
	}

	@Override
	public List<GetPoDtlsByDate> getPoDtlsByDateAndPoId(String fromDate, String toDate, String projectId,
			String tenantId) {
		List<GetPoDtlsByDate> list = null;
		try {
			String qry = "SELECT \r\n" + "    po.PO_ID, po.PO_CODE, vm.VENDOR_NAME, vm.VENDOR_CODE\r\n" + "FROM\r\n"
					+ "    po_hdr po\r\n" + "        INNER JOIN\r\n"
					+ "    indent_hdr ih ON po.INDENT_ID = ih.INDENT_ID\r\n" + "        INNER JOIN\r\n"
					+ "    vendor_mst AS vm ON po.VENDOR_CODE = vm.VENDOR_CODE\r\n" + "WHERE\r\n"
					+ "    ih.PROJECT_ID = ? AND po.TENANT_ID = ?\r\n" + "        AND po.IS_APPROVED = '1' AND po.IS_LATEST='1'";

			list = this.jdbcTemplate.query(qry, new GetPoDtlsByDateRowMapper(), projectId, tenantId);
		} catch (Exception e) {
			logger.error("getVendorLocDtls method Error" + e);
		}
		return list;
	}
	@Override
	public String getProdDescByIndentDtlId(String indentDtlId) {
		String prodCode = "";
		try {
			String query = "select DESCRIPTION from indent_dtl where INDENT_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,indentDtlId);
			prodCode = resultMap.get("DESCRIPTION").toString();
		} catch (Exception e) {
			logger.error("getProdDescByIndentDtlId method Error" + e);
		}
		return prodCode;
	}

	@Override
	public String getProdDescByQiId(String indentDtlId) {
		String prodCode = "";
		try {
			String query = "select dtl.DESCRIPTION As DESCRIPTION	 from quality_inspection_hdr hdr inner join quality_inspection_request qir on qir.QI_ID = hdr.QI_ID inner join indent_dtl dtl on dtl.INDENT_DTL_ID = qir.INDENT_DTL_ID where qir.QI_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,indentDtlId);
			prodCode = resultMap.get("DESCRIPTION").toString();
		} catch (Exception e) {
			logger.error("getProdDescByQiId method Error" + e);
		}
		return prodCode;
	}

	@Override
	public void removePoHdrIdBased(String poHdrId) {

		try {

			String deleteQuery = "DELETE FROM po_payment_term WHERE PO_ID=?";

			this.jdbcTemplate.update(deleteQuery, poHdrId);

		} catch (Exception e) {
			logger.error("removePoHdrIdBased method Error" + e);
		}

	}

	@Override
	public int getPtDtlCount(String poId) {
		int count = 0;
		try {

			String qry = "select count(*) as COUNT from po_payment_term where PO_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("getPtDtlCount method Error" + e);
		}
		return count;
	}

	@Override
	public int qtyInspectReqCount(String poDtlId) {
		int qtyInspectReqCount = 0;
		try {
			String qtyInspectReqCountStr = "Select Count(*) as COUNT from quality_inspection_request where PO_DTL_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qtyInspectReqCountStr,poDtlId);
			qtyInspectReqCount = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("qtyInspectReqCount method Error" + ex);
		}
		return qtyInspectReqCount;
	}

	@Override
	public int updateIntentHdrSeqAndStatus(String poId, String lastSeq, String docStatus, int indtentId, String isCompleted) {
		int updateStatus = 0;
//		int isCompleted = 1;
		try {

			String qry = "UPDATE indent_hdr SET SEQUENCE_N0=?, SEQUENCE_STATUS=?,IS_COMPLETED=? WHERE INDENT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,lastSeq,docStatus,isCompleted,indtentId);

		} catch (Exception ex) {
			logger.error("updateIntentHdrSeqAndStatus method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int retriveIndentId(String poId) {
		int indtentId = 0;
		try {
			String retQry = "select INDENT_ID from po_hdr where PO_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,poId);
			indtentId = Integer.parseInt(resultMap.get("INDENT_ID").toString());

		} catch (Exception ex) {
			logger.error("retriveIndentId method Error" + ex);
		}
		return indtentId;

	}

	public List<GetDCTypeDtlEntity> getDctypeCode() {
		List<GetDCTypeDtlEntity> list = new ArrayList<GetDCTypeDtlEntity>();
		try {

			String dcTypeList = "select  * from dc_type_mst where IS_ACTIVE =1";

			list = this.jdbcTemplate.query(dcTypeList, new GetDCTypeDtlRowmapper());
		} catch (Exception e) {
			logger.error("getDctypeCode method Error" + e);
		}
		return list;
	}

	public List<ProductMstDropDownEntity> getDCProductDropDown(String name, String pmHdr, String tenantID) {
		List<ProductMstDropDownEntity> list = new ArrayList<ProductMstDropDownEntity>();
		try {
			String invTypeCode = "", invTypeCode2 = "" ;
			String inventoryStr = "";
			if (name != null && !name.equalsIgnoreCase("")) {
				String[] typeList = name.split(",");
				for (int i = 0; i < typeList.length; i++) {
					if (invTypeCode.equalsIgnoreCase("")) {
						invTypeCode = typeList[i];
					} else {
						invTypeCode = invTypeCode + "','" + typeList[i];
					}
				}
				if(invTypeCode.equalsIgnoreCase("ILC0002")) {
					invTypeCode2 =  invTypeCode + "','" + "ILC0001";
				}else {
					invTypeCode2 = invTypeCode + "','" + "ILC0005";
				}
				inventoryStr = "AND inv.INVENTORY_LOCATION_CODE in('" + invTypeCode2 + "') ";
			} else {
//				inventoryStr = "AND (ilm.INVENTORY_LOCATION_CODE = 'ILC0002' or ilm.INVENTORY_LOCATION_PARENT_CODE = 'ILC0002' )";
				inventoryStr = "AND (ilm.INVENTORY_LOCATION_CODE = 'ILC0001' or ilm.INVENTORY_LOCATION_CODE = 'ILC0002' or ilm.INVENTORY_LOCATION_CODE = 'ILC0004' or ilm.INVENTORY_LOCATION_CODE = 'ILC0005' or ilm.INVENTORY_LOCATION_PARENT_CODE = 'ILC0002' )";
			}

			String dcTypeList = "SELECT \r\n"
					+ "    prod.PRODUCT_CODE, prod.PRODUCT_ID, prod.PRODUCT_DESCRIPTION,inv.PRODUCT_QUANTITY_ON_HAND, inv.INVENTORY_LOCATION_CODE \r\n"
					+ "FROM\r\n" + "    product_mst prod\r\n" + "        INNER JOIN\r\n"
					+ "    inventory_product_dtl inv ON prod.PRODUCT_ID = inv.PRODUCT_ID "
					+ " INNER JOIN inventory_location_mst ilm on ilm.INVENTORY_LOCATION_CODE = inv.INVENTORY_LOCATION_CODE \r\n"
					+ "WHERE\r\n" + "    prod.PM_HDR_ID = '" + pmHdr + "'\r\n" + "        AND prod.TENANT_ID = '"
					+ tenantID + "'\r\n" + "        " + inventoryStr + "\r\n"
					+ "        AND inv.PRODUCT_QUANTITY_ON_HAND > 0";

			list = this.jdbcTemplate.query(dcTypeList, new ProductDropDownRowMapper());
		} catch (Exception e) {
			logger.error("getDCProductDropDown method Error" + e);
		}
		return list;
	}

	@Override
	public List<AddressDtlByDcTypeEntity> getVendorCode(String custCode,String tenantId) {
		List<AddressDtlByDcTypeEntity> returnList = new ArrayList<AddressDtlByDcTypeEntity>();
		try {
			String retQry = "SELECT \r\n" + "    VENDOR_NAME AS NAME,\r\n" + "    GST AS GSTNO,\r\n"
					+ "    LOCATION_ADDRESSLINE AS ADDRESS,\r\n" + "    LOCATION_CITY AS CITY,\r\n"
					+ "    LOCATION_STATE AS STATE,\r\n"
					+ "    LOCATION_PINCODE AS PINCODE,VENDOR_CODE AS CODE,CONTACT_NO AS CONTACT_NO \r\n" + "FROM\r\n"
					+ "    vendor_mst mst\r\n" + "        INNER JOIN\r\n"
					+ "    location_mst lmst ON mst.LOCATION_ID = lmst.LOCATION_ID where  mst.TENANT_ID = ? \r\n" + "" + "    ";

			returnList = this.jdbcTemplate.query(retQry, new AddressDtlByDcTypeRowMapper(),tenantId);

		} catch (Exception ex) {
			logger.error("getIndentGrpHdrIdList error---> " + ex);
		}
		return returnList;

	}

	@Override
	public List<AddressDtlByDcTypeEntity> getOrganCode(String custCode,String tenantId) {
		List<AddressDtlByDcTypeEntity> returnList = new ArrayList<AddressDtlByDcTypeEntity>();
		try {
			String retQry = "SELECT \r\n" + "    dtl.LOCATION_REFERENCENAME AS NAME,\r\n"
					+ "    LOCATION_ADDRESSLINE AS ADDRESS,\r\n" + "    LOCATION_CITY AS CITY,\r\n"
					+ "    LOCATION_STATE AS STATE,\r\n"
					+ "    LOCATION_PINCODE AS PINCODE,dtl.LOCATION_ID AS CODE,CONTACT_NO As CONTACT_NO,dtl.LOCATION_ID AS LOCATION_ID,dtl.GST_NUMBER AS GSTNO \r\n"
					+ "FROM\r\n"
					+ "    organization_location_dtl dtl inner join organization_info info on dtl.ORG_CODE = info.ORG_CODE where info.TENANT_ID = ? ";

			returnList = this.jdbcTemplate.query(retQry, new AddressDtlByDcTypeRowMapper(),tenantId);

		} catch (Exception ex) {
			logger.error("getIndentGrpHdrIdList error---> " + ex);
		}
		return returnList;

	}

	@Override
	public List<AddressDtlByDcTypeEntity> getCustomerCode(String custCode,String tenantId) {
		List<AddressDtlByDcTypeEntity> returnList = new ArrayList<AddressDtlByDcTypeEntity>();
		try {
			String retQry = "SELECT \r\n" + "    CUST_NAME AS NAME,\r\n" + "    GST AS GSTNO,\r\n"
					+ "    ADDRESS AS ADDRESS,\r\n" + "    CITY AS CITY,\r\n" + "    STATE AS STATE,\r\n"
					+ "    PINCODE AS PINCODE,CUST_CODE AS CODE,CONTACT_NO AS CONTACT_NO \r\n" + "FROM\r\n"
					+ "    customer_mst where TENANT_ID = ? ";

			returnList = this.jdbcTemplate.query(retQry, new AddressDtlByDcTypeRowMapper(),tenantId);

		} catch (Exception ex) {
			logger.error("getIndentGrpHdrIdList error---> " + ex);
		}
		return returnList;

	}

	@Override
	public List<PoInstoreDtlEntity> getpoInstoreDtlByPmId(String pmHdrId, String tenantId, String isFlag) {
		List<PoInstoreDtlEntity> returnList = new ArrayList<PoInstoreDtlEntity>();
		try {
			String retQry = "";
			if (isFlag.equalsIgnoreCase("1")) {
				retQry = "SELECT \r\n" + "    distinct(pm.PRODUCT_CODE),pkam.PK_DESC,pksam.PSK_DESC,\r\n"
						+ "    pm.*,\r\n" + "    um.UOM_LONG_DESCRIPTION,\r\n" + "    um.UOM_SHORT_DESCRIPTION\r\n"
						+ "FROM\r\n" + "    product_mst AS pm\r\n" + "        INNER JOIN\r\n"
						+ "    uom_mst AS um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + "		left Join\r\n"
						+ "	project_key_area pka on pka.PKA_ID = pm.PKA_ID\r\n" + "		left join\r\n"
						+ "	project_key_area_mst pkam on pkam.PK_ID = pka.PK_ID\r\n" + "		left Join\r\n"
						+ "	project_key_sub_area pksa on pksa.PKSA_ID = pm.PSKA_ID\r\n" + "		left join\r\n"
						+ "	project_key_sub_area_mst pksam on pksam.PSK_ID = pksa.PSK_ID\r\n" + "		\r\n"
						+ "WHERE\r\n" + "    \r\n"
						+ "       pm.TENANT_ID =? and PRODUCT_CATEGORY !=\"SE\" group by pm.PRODUCT_CODE order by pm.PRODUCT_CODE ";
				returnList = this.jdbcTemplate.query(retQry, new PoInstoreDtlRowMapper(), tenantId);
			} else {
				retQry = "SELECT \r\n" + "    pd.PRODUCT_QUANTITY_ON_HAND,\r\n" + "    pm.*,\r\n"
						+ "    um.UOM_LONG_DESCRIPTION,\r\n"
						+ "    um.UOM_SHORT_DESCRIPTION,pkam.PK_DESC,pksam.PSK_DESC\r\n" + "FROM\r\n"
						+ "    product_mst AS pm\r\n" + "        INNER JOIN\r\n"
						+ "    uom_mst AS um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + "        INNER JOIN\r\n"
						+ "    inventory_product_dtl pd ON pd.PRODUCT_ID = pm.PRODUCT_ID\r\n" + "    	left Join\r\n"
						+ "	project_key_area pka on pka.PKA_ID = pm.PKA_ID\r\n" + "		left join\r\n"
						+ "	project_key_area_mst pkam on pkam.PK_ID = pka.PK_ID\r\n" + "		left Join\r\n"
						+ "	project_key_sub_area pksa on pksa.PKSA_ID = pm.PSKA_ID\r\n" + "		left join\r\n"
						+ "	project_key_sub_area_mst pksam on pksam.PSK_ID = pksa.PSK_ID\r\n" + "WHERE\r\n"
						+ "    pm.PM_HDR_ID =?\r\n" + "        AND pm.TENANT_ID = ? \r\n"
						+ "        AND pd.PRODUCT_QUANTITY_ON_HAND > 0 \r\n"
						+ "        AND pd.INVENTORY_LOCATION_CODE = 'ILC0002' and  PRODUCT_CATEGORY !=\"SE\" order by PRODUCT_CODE";
				returnList = this.jdbcTemplate.query(retQry, new PoInstoreDtlRowMapper(), pmHdrId, tenantId);
			}

		} catch (Exception ex) {
			logger.error("getpoInstoreDtlByPmId error---> " + ex);
		}
		return returnList;

	}

	@Override
	public List<DcHdrEntity> getAllDcHdrByPmId(String pmHdrId, String tenantId, String getReturnable) {
		List<DcHdrEntity> returnList = new ArrayList<DcHdrEntity>();
		if (pmHdrId.equalsIgnoreCase("getAll")) {
			pmHdrId = "%%";
		}
		try {
			if(getReturnable == null|| getReturnable.isEmpty()) {
			   String retQry = "select hdr.*,phr.PROJECT_CODE,phr.PROJECT_NAME from dc_hdr hdr left join project_hdr phr on phr.PM_HDR_ID=hdr.PM_HDR_ID  where hdr.PM_HDR_ID like ? and hdr.TENANT_ID =?";
			   returnList = this.jdbcTemplate.query(retQry, new DcHdrRowMapper(), pmHdrId, tenantId);
			}else {
				String retQry = "select hdr.*,phr.PROJECT_CODE,phr.PROJECT_NAME from dc_hdr hdr left join project_hdr phr on phr.PM_HDR_ID=hdr.PM_HDR_ID  where hdr.PM_HDR_ID like ? and hdr.TENANT_ID =? and DC_TYPE = 'Returnable'";
				returnList = this.jdbcTemplate.query(retQry, new DcHdrRowMapper(), pmHdrId, tenantId);
			}

		} catch (Exception ex) {
			logger.error("getAllDcHdrByPmId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<DcHdrEntity> getDcHdrDtlById(String dcId, String tenantId) {
		List<DcHdrEntity> returnList = new ArrayList<DcHdrEntity>();
		try {
			String retQry = "select * from dc_hdr where DC_ID=? and TENANT_ID=?";

			returnList = this.jdbcTemplate.query(retQry, new DcHdrRowMapper(), dcId, tenantId);

		} catch (Exception ex) {
			logger.error("getDcDtlById error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<DcDtlEntity> getDtlByDcId(String dcId, String tenantId, String pmHdrId) {
		List<DcDtlEntity> returnList = new ArrayList<DcDtlEntity>();
		try {
			// old query
			// String retQry = "select dtl.*,uom.UOM_LONG_DESCRIPTION from dc_dtl dtl inner
			// join uom_mst uom on dtl.UOM= uom.UOM_CODE where DC_ID =? and TENANT_ID = ? ";

			// new query
//			String retQry = "SELECT \r\n" + "    dtl.*, uom.UOM_LONG_DESCRIPTION, pm.PRODUCT_CODE,\r\n" + "    pm.PRODUCT_ID AS PM_PRODUCT_ID \n"+ "FROM\r\n"
//					+ "    dc_dtl dtl\r\n" + "        LEFT JOIN\r\n" + "    uom_mst uom ON dtl.UOM = uom.UOM_CODE\r\n"
//					+ "        LEFT JOIN\r\n" + "    product_mst pm ON dtl.PRODUCT_ID = pm.PRODUCT_ID\r\n"  + "WHERE\r\n"
//					+ "    DC_ID = ? AND uom.TENANT_ID = ?";

			// Updated query with subquery for INDENT_DTL_ID
			String retQry = "SELECT \r\n" + 
					"    dtl.*, uom.UOM_LONG_DESCRIPTION, pm.PRODUCT_CODE, \r\n" + 
					"    pm.PRODUCT_ID AS PM_PRODUCT_ID \r\n" + 
//					"    ,( \r\n" + 
//					"        SELECT \r\n" + 
//					"            id.INDENT_DTL_ID \r\n" + 
//					"        FROM \r\n" + 
//					"            dc_hdr dh \r\n" + 
//					"        LEFT JOIN \r\n" + 
//					"            po_hdr ph ON dh.PO_NO = ph.PO_CODE \r\n" + 
//					"        LEFT JOIN \r\n" + 
//					"            indent_dtl id ON ph.INDENT_ID = id.INDENT_ID \r\n" + 
//					"        WHERE \r\n" + 
//					"            id.PRODUCT_CODE = dtl.PRODUCT_CODE \r\n" + 
//					"            AND id.TENANT_ID = uom.TENANT_ID \r\n" + 
//					"        LIMIT 1 \r\n" + 
//					"    ) AS INDENT_DTL_ID \r\n" + 
					"FROM \r\n" + 
					"    dc_dtl dtl INNER JOIN dc_hdr hdr \r\n" + 
					"ON dtl.DC_ID = hdr.DC_ID\r\n" + 
					"LEFT JOIN \r\n" + 
					"    uom_mst uom ON dtl.UOM = uom.UOM_CODE \r\n" + 
					"LEFT JOIN \r\n" + 
//					"    product_mst pm ON \r\n" + 
//					"     pm.PRODUCT_CODE = dtl.PRODUCT_CODE \r\n" + 
//					"    and pm.PRODUCT_DESCRIPTION = dtl.DESCRIPTION_OF_GOODS\r\n" +
//					in future needs to change the join only 
                  "    product_mst pm ON dtl.PRODUCT_ID = pm.PRODUCT_ID \r\n" + 
	                "WHERE \r\n" +
	                "    hdr.DC_ID = ? AND hdr.TENANT_ID = ? AND pm.PM_HDR_ID = ?";

			returnList = this.jdbcTemplate.query(retQry, new DcDtlRowMapper(), dcId, tenantId, pmHdrId);

		} catch (Exception ex) {
			logger.error("getDtlByDcId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public String getpoIdByPoDtlId(String poDtlId) {

		String returnList = "";
		try {
			String retQry = "select PO_ID from po_dtl where PO_DTL_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,poDtlId);
			returnList = resultMap.get("PO_ID").toString();

		} catch (Exception ex) {
			logger.error("getpoIdByPoDtlId error---> " + ex);
		}
		return returnList;
	}

	@Override
	public int getCountDtlByDcId(String dcId, String tenantId) {
		int getCountDtlByDcId = 0;
		try {
			String getCountDtlByDcIdStr = "select Count(*) as COUNT from dc_dtl  where DC_ID =? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCountDtlByDcIdStr,dcId);
			getCountDtlByDcId = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception e) {
			logger.error("getCountDtlByDcId method Error" + e);
		}
		return getCountDtlByDcId;
	}

	@Override
	public int insertDcHdr(DcHdrEntity dcHdrEntity) {
		int insertRes = 0;
		try {
			String insertDcHdr = "INSERT INTO `dc_hdr` (`DC_CODE`, `DC_TYPE`, `DC_DATE`, `PM_HDR_ID`, `PO_NO`,"
					+ " `PO_DATE`, `TRANSPORTATION_NAME`, `TRANSPORTATION_MODE`, `LR_DATE_TIME`, `LR_NO`, "
					+ "`SHIPPED_FROM`, `SHIPPED_FROM_ADDRESS`, `SHIPPED_FROM_DISTRICT`, `SHIPPED_FROM_STATE`, "
					+ "`SHIPPED_FROM_COUNTRY`, `DELIVERED_TO`, `DELIVERED_TO_ADDRESS`, `DELIVERED_TO_DISTRICT`,"
					+ " `DELIVERED_TO_STATE`, `DELIVERED_TO_COUNTRY`, `AMOUNT_IN_WORDS`, `REMARKS`, `TOTAL_BASIC`,"
					+ " `GST_VALUE`, `TOTAL_VALUE`, `TENANT_ID`,`FINANCIAL_YEAR_MST_ID`,`SHIPPED_FROM_GSTIN`, "
					+ "`SHIPPED_FROM_PINCODE`, `DELIVERED_FROM_PINCODE` ,`DELIVERED_FROM_GSTIN`,  `DIVISION`,"
					+ "`TRANSACTION_NO`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(),
					dcHdrEntity.getTenantId(), "dc_hdr", "5", jdbcTemplate, 1, 1, null,0);

			String dcCode = gen.getEnquiryCode();
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertDcHdr, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, dcCode);
					ps.setString(2, dcHdrEntity.getDcType());
					ps.setString(3, dcHdrEntity.getDcDate());
					ps.setString(4, dcHdrEntity.getPmHdrId());
					ps.setString(5, dcHdrEntity.getPoNO());
					ps.setString(6, dcHdrEntity.getPoDATE() == "" ? null : dcHdrEntity.getPoDATE());
					ps.setString(7, dcHdrEntity.getTransportationName());
					ps.setString(8, dcHdrEntity.getTransportationMode());
					ps.setString(9, dcHdrEntity.getLrDateTime()  == "" ? null : dcHdrEntity.getLrDateTime());
					ps.setString(10, dcHdrEntity.getLrNo());
					ps.setString(11, dcHdrEntity.getShippedFrom());
					ps.setString(12, dcHdrEntity.getShippedFromAddress());
					ps.setString(13, dcHdrEntity.getShippedFromDistrict());
					ps.setString(14, dcHdrEntity.getShippedFromState());
					ps.setString(15, dcHdrEntity.getShippedFromCountry());
					ps.setString(16, dcHdrEntity.getDeliveredTo());
					ps.setString(17, dcHdrEntity.getDeliveredToAddress());
					ps.setString(18, dcHdrEntity.getDeliveredToDistrict());
					ps.setString(19, dcHdrEntity.getDeliveredToState());
					ps.setString(20, dcHdrEntity.getDeliveredToCountry());
					ps.setString(21, dcHdrEntity.getAmountInWords());
					ps.setString(22, dcHdrEntity.getRemarks());
					ps.setString(23, dcHdrEntity.getTotalBasic());
					ps.setString(24, dcHdrEntity.getGstValue());
					ps.setString(25, dcHdrEntity.getTotalValue());
					ps.setString(26, dcHdrEntity.getTenantId());
					ps.setString(27, gen.getFinainceId());
					ps.setString(28, dcHdrEntity.getShippedGstIn());
					ps.setString(29, dcHdrEntity.getShippedPinCode());
					ps.setString(30, dcHdrEntity.getDeliveredPinCode());
					ps.setString(31, dcHdrEntity.getDeliveredGstIn());
					// ps.setString(32, dcHdrEntity.getRecNo());
					ps.setString(32, dcHdrEntity.getDivision());
					ps.setString(33, String.valueOf(gen.getSeq()));
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();
			if (insertRes > 0) {
				this.jdbcTemplate.update("UPDATE `dc_hdr` SET `REC_NO`=? WHERE `DC_ID`= ? ", insertRes, insertRes);
				String dcDtlInsertStr = "INSERT INTO `dc_dtl` (`DC_ID`, `DESCRIPTION_OF_GOODS`, `HSN_NO`, `QTY`, `UOM`, `RATE`, `TOTAL`,`PRODUCT_ID`, `PRODUCT_CODE`) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";
				for (int j = 0; j < dcHdrEntity.getDcDtlList().size(); j++) {
					String uom = "";
					if (dcHdrEntity.getDcDtlList().get(j).getUomDesc().equalsIgnoreCase("")) {
						uom = iIndentUploadDAO.getUomCodeByUnit(dcHdrEntity.getDcDtlList().get(j).getUom(),
								dcHdrEntity.getTenantId());
						if (uom.equalsIgnoreCase("0")) {
							iIndentUploadDAO.createNewUomAndInsert(dcHdrEntity.getDcDtlList().get(j).getUom().trim(),
									dcHdrEntity.getTenantId());
							uom = iIndentUploadDAO.getUomCodeByUnit(dcHdrEntity.getDcDtlList().get(j).getUom().trim(),
									dcHdrEntity.getTenantId());
						}
					} else {
						uom = dcHdrEntity.getDcDtlList().get(j).getUomDesc();
					}
                         String productId = "";
					if(dcHdrEntity.getDcDtlList().get(j).getProductId().equalsIgnoreCase("")) {
						String query = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(PRODUCT_ID) > 0 THEN PRODUCT_ID\r\n"
								+ "        ELSE 0\r\n" + "    END as COUNT\r\n" + "FROM\r\n" + "    product_mst\r\n" + "WHERE\r\n"
								+ "    PRODUCT_CODE = ?\r\n" + "        AND PM_HDR_ID = ?\r\n"
								+ "        AND TENANT_ID = ?;";

						Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(query, dcHdrEntity.getDcDtlList().get(j).getProductCode(),
								dcHdrEntity.getPmHdrId(),dcHdrEntity.getTenantId());
						int i = Integer.parseInt(resultMap1.get("COUNT").toString());
						if(i==0) {
							LocalDateTime createddatetime = LocalDateTime.now();

							final String date = String.valueOf(createddatetime);
                            String productCode =dcHdrEntity.getDcDtlList().get(j).getProductCode();
                            String desc = dcHdrEntity.getDcDtlList().get(j).getDescOfGoods();
                            String UOM = dcHdrEntity.getDcDtlList().get(j).getUomDesc();

							String productmstInsertquery = "insert into product_mst(PRODUCT_CODE,PRODUCT_DESCRIPTION,PM_HDR_ID,PRODUCT_UOM_CODE,CREATED_USER_ID,CREATED_DATETIME,LAST_UPDATED_USER_ID,LAST_UPDATED_DATETIME,TENANT_ID) values(?,?,?,?,?,?,?,?,?) ;";
							KeyHolder holder1 = new GeneratedKeyHolder();
							jdbcTemplate.update(new PreparedStatementCreator() {
								@Override
								public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
									PreparedStatement ps = connection.prepareStatement(productmstInsertquery,
											Statement.RETURN_GENERATED_KEYS);
									ps.setObject(1, productCode);
									ps.setString(2, desc);
									ps.setString(3, dcHdrEntity.getPmHdrId());
									ps.setString(4, UOM);
									ps.setString(5, "E0001");
									ps.setString(6, date);
									ps.setString(7, "E0001");
									ps.setString(8, date);
									ps.setString(9, dcHdrEntity.getTenantId());
									return ps;
								}
							}, holder1);
							productId = String.valueOf(holder1.getKey().intValue());
						}
					}else {
						 productId = dcHdrEntity.getDcDtlList().get(j).getProductId();
					}
					this.jdbcTemplate.update(dcDtlInsertStr, insertRes,
							dcHdrEntity.getDcDtlList().get(j).getDescOfGoods(),
							dcHdrEntity.getDcDtlList().get(j).getHsnNo(), dcHdrEntity.getDcDtlList().get(j).getQty(),
							uom, dcHdrEntity.getDcDtlList().get(j).getRate(),
							dcHdrEntity.getDcDtlList().get(j).getTotal(),
							productId,dcHdrEntity.getDcDtlList().get(j).getProductCode());

					String mrHdrId = dcHdrEntity.getDcDtlList().get(j).getMrHdrId();
					String Qty =  dcHdrEntity.getDcDtlList().get(j).getQty();
					if(mrHdrId !=null) {
					String dtlQtyCheckStr="SELECT \r\n" +
							"   Count(*) AS COUNT \r\n" +
							"FROM\r\n" +
							"    material_request_dtl dtl\r\n" +
							"        INNER JOIN\r\n" +
							"    material_request_hdr hdr ON dtl.MR_HDR_ID = hdr.MR_HDR_ID\r\n" +
							"WHERE\r\n" +
							"    dtl.MR_HDR_ID = ? AND dtl.PRODUCT_ID= ? AND dtl.TENANT_ID = ?";
					Map<String, Object>  resultCheck = this.jdbcTemplate.queryForMap(dtlQtyCheckStr, mrHdrId, productId, dcHdrEntity.getTenantId());
						int dtlQtyCheck = Integer.parseInt(resultCheck.get("COUNT").toString());
						if(dtlQtyCheck>0) {
					String DtlQty = "SELECT \r\n" +
							"    MR_DTL_ID\r\n" +
							"FROM\r\n" +
							"    material_request_dtl dtl\r\n" +
							"        INNER JOIN\r\n" +
							"    material_request_hdr hdr ON dtl.MR_HDR_ID = hdr.MR_HDR_ID\r\n" +
							"WHERE\r\n" +
							"    dtl.MR_HDR_ID = ? AND dtl.PRODUCT_ID= ? AND dtl.TENANT_ID = ?";
					Map<String, Object>  resultMap = this.jdbcTemplate.queryForMap(DtlQty, mrHdrId, productId, dcHdrEntity.getTenantId());
					String mrDtlId = resultMap.get("MR_DTL_ID").toString();

					String updateQry = "update material_request_dtl set ISSUED_QTY=ISSUED_QTY + ? where MR_DTL_ID=? and PRODUCT_ID = ? and TENANT_ID = ?";
					 this.jdbcTemplate.update(updateQry, Qty, mrDtlId, productId, dcHdrEntity.getTenantId());
						}
					}
                     String productCode="";
					if(!dcHdrEntity.getDcDtlList().get(j).getProductId().equalsIgnoreCase("")) {
					    productCode = getProdCodeByprodId(dcHdrEntity.getDcDtlList().get(j).getProductId());
					}else {
						productCode = dcHdrEntity.getDcDtlList().get(j).getProductCode();
					}
					if (!productCode.equalsIgnoreCase("")) {

//						String name = invLocType(dcHdrEntity.getShippedFrom(), dcHdrEntity.getTenantId());
						String name = dcHdrEntity.getDcDtlList().get(j).getLocationCode();

						if (name.equalsIgnoreCase("")) {
							name = "ILC0002";
						}

						CommonMethod.updateProductInvDtl(dcHdrEntity.getPmHdrId(), productId, name,
								new BigDecimal(dcHdrEntity.getDcDtlList().get(j).getQty()), "Subraction", "ITTC0018",
								dcCode, dcHdrEntity.getEmpId(), CommonMethod.getCurrentDateTime(),
								dcHdrEntity.getTenantId(), jdbcTemplate);
					}
				}
			}
		} catch (Exception e) {
			logger.error("insertDcHdr method Error" + e);
		}
		return insertRes;
	}

	@Override
	public int cancelDcHdr(GetDcDtlByDcIdRequest getDcDtlByDcIdReq) {
		int cancelDcHdr = 0;
		try {
			String cancelDcHdrStr = "UPDATE `dc_hdr` SET `IS_CANCEL`='1' WHERE `DC_ID`= ? ";
			cancelDcHdr = this.jdbcTemplate.update(cancelDcHdrStr, getDcDtlByDcIdReq.getDcHdrId());

		} catch (Exception e) {
			logger.error("cancelDcHdr method Error" + e);
		}
		return cancelDcHdr;
	}
	@Override
	public String getBinValue(String dcId) {
	    String bin = "";
	    try {
	        String qry = "SELECT CASE WHEN p.BIN IS NOT NULL THEN p.BIN ELSE '' END AS BIN " +
	                     "FROM dc_dtl d " +
	                     "JOIN product_mst p ON d.PRODUCT_ID = p.PRODUCT_ID AND d.PRODUCT_CODE = p.PRODUCT_CODE " +
	                     "WHERE d.DC_ID = ?";
	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, dcId);
	        bin = resultMap.get("BIN").toString();
	    } catch (Exception e) {
	        logger.error("getBinValue method Error: " + e);
	    }
	    return bin;
	}
	@Override
	public int increaseIntPrdDtl(String productId, String qty, String pmHdrId, String dcCode, String productCode,
			String empId, String tenantId) {
		int reduceIntPrdDtl = 0;
		try {
			// String updateInventoryProdDtl="update inventory_product_dtl set
			// PRODUCT_QUANTITY_ON_HAND = PRODUCT_QUANTITY_ON_HAND + ? where PRODUCT_ID = ?
			// and INVENTORY_LOCATION_CODE ='ILC0002'";
			// reduceIntPrdDtl=
			// this.jdbcTemplate.update(updateInventoryProdDtl,qty,productId);

			CommonMethod.updateProductInvDtl(pmHdrId, productId, "ILC0002", new BigDecimal(qty), "", "ITTC0018",
					dcCode, empId, CommonMethod.getCurrentDateTime(), tenantId, jdbcTemplate);
		} catch (Exception e) {
			logger.error("reduceIntPrdDtl method Error" + e);
		}
		return reduceIntPrdDtl;
	}

	@Override
	public String getTenantPropertyVal(String tenantId, String propertyName) {
		String getTenantPropertyVal = "";
		try {
			getTenantPropertyVal = GetPropertyValue.getPropValueByTenant(tenantId, propertyName, this.jdbcTemplate);
			;

		} catch (Exception e) {
			logger.error("getTenantPropertyVal method Error" + e);
		}
		return getTenantPropertyVal;
	}

	@Override
	public int getIndentCloseStatus(String indentId, String tenantId) {
		int getIndentCloseStatus = 0;
		try {
			String getIndentCloseStatusStr = "SELECT \r\n" + "    COUNT(*) as COUNT \r\n" + "FROM\r\n" + "    indent_hdr hdr\r\n"
					+ "        INNER JOIN\r\n" + "    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + "WHERE\r\n"
					+ "    hdr.INDENT_ID = ?\r\n" + "        AND dtl.TENANT_ID = ?\r\n" + "        AND dtl.INDENT_DTL_ID NOT IN (SELECT \r\n"
					+ "            dtl.INDENT_DTL_ID\r\n" + "        FROM\r\n" + "            po_dtl dtl\r\n"
					+ "                INNER JOIN\r\n" + "            po_hdr hdr ON hdr.PO_ID = dtl.PO_ID\r\n"
					+ "        WHERE\r\n" + "            INDENT_ID = ? AND hdr.IS_LATEST = 1\r\n"
					+ "                AND hdr.IS_APPROVED = 1);";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getIndentCloseStatusStr,indentId,tenantId,indentId);
			getIndentCloseStatus = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("getIndentCloseStatus method Error" + e);
		}
		return getIndentCloseStatus;
	}

	@Override
	public int updateindentClose(String indentId) {
		int updateindentClose = 0;
		try {
			String updateInventoryProdDtl = "UPDATE `indent_hdr` SET `CLOSED_DATE`=? WHERE `INDENT_ID`=?";
			updateindentClose = this.jdbcTemplate.update(updateInventoryProdDtl,CommonMethod.getCurrentDate(),indentId);
		} catch (Exception e) {
			logger.error("updateindentClose method Error" + e);
		}
		return updateindentClose;
	}

	@Override
	public String getIndentIdByPraId(String praId, String tenantId) {
		String indentId = "";
		try {
			String qry = "SELECT ph.INDENT_ID FROM pra_hdr prh " +
					"INNER JOIN po_hdr ph ON ph.PO_ID = prh.PO_ID " +
					"WHERE prh.PRA_ID = ? AND prh.TENANT_ID = ? LIMIT 1";
			indentId = this.jdbcTemplate.queryForObject(qry, String.class, praId, tenantId);
		} catch (Exception ex) {
			logger.error("getIndentIdByPraId method Error " + ex);
		}
		return indentId;
	}

	@Override
	public int getUncompletedPraCountByIndentId(String indentId, String tenantId) {
		int count = 0;
		try {
			String qry = "SELECT COUNT(*) FROM pra_hdr prh " +
					"INNER JOIN po_hdr ph ON ph.PO_ID = prh.PO_ID " +
					"WHERE ph.INDENT_ID = ? AND prh.TENANT_ID = ? " +
					"AND ph.IS_LATEST = 1 AND ph.IS_APPROVED = 1 AND prh.IS_LATEST = 1 " +
					"AND (prh.IS_COMPLETED IS NULL OR prh.IS_COMPLETED != '1')";
			count = this.jdbcTemplate.queryForObject(qry, Integer.class, indentId, tenantId);
		} catch (Exception ex) {
			logger.error("getUncompletedPraCountByIndentId method Error " + ex);
		}
		return count;
	}

	@Override
	public int getUncoveredNonInventoryCount(String indentId, String tenantId) {
		int count = 0;
		try {
			String qry = "SELECT COUNT(*) FROM indent_hdr hdr " +
					"INNER JOIN indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID " +
					"WHERE hdr.INDENT_ID = ? AND dtl.TENANT_ID = ? " +
					"AND dtl.INDENT_DTL_ID NOT IN (" +
						"SELECT dtl2.INDENT_DTL_ID FROM po_dtl dtl2 " +
						"INNER JOIN po_hdr hdr2 ON hdr2.PO_ID = dtl2.PO_ID " +
						"WHERE hdr2.INDENT_ID = ? AND hdr2.IS_LATEST = 1 AND hdr2.IS_APPROVED = 1) " +
					"AND dtl.QTY > COALESCE((" +
						"SELECT SUM(igd.INVENTORY) FROM indent_grp_dtl igd " +
						"INNER JOIN indent_grp_hdr igh ON igh.IG_HDR_ID = igd.IG_HDR_ID " +
						"WHERE igd.INDENT_DTL_ID = dtl.INDENT_DTL_ID AND igh.IS_INVENTORY = 1), 0)";
			count = this.jdbcTemplate.queryForObject(qry, Integer.class, indentId, tenantId, indentId);
		} catch (Exception ex) {
			logger.error("getUncoveredNonInventoryCount method Error " + ex);
		}
		return count;
	}

	@Override
	public List<String> getEmpListByIndentId(String indentId) {
		List<String> empList = new ArrayList<String>();
		try {
			String qry = " select distinct(UPDATED_BY) from indent_status_dtl where REFERENCE_ID='" + indentId + "'";
			empList = this.jdbcTemplate.queryForList(qry, String.class);
		} catch (Exception e) {
			logger.error("getEmpListByIndentId method Error" + e);
		}
		return empList;
	}

	@Override
	public List<String> getEmpListByPoId(String poId) {
		List<String> empList = new ArrayList<String>();
		try {
			String qry = " select distinct(UPDATED_BY) from po_status where PO_ID='" + poId + "'";
			empList = this.jdbcTemplate.queryForList(qry, String.class);
		} catch (Exception e) {
			logger.error("getEmpListByPoId method Error" + e);
		}
		return empList;
	}

	@Override
	public String getAppDesig(String docGrp, String currentseq,String tenantId) {
		String desc = "";
		try {
			String ApprovingDescQry = "SELECT \r\n" + "    APPR_DESI\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "    DOC_TYPE =?\r\n"
					+ "         AND CURR_SEQUENCE = ? AND TENANT_ID = '"+tenantId+"'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ApprovingDescQry,docGrp,currentseq);
			desc = resultMap.get("APPR_DESI").toString();
		} catch (Exception ex) {
			logger.error("getNxtAppDesc error " + ex);
		}
		return desc;
	}

	@Override
	public int materialInwardCheck(String poId) {
		int materialInwardCheck = 0;
		try {
			String materialInwardCheckStr = "select count(*) as COUNT from material_inward_hdr where PO_ID =? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(materialInwardCheckStr,poId);
			materialInwardCheck = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("materialInwardCheck method Error" + e);
		}
		return materialInwardCheck;
	}
	@Override
	public String getPendingTransportChrg(String poId) {
		String pendingChrg = "0.000";
		try {
			String qry = "SELECT SUM(TRANSPORT_CHARGE) AS SUM FROM pra_hdr WHERE PO_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, poId);

			Object sumObj = resultMap.get("SUM");
			if (sumObj != null) {
				pendingChrg = sumObj.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error fetching pending transport charge", e);
		}
		return pendingChrg;
	}

	@Override
	public String getPendingInsuranceChrg(String poId) {
		String pendingChrg = "0.000";
		try {
			String qry = "SELECT SUM(INSURANCE_CHARGE) AS SUM FROM pra_hdr WHERE PO_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, poId);

			Object sumObj = resultMap.get("SUM");
			if (sumObj != null) {
				pendingChrg = sumObj.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error fetching pending insurance charge", e);
		}
		return pendingChrg;
	}

	@Override
	public String getPendingPfChrg(String poId) {
		String pendingChrg = "0.000";
		try {
			String qry = "SELECT SUM(P_F_CHARGE) AS SUM FROM pra_hdr WHERE PO_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, poId);

			Object sumObj = resultMap.get("SUM");
			if (sumObj != null) {
				pendingChrg = sumObj.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error fetching pending PF charge", e);
		}
		return pendingChrg;
	}

	@Override
	public String getPendingOtherChrg(String poId) {
		String pendingChrg = "0.000";
		try {
			String qry = "SELECT SUM(OTHER_CHARGE) AS SUM FROM pra_hdr WHERE PO_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, poId);

			Object sumObj = resultMap.get("SUM");
			if (sumObj != null) {
				pendingChrg = sumObj.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error fetching pending OTHER_CHARGE", e);
		}
		return pendingChrg;
	}

	@Override
	public int praCheck(String poId) {
		int praCheck = 0;
		try {
			String praCheckQry = "select count(*) as COUNT from pra_hdr where PO_ID =? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCheckQry,poId);
			praCheck = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("praCheck method Error" + e);
		}
		return praCheck;
	}
	@Override
	public int praCheckForPendingValues(String poId) {
		int praCheck = 0;
		try {
			String praCheckQry = "select count(*) as COUNT from pra_hdr where PO_ID =? AND IS_LATEST=1 AND IS_COMPLETED!=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCheckQry,poId);
			praCheck = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("praCheck method Error" + e);
		}
		return praCheck;
	}
	@Override
	public int praCheckForCancelledPo(String poId) {
		int praCheck = 0;
		try {
			String praCheckQry = "select count(*) as COUNT from pra_hdr where PO_ID =? AND IS_LATEST=0";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCheckQry,poId);
			praCheck = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("praCheck method Error" + e);
		}
		return praCheck;
	}
	@Override
	public int praCheckByPotId(String potId) {
		int materialInwardCheck = 0;
		try {
			String materialInwardCheckStr = "SELECT CASE WHEN COUNT(*)>0 THEN COUNT(*) ELSE 0 END AS COUNT FROM pra_hdr WHERE POT_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(materialInwardCheckStr, potId);

			Object countObj = resultMap.get("COUNT");
			if (countObj != null) {
				materialInwardCheck = Integer.parseInt(countObj.toString());
			} else {
				logger.warn("COUNT is null for POT_ID: " + potId);
			}

		} catch (NullPointerException e) {
			logger.error("NullPointerException in praCheckByPotId for POT_ID: " + potId, e);
		} catch (Exception e) {
			logger.error("Exception in praCheckByPotId for POT_ID: " + potId, e);
		}
		return materialInwardCheck;
	}

	@Override
	public String gstSumByPotId(String poId){
		String pendingChrg = "";
		try{
			String qry = "Select SUM(IFNULL(GST_VALUE, 0) + IFNULL(IGST_VALUE, 0)) as SUM from pra_hdr where POT_ID=? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, poId);
			pendingChrg = resultMap.get("SUM").toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pendingChrg;
	}

	@Override
	public String getPoIdByIgscsId(String igscsId, String tenantId) {
		String poId = "";
		try {
			String qry = "SELECT PO_ID FROM po_hdr WHERE IG_SCS_ID = ? AND TENANT_ID = ? AND IS_LATEST = 0 ORDER BY PO_ID DESC LIMIT 1";

			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(qry, igscsId, tenantId);

			if (!resultList.isEmpty()) {
				poId = resultList.get(0).get("PO_ID").toString();
			}
		} catch (Exception e) {
			logger.error("Error in getPoIdByIgscsId" + e);
		}
		return poId;
	}
		@Override
		public String getPoIdByIgscsIdForCancelledPo(String igscsId, String tenantId){
			String poId = "";
			try{
				String qry = "select PO_ID from po_hdr where IG_SCS_ID=? AND IS_LATEST=0 AND TENANT_ID=?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, igscsId, tenantId);
				poId = resultMap.get("PO_ID").toString();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return poId;
		}


	@Override
	public int updatePoType(PoTypeUpdateReq poTypeReq) {
		int updateStatus = 0;
		try {
			String qry = " UPDATE po_hdr SET PO_TYPE=?, LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=? WHERE PO_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, poTypeReq.getPoType(), CommonMethod.getCurrentDateTime(),
					poTypeReq.getEmpId(), poTypeReq.getPoId());

		} catch (Exception e) {
			logger.error("updatePoType method Error" + e);
		}
		return updateStatus;
	}
	
	@Override
	public String getPmHdrIdByPoId(String poId,String tenantId) {
		String pmHdrId = "";
		try {
			String qry = "SELECT \r\n" + 
					"    ih.PROJECT_ID as PROJECT_ID \r\n" + 
					"FROM\r\n" + 
					"    po_hdr po\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr ih ON po.INDENT_ID = ih.INDENT_ID\r\n" + 
					"WHERE\r\n" + 
					"    PO_ID = ? AND po.TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,poId,tenantId);
			pmHdrId = (resultMap.get("PROJECT_ID").toString());

		} catch (Exception e) {
			logger.error("getPmHdrIdByPoId method Error" + e);
		}
		return pmHdrId;
	}
	

	@Override
	public List<PoHsnEntity> getHSNbyParno(String partNo, String tenantId) {
		List<PoHsnEntity> returnList = new ArrayList<PoHsnEntity>();
		try {
			String qry = "SELECT DISTINCT\r\n" + "    @a:=@a + 1 S_NO, pd.HSN_CODE, pd.UOM_CODE, um.UOM_SHORT_DESCRIPTION \r\n" + "FROM\r\n"
					+ "    po_dtl AS pd\r\n" + "        INNER JOIN\r\n"
					+ "    indent_dtl AS id ON pd.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" 
					+ "    INNER  JOIN uom_mst AS um ON um.UOM_CODE = pd.UOM_CODE,\r\n"
					+ "    (SELECT @a:=0) AS a\r\n"
					+ "WHERE\r\n" + "    id.DESCRIPTION = ? \r\n" + "        AND id.TENANT_ID = ? \r\n"
					+ "        AND pd.HSN_CODE IS NOT NULL group by pd.HSN_CODE ";
			returnList = this.jdbcTemplate.query(qry, new PoHsnRowMapper(), partNo, tenantId);

		} catch (Exception ex) {
			logger.error("getHSNbyParno error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDescMstEntity> getdivisionDesc(String tenantId) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select PD_ID AS TYPE_ID , PD_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID from po_division where TENANT_ID = ? and IS_ACTIVE =1";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getdivisionDesc error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDescMstEntity> getransitInsuranceDesc(String tenantId) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select PTI_ID AS TYPE_ID , PTI_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID  from po_transit_insurance where TENANT_ID =? and IS_ACTIVE =1;";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getransitInsuranceDesc error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDescMstEntity> getModeOfDispatchDesc(String tenantId) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select MOD_ID AS TYPE_ID , MOD_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID  from po_mode_of_dispatch where TENANT_ID =? and IS_ACTIVE =1";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getModeOfDispatchDesc error---> " + ex);
		}
		return returnList;
	}

	@Override
	public List<PoDescMstEntity> getInspectScopeDesc(String tenantId) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select IS_ID AS TYPE_ID , IS_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID from po_inspect_scope where TENANT_ID = ? and IS_ACTIVE =1";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getInspectScopeDesc error---> " + ex);
		}
		return returnList;
	}

	public List<PoDescMstEntity> getdivisionDescById(String tenantId, String id) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select PD_ID AS TYPE_ID , PD_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID from po_division where TENANT_ID = ? and IS_ACTIVE =1 and PD_ID = ? ";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId, id);

		} catch (Exception ex) {
			logger.error("getdivisionDescById error---> " + ex);
		}
		return returnList;
	}

	public List<PoDescMstEntity> getransitInsuranceDescById(String tenantId, String id) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select PTI_ID AS TYPE_ID , PTI_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID  from po_transit_insurance where TENANT_ID =? and IS_ACTIVE =1 and PTI_ID = ? ";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId, id);

		} catch (Exception ex) {
			logger.error("getransitInsuranceDescById error---> " + ex);
		}
		return returnList;
	}

	public List<PoDescMstEntity> getModeOfDispatchDescById(String tenantId, String id) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select MOD_ID AS TYPE_ID , MOD_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID  from po_mode_of_dispatch where TENANT_ID =? and IS_ACTIVE =1 and MOD_ID = ? ";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId, id);

		} catch (Exception ex) {
			logger.error("getModeOfDispatchDescById error---> " + ex);
		}
		return returnList;
	}
   public String getGstType (String tenantId, String vendorCode) {
	   String gstType = "";
		try {
			String gstTypeStr = "SELECT GST_TYPE\r\n" + 
					"FROM vendor_mst\r\n" + 
					"WHERE VENDOR_CODE = ? AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(gstTypeStr,vendorCode,tenantId);
			gstType = resultMap.get("GST_TYPE").toString();

		} catch (Exception e) {
			logger.error("gstType method Error" + e);
		}
		return gstType;
	   
   }
   public String getPartCount (String poHdrID) {
	   String partCount = "";
		try {
			String partCountStr = "SELECT COUNT(*) AS NumberOfProducts FROM po_dtl WHERE PO_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(partCountStr,poHdrID);
			partCount = resultMap.get("NumberOfProducts").toString();

		} catch (Exception e) {
			logger.error("getPartCount method Error" + e);
		}
		return partCount;
	   
   }
	public String getInspectionStatusByPoHdrId(String poHdrId, String tenantId){
		String inspectionStatus = "";
		try{
			String inspectionStatusQry = "select  sum(QTY - INSPECTED_QTY) as Inspection_qty from po_dtl dtl\n" +
					"inner join po_hdr hdr on hdr.PO_ID = dtl.PO_ID \n" +
					"where dtl.PO_ID = ? and hdr.TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(inspectionStatusQry, poHdrId, tenantId);
			inspectionStatus = resultMap.get("Inspection_qty").toString();
		} catch (Exception e) {
			logger.error("getInspectionStatusByPoHdrId error---> " + e);
		}
		Float quantity = (inspectionStatus != null && !inspectionStatus.isEmpty())
				? Float.parseFloat(inspectionStatus) : null;
		return (quantity != null && quantity > 0) ? "Pending" : "Completed";
	}
	public List<PoDescMstEntity> getInspectScopeDescById(String tenantId, String id) {
		List<PoDescMstEntity> returnList = new ArrayList<PoDescMstEntity>();
		try {
			String qry = "select IS_ID AS TYPE_ID , IS_DESC AS TYPE_DESC , IS_ACTIVE, TENANT_ID from po_inspect_scope where TENANT_ID = ? and IS_ACTIVE =1 and IS_ID = ? ";
			returnList = this.jdbcTemplate.query(qry, new PoDescMstRowMapper(), tenantId, id);

		} catch (Exception ex) {
			logger.error("getInspectScopeDescById error---> " + ex);
		}
		return returnList;
	}

	@Override
	public String invLocType(String name, String tenantId) {
		String invLocType = "";
		try {
			String invLocTypeStr = "select case when count(*) >0 then INVENTORY_LOC_TYPE else '' end AS LOC_TYPE from organization_location_dtl where LOCATION_REFERENCENAME = ? and TENANT_ID =? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(invLocTypeStr,name,tenantId);
			invLocType = resultMap.get("LOC_TYPE").toString();

		} catch (Exception e) {
			logger.error("invLocType method Error" + e);
		}
		return invLocType;
	}

	@Override
	public String getQcRequestyQty(String poDtlId) {

		String qty = "";
		try {
			String retQry = "select SUM(QTY_TO_BE_INSPECTED) as QTY_TO_BE_INSPECTED from quality_inspection_request where PO_DTL_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,poDtlId);
			qty = resultMap.get("QTY_TO_BE_INSPECTED").toString();

		} catch (Exception ex) {
			logger.error("getQcRequestyQty error---> " + ex);
		}
		return qty;
	}

	@Override
	public String getQcOkQty(String qiHdrId) {

		String qty = "";
		try {
			String retQry = "select OK_QTY from quality_inspection_hdr where QI_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,qiHdrId);
			qty = resultMap.get("OK_QTY").toString();

		} catch (Exception ex) {
			logger.error("getQcOkQty error---> " + ex);
		}
		return qty;
	}

	@Override
	public String getPoDtQty(String poDtlId){
		String qty = "";
		try{
			String retQry = "select QTY from po_dtl where PO_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,poDtlId);
			qty = resultMap.get("QTY").toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return qty;
	}

	@Override
	public int getQcIsCompletedStatus(String qiId) {

		int qcHdrId = 0;
		try {
			String retQry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN QI_HDR_ID\r\n" + 
					"        ELSE 0\r\n" + 
					"    END as COUNT\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_hdr\r\n" + 
					"WHERE\r\n" + 
					"    QI_ID = ? AND IS_COMPLETED = 1 AND CANCEL_FLAG = 0";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,qiId);
			qcHdrId = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getQcIsCompletedStatus error---> " + ex);
		}
		return qcHdrId;
	}
	
	@Override
	public int getCARaisedCount(String qiHdrId) {

		int cnt = 0;
		try {
			String retQry = "select count(*) as COUNT from quality_ca_dtl where QI_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,qiHdrId);
			cnt = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getCARaisedCount error---> " + ex);
		}
		return cnt;
	}
	
	@Override
	public int checkQcIsRaised(String qiId) {

		int cnt = 0;
		try {
			String retQry = "select count(*) as COUNT from quality_inspection_hdr where QI_ID = ? AND CANCEL_FLAG = 0";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,qiId);
			cnt = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("checkQcIsRaised error---> " + ex);
		}
		return cnt;
	}

	@Override
	public int getCaApprovedStatus(String qiHdrId) {

		int cnt = 0;
		try {
			String retQry = "select count(*) as COUNT from quality_ca_dtl where QI_HDR_ID=? and IS_APPROVED = 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,qiHdrId);
			cnt = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getCaApprovedStatus error---> " + ex);
		}
		return cnt;
	}

	@Override
	public List<RetrieveQualitInspectionEntity> getQcReqDetails(String poDtlId, String type){
		List<RetrieveQualitInspectionEntity> inspectQuality = new ArrayList<>();
		try {

//			String Qry = "SELECT \r\n" +
//					"    PO_CODE,\r\n" +
//					"    QI_CODE,\r\n" +
//					"    INDENT_DTL_ID,\r\n" +
//					"    PM_HDR_ID,\r\n" +
//					"    QI_ID,\r\n" +
//					"    PO_ID,\r\n" +
//					"    QTY_INSPECTED,\r\n" +
//					"    QTY_TO_BE_INSPECTED,\r\n" +
//					"    PO_DTL_ID\r\n" +
//					"FROM\r\n" +
//					"    quality_inspection_request \r\n" +
//					"WHERE\r\n" +
//					"    PO_DTL_ID='"+poDtlId+"'and REQUEST_FROM='PO'";
			
			String Qry = "SELECT \r\n" +
					"    PO_CODE,\r\n" +
					"    QI_CODE,\r\n" +
					"    INDENT_DTL_ID,\r\n" +
					"    req.PM_HDR_ID,\r\n" +
					"    req.QI_ID,\r\n" +
					"    PO_ID,\r\n" +
					"    QTY_INSPECTED,\r\n" +
					"    QTY_TO_BE_INSPECTED,\r\n" +
					"    PO_DTL_ID, CANCEL_FLAG, REWORK_QTY as REWORK_CNT, REWORK_INTERNAL as IS_REWORK \r\n" +
					"FROM\r\n" +
					"      quality_inspection_request req  left join quality_inspection_hdr hdr\r\n" +
					"       ON hdr.QI_ID = req.QI_ID \r\n" +
					"WHERE\r\n" +
					"     PO_DTL_ID='"+poDtlId+"' and REQUEST_FROM LIKE '"+type+"';";
		
			inspectQuality = this.jdbcTemplate.query(Qry, new RetrieveQualitInspectionRowMapper());


		} catch (Exception ex) {
			logger.error("getQcReqDetails Error" + ex);
		}
		return inspectQuality;
	}
	
	public String getQcForwaitingStatus(String poDtlId, String qiId) {
		String cnt = "0";
		try {
			String retQry = "SELECT \r\n" + 
					"        CASE WHEN QTY_TO_BE_INSPECTED > 0 THEN\r\n" + 
					"    QTY_TO_BE_INSPECTED ELSE 0 END AS QTY_TO_BE_INSPECTED\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request req left join quality_inspection_hdr hdr \r\n" + 
					"    on hdr.QI_ID = req.QI_ID and CANCEL_FLAG !=1 and IS_COMPLETED != 1\r\n" +
					"WHERE\r\n" +
					" PO_DTL_ID = ? AND req.QI_ID = ?; ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry, poDtlId, qiId);
			cnt = resultMap.get("QTY_TO_BE_INSPECTED").toString();

		} catch (Exception ex) {
			logger.error("getQcForwaitingStatus error---> " + ex);
		}
		return cnt;
	}


	@Override
	public String getMiOkDetails(String poDtlId) {
		String cnt = "";
		try {
			String retQry = " SELECT \r\n" + 
					"        CASE WHEN SUM(OK_QTY+CA_INTERNAL+CA_VENDOR) > 0 THEN\r\n" + 
					"    SUM(OK_QTY+CA_INTERNAL+CA_VENDOR) ELSE 0 END AS QTY_INSPECTED\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request req left join quality_inspection_hdr hdr \r\n" + 
					"    on hdr.QI_ID = req.QI_ID\r\n" + 
					"WHERE\r\n" + 
					"     REQUEST_FROM = 'MI' AND CANCEL_FLAG !=1 and IS_COMPLETED != 0\r\n" + 
					"        AND PO_DTL_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(retQry,poDtlId);
			cnt = resultMap.get("QTY_INSPECTED").toString();

		} catch (Exception ex) {
			logger.error("getMiOkDetails error---> " + ex);
		}
		return cnt;
	}
	
	@Override
	public List<RetrieveQualitInspectionEntity> getMiQcReqDetails(String miDtlId, String poDtlId){
		List<RetrieveQualitInspectionEntity> inspectQuality = new ArrayList<>();
		try {

			String Qry = "SELECT \r\n" +
					"    req.PO_CODE,\r\n" +
					"    QI_CODE,\r\n" +
					"    INDENT_DTL_ID,\r\n" +
					"    req.PM_HDR_ID,\r\n" +
					"    req.QI_ID,\r\n" +
					"    req.PO_ID,\r\n" +
					"    QTY_INSPECTED,\r\n" +
					"    QTY_TO_BE_INSPECTED,\r\n" +
					"    req.PO_DTL_ID, CANCEL_FLAG, (hdr.REWORK_INTERNAL+hdr.REWORK_VENDOR) as REWORK_CNT, " +
				    " (hdr.REJECTED_INTERNAL+hdr.REJECTED_EXTERNAL) as REJECT_CNT \r\n" +
					"FROM\r\n" +
					"      quality_inspection_request req  left join quality_inspection_hdr hdr\r\n" +
					"       ON hdr.QI_ID = req.QI_ID \r\n" +
					"WHERE\r\n" +
					"    (MI_DTL_ID='"+miDtlId+"' and req.PO_DTL_ID='"+poDtlId+"') and REQUEST_FROM like '%%';";
			inspectQuality = this.jdbcTemplate.query(Qry, new RetrieveQualitInspectionRowMapper());


		} catch (Exception ex) {
			logger.error("getMiQcReqDetails Error" + ex);
		}
		return inspectQuality;
	}

	@Override
	public int getIsInventoryCount(String IndentId, String tenantId) {
		// TODO Auto-generated method stub
		int isInvCount = 0;
		try {
			String IsInvQry = "SELECT \r\n" + "    COUNT(*) as COUNT \r\n" + "FROM\r\n" + "    indent_grp_hdr igh\r\n"
					+ "        INNER JOIN\r\n" + "    indent_grp_dtl igd ON igd.IG_HDR_ID = igh.IG_HDR_ID\r\n"
					+ "WHERE\r\n" + "    IS_INVENTORY = '1'\r\n" + "        AND FIND_IN_SET(igd.INDENT_DTL_ID,\r\n"
					+ "            (SELECT \r\n" + "                    GROUP_CONCAT(INDENT_DTL_ID)\r\n"
					+ "                FROM\r\n" + "                    indent_hdr hdr\r\n"
					+ "                        INNER JOIN\r\n"
					+ "                    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n"
					+ "                WHERE\r\n" + "                    hdr.INDENT_ID = ? and hdr.TENANT_ID=?));";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(IsInvQry,IndentId,tenantId);
			isInvCount = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getIsInventoryCount error---> " + ex);
		}

		return isInvCount;
	}

	@Override
	public List<GetProductUnitCostEntity> getProductUnitCostList(String poHdr, String tenantId) {
		List<GetProductUnitCostEntity> returnList = new ArrayList<GetProductUnitCostEntity>();
		try {
			String qry = "SELECT \n" + "    pm.PRODUCT_ID, podtl.UNITE_RATE\n" + "FROM\n" + "    po_hdr hdr,\n"
					+ "    po_dtl podtl,\n" + "    indent_hdr ihdr,\n" + "    indent_dtl idtl,\n"
					+ "    product_mst pm\n" + "WHERE\n" + "    hdr.PO_ID = podtl.PO_ID\n"
					+ "        AND podtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\n"
					+ "        AND ihdr.INDENT_ID = idtl.INDENT_ID\n"
					+ "        AND idtl.PRODUCT_CODE = pm.PRODUCT_CODE AND idtl.DESCRIPTION = pm.PRODUCT_DESCRIPTION\n"
					+ "        AND idtl.SPECIFICATION=pm.SPECIFICATION AND pm.PM_HDR_ID = ihdr.PROJECT_ID\n"
					+ "        AND hdr.PO_ID = ? AND hdr.TENANT_ID = ? \n" + "GROUP BY podtl.PO_DTL_ID ";
			returnList = this.jdbcTemplate.query(qry, new GetProductUnitCostRowMapper(), poHdr, tenantId);

		} catch (Exception ex) {
			logger.error("getProductUnitCostList error---> " + ex);
		}
		return returnList;
	}

	@Override
	public int updateproductMstUnitCost(String productId, String cost) {
		int udpateproductMstUnitCost = 0;
		try {
			String udpateproductMstUnitCostStr = "update product_mst set PRODUCT_COST_PER_UNIT = ? where PRODUCT_ID = ? ";
			udpateproductMstUnitCost = this.jdbcTemplate.update(udpateproductMstUnitCostStr, cost, productId);

		} catch (Exception e) {
			logger.error("udpateproductMstUnitCost method Error" + e);
		}
		return udpateproductMstUnitCost;
	}

	@Override
	public String getPaymentTermStatus(String potId) {
		String returnList = "";
		try {
			String retQry = "SELECT "
					+ " '1' as CREATED, "
					+ " PRA_ID, "
					+ " PRA_DATE, "
					+ " PRA_CODE, "
					+ " typ.DOCUMENT_STATUS_TYPE_DESCRIPTION "
					+ " FROM pra_hdr pra "
					+ " INNER JOIN document_status_type_code typ "
					+ " ON typ.DOCUMENT_STATUS_TYPE_CODE = pra.STATUS_CODE "
					+ " WHERE POT_ID = ? "
					+ " AND pra.IS_LATEST = 1 "
					+ " ORDER BY pra.PRA_ID DESC "
					+ " LIMIT 1";

			Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(retQry, potId);

			String created = (String) resultMap.getOrDefault("CREATED", "0");
			Date praDate = (Date) resultMap.get("PRA_DATE");
			String praCode = (String) resultMap.getOrDefault("PRA_CODE", "0");
			String documentStatus = (String) resultMap.getOrDefault("DOCUMENT_STATUS_TYPE_DESCRIPTION", "0");

			String praDateString = (praDate != null) ? praDate.toString() : "0";

			returnList = created + "|" + praDateString + "|" + praCode + "|" + documentStatus;

		} catch (Exception ex) {
			logger.error("getPaymentTermStatus error---> " + ex);

			// return safe default values
			returnList = "0|0|0|0";
		}
		return returnList;
	}

	public List<GetPoDtlsEntity> getPreRevisionPoDtls(String igScpId, String revision, String tenantId) {
		List<GetPoDtlsEntity> returnList = new ArrayList<GetPoDtlsEntity>();
		try {
			String retQry = "SELECT * FROM po_hdr WHERE IG_SCS_ID = ? and REVISION = ? - 1 AND TENANT_ID = ?";

			returnList = this.jdbcTemplate.query(retQry, new PoHdrRowMapper(), igScpId, revision, tenantId);

		} catch (Exception ex) {
			logger.error("getPreRevisionPoDtls error---> " + ex);

		}
		return returnList;
	}

	@Override
	public int praCheckByPoId(String poId) {
		int praCheck = 0;
		try {
			String praCheckStr = "SELECT COUNT(*) AS COUNT FROM pra_hdr WHERE PO_ID = ? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCheckStr, poId);

			Object countObj = resultMap.get("COUNT");
			if (countObj != null) {
				praCheck = Integer.parseInt(countObj.toString());
			} else {
				logger.warn("COUNT is null for POT_ID: " + poId);
			}

		} catch (NullPointerException e) {
			logger.error("NullPointerException in praCheckByPotId for POT_ID: " + poId, e);
		} catch (Exception e) {
			logger.error("Exception in praCheckByPotId for POT_ID: " + poId, e);
		}
		return praCheck;
	}

	@Override
	public int praIsAvilableCheck(String poId) {
		int praCheck = 0;
		try {
			String praCheckQry = "select count(*) as COUNT from pra_hdr where PO_ID =? AND IS_LATEST=1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(praCheckQry,poId);
			praCheck = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception e) {
			logger.error("praCheck method Error" + e);
		}
		return praCheck;
	}
	
	@Override
	public String getPendingAmtForPraStatus(String poId) {
	    String praPendingVal = "0";
	    try {
	        String praCheckQry = "SELECT COALESCE(SUM(PENDING_AMOUNT), 0) AS PENDING_AMOUNT " +
	                             "FROM po_payment_term " +
	                             "WHERE PO_ID = ? AND IS_LAST != 2";
	        Double result = jdbcTemplate.queryForObject(praCheckQry,Double.class, poId);
	        if (result != null) {
	        	praPendingVal = String.valueOf(Math.round(result));
	        }
	    } catch (Exception e) {
	        logger.error("getPendingAmtForPraStatus error: " + e);
	    }
	    return praPendingVal;
	}

	@Override
	public String getApprovedPoTotalByPkaId(String pkaId) {
	    String totalVal = "0";
	    try {
	        String qry = "SELECT CASE WHEN COUNT(*) > 0 THEN SUM(ph.BASIC_TOTAL) ELSE 0 END AS VAL " +
	                     "FROM po_hdr ph " +
	                     "INNER JOIN indent_hdr ih ON ih.INDENT_ID = ph.INDENT_ID " +
	                     "WHERE ih.PKA_ID = ? AND ph.IS_LATEST = 1 AND ph.IS_APPROVED = 1";
	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pkaId);
	        totalVal = resultMap.get("VAL").toString();
	    } catch (Exception e) {
	        logger.error("getApprovedPoTotalByPkaId error: " + e);
	    }
	    return totalVal;
	}

	@Override
	public String getApprovedPoTotalByProjectId(String projectId) {
	    String totalVal = "0";
	    try {
	        String qry = "SELECT CASE WHEN COUNT(*) > 0 THEN SUM(ph.BASIC_TOTAL) ELSE 0 END AS VAL " +
	                     "FROM po_hdr ph " +
	                     "INNER JOIN indent_hdr ih ON ih.INDENT_ID = ph.INDENT_ID " +
	                     "WHERE ih.PROJECT_ID = ? AND ph.IS_LATEST = 1 AND ph.IS_APPROVED = 1";
	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, projectId);
	        totalVal = resultMap.get("VAL").toString();
	    } catch (Exception e) {
	        logger.error("getApprovedPoTotalByProjectId error: " + e);
	    }
	    return totalVal;
	}

	@Override
	public String getApprovedPoTotalByProjectIdAndSbcCode(String projectId, String sbcCode) {
	    String totalVal = "0";
	    try {
	        String qry = "SELECT CASE WHEN COUNT(*) > 0 THEN SUM(ph.BASIC_TOTAL) ELSE 0 END AS VAL " +
	                     "FROM po_hdr ph " +
	                     "INNER JOIN indent_hdr ih ON ih.INDENT_ID = ph.INDENT_ID " +
	                     "WHERE ih.PROJECT_ID = ? AND ih.SBC_CODE = ? AND ph.IS_LATEST = 1 AND ph.IS_APPROVED = 1";
	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, projectId, sbcCode);
	        totalVal = resultMap.get("VAL").toString();
	    } catch (Exception e) {
	        logger.error("getApprovedPoTotalByProjectIdAndSbcCode error: " + e);
	    }
	    return totalVal;
	}

}
