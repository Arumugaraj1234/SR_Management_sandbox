package com.vmfg.mis.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.IPMMisDAO;
import com.vmfg.mis.response.PMWorkLoadResponse;
import com.vmfg.mis.response.ReportProjectMilestoneResponse;
import com.vmfg.mis.response.ReportProjectTrackerResponse;
import com.vmfg.mis.rowmapper.PMMileStoneRowMapper;
import com.vmfg.mis.rowmapper.PMWidgetRowMapper;
import com.vmfg.mis.rowmapper.PMWorkLoadRowMapper;

@Transactional
@Repository
public class PMMisDAO implements IPMMisDAO {

	private static final Logger logger = LoggerFactory.getLogger(PMMisDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ReportProjectTrackerResponse> getAllPMDtlByDate(String fromDate, String endDate, String tenantId,
			String sbcCode) {
		List<ReportProjectTrackerResponse> list = null;
		String sbc = " = '" + sbcCode+"'";
		if (sbcCode.equalsIgnoreCase("getall")) {
			sbc = " like '%%'";
		}
		try {

			String getQ = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SALE_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, SALE_VALUE AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                    PM_CREATED_DATE BETWEEN ? AND ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE "+sbc+") tmp)\r\n" + 
					"    END AS SALE_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SALE_CONTRIBUTION) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, SALE_CONTRIBUTION AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                    PM_CREATED_DATE BETWEEN ? AND ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE  "+sbc+") tmp)\r\n" + 
					"    END AS SALE_CONTRIBUTION,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(PM_BUDGET_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(PM_BUDGET_VALUE)\r\n" + 
					"    END AS PM_BUDGET_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(PM_TARGET_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(PM_TARGET_VALUE)\r\n" + 
					"    END AS PM_TARGET_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SCM_PO_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(SCM_PO_VALUE)\r\n" + 
					"    END AS SCM_PO_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(MATERIAL_TRANSFER_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(MATERIAL_TRANSFER_VALUE)\r\n" + 
					"    END AS MATERIAL_TRANSFER_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(BALANCE_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, BALANCE_VALUE AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                    PM_CREATED_DATE BETWEEN ? AND ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE  "+sbc+") tmp)\r\n" + 
					"    END AS BALANCE_VALUE,CASE WHEN SUM(EMPLOYEE_COST) IS NULL THEN 0  \n"
					+ "					        ELSE (SELECT   \n"
					+ "					                SUM(ss)  \n"
					+ "					            FROM  \n"
					+ "					                (SELECT DISTINCT  \n"
					+ "					                    PM_HDR_ID, EMPLOYEE_COST AS ss  \n"
					+ "					                FROM  \n"
					+ "					                    report_project_tracker  \n"
					+ "					                WHERE  \n"
					+ "					                   PM_CREATED_DATE BETWEEN ? AND ?  \n"
					+ "					                        AND TENANT_ID = ?  \n"
					+ "					                        AND SBC_CODE "+sbc+" ) tmp)  \n"
					+ "					    END AS EMPLOYEE_COST ,\r\n" + 
					"    phdr.PROJECT_CODE\r\n" + 
					"FROM\r\n" + 
					"    report_project_tracker rt,\r\n" + 
					"    project_hdr phdr\r\n" + 
					"WHERE\r\n" + 
					"    rt.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"        AND rt.PM_CREATED_DATE BETWEEN ? AND ?\r\n" + 
					"        AND rt.TENANT_ID = ?\r\n" + 
					"        AND rt.SBC_CODE  "+sbc+"";
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, fromDate, endDate, tenantId, fromDate, endDate, tenantId, fromDate, endDate, tenantId, fromDate, endDate, tenantId, fromDate, endDate, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getAllPMDtlByDate DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ReportProjectTrackerResponse> getPMDtlByPmId(String tenantId, String pmHdrId, String sbcCode) {
		List<ReportProjectTrackerResponse> list = null;
		String sbc = "='" + sbcCode+"'";
		if (sbcCode.equalsIgnoreCase("getall")) {
			sbc = " like '%%'";
		}
		try {

			String getQ = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SALE_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, SALE_VALUE AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                    PM_HDR_ID = ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE "+sbc+") tmp)\r\n" + 
					"    END AS SALE_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SALE_CONTRIBUTION) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, SALE_CONTRIBUTION AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                   PM_HDR_ID = ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE  "+sbc+") tmp)\r\n" + 
					"    END AS SALE_CONTRIBUTION,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(PM_BUDGET_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(PM_BUDGET_VALUE)\r\n" + 
					"    END AS PM_BUDGET_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(PM_TARGET_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(PM_TARGET_VALUE)\r\n" + 
					"    END AS PM_TARGET_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(SCM_PO_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(SCM_PO_VALUE)\r\n" + 
					"    END AS SCM_PO_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(MATERIAL_TRANSFER_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE SUM(MATERIAL_TRANSFER_VALUE)\r\n" + 
					"    END AS MATERIAL_TRANSFER_VALUE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN SUM(BALANCE_VALUE) IS NULL THEN 0\r\n" + 
					"        ELSE (SELECT \r\n" + 
					"                SUM(ss)\r\n" + 
					"            FROM\r\n" + 
					"                (SELECT DISTINCT\r\n" + 
					"                    PM_HDR_ID, BALANCE_VALUE AS ss\r\n" + 
					"                FROM\r\n" + 
					"                    report_project_tracker\r\n" + 
					"                WHERE\r\n" + 
					"                    PM_HDR_ID = ?\r\n" + 
					"                        AND TENANT_ID = ?\r\n" + 
					"                        AND SBC_CODE  "+sbc+") tmp)\r\n" + 
					"    END AS BALANCE_VALUE, CASE WHEN SUM(EMPLOYEE_COST) IS NULL THEN 0  \n"
					+ "					        ELSE (SELECT   \n"
					+ "					                SUM(ss)  \n"
					+ "					            FROM  \n"
					+ "					                (SELECT DISTINCT  \n"
					+ "					                    PM_HDR_ID, EMPLOYEE_COST AS ss  \n"
					+ "					                FROM  \n"
					+ "					                    report_project_tracker  \n"
					+ "					                WHERE  \n"
					+ "					                   PM_HDR_ID = ?  \n"
					+ "					                        AND TENANT_ID = ?  \n"
					+ "					                        AND SBC_CODE "+sbc+" ) tmp)  \n"
					+ "					    END AS EMPLOYEE_COST,\r\n" + 
					"    phdr.PROJECT_CODE\r\n" + 
					"FROM\r\n" + 
					"    report_project_tracker rt,\r\n" + 
					"    project_hdr phdr\r\n" + 
					"WHERE\r\n" + 
					"    rt.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"        AND rt.PM_HDR_ID = ?\r\n" + 
					"        AND rt.TENANT_ID = ?\r\n" + 
					"        AND rt.SBC_CODE "+sbc+" ;";
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, pmHdrId, tenantId, pmHdrId, tenantId, pmHdrId, tenantId, pmHdrId, tenantId, pmHdrId, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ReportProjectTrackerResponse> getPMBySBCTypeAll(String fromDate, String endDate, String tenantId,String pmHdrId) {
		List<ReportProjectTrackerResponse> list = null;
		try {
			String pro = " and phdr.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and phdr.PM_HDR_ID = '"+pmHdrId+"'";
			}

			String getQ = "select case when sum(SALE_VALUE) is null then 0 else SALE_VALUE end as SALE_VALUE,\r\n"
					+ "case when sum(SALE_CONTRIBUTION) is null then 0 else SALE_CONTRIBUTION end as SALE_CONTRIBUTION,\r\n"
					+ "case when sum(PM_BUDGET_VALUE) is null then 0 else sum(PM_BUDGET_VALUE) end as PM_BUDGET_VALUE,\r\n"
					+ "case when sum(PM_TARGET_VALUE) is null then 0 else sum(PM_TARGET_VALUE) end as PM_TARGET_VALUE,\r\n"
					+ "case when sum(SCM_PO_VALUE) is null then 0 else  sum(SCM_PO_VALUE) end as SCM_PO_VALUE,\r\n"
					+ "case when sum(MATERIAL_TRANSFER_VALUE) is null then 0 else sum(MATERIAL_TRANSFER_VALUE) end as MATERIAL_TRANSFER_VALUE,\r\n"
					+ "case when sum(BALANCE_VALUE) is null then 0 else BALANCE_VALUE end as BALANCE_VALUE ,phdr.PROJECT_CODE,case when sum(EMPLOYEE_COST) is null then 0 else EMPLOYEE_COST end as EMPLOYEE_COST  \r\n"
					+ "from report_project_tracker rt , project_hdr phdr where rt.PM_HDR_ID = phdr.PM_HDR_ID and (rt.PM_CREATED_DATE between ? and ? or rt.IS_COMPLETED=0)  and rt.TENANT_ID = ? "+pro+ " group by rt.PM_HDR_ID ORDER BY phdr.PROJECT_CODE";
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, fromDate, endDate, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ReportProjectTrackerResponse> getPMBySBCType(String fromDate, String endDate, String tenantId,
			String sbcCode,String pmHdrId) {
		List<ReportProjectTrackerResponse> list = null;
		try {

			String pro = " and phdr.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and phdr.PM_HDR_ID = '"+pmHdrId+"'";
			}
			String getQ = "SELECT * FROM report_project_tracker rt , project_hdr phdr where phdr.PM_HDR_ID =rt.PM_HDR_ID and (PM_CREATED_DATE between ? and ? or rt.IS_COMPLETED=0)  and rt.TENANT_ID = ? and rt.SBC_CODE = ? "+pro+" ORDER BY phdr.PROJECT_CODE ";
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, fromDate, endDate, tenantId, sbcCode);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ReportProjectMilestoneResponse> getPMMilestoneByMonthYr(String month, String yr, String tenantId,String pmHdrId) {
		List<ReportProjectMilestoneResponse> list = null;
		try {
			String pro = " and ph.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and ph.PM_HDR_ID = '"+pmHdrId+"'";
			}
			String getQ = "SELECT \r\n" + 
					"    ph.PROJECT_CODE,\r\n" + 
					"    pt.MILESTONE_NAME,\r\n" + 
					"    dt.DEPARTMENT_NAME,\r\n" + 
					"    pt.PLANNED_START_DATE,\r\n" + 
					"    pt.PLANNED_END_DATE,\r\n" + 
					"    pt.PM_HDR_ID,\r\n" + 
					"    pt.RESPONSIBLE_DEPT_CODE\r\n" + 
					"FROM\r\n" + 
					"    project_timeline pt,\r\n" + 
					"    project_hdr ph,\r\n" + 
					"    department dt\r\n" + 
					"WHERE\r\n" + 
					"    pt.PM_HDR_ID = ph.PM_HDR_ID\r\n" + 
					"        AND dt.DEPARTMENT_CODE = pt.RESPONSIBLE_DEPT_CODE\r\n" + 
					"        AND MONTH(pt.PLANNED_END_DATE) = ? \r\n" + 
					"        AND YEAR(pt.PLANNED_END_DATE) = ? \r\n" + 
					"        AND ph.TENANT_ID = ? "+pro+" order by pt.PLANNED_END_DATE asc";
			RowMapper<ReportProjectMilestoneResponse> rowmapper = new PMMileStoneRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, month, yr, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getPMMilestoneByMonthYr DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public int MileCountCheck(String department, String pmHdrId, String tenantId) {
		int ret = 0;
		try {

			String getQ = "SELECT \r\n" + "    count(*) as COUNT\r\n" + "FROM\r\n" + "    task_entry_hdr hdr,\r\n"
					+ "    task_entry_dtl dtl\r\n" + "WHERE\r\n" + "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n"
					+ "        AND hdr.DEPARTMENT_CODE =  ? AND hdr.PM_HDR_ID = ? \r\n"
					+ " and hdr.TENANT_ID= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ, department, pmHdrId, tenantId);
			ret = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.info("Error with getPMMilestoneByMonthYr DAO method" + ex.getMessage());
		}
		return ret;
	}

	@Override
	public String getMileStoneEndDate(String department, String pmHdrId, String tenantId) {
		String ret = "";
		try {

			String getQ = "SELECT \r\n"
					+ "    case when IS_COMPLETED > 0 then COMPLETED_DATE else '-' end as COMP_DATE\r\n"
					+ "FROM\r\n" + "    task_entry_hdr thdr , task_entry_dtl tdtl \r\n" + "WHERE\r\n"
					+ "thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + "    and PM_HDR_ID = ? \r\n"  
					+ " and thdr.DEPARTMENT_CODE= ? and thdr.TENANT_ID = ? \r\n"  
					+ " order by IS_COMPLETED limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ, pmHdrId,department,tenantId);
			ret = resultMap.get("COMP_DATE").toString();

		} catch (Exception ex) {
			logger.info("Error with getPMMilestoneByMonthYr DAO method" + ex.getMessage());
		}
		return ret;
	}

	@Override
	public List<ReportProjectTrackerResponse> getPMBySBCTypeAllwithoutDate(String tenantId,String pmHdrId) {
		List<ReportProjectTrackerResponse> list = null;
		try {
			String pro = " and phdr.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and phdr.PM_HDR_ID = '"+pmHdrId+"'";
			}
			String getQ = "select case when sum(SALE_VALUE) is null then 0 else SALE_VALUE end as SALE_VALUE,\r\n"
					+ "case when sum(SALE_CONTRIBUTION) is null then 0 else SALE_CONTRIBUTION end as SALE_CONTRIBUTION,\r\n"
					+ "case when sum(PM_BUDGET_VALUE) is null then 0 else sum(PM_BUDGET_VALUE) end as PM_BUDGET_VALUE,\r\n"
					+ "case when sum(PM_TARGET_VALUE) is null then 0 else sum(PM_TARGET_VALUE) end as PM_TARGET_VALUE,\r\n"
					+ "case when sum(SCM_PO_VALUE) is null then 0 else sum(SCM_PO_VALUE) end as SCM_PO_VALUE,\r\n"
					+ "case when sum(MATERIAL_TRANSFER_VALUE) is null then 0 else sum(MATERIAL_TRANSFER_VALUE) end as MATERIAL_TRANSFER_VALUE,\r\n"
					+ "case when sum(BALANCE_VALUE) is null then 0 else BALANCE_VALUE end as BALANCE_VALUE ,phdr.PROJECT_CODE ,case when sum(EMPLOYEE_COST) is null then 0 else EMPLOYEE_COST end as EMPLOYEE_COST\r\n"
					+ "from report_project_tracker rt , project_hdr phdr where rt.PM_HDR_ID = phdr.PM_HDR_ID and rt.TENANT_ID = ? "+pro+" group by rt.PM_HDR_ID ORDER BY phdr.PROJECT_CODE";
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ReportProjectTrackerResponse> getPMBySBCTypewithoutDate(String tenantId, String sbcCode,String pmHdrId) {
		List<ReportProjectTrackerResponse> list = null;
		try {
			String pro = " and phdr.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and phdr.PM_HDR_ID = '"+pmHdrId+"'";
			}
			String getQ = "SELECT * FROM report_project_tracker rt , project_hdr phdr where phdr.PM_HDR_ID =rt.PM_HDR_ID  and rt.TENANT_ID = ? and rt.SBC_CODE = ?" + pro;
			RowMapper<ReportProjectTrackerResponse> rowmapper = new PMWidgetRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, tenantId, sbcCode);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

	@Override
	public List<PMWorkLoadResponse> getPMWorkLoad(String tenantId,String pmHdrId) {
		List<PMWorkLoadResponse> list = null;
		try {
			String pro = " and phdr.PM_HDR_ID like '%%' ";
			if(!pmHdrId.equalsIgnoreCase("getall")) {
				pro = " and phdr.PM_HDR_ID = '"+pmHdrId+"'";
			}
			String getQ = "SELECT \r\n" + 
					"    pat.ASSIGNED_EMP_ID ,em.EMPLOYEE_CODE , em.EMPLOYEE_FIRSTNAME, count(*) as projCount \r\n" + 
					"FROM\r\n" + 
					"    project_hdr phdr,\r\n" + 
					"    process_assigned_team pat,\r\n" + 
					"    employee_mst em\r\n" + 
					"WHERE\r\n" + 
					"    phdr.PM_HDR_ID = pat.MASTER_ID\r\n" + 
					"    and em.EMPLOYEE_ID = pat.ASSIGNED_EMP_ID\r\n" + 
					"        AND PM_ID = 3 and phdr.TENANT_ID= ? and phdr.IS_COMPLETED =0 "+pro+ " group by EMPLOYEE_ID";
			RowMapper<PMWorkLoadResponse> rowmapper = new PMWorkLoadRowMapper();
			list = this.jdbcTemplate.query(getQ, rowmapper, tenantId);

		} catch (Exception ex) {
			logger.info("Error with getPMDtlByPmId DAO method" + ex.getMessage());
		}
		return list;
	}

}
