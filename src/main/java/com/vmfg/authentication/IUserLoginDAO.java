package com.vmfg.authentication;

import java.util.List;

import org.json.JSONObject;

import com.vmfg.general.request.GetMenuByUserRoleRequest;
import com.vmfg.general.request.GetlinkurlStatusRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IUserLoginDAO {

	List<UserLogin> getUserLoginInfo(String username, String password,String ipaddress,String hostname);

	List<LoginUserRoles> getAllUserRoles(String tenantid);

	List<LoginUserDetails> getAllLoginUsers(String activeflag,String tenantid);

	String insertLoginUsers(JSONObject useritems);

	String updateLoginUsers(JSONObject jsonObj);
	
	List<LoginUserDetails> getUserAndRoleinfo(String loginid, String empid,String tenantid);

	String resetLoginUsersPassword(JSONObject jsonObj);

	List<ResetPasswordEmpEntity> getResetLoginUsersInfo(JSONObject jsonObj);

	List<UserScreenMap> getUserScreenInfo(int roleid, String moduleid,String tenantid);
	List<UIModuleMst> getModules(JSONObject jsonObj);

	List<UserScreenMap> getScreenIdByRole(int roleid, String moduleid,String tenantid);

	String updateUserRoleMapping(JSONObject jsonObj);

	List<LoggedInUserDetailsHdr> getLoggedUserDetails(JSONObject jsonObj);

	List<LoginUserInfoHdr> getUserInfo(JSONObject jsonObj);
	ResponseAsList getMenuByUserRole(GetMenuByUserRoleRequest getMenuByUserRoleReq);
	ResponseAsMessage getlinkurlStatus(GetlinkurlStatusRequest getlinkurlStatusReq);
	
	List<UIModuleMst> getuserRoleList(String roleId,String tenantId);
	List<UIScreenMst> getuiscreenList(String roleId,String tenantId,String moduleId);
	String onloadLandingPage(String loginId,String tenantId);
	int getActivelinkurlStatus(String linkUrl,String roleId,String tenantId);

	String onLoadCurrency(String tenantId);

	List<LinkUrlEntity> getUrlDtls(String userRoleId, String tenantId);
	
	List<ImageUrlEntity> getImageUrl(String url);
}
