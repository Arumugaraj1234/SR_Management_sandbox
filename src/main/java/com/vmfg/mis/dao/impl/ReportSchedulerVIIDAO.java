package com.vmfg.mis.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.vmfg.mis.dao.interfaces.iReportSchedulerVIIDAO;
import com.vmfg.mis.rowmapper.IndentHdrEntityRowMapper;
import com.vmfg.mis.rowmapper.ProjectHdrEntityRowMapper;
import com.vmfg.mis.rowmapper.ReInspectionVendorMasterRowMapper;
import com.vmfg.project.entity.IndentHdrEntity;
import com.vmfg.project.entity.ProjectHdrEntity;
import com.vmfg.project.entity.ReInspectionVendorMasterEntity;

@Transactional
@Repository
public class ReportSchedulerVIIDAO implements iReportSchedulerVIIDAO {
	private static final Logger logger = LoggerFactory.getLogger(ReportSchedulerVIIDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ProjectHdrEntity> getProjects(String tenantId) {
		List<ProjectHdrEntity> list = new ArrayList<ProjectHdrEntity>();
		try {
			String qry = "SELECT \r\n" + "    PM_HDR_ID,\r\n" + "    PROJECT_NAME,\r\n" + "    ENQUIRY_ID,\r\n"
					+ "    CREATED_DATE,\r\n" + "    IS_COMPLETED,\r\n" + "    COMPLETED_DATETIME,TENANT_ID,\r\n"
					+ "    LAST_UPDATED_DATETIME\r\n" + "FROM\r\n" + "    project_hdr where TENANT_ID = ? ";
			list = this.jdbcTemplate.query(qry, new ProjectHdrEntityRowMapper(),tenantId);
		} catch (Exception ex) {
			logger.error("getProjects Method Exception " + ex);
		}
		return list;
	}

	@Override
	public List<IndentHdrEntity> getIndentsByProjectId(String pmHdrId) {
		List<IndentHdrEntity> list = new ArrayList<IndentHdrEntity>();
		try {
			String qry = "SELECT \r\n" + "    SBC_CODE,\r\n" + "    SUM(BUDGET_VALUE) as BUDGET_VALUE,\r\n"
					+ "    SUM(SCM_BUDGET_ALLOCATED) as SCM_BUDGET_ALLOCATED,\r\n"
					+ "    SUM(TARGET_VALUE) as TARGET_VALUE\r\n" + "FROM\r\n" + "    indent_hdr\r\n" + "WHERE\r\n"
					+ "    PROJECT_ID = '" + pmHdrId + "' and SBC_CODE is not null\r\n" + "GROUP BY SBC_CODE";
			list = this.jdbcTemplate.query(qry, new IndentHdrEntityRowMapper());
		} catch (Exception ex) {
			logger.error("getIndentsByProjectId Method Exception " + ex);
		}
		return list;
	}

	@Override
	public int reportProjectTrackerCountCheck(String pmHdrId, String sbcCode) {
		int count = 0;
		try {
			String qry = "select case when count(RPT_ID)>0 then RPT_ID else 0 end RPT_ID from report_project_tracker where PM_HDR_ID= ? and SBC_CODE= ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId, sbcCode);
			count = Integer.parseInt(resultMap.get("RPT_ID").toString());
		} catch (Exception ex) {
			logger.error("reportProjectTrackerCountCheck Method Exception " + ex);
		}
		return count;
	}

	@Override
	public int reportProjectTrackerInsert(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String completedDateTime,
			String lastUpdatedDateTime, String tenantId) {
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO report_project_tracker\r\n" + "(`PM_HDR_ID`,\r\n" + "`PM_CREATED_DATE`,\r\n"
					+ "`SBC_CODE`,\r\n" + "`PM_BUDGET_VALUE`,\r\n" + "`PM_TARGET_VALUE`,\r\n" + "`SCM_PO_VALUE`,\r\n"
					+ "`IS_COMPLETED`,\r\n" + "`COMPLETED_DATETIME`,\r\n" + "`LAST_UPDATED_DATETIME`,\r\n"
					+ "`TENANT_ID`)\r\n" + "VALUES\r\n" + "(?,?,?,?,?,?,?,?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, pmHdrId);
					ps.setString(2, createdDate);
					ps.setString(3, sbcCode);
					ps.setString(4, budgetValue);
					ps.setString(5, targetValue);
					ps.setString(6, budgetAllocated);
					ps.setString(7, isCompleted);
					ps.setString(8, completedDateTime);
					ps.setString(9, lastUpdatedDateTime);
					ps.setString(10, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.info("reportProjectTrackerInsert Method Exception " + ex);
		}
		return insertRes;
	}

	@Override
	public int reportProjectTrackerUpdate(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String completedDateTime,
			String lastUpdatedDateTime, String tenantId, int hdrId) {
		int updateRes = 0;
		BigDecimal budAll = budgetAllocated != null ? new BigDecimal(budgetAllocated) : BigDecimal.ZERO;
		try {
			String qry = "UPDATE report_project_tracker\r\n" + "SET\r\n" + "`PM_HDR_ID` = ?,\r\n"
					+ "`PM_CREATED_DATE` = ?,\r\n" + "`SBC_CODE` = ?,\r\n" + "`PM_BUDGET_VALUE` = ?,\r\n"
					+ "`PM_TARGET_VALUE` = ?,\r\n" + "`SCM_PO_VALUE` = ?,\r\n" + "`IS_COMPLETED` = ?,\r\n"
					+ "`COMPLETED_DATETIME` = ?,\r\n" + "`LAST_UPDATED_DATETIME` = ?,\r\n" + "`TENANT_ID` = ?\r\n"
					+ "WHERE `RPT_ID` = ?";
			updateRes = this.jdbcTemplate.update(qry, pmHdrId, createdDate, sbcCode, budgetValue, targetValue, budAll,
					isCompleted, completedDateTime, lastUpdatedDateTime, tenantId, hdrId);

		} catch (Exception ex) {
			logger.error("reportProjectTrackerUpdate Method Exception " + ex);
		}
		return updateRes;
	}

	@Override
	public BigDecimal reportGetSaleContribution(String enqId) {
		BigDecimal res = BigDecimal.ZERO;
		try {
			String qry = "select case when count(*) >0 then (SALE_PERCENT+CR_SALE_PERCENT) else 0 end sale from sales_budget_sheet_hdr where MASTER_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, enqId);
			res = new BigDecimal(resultMap.get("sale").toString());
			res.setScale(2, RoundingMode.HALF_UP);
		} catch (Exception ex) {
			logger.error("reportGetSaleContribution Method Exception " + ex);
		}
		return res;
	}

	@Override
	public int reportUpdateSaleValue(String pmHdrId, BigDecimal saleContribution,BigDecimal saleValue) {
		int res = 0;
		try {
			String qry = "update report_project_tracker set SALE_VALUE=?,SALE_CONTRIBUTION=? where PM_HDR_ID=?";
			res = this.jdbcTemplate.update(qry, saleValue, saleContribution, pmHdrId);
		} catch (Exception ex) {
			logger.error("reportUpdateSaleValue Method Exception " + ex);
		}
		return res;
	}

	@Override
	public BigDecimal getMatrialCost(String pmHdrId, String sbcCode) {
		BigDecimal res = BigDecimal.ZERO;
		try {

			String qry = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN count(OVER_ALL_COST) > 0 THEN SUM(OVER_ALL_COST)\r\n" + "        ELSE 0.00\r\n"
					+ "    END AS overAllCost\r\n" + "FROM\r\n" + "    indent_grp_hdr hdr\r\n"
					+ "        INNER JOIN\r\n" + "    indent_grp_dtl dtl ON hdr.IG_HDR_ID = dtl.IG_HDR_ID\r\n"
					+ "        INNER JOIN\r\n" + "    indent_dtl indtl ON indtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n"
					+ "        INNER JOIN\r\n" + "    indent_hdr indhdr ON indhdr.INDENT_ID = indtl.INDENT_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    inventroy_material_transfer material ON material.TO_PM_HDR_ID = indhdr.PROJECT_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    product_mst pmaster ON pmaster.PRODUCT_ID = material.PRODUCT_ID\r\n" + "WHERE\r\n"
					+ "    hdr.IS_INVENTORY = '1'\r\n" + "        AND indhdr.PROJECT_ID = ?\r\n"
					+ "        AND indhdr.SBC_CODE = ?\r\n" + "GROUP BY indhdr.SBC_CODE";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId, sbcCode);
			res = new BigDecimal(resultMap.get("overAllCost").toString());
		} catch (Exception ex) {
			logger.error("getMatrialCost Method Exception " + ex);
		}
		return res;
	}

	@Override
	public int updateMatrialCost(String pmHdrId, String sbcCode, BigDecimal materialCost) {
		int res = 0;
		try {
			String qry = "update report_project_tracker set MATERIAL_TRANSFER_VALUE=? , BALANCE_VALUE = BALANCE_VALUE-? where SBC_CODE=? and PM_HDR_ID=?";
			res = this.jdbcTemplate.update(qry, materialCost, materialCost, sbcCode, pmHdrId);
		} catch (Exception ex) {
			logger.error("updateMatrialCost Method Exception " + ex);
		}
		return res;
	}

	@Override
	public int updateHumanCost(String pmHdrId, String sbcCode, BigDecimal humanCost) {
		int res = 0;
		try {
			String qry = "update report_project_tracker set EMPLOYEE_COST=? , BALANCE_VALUE = BALANCE_VALUE-? where SBC_CODE=? and PM_HDR_ID=?";
			res = this.jdbcTemplate.update(qry, humanCost, humanCost, sbcCode, pmHdrId);
		} catch (Exception ex) {
			logger.error("updateMatrialCost Method Exception " + ex);
		}
		return res;
	}

	@Override
	public int reportProjectTrackerInsert1(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String lastUpdatedDateTime,
			String tenantId) {

		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO report_project_tracker\r\n" + "(`PM_HDR_ID`,\r\n" + "`PM_CREATED_DATE`,\r\n"
					+ "`SBC_CODE`,\r\n" + "`PM_BUDGET_VALUE`,\r\n" + "`PM_TARGET_VALUE`,\r\n" + "`SCM_PO_VALUE`,\r\n"
					+ "`IS_COMPLETED`,\r\n" + "`LAST_UPDATED_DATETIME`,\r\n" + "`TENANT_ID`)\r\n" + "VALUES\r\n"
					+ "(?,?,?,?,?,?,?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, pmHdrId);
					ps.setString(2, createdDate);
					ps.setString(3, sbcCode);
					ps.setString(4, budgetValue);
					ps.setString(5, targetValue);
					ps.setString(6, budgetAllocated);
					ps.setString(7, isCompleted);
					ps.setString(8, lastUpdatedDateTime);
					ps.setString(9, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.info("reportProjectTrackerInsert Method Exception " + ex);
		}
		return insertRes;
	}

	@Override
	public int reportProjectTrackerUpdate1(String pmHdrId, String createdDate, String sbcCode, String budgetValue,
			String budgetAllocated, String targetValue, String isCompleted, String lastUpdatedDateTime, String tenantId,
			int hdrId) {
		int updateRes = 0;
		BigDecimal budAll = budgetAllocated != null ? new BigDecimal(budgetAllocated) : BigDecimal.ZERO;
		try {
			String qry = "UPDATE report_project_tracker\r\n" + "SET\r\n" + "`PM_HDR_ID` = ?,\r\n"
					+ "`PM_CREATED_DATE` = ?,\r\n" + "`SBC_CODE` = ?,\r\n" + "`PM_BUDGET_VALUE` = ?,\r\n"
					+ "`PM_TARGET_VALUE` = ?,\r\n" + "`SCM_PO_VALUE` = ?,\r\n" + "`IS_COMPLETED` = ?,\r\n"
					+ "`LAST_UPDATED_DATETIME` = ?,\r\n" + "`TENANT_ID` = ?\r\n" + "WHERE `RPT_ID` = ?";
			updateRes = this.jdbcTemplate.update(qry, pmHdrId, createdDate, sbcCode, budgetValue, targetValue, budAll,
					isCompleted, lastUpdatedDateTime, tenantId, hdrId);

		} catch (Exception ex) {
			logger.error("reportProjectTrackerUpdate Method Exception " + ex);
		}
		return updateRes;
	}

	@Override
	public BigDecimal getMatrialCostCount(String pmHdrId, String sbcCode) {
		BigDecimal res = BigDecimal.ZERO;
		try {

//			String qry ="SELECT \r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN count(OVER_ALL_COST) > 0 THEN SUM(OVER_ALL_COST)\r\n" + 
//					"        ELSE 0.00\r\n" + 
//					"    END AS overAllCost\r\n" + 
//					"FROM\r\n" + 
//					"    indent_grp_hdr hdr\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_grp_dtl dtl ON hdr.IG_HDR_ID = dtl.IG_HDR_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_dtl indtl ON indtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_hdr indhdr ON indhdr.INDENT_ID = indtl.INDENT_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    inventroy_material_transfer material ON material.TO_PM_HDR_ID = indhdr.PROJECT_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    product_mst pmaster ON pmaster.PRODUCT_ID = material.PRODUCT_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    hdr.IS_INVENTORY = '1'\r\n" + 
//					"        AND indhdr.PROJECT_ID = '"+pmHdrId+"'\r\n" + 
//					"        AND indhdr.SBC_CODE = '"+sbcCode+"'\r\n" + 
//					"";

			String qry = "SELECT \r\n" + "    CASE\r\n" + "        WHEN SUM(OVER_ALL_COST) > 0 THEN OVER_ALL_COST\r\n"
					+ "        ELSE 0\r\n" + "    END AS OVER_ALL_COST\r\n" + "FROM\r\n"
					+ "    inventroy_material_transfer\r\n" + "WHERE\r\n" + "    PRODUCT_ID IN (SELECT \r\n"
					+ "            pm.PRODUCT_ID\r\n" + "        FROM\r\n" + "            indent_hdr hdr\r\n"
					+ "                INNER JOIN\r\n"
					+ "            indent_dtl dtl ON hdr.indent_id = dtl.INDENT_ID\r\n"
					+ "                INNER JOIN\r\n"
					+ "            product_mst pm ON pm.PRODUCT_DESCRIPTION = dtl.DESCRIPTION\r\n"
					+ "                AND hdr.PROJECT_ID = pm.PM_HDR_ID\r\n" + "                INNER JOIN\r\n"
					+ "            indent_grp_dtl igd ON igd.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n"
					+ "                INNER JOIN\r\n"
					+ "            indent_grp_hdr igh ON igh.IG_HDR_ID = igd.IG_HDR_ID\r\n" + "        WHERE\r\n"
					+ "            hdr.project_Id = ?\r\n" + "                AND igh.IS_INVENTORY = 1)";

			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId);
			res = new BigDecimal(resultMap.get("OVER_ALL_COST").toString());
		} catch (Exception ex) {
			logger.error("getMatrialCost Method Exception " + ex);
		}
		return res;
	}

	@Override
	public BigDecimal getHumanCost(String pmHdrId) {
		BigDecimal bg = BigDecimal.ZERO;
		try {
			String getQ = "SELECT \r\n"
					+ "    case when sum(tdtl.TIMESHEET_COST) is null then 0 else sum(tdtl.TIMESHEET_COST) end as MAN_COST\r\n"
					+ "FROM\r\n" + "    timesheet_hdr thdr,\r\n" + "    timesheet_dtl tdtl\r\n" + "WHERE\r\n"
					+ "    thdr.T_HDR_ID = tdtl.T_HDR_ID\r\n" + "        AND thdr.PM_HDR_ID = ?";
			Object[] args = { pmHdrId };
			bg = this.jdbcTemplate.queryForObject(getQ, BigDecimal.class, args);
		} catch (Exception ex) {
			logger.error("getMatrialCost Method Exception " + ex);
		}
		return bg;
	}

	@Override
	public BigDecimal reportGetSaleValue(String enquiryId) {
		BigDecimal res = BigDecimal.ZERO;
		try {
			String qry = "select case when count(*) >0 then (CR_COST+TOTAL_BUDGET_COST) else 0 end sale from sales_budget_sheet_hdr where MASTER_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, enquiryId);
			res = new BigDecimal(resultMap.get("sale").toString());
			res.setScale(2, RoundingMode.HALF_UP);
		} catch (Exception ex) {
			logger.error("reportGetSaleValue Method Exception " + ex);
		}
		return res;
	}

	@Override
	public List<ReInspectionVendorMasterEntity> getVendorList() {
		List<ReInspectionVendorMasterEntity> vendorList = new ArrayList<ReInspectionVendorMasterEntity>();
		try {
			String qry = "SELECT \r\n" + 
					"    CONCAT(VENDOR_UNIQUE_CODE,\r\n" + 
					"        ' - ',\r\n" + 
					"        VENDOR_NAME,\r\n" + 
					"        ' due for Inspection On ',\r\n" + 
					"        DATE_FORMAT(REINSPECTION_DATE, '%d-%M-%Y')) AS message,TENANT_ID\r\n" + 
					"FROM\r\n" + 
					"    vendor_mst\r\n" + 
					"WHERE\r\n" + 
					"    REINSPECTION_DATE = DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY)";
			vendorList = this.jdbcTemplate.query(qry, new ReInspectionVendorMasterRowMapper());
		}catch(Exception ex) {
			logger.error("getVendorList Method Exception " + ex);
		}
		return vendorList;
	}

	@Override
	public String getNextApprovingSequence(String tenantId,String pmId) {
		String res="";
		try {
			String qry="select PRIMARY_POC from project_wbs_initiation_mst where TENANT_ID=? and PM_ID= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, tenantId,pmId);
			res = resultMap.get("PRIMARY_POC").toString();
			
		}catch(Exception ex) {
			logger.error("getNextApprovingSequence Method Exception " + ex);
		}
		return res;
	}


}
