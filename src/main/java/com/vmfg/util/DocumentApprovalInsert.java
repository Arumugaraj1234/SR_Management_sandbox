package com.vmfg.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DocumentApprovalInsert {

	private static final Logger logger = LoggerFactory.getLogger(DocumentApprovalInsert.class);

	public static String InsertDocApprovalDtls(String pmId, String docTypeCode, String refId, String pmHdrId,
			String tenatId, String employeeId, String designation, String enqId,String refCode, JdbcTemplate jdbctemp) {
		String response = "", DesCode = "";
		int insertHdrId = 0;
		try {
			logger.info("InsertDocApprovalDtls starts");
			if (employeeId != null && !employeeId.equalsIgnoreCase("") && !employeeId.isEmpty()) {
				String DesCodeQry = "SELECT \r\n" + 
						"    GROUP_CONCAT(mst.DESIGNATION_CODE) AS DESIGNATION_CODE\r\n" + 
						"FROM\r\n" + 
						"    employee_mst mst\r\n" + 
						"WHERE\r\n" + 
						"    FIND_IN_SET(mst.EMPLOYEE_ID,?); ";
				Map<String, Object> resultMap = jdbctemp.queryForMap(DesCodeQry,employeeId);
				DesCode = resultMap.get("DESIGNATION_CODE").toString();
			}

//			if ( designation == null || designation.isEmpty()) {
//				designation = DesCode;
//			}
			
			Map<String, Object> resultMap;
			if(pmHdrId!=null && enqId!=null && !pmHdrId.isEmpty()){
				String dupCheck = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN DN_APP_ID\r\n"
						+ "        ELSE 0\r\n" + "    END AS DN_APP_ID\r\n" + "FROM\r\n" + "    document_approve_hdr\r\n"
						+ "WHERE\r\n" + "   (PM_HDR_ID = ? || ENQUIRY_ID=?)  and  DOC_TYPE_CODE = ? and REFERENCE_ID=? and TENANT_ID=? ; ";
				
				 resultMap = jdbctemp.queryForMap(dupCheck, pmHdrId,enqId,docTypeCode,refId,tenatId);
			}else {
				String dupCheck = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN DN_APP_ID\r\n"
						+ "        ELSE 0\r\n" + "    END AS DN_APP_ID\r\n" + "FROM\r\n" + "    document_approve_hdr\r\n"
						+ "WHERE\r\n" + " DOC_TYPE_CODE = ? and REFERENCE_ID=? and TENANT_ID=?; ";
				
				 resultMap = jdbctemp.queryForMap(dupCheck,docTypeCode,refId,tenatId);
			}
		
			insertHdrId = Integer.parseInt(resultMap.get("DN_APP_ID").toString());
			logger.info("DocumentApprovalInsert insertHdrId "+insertHdrId);
			if (insertHdrId == 0) {
				String insertHdr = "INSERT INTO document_approve_hdr\r\n" + "(\r\n" + "`PM_ID`,\r\n"
						+ "`DOC_TYPE_CODE`,\r\n" + "`REFERENCE_ID`,\r\n" + "`PM_HDR_ID`,\r\n"
						+ "`TENANT_ID`,ENQUIRY_ID,REFERENCE_CODE,LAST_UPDATED_DATETIME)\r\n" + "VALUES\r\n" + "(?,?,?,?,?,?,?,?);";

				KeyHolder holder = new GeneratedKeyHolder();

				jdbctemp.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertHdr, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, pmId);
						ps.setString(2, docTypeCode);
						ps.setString(3, refId);
						ps.setString(4, pmHdrId);
						ps.setString(5, tenatId);
						ps.setString(6, enqId);
						ps.setString(7, refCode);
						ps.setString(8, CommonMethod.getCurrentDateTime());
						return ps;
					}

				}, holder);
				insertHdrId = holder.getKey().intValue();
				if (designation != null && !designation.isEmpty() ) {
				String desArr[] = designation.split(",");
					for (int i = 0; i < desArr.length; i++) {
						
							String getPmIdForDesign="SELECT \r\n" + 
									"    proj.PM_ID as PM_ID\r\n" + 
									"FROM\r\n" + 
									"    project_wbs_initiation_mst proj\r\n" + 
									"        INNER JOIN\r\n" + 
									"    employee_designation emp ON proj.DEPARTMENT_CODE = emp.DEPARTMENT_CODE\r\n" + 
									"WHERE\r\n" + 
									"    emp.DESIGNATION_CODE = ?\r\n" + 
									"        AND emp.TENANT_ID = ?";
							
							Map<String, Object> result = jdbctemp.queryForMap(getPmIdForDesign,desArr[i],tenatId);
							String pmIdForDept= result.get("PM_ID").toString();

							String insertDtlQry = "INSERT INTO document_approve_dtl\r\n" + "(\r\n" + "`DN_APP_ID`,\r\n"
									+ "`DESIGNATION`,\r\n" + "`TENANT_ID`,`INSERTED_DATETIME`,PM_ID)\r\n" + "VALUES\r\n" + "(?,?,?,?,?);";
							int insVal = jdbctemp.update(insertDtlQry, insertHdrId, desArr[i], tenatId,CommonMethod.getCurrentDateTime(),pmIdForDept);
							if (insVal > 0) {
								response = "Success";
							}
						}
					} else {
						String insertDtlQry = "INSERT INTO document_approve_dtl\r\n" + "(\r\n" + "`DN_APP_ID`,\r\n"
								+ "`DESIGNATION`,\r\n" + "`TENANT_ID`,`INSERTED_DATETIME`)\r\n" + "VALUES\r\n" + "(?,?,?,?);";
						int insVal = jdbctemp.update(insertDtlQry, insertHdrId, designation, tenatId,CommonMethod.getCurrentDateTime());
						if (insVal > 0) {
							response = "Success";
						}
					}
//				}

			} else {
				String updateHdrQry = "UPDATE document_approve_hdr \r\n" + 
						"SET \r\n" + 
						"    PM_HDR_ID = ?,\r\n" + 
						"    TENANT_ID = ?,"
						+" LAST_UPDATED_DATETIME=?\r\n" + 
						"WHERE\r\n" + 
						"    REFERENCE_ID = ?\r\n" + 
						"        AND (PM_HDR_ID = ? || ENQUIRY_ID = ?)\r\n" + 
						"        AND PM_ID = ?\r\n" + 
						"        AND DOC_TYPE_CODE = ?";
				jdbctemp.update(updateHdrQry, pmHdrId, tenatId,CommonMethod.getCurrentDateTime(), refId, pmHdrId, enqId,pmId, docTypeCode);

				String dtlQry = "DELETE FROM `document_approve_dtl` WHERE `DN_APP_ID`=?;";
				int delDtl = jdbctemp.update(dtlQry,insertHdrId);

				if (delDtl > 0) {
					if (designation != null && !designation.isEmpty()) {
						String desArr[] = designation.split(",");
						for (int i = 0; i < desArr.length; i++) {
							
							String getPmIdForDesign="SELECT \r\n" + 
									"    proj.PM_ID as PM_ID\r\n" + 
									"FROM\r\n" + 
									"    project_wbs_initiation_mst proj\r\n" + 
									"        INNER JOIN\r\n" + 
									"    employee_designation emp ON proj.DEPARTMENT_CODE = emp.DEPARTMENT_CODE\r\n" + 
									"WHERE\r\n" + 
									"    emp.DESIGNATION_CODE = ?\r\n" + 
									"        AND emp.TENANT_ID = ?";
							
							Map<String, Object> result = jdbctemp.queryForMap(getPmIdForDesign,desArr[i],tenatId);
							String pmIdForDept= result.get("PM_ID").toString();
							
							String insertDtlQry = "INSERT INTO document_approve_dtl\r\n" + "(\r\n" + "`DN_APP_ID`,\r\n"
									+ "`DESIGNATION`,\r\n" + "`TENANT_ID`,`INSERTED_DATETIME`,PM_ID)\r\n" + "VALUES\r\n" + "(?,?,?,?,?);";
							int insVal = jdbctemp.update(insertDtlQry, insertHdrId, desArr[i], tenatId,CommonMethod.getCurrentDateTime(),pmIdForDept);
							if (insVal > 0) {
								response = "Success";
							}

						}

					} else {
						String insertDtlQry = "INSERT INTO document_approve_dtl\r\n" + "(\r\n" + "`DN_APP_ID`,\r\n"
								+ "`DESIGNATION`,\r\n" + "`TENANT_ID`,`INSERTED_DATETIME`)\r\n" + "VALUES\r\n" + "(?,?,?,?);";
						int insVal = jdbctemp.update(insertDtlQry, insertHdrId, designation, tenatId,CommonMethod.getCurrentDateTime());
						if (insVal > 0) {
							response = "Success";
						}
					}
				}

			}
		} catch (Exception ex) {
			logger.error("DocumentApprovalInsert error " + ex);
		}
		logger.info("InsertDocApprovalDtls end");

		return response;
	}

}
