package com.vmfg.general.services.interfaces;

import java.util.List;

import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.general.entity.DepartmentInfoEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.GetAssignTeamDtl;
import com.vmfg.general.request.DeptByPMRequest;
import com.vmfg.general.request.EmployeeDetailsRequest;
import com.vmfg.general.request.EmployeeTenantRequest;
import com.vmfg.general.request.EmpsalaryRequest;
import com.vmfg.general.request.GetEmployeeByDepartmentRequest;
import com.vmfg.general.request.ProcessAssignedTeamRequest;
import com.vmfg.general.request.RoleMappingRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDepartmentAndEmployeeService {

	List<DepartmentInfoEntity> getDepartmentAndEmpInfo(EmployeeTenantRequest departmentAndEmpInfoRequest);

	GetAssignTeamDtl getprocessAssignedTeam(ProcessAssignedTeamRequest processAssigned);

	ResponseAsMessage insertProcessAssignedTeam(ProcessAssignedTeamRequest processAssignedTeamRequest);

	ResponseAsMessage deleteProcessAssignedTeam(ProcessAssignedTeamRequest processAssignedTeamRequest);

	List<EmployeeForDepartmentEntity> getEmployeeForDepartment(GetEmployeeByDepartmentRequest deptInfoId);

	List<DepartmentInfoEntity> getDeptForPM(DeptByPMRequest deptInfoId);

	ResponseAsList getEmployeeUserDtlForDept(GetEmployeeByDepartmentRequest deptInfoId);

	ResponseAsList getEmpDesignation(String tenantId);

	ResponseAsList getEmpStatus(String tenantId);

	ResponseAsList getEmpRole(String tenantId);

	ResponseAsMessage updateEmployeeDtl(EmployeeDetailsRequest empDtl);

	ResponseAsList getModuleMstDtls(String tenantId);

	ResponseAsList getModuleBasedScreenDtls(String tenantId, String moduleId, String roleId);

	ResponseAsMessage updateUserRoleMapping(List<RoleMappingRequest> roleReq);

	ResponseAsMessage insertEmployeeDtl(EmployeeDetailsRequest empDtl);

	ResponseAsMessage createUserDetailsEnable(TenantEmpRequest tempEmp);

	List<EmployeeForDepartmentEntity> getMstPocEmpForDepartment(GetEmployeeByDepartmentRequest deptInfoId);

	ResponseAsList getEmpdetails(GetEmployeeByDepartmentRequest deptInfoId);

	ResponseAsMessage updtempmstdetails(EmpsalaryRequest empsal);


}
