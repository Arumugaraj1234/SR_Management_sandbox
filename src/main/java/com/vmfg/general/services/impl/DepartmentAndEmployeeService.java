package com.vmfg.general.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.export.dao.impl.DeliveryChallanReportTrackerDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.dao.interfaces.IDepartmentAndEmployeeDAO;
import com.vmfg.general.entity.DepartmentInfoEntity;
import com.vmfg.general.entity.DesignationEntity;
import com.vmfg.general.entity.DocDeptEntity;
import com.vmfg.general.entity.EmpRoleEntity;
import com.vmfg.general.entity.EmpStatusEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.EmployeeForUserDtl;
import com.vmfg.general.entity.EmployeemstdetailsEntity;
import com.vmfg.general.entity.GetAssignTeamDtl;
import com.vmfg.general.entity.ModuleMstEntity;
import com.vmfg.general.entity.ModuleScreenList;
import com.vmfg.general.entity.ProcessAssignedTeamEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.request.DeptByPMRequest;
import com.vmfg.general.request.EmployeeDetailsRequest;
import com.vmfg.general.request.EmployeeTenantRequest;
import com.vmfg.general.request.EmpsalaryRequest;
import com.vmfg.general.request.GetEmployeeByDepartmentRequest;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.request.ProcessAssignedTeamRequest;
import com.vmfg.general.request.RoleMappingRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.IDepartmentAndEmployeeService;
import com.vmfg.sales.dao.impl.EnquiryDAO;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.security.config.WebSecurityConfig;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class DepartmentAndEmployeeService implements IDepartmentAndEmployeeService {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentAndEmployeeService.class);

	@Autowired
	IDepartmentAndEmployeeDAO iDepartmentAndEmployeeDAO;

	@Autowired
	UploadManagementDAO uploadManagementDAO;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Autowired
	IndentUploadDAO indentUploadDAO;

	@Autowired
	EnquiryDAO enquiryDAO;

	@Autowired
	DeliveryChallanReportTrackerDAO deliveryChallanReportTrackerDAO;

	@Autowired
	StageManagementDAO stageManagementDAO;

	@Override
	public List<DepartmentInfoEntity> getDepartmentAndEmpInfo(EmployeeTenantRequest departmentAndEmpInfoRequest) {
		List<DepartmentInfoEntity> departmentInfoEntity = null;
		logger.info("DepartmentAndEmployeeService  method start");
		try {

			String TENANT_ID = departmentAndEmpInfoRequest.getTenantId();
			String IS_ACTIVE = departmentAndEmpInfoRequest.getIsActive();
			String EMPLOYEE_ID = departmentAndEmpInfoRequest.getEmployeID();

			// SERVICES

			if ((null != TENANT_ID && !TENANT_ID.isEmpty()) && !IS_ACTIVE.isEmpty()) {
				departmentInfoEntity = iDepartmentAndEmployeeDAO.getDepartmentInfo(TENANT_ID, IS_ACTIVE, EMPLOYEE_ID);
			}
//			else if(null!=TENANT_ID&&!TENANT_ID.isEmpty() &&!IS_ACTIVE.isEmpty()&&!EMPLOYEE_ID.isEmpty()){
//				departmentInfoEntity=iDepartmentAndEmployeeDAO.getEmpInfo(TENANT_ID,IS_ACTIVE,EMPLOYEE_ID);
//			}

		} catch (Exception ex) {
			logger.error("DepartmentAndEmployeeService  method exception-->" + ex);
		}
		logger.debug("DepartmentAndEmployeeService  method end");
		return departmentInfoEntity;
	}

	@Override
	public GetAssignTeamDtl getprocessAssignedTeam(ProcessAssignedTeamRequest processAssignedTeamRequest) {
		GetAssignTeamDtl list = new GetAssignTeamDtl();
		List<ProcessAssignedTeamEntity> processAssignedTeamEntity = null;
		logger.info("getprocessAssignedTeam  method start");
		try {
			String assignTeam = "";
			String TENANT_ID = processAssignedTeamRequest.getTenantId();
			String REFERENCE_ID = processAssignedTeamRequest.getReferenceId();
			String REFERENCE_DOC = processAssignedTeamRequest.getReferenceDoc();

			// SERVICES

			if ((null != TENANT_ID && !TENANT_ID.isEmpty()) && !REFERENCE_DOC.isEmpty() && !REFERENCE_ID.isEmpty()) {
				processAssignedTeamEntity = iDepartmentAndEmployeeDAO.getprocessAssignedTeam(TENANT_ID, REFERENCE_ID,
						REFERENCE_DOC);
				String desig = uploadManagementDAO.getDesigCodeByEmpId(processAssignedTeamRequest.getEmployeeID(),
						processAssignedTeamRequest.getTenantId());
				assignTeam = iDepartmentAndEmployeeDAO.getPrimaryDocFlagVal(desig, REFERENCE_DOC,processAssignedTeamRequest.getTenantId());
				for(int i =0;i<processAssignedTeamEntity.size();i++) {
					String eachDesig = uploadManagementDAO.getDesigCodeByEmpId(processAssignedTeamEntity.get(i).getEmpId(),
							processAssignedTeamRequest.getTenantId());
			String primarycheck =	iDepartmentAndEmployeeDAO.getPrimaryVal(eachDesig, REFERENCE_DOC);
			processAssignedTeamEntity.get(i).setIsPrimary(primarycheck);
				}
				list.setAssignTeam(assignTeam);
				list.setProcessAssignedTeamEntity(processAssignedTeamEntity);
			}

		} catch (Exception ex) {
			logger.error("getprocessAssignedTeam  method exception-->" + ex);
		}
		logger.debug("getprocessAssignedTeam  method end");
		return list;
	}

	@Override
	public ResponseAsMessage insertProcessAssignedTeam(ProcessAssignedTeamRequest processAssignedTeamRequest) {

		logger.info("insertProcessAssignedTeam  method start");
		ResponseAsMessage returnMsg = null;
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {

			String TENANT_ID = processAssignedTeamRequest.getTenantId();
			String REFERENCE_ID = processAssignedTeamRequest.getReferenceId();
			String pmId = processAssignedTeamRequest.getReferenceDoc();
			String ASSIGNED_EMP_ID = processAssignedTeamRequest.getEmployeeID();

			// SERVICES

			if ((!TENANT_ID.isEmpty()) && !pmId.isEmpty() && !REFERENCE_ID.isEmpty() && !ASSIGNED_EMP_ID.isEmpty()) {
				returnMsg = iDepartmentAndEmployeeDAO.insertProcessAssignedTeam(TENANT_ID, REFERENCE_ID, pmId,
						ASSIGNED_EMP_ID, processAssignedTeamRequest.getProjectId());
				if (returnMsg.getResponseCode().equalsIgnoreCase("200")) {
					messageList.add(enquiryDAO.getsaleEnquiryCode(REFERENCE_ID));
					messageList.add(indentUploadDAO.getEmpNameByEmpId(ASSIGNED_EMP_ID));
					commonNotifyMethod.InvokeNotificationMethod(2, 4, null, TENANT_ID, messageList, otherEmpId, "1",
							pmId, REFERENCE_ID, null);
				}
			}

		} catch (Exception ex) {
			logger.error("insertProcessAssignedTeam  method exception-->" + ex);
		}
		logger.debug("insertProcessAssignedTeam  method end");
		return returnMsg;
	}

	@Override
	public ResponseAsMessage deleteProcessAssignedTeam(ProcessAssignedTeamRequest processAssignedTeamRequest) {
		logger.info("deleteProcessAssignedTeam  method start");
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {

			String TENANT_ID = processAssignedTeamRequest.getTenantId();
			String REFERENCE_ID = processAssignedTeamRequest.getReferenceId();
			String pmId = processAssignedTeamRequest.getReferenceDoc();
			String ASSIGNED_EMP_ID = processAssignedTeamRequest.getEmployeeID();
			// SERVICES
			int checkCount = iDepartmentAndEmployeeDAO.getAssignTeamCount(TENANT_ID, REFERENCE_ID, pmId);
			String desig = uploadManagementDAO.getDesigCodeByEmpId(ASSIGNED_EMP_ID, TENANT_ID);
			int checkAssignMaster = iDepartmentAndEmployeeDAO.getMasterAssignTeam(desig, TENANT_ID);
			if (checkCount > 1 && checkAssignMaster == 0) {
				returnMsg = iDepartmentAndEmployeeDAO.deleteProcessAssignedTeam(TENANT_ID, REFERENCE_ID, pmId,
						ASSIGNED_EMP_ID);
				if (returnMsg.getResponseCode().equalsIgnoreCase("200")) {

					messageList.add(enquiryDAO.getsaleEnquiryCode(REFERENCE_ID));
					messageList.add(indentUploadDAO.getEmpNameByEmpId(ASSIGNED_EMP_ID));
					otherEmpId.add(ASSIGNED_EMP_ID);
					commonNotifyMethod.InvokeNotificationMethod(2, 5, null, TENANT_ID, messageList, otherEmpId, "1",
							pmId, REFERENCE_ID, null);
				}
			} else {
				returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMsg.setResponseDataMessage(ResponseMessageMap.deleteUnSuccessful);
				if (checkAssignMaster > 0) {
					returnMsg.setResponseMessage(ResponseMessageMap.masterTeamMember);
				} else {
					returnMsg.setResponseMessage(ResponseMessageMap.atleastOneTeam);
				}

			}

		} catch (Exception ex) {
			logger.error("deleteProcessAssignedTeam  method exception-->" + ex);
		}
		logger.debug("deleteProcessAssignedTeam  method end");
		return returnMsg;
	}

	@Override
	public List<EmployeeForDepartmentEntity> getEmployeeForDepartment(GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmployeeForDepartment  method start");
		List<EmployeeForDepartmentEntity> returnnMsg = null;
		try {

			String TENANT_ID = deptInfoId.getTenantId();
			String departmentId = deptInfoId.getDepartmentId();

			// SERVICES
			returnnMsg = iDepartmentAndEmployeeDAO.getEmployeeForDepartment(TENANT_ID, departmentId,
					deptInfoId.getEmployeeId());

		} catch (Exception ex) {
			logger.error("getEmployeeForDepartment  method exception-->" + ex);
		}
		logger.debug("getEmployeeForDepartment  method end");
		return returnnMsg;
	}

	@Override
	public List<DepartmentInfoEntity> getDeptForPM(DeptByPMRequest deptInfoId) {
		List<DepartmentInfoEntity> deptinfo = null;
		try {

			deptinfo = iDepartmentAndEmployeeDAO.getDeptForPM(deptInfoId);

		} catch (Exception ex) {
			logger.error("getDeptForPM  method exception-->" + ex);
		}
		return deptinfo;
	}

	@Override
	public ResponseAsList getEmployeeUserDtlForDept(GetEmployeeByDepartmentRequest deptInfoId) {
		ResponseAsList returnL = new ResponseAsList();
		List<EmployeeForUserDtl> empDtl = null;
		try {
			String tenantVal = deliveryChallanReportTrackerDAO.getPropValueByTenant(deptInfoId.getTenantId(),
					"ADMIN_USER");
			int isflag = 0;
			String designation = uploadManagementDAO.getDesigCodeByEmpId(deptInfoId.getEmployeeId(),
					deptInfoId.getTenantId());
			String tenantValarr[] = tenantVal.split(",");
			for (int q = 0; q < tenantValarr.length; q++) {
				if (tenantValarr[q].equalsIgnoreCase(designation)) {
					isflag = isflag + 1;
				}
			}
			if (isflag == 0) {
				String dept = uploadManagementDAO.getDepCodeByEmpId(deptInfoId.getEmployeeId(),
						deptInfoId.getTenantId());
				List<DocDeptEntity> deptDtl = iDepartmentAndEmployeeDAO.getDeptForDesig(dept, deptInfoId.getTenantId(),designation);

				if (deptDtl.size() > 0) {
					String[] arrDeptDtl = deptDtl.get(0).getDeptCode().split(",");
					String dep = "";
					for (int g = 0; g < arrDeptDtl.length; g++) {
						if (g == 0) {
							dep = "'" + arrDeptDtl[g] + "'";
						} else {
							dep = dep + ",'" + arrDeptDtl[g] + "'";
						}
					}

					empDtl = iDepartmentAndEmployeeDAO.getEmployeeUserDtlForDeptByDept(dep, deptInfoId.getTenantId());

				} else {

					empDtl = iDepartmentAndEmployeeDAO.getEmployeeUserDtlForDeptByEmpId(deptInfoId.getEmployeeId(),
							deptInfoId.getTenantId());

				}
			} else {
				empDtl = iDepartmentAndEmployeeDAO.getAllEmployeeUserDtl(deptInfoId.getTenantId());
			}

			if (empDtl.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(empDtl);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getDeptForPM  method exception-->" + ex);
		}
		return returnL;
	}

	@Override
	public ResponseAsList getEmpDesignation(String tenantId) {

		ResponseAsList returnL = new ResponseAsList();
		try {
			List<DesignationEntity> desig = iDepartmentAndEmployeeDAO.getEmpDesignation(tenantId);
			if (desig.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(desig);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getEmpDesignation  method exception-->" + ex);
		}
		return returnL;

	}

	@Override
	public ResponseAsList getEmpStatus(String tenantId) {

		ResponseAsList returnL = new ResponseAsList();
		try {
			List<EmpStatusEntity> desig = iDepartmentAndEmployeeDAO.getEmpStatus(tenantId);
			if (desig.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(desig);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getEmpDesignation  method exception-->" + ex);
		}
		return returnL;

	}

	@Override
	public ResponseAsList getEmpRole(String tenantId) {

		ResponseAsList returnL = new ResponseAsList();
		try {
			List<EmpRoleEntity> desig = iDepartmentAndEmployeeDAO.getEmpRole(tenantId);
			if (desig.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(desig);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getEmpDesignation  method exception-->" + ex);
		}
		return returnL;

	}

	@Override
	public ResponseAsMessage updateEmployeeDtl(EmployeeDetailsRequest empDtl) {
		ResponseAsMessage rm = new ResponseAsMessage();
		WebSecurityConfig ws = new WebSecurityConfig();
		try {
			int empCodeCheck = iDepartmentAndEmployeeDAO.getupdateCodeAndMailCheck("EMPLOYEE_CODE",empDtl.getEmpCode() , empDtl.getEmpId(), empDtl.getTenantId());
			int empmailCheck = iDepartmentAndEmployeeDAO.getupdateCodeAndMailCheck("EMAIL_ID", empDtl.getEmailId(), empDtl.getEmpId(), empDtl.getTenantId());
			
			if(empCodeCheck>0 ||empmailCheck >0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				if(empCodeCheck>0) {
					rm.setResponseMessage(ResponseMessageMap.empIdExist);
				}else {
					rm.setResponseMessage(ResponseMessageMap.empMailIdExist);
				}
				
			}else {
			String empDesignation = "";
			empDesignation = iDepartmentAndEmployeeDAO.checkDesignationExist(empDtl.getDesignation(),
					empDtl.getTenantId(),empDtl.getDepartment());
			if (empDesignation.equalsIgnoreCase("NA")) {
				iDepartmentAndEmployeeDAO.insertDesignation(empDtl.getDesignation(),
						empDtl.getTenantId(),empDtl.getDepartment());
			//	empDesignation = String.valueOf(desig);
				empDesignation = iDepartmentAndEmployeeDAO.checkDesignationExist(empDtl.getDesignation(),
						empDtl.getTenantId(),empDtl.getDepartment());
			}
			empDtl.setDesignation(empDesignation);
			if (!empDtl.getStatus().equalsIgnoreCase("ES001")) {
				String userLoginId = iDepartmentAndEmployeeDAO.getUserLoginId(empDtl.getEmpId());
				iDepartmentAndEmployeeDAO.updateuserLoginAsInactive(userLoginId, "0",empDtl.getEmailId());
				iDepartmentAndEmployeeDAO.updateEmployeeDtl(empDtl);

				String encPassword = ws.passwordEncoder().encode("defaultpwdsrt");
				iDepartmentAndEmployeeDAO.updateEmployeePwd(empDtl.getEmpId(), encPassword);

				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.successCreated);
			} else {

				int userCnt = iDepartmentAndEmployeeDAO.getActiveUser(empDtl.getTenantId());
				String empStat = iDepartmentAndEmployeeDAO.getEmpStatusByEmpId(empDtl.getEmpId(), userCnt,
						empDtl.getTenantId());
				if (empStat.equalsIgnoreCase("NR")) {

					rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					rm.setResponseMessage(ResponseMessageMap.maxUserCnt);
					rm.setResponseMessage(ResponseMessageMap.maxUserCnt);
				} else {
					if (!empDtl.getPassword().isEmpty()) {
						String encPassword = ws.passwordEncoder().encode(empDtl.getPassword());
						iDepartmentAndEmployeeDAO.updateEmployeePwd(empDtl.getEmpId(), encPassword);

					}
					String userLoginId = iDepartmentAndEmployeeDAO.getUserLoginId(empDtl.getEmpId());
					iDepartmentAndEmployeeDAO.updateuserLoginAsInactive(userLoginId, "1",empDtl.getEmailId());
					iDepartmentAndEmployeeDAO.updateEmployeeDtl(empDtl);
					iDepartmentAndEmployeeDAO.updateUserLogin(empDtl.getEmpId(), empDtl.getRole(), userLoginId,
							empDtl.getTenantId());
					int processAssignCount = iDepartmentAndEmployeeDAO.countProcessAssign(empDtl.getFromEmployee(), empDtl.getTenantId());
					if (processAssignCount == 1) {
					    iDepartmentAndEmployeeDAO.insertExistUserProcessAssign(empDtl.getFromEmployee(), empDtl.getEmpId(), empDtl.getTenantId());
					    rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					    rm.setResponseDataMessage(ResponseMessageMap.successCreated);
					} else {
						rm.setResponseCode(ResponseMessageMap.failToupdateCode);
						rm.setResponseDataMessage(ResponseMessageMap.failToCreateMsg);
					}

					int indentAssignCount = iDepartmentAndEmployeeDAO.countHodIndentDetail(empDtl.getFromEmployee(), empDtl.getTenantId());
					if (indentAssignCount == 1) {
					    iDepartmentAndEmployeeDAO.insertExistUserIndentAssign(empDtl.getFromEmployee(), empDtl.getEmpId(), empDtl.getTenantId());
					    rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					    rm.setResponseDataMessage(ResponseMessageMap.successCreated);
					} else {
						rm.setResponseCode(ResponseMessageMap.failToupdateCode);
						rm.setResponseDataMessage(ResponseMessageMap.failToCreateMsg);
					}
					rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					rm.setResponseMessage(ResponseMessageMap.successCreated);
				}

			}
			}
		} catch (Exception ex) {
			logger.error("updateEmployeeDtl  method exception-->" + ex);
		}

		return rm;

	}

	@Override
	public ResponseAsList getModuleMstDtls(String tenantId) {
		ResponseAsList returnL = new ResponseAsList();
		List<ModuleMstEntity> moduleList = new ArrayList<ModuleMstEntity>();
		try {
			moduleList = iDepartmentAndEmployeeDAO.getModuleMstDtlList(tenantId);
			if (moduleList.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(moduleList);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
				returnL.setResponseData(moduleList);
			}
		} catch (Exception ex) {
			logger.error("getModuleMstDtls method error " + ex);
		}
		return returnL;
	}

	@Override
	public ResponseAsList getModuleBasedScreenDtls(String tenantId, String moduleId, String roleId) {
		ResponseAsList returnL = new ResponseAsList();
		List<ModuleScreenList> moduleList = new ArrayList<ModuleScreenList>();
		try {
			String checkUserRole = "";
			int userRole = 0;
			checkUserRole = iDepartmentAndEmployeeDAO.checkUserRoleExist(tenantId, roleId);
			if (checkUserRole.equalsIgnoreCase("NA")) {
				userRole = iDepartmentAndEmployeeDAO.insertUserRole(roleId, tenantId);
				checkUserRole = String.valueOf(userRole);
			}
			moduleList = iDepartmentAndEmployeeDAO.getModuleBasedScreenDtlList(tenantId, moduleId, checkUserRole);
			for (int i = 0; i < moduleList.size(); i++) {
				moduleList.get(i).setRoleId(checkUserRole);
			}
			if (moduleList.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(moduleList);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
				returnL.setResponseData(moduleList);
			}
		} catch (Exception ex) {
			logger.error("getModuleBasedScreenDtls method error " + ex);
		}
		return returnL;
	}

	@Override
	public ResponseAsMessage updateUserRoleMapping(List<RoleMappingRequest> roleReq) {
		// TODO Auto-generated method stub
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		int countCheck = 0;
		for (int i = 0; i < roleReq.size(); i++) {
			int checkCount = iDepartmentAndEmployeeDAO.getCount(roleReq.get(i).getRoleId(),
					roleReq.get(i).getUiScreenMstId(), roleReq.get(i).getTenantId());

			if (checkCount == 0) {
				int insert = iDepartmentAndEmployeeDAO.InsertNewRoleEntry(roleReq.get(i).getRoleId(),
						roleReq.get(i).getUiScreenMstId(), roleReq.get(i).getTenantId());
				countCheck += insert;
			}

			if (checkCount > 0 && roleReq.get(i).getIsActive() == 0) {
				int delete = iDepartmentAndEmployeeDAO.deleteRoleEntry(roleReq.get(i).getRoleId(),
						roleReq.get(i).getUiScreenMstId(), roleReq.get(i).getTenantId());
				countCheck += delete;
			}

		}

		if (countCheck == roleReq.size()) {
			returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnMsg.setResponseDataMessage(ResponseMessageMap.successUpdated);
		} else {
			returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
			returnMsg.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
		}

		return returnMsg;
	}

	@Override
	public ResponseAsMessage insertEmployeeDtl(EmployeeDetailsRequest empDtl) {
		// TODO Auto-generated method stub
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		WebSecurityConfig ws = new WebSecurityConfig();

		try {

			int getEmpCnt = iDepartmentAndEmployeeDAO.getEmpDtl(empDtl.getEmpId(), empDtl.getTenantId());
			int getEmpMailCnt = iDepartmentAndEmployeeDAO.getEmpMailDtl(empDtl.getEmailId(), empDtl.getTenantId());
			if (getEmpCnt > 0 || getEmpMailCnt >0) {
				returnMsg.setResponseCode(ResponseMessageMap.responseAlreadyExists);
				if(getEmpCnt > 0) {
				returnMsg.setResponseDataMessage(ResponseMessageMap.empIdExist);
				}else {
					returnMsg.setResponseDataMessage(ResponseMessageMap.empMailIdExist);	
				}
			} else {

				int userCnt = iDepartmentAndEmployeeDAO.getActiveUser(empDtl.getTenantId());
				String empCountCheck = iDepartmentAndEmployeeDAO.getTotalEmpCount(userCnt, empDtl.getTenantId());
				if (empCountCheck.equalsIgnoreCase("NR")) {
					returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMsg.setResponseDataMessage(ResponseMessageMap.maxUserCnt);
				} else {
					int checkUserName = iDepartmentAndEmployeeDAO.checkUserNameExist(empDtl.getEmpName());
					if (checkUserName == 0) {
						String empDesignation = "";
						empDesignation = iDepartmentAndEmployeeDAO.checkDesignationExist(empDtl.getDesignation(),
								empDtl.getTenantId(),empDtl.getDepartment());
						if (empDesignation.equalsIgnoreCase("NA")) {
							 iDepartmentAndEmployeeDAO.insertDesignation(empDtl.getDesignation(),
									empDtl.getTenantId(),empDtl.getDepartment());
						//	empDesignation = String.valueOf(desig);
							empDesignation = iDepartmentAndEmployeeDAO.checkDesignationExist(empDtl.getDesignation(),
									empDtl.getTenantId(),empDtl.getDepartment());
						}
						String newEmployeeId = iDepartmentAndEmployeeDAO.getNewEmpId(empDtl.getTenantId());
						String newEmpId = iDepartmentAndEmployeeDAO.insertNewEmpId(empDtl.getDepartment(), empDesignation,
								empDtl.getEmpId(), empDtl.getEmpName(), empDtl.getStatus(), empDtl.getTenantId(),
								newEmployeeId, empDtl.getEmailId(), empDtl.getClientDesign());
						String encPassword = ws.passwordEncoder().encode(empDtl.getPassword());

						int userLoginId = iDepartmentAndEmployeeDAO.insertNewUserLogin(empDtl.getEmailId(), encPassword,
								newEmployeeId, empDtl.getTenantId());

						int processAssignCount = iDepartmentAndEmployeeDAO.countProcessAssign(empDtl.getFromEmployee(), empDtl.getTenantId());
						if (processAssignCount == 1) {
						    iDepartmentAndEmployeeDAO.insertUserProcessAssign(empDtl.getFromEmployee(), newEmpId, empDtl.getTenantId());
						    returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
						    returnMsg.setResponseDataMessage(ResponseMessageMap.successCreated);
						} else {
						    returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
						    returnMsg.setResponseDataMessage(ResponseMessageMap.failToCreateMsg);
						}

						int indentAssignCount = iDepartmentAndEmployeeDAO.countHodIndentDetail(empDtl.getFromEmployee(), empDtl.getTenantId());
						if (indentAssignCount == 1) {
						    iDepartmentAndEmployeeDAO.insertUserIndentAssign(empDtl.getFromEmployee(), newEmpId, empDtl.getTenantId());
						    returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
						    returnMsg.setResponseDataMessage(ResponseMessageMap.successCreated);
						} else {
						    returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
						    returnMsg.setResponseDataMessage(ResponseMessageMap.failToCreateMsg);
						}
						String userRoleMap = iDepartmentAndEmployeeDAO.insertUserRoleMapping(userLoginId,
								empDtl.getRole(), empDtl.getTenantId());
						if (userRoleMap.equalsIgnoreCase("Success")) {
							returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMsg.setResponseDataMessage(ResponseMessageMap.successCreated);
						} else {
							returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMsg.setResponseDataMessage(ResponseMessageMap.failToCreateMsg);
						}
					} else {
						returnMsg.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnMsg.setResponseDataMessage(ResponseMessageMap.userNameExist);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("insertEmployeeDtl method error " + ex);
		}
		return returnMsg;
	}

	@Override
	public ResponseAsMessage createUserDetailsEnable(TenantEmpRequest tempEmp) {
		// TODO Auto-generated method stub
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {
			String empDesiCode = uploadManagementDAO.getDesigCodeByEmpId(tempEmp.getEmpId(), tempEmp.getTenantID());
			int empCreateQry = iDepartmentAndEmployeeDAO.getEmpCanCreate(empDesiCode, tempEmp.getTenantID());
			returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnMsg.setResponseDataMessage(String.valueOf(empCreateQry));
			returnMsg.setResponseMessage(ResponseMessageMap.successMsg);
		} catch (Exception ex) {
			logger.error("createUserDetailsEnable method error " + ex);
		}
		return returnMsg;
	}

	@Override
	public List<EmployeeForDepartmentEntity> getMstPocEmpForDepartment(GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmployeeForDepartment  method start");
		List<EmployeeForDepartmentEntity> empFinal = new ArrayList<EmployeeForDepartmentEntity>();
		try {

			String TENANT_ID = deptInfoId.getTenantId();
			String departmentId = deptInfoId.getDepartmentId();
			InitiateProcessRequest initiateProcessReq = new InitiateProcessRequest();
			initiateProcessReq.setTenantId(TENANT_ID);
			initiateProcessReq.setDeptCode(departmentId);
			List<ProjectWbsInitiationMst> getPMFromDept = stageManagementDAO.getPMFromDept(initiateProcessReq);
			// SERVICES
			if (getPMFromDept.size() > 0) {
				String designarr[] = getPMFromDept.get(0).getMasterPoc().split(",");

				for (int i = 0; i < designarr.length; i++) {
					List<EmployeeForDepartmentEntity> returnnMsg = new ArrayList<EmployeeForDepartmentEntity>();
					returnnMsg = iDepartmentAndEmployeeDAO.getMstPocEmpForDepartment(TENANT_ID, designarr[i]);
					empFinal.addAll(returnnMsg);
				}
			}
		} catch (Exception ex) {
			logger.error("getMstPocEmpForDepartment  method exception-->" + ex);
		}
		logger.debug("getMstPocEmpForDepartment  method end");
		return empFinal;
	}

	@Override
	public ResponseAsList getEmpdetails(GetEmployeeByDepartmentRequest deptInfoId) {
		ResponseAsList responseList = new ResponseAsList();
		List<EmployeemstdetailsEntity> list = new ArrayList<>();
		String tenantId = deptInfoId.getTenantId();
		String departmentId = deptInfoId.getDepartmentId();
		String empStsCode = deptInfoId.getEmploymentStatusCode();
		try {
			list = iDepartmentAndEmployeeDAO.getEmpdetails(tenantId,departmentId,empStsCode);
			if(list.size()>0) {
				responseList.setResponseCode(ResponseMessageMap.responseCodeOk);
				responseList.setResponseMessage(ResponseMessageMap.success);
				responseList.setResponseData(list);
			}else {
				responseList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				responseList.setResponseMessage(ResponseMessageMap.noRecord);			
			}
		}catch (Exception e) {
			logger.error("getEmpdetails service method exception"+e);
		}	
		return responseList;
	}

	@Override
	public ResponseAsMessage updtempmstdetails(EmpsalaryRequest empsal) {	
		ResponseAsMessage msg = new ResponseAsMessage();
		String empId = empsal.getEmpId();
		String salary = empsal.getSalary();
		String tenantId = empsal.getTenantId();
		logger.info("updtempmstdetails method start");
		try {
			int update = iDepartmentAndEmployeeDAO.updtempmstdetails(empId,tenantId,salary);
			if(update>0) {
				msg.setResponseMessage(ResponseMessageMap.successUpdated);
				msg.setResponseCode(ResponseMessageMap.responseCodeOk);	
			}
			else {
				msg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				msg.setResponseCode(ResponseMessageMap.failToupdateCode);
			}
		}catch (Exception e) {
			logger.error("updtempmstdetails service method exception"+e);
		}
		return msg;
	}

}
