package com.vmfg.task.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.rowmapper.DocLifeCycleMstTableRowMapper;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.task.dao.interfaces.IDesignTaskDAO;
import com.vmfg.task.entity.GetAllTaskCategorytcEntity;
import com.vmfg.task.entity.GetAllTaskTypeEntity;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.task.entity.GetTaskHdrAndDtlIdEntity;
import com.vmfg.task.entity.GetTaskHdrByEmpIdEntity;
import com.vmfg.task.entity.TaskCategoryMstEntity;
import com.vmfg.task.entity.TaskEntryDtlEntity;
import com.vmfg.task.entity.TaskEntryHdrEntity;
import com.vmfg.task.entity.TaskEntryRemarksEntity;
import com.vmfg.task.entity.TaskTemplateDtlEntity;
import com.vmfg.task.entity.TaskTypeMstEntity;
import com.vmfg.task.entity.TemplateHdrNameEntity;
import com.vmfg.task.request.GetTaskCategoryByPmHdrIdRequest;
import com.vmfg.task.request.GetTaskHdrByEmpIdReq;
import com.vmfg.task.request.GetTaskPercentFlagRequest;
import com.vmfg.task.request.GetTaskRecordedRequest;
import com.vmfg.task.request.GetTaskRemarksByIDRequest;
import com.vmfg.task.request.TaskReassignRequest;
import com.vmfg.task.response.GetRemarksByIdResponse;
import com.vmfg.task.response.GetTaskTemplateHdrResponse;
import com.vmfg.task.rowmapper.GetAllTaskCategorytcRowMapper;
import com.vmfg.task.rowmapper.GetAllTaskTypeRowMapper;
import com.vmfg.task.rowmapper.GetTaskEntryDtlRowMapper;
import com.vmfg.task.rowmapper.GetTaskEntryRemarksRowMapper;
import com.vmfg.task.rowmapper.GetTaskHdrAndDtlIdRowMapper;
import com.vmfg.task.rowmapper.GetTaskHdrByEmpIdRowMapper;
import com.vmfg.task.rowmapper.GetTaskTemplateHdrRowMapper;
import com.vmfg.task.rowmapper.TaskCategoryMstRowMapper;
import com.vmfg.task.rowmapper.TaskEntryDtlRowMapper;
import com.vmfg.task.rowmapper.TaskEntryHdrRowMapper;
import com.vmfg.task.rowmapper.TaskRemarksRowMapper;
import com.vmfg.task.rowmapper.TaskTemplateDtlRowMapper;
import com.vmfg.task.rowmapper.TaskTypeMstRowMapper;
import com.vmfg.task.rowmapper.TemplateHdrNameRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class DesignTaskDAO implements IDesignTaskDAO {
	private static final Logger logger = LoggerFactory.getLogger(DesignTaskDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	@Autowired
	StageManagementDAO stageManagementDAO;

	@Override
	public String getEmpDesinationCode(String empId, String tenantId) {
		String getEmpDesinationCode = "";
		try {
			String getEmpDesinationCodeStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN DESIGNATION_CODE\r\n" + "        ELSE ''\r\n"
					+ "    END AS DESIGNATION_CODE\r\n" + "FROM\r\n" + "    employee_mst\r\n" + "WHERE\r\n"
					+ "    EMPLOYEE_ID = ? AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getEmpDesinationCodeStr,empId,tenantId);
			getEmpDesinationCode = resultMap.get("DESIGNATION_CODE").toString();
		} catch (Exception ex) {
			logger.error("getEmpDesinationCode Error" + ex);
		}
		return getEmpDesinationCode;
	}

	@Override
	public String getEmpDepartmentCode(String empId, String tenantId) {
		String getEmpDesinationCode = "";
		try {
			String getEmpDesinationCodeStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN DEPARTMENT_CODE\r\n" + "        ELSE ''\r\n"
					+ "    END AS DEPARTMENT_CODE\r\n" + "FROM\r\n" + "    employee_mst\r\n" + "WHERE\r\n"
					+ "    EMPLOYEE_ID = ?\r\n" + "        AND TENANT_ID = ?";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getEmpDesinationCodeStr,empId,tenantId);
			getEmpDesinationCode = resultMap.get("DEPARTMENT_CODE").toString();
		} catch (Exception ex) {
			logger.error("getEmpDepartmentCode Error" + ex);
		}
		return getEmpDesinationCode;
	}

	@Override
	public List<GetTaskTemplateHdrResponse> getTaskNameByDept(String deptCode, String tenantId, String isActive) {
		List<GetTaskTemplateHdrResponse> list = new ArrayList<GetTaskTemplateHdrResponse>();
		try {
			String taskListStr = "SELECT \r\n" + "    TT_NAME AS TT_NAME, TT_HDR_ID AS TT_HDR_ID\r\n" + "FROM\r\n"
					+ "    task_template_hdr\r\n" + "WHERE\r\n" + "    TT_DEPARTMENT_CODE = ? \r\n"
					+ "        AND TENANT_ID =? and IS_ACTIVE = ? ";
			list = this.jdbcTemplate.query(taskListStr, new GetTaskTemplateHdrRowMapper(), deptCode, tenantId,
					isActive);
		} catch (Exception ex) {
			logger.error("getTaskNameByDept Error" + ex);
		}
		return list;
	}

	@Override
	public List<TaskTypeMstEntity> getTaskTypeDtl(String deptCode,String isActive, String tenantId) {
		List<TaskTypeMstEntity> list = new ArrayList<TaskTypeMstEntity>();
		try {
			String taskListStr = "SELECT \r\n" + "    *\r\n" + "FROM\r\n" + "    task_type_mst\r\n" + "WHERE\r\n"
					+ "    DEPT_CODE = ? AND IS_ACTIVE = ? AND TENANT_ID = ? ";
			list = this.jdbcTemplate.query(taskListStr, new TaskTypeMstRowMapper(), deptCode,isActive, tenantId);
		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<TaskCategoryMstEntity> getCategoryMst(String tenantId, String isActive, String typeCode) {
		List<TaskCategoryMstEntity> list = new ArrayList<TaskCategoryMstEntity>();
		try {
			String taskListStr = "SELECT \r\n" + "    *\r\n" + "FROM\r\n" + "    task_category_mst\r\n" + "WHERE\r\n"
					+ "    TENANT_ID = ? AND IS_ACTIVE = ? AND TT_CODE = ? ";
			list = this.jdbcTemplate.query(taskListStr, new TaskCategoryMstRowMapper(), tenantId, isActive, typeCode);
		} catch (Exception ex) {

		}
		return list;
	}
	@Override
	public List<GetAllTaskCategorytcEntity> getCategoryMstAll(String tenantId, String typeCode) {
		List<GetAllTaskCategorytcEntity> list = new ArrayList<GetAllTaskCategorytcEntity>();
		try {
			String taskListStr = "SELECT \n"
					+ "    tcm.*, ttm.TT_DESC\n"
					+ "FROM\n"
					+ "    .task_category_mst tcm\n"
					+ "        INNER JOIN\n"
					+ "    task_type_mst ttm ON tcm.TT_CODE = ttm.TT_CODE\n"
					+ "WHERE\n"
					+ "    tcm.tenant_id = ? and tcm.TT_CODE = ? ";
			list = this.jdbcTemplate.query(taskListStr, new GetAllTaskCategorytcRowMapper(), tenantId, typeCode);
		} catch (Exception ex) {

		}
		return list;
	}

	@Override
	public List<TaskEntryHdrEntity> getTaskEntryHdr(String typeCode, String category, String masterId,
			String tenantId) {
		List<TaskEntryHdrEntity> list = new ArrayList<TaskEntryHdrEntity>();
		try {
			String taskHdrStr = "SELECT \r\n"
					+ "    hdr.*,dp.DEPARTMENT_NAME As DEPARTMENT_NAME,ttm.TT_DESC AS TT_DESC , tcm.TC_DESC AS TC_DESC\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        LEFT JOIN\r\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\r\n"
					+ "    where hdr.TASK_CATEGORY_CODE = ? and hdr.TASK_TYPE_CODE = ? and hdr.MASTER_ID  = ? and hdr.TENANT_ID = ? ";
			list = this.jdbcTemplate.query(taskHdrStr, new TaskEntryHdrRowMapper(), category, typeCode, masterId,
					tenantId);
		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<TaskEntryDtlEntity> getTaskEntryDtl(String ttHdrId) {
		List<TaskEntryDtlEntity> list = new ArrayList<TaskEntryDtlEntity>();
		try {
			String taskDtlStr = "SELECT \r\n"
					+ "    dtl.*, dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME\r\n"
					+ "FROM\r\n" + "    task_entry_dtl dtl\r\n" + "        LEFT JOIN\r\n"
					+ "    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = dtl.APPROVAL_STATUS LEFT JOIN employee_mst em on em.EMPLOYEE_ID = dtl.ASSIGNED_TO\r\n"
					+ "WHERE\r\n" + "    TE_HDR_ID = ? ";
			list = this.jdbcTemplate.query(taskDtlStr, new TaskEntryDtlRowMapper(), ttHdrId);
		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<TaskEntryHdrEntity> getTaskEntryHdrByDependentId(String typeCode, String category, String slaveId,
			String tenantId, String dependentId) {
		List<TaskEntryHdrEntity> list = new ArrayList<TaskEntryHdrEntity>();
		try {
			String taskHdrStr = "SELECT \r\n"
					+ "    hdr.*,dp.DEPARTMENT_NAME As DEPARTMENT_NAME,ttm.TT_DESC AS TT_DESC , tcm.TC_DESC AS TC_DESC\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        LEFT JOIN\r\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\r\n"
					+ "    where hdr.TASK_CATEGORY_CODE = ? and hdr.TASK_TYPE_CODE =? and hdr.MASTER_ID  = ? and hdr.TENANT_ID = ? AND hdr.DEPENDENT_TE_HDR_ID =? ";
			list = this.jdbcTemplate.query(taskHdrStr, new TaskEntryHdrRowMapper(), category, typeCode, slaveId,
					tenantId, dependentId);
		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<TaskEntryHdrEntity> getTaskEntryHdrByDtlId(String tenantId, String dependentId) {
		List<TaskEntryHdrEntity> list = new ArrayList<TaskEntryHdrEntity>();
		try {
			String taskHdrStr = "SELECT \r\n"
					+ "    hdr.*,dp.DEPARTMENT_NAME As DEPARTMENT_NAME,ttm.TT_DESC AS TT_DESC , tcm.TC_DESC AS TC_DESC\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        LEFT JOIN\r\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\r\n"
					+ "    where  hdr.TENANT_ID = ? AND hdr.DEPENDENT_TE_HDR_ID =? ";
			list = this.jdbcTemplate.query(taskHdrStr, new TaskEntryHdrRowMapper(), tenantId, dependentId);
		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int insertTaskEntryHdr(String ttHdrId, String masterId, String departmentCode, String taskTypeCode,
			String taskCategoryCode, String dependentTeHdrId, String empId, String tenantId, String pmHdrId) {
		int insertSts = 0;
		try {
			if (ttHdrId.isEmpty()) {
				ttHdrId = "0";
			}
			final String finalTtHdrId = ttHdrId;
			String insertStr = "INSERT INTO `task_entry_hdr` (`TT_HDR_ID`, `MASTER_ID`, `DEPARTMENT_CODE`, `TASK_TYPE_CODE`, `TASK_CATEGORY_CODE`, `DEPENDENT_TE_HDR_ID`, `LAST_UPDATED_DATETIME`, `LAST_UPDATED_BY`, `TENANT_ID`,`PM_HDR_ID`) VALUES (?,?,?,?,?,?,?,?,?,?)";
			// insertStr =
			// this.jdbcTemplate.update(insertStr,ttHdrId,slaveId,taskName,departmentCode,taskTypeCode,taskCategoryCode,dependentTeHdrId,c)

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertStr, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, finalTtHdrId);
					ps.setString(2, masterId);
					ps.setString(3, departmentCode);
					ps.setString(4, taskTypeCode);
					ps.setString(5, taskCategoryCode);
					ps.setString(6, dependentTeHdrId);
					ps.setString(7, CommonMethod.getCurrentDateTime());
					ps.setString(8, empId);
					ps.setString(9, tenantId);
					ps.setString(10, pmHdrId);
					return ps;
				}

			}, holder);
			insertSts = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertTaskEntryHdr Error" + ex);
		}
		return insertSts;
	}

	@Override
	public int insertTaskEntryDtl(String teHdrId, String ttDtlId, String activityName, String plannedStartDate,
			String dueDate, String plannedCompletedDAte, String completedDate, String approvalSeq,
			String ApprovalStatus, String isCompleted, String tenantId, String assigned, String updatedBy,
			String requirment, String qty) {
		int insertSts = 0;
		try {
			if (ttDtlId.equalsIgnoreCase("")) {
				ttDtlId = "0";
			}
			String insertStr = "INSERT INTO `task_entry_dtl` (`TE_HDR_ID`, `TT_DTL_ID`, `ACTIVITY_NAME`, `PLANNED_START_DATE`, `DUE_DATE`, `PLANNED_COMPLETED_DATE`,`COMPLETED_DATE`, `APPROVAL_SEQ`, `APPROVAL_STATUS`, `TENANT_ID`,`ASSIGNED_TO`,`LAST_UPDATE_DATETIME`,`LAST_UPDATED_BY`,REQUIREMENT_FROM,QTY) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			final String ttDtlId1 = ttDtlId;

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertStr, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, teHdrId);
					ps.setString(2, ttDtlId1);
					ps.setString(3, activityName);
					ps.setString(4, plannedStartDate);
					ps.setString(5, dueDate);
					ps.setString(6, plannedCompletedDAte);
					ps.setString(7, completedDate);
					ps.setString(8, approvalSeq);
					ps.setString(9, ApprovalStatus);
					ps.setString(10, tenantId);
					ps.setString(11, assigned);
					ps.setString(12, CommonMethod.getCurrentDateTime());
					ps.setString(13, updatedBy);
					ps.setString(14, requirment);
					ps.setString(15, qty);

					return ps;
				}

			}, holder);
			insertSts = holder.getKey().intValue();
			DesignTaskDAO.insertTaskRemarks(insertSts, ApprovalStatus, "Task created", tenantId, updatedBy,
					this.jdbcTemplate);

		} catch (Exception ex) {
			logger.error("insertTaskEntryDtl Error " + ex);
		}
		return insertSts;
	}

	public static void insertTaskRemarks(int insertSts, String status, String remarks, String tenantId, String empId,
			JdbcTemplate jdbcTemp) {
		try {
			String insertQ = "INSERT INTO task_entry_status_dtl (TE_DTL_ID, REMARKS, STATUS, EMPLOYEE_ID, TRANSCACTION_DATETIME, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?)";
			jdbcTemp.update(insertQ, insertSts, remarks, status, empId, CommonMethod.getCurrentDateTime(), tenantId);
		} catch (Exception ex) {
			logger.error("insertTaskRemarks Error" + ex);
		}
	}

	@Override
	public int updateTaskEntryDtl(String teHdrId, String ttDtlId, String activityName, String plannedStartDate,
			String dueDate, String plannedCompletedDAte, String completedDate, String isCompleted, String teDtlId,
			String assigned, String updatedBy) {
		int updateSts = 0;
		try {
			if (ttDtlId.equalsIgnoreCase("")) {
				ttDtlId = "0";
			}
			String updateStr = "UPDATE `task_entry_dtl` SET `TE_HDR_ID`=?, `TT_DTL_ID`=?, `ACTIVITY_NAME`=?, `PLANNED_START_DATE`=?, `DUE_DATE`=?, `PLANNED_COMPLETED_DATE`=?, `COMPLETED_DATE`=?, `IS_COMPLETED`=?,`ASSIGNED_TO`=? , `LAST_UPDATE_DATETIME` = ?,`LAST_UPDATED_BY` =?  WHERE `TE_DTl_ID`=? ";
			updateSts = this.jdbcTemplate.update(updateStr, teHdrId, ttDtlId, activityName, plannedStartDate, dueDate,
					plannedCompletedDAte, completedDate, isCompleted, CommonMethod.getCurrentDateTime(), assigned,
					updatedBy, teDtlId);
		} catch (Exception ex) {
			logger.error("insertTaskEntryDtl Error " + ex);
		}
		return updateSts;
	}

	@Override
	public int updateTaskEntryHdr(String teHdrId, String empid) {
		int updateStatus = 0;
		try {
			String updateStatusStr = "UPDATE `task_entry_hdr` SET `LAST_UPDATED_DATETIME`=? , `LAST_UPDATED_BY`=? WHERE `TE_HDR_ID`=? ";
			updateStatus = this.jdbcTemplate.update(updateStatusStr, CommonMethod.getCurrentDateTime(), empid, teHdrId);
		} catch (Exception ex) {
			logger.error("updateTaskEntryHdr Error " + ex);
		}
		return updateStatus;
	}

	@Override
	public int taskEntryCompleteSts(String tedtlId) {
		int checkCount = 0;
		try {
			String checkCountStr = "Select Count(*) as COUNT from task_entry_dtl where TE_DTl_ID = ? and IS_COMPLETED =1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(checkCountStr,tedtlId);
			checkCount = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("taskEntryCompleteSts Error " + ex);
		}
		return checkCount;
	}

	@Override
	public int updateTaskDtlStatus(String seq, String status, String teDtlId, String tenantId, String isCompleted,
			String completedDate, String statusDesc, String remarks, String empId, String completedptg) {
		int updateStatus = 0;
		try {
			String updateStatusStr = "UPDATE `task_entry_dtl` SET `APPROVAL_SEQ`= ? , `APPROVAL_STATUS`=? ,`COMPLETED_DATE` = ? ,`IS_COMPLETED` = ? ,`LAST_UPDATE_DATETIME`=?,`LAST_UPDATED_BY`=?, `COMPLETED_PTG`=? WHERE `TE_DTl_ID`=? AND `TENANT_ID` = ? ";
			updateStatus = this.jdbcTemplate.update(updateStatusStr, seq, status, completedDate, isCompleted,CommonMethod.getCurrentDateTime(),empId,completedptg, teDtlId,
					tenantId);
			insertTaskRemarks(Integer.parseInt(teDtlId), status, remarks, tenantId, empId, this.jdbcTemplate);
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return updateStatus;
	}

	@Override
	public String projectEnquiryIdByprojId(String projId, String tenantId) {
		String prjEnqId = "";
		try {
			String prjEnqIdStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN ENQUIRY_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS ENQUIRY_ID\r\n" + "FROM\r\n" + "    project_hdr\r\n"
					+ "WHERE\r\n" + "    PM_HDR_ID = ? AND TENANT_ID = ? ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(prjEnqIdStr,projId,tenantId);
			prjEnqId = resultMap.get("ENQUIRY_ID").toString();
			
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return prjEnqId;
	}

	@Override
	public List<GetTaskEntryDtlEntity> getTaskEntryDtlEntity(String ttCode, String tcCode, String masterId,
			String tenantid, String dependentId, String department) {
		List<GetTaskEntryDtlEntity> list = new ArrayList<GetTaskEntryDtlEntity>();
		try {
			String typeCode = "";
			String typeCategory = "";
			String hdrcheck = "";
			if (ttCode.equalsIgnoreCase("getAll")) {
				typeCode = "%%";
			} else {
				typeCode = "%" + ttCode + "%";
			}

			if (tcCode.equalsIgnoreCase("getAll")) {
				typeCategory = "%%";
			} else {
				typeCategory = "%" + tcCode + "%";
			}
			if (dependentId.equalsIgnoreCase("")) {
				hdrcheck = " is null and  hdr.TT_HDR_ID > 0";
			} else if (dependentId.equalsIgnoreCase("getAll")) {
				hdrcheck = "like '%%' and hdr.TT_HDR_ID = 0";
			} else {
				hdrcheck = "=" + dependentId + " and  hdr.TT_HDR_ID = 0";
			}

			// Step 1: Split the department codes
			String[] deptCodes = department.split(",");
			String deptInClause = Arrays.stream(deptCodes)
			        .map(code -> "'" + code.trim() + "'") // Add single quotes
			        .collect(Collectors.joining(", ", "(", ")"));
			
			String taskHdrStr = "SELECT \r\n" + "    dtl.*,\r\n" + "    dp.DEPARTMENT_NAME AS DEPARTMENT_NAME,\r\n"
					+ "    ttm.TT_DESC AS TT_DESC,dtl.REQUIREMENT_FROM,dtl.QTY,hdr.TASK_CATEGORY_CODE,hdr.TASK_TYPE_CODE,\r\n"
					+ "    tcm.TC_DESC AS TC_DESC, dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "        LEFT JOIN\r\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = dtl.APPROVAL_STATUS\r\n"
					+ "        LEFT JOIN\r\n" + "    employee_mst em ON em.EMPLOYEE_ID = dtl.ASSIGNED_TO\r\n"
					+ "WHERE\r\n" + "    hdr.TASK_TYPE_CODE LIKE ? \r\n"
					+ "        AND hdr.TASK_CATEGORY_CODE LIKE ? \r\n"
					+ "        AND hdr.MASTER_ID = ? and hdr.DEPARTMENT_CODE IN "+deptInClause+" and hdr.TENANT_ID = ? and hdr.DEPENDENT_TE_HDR_ID "
					+ hdrcheck + "  order by IS_COMPLETED asc, DUE_DATE asc";

			list = this.jdbcTemplate.query(taskHdrStr, new GetTaskEntryDtlRowMapper(), typeCode, typeCategory, masterId,
					 tenantid);
			for(int i=0;i<list.size();i++) {
				String subTaskCountQry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(dtl.TE_HDR_ID) > 0 THEN COUNT(*)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END SUB_TASK_COUNT\r\n" + 
						"FROM\r\n" + 
						"    task_entry_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    task_entry_dtl dtl ON dtl.TE_HDR_ID = hdr.TE_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    DEPENDENT_TE_HDR_ID = ?\r\n" + 
						"        AND TT_HDR_ID = 0;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(subTaskCountQry, list.get(i).getTeDtlId());
				int subTaskCount = Integer.parseInt(resultMap.get("SUB_TASK_COUNT").toString());
				list.get(i).setSubTaskCount(subTaskCount);
			}

		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int UpdateTaskDtlPtg(String teDtlId, String ptgVal, String empId, String remarks) {
		int updateval = 0;
		try {
			String updatevalStr = "UPDATE `task_entry_dtl` SET `COMPLETED_PTG`=? , ACTUAL__START_DATE = IFNULL(ACTUAL__START_DATE, ?) WHERE `TE_DTl_ID`=? ";
			updateval = this.jdbcTemplate.update(updatevalStr, ptgVal, CommonMethod.getCurrentDate(), teDtlId);

			String insertQ = "INSERT INTO task_entry_remarks (TE_DTl_ID, REMARKS, LOGGED_BY, LOGGED_DATETIME) VALUES (?, ?, ?, ?)";
			remarks = ptgVal + "% - " + remarks;
			updateval = this.jdbcTemplate.update(insertQ, teDtlId, remarks, empId, CommonMethod.getCurrentDate());

		} catch (Exception ex) {
			logger.error("UpdateTaskDtlPtg Error " + ex);
		}
		return updateval;
	}

	@Override
	public int updateAvgPercent(String teDtlId, String tenantId,String empId) {
		int updateval = 0;
		String updateDate=CommonMethod.getCurrentDateTime();
		try {

			String qry = "select case when count(*)>0 then 1 else 0 end AS seq from task_entry_dtl where TE_DTl_ID =? and APPROVAL_SEQ =1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,teDtlId);
			int checkSeq = Integer.parseInt(resultMap.get("seq").toString());
			List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO
					.getDocDtlcurrentSeq("DC014", "2", tenantId);
			
			if (checkSeq > 0) {
				if (currSeqDocLifeCycleMstList.size() > 0) {
					this.jdbcTemplate.update(
							"UPDATE `task_entry_dtl` SET `APPROVAL_SEQ`=? , `APPROVAL_STATUS`=?,`LAST_UPDATE_DATETIME`=?,`LAST_UPDATED_BY`=? WHERE `TE_DTl_ID`=? ",
							currSeqDocLifeCycleMstList.get(0).getCurrSequence(),
							currSeqDocLifeCycleMstList.get(0).getDocStatus(),updateDate,empId,teDtlId);
				}
			}
			String selQ = "SELECT \r\n" + "    CASE\r\n" + "        WHEN DEPENDENT_TE_HDR_ID IS NULL THEN 0\r\n"
					+ "        ELSE DEPENDENT_TE_HDR_ID\r\n" + "    END as COUNT\r\n" + "FROM\r\n"
					+ "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl dtl\r\n" + "WHERE\r\n"
					+ "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "        AND dtl.TE_DTl_ID = ?";
			
			Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(selQ,teDtlId);
			int depV = Integer.parseInt(resultMap1.get("COUNT").toString());
			if (depV > 0) {

				String getQ = "SELECT \r\n" + "    ROUND(AVG(dtl.COMPLETED_PTG), 2) AS AVG_COMPLETED_PTG\r\n"
						+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        INNER JOIN\r\n"
						+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "WHERE\r\n"
						+ "    hdr.DEPENDENT_TE_HDR_ID = ?;";
				
				Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(getQ,depV);
				String resp = resultMap2.get("AVG_COMPLETED_PTG").toString();
				// check main task status and update
				Map<String, Object> map = this.jdbcTemplate.queryForMap(
						"select case when count(*)>0 then 1 else 0 end AS seq from task_entry_dtl where TE_DTl_ID =? and APPROVAL_SEQ =1",depV);
				int checkHdrSeq = Integer.parseInt(map.get("seq").toString());
				if(checkHdrSeq>0) {
					String updateQ = "update task_entry_dtl set COMPLETED_PTG = ? ,`APPROVAL_SEQ`=? , `APPROVAL_STATUS`=?,`LAST_UPDATE_DATETIME`=?,`LAST_UPDATED_BY`=? where TE_DTl_ID= ?";
					updateval = this.jdbcTemplate.update(updateQ, resp,currSeqDocLifeCycleMstList.get(0).getCurrSequence(),
							currSeqDocLifeCycleMstList.get(0).getDocStatus(),updateDate,empId,depV);
				}else {
					String updateQ = "update task_entry_dtl set COMPLETED_PTG = ? where TE_DTl_ID= ?";
					updateval = this.jdbcTemplate.update(updateQ, resp, depV);
				}
				
			}
		} catch (Exception ex) {
			logger.error("updateAvgPercent Error" + ex);
		}
		return updateval;
	}

	@Override
	public List<TaskTemplateDtlEntity> tasktemplateDtl(String ttHdrId, String tenantId) {
		List<TaskTemplateDtlEntity> list = new ArrayList<TaskTemplateDtlEntity>();
		try {
			String taskListStr = "SELECT \r\n" + 
					"    @a:=@a + 1 Serial_Number, dtl.*\r\n" + 
					"FROM\r\n" + 
					"    task_template_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_template_dtl dtl ON hdr.TT_HDR_ID = dtl.TT_HDR_ID,\r\n" + 
					"    (SELECT @a:=0) AS a\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TT_HDR_ID = ?\r\n" + 
					"        AND hdr.TENANT_ID = ? ";
			list = this.jdbcTemplate.query(taskListStr, new TaskTemplateDtlRowMapper(), ttHdrId, tenantId);
		} catch (Exception ex) {
			logger.error("tasktemplateDtl Error" + ex);
		}
		return list;
	}

	@Override
	public List<TemplateHdrNameEntity> taskhdrName(String deptCode, String typeCode, String catCode, String tenantId) {
		List<TemplateHdrNameEntity> list = new ArrayList<TemplateHdrNameEntity>();
		try {
			String taskListStr = "SELECT \r\n" + "    hdr.TT_HDR_ID, hdr.TT_NAME \r\n" + "FROM\r\n"
					+ "    task_template_hdr hdr\r\n" + "WHERE\r\n" + "    hdr.TT_DEPARTMENT_CODE = ? \r\n"
					+ "        AND hdr.TASK_TYPE_CODE = ? \r\n" + "        AND hdr.TASK_CATEGORY_CODE = ? \r\n"
					+ "        AND hdr.TENANT_ID = ? ";
			list = this.jdbcTemplate.query(taskListStr, new TemplateHdrNameRowMapper(), deptCode, typeCode, catCode,
					tenantId);
		} catch (Exception ex) {
			logger.error("taskhdrName Error" + ex);
		}
		return list;
	}

	@Override
	public String getTeHdrId(String dept, String typeCode, String typeCat, String ttHdrId, String tenantId,
			String masterId) {
		String prjEnqId = "";
		try {
			String prjEnqIdStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN TE_HDR_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS TE_HDR_ID\r\n" + "FROM\r\n" + "    task_entry_hdr\r\n"
					+ "WHERE\r\n" + "    TT_HDR_ID =? AND DEPARTMENT_CODE =?\r\n"
					+ "        AND TASK_TYPE_CODE =?   AND TASK_CATEGORY_CODE = ?  AND TENANT_ID = ? and MASTER_ID=?";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(prjEnqIdStr,ttHdrId,dept,typeCode,typeCat,tenantId,masterId);
			prjEnqId = resultMap.get("TE_HDR_ID").toString();
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return prjEnqId;
	}

	@Override
	public List<DocumentStatusMstEntity> getNextSeqandStatus(int currentSeq, String docType, String tenantId) {
		String NEXT_SEQ = "";
		List<DocumentStatusMstEntity> getLi = new ArrayList<DocumentStatusMstEntity>();
		try {
			String qry = "select case when NEXT_SEQ is not null then NEXT_SEQ else 'NA' end as NEXT_SEQ from document_lifecycle_mst where DOC_TYPE=? and TENANT_ID=? and CURR_SEQUENCE =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,docType,tenantId,currentSeq);
			NEXT_SEQ = resultMap.get("NEXT_SEQ").toString();

			if(!NEXT_SEQ.equalsIgnoreCase("NA")) {
			qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='" + tenantId
					+ "' and CURR_SEQUENCE ='" + NEXT_SEQ + "' ";
			getLi = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
			}
			
		} catch (Exception ex) {
			logger.error("getNextSeqandStatus Error" + ex);
		}
		return getLi;

	}

	@Override
	public List<DocumentStatusMstEntity> getNextSeqandStatusByDoc(int currentSeq, String docType, String tenantId,
			String docGrp) {
		String NEXT_SEQ = "";
		List<DocumentStatusMstEntity> getLi = new ArrayList<DocumentStatusMstEntity>();
		try {

			String nullQry = "select case when NEXT_SEQ is not null then 1 else 0 end as NEXT_SEQ from document_lifecycle_mst where DOC_TYPE=? and TENANT_ID=? and CURR_SEQUENCE =? and  DOC_GROUP = ?";
			Map<String, Object> result = jdbcTemplate.queryForMap(nullQry,docType,tenantId,currentSeq,docGrp);
			int notNull = Integer.parseInt(result.get("NEXT_SEQ").toString());
			if (notNull==1) {
				String qry = "select NEXT_SEQ from document_lifecycle_mst where DOC_TYPE=? and TENANT_ID=? and CURR_SEQUENCE =? and  DOC_GROUP = ?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,docType,tenantId,currentSeq,docGrp);
				NEXT_SEQ = resultMap.get("NEXT_SEQ").toString();
	
				qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='" + tenantId
						+ "' and CURR_SEQUENCE ='" + NEXT_SEQ + "' and  DOC_GROUP =  '" + docGrp + "'";
				getLi = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
			}
		} catch (Exception ex) {
			logger.error("getNextSeqandStatusByDoc Error" + ex);
		}
		return getLi;

	}
	@Override
	public int updateTaskDtlStatusTbl(String seq, String status, String teDtlId, String tenantId, String isCompleted,
			String completedDate, String statusDesc, String remarks, String empId) {
		int updateStatus = 0;
		try {
			String updateStatusStr = "UPDATE `task_entry_dtl` SET `APPROVAL_SEQ`= ? , `APPROVAL_STATUS`=? ,`COMPLETED_DATE` = ? ,`IS_COMPLETED` = ? ,`LAST_UPDATE_DATETIME`=?,`LAST_UPDATED_BY`=? WHERE `TE_DTl_ID`=? AND `TENANT_ID` = ? ";
			updateStatus = this.jdbcTemplate.update(updateStatusStr, seq, status, completedDate, isCompleted,CommonMethod.getCurrentDateTime(),empId, teDtlId,
					tenantId);
			insertTaskRemarks(Integer.parseInt(teDtlId), status, remarks, tenantId, empId, this.jdbcTemplate);
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatusTbl Error " + ex);
		}
		return updateStatus;
	}


	@Override
	public List<GetRemarksByIdResponse> getTemplateHdrName(GetTaskRemarksByIDRequest getTaskRemarksByIDRequest) {

		List<GetRemarksByIdResponse> remarks = null;
		try {
			String prjEnqIdStr = "SELECT \r\n"
					+ "    te.*,em.EMPLOYEE_FIRSTNAME , ds.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    task_entry_status_dtl te,\r\n" + "    employee_mst em,\r\n"
					+ "    document_status_type_code ds\r\n" + "WHERE\r\n"
					+ "    TE_DTL_ID = ? AND te.TENANT_ID = ?\r\n"
					+ "    and ds.DOCUMENT_STATUS_TYPE_CODE = te.STATUS\r\n" + "    and te.EMPLOYEE_ID= em.EMPLOYEE_ID";
			remarks = this.jdbcTemplate.query(prjEnqIdStr, new TaskRemarksRowMapper(),
					getTaskRemarksByIDRequest.getTaskDtlId(), getTaskRemarksByIDRequest.getTenantId());
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return remarks;
	}

	@Override
	public String getStatusByDesc(String statusCode, String tenantId) {

		String resp = "";
		try {
			String prjEnqIdStr = "SELECT DOCUMENT_STATUS_TYPE_DESCRIPTION " +
					"FROM document_status_type_code " +
					"WHERE DOCUMENT_STATUS_TYPE_CODE = ?";

			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(prjEnqIdStr, statusCode);

			if (!resultList.isEmpty()) {
				resp = resultList.get(0).get("DOCUMENT_STATUS_TYPE_DESCRIPTION").toString();
			} else {
				resp = null; // or set to "Unknown", depending on your requirement
			}
		} catch (Exception ex) {
			logger.error("getStatusByDesc Error " + ex);
		}
		return resp;
	}

	@Override
	public int getCounthdrId(String teHdrId, String tenantId) {
		int getCounthdrId = 0;
		try {

			String getCounthdrIdStr = "select count(*) as COUNT from task_entry_dtl where TE_HDR_ID = ? and TENANT_ID =? and IS_COMPLETED <>1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCounthdrIdStr, teHdrId,tenantId);
			getCounthdrId = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getCounthdrId Error" + ex);
		}
		return getCounthdrId;
	}

	@Override
	public int getCounthdrIdIsCompleted(String teHdrId, String tenantId, String completed) {
		int getCounthdrIdIsCompleted = 0;
		try {
			String getCounthdrIdIsCompletedStr = "select count(*) as COUNT from task_entry_dtl where TE_HDR_ID = ? and TENANT_ID = ? and IS_COMPLETED =? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCounthdrIdIsCompletedStr, teHdrId,tenantId,completed);
			getCounthdrIdIsCompleted = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getCounthdrIdIsCompleted Error" + ex);
		}
		return getCounthdrIdIsCompleted;
	}

	@Override
	public String TaskReassignFlag(TaskReassignRequest taskReassReq) {

		String resp = "0";
		try {

			String checkQ = "select count(*) as COUNT from task_entry_dtl where TE_DTl_ID=? and IS_COMPLETED =0";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(checkQ,taskReassReq.getTaskdtlId());
			int taskCnt = Integer.parseInt(resultMap.get("COUNT").toString());

			if (taskCnt > 0) {
				String desig = uploadManagementDAO.getDesigCodeByEmpId(taskReassReq.getEmpId(),
						taskReassReq.getTenantId());
				String getQ = "select count(*) as COUNT from project_wbs_initiation_mst where PM_ID =? and FIND_IN_SET(?,MASTER_POC) ";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,taskReassReq.getPmId(),desig);
				resp = resultMap1.get("COUNT").toString();
			}

		} catch (Exception ex) {
			logger.error("TaskReassignFlag Error " + ex);
		}
		return resp;
	}

	@Override
	public int TaskReassignForEmpId(TaskReassignRequest taskReassReq) {

		int resp = 0;
		try {

			String getQ = "UPDATE task_entry_dtl SET ASSIGNED_TO=? WHERE TE_DTl_ID=? and IS_COMPLETED =0";
			resp = this.jdbcTemplate.update(getQ, taskReassReq.getEmpId(), taskReassReq.getTaskdtlId());

		} catch (Exception ex) {
			logger.error("TaskReassignFlag Error " + ex);
		}
		return resp;
	}

	@Override
	public List<TaskCategoryMstEntity> getTaskCategoryRecorded(GetTaskRecordedRequest getRecordedTask) {

		List<TaskCategoryMstEntity> taskCate = null;
		try {
			String prjEnqIdStr = "SELECT \r\n" + "    distinct tc.TC_CODE,\r\n"
					+ "    tc.TC_DESC,tc.IS_ACTIVE,tc.TENANT_ID\r\n" + "FROM\r\n" + "    task_entry_hdr hdr,\r\n"
					+ "    task_category_mst tc\r\n" + "WHERE\r\n"
					+ "    hdr.TASK_CATEGORY_CODE = tc.TC_CODE and hdr.TASK_TYPE_CODE = ? and hdr.DEPARTMENT_CODE=? and hdr.TENANT_ID=?";
			taskCate = this.jdbcTemplate.query(prjEnqIdStr, new TaskCategoryMstRowMapper(),
					getRecordedTask.getTypeCode(), getRecordedTask.getDeptCode(), getRecordedTask.getTenantId());
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return taskCate;
	}

	@Override
	public List<TaskEntryRemarksEntity> getTaskDtlRemarksById(GetTaskRemarksByIDRequest getTaskRemarksByID) {

		List<TaskEntryRemarksEntity> taskCate = null;
		try {
			String prjEnqIdStr = "SELECT \r\n" + "    ter.*,em.EMPLOYEE_FIRSTNAME\r\n" + "FROM\r\n"
					+ "    task_entry_remarks ter,\r\n" + "    employee_mst em\r\n" + "WHERE\r\n"
					+ "    em.EMPLOYEE_ID = ter.LOGGED_BY\r\n" + "        AND TE_DTl_ID = ?";
			taskCate = this.jdbcTemplate.query(prjEnqIdStr, new GetTaskEntryRemarksRowMapper(),
					getTaskRemarksByID.getTaskDtlId());
		} catch (Exception ex) {
			logger.error("updateTaskDtlStatus Error " + ex);
		}
		return taskCate;
	}

	@Override
	public String TaskPercentageFlag(GetTaskPercentFlagRequest taskPercentFlag) {

		String resp = "NA";
		try {

			String checkQ = "select count(*) as COUNT from task_entry_hdr where DEPENDENT_TE_HDR_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(checkQ,taskPercentFlag.getDtlId());
			int taskCnt = Integer.parseInt(resultMap.get("COUNT").toString());

			if (taskCnt > 0) {
				String getQ = "SELECT \r\n" + "    avg(dtl.COMPLETED_PTG) as avgComp\r\n" + "FROM\r\n"
						+ "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl dtl\r\n" + "WHERE\r\n"
						+ "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "    and DEPENDENT_TE_HDR_ID = ?";
				
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(getQ,taskPercentFlag.getDtlId());
				resp = resultMap1.get("avgComp").toString();

				String updateQ = "update task_entry_dtl set COMPLETED_PTG = ? where TE_DTl_ID= ?";
				this.jdbcTemplate.update(updateQ, resp, taskPercentFlag.getDtlId());
			}

		} catch (Exception ex) {
			logger.error("TaskPercentageFlag DAO Error " + ex);
		}
		return resp;
	}

	@Override
	public List<GetTaskHdrByEmpIdEntity> getTaskHdrByEmpId(GetTaskHdrByEmpIdReq getTaskHdrByEmpIdReq) {

		List<GetTaskHdrByEmpIdEntity> taskCate = null;
		try {
			String prjEnqIdStr = "SELECT DISTINCT\r\n" + "    (teh.TASK_CATEGORY_CODE) AS TASK_CATEGORY_CODE,\r\n"
					+ "    tcm.TC_DESC\r\n" + "FROM\r\n" + "    task_entry_hdr teh,\r\n"
					+ "    task_category_mst tcm,\r\n" + "    employee_mst em\r\n" + "WHERE\r\n" + "   \r\n"
					+ "    teh.DEPARTMENT_CODE=em.DEPARTMENT_CODE\r\n"
					+ "        AND tcm.TC_CODE = teh.TASK_CATEGORY_CODE\r\n" + "        AND em.EMPLOYEE_ID = ?\r\n"
					+ "        AND em.TENANT_ID = ?";
			taskCate = this.jdbcTemplate.query(prjEnqIdStr, new GetTaskHdrByEmpIdRowMapper(),
					getTaskHdrByEmpIdReq.getEmpId(), getTaskHdrByEmpIdReq.getTenantId());
		} catch (Exception ex) {
			logger.error("getTaskHdrByEmpId Error " + ex);
		}
		return taskCate;
	}

	@Override
	public List<GetTaskHdrAndDtlIdEntity> deleteTaskHdrAndDtl(String teHdrId, String tenantId) {

		List<GetTaskHdrAndDtlIdEntity> dtlList = new ArrayList<>();
		try {

			String getdtlQ = "select TE_DTl_ID,TE_HDR_ID from task_entry_dtl where TE_HDR_ID='" + teHdrId
					+ "' and TENANT_ID='" + tenantId + "'";
			dtlList = this.jdbcTemplate.query(getdtlQ, new GetTaskHdrAndDtlIdRowMapper());
		} catch (Exception ex) {
			logger.error("deleteTaskHdrAndDtl Error " + ex);
		}
		return dtlList;
	}

	public int commanDeleteTaskHdrAndDtl(String teHdrId, String tenantId) {
		int hdrDeleteId = 0;
		int detailDeleteId = 0;
		int statusDetailDeleteId = 0;
		String getdtlQ = "select TE_DTl_ID,TE_HDR_ID from task_entry_dtl where TE_HDR_ID='" + teHdrId
				+ "' and TENANT_ID='" + tenantId + "'";
		List<GetTaskHdrAndDtlIdEntity> getTaskHdrAndDtlId = this.jdbcTemplate.query(getdtlQ,
				new GetTaskHdrAndDtlIdRowMapper());

		if (getTaskHdrAndDtlId.size() > 0) {
			for (GetTaskHdrAndDtlIdEntity obj : getTaskHdrAndDtlId) {
				String statusDtlQ1 = "delete from task_entry_status_dtl where TE_DTL_ID=? and TENANT_ID=?";
				statusDetailDeleteId = this.jdbcTemplate.update(statusDtlQ1,obj.getTeDtlId(),tenantId);

				String remarkQ = "select count(*) as COUNT from task_entry_remarks where TE_DTl_ID=?";
				Map<String,Object> resultMap = this.jdbcTemplate.queryForMap(remarkQ,obj.getTeDtlId() );
				int count  = Integer.parseInt(resultMap.get("COUNT").toString());
				if (count > 0) {
					String statusDtlQ2 = "delete from task_entry_remarks where TE_DTl_ID=?";
					this.jdbcTemplate.update(statusDtlQ2,obj.getTeDtlId());
				}

			}
		}
		if (statusDetailDeleteId > 0) {
			String dtlQ1 = "delete from task_entry_dtl where TE_HDR_ID=? and TENANT_ID=?";
			detailDeleteId = this.jdbcTemplate.update(dtlQ1,teHdrId,tenantId);
		}
		if (detailDeleteId > 0) {
			String hdrQ1 = "delete from task_entry_hdr where TE_HDR_ID=? and TENANT_ID=?";
			hdrDeleteId = this.jdbcTemplate.update(hdrQ1,teHdrId,tenantId);
		}
		return hdrDeleteId;
	}

	public int deleteTaskDtl(String teDtlId, String tenantId) {
		// int hdrDeleteId = 0;
		int detailDeleteId = 0;
		int statusDetailDeleteId = 0;

		

		String statusDtlQ1 = "delete from task_entry_status_dtl where TE_DTL_ID=? and TENANT_ID=?";
		statusDetailDeleteId = this.jdbcTemplate.update(statusDtlQ1,teDtlId,tenantId);

		String remarkQ = "select count(*) as COUNT from task_entry_remarks where TE_DTl_ID=?";
		Map<String,Object> resultMap = this.jdbcTemplate.queryForMap(remarkQ,teDtlId);
		int count = Integer.parseInt(resultMap.get("COUNT").toString());
		if (count > 0) {
			String statusDtlQ2 = "delete from task_entry_remarks where TE_DTl_ID=?";
			this.jdbcTemplate.update(statusDtlQ2,teDtlId);
		}

		if (statusDetailDeleteId > 0) {
			String dtlQ1 = "delete from task_entry_dtl where TE_DTl_ID=? and TENANT_ID=?";
			detailDeleteId = this.jdbcTemplate.update(dtlQ1,teDtlId,tenantId);
		}
		// if (detailDeleteId > 0) {
//				String hdrQ1 = "delete from task_entry_hdr where TE_HDR_ID='" + teHdrId + "' and TENANT_ID='" + tenantId
		// + "'";
		// hdrDeleteId = this.jdbcTemplate.update(hdrQ1);
//			}
		return detailDeleteId;
	}

	@Override
	public void relaventDeleteTaskHdrAndDtl(String teDtlId, String tenantId) {
		//GetTaskHdrAndDtlIdEntity getTaskHdrAndDtlIdEntity = new GetTaskHdrAndDtlIdEntity();
		String teHdrId = "";
		try {

			String hdrQry = "SELECT \r\n" + "    CASE\r\n" + "        WHEN count(*)>0 THEN TE_HDR_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS TE_HDR_ID\r\n" + "FROM\r\n" + "    task_entry_hdr\r\n"
					+ "WHERE\r\n" + "    DEPENDENT_TE_HDR_ID = ? ";
			Map<String,Object> resultMap = this.jdbcTemplate.queryForMap(hdrQry,teDtlId);
			teHdrId = resultMap.get("TE_HDR_ID").toString();

			if (!teHdrId.isEmpty() && null != teHdrId) {

				commanDeleteTaskHdrAndDtl(teHdrId, tenantId);

			}

		} catch (Exception ex) {
			logger.error("relaventDeleteTaskHdrAndDtl Error " + ex);
		}
	}

	@Override
	public int CommanDeleteTasHdrAndDtls(String teHdrId, String tenantId) {
		int hdrIds = 0;
		try {
			hdrIds = commanDeleteTaskHdrAndDtl(teHdrId, tenantId);

		} catch (Exception ex) {
			logger.error("CommanDeleteTasHdrAndDtls Error " + ex);
		}
		return hdrIds;
	}

	@Override
	public List<GetTaskHdrByEmpIdEntity> getTaskCategoryByPmHdrId(
			GetTaskCategoryByPmHdrIdRequest getTaskCategoryByPmHdrIdReq) {
		List<GetTaskHdrByEmpIdEntity> taskCate = null;
		try {
			String prjEnqIdStr = "SELECT DISTINCT(TASK_CATEGORY_CODE) AS TASK_CATEGORY_CODE,\n"
					+ "		ttm.TC_DESC AS TC_DESC \n"
					+ "	FROM\n"
					+ "		task_entry_hdr hdr\n"
					+ "			INNER JOIN\n"
					+ "		task_category_mst ttm ON hdr.TASK_CATEGORY_CODE = ttm.TC_CODE\n"
					+ "	WHERE\n"
					+ "		PM_HDR_ID = ? \n"
					+ "			AND TASK_TYPE_CODE = ? \n"
					+ "				AND hdr.TENANT_ID = ? ";
			taskCate = this.jdbcTemplate.query(prjEnqIdStr, new GetTaskHdrByEmpIdRowMapper(),
					getTaskCategoryByPmHdrIdReq.getPmHdrId(), getTaskCategoryByPmHdrIdReq.getTaskTypeCode(), getTaskCategoryByPmHdrIdReq.getTenantId());
		} catch (Exception ex) {
			logger.error("getTaskCategoryByPmHdrId Error " + ex);
		}
		return taskCate;
	}

	@Override
	public String getAssignedTo(String dtlId) {
		// TODO Auto-generated method stub
		String assignEmp="";
		try {
			String empQry="select ASSIGNED_TO from task_entry_dtl where TE_DTl_ID=?;\r\n" ;
			Map<String,Object> resultMap = this.jdbcTemplate.queryForMap(empQry,dtlId);
			assignEmp = resultMap.get("ASSIGNED_TO").toString();
		}catch(Exception ex) {
			logger.error("getAssignedTo Error " + ex);
		}
		return assignEmp;
	}

	@Override
	public int updateSubTaskSeq(String teHdrId,String empId,String seq, String status) {
		int resp = 0;
		try {
			String getQ = "UPDATE task_entry_dtl SET APPROVAL_SEQ=?, APPROVAL_STATUS=?, LAST_UPDATE_DATETIME=?, LAST_UPDATED_BY=? WHERE TE_HDR_ID=? and IS_COMPLETED=0";
			resp = this.jdbcTemplate.update(getQ,seq,status,CommonMethod.getCurrentDateTime(),empId, teHdrId);

		} catch (Exception ex) {
			logger.error("TaskReassignFlag Error " + ex);
		}
		return resp;
	}

	@Override
	public int CheckSubCount(String teDtlId, String tenantId) {
		// 
		int countCheck=0;
		try {
			String CountQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(dtl.TE_DTl_ID) > 0 THEN COUNT(*)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END SUB_TASK_COUNT\r\n" + 
					"FROM\r\n" + 
					"    task_entry_hdr hdr inner join task_entry_dtl dtl on dtl.TE_HDR_ID=hdr.TE_HDR_ID\r\n" + 
					"WHERE "+ 
					"    hdr.DEPENDENT_TE_HDR_ID = ? and dtl.TENANT_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(CountQry, teDtlId,tenantId);
			countCheck = Integer.parseInt(resultMap.get("SUB_TASK_COUNT").toString());
		}catch(Exception ex) {
			logger.error("CheckSubCount Error " + ex);
		}

		return countCheck;
	}

	@Override
	public int insertTaskTypeMaster(String taskTypeCode, String taskDesc, String deptCode, String tenantId,
			String isActive) {
		int hdrId=0;
		try {
			String ttCode="";

			String ttCodeQry = "select TT_CODE from  task_type_mst order by TT_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ttCodeQry);
			ttCode = resultMap.get("TT_CODE").toString();
			String newTtCode = CommonMethod.getTaskTypeNewCode(ttCode);

			String inquery = "INSERT INTO `task_type_mst`\r\n" + 
					"(`TT_CODE`,\r\n" + 
					"`TT_DESC`,\r\n" + 
					"`DEPT_CODE`,\r\n" + 
					"`IS_ACTIVE`,\r\n" + 
					"`TENANT_ID`)\r\n" + 
					"VALUES\r\n" + 
					"(?,?,?,?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, newTtCode);
					ps.setString(2, taskDesc);
					ps.setString(3, deptCode);
					ps.setString(4, isActive);
					ps.setString(5, tenantId);
					return ps;
				}

			}, holder);
			hdrId = holder.getKey().intValue();
		}catch(Exception ex) {
			logger.error("insertTaskTypeMaster Error " + ex);
		}
		return hdrId;
	}

	@Override
	public int updateTypeMaster(String taskTypeCode, String taskDesc, String deptCode, String tenantId,
			String isActive) {
		int resp=0;
		try {
			String qry="UPDATE task_type_mst \r\n" + 
					"SET \r\n" + 
					"    TT_DESC =?,\r\n" + 
					"    DEPT_CODE =?,\r\n" + 
					"    IS_ACTIVE = ?,\r\n" + 
					"    TENANT_ID = ?\r\n" + 
					"WHERE\r\n" + 
					"    TT_CODE = ?;";
			
			resp = this.jdbcTemplate.update(qry,taskDesc,deptCode,isActive,tenantId,taskTypeCode);
			
		}catch(Exception ex) {
			logger.error("updateTypeMaster Error " + ex);
		}
		return resp;
	}

	@Override
	public int insertTaskCategoryMst(String taskCategoryCode, String taskCategoryDesc, String taskTypeCode,
			String isActive, String tenantID) {
		int hdrId=0;
		try {
			
			String ttCode="";
			String ttCodeQry = "select TC_CODE from task_category_mst order by TC_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ttCodeQry);
			ttCode = resultMap.get("TC_CODE").toString();
			String newTtCode = CommonMethod.getTaskCategoryMasterNewCode(ttCode);

			String inquery = "INSERT INTO `task_category_mst`\r\n" + 
					"(`TC_CODE`,\r\n" + 
					"`TC_DESC`,\r\n" + 
					"`TT_CODE`,\r\n" + 
					"`IS_ACTIVE`,\r\n" + 
					"`TENANT_ID`)\r\n" + 
					"VALUES\r\n" + 
					"(?,?,?,?,?);";
			hdrId=this.jdbcTemplate.update(inquery,newTtCode,taskCategoryDesc,taskTypeCode,isActive,tenantID);
			
			
		}catch(Exception ex) {
			logger.error("insertTaskCategoryMst Method Exception "+ex);
		}
		return hdrId;
	}

	@Override
	public int updateTaskCategoryMst(String taskCategoryCode, String taskCategoryDesc, String taskTypeCode,
			String isActive, String tenantID) {
		int resp=0;
		try {
			
			String qry="UPDATE `task_category_mst`\r\n" + 
					"SET\r\n" + 
					"`TC_DESC` = ?,\r\n" + 
					"`TT_CODE` = ?,\r\n" + 
					"`IS_ACTIVE` = ?,\r\n" + 
					"`TENANT_ID` = ?\r\n" + 
					"WHERE `TC_CODE` = ?;";
			
			resp = this.jdbcTemplate.update(qry,taskCategoryDesc,taskTypeCode,isActive,tenantID,taskCategoryCode);
			
		}catch(Exception ex) {
			logger.error("updateTaskCategoryMst Method Exception "+ex);
		}
		return resp;
	}

	@Override
	public List<GetAllTaskTypeEntity> getAllTaskType(String tenantId, String deptCode) {
		List<GetAllTaskTypeEntity> list = new ArrayList<GetAllTaskTypeEntity>();
		try {
			String finalDeptCode = "";
			if(deptCode.equalsIgnoreCase("getAll")) {
				finalDeptCode = "%%";
			}else {
				finalDeptCode =deptCode;
			}
			String taskListStr = "SELECT \n"
					+ "    ttm.*, d.DEPARTMENT_NAME\n"
					+ "FROM\n"
					+ "    task_type_mst ttm\n"
					+ "        INNER JOIN\n"
					+ "    department d ON ttm.DEPT_CODE = d.DEPARTMENT_CODE\n"
					+ "WHERE\n"
					+ "    ttm.TENANT_ID = ? \n"
					+ "        AND ttm.DEPT_CODE like '"+finalDeptCode+"' ";
			list = this.jdbcTemplate.query(taskListStr, new GetAllTaskTypeRowMapper(), tenantId);
		} catch (Exception ex) {

		}
		return list;
	}

	@Override
	public int insertTaskTemplateHdr(String ttName, String ttCreatedBy, String createdOn, String ttDepartmentCode,
			String taskTypeCode, String taskCategoryCode, String isActive, String lastUpdatedDateTime,
			String lastUpdatedBy, String tenantId) {
		int hdrId=0;
		try {

			String inquery = "INSERT INTO `task_template_hdr` ( `TT_NAME`, `TT_CREATED_BY`, `TT_CREATED_ON`, `TT_DEPARTMENT_CODE`, `TASK_TYPE_CODE`, `TASK_CATEGORY_CODE`, `IS_ACTIVE`, `LAST_UPDATED_DATETIME`, `LAST_UPDATED_BY`, `TENANT_ID`) VALUES (?,?,?,?,?,?,?,?,?,?) ";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, ttName);
					ps.setString(2, ttCreatedBy);
					ps.setString(3, createdOn);
					ps.setString(4, ttDepartmentCode);
					ps.setString(5, taskTypeCode);
					ps.setString(6, taskCategoryCode);
					ps.setString(7, isActive);
					ps.setString(8, lastUpdatedDateTime);
					ps.setString(9, lastUpdatedBy);
					ps.setString(10, tenantId);
					return ps;
				}

			}, holder);
			hdrId = holder.getKey().intValue();
		}catch(Exception ex) {
			logger.error("insertTaskTemplateHdr Error " + ex);
		}
		return hdrId;
	}

	@Override
	public int updateTaskTemplateHdr(String ttName, String ttDepartmentCode,String taskTypeCode,String taskCategoryCode,String isActive, String lastUpdatedDateTime,
			String lastUpdatedBy, String ttHdrId) {
		int resp=0;
		try {
			
			String qry="UPDATE `task_template_hdr` SET `TT_NAME`= ?, `LAST_UPDATED_DATETIME`= ? , `LAST_UPDATED_BY`= ? , `IS_ACTIVE`= ?, `TT_DEPARTMENT_CODE`=?, `TASK_TYPE_CODE` =? WHERE `TT_HDR_ID`= ? ";
			
			resp = this.jdbcTemplate.update(qry,ttName,lastUpdatedDateTime,lastUpdatedBy,isActive,ttDepartmentCode,taskTypeCode,ttHdrId);
			
		}catch(Exception ex) {
			logger.error("updateTaskTemplateHdr Method Exception "+ex);
		}
		return resp;
	}

	@Override
	public int insertTaskTemplateDtl(String ttHdrId, String activityName, String plannedDurationDays, String isActive,
			String lastUpdatedDateTime, String lastUpdatedBy, String tenantId) {
		int hdrId=0;
		try {

			String inquery = "INSERT INTO `task_template_dtl` (`TT_HDR_ID`, `ACTIVITY_NAME`, `PLANNED_DURATION_DAYS`, `IS_ACTIVE`, `LAST_UPDATED_DATETIME`, `LAST_UPDATED_BY`, `TENANT_ID`) VALUES ( ?,?,?,?,?,?,?)"
					+ "";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, ttHdrId);
					ps.setString(2, activityName);
					ps.setString(3, plannedDurationDays);
					ps.setString(4, isActive);
					ps.setString(5, lastUpdatedDateTime);
					ps.setString(6, lastUpdatedBy);
					ps.setString(7, tenantId);
					return ps;
				}

			}, holder);
			hdrId = holder.getKey().intValue();
		}catch(Exception ex) {
			logger.error("insertTaskTemplateDtl	 Error " + ex);
		}
		return hdrId;
	}

	@Override
	public int updateTaskTemplateDtl(String activityName, String plannedDurationDays, String isActive,
			String lastUpdatedDateTime, String lastUpdatedBy, String ttDtlId) {
		int resp=0;
		try {
			
			String qry="UPDATE `task_template_dtl` SET `ACTIVITY_NAME`= ?, `PLANNED_DURATION_DAYS`= ?, `IS_ACTIVE`=?, `LAST_UPDATED_DATETIME`=? , `LAST_UPDATED_BY`= ? WHERE `TT_DTL_ID`=? ";
			
			resp = this.jdbcTemplate.update(qry,activityName,plannedDurationDays,isActive,lastUpdatedDateTime,lastUpdatedBy,ttDtlId);
			
		}catch(Exception ex) {
			logger.error("updateTaskTemplateDtl Method Exception "+ex);
		}
		return resp;
	}

	@Override
	public int deleteTemplateHdrAndDtl(String ttDtlId, String tenantId) {
		int deleteDtl = 0;
		try {
			String dtlQry = "delete from task_template_dtl where TT_DTL_ID=? and  TENANT_ID=?";
			deleteDtl = this.jdbcTemplate.update(dtlQry,ttDtlId,tenantId);
		} catch (Exception ex) {
			logger.error("deleteTemplateHdrAndDtl method Error" + ex);
		}
		return deleteDtl;
	}

	@Override
	public int CheckTtTempDtl(String ttHdrId, String tenantId) {
		int ChcktdId = 0;
		int delRec = 0;
		try {
			String ChckQuery = "SELECT COUNT(TT_HDR_ID) as COUNT FROM task_template_dtl WHERE TT_HDR_ID =? and  TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChckQuery, ttHdrId,tenantId);
			ChcktdId = Integer.parseInt(resultMap.get("COUNT").toString());
			if( ChcktdId == 0) {
				String delQry = "DELETE FROM task_template_hdr WHERE TT_HDR_ID = ? and  TENANT_ID=?";
				delRec = this.jdbcTemplate.update(delQry,ttHdrId,tenantId);
			}			
		}catch (Exception ex) {
			logger.error("CheckTtTempDtl method Error" + ex);
		}
		return delRec;
	}

	public List<DocumentStatusMstEntity> getNextSeqandStatusByDoc(int currentSeq, String docType, String tenantId,
			String docGrp, String processDoc) {
		String NEXT_SEQ = "";
		List<DocumentStatusMstEntity> getLi = new ArrayList<DocumentStatusMstEntity>();
		try {

			String nullQry = "select case when NEXT_SEQ is not null then 1 else 0 end as NEXT_SEQ from document_lifecycle_mst where DOC_TYPE=? and TENANT_ID=? and CURR_SEQUENCE =? and  DOC_GROUP = ? and PROCESS_CODE =?";
			Map<String, Object> result = jdbcTemplate.queryForMap(nullQry,docType,tenantId,currentSeq,docGrp,processDoc);
			int notNull = Integer.parseInt(result.get("NEXT_SEQ").toString());
			if (notNull==1) {
				String qry = "select NEXT_SEQ from document_lifecycle_mst where DOC_TYPE=? and TENANT_ID=? and CURR_SEQUENCE =? and  DOC_GROUP = ? and PROCESS_CODE=?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,docType,tenantId,currentSeq,docGrp,processDoc);
				NEXT_SEQ = resultMap.get("NEXT_SEQ").toString();
	
				qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='" + tenantId
						+ "' and CURR_SEQUENCE ='" + NEXT_SEQ + "' and  DOC_GROUP =  '" + docGrp + "' and PROCESS_CODE='"+processDoc+"'";
				getLi = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());
			}
		} catch (Exception ex) {
			logger.error("getNextSeqandStatusByDoc Error" + ex);
		}
		return getLi;

	}

}
