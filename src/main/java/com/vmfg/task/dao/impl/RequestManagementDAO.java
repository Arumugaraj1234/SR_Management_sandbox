package com.vmfg.task.dao.impl;

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

import com.vmfg.task.dao.interfaces.IRequestManagementDAO;
import com.vmfg.task.entity.GetAllCountByEmpIdEntity;
import com.vmfg.task.entity.GetRemarksDtlEntity;
import com.vmfg.task.entity.GetReqManHdrDtlEntity;
import com.vmfg.task.entity.GetRequestCategoryEntity;
import com.vmfg.task.entity.GetStatusRemarksDtlEntity;
import com.vmfg.task.rowmapper.GetRemarksDtlRowMapper;
import com.vmfg.task.rowmapper.GetReqManHdrDtlRowMapper;
import com.vmfg.task.rowmapper.GetRequestCategoryRowMapper;
import com.vmfg.task.rowmapper.GetStatusRemarksDtlRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class RequestManagementDAO implements IRequestManagementDAO {
	private static final Logger logger = LoggerFactory.getLogger(RequestManagementDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insertReqManagementHdr(String pmHdrId, String remarks, String reqCategory, String requstedDate,
			String reqDesc, String reqName, String requestedBy, String requestedByDept, String requestedTo,
			String requestedToDept, String ticketReporter, String tenantId, String seqNo, String seqStatus,String dueDate) {
		int id = 0;
		String isCompleted = "0";
		try {
			//ticketReporter
			String reporter=GetPropertyValue.getPropValue("TICKET_REPORTER", tenantId, jdbcTemplate);
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO req_man_hdr ( PM_HDR_ID, REQUEST_CATEGORY, REQUEST_NAME, REQUEST_DESCRIPTION, REQUESTED_BY, REQUESTED_BY_DEPT,"
						+ " REQUESTED_TO, REQUESTED_TO_DEPT, TICKET_REPORTER,REQUESTED_DATE, CLOSED_DATE,DUE_DATE,REQUESTED_DATETIME, SEQUENCE_NO, SEQUENCE_STATUS, IS_COMPLETED,"
						+ " TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, pmHdrId);
					ps.setString(2, reqCategory);
					ps.setString(3, reqName);
					ps.setString(4, reqDesc);
					ps.setString(5, requestedBy);
					ps.setString(6, requestedByDept);
					ps.setString(7, requestedTo);
					ps.setString(8, requestedToDept);
					ps.setString(9, reporter);
					ps.setString(10, CommonMethod.getCurrentDate());
					ps.setString(11, null);
					ps.setString(12, dueDate);
					ps.setString(13,  CommonMethod.getCurrentDateTime());
					ps.setString(14, seqNo);
					ps.setString(15, seqStatus);
					ps.setString(16, isCompleted);
					ps.setString(17, tenantId);

					return ps;
				}
			}, holder);

			id = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertReqManagementHdr method Error" + ex);
		}
		return id;

	}

	@Override
	public int insertReqRemarks(int insertHdr, String remarksBy, String remarks, String tenantId) {
		int id = 0;
		try {
			String insrtQry = "INSERT INTO req_remarks (RQ_ID, REMARKS_BY, REMARKS,REQUESTED_DATETIME,TENANT_ID) VALUES (?,?,?,NOW(),?)";
			id = this.jdbcTemplate.update(insrtQry, insertHdr, remarksBy, remarks, tenantId);

		} catch (Exception ex) {
			logger.error("insertReqRemarks method Error" + ex);
		}
		return id;

	}

	@Override
	public int insertReqManStatusDtl(int insertHdr, String seqNo, String seqStatus, String empId, String remarks,
			String tenantId) {
		int id = 0;
		try {
			String insrtQry = "INSERT INTO req_man_status_dtl ( RQ_ID, SEQUENCE, STATUS_TYPE_CODE, EMPLOYEE_ID, REMARKS, TENANT_ID) VALUES (?,?,?,?,?,?)";
			id = this.jdbcTemplate.update(insrtQry, insertHdr, seqNo, seqStatus, empId, remarks, tenantId);

		} catch (Exception ex) {
			logger.error("insertReqManStatusDtl method Error" + ex);
		}
		return id;

	}

	@Override
	public List<GetStatusRemarksDtlEntity> getStatusRemarksDtl(String rqId, String tenantId) {

		List<GetStatusRemarksDtlEntity> dtlList = new ArrayList<>();
		try {

			String qry = "SELECT \r\n" + "    rmsd.REMARKS,\r\n" + "    em.EMPLOYEE_FIRSTNAME,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "FROM\r\n" + "    req_man_status_dtl rmsd,\r\n" + "    employee_mst em,\r\n"
					+ "    document_status_type_code dstc\r\n" + "WHERE\r\n"
					+ "    rmsd.STATUS_TYPE_CODE = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmsd.EMPLOYEE_ID = em.EMPLOYEE_ID\r\n" + "        AND rmsd.RQ_ID = '" + rqId
					+ "'\r\n" + "        AND rmsd.TENANT_ID = '" + tenantId + "'";
			dtlList = this.jdbcTemplate.query(qry, new GetStatusRemarksDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getStatusRemarksDtl Error " + ex);
		}
		return dtlList;
	}

	@Override
	public int insertReqStatus(String empId, String rqId, String seqNo, String seqStatus, String statusRemarks,
			String tenantId) {
		int id = 0;
		try {
			String insrtQry = "INSERT INTO req_man_status_dtl ( RQ_ID, SEQUENCE, STATUS_TYPE_CODE, EMPLOYEE_ID, REMARKS, TENANT_ID) VALUES (?,?,?,?,?,?)";
			id = this.jdbcTemplate.update(insrtQry, rqId, seqNo, seqStatus, empId, statusRemarks, tenantId);

		} catch (Exception ex) {
			logger.error("insertReqStatus method Error" + ex);
		}
		return id;

	}

	@Override
	public List<GetRequestCategoryEntity> getRequestCategory(String tenantId) {
		List<GetRequestCategoryEntity> categoryList = new ArrayList<>();
		try {

			String qry = " select REQ_CAT_DESC,REQ_CATE_ID from request_category where TENANT_ID='" + tenantId + "'";
			categoryList = this.jdbcTemplate.query(qry, new GetRequestCategoryRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestCategory Error " + ex);
		}
		return categoryList;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getReqManHdrDtl(String pmHdrId, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {
			String pm = "AND rmh.PM_HDR_ID = '"+pmHdrId+"'";
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pm =  "AND rmh.PM_HDR_ID like '%%'";
			}
			

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+pm
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestCategory Error " + ex);
		}
		return hdrList;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getReqManHdrAndStatusAndRemarks(String rqId, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME\r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.RQ_ID = '"+rqId+"'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestCategory Error " + ex);
		}
		return hdrList;
	}

	@Override
	public List<GetRemarksDtlEntity> getRemarksDtl(String rqId, String tenantId) {
		List<GetRemarksDtlEntity> list = new ArrayList<>();
		try {

			String qry = "SELECT \r\n"
					+ "    remark.RQ_DTL_ID,\r\n"
					+ "    remark.REMARKS,\r\n"
					+ "    remark.RQ_ID,\r\n"
					+ "    remark.REQUESTED_DATETIME,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME\r\n"
					+ "FROM\r\n"
					+ "    req_remarks remark,\r\n"
					+ "    employee_mst em\r\n"
					+ "WHERE\r\n"
					+ "    em.EMPLOYEE_ID = remark.REMARKS_BY\r\n"
					+ "        AND remark.RQ_ID='"+rqId+"'\r\n"
					+ "        AND remark.TENANT_ID='"+tenantId+"'";
			list = this.jdbcTemplate.query(qry, new GetRemarksDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRemarksDtl Error " + ex);
		}
		return list;
	}

	@Override
	public void updateClosedDateInHdr(String rqId,String closedDate,int isComplete, int isClosed) {
		logger.debug("updateClosedDateInHdr   method Start");
		String seq="";
		String seqCode="";
		String deleteDtlQ="";
		seq="2";
		seqCode="DS119";
		try {
//			if(isClosed==1) {
//				
//				 deleteDtlQ = "UPDATE  req_man_hdr SET SEQUENCE_NO='"+seq+"',SEQUENCE_STATUS='"+seqCode+"',IS_COMPLETED='"+isComplete+"' WHERE RQ_ID = '" + rqId + "'";
//			}else if(isComplete==1) {
//				seq="3";
//				seqCode="DS012";
//				 
//			}
			deleteDtlQ = "UPDATE  req_man_hdr SET CLOSED_DATE=?,SEQUENCE_NO=?,SEQUENCE_STATUS=?,IS_COMPLETED=? WHERE RQ_ID = ?;";

			
			this.jdbcTemplate.update(deleteDtlQ,closedDate,seq,seqCode,isComplete,rqId);

		} catch (Exception ex) {
			logger.error("updateClosedDateInHdr  method  exception" + ex);
		}
		logger.debug("updateClosedDateInHdr   method end");

	}

	@Override
	public GetAllCountByEmpIdEntity getAllCounts(String empId, String tenantId) {
		GetAllCountByEmpIdEntity objCount = new GetAllCountByEmpIdEntity();
		int requestedByCount=0,reqByWithCompletedCount=0,reqToCount=0;
	//	ReqToCountWithDeptName reqToObjList=new ReqToCountWithDeptName();
		try {

			String deptCodeStr = "select DEPARTMENT_CODE from employee_mst where EMPLOYEE_ID = ? ";
			Map<String, Object> deptCodeStrMap = jdbcTemplate.queryForMap(deptCodeStr, empId);
			String deptCode = deptCodeStrMap.get("DEPARTMENT_CODE").toString();
			//count(*) of REQUESTED_BY = emp id from UI  ( Assigned By Me)
			String requestedByCountQ = "select CASE WHEN count(REQUESTED_BY)>0 THEN count(REQUESTED_BY) ELSE 0 END REQUESTED_BY from req_man_hdr where REQUESTED_BY=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(requestedByCountQ, empId,tenantId);
			requestedByCount = Integer.parseInt(resultMap.get("REQUESTED_BY").toString());
logger.info("requestedByCount "+requestedByCount);
			//coun(*)  of REQUESTED_BY  and IS_COMPLETED =1 (Task Completed By Others)
			String reqByWithCompletedCountQ="select CASE WHEN count(REQUESTED_BY)>0 THEN count(REQUESTED_BY) ELSE 0 END REQUESTED_BY from req_man_hdr where REQUESTED_BY=? and TENANT_ID=? and IS_COMPLETED='1'";
			Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(reqByWithCompletedCountQ, empId,tenantId);
			reqByWithCompletedCount = Integer.parseInt(resultMap1.get("REQUESTED_BY").toString());
			logger.info("reqByWithCompletedCount "+reqByWithCompletedCount);

			//get the dept for the emp id and get count(*) of REQUESTED_TO_DEPT   ( Assigned To Dept)
			String reqToObjQ = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(REQUESTED_TO_DEPT) > 0 THEN COUNT(REQUESTED_TO_DEPT)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END REQUESTED_TO_DEPT\r\n" + 
					"FROM\r\n" + 
					"    req_man_hdr rmh,\r\n" + 
					"    department dept\r\n" + 
					"WHERE\r\n" + 
					"    rmh.REQUESTED_TO_DEPT = dept.DEPARTMENT_CODE\r\n" + 
					"        AND rmh.REQUESTED_TO_DEPT = ?\r\n" + 
					"        AND rmh.TENANT_ID = ?;";
			Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(reqToObjQ, deptCode,tenantId);
			int reqdepCount  = Integer.parseInt(resultMap2.get("REQUESTED_TO_DEPT").toString());
			logger.info("reqdepCount "+reqdepCount);

			//count(*) - >REQUESTED_TO ( Assigned to Me)
			String reqToCountQ=" SELECT \r\n"
					+ "CASE WHEN count(REQUESTED_TO)>0 THEN count(REQUESTED_TO) ELSE 0 END REQUESTED_TO\r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr\r\n"
					+ "WHERE\r\n"
					+ " REQUESTED_TO = ?\r\n"
					+ " AND TENANT_ID = ?";
			Map<String, Object> resultMap3 = jdbcTemplate.queryForMap(reqToCountQ, empId,tenantId);
			reqToCount  = Integer.parseInt(resultMap3.get("REQUESTED_TO").toString());
			logger.info("reqToCount "+reqToCount);


			objCount.setRequestedByCount(String.valueOf(requestedByCount));
			objCount.setReqByWithCompletedCount(String.valueOf(reqByWithCompletedCount));
			objCount.setReqToCount(String.valueOf(reqToCount));
			objCount.setReqToDeptCount(String.valueOf(reqdepCount));
		} catch (Exception ex) {
			logger.error("getAllCounts Error " + ex);
		}
		return objCount;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getRequestedToDtl(String empId, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.REQUESTED_TO like '"+empId+"'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestedToDtl Error " + ex);
		}
		return hdrList;
	}
	
	@Override
	public List<GetReqManHdrDtlEntity> getRequestAssignedTo(String empId, String tenantId,String dept,String fromDate,String toDate) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.REQUESTED_TO like '"+empId+"'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"' AND rmh.REQUESTED_TO_DEPT='"+dept+"'"
					+ "        AND rmh.DUE_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestedToDtl Error " + ex);
		}
		return hdrList;
	}
	
	@Override
	public List<GetReqManHdrDtlEntity> getRequestAssignedFrom(String empId, String tenantId,String dept,String fromDate,String toDate) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n" + 
					"    rmh.*,\r\n" + 
					"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
					"    ph.PROJECT_DESCRIPTION,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n" + 
					"    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n" + 
					"    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n" + 
					"    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n" + 
					"    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n" + 
					"    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n" + 
					"    dept.DEPARTMENT_NAME AS REQUESTED_BY_DEPT_NAME,\r\n" + 
					"    dept1.DEPARTMENT_NAME AS REQUESTED_TO_DEPT_NAME,\r\n" + 
					"    rc.REQ_CAT_DESC AS REQ_CAT_DESC,\r\n" + 
					"    ph.PROJECT_NAME,\r\n" + 
					"    ph.PROJECT_CODE\r\n" + 
					"FROM\r\n" + 
					"    req_man_hdr rmh,\r\n" + 
					"    employee_mst em,\r\n" + 
					"    employee_mst em1,\r\n" + 
					"    employee_mst em2,\r\n" + 
					"    project_hdr ph,\r\n" + 
					"    document_status_type_code dstc,\r\n" + 
					"    department dept,\r\n" + 
					"    department dept1,\r\n" + 
					"    request_category rc\r\n" + 
					"WHERE\r\n" + 
					"    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n" + 
					"        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n" + 
					"        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n" + 
					"        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n" + 
					"        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n" + 
					"        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n" + 
					"        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n" + 
					"        AND rmh.REQUESTED_BY LIKE '"+empId+"'\r\n" + 
					"        AND rmh.REQUESTED_BY_DEPT = '"+dept+"'\r\n" + 
					"        AND rmh.TENANT_ID = '"+tenantId+"'\r\n" + 
				    "        AND rmh.DUE_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'"+
					"        AND rmh.RQ_ID NOT IN (SELECT \r\n" + 
					"            rmh.RQ_ID\r\n" + 
					"        FROM\r\n" + 
					"            req_man_hdr rmh\r\n" + 
					"        WHERE\r\n" + 
					"            rmh.REQUESTED_TO LIKE '"+empId+"'\r\n" + 
					"                AND rmh.REQUESTED_TO_DEPT = '"+dept+"'\r\n" + 
					"                AND rmh.TENANT_ID = '"+tenantId+"')";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestedToDtl Error " + ex);
		}
		return hdrList;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getRequestedByDtl(String empId, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.REQUESTED_BY = '"+empId+"'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestedByDtl Error " + ex);
		}
		return hdrList;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getRequestedByDtlWithIsComplete(String empId, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc\r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.REQUESTED_BY = '"+empId+"'\r\n"
					+ "        AND rmh.IS_COMPLETED = '1'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestedByDtlWithIsComplete Error " + ex);
		}
		return hdrList;
	}

	@Override
	public List<GetReqManHdrDtlEntity> getRequestToWithAllDepartment(String deptCode, String tenantId) {
		List<GetReqManHdrDtlEntity> hdrList = new ArrayList<>();
		
		try {

			String qry = " SELECT \r\n"
					+ "    rmh.*,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    ph.PROJECT_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS REQUESTED_BY_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS REQUESTED_TO_NAME,\r\n"
					+ "    em2.EMPLOYEE_FIRSTNAME AS TICKET_REPORTER_NAME,\r\n"
					+ "    em.EMPLOYEE_ID AS REQUESTED_BY_ID,\r\n"
					+ "    em1.EMPLOYEE_ID AS REQUESTED_TO_ID,\r\n"
					+ "    em2.EMPLOYEE_ID AS TICKET_REPORTER_ID,\r\n"
					+ "    dept.DEPARTMENT_NAME as REQUESTED_BY_DEPT_NAME,\r\n"
					+ "    dept1.DEPARTMENT_NAME as REQUESTED_TO_DEPT_NAME,\r\n"
					+ "    rc.REQ_CAT_DESC as REQ_CAT_DESC,\r\n"
					+ "    ph.PROJECT_NAME,ph.PROJECT_CODE \r\n"
					
					+ "FROM\r\n"
					+ "    req_man_hdr rmh,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    employee_mst em1,\r\n"
					+ "    employee_mst em2,\r\n"
					+ "    project_hdr ph,\r\n"
					+ "    document_status_type_code dstc,\r\n"
					+ "    department dept,\r\n"
					+ "    department dept1,\r\n"
					+ "    request_category rc \r\n"
					+ "WHERE\r\n"
					+ "    rmh.SEQUENCE_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND rmh.REQUESTED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUESTED_TO = em1.EMPLOYEE_ID\r\n"
					+ "        AND rmh.TICKET_REPORTER = em2.EMPLOYEE_ID\r\n"
					+ "        AND rmh.REQUEST_CATEGORY = rc.REQ_CATE_ID\r\n"
					+ "        AND dept.DEPARTMENT_CODE = rmh.REQUESTED_BY_DEPT\r\n"
					+ "        AND dept1.DEPARTMENT_CODE = rmh.REQUESTED_TO_DEPT\r\n"
					+ "        AND ph.PM_HDR_ID = rmh.PM_HDR_ID\r\n"
					+ "        AND rmh.REQUESTED_TO_DEPT = '"+deptCode+"'\r\n"
					+ "        AND rmh.TENANT_ID = '"+tenantId+"'";
			hdrList = this.jdbcTemplate.query(qry, new GetReqManHdrDtlRowMapper());
		} catch (Exception ex) {
			logger.error("getRequestToWithAllDepartment Error " + ex);
		}
		return hdrList;
	}

	@Override
	public String getdepartCode(String empId, String tenantId) {
		String depCode="";
		try {
			String countQ="SELECT \r\n"
					+ "    COUNT(REQUESTED_TO_DEPT) as COUNT\r\n"
					+ "FROM\r\n"
					+ "    req_man_hdr\r\n"
					+ "WHERE\r\n"
					+ " REQUESTED_TO = ? AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countQ, empId,tenantId);
			int	count = Integer.parseInt(resultMap.get("COUNT").toString());

			if(count>0) {
				String deptCodeQ = "  SELECT \r\n"
						+ "  distinct  REQUESTED_TO_DEPT\r\n"
						+ "FROM\r\n"
						+ "    req_man_hdr\r\n"
						+ "WHERE\r\n"
						+ "REQUESTED_TO = ? AND TENANT_ID = ?";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(deptCodeQ, empId,tenantId);
				depCode =resultMap1.get("REQUESTED_TO_DEPT").toString();
			}
		} catch (Exception ex) {
			logger.error("getRequestToWithAllDepartment Error " + ex);
		}
		return depCode;
	}

}
