package com.vmfg.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.sales.entity.FinancialYearTransactionMstEntity;
import com.vmfg.sales.rowmapper.FinancialYearTransactionMstRowMapper;

public class FinanaceCodeGen {
	private static final Logger logger = LoggerFactory.getLogger(FinanaceCodeGen.class);

	public static String getFinancialMstId(String date, String tenantId, JdbcTemplate jdbcTemplate) {
		String financialMstId = "";
		try {
			String financialMstIdStr = "SELECT \r\n"
					+ "    case when count(*) > 0 then FINANCIAL_YEAR_MST_ID else \"0\" end AS ID\r\n" + "FROM\r\n"
					+ "    financial_year_mst WHERE ? \r\n"
					+ " BETWEEN START_DATE AND END_DATE\r\n" + " AND TENANT_ID =  ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,date,tenantId);
			financialMstId = resultMap.get("ID").toString();

		} catch (Exception e) {
			logger.error("getfinacicalTransDtlByDocType Error" + e);
		}
		return financialMstId;
	}
	
	public static String getFinancialYr(String date, String tenantId, JdbcTemplate jdbcTemplate) {
		String financialMstId = "";
		try {
			String financialMstIdStr = "SELECT \r\n"
					+ "    case when count(*) > 0 then FINANCIAL_YEAR else \"0\" end AS ID\r\n" + "FROM\r\n"
					+ "    financial_year_mst\r\n" + "WHERE\r\n" + "  ?  BETWEEN START_DATE AND END_DATE AND TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,date,tenantId);
			financialMstId = resultMap.get("ID").toString();

		} catch (Exception e) {
			logger.error("getfinacicalTransDtlByDocType Error" + e);
		}
		return financialMstId;
	}

	public static int checkFinancialNewSeq(String financialId, String tenantId, String tableName,
			JdbcTemplate jdbcTemplate) {
		int financeID = 1;
		try {
			String financialMstIdStr = "SELECT   COUNT(*) as COUNT FROM  "+tableName+" \r\n" 
					+ "WHERE FINANCIAL_YEAR_MST_ID ='"+financialId+"' \r\n"
					+ "        AND TENANT_ID = '"+tenantId+"'";
			
			//Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,,,);
			int financialMstCountId = jdbcTemplate.queryForObject(financialMstIdStr, Integer.class);
			if (financialMstCountId > 0) {

				String getQ = " select TRANSACTION_NO +1 as TRANSACTION_NO from "+tableName+" where FINANCIAL_YEAR_MST_ID=? and TENANT_ID=? ORDER BY TRANSACTION_NO DESC limit 1";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,financialId,tenantId);
				financeID = Integer.parseInt(resultMap1.get("TRANSACTION_NO").toString());

			}

		} catch (Exception e) {
			logger.error("checkFinancialNewSeq Error" + e);
		}
		return financeID;
	}
	public static int checkFinancialcurYearNewSeq(String enqDate, String tenantId, String tableName,
			JdbcTemplate jdbcTemplate,String columnName) {
		int financeID = 1;
		try {
			String financialMstIdStr = "select count(*) AS ID from "+tableName+" where YEAR("+columnName+") = ? and TENANT_ID= ? order by TRANSACTION_NO ";
			
			//Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,,,);
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,enqDate,tenantId);
		int	financialMstCountId = Integer.parseInt(resultMap.get("ID").toString());
			if (financialMstCountId > 0) {

				String getQ = " select TRANSACTION_NO +1 as TRANSACTION_NO from "+tableName+" where YEAR("+columnName+") = ? and TENANT_ID= ? order by TRANSACTION_NO desc limit 1";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,enqDate,tenantId);
				financeID = Integer.parseInt(resultMap1.get("TRANSACTION_NO").toString());

			}

		} catch (Exception e) {
			logger.error("checkFinancialcurYearNewSeq Error" + e);
		}
		return financeID;
	}
	public static int checkFinancialcurYearNewSeqForPOCde(String enqDate, String tenantId, String tableName,
			JdbcTemplate jdbcTemplate,String columnName,String pmHdrId) {
		int financeID = 1;
		try {
			String financialMstIdStr = "select count(*) AS ID from po_hdr hdr inner join indent_hdr ind on hdr.INDENT_ID = ind.INDENT_ID where ind.PROJECT_ID =? and YEAR(DATE) = ?  and hdr.TENANT_ID = ? order by TRANSACTION_NO ";
			
			//Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,,,);
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,pmHdrId,enqDate,tenantId);
		int	financialMstCountId = Integer.parseInt(resultMap.get("ID").toString());
			if (financialMstCountId > 0) {

				String getQ = "select hdr.TRANSACTION_NO +1  as TRANSACTION_NO from po_hdr hdr inner join indent_hdr ind on hdr.INDENT_ID = ind.INDENT_ID where ind.PROJECT_ID = ? and YEAR(DATE) = ? and hdr.TENANT_ID = ? order by TRANSACTION_NO  desc limit 1";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,pmHdrId,enqDate,tenantId);
				financeID = Integer.parseInt(resultMap1.get("TRANSACTION_NO").toString());

			}

		} catch (Exception e) {
			logger.error("checkFinancialcurYearNewSeqForPOCde Error" + e);
		}
		return financeID;
	}
	public static int checkNewRunningSeq( String tenantId, String tableName,
			JdbcTemplate jdbcTemplate) {
		int financeID = 1;
		try {
			String financialMstIdStr = "SELECT   COUNT(*) as COUNT FROM  "+tableName+" \r\n" 
					+ "WHERE  \r\n"
					+ "  TENANT_ID = '"+tenantId+"'";
			
			//Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialMstIdStr,,,);
			int financialMstCountId = jdbcTemplate.queryForObject(financialMstIdStr, Integer.class);
			if (financialMstCountId > 0) {

				String getQ = " select TRANSACTION_NO +1 as TRANSACTION_NO from "+tableName+" where  TENANT_ID=? ORDER BY TRANSACTION_NO DESC limit 1";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,tenantId);
				financeID = Integer.parseInt(resultMap1.get("TRANSACTION_NO").toString());

			}

		} catch (Exception e) {
			logger.error("checkNewRunningSeq Error" + e);
		}
		return financeID;
	}

	public static List<FinancialYearTransactionMstEntity> getfinacicalTransDtlByDocType(String pmId, String tenantId,
			JdbcTemplate jdbcTemplate, String tableName,String dateChk) {
		List<FinancialYearTransactionMstEntity> getDtl = new ArrayList<FinancialYearTransactionMstEntity>();
		try {
			String getDtlStr = "SELECT \r\n" + 
					"    *\r\n" + 
					"FROM\r\n" + 
					"    financial_year_transaction_mst fym\r\n" + 
					"WHERE\r\n" + 
					"    PM_ID = ? AND IS_ACTIVE = ?\r\n" + 
					"        AND TABLE_NAME = ? and TENANT_ID  = ? ";
			getDtl = jdbcTemplate.query(getDtlStr, new FinancialYearTransactionMstRowMapper(), pmId, 1,
					tableName,tenantId);

		} catch (Exception e) {
			logger.error("getfinacicalTransDtlByDocType Error" + e);
		}
		return getDtl;
	}

	public static String getPmHdrIdByprjCode(JdbcTemplate jdbcTemplate,String prjCode) {
		String pmHdrId = "";
		try {
			String qry = "select PM_HDR_ID from project_hdr where PROJECT_CODE='"+prjCode+"' ORDER BY PM_HDR_ID DESC LIMIT 1";
			pmHdrId = jdbcTemplate.queryForObject(qry, String.class);

		} catch (Exception ex) {
			logger.error("getPmHdrIdByprjCode Error" + ex);
		}
		return pmHdrId;
	}
	
	public static GeneralLastSeqEntity genCodeForTrans(String enqDate, String tenantId, String tableName, String pmId,
			JdbcTemplate jdbcTemplate,int finaceIdReq,int curYearFlag,String projectCode,int isSeq) {
		GeneralLastSeqEntity genLas = new GeneralLastSeqEntity();

		try {
			String financialMstId = FinanaceCodeGen.getFinancialMstId(enqDate, tenantId, jdbcTemplate);
			String financialMstYr = FinanaceCodeGen.getFinancialYr(enqDate, tenantId, jdbcTemplate);
			int seq = FinanaceCodeGen.checkFinancialNewSeq(financialMstId, tenantId, tableName, jdbcTemplate);
			if(curYearFlag==1) {
				String year =enqDate.split("-")[0];
			 if(tableName.equalsIgnoreCase("material_inward_hdr")) {
				seq = FinanaceCodeGen.checkFinancialcurYearNewSeq(year, tenantId, tableName, jdbcTemplate, "CREATED_ON");
			 }else if(tableName.equalsIgnoreCase("po_hdr")) {
				 String pmhdrId= FinanaceCodeGen.getPmHdrIdByprjCode(jdbcTemplate, projectCode);
					seq = FinanaceCodeGen.checkFinancialcurYearNewSeqForPOCde(year, tenantId, tableName, jdbcTemplate, "DATE",pmhdrId);	
				}
				else if(tableName.equalsIgnoreCase("DC_hdr")) {
					seq = FinanaceCodeGen.checkFinancialcurYearNewSeq(year, tenantId, tableName, jdbcTemplate, "DC_DATE");	
				}
				else if(tableName.equalsIgnoreCase("grn_hdr")) {
					seq = FinanaceCodeGen.checkFinancialcurYearNewSeq(year, tenantId, tableName, jdbcTemplate, "CREATED_ON");	
				}
			}
			
			List<FinancialYearTransactionMstEntity> finaceTransDoc = FinanaceCodeGen.getfinacicalTransDtlByDocType(pmId,
					tenantId, jdbcTemplate, tableName,enqDate);
			String prefix = "";
			String suffix = "";
			String enqCode = "";
			int startId = 1;
			Calendar calendar = Calendar.getInstance();
	        String currYear = Integer.toString(calendar.get(Calendar.YEAR));
			if (finaceTransDoc.size() > 0) {
				if (finaceTransDoc.get(0).getPrefixCode() != null) {
					prefix = finaceTransDoc.get(0).getPrefixCode();
				}
				if (finaceTransDoc.get(0).getSuffixCode() != null) {
					suffix = finaceTransDoc.get(0).getSuffixCode();
				}
				if (finaceTransDoc.get(0).getStartId() != null) {
					startId = Integer.parseInt(finaceTransDoc.get(0).getStartId());
				}
			}
			if (seq == 1) {

				seq = startId;
			}else {
				if(isSeq == 1) {
					seq = FinanaceCodeGen.checkNewRunningSeq( tenantId, tableName, jdbcTemplate);
				}
			}

			if(finaceIdReq ==0) {
				if (!prefix.equalsIgnoreCase("")) {
					enqCode = prefix + "/" + seq;
				} else {
					enqCode = seq+"";
				}
			}else {
				if (!prefix.equalsIgnoreCase("")) {
					if(curYearFlag==1 && projectCode!=null) {
						enqCode = prefix + "/" + currYear + "/" + projectCode + "/" + seq;
				    }else if(curYearFlag==1) {
						enqCode = prefix + "/" + currYear+ "/" + seq;
					}else if(curYearFlag==2) {
						enqCode = prefix +  "/" + seq;
					}else {
						enqCode = prefix + "/" + financialMstYr + "/" + seq;
					}
				} else {
					enqCode = financialMstYr + "/" + seq;
				}
			}
			
			
			

			if (!suffix.equalsIgnoreCase("")) {
				enqCode = enqCode.concat("/").concat(suffix);
			}

			genLas.setEnquiryCode(enqCode);
			genLas.setFinainceId(financialMstId);
			genLas.setSeq(seq);
		} catch (Exception ex) {
			logger.error("genCodeForTrans error " + ex.getMessage());
		}
		return genLas;
	}

}
