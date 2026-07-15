package com.vmfg.sales.dao.impl;

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
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.dao.interfaces.ISalesBudgetSheetDAO;
import com.vmfg.sales.entity.BudgetKeyCategory;
import com.vmfg.sales.entity.BudgetListEntity;
import com.vmfg.sales.entity.BudgetSheetFileEntity;
import com.vmfg.sales.entity.BudgetSheetPaymentEntity;
import com.vmfg.sales.entity.SalesBudgetSheetHdrEntity;
import com.vmfg.sales.request.DeleteBudgetSheetRequest;
import com.vmfg.sales.request.DeleteBudgetValue;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;
import com.vmfg.sales.request.getFileConfigDtlRequest;
import com.vmfg.sales.rowmapper.BudgetSheetPaymentRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetSheetDtlRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetSheetExntDtlRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetSheetRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Transactional
@Repository
public class SalesBudgetSheetDAO implements ISalesBudgetSheetDAO {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetDAO.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IEnquiryDAO iEnquiryDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDao;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Override
	public ResponseAsList getSalesBudgetSheetHdrAndDtl(String masterId, String tenantId,String isBudgetFlag) {
		ResponseAsList returnList = new ResponseAsList();
		List<SalesBudgetSheetHdrEntity> list = new ArrayList<>();
		try {

//			String query = " SELECT \r\n" + "    sbsh.SB_HDR_ID,\r\n" + "    sbsh.TOTAL_BUDGET_COST,\r\n"
//					+ "    sbsh.PAYMENT_TERMS,\r\n" + "    sbsh.TRANSACTION_STATUS,\r\n"
//					+ "    sbsh.TRANSACTION_STATUS_SEQ,\r\n" + "    sbsd.MASTER_ID,\r\n" + "    sbc.SBC_DESC,\r\n"
//					+ "    sbsd.VALUE\r\n" + "FROM\r\n" + "    sales_budget_sheet_hdr AS sbsh\r\n" + "WHERE\r\n"
//					+ "    sbsh.MASTER_ID = ?\r\n" + "        AND sbsh.TENANT_ID = ?";
			
			String addQry="";
			if(isBudgetFlag.equalsIgnoreCase("1")) {
				addQry=" and (sbsd.SBC_ID is null or sbsd.SBC_ID = '0') ";
			}else {
				addQry=" and sbsd.SBC_ID is not null ";
			}
			final String addFinalQry = addQry;
			String query = " SELECT \r\n" + "    sbsh.SB_HDR_ID,\r\n" + "    sbsh.TOTAL_BUDGET_COST,\r\n"
					+ "    sbsh.PAYMENT_TERMS,\r\n" + "    dtl.TRANSACTION_STATUS,SALE_PERCENT,FINAL_SALE_VALUE,sbsh.CR_COST,sbsh.FINAL_CR_SALE_COST,sbsh.CR_SALE_PERCENT,\r\n"
					+ "    dtl.TRANSACTION_STATUS_SEQ\r\n" + "FROM\r\n"
					+ "    sales_budget_sheet_hdr AS sbsh inner join sales_bs_dtl dtl on dtl.MASTER_ID = sbsh.MASTER_ID  \r\n"
					+ "WHERE\r\n" + "    sbsh.MASTER_ID = ?\r\n" + "        AND sbsh.TENANT_ID = ?";
			list = this.jdbcTemplate.query(query, new SalesBudgetSheetRowMapper(), masterId, tenantId);

			list.forEach(li -> {
				String getQ = "SELECT \r\n" + "\r\n" + "    sbsd.SB_DTL_ID,\r\n" + "    sbc.SBC_DESC,\r\n"
						+ "    sbsd.VALUE,sbsd.SBC_ID\r\n" + "FROM\r\n" + "    sales_budget_sheet_dtl AS sbsd \r\n"
						+ "        INNER JOIN\r\n"
						+ "    sales_budget_category AS sbc ON sbsd.KEY_CATEGORY = sbc.SBC_CODE\r\n" + "WHERE\r\n"
						+ "    sbsd.SB_HDR_ID = ?\r\n" + "        AND sbsd.TENANT_ID = ? "+addFinalQry;

				li.setSalesDtlList(
						this.jdbcTemplate.query(getQ, new SalesBudgetSheetDtlRowMapper(), li.getSbHdrId(), tenantId));
			});
			
			String qry = "SELECT * FROM budget_sheet_payment_terms WHERE BS_HDR_ID = ?";
			for (SalesBudgetSheetHdrEntity sheet : list) {
			    List<BudgetSheetPaymentEntity> paymentTerms = this.jdbcTemplate.query(qry, new BudgetSheetPaymentRowMapper(), sheet.getSbHdrId());
			    sheet.setPaymentTermList(paymentTerms);
			}


			if (list.size() > 0) {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
				returnList.setResponseData(list);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getSalesBudgetSheetHdrAndDtl Error" + ex);
		}
		return returnList;
	}

	@Override
	public String insertOrUpdateSalesBudgetSheetHdr(String masterId, String tenantId, String paymentTerms,
			String totalBudgetCost, String transactionStatus, String transactionStatusSeq, String sbHdrId) {
		String dtlSbHdrId = "";

		try {
			// INSERT SALES HDR
			if (sbHdrId == null || sbHdrId.isEmpty()) {
				String query = "insert into sales_budget_sheet_hdr(MASTER_ID,TOTAL_BUDGET_COST,PAYMENT_TERMS,TENANT_ID) "
						+ "values(?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, masterId);
						ps.setString(2, totalBudgetCost);
						ps.setString(3, paymentTerms);
						ps.setString(4, tenantId);
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				dtlSbHdrId = insertRes + "";

				// UPDATE SALES HDR
			} else {
				String updateQuery = "Update sales_budget_sheet_hdr set MASTER_ID=?," + "TOTAL_BUDGET_COST=?,"
						+ "PAYMENT_TERMS=? , TENANT_ID=?" + " WHERE SB_HDR_ID=? ";
				this.jdbcTemplate.update(updateQuery, masterId, totalBudgetCost, paymentTerms, tenantId, sbHdrId);

			}

		} catch (Exception ex) {
			logger.error("insertOrUpdateSalesBudgetSheetHdrAndDtl DAO method exception-->" + ex);
		}
		logger.debug("insertOrUpdateSalesBudgetSheetHdrAndDtl DAO method ends");
		return dtlSbHdrId;
	}

	@Override
	public ResponseAsMessage insertOrUpdateSalesBudgetSheetDtl(String masterId, String tenantId, String paymentTerms,
			String totalBudgetCost, String transactionStatus, String transactionStatusSeq, String sbHdrId,
			String sbDtlId, String keyCategory, String dtlSbHdrId, String dtlTenantId, String dtlValue) {
		ResponseAsMessage resp = new ResponseAsMessage();

		try {

			// INSERT SALES DTL
			if (sbDtlId == null || sbDtlId.isEmpty()) {

				String query = "insert into sales_budget_sheet_dtl(SB_HDR_ID,KEY_CATEGORY,VALUE,TENANT_ID) "
						+ "values(?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, dtlSbHdrId);
						ps.setString(2, keyCategory);
						ps.setString(3, dtlValue);
						ps.setString(4, dtlTenantId);

						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();

				if (insertRes > 0) {
					resp.setResponseCode(ResponseMessageMap.responseCodeOk);
					resp.setResponseMessage(ResponseMessageMap.successInserted);

				} else {
					resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);

				}
				// UPDATE DTL SALES
			} else {
				String updateQuery = "Update sales_budget_sheet_dtl set SB_HDR_ID=?," + "KEY_CATEGORY=?," + "VALUE=?,"
						+ "TENANT_ID=?" + " WHERE SB_DTL_ID=? ";
				int updateId = this.jdbcTemplate.update(updateQuery, dtlSbHdrId, keyCategory, dtlValue, dtlTenantId,
						sbDtlId);
				if (updateId > 0) {
					resp.setResponseCode(ResponseMessageMap.responseCodeOk);
					resp.setResponseMessage(ResponseMessageMap.successUpdated);

				} else {
					resp.setResponseMessage(ResponseMessageMap.responseCodeNotOk);

				}

			}

		} catch (Exception ex) {
			logger.error("insertOrUpdateSalesBudgetSheetHdrAndDtl DAO method exception-->" + ex);
		}
		logger.debug("insertOrUpdateSalesBudgetSheetHdrAndDtl DAO method ends");
		return resp;
	}

	@Override
	public ResponseAsList getKeyCategory(getFileConfigDtlRequest request) {
		logger.debug("getKeyCategory DAO method start");
		ResponseAsList resp = new ResponseAsList();
		try {
			List<BudgetKeyCategory> catLi = null;
			String getQ = "";
			if (request.getEnquiryId().isEmpty()) {
				getQ = "SELECT SBC_CODE,SBC_DESC FROM sales_budget_category WHERE IS_DEFAULT = 1 AND IS_ACTIVE = 1 AND TENANT_ID=?";
				catLi = this.jdbcTemplate.query(getQ, new SalesBudgetRowMapper(), request.getTenantId());
			} else {
				getQ = "SELECT \r\n" + "    sbc.SBC_CODE , sbc.SBC_DESC\r\n" + "FROM\r\n"
						+ "    sales_budget_sheet_hdr hdr,\r\n" + "    sales_budget_sheet_dtl dtl,\r\n"
						+ "    sales_budget_category sbc\r\n" + "WHERE\r\n" + "    hdr.SB_HDR_ID = dtl.SB_HDR_ID\r\n"
						+ "    and sbc.SBC_CODE = dtl.KEY_CATEGORY\r\n"
						+ "        AND MASTER_ID = ? and hdr.TENANT_ID=? and dtl.TENANT_ID=?";
				catLi = this.jdbcTemplate.query(getQ, new SalesBudgetRowMapper(), request.getEnquiryId(),
						request.getTenantId(), request.getTenantId());
			}

			if (catLi.size() > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.success);
				resp.setResponseData(catLi);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getKeyCategory DAO method exception-->" + ex);
		}
		logger.debug("getKeyCategory DAO method ends");
		return resp;
	}

	@Override
	public ResponseAsMessage deleteKeyCategory(DeleteBudgetValue request) {
		ResponseAsMessage msg = new ResponseAsMessage();
		try {

			String delQ = "DELETE FROM sales_budget_sheet_dtl WHERE SB_DTL_ID=" + request.getSbDtlId();
			this.jdbcTemplate.update(delQ);
			msg.setResponseCode(ResponseMessageMap.responseCodeOk);
			msg.setResponseMessage(ResponseMessageMap.noRecord);
			msg.setResponseDataMessage(ResponseMessageMap.successfulDeleted);

		} catch (Exception ex) {
			logger.error("deleteKeyCategory DAO method exception-->" + ex);
		}
		return msg;
	}

	@Override
	public int uploadBudgetSheetTemplate(BudgetListEntity budgetSheetFileEntity) {
		int hdrMstCnt = 0;
		try {
			String newCode = "";
			String masterId = budgetSheetFileEntity.getMasterId();
			String tenantId = budgetSheetFileEntity.getTenantId();
			BigDecimal salePercent = new BigDecimal(budgetSheetFileEntity.getSalePercent());
			String finalSale = budgetSheetFileEntity.getFinalSaleVal();

			String hdrMstidQry = "select case when count(*) > 0 then SB_HDR_ID else 0 end as SB_HDR_ID from sales_budget_sheet_hdr where MASTER_ID =?"
					  + " and TENANT_ID=?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(hdrMstidQry, masterId, tenantId);
			hdrMstCnt = Integer.parseInt(resultMap.get("SB_HDR_ID").toString());
			if (hdrMstCnt > 0) {
				
				
				if (budgetSheetFileEntity.getIsBudget().equalsIgnoreCase("1")) { 
					String hdrUpdate = "UPDATE sales_budget_sheet_hdr SET SALE_PERCENT=?,FINAL_SALE_VALUE=?,TENANT_ID =?,PAYMENT_TERMS=? where MASTER_ID=?";
					this.jdbcTemplate.update(hdrUpdate, salePercent, finalSale, tenantId,
							budgetSheetFileEntity.getPaymentTerms(), masterId);
					String deleteBd = "delete from sales_budget_change where SB_HDR_ID=? and SBC_ID>0";
					this.jdbcTemplate.update(deleteBd, hdrMstCnt);
					
					String delExtnQry = "DELETE sales_budget_sheet_extn FROM sales_budget_sheet_extn\r\n"
							+ "				        INNER JOIN\r\n"
							+ "				    sales_budget_sheet_dtl ON sales_budget_sheet_extn.SB_DTL_ID = sales_budget_sheet_dtl.SB_DTL_ID\r\n"
							+ "				    inner join sales_budget_sheet_hdr hdr on hdr.SB_HDR_ID = sales_budget_sheet_dtl.SB_HDR_ID\r\n"
							+ "				    where hdr.SB_HDR_ID=? and SB_EXTN_ID>0";
					this.jdbcTemplate.update(delExtnQry, hdrMstCnt);
					String delDtl = "DELETE FROM sales_budget_sheet_dtl WHERE SB_HDR_ID=?;";
					this.jdbcTemplate.update(delDtl, hdrMstCnt);
				}
				if(!budgetSheetFileEntity.getPaymentTermList().get(0).getTerm().isEmpty()) {
				  String delTerm = "DELETE FROM budget_sheet_payment_terms WHERE BS_HDR_ID=?;";
				  this.jdbcTemplate.update(delTerm, hdrMstCnt);
				}

			} else {
				String hdrQry = "INSERT INTO sales_budget_sheet_hdr (MASTER_ID,SALE_PERCENT,FINAL_SALE_VALUE,TENANT_ID) VALUES (?, ?, ?, ?)";
				KeyHolder holder = new GeneratedKeyHolder();
				hdrMstCnt = this.jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(hdrQry, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, masterId);
						ps.setBigDecimal(2, salePercent);
						ps.setString(3, finalSale);
						ps.setString(4, tenantId);
						return ps;
					}

				}, holder);
				hdrMstCnt = holder.getKey().intValue();
			}
			int dttID =0;
			String chkQry = "select case when count(*)> 0 then SB_DTL_ID else 0 end as SB_DTL_ID from sales_budget_sheet_dtl where KEY_CATEGORY = ? and TENANT_ID= ? AND SB_HDR_ID= ? and SBC_ID is null " ;
			 Map<String,Object>resultData;
			//	 Map<String,Object>resultDtl = jdbcTemplate.queryForMap(chkQ, newCode, tenantId, hdrMstCnt);
	//		  dttID = Integer.parseInt(resultDtl.get("SB_DTL_ID").toString());
//			String sbc_id = " and SBC_ID is "+null;
			String sbc_value=null;
			if (budgetSheetFileEntity.getIsBudget().equalsIgnoreCase("0")) {
				String insertSalesChange = "INSERT INTO `sales_budget_change`\r\n" + "(`SB_HDR_ID`,\r\n"
						+ "`REMARKS`,\r\n" + "`CR_VALUE`,\r\n" + "`CR_DATETIME`,\r\n" + "`TENANT_ID`)\r\n"
						+ "VALUES\r\n" + "(?,?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();
				final int intVal = hdrMstCnt;
				final String remarks = budgetSheetFileEntity.getRemarks();
				this.jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertSalesChange, Statement.RETURN_GENERATED_KEYS);

						ps.setInt(1, intVal);
						ps.setString(2, remarks);
						ps.setString(3, "0");
						ps.setString(4, CommonMethod.getCurrentDateTime());
						ps.setString(5, tenantId);
						return ps;
					}

				}, holder);
				sbc_value = holder.getKey().intValue() + "";
				
				 chkQry = "select case when count(*)> 0 then SB_DTL_ID else 0 end as SB_DTL_ID from sales_budget_sheet_dtl where KEY_CATEGORY = ?\r\n"
						+ " and TENANT_ID= ? AND SB_HDR_ID= ? and SBC_ID = ? " ;	  
			//	 Map<String,Object>resultData = jdbcTemplate.queryForMap(chkQry, newCode, tenantId, hdrMstCnt, sbc_value);
			//	 dttID = Integer.parseInt(resultData.get("SB_DTL_ID").toString());
//				sbc_id = " and SBC_ID = '"+sbc_value+"'";
			}
			BigDecimal totalSum=BigDecimal.ZERO;
			for (BudgetSheetFileEntity lis : budgetSheetFileEntity.getList()) {

				String mstInfo = "select count(*) as COUNT  from sales_budget_category where SBC_DESC= ? and TENANT_ID=?";
				Map<String,Object> resultDataCount = jdbcTemplate.queryForMap(mstInfo, lis.getSalesDescription(),tenantId);
				int mstCnt = Integer.parseInt(resultDataCount.get("COUNT").toString());
				
				if (mstCnt > 0) {
					String mstCode = "select SBC_CODE from sales_budget_category where SBC_DESC=? and TENANT_ID=?";
					Map<String,Object> resultCode = jdbcTemplate.queryForMap(mstCode, lis.getSalesDescription(),tenantId);
					newCode = resultCode.get("SBC_CODE").toString();
				} else {
					String mstCodeQry = "select SBC_CODE from sales_budget_category order by SBC_CODE desc limit 1";
					Map<String,Object> resultCode = jdbcTemplate.queryForMap(mstCodeQry);
					String mstCodeval = resultCode.get("SBC_CODE").toString();
					newCode = CommonMethod.getUomNewCode("SBC", mstCodeval, "SBC001");
					String SliceData = lis.getSalesDescription();
					char ShortDesc = SliceData.charAt(0);
					String FirstWord = String.valueOf(ShortDesc);

					String mstInstQry = "INSERT INTO sales_budget_category (SBC_CODE, SHORT_DESC, SBC_DESC, IS_ACTIVE,TENANT_ID) VALUES (?, ?, ?, ? ,?)";
					this.jdbcTemplate.update(mstInstQry, newCode, FirstWord, lis.getSalesDescription(), "1", tenantId);
				}

//				String chkQ = "select case when count(*)> 0 then SB_DTL_ID else 0 end from sales_budget_sheet_dtl where KEY_CATEGORY ='"
//						+ newCode + "' and TENANT_ID='" + tenantId + "' AND SB_HDR_ID='" + hdrMstCnt
//						+"'"+  sbc_id ;
//				int dttID = this.jdbcTemplate.queryForObject(chkQ, Integer.class);
				if(sbc_value !=null) {
				 resultData = jdbcTemplate.queryForMap(chkQry, newCode, tenantId, hdrMstCnt, sbc_value);
				 dttID = Integer.parseInt(resultData.get("SB_DTL_ID").toString());
				}else {
					 resultData = jdbcTemplate.queryForMap(chkQry, newCode, tenantId, hdrMstCnt);
					 dttID = Integer.parseInt(resultData.get("SB_DTL_ID").toString());
				}
				if (dttID == 0) {
					String insQry = "INSERT INTO sales_budget_sheet_dtl (SB_HDR_ID, KEY_CATEGORY, TENANT_ID,SBC_ID) VALUES (?, ?, ?,?)";
					KeyHolder holder = new GeneratedKeyHolder();
					final int intVal = hdrMstCnt;
					final String newCodeVal = newCode;
					final String newCodeSbc = sbc_value;
					this.jdbcTemplate.update(new PreparedStatementCreator() {
						@Override
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							PreparedStatement ps = con.prepareStatement(insQry, Statement.RETURN_GENERATED_KEYS);

							ps.setInt(1, intVal);
							ps.setString(2, newCodeVal);
							ps.setString(3, tenantId);
							ps.setString(4, newCodeSbc);
							return ps;
						}

					}, holder);
					dttID = holder.getKey().intValue();
				}
					
				if (lis.getValue().equalsIgnoreCase("")) {
					lis.setValue("0");
				}

				if (lis.getTotalValue().equalsIgnoreCase("")) {
					lis.setTotalValue("0");
				}

				if (lis.getSubTotalvalue().equalsIgnoreCase("")) {
					lis.setSubTotalvalue("0");
				}

//				if (lis.getCritical().equalsIgnoreCase("Y") || lis.getCritical().equalsIgnoreCase("yes")) {
//					lis.setCritical("1");
//				} else if (lis.getCritical().equalsIgnoreCase("")) {
//					lis.setCritical("0");
//				}

				String criticalVal = lis.getCritical() != null ? lis.getCritical().trim() : "";
				if (criticalVal.equalsIgnoreCase("Y") || criticalVal.equalsIgnoreCase("Yes")) {
					lis.setCritical("1");
				} else if (criticalVal.equalsIgnoreCase("N") || criticalVal.equalsIgnoreCase("No") || criticalVal.isEmpty()) {
					lis.setCritical("0");
				} else {
					lis.setCritical("0");
				}



				if (lis.getValueSubAssy().equalsIgnoreCase("")) {
					lis.setValueSubAssy("0");
				}
				if (lis.getTimelineInWeeks().equalsIgnoreCase("")) {
					lis.setTimelineInWeeks("0");
				}
				String insExtnQrys = "INSERT INTO sales_budget_sheet_extn (SB_DTL_ID, ELEMENT_HDR, STN_NO, LEG, ELEMENT_DTL, SPECIFICATION, \r\n"
						+ "MAKE, QTY, UOM, VALUE, SUB_TOTAL_VALUE, SUB_ASSY_VALUE, CONTIGENCY, TOTAL_VALUE, CRITICAL, TIMELINE_IN_WEEKS,\r\n"
						+ " ALLOCATED_QTY, ALLOCATED_VALUE, TENANT_ID,SBC_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
				KeyHolder holder = new GeneratedKeyHolder();
				final int dtlVal = dttID;
				final String newCodeSbc = sbc_value;
				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insExtnQrys, Statement.RETURN_GENERATED_KEYS);

						ps.setInt(1, dtlVal);
						ps.setString(2, lis.getElementHeader());
						ps.setString(3, lis.getStnNo());
						ps.setString(4, lis.getLeg());
						ps.setString(5, lis.getElementDescription());
						ps.setString(6, lis.getSpecification());
						ps.setString(7, lis.getMake());
						ps.setString(8, lis.getQty());
						ps.setString(9, lis.getUom());
						ps.setString(10, lis.getValue());
						ps.setString(11, lis.getSubTotalvalue());
						ps.setString(12, lis.getValueSubAssy());
						ps.setString(13, lis.getContingency());
						ps.setString(14, lis.getTotalValue());
						ps.setString(15, lis.getCritical());
						ps.setString(16, lis.getTimelineInWeeks());
						ps.setString(17, "0");
						ps.setString(18, "0");
						ps.setString(19, tenantId);
						ps.setString(20, newCodeSbc);
						return ps;
					}

				}, holder);
				
				totalSum = totalSum.add(new BigDecimal(lis.getTotalValue()!=null&&lis.getTotalValue()!=""?lis.getTotalValue():"0"));
				String valueQry = "select case when sum(TOTAL_VALUE) is null then 0 else sum(TOTAL_VALUE) end as TOTAL from sales_budget_sheet_extn where SB_DTL_ID=?";
				Map<String,Object> resultval = jdbcTemplate.queryForMap(valueQry,dttID);
				String value = resultval.get("TOTAL").toString();
				
				String valUpdateQry = "UPDATE sales_budget_sheet_dtl SET VALUE=? where SB_DTL_ID=?";
				this.jdbcTemplate.update(valUpdateQry, value, dttID);

				String totalCastVal = "select case when sum(VALUE) is null then 0 else sum(VALUE) end as VALUE from sales_budget_sheet_dtl where SB_HDR_ID=?";
				Map<String,Object> resultTotalVal = jdbcTemplate.queryForMap(totalCastVal,hdrMstCnt);
				String totalVal = resultTotalVal.get("VALUE").toString();
				if (budgetSheetFileEntity.getIsBudget().equalsIgnoreCase("1")) {
					String totalUpQry = "Update sales_budget_sheet_hdr SET TOTAL_BUDGET_COST =?,BUDGET_COST=? where MASTER_ID=?";

					this.jdbcTemplate.update(totalUpQry, totalVal, totalVal, masterId);
				}

			}
			String totalBud = "select TOTAL_BUDGET_COST from sales_budget_sheet_hdr where MASTER_ID= ?";
			Map<String,Object> resultTotalVal = jdbcTemplate.queryForMap(totalBud,masterId);
			BigDecimal totalBudDataDec = new BigDecimal( resultTotalVal.get("TOTAL_BUDGET_COST").toString());
			//BigDecimal incVal = totalBudDataDec.multiply((salePercent).divide(new BigDecimal("100")));
			BigDecimal saleValue = totalBudDataDec.add(salePercent);

			if (budgetSheetFileEntity.getIsBudget().equalsIgnoreCase("0")) {

				String getQ = "SELECT \r\n" + "    CASE\r\n" + "        WHEN SUM(VALUE) IS NOT NULL THEN SUM(VALUE)\r\n"
						+ "        ELSE 0\r\n" + "    END AS VALUE\r\n" + "FROM\r\n" + "    sales_budget_sheet_dtl\r\n"
						+ "WHERE\r\n" + "    SB_HDR_ID = ? AND SBC_ID IS NOT NULL";
				Map<String,Object> resultValue = jdbcTemplate.queryForMap(getQ,hdrMstCnt);
				BigDecimal cr = new BigDecimal( resultValue.get("VALUE").toString());
				
				String getQry = "SELECT     CASE        WHEN SUM(CR_SALE_PERCENT) IS NOT NULL THEN SUM(CR_SALE_PERCENT)\r\n" + 
						" ELSE 0    END AS VALUE FROM    sales_budget_sheet_hdr\r\n" + 
						"   WHERE    MASTER_ID = ? AND MASTER_ID IS NOT NULL;";
				Map<String,Object> result = jdbcTemplate.queryForMap(getQry,masterId);
				BigDecimal cr_per = new BigDecimal( result.get("VALUE").toString());
				
				String totalUpQry = "Update sales_budget_sheet_hdr SET CR_COST =?,CR_SALE_PERCENT = ?,FINAL_CR_SALE_COST =? where MASTER_ID=?";
				this.jdbcTemplate.update(totalUpQry,cr,cr_per.add(new BigDecimal(budgetSheetFileEntity.getCrSalePercent())),cr.add(new BigDecimal(budgetSheetFileEntity.getCrSalePercent())).add(cr_per), masterId);
				totalUpQry = "Update sales_budget_change SET CR_VALUE =? where SBC_ID=?";
				this.jdbcTemplate.update(totalUpQry, cr.add(new BigDecimal(budgetSheetFileEntity.getCrSalePercent())).add(cr_per), sbc_value);
				saleValue.add(cr);
				
				if (budgetSheetFileEntity.getPaymentTermList() != null && !budgetSheetFileEntity.getPaymentTermList().isEmpty()) {
					for (BudgetSheetPaymentEntity terms : budgetSheetFileEntity.getPaymentTermList()) {
						String insQry = "INSERT INTO budget_sheet_payment_terms (BS_HDR_ID, TERM, PERCENTAGE, PLANNED_DATE, ACTUAL_DATE) VALUES (?, ?, ?, ?, ?)";
						KeyHolder holder = new GeneratedKeyHolder();
						final int intVal = hdrMstCnt;
						final String term = terms.getTerm();
						final String percentage = terms.getPercentage();
						final String plannedDate = terms.getPlannedDate();
						final String actualDate = terms.getPlannedDate(); // Use actual date if available, not plannedDate
						this.jdbcTemplate.update(new PreparedStatementCreator() {
							@Override
							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
								PreparedStatement ps = con.prepareStatement(insQry, Statement.RETURN_GENERATED_KEYS);
								ps.setInt(1, intVal);
								ps.setString(2, term);
								ps.setString(3, percentage);
								ps.setString(4, plannedDate);
								ps.setString(5, actualDate);
								return ps;
							}
						}, holder);
					}
				}
			}else {

			String updatesalePercent = "update sales_budget_sheet_hdr set SALE_PERCENT=?,FINAL_SALE_VALUE=?,PAYMENT_TERMS=? where MASTER_ID=?";
			this.jdbcTemplate.update(updatesalePercent, salePercent, saleValue, budgetSheetFileEntity.getPaymentTerms(),
					masterId);

//			if(budgetSheetFileEntity.getPaymentTerms() == null || budgetSheetFileEntity.getPaymentTerms().isEmpty() ) {
//				for(BudgetSheetPaymentEntity terms : budgetSheetFileEntity.getPaymentTermList()) {
////	                 int ptId=0;
//						String insQry = "INSERT INTO budget_sheet_payment_terms (BS_HDR_ID, TERM, PERCENTAGE,PLANNED_DATE,ACTUAL_DATE) VALUES (?, ?, ?, ?, ?)";
//						KeyHolder holder = new GeneratedKeyHolder();
//						final int intVal = hdrMstCnt;
//						final String term = terms.getTerm();
//						final String percenteage = terms.getPercentage();
//						final String plannedDate = terms.getPlannedDate();
//						final String actualDate = terms.getPlannedDate();
//						this.jdbcTemplate.update(new PreparedStatementCreator() {
//							@Override
//							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//								PreparedStatement ps = con.prepareStatement(insQry, Statement.RETURN_GENERATED_KEYS);
//
//								ps.setInt(1, intVal);
//								ps.setString(2, term);
//								ps.setString(3, percenteage);
//								ps.setString(4, plannedDate);
//								ps.setString(5, actualDate);
//								return ps;
//							}
//
//						}, holder);
////						ptId = holder.getKey().intValue();
//				 }
//			}

// Save payment terms if provided
				if (budgetSheetFileEntity.getPaymentTermList() != null && !budgetSheetFileEntity.getPaymentTermList().isEmpty()) {
					for (BudgetSheetPaymentEntity terms : budgetSheetFileEntity.getPaymentTermList()) {
						String insQry = "INSERT INTO budget_sheet_payment_terms (BS_HDR_ID, TERM, PERCENTAGE, PLANNED_DATE, ACTUAL_DATE) VALUES (?, ?, ?, ?, ?)";
						KeyHolder holder = new GeneratedKeyHolder();
						final int intVal = hdrMstCnt;
						final String term = terms.getTerm();
						final String percentage = terms.getPercentage();
						final String plannedDate = terms.getPlannedDate();
						final String actualDate = terms.getPlannedDate(); // Use actual date if available, not plannedDate
						this.jdbcTemplate.update(new PreparedStatementCreator() {
							@Override
							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
								PreparedStatement ps = con.prepareStatement(insQry, Statement.RETURN_GENERATED_KEYS);
								ps.setInt(1, intVal);
								ps.setString(2, term);
								ps.setString(3, percentage);
								ps.setString(4, plannedDate);
								ps.setString(5, actualDate);
								return ps;
							}
						}, holder);
					}
				}


			}
			String enqCode = iEnquiryDAO.getsaleEnquiryCode(masterId);
			String curSeq=iEnquiryDAO.getsaleEnquiryCurSeq(masterId);
			String pmHdrId="0";
			List<String> messageList = new ArrayList<>();
			List<String> otherEmp = new ArrayList<>();
			messageList.add("Enquiry " + enqCode);
			int ScnName=0;
			if (budgetSheetFileEntity.getIsBudget().equalsIgnoreCase("0")) {
				ScnName=59;
			}else {
				ScnName=7;
			}
			
			String nextApproveDesig = commonNotifyMethod.getNxtAppDesc(budgetSheetFileEntity.getDocumentTypeCode(),
					curSeq,tenantId);

			commonNotifyMethod.InvokeNotificationMethod(1, ScnName, null, tenantId, messageList, otherEmp, "1",
					budgetSheetFileEntity.getPmId(), masterId, null);
			commonNotifyMethod.InvokeApprovalDesigMethod(budgetSheetFileEntity.getPmId(),
					budgetSheetFileEntity.getDocumentTypeCode(), masterId, pmHdrId, tenantId, budgetSheetFileEntity.getEmpId(), nextApproveDesig,
					curSeq, enqCode);
		} catch (Exception e) {
			logger.error("uploadIndentTemplate DAO  Method Exception" + e);
		}

		return hdrMstCnt;
	}

	@Override
	public List<SalesBudgetSheetExntDtlEntity> getcriticalListByPmHdrId(String pmHdrId, String tenantId) {
		List<SalesBudgetSheetExntDtlEntity> list = new ArrayList<SalesBudgetSheetExntDtlEntity>();
		try {

			String qry = "SELECT \n" + "    extn.*\n" + "FROM\n" + "    sales_budget_sheet_hdr shdr,\n"
					+ "    sales_budget_sheet_dtl sdtl,\n" + "    sales_budget_sheet_extn extn,\n"
					+ "    project_hdr hdr\n" + "WHERE\n" + "    hdr.ENQUIRY_ID = shdr.MASTER_ID\n"
					+ "        AND shdr.SB_HDR_ID = sdtl.SB_HDR_ID\n" + "        AND extn.SB_DTL_ID = sdtl.SB_DTL_ID\n"
					+ "        AND hdr.PM_HDR_ID = ? and (CRITICAL>0 or TIMELINE_IN_WEEKS>0) and hdr.TENANT_ID =  ? ";
			list = this.jdbcTemplate.query(qry, new SalesBudgetSheetExntDtlRowMapper(), pmHdrId, tenantId);

		} catch (Exception ex) {
			logger.error("getcriticalListByPmHdrId Error" + ex);
		}
		return list;
	}

	@Override
	public int deleteBudgetSheetCR(DeleteBudgetSheetRequest deleteBudgetSheetRequest) {
		ResponseAsMessage msg = new ResponseAsMessage();
		int update = 0;

		try {
			
			 String checkQuery = "SELECT \r\n" +
			            "  CASE \r\n" +
			            "    WHEN COUNT(CASE WHEN p.ALLOCATED_VALUE != 0 THEN 1 END) = 0 THEN 1\r\n" +
			            "    ELSE 0\r\n" +
			            "  END AS result\r\n" +
			            "FROM sales_budget_sheet_extn s\r\n" +
			            "INNER JOIN project_key_area_extn p\r\n" +
			            "ON s.SB_EXTN_ID = p.SB_EXTN_ID\r\n" +
			            "WHERE s.SBC_ID = ? ";
			int canDelete = this.jdbcTemplate.queryForObject(checkQuery,Integer.class,deleteBudgetSheetRequest.getSbcId());
			
            if(canDelete == 1) {
            logger.info("Valid to Delete");
            int CR_Cost = 0; int CR_Value = 0; int offer_Value=0; int pre_conVal=0;
            String strQ1 = "select CR_COST from sales_budget_sheet_hdr where sb_hdr_id=?";
			 CR_Cost = this.jdbcTemplate.queryForObject(strQ1,Integer.class,deleteBudgetSheetRequest.getHdrId());
			
			String strQ = "select SUM(VALUE) from sales_budget_sheet_dtl where sbc_id=?" ; 
			 CR_Value = this.jdbcTemplate.queryForObject(strQ,Integer.class, deleteBudgetSheetRequest.getSbcId());
			 
			  CR_Cost = CR_Cost - CR_Value;
			 
//			String StrQ = "select CR_SALE_PERCENT from sales_budget_sheet_hdr where SB_HDR_ID=?" ; 
//			per_Value = this.jdbcTemplate.queryForObject(StrQ, Integer.class,deleteBudgetSheetRequest.getHdrId());
			 
		    String qry = "SELECT COALESCE((\r\n" + 
		    		"    SELECT CR_VALUE\r\n" + 
		    		"    FROM sales_budget_change \r\n" + 
		    		"    WHERE sbc_id < ? AND SB_HDR_ID = ? \r\n" + 
		    		"    ORDER BY sbc_id DESC\r\n" + 
		    		"    LIMIT 1\r\n" + 
		    		"), 0) AS offer_Value" ; 
		    offer_Value = this.jdbcTemplate.queryForObject(qry, Integer.class,deleteBudgetSheetRequest.getSbcId(), deleteBudgetSheetRequest.getHdrId());		 
			  
		         pre_conVal = Math.max(0, offer_Value - CR_Cost);
//			     fin_Value = Math.max(0, CR_Cost + offer_Value);
					 
			String delQ1 = "delete from sales_budget_sheet_extn where SBC_ID= ? ";
			this.jdbcTemplate.update(delQ1, deleteBudgetSheetRequest.getSbcId());
			
			String delQ = "delete from sales_budget_sheet_dtl where SBC_ID= ? " ; 
			this.jdbcTemplate.update(delQ, deleteBudgetSheetRequest.getSbcId());
		
			String deleteChangeTableQry = "delete from sales_budget_change where SBC_ID= ?";
			this.jdbcTemplate.update(deleteChangeTableQry, deleteBudgetSheetRequest.getSbcId());
					
			String updateHdrQry = "update sales_budget_sheet_hdr set FINAL_CR_SALE_COST= ?, CR_COST= ?, CR_SALE_PERCENT= ? where SB_HDR_ID= ? ";
			update = this.jdbcTemplate.update(updateHdrQry, offer_Value, CR_Cost, pre_conVal, deleteBudgetSheetRequest.getHdrId());
			
		//	String updateFinalVal = "update sales_budget_sheet_hdr set FINAL_SALE_VALUE='"+fiVal1.subtract(crValue1)+"' where SB_HDR_ID='"+deleteBudgetSheetRequest.getHdrId()+"'";
		//	this.jdbcTemplate.update(updateFinalVal);

			msg.setResponseCode(ResponseMessageMap.responseCodeOk);
			msg.setResponseMessage(ResponseMessageMap.noRecord);
			msg.setResponseDataMessage(ResponseMessageMap.successfulDeleted);
            }else {
            	update = 2;
            	 logger.info("Deletion skipped as condition was not met.");
                 
            }
		} catch (Exception ex) {
			logger.error("deleteBudgetSheetCR DAO method exception-->" + ex);
		}
		return update;

	}
}
