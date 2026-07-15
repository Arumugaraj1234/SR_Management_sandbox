package com.vmfg.authentication;

import java.util.List;

import org.json.JSONObject;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.request.GetMenuByUserRoleRequest;
import com.vmfg.general.request.GetlinkurlStatusRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IUserLoginService {

	List<UserLogin> getUserLoginInfo(String username, String password,String ipaddress,String hostname);

	List<LoginUserRoles> getAllUserRoles(String tenantid);

	List<LoginUserDetails> getAllLoginUsers(String activeflag,String tenantid);

	String insertLoginUsers(JSONObject useritems);

	String updateLoginUsers(JSONObject jsonObj);
	
	List<LoginUserDetails> getUserAndRoleinfo(String loginid, String empid,String tenantid);

	String resetLoginUsersPassword(JSONObject jsonObj);

	List<ResetPasswordEmpEntity> getResetLoginUsersInfo(JSONObject jsonObj);

	List<UserScreenMap> getUserScreenInfo(int roleid, String moduleid,String tenantid);

	List<UserScreenMap> getScreenIdByRole(int roleid,String moduleid, String tenantid);
	
	String updateUserRoleMapping(JSONObject jsonObj);
	List<UIModuleMst> getModules(JSONObject jsonObj);

	List<LoggedInUserDetailsHdr> getLoggedUserDetails(JSONObject jsonObj);

	List<LoginUserInfoHdr> getUserInfo(JSONObject jsonObj);

	ResponseAsList getMenuByUserRole(GetMenuByUserRoleRequest getMenuByUserRoleReq);
	ResponseAsMessage getlinkurlStatus(GetlinkurlStatusRequest getlinkurlStatusReq);
	ResponseAsMessage getImageUrl(TenantRequest tenantReq);
}
