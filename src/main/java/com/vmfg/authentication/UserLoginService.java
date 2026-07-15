package com.vmfg.authentication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.request.GetMenuByUserRoleRequest;
import com.vmfg.general.request.GetlinkurlStatusRequest;
import com.vmfg.general.response.GetMenuByUserRoleResponse;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.util.CommonBase64Class;

@Service
public class UserLoginService implements IUserLoginService {
	private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);
	@Autowired
	private IUserLoginDAO iuserLoginDAO;

	@Override
	public List<UserLogin> getUserLoginInfo(String username, String password, String ipaddress, String hostname) {
		return iuserLoginDAO.getUserLoginInfo(username, password, ipaddress, hostname);
	}

	@Override
	public List<LoginUserRoles> getAllUserRoles(String tenantid) {
		return iuserLoginDAO.getAllUserRoles(tenantid);
	}

	@Override
	public List<LoginUserDetails> getAllLoginUsers(String activeflag, String tenantid) {
		return iuserLoginDAO.getAllLoginUsers(activeflag, tenantid);
	}

	@Override
	public String insertLoginUsers(JSONObject useritems) {
		return iuserLoginDAO.insertLoginUsers(useritems);
	}

	@Override
	public String updateLoginUsers(JSONObject jsonObj) {
		return iuserLoginDAO.updateLoginUsers(jsonObj);
	}

	@Override
	public List<LoginUserDetails> getUserAndRoleinfo(String loginid, String empid, String tenantid) {
		return iuserLoginDAO.getUserAndRoleinfo(loginid, empid, tenantid);
	}

	@Override
	public String resetLoginUsersPassword(JSONObject jsonObj) {
		return iuserLoginDAO.resetLoginUsersPassword(jsonObj);
	}

	@Override
	public List<ResetPasswordEmpEntity> getResetLoginUsersInfo(JSONObject jsonObj) {
		return iuserLoginDAO.getResetLoginUsersInfo(jsonObj);
	}

	@Override
	public List<UserScreenMap> getUserScreenInfo(int roleid, String moduleid, String tenantid) {
		return iuserLoginDAO.getUserScreenInfo(roleid, moduleid, tenantid);
	}

	@Override
	public List<UserScreenMap> getScreenIdByRole(int roleid, String moduleid, String tenantid) {
		return iuserLoginDAO.getScreenIdByRole(roleid, moduleid, tenantid);
	}

	@Override
	public String updateUserRoleMapping(JSONObject jsonObj) {
		return iuserLoginDAO.updateUserRoleMapping(jsonObj);
	}

	@Override
	public List<UIModuleMst> getModules(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		return iuserLoginDAO.getModules(jsonObj);
	}

	@Override
	public List<LoggedInUserDetailsHdr> getLoggedUserDetails(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		return iuserLoginDAO.getLoggedUserDetails(jsonObj);
	}

	@Override
	public List<LoginUserInfoHdr> getUserInfo(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		return iuserLoginDAO.getUserInfo(jsonObj);
	}

	@Override
	public ResponseAsList getMenuByUserRole(GetMenuByUserRoleRequest getMenuByUserRoleReq) {
		// TODO Auto-generated method stub
		ResponseAsList list = new ResponseAsList();
		List<GetMenuByUserRoleResponse> getMenuByUserRoleListResp = new ArrayList<GetMenuByUserRoleResponse>();

		try {
			GetMenuByUserRoleResponse getMenuByUserRoleResp = new GetMenuByUserRoleResponse();
			List<UIModuleMst> userRoleList = iuserLoginDAO.getuserRoleList(getMenuByUserRoleReq.getUserRoleId(),
					getMenuByUserRoleReq.getTenantId());
			if (userRoleList.size() > 0) {
				for (int i = 0; i < userRoleList.size(); i++) {
					List<UIScreenMst> uiScreenMstlist = iuserLoginDAO.getuiscreenList(
							getMenuByUserRoleReq.getUserRoleId(), getMenuByUserRoleReq.getTenantId(),
							String.valueOf(userRoleList.get(i).getUiModuleMstID()));
					userRoleList.get(i).setUiScreenMstList(uiScreenMstlist);

				}
				
				List<LinkUrlEntity> Linklist=iuserLoginDAO.getUrlDtls(
						getMenuByUserRoleReq.getUserRoleId(), getMenuByUserRoleReq.getTenantId());
				String landingPage = iuserLoginDAO.onloadLandingPage(getMenuByUserRoleReq.getLoginId(),
						getMenuByUserRoleReq.getTenantId());
				String currency = iuserLoginDAO.onLoadCurrency(getMenuByUserRoleReq.getTenantId());

				getMenuByUserRoleResp.setCurrency(currency);
				getMenuByUserRoleResp.setUiModule(userRoleList);
				getMenuByUserRoleResp.setLandingPageUrl(landingPage);
				getMenuByUserRoleListResp.add(getMenuByUserRoleResp);
				getMenuByUserRoleResp.setAuthUrl(Linklist);
				list.setResponseData(getMenuByUserRoleListResp);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				list.setResponseData(getMenuByUserRoleListResp);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getMenuByUserRole Error" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage getlinkurlStatus(GetlinkurlStatusRequest getlinkUrlstatus) {
		// TODO Auto-generated method stub
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int linkurlStatus = iuserLoginDAO.getActivelinkurlStatus(getlinkUrlstatus.getLinkUrl(),
					getlinkUrlstatus.getRoleId(), getlinkUrlstatus.getTenantId());
			if (linkurlStatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage("True");
				message.setResponseMessage(ResponseMessageMap.success);
			} else {

				message.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				message.setResponseDataMessage("False");
				message.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getlinkurlStatus DAO Method Exception" + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage getImageUrl(TenantRequest tenantReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		List<ImageUrlEntity> result=new ArrayList<ImageUrlEntity>();
		try {
			 result  = iuserLoginDAO.getImageUrl(tenantReq.getTenantID());
			if(result.size()>0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(CommonBase64Class.getBasesBinary(new File(result.get(0).getImageUrl())));
				message.setResponseMessage(result.get(0).getTenantId());
			}
			
		} catch (Exception ex) {
			logger.error("getImageUrl DAO Method Exception" + ex);
		}
		return message;
	}

}
