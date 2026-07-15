package com.vmfg.general.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.general.entity.DepartmentInfoEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.GetAssignTeamDtl;
import com.vmfg.general.request.DeptByPMRequest;
import com.vmfg.general.request.EmployeeDetailsRequest;
import com.vmfg.general.request.EmployeeTenantRequest;
import com.vmfg.general.request.EmpsalaryRequest;
import com.vmfg.general.request.GetEmployeeByDepartmentRequest;
import com.vmfg.general.request.ModuleScreenRequest;
import com.vmfg.general.request.ProcessAssignedTeamRequest;
import com.vmfg.general.request.RoleMappingRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.services.interfaces.IDepartmentAndEmployeeService;

@Controller
@RequestMapping("/")
public class DepartmentAndEmployeeInfoController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentAndEmployeeInfoController.class);

	@Autowired
	IDepartmentAndEmployeeService iDepartmentAndEmployeeService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDepartmentAndEmpInfo")
	public ResponseEntity<List<DepartmentInfoEntity>> getDepartmentAndEmpInfo(
			@RequestBody EmployeeTenantRequest deptInfoId) {
		logger.info("DepartmentAndEmployeeInfoController   method Start");
		List<DepartmentInfoEntity> departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDepartmentAndEmployeeService.getDepartmentAndEmpInfo(deptInfoId);

		} catch (Exception ex) {
			logger.error("DepartmentAndEmployeeInfoController  method  exception" + ex);
		}
		logger.debug("DepartmentAndEmployeeInfoController   method end");
		return new ResponseEntity<List<DepartmentInfoEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDeptForPM")
	public ResponseEntity<List<DepartmentInfoEntity>> getDeptForPM(@RequestBody DeptByPMRequest deptInfoId) {
		logger.info("getDeptForPM   method Start");
		List<DepartmentInfoEntity> departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDepartmentAndEmployeeService.getDeptForPM(deptInfoId);

		} catch (Exception ex) {
			logger.error("getDeptForPM  method  exception" + ex);
		}
		return new ResponseEntity<List<DepartmentInfoEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProcessAssignedTeam")
	public ResponseEntity<GetAssignTeamDtl> getProcessAssignedTeam(
			@RequestBody ProcessAssignedTeamRequest ProcessAssigned) {
		logger.info("getprocessAssignedTeam   method Start");
		GetAssignTeamDtl list = null;
		try {

			list = iDepartmentAndEmployeeService.getprocessAssignedTeam(ProcessAssigned);

		} catch (Exception ex) {
			logger.error("getprocessAssignedTeam  method  exception" + ex);
		}
		logger.debug("getprocessAssignedTeam   method end");
		return new ResponseEntity<GetAssignTeamDtl>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertProcessAssignedTeam")
	public ResponseEntity<ResponseAsMessage> insertProcessAssignedTeam(
			@RequestBody ProcessAssignedTeamRequest insertProcessAssigned) {
		logger.info("insertProcessAssignedTeam   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iDepartmentAndEmployeeService.insertProcessAssignedTeam(insertProcessAssigned);

		} catch (Exception ex) {
			logger.error("insertProcessAssignedTeam  method  exception" + ex);
		}
		logger.debug("insertProcessAssignedTeam   method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteProcessAssignedTeam")
	public ResponseEntity<ResponseAsMessage> deleteProcessAssignedTeam(
			@RequestBody ProcessAssignedTeamRequest insertProcessAssigned) {
		logger.info("deleteProcessAssignedTeam   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iDepartmentAndEmployeeService.deleteProcessAssignedTeam(insertProcessAssigned);

		} catch (Exception ex) {
			logger.error("deleteProcessAssignedTeam  method  exception" + ex);
		}
		logger.debug("deleteProcessAssignedTeam   method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmployeeForDepartment")
	public ResponseEntity<List<EmployeeForDepartmentEntity>> getEmployeeForDepartment(
			@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("DepartmentAndEmployeeInfoController   method Start");
		List<EmployeeForDepartmentEntity> departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDepartmentAndEmployeeService.getEmployeeForDepartment(deptInfoId);

		} catch (Exception ex) {
			logger.error("getEmployeeForDepartmentController  method  exception" + ex);
		}
		logger.debug("getEmployeeForDepartmentController   method end");
		return new ResponseEntity<List<EmployeeForDepartmentEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmployeeUserDtlForDept")
	public ResponseEntity<ResponseAsList> getEmployeeUserDtlForDept(
			@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmployeeUserDtlForDept controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getEmployeeUserDtlForDept(deptInfoId);

		} catch (Exception ex) {
			logger.error("getEmployeeUserDtlForDept controller method  exception" + ex);
		}
		logger.debug("getEmployeeUserDtlForDept  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpDesignation")
	public ResponseEntity<ResponseAsList> getEmpDesignation(@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmpDesignation controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getEmpDesignation(deptInfoId.getTenantId());

		} catch (Exception ex) {
			logger.error("getEmpDesignation controller method  exception" + ex);
		}
		logger.debug("getEmpDesignation  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpStatus")
	public ResponseEntity<ResponseAsList> getEmpStatus(@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmpStatus controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getEmpStatus(deptInfoId.getTenantId());

		} catch (Exception ex) {
			logger.error("getEmpStatus controller method  exception" + ex);
		}
		logger.debug("getEmpStatus  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpRole")
	public ResponseEntity<ResponseAsList> getEmpRole(@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getEmpRole controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getEmpRole(deptInfoId.getTenantId());

		} catch (Exception ex) {
			logger.error("getEmpRole controller method  exception" + ex);
		}
		logger.debug("getEmpRole  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateEmployeeDtl")
	public ResponseEntity<ResponseAsMessage> updateEmployeeDtl(@RequestBody EmployeeDetailsRequest empDtl) {
		logger.info("updateEmployeeDtl controller   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDepartmentAndEmployeeService.updateEmployeeDtl(empDtl);

		} catch (Exception ex) {
			logger.error("updateEmployeeDtl controller method  exception" + ex);
		}
		logger.debug("updateEmployeeDtl  controller  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertEmployeeDtl")
	public ResponseEntity<ResponseAsMessage> insertEmployeeDtl(@RequestBody EmployeeDetailsRequest empDtl) {
		logger.info("insertEmployeeDtl controller   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDepartmentAndEmployeeService.insertEmployeeDtl(empDtl);

		} catch (Exception ex) {
			logger.error("insertEmployeeDtl controller method  exception" + ex);
		}
		logger.debug("insertEmployeeDtl  controller  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getModuleMstDtls")
	public ResponseEntity<ResponseAsList> getModuleMstDtls(@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getModuleMstDtls controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getModuleMstDtls(deptInfoId.getTenantId());

		} catch (Exception ex) {
			logger.error("getModuleMstDtls controller method  exception" + ex);
		}
		logger.debug("getModuleMstDtls  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getModuleBasedScreenDtls")
	public ResponseEntity<ResponseAsList> getModuleBasedScreenDtls(@RequestBody ModuleScreenRequest moduleInfo) {
		logger.info("getModuleBasedScreenDtls controller   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDepartmentAndEmployeeService.getModuleBasedScreenDtls(moduleInfo.getTenantId(), moduleInfo.getModuleId(),moduleInfo.getRoleId());

		} catch (Exception ex) {
			logger.error("getModuleBasedScreenDtls controller method  exception" + ex);
		}
		logger.debug("getModuleBasedScreenDtls  controller  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateUserRoleMapping")
	public ResponseEntity<ResponseAsMessage> updateUserRoleMapping(@RequestBody List<RoleMappingRequest> roleReq) {
		logger.info("updateUserRoleMapping controller   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDepartmentAndEmployeeService.updateUserRoleMapping(roleReq);

		} catch (Exception ex) {
			logger.error("updateUserRoleMapping controller method  exception" + ex);
		}
		logger.debug("updateUserRoleMapping  controller  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("createUserDetailsEnable")
	public ResponseEntity<ResponseAsMessage> createUserDetailsEnable(@RequestBody TenantEmpRequest tempEmp) {
		logger.info("createUserDetailsEnable controller   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDepartmentAndEmployeeService.createUserDetailsEnable(tempEmp);

		} catch (Exception ex) {
			logger.error("createUserDetailsEnable controller method  exception" + ex);
		}
		logger.debug("createUserDetailsEnable  controller  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMstPocEmpForDepartment")
	public ResponseEntity<List<EmployeeForDepartmentEntity>> getMstPocEmpForDepartment(
			@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getMstPocEmpForDepartment   method Start");
		List<EmployeeForDepartmentEntity> departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDepartmentAndEmployeeService.getMstPocEmpForDepartment(deptInfoId);

		} catch (Exception ex) {
			logger.error("getMstPocEmpForDepartment  method  exception" + ex);
		}
		logger.debug("getMstPocEmpForDepartment   method end");
		return new ResponseEntity<List<EmployeeForDepartmentEntity>>(departmentInfoEntity, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpdetails")
	public ResponseEntity<ResponseAsList> getEmpdetails(
			@RequestBody GetEmployeeByDepartmentRequest deptInfoId) {
		logger.info("getMstPocEmpForDepartment   method Start");
		ResponseAsList responseList = null;
		try {
			responseList = iDepartmentAndEmployeeService.getEmpdetails(deptInfoId);
		} catch (Exception ex) {
			logger.error("getMstPocEmpForDepartment  method  exception" + ex);
		}
		logger.debug("getMstPocEmpForDepartment   method end");
		return new ResponseEntity<ResponseAsList>(responseList, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updtempmstdetails")
	public ResponseEntity<ResponseAsMessage> updtempmstdetails(
			@RequestBody EmpsalaryRequest empsal) {
		logger.info("getMstPocEmpForDepartment   method Start");
		ResponseAsMessage responseMsg = null;
		try {
			responseMsg = iDepartmentAndEmployeeService.updtempmstdetails(empsal);
		} catch (Exception ex) {
			logger.error("getMstPocEmpForDepartment  method  exception" + ex);
		}
		logger.debug("getMstPocEmpForDepartment   method end");
		return new ResponseEntity<ResponseAsMessage>(responseMsg, HttpStatus.OK);
	}
	
	
}
