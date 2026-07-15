package com.vmfg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.entity.ProcessAssignedTeamEntity;
import com.vmfg.general.rowmapper.ProcessAssignedTeamRowMapper;
import com.vmfg.scm.dao.impl.PoDAO;

@Transactional
@Repository
public class NotificationRequest {

	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	DepartmentAndEmployeeDAO departmentAndEmployeeDAO;
	
	@Autowired
	PoDAO poDAO;

	private static final Logger logger = LoggerFactory.getLogger(NotificationRequest.class);

	private static final Map<Integer, String> REQUEST_TYPE = new HashMap<>();
	private static final Map<Integer, String> REQUEST_MSG = new HashMap<>();

	static {
		REQUEST_TYPE.put(1, "Action");
		REQUEST_TYPE.put(2, "Notify");
		
		
		REQUEST_MSG.put(1, "Replace1 has been created");
		REQUEST_MSG.put(2, "Replace1 has been updated");
		REQUEST_MSG.put(3, "Replace1 - Replace2 has been uploaded");
		REQUEST_MSG.put(4, "Replace1 - Replace2 has been added to the team");
		REQUEST_MSG.put(5, "Replace1 - Replace2 has been removed from the team");
		REQUEST_MSG.put(6, "Replace1 - Replace2 has been approved");
		REQUEST_MSG.put(7, "Replace1 - Budget sheet has been created");
		REQUEST_MSG.put(8, "Replace1 - Replace2 has been updated as Replace3");
		REQUEST_MSG.put(9, "Replace1 - Project has been initated");
		REQUEST_MSG.put(10, "Replace1 - Station has been updated");
		REQUEST_MSG.put(11, "Replace1 - Replace2 Budget has been updated");
		REQUEST_MSG.put(13, "Request raised in Project Replace1 by Replace2 ");
		REQUEST_MSG.put(14, "Request closed in Project Replace1 ");
//		REQUEST_MSG.put(15, "Replace1 - Access enabled for  Replace2 document");
		REQUEST_MSG.put(15, "Replace1 - Document access has been given.");
		REQUEST_MSG.put(21, "Replace1 - New task has been allocated");
		REQUEST_MSG.put(22, "Replace1 - Task to be Replace2");
		REQUEST_MSG.put(23, "QC Inspection request raised for PO: Replace1 , Project: Replace2 , Part Number : Replace3 ");
		REQUEST_MSG.put(24, "Material Inward - PO: Replace1 , Project: Replace2");
		REQUEST_MSG.put(25, "Concession Note raised - PO: Replace1 , Project: Replace2");
		REQUEST_MSG.put(26, "Project Replace1 - Concession Note raised by Quality Team");
		REQUEST_MSG.put(27, "PJS for Indent Code:Replace1 to be approved");
		
		
		REQUEST_MSG.put(41, "Indent Code : Replace1 to be approved for Project ");
		REQUEST_MSG.put(42, "Indent Code : Replace1 Indent has been created  for Project ");
		REQUEST_MSG.put(43, "Indent Code : Replace1 Budget Excess has been created");
		REQUEST_MSG.put(44, "Indent Code : Replace1 to be approved");
		REQUEST_MSG.put(45, "Indent Code : Replace1 closed");
		REQUEST_MSG.put(46, "Replace1 - to be approved");
		REQUEST_MSG.put(47, "Replace1 - Approved");
		REQUEST_MSG.put(48, "Replace1 - Cancelled");
		REQUEST_MSG.put(49, "New Indent Creation for the Project Replace1 has been paused");
		REQUEST_MSG.put(50, "New Indent Creation for the Project Replace1 has been resumed");
		REQUEST_MSG.put(51, "New MR Creation for the Project Replace1 has been resumed");
		REQUEST_MSG.put(52, "New MR Creation for the Project Replace1 has been paused");
		REQUEST_MSG.put(53, "Replace1 - Inspection Raised");
		REQUEST_MSG.put(54, "Replace1 - Inspection Completed");
		REQUEST_MSG.put(55,"New PRA Replace1 created in Project Replace2 By Replace3 for PO Replace4");
		REQUEST_MSG.put(56,"PRA Replace1 for PO Replace2 in Project Replace3 Approved.");
		REQUEST_MSG.put(57, "QC Inspection created for PO: Replace1 , Project: Replace2 ");
		REQUEST_MSG.put(58, "Concession Note created for PO: Replace1 , Project: Replace2 ");
		REQUEST_MSG.put(59, "Replace1 - Change Request sheet has been created");
		REQUEST_MSG.put(60, "PJS for Indent Code:Replace1 has been Deleted.");
		REQUEST_MSG.put(61, "QC Inspection Completed for PO: Replace1 , Project: Replace2 ");

	
	}

	public static String getRequestType(int number) {
		return REQUEST_TYPE.get(number);
	}

	public static String getRequestMsg(int number) {
		return REQUEST_MSG.get(number);
	}

	public static String notificationreq(int notifyType, int notifyMessage, String empId, String tenantId,
			List<String> messageList, List<String> otherEmpId, String assignTeam, String pmId, String masterId,
			String approveDesig, JdbcTemplate jdbctemp) {
		String response="";
		logger.info("notificationreq start");

		try {

			// if (messageList.size() > 0) {

			// msg = getRequestMsg(notifyMessage);

			// }
			ArrayList<String> empArr = new ArrayList<String>();

			if (approveDesig != null) {
				String[] appdesigArr = approveDesig.split(",");
				for (int w = 0; w < appdesigArr.length; w++) {
					List<ProcessAssignedTeamEntity> list=new ArrayList<ProcessAssignedTeamEntity>();
					
					String getPmIdForDesign="SELECT \r\n" + 
							"    CASE\r\n" + 
							"        WHEN COUNT(*) > 0 THEN proj.PM_ID\r\n" + 
							"        ELSE ''\r\n" + 
							"    END AS PM_ID\r\n" + 
							"FROM\r\n" + 
							"    project_wbs_initiation_mst proj\r\n" + 
							"        INNER JOIN\r\n" + 
							"    employee_designation emp ON proj.DEPARTMENT_CODE = emp.DEPARTMENT_CODE\r\n" + 
							"WHERE\r\n" + 
							"    emp.DESIGNATION_CODE = ?\r\n" + 
							"        AND emp.TENANT_ID = ?";
					
					Map<String, Object> result = jdbctemp.queryForMap(getPmIdForDesign,appdesigArr[w],tenantId);
					String pmIdForDept= result.get("PM_ID").toString();
					
					if(masterId!=null && !masterId.isEmpty() && !pmIdForDept.isEmpty()) {
						String assignDesigEmpStr="SELECT \n"
								+ "    em.EMPLOYEE_FIRSTNAME, em.EMPLOYEE_ID\n"
								+ "FROM\n"
								+ "    process_assigned_team pat\n"
								+ "        INNER JOIN\n"
								+ "    employee_mst AS em ON pat.ASSIGNED_EMP_ID = em.EMPLOYEE_ID\n"
								+ "WHERE\n"
								+ "    em.DESIGNATION_CODE = ? \n"
								+ "        AND pat.MASTER_ID = ? \n"
								+ "        AND pat.PM_ID = ? \n"
								+ "        AND em.TENANT_ID = ? ";
						RowMapper<ProcessAssignedTeamEntity> rowmapper = new ProcessAssignedTeamRowMapper();
						list = jdbctemp.query(assignDesigEmpStr,rowmapper, appdesigArr[w],masterId,pmIdForDept,tenantId);
					}else {
						String assignDesigEmpStr="SELECT \r\n" + 
								"    EMPLOYEE_FIRSTNAME, EMPLOYEE_ID\r\n" + 
								"FROM\r\n" + 
								"    employee_mst \r\n" + 
								"WHERE\r\n" + 
								"    DESIGNATION_CODE = ?\r\n" + 
								"        AND TENANT_ID = ?";
						RowMapper<ProcessAssignedTeamEntity> rowmapper = new ProcessAssignedTeamRowMapper();
						list = jdbctemp.query(assignDesigEmpStr,rowmapper, appdesigArr[w],tenantId);
					}
					
					logger.info("appdesigArr"+ appdesigArr[w] + approveDesig + tenantId);
					

					for (int q = 0; q < list.size(); q++) {
						empArr.add(list.get(q).getEmpId());
					}
				}
			}
			
			if (assignTeam.equalsIgnoreCase("1")) {
				String assignTeamStr = "SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(*) > 0 THEN GROUP_CONCAT(ASSIGNED_EMP_ID)\r\n" + 
						"        ELSE ''\r\n" + 
						"    END AS RESULT\r\n" + 
						"FROM\r\n" + 
						"    process_assigned_team\r\n" + 
						"WHERE\r\n" + 
						"    PM_ID = ? AND MASTER_ID = ?\r\n" + 
						"        AND TENANT_ID = ?\r\n" + 
						"        AND IS_ACTIVE = 1";
				
				Map<String, Object> resultMap = jdbctemp.queryForMap(assignTeamStr,pmId,masterId,tenantId);
				String[] assignEmpList = resultMap.get("RESULT").toString().split(",");
				for (int a = 0; a < assignEmpList.length; a++) {
					if(!assignEmpList[a].equalsIgnoreCase("")) {
						empArr.add(assignEmpList[a]);
					}
				}
			} else {
				if(empId!=null && !empId.isEmpty()) {
					empArr.add(empId);
				}
			}
			for (int p = 0; p < otherEmpId.size(); p++) {
				empArr.add(otherEmpId.get(p));
			}
			Set<String> uniqueEmpSet = empArr.stream()
				    .filter(x -> !x.equals(""))      // Remove empty strings
				    .collect(Collectors.toCollection(LinkedHashSet::new)); // Remove duplicates & maintain order

				if (!uniqueEmpSet.isEmpty()) {
				    for (String employeeListRaw : uniqueEmpSet) {
				        String msg = REQUEST_MSG.get(notifyMessage);

				        for (int o = 0; o < messageList.size(); o++) {
				            if (msg != null) {
				                String replacestr = "Replace" + (o + 1);
				                msg = msg.replaceAll(replacestr, messageList.get(o));
				            } else {
				                msg = messageList.get(o);
				            }
				        }

				        String[] empIds = employeeListRaw.split(",");
				        for (String empIdRaw : empIds) {
				            String employeeId = empIdRaw.trim(); 

					    
					    String checkRecordStr = "SELECT COUNT(*) AS COUNT FROM notification_req " +
					            "WHERE NOTIFICATION_MESSAGE = ? AND NOTIFICATION_TYPE = ? AND EMPLOYEE_ID = ? " +
					            "AND NOTIFIED = 0 AND TENANT_ID = ?";
					    logger.info(getRequestType(notifyType)+"params");
					    Map<String, Object> resultMap = jdbctemp.queryForMap(checkRecordStr, msg, getRequestType(notifyType), employeeId, tenantId);
					    int checkrecord = Integer.parseInt(resultMap.get("COUNT").toString());

					    
					    if (checkrecord == 0) {
					        String insertQ = "INSERT INTO `notification_req` " +
					                "(`NOTIFICATION_TYPE`, `NOTIFICATION_MESSAGE`, `EMPLOYEE_ID`, `NOTIFY_DATETIME`, `NOTIFIED`, `NOTIFICATION_VIEWED_DATETIME`, `TENANT_ID`) " +
					                "VALUES (?, ?, ?, ?, ?, ?, ?)";

					        int insreq = jdbctemp.update(insertQ,
					                getRequestType(notifyType),
					                msg,
					                employeeId,
					                CommonMethod.getCurrentDateTime(),
					                "0",
					                null,
					                tenantId
					        );

					        if (insreq > 0) {
					            response = "Success";
					        }
					    }
					}


				}
			}

		} catch (Exception ex) {
			logger.error(" Exception with notificationreq" + ex.getMessage());
		}
		logger.info("notificationreq end");
		return response;
	}
}
