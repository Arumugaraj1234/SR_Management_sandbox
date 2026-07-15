package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.DepartmentInfoEntity;
import com.vmfg.general.entity.DesignationEntity;
import com.vmfg.general.entity.DocDeptEntity;
import com.vmfg.general.entity.EmpRoleEntity;
import com.vmfg.general.entity.EmpStatusEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.EmployeeForUserDtl;
import com.vmfg.general.entity.EmployeemstdetailsEntity;
import com.vmfg.general.entity.ModuleMstEntity;
import com.vmfg.general.entity.ModuleScreenList;
import com.vmfg.general.entity.ProcessAssignedTeamEntity;
import com.vmfg.general.request.DeptByPMRequest;
import com.vmfg.general.request.EmployeeDetailsRequest;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDepartmentAndEmployeeDAO {

	List<DepartmentInfoEntity> getDepartmentInfo(String tENANT_ID, String iS_ACTIVE, String EmployeeId);

	List<DepartmentInfoEntity> getEmpInfo(String tENANT_ID, String iS_ACTIVE, String eMPLOYEE_ID);

	List<ProcessAssignedTeamEntity> getprocessAssignedTeam(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC);

	ResponseAsMessage deleteProcessAssignedTeam(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC,
			String aSSIGNED_EMP_ID);

	List<EmployeeForDepartmentEntity> getEmployeeForDepartment(String tENANT_ID, String departmentId, String empId);

	int getAssignTeamCount(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC);

	int getMasterAssignTeam(String aSSIGNED_EMP_ID, String tENANT_ID);

	String getPrimaryVal(String design, String pmId);

	List<DepartmentInfoEntity> getDeptForPM(DeptByPMRequest deptInfoId);

	List<DocDeptEntity> getDeptForDesig(String dept, String tenantId, String designation);

	List<EmployeeForUserDtl> getEmployeeUserDtlForDeptByDept(String deptCode, String tenantId);

	List<EmployeeForUserDtl> getEmployeeUserDtlForDeptByEmpId(String employeeId, String tenantId);

	List<DesignationEntity> getEmpDesignation(String tenantId);

	List<EmpStatusEntity> getEmpStatus(String tenantId);

	List<EmpRoleEntity> getEmpRole(String tenantId);

	void updateEmployeeDtl(EmployeeDetailsRequest empDtl);

	void updateEmployeePwd(String empId, String encPassword);

	String getUserLoginId(String empId);

	void updateUserLogin(String empId, String role, String userLoginId, String tenantId);

	void updateuserLoginAsInactive(String userLoginId, String state ,String mailId);

	String getEmpStatusByEmpId(String empId,int userCnt,String tenantId);

	int getActiveUser(String tenantId);

	ResponseAsMessage insertProcessAssignedTeam(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC,
			String aSSIGNED_EMP_ID, String projectId);

	List<ModuleMstEntity> getModuleMstDtlList(String tenantId);

	List<ModuleScreenList> getModuleBasedScreenDtlList(String tenantId, String moduleId, String roleId);

	int getCount(String roleId, String uiScreenMstId, String tenantId);

	int InsertNewRoleEntry(String roleId, String uiScreenMstId, String tenantId);

	int deleteRoleEntry(String roleId, String uiScreenMstId, String tenantId);

	String getTotalEmpCount(int userCnt, String tenantId);

	String getNewEmpId(String tenantId);

	int insertNewUserLogin(String empName, String encPassword, String newEmployeeId, String tenantId);

	String insertUserRoleMapping(int userLoginId, String role, String tenantId);
	
	String insertNewEmpId(String department, String designation, String empId, String empName, String status,
			String tenantId, String newEmpId, String emailId, String clientDesign);

	int checkUserNameExist(String empName);

	String checkUserRoleExist(String tenantid, String roleDesc);

	int insertUserRole(String role, String tenantId);

	String checkDesignationExist(String Design, String tenantid, String dept);

	int insertDesignation(String designCode, String tenantid, String dept);

	int getEmpCanCreate(String empDesiCode, String tenantID);

	List<EmployeeForDepartmentEntity> getMstPocEmpForDepartment(String tENANT_ID, String designation);

	List<EmployeeForUserDtl> getAllEmployeeUserDtl(String tenantId);

	int getEmpDtl(String empId, String tenantId);
	
	int getEmpMailDtl(String empId, String tenantId);
	
	int getupdateCodeAndMailCheck(String cloumnName,String val,String empId, String tenantId);

	List<EmployeemstdetailsEntity> getEmpdetails(String tenantId, String departmentId, String empStsCode);

	int updtempmstdetails(String empId, String tenantId, String salary);

	String getPrimaryDocFlagVal(String empId, String pmId, String tenantId);
	
	int countHodIndentDetail(String empId, String tenantId);

	int countProcessAssign(String empId, String tenantId);

	int insertUserIndentAssign(String fromEmpId, String toEmpId, String tenantId);

	int insertUserProcessAssign(String fromEmpId, String toEmpId, String tenantId);

	int insertExistUserProcessAssign(String fromEmpId, String toEmpId, String tenantId);

	int insertExistUserIndentAssign(String fromEmpId, String toEmpId, String tenantId);



}
