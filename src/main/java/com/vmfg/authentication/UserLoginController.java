package com.vmfg.authentication;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.request.GetMenuByUserRoleRequest;
import com.vmfg.general.request.GetlinkurlStatusRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class UserLoginController {
	private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

	
	@Autowired
	private IUserLoginService uiserLoginService;

//	@CrossOrigin(maxAge = 3600)
//	@PostMapping("login")
//	public ResponseEntity<List<UserLogin>> login(@RequestParam("username") String username,
//			@RequestParam("password") String password,@RequestParam("ipaddress") String ipaddress,@RequestParam("hostname") String hostname)
//			throws ParseException {
//		List<UserLogin> list = null;
//		try {
//			logger.info("login Method Start");
//			logger.info("<--------login username:--------------->" + username);
//        	list = uiserLoginService.getUserLoginInfo(username, password,ipaddress,hostname);
//			logger.info("login Method end");
//		} catch (Exception ex) {
//			logger.error("login Method Excepion:" + ex);
//			UserLogin ul = new UserLogin();
//			ul.setStatus("E0016");
//			logger.info("<---E0016--------login Exception--SQL Exception--->");
//			ul.setStatusMessage(ex.getMessage());
//			list.add(ul);
//		}
//		return new ResponseEntity<List<UserLogin>>(list, HttpStatus.OK);
//	}

	@CrossOrigin(maxAge = 3600)
	@GetMapping("getalluserroles/{tenantid}")
	public ResponseEntity<List<LoginUserRoles>> getAllUserRoles(@PathVariable("tenantid") String tenantid) {
		logger.info("getAllUserRoles Method Start");
		List<LoginUserRoles> list = null;
		try {
			list = uiserLoginService.getAllUserRoles(tenantid);

		} catch (Exception ex) {
			logger.error("getAllUserRoles Method Exception" + ex);
		}
		logger.info("getAllUserRoles Method end");
		return new ResponseEntity<List<LoginUserRoles>>(list, HttpStatus.OK);

	}

	@CrossOrigin(maxAge = 3600)
	@GetMapping("getallloginusers/{activeflag}/{tenantid}")
	public ResponseEntity<List<LoginUserDetails>> getAllLoginUsers(@PathVariable("activeflag") String activeflag,
			@PathVariable("tenantid") String tenantid) {
		logger.info("getAllLoginUsers Method Start");
		List<LoginUserDetails> list = null;
		try {
			list = uiserLoginService.getAllLoginUsers(activeflag, tenantid);

		} catch (Exception ex) {
			logger.error("getAllLoginUsers Method Exception" + ex);
		}
		logger.info("getAllLoginUsers Method end");
		return new ResponseEntity<List<LoginUserDetails>>(list, HttpStatus.OK);

	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertloginusers")
	public ResponseEntity<String> insertLoginUsers(@RequestParam("usersdtl") String usersdtl) {
		logger.info("insertLoginUsers Method Start");
		String returnMsg = "";
		try {
			JSONObject jsonObj = new JSONObject(usersdtl);

			returnMsg = uiserLoginService.insertLoginUsers(jsonObj);

		} catch (Exception ex) {
			logger.error("insertLoginUsers Method Exception" + ex);
		}
		logger.info("insertLoginUsers Method end");
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);

	}

	@CrossOrigin(maxAge = 3600)
	@GetMapping("getuserandroleinfo/{loginid}/{empid}/{tenantid}")
	public ResponseEntity<List<LoginUserDetails>> getUserAndRoleinfo(@PathVariable("loginid") String loginid,
			@PathVariable("empid") String empid, @PathVariable("tenantid") String tenantid) {
		logger.info("getUserAndRoleinfo method start");
		List<LoginUserDetails> list = null;
		try {
			list = uiserLoginService.getUserAndRoleinfo(loginid, empid, tenantid);
		} catch (Exception ex) {
			logger.error("getUserAndRoleinfo exception:" + ex);
		}
		logger.info("updateloginusersinfo method end");
		return new ResponseEntity<List<LoginUserDetails>>(list, HttpStatus.OK);

	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateloginuser")
	public ResponseEntity<String> updateLoginUsers(@RequestParam("updateuser") String updateuser) {
		logger.info("updateLoginUsers Method Start");
		String returnMsg = "";
		try {
			JSONObject jsonObj = new JSONObject(updateuser);
			returnMsg = uiserLoginService.updateLoginUsers(jsonObj);
		} catch (Exception ex) {
			logger.error("updateLoginUsers Method Exception" + ex);
		}
		logger.info("updateLoginUsers Method end");
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);

	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("resetpassword")
	public ResponseEntity<String> resetLoginUsersPassword(@RequestParam("resetinfo") String resetinfo) {
		logger.info("resetLoginUsersPassword Method Start");
		String returnMsg = "";
		try {
			JSONObject jsonObj = new JSONObject(resetinfo);
			returnMsg = uiserLoginService.resetLoginUsersPassword(jsonObj);
		} catch (Exception ex) {
			logger.error("resetLoginUsersPassword Method Exception" + ex);
		}
		logger.info("resetLoginUsersPassword Method end");
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);

	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("resetgetempinfo")
	public ResponseEntity<List<ResetPasswordEmpEntity>> getResetLoginUsersInfo(@RequestParam("resetempinfo") String resetempinfo) {
		logger.info("getResetLoginUsersInfo Method Start");
		List<ResetPasswordEmpEntity> list = null;
		try {
			JSONObject jsonObj = new JSONObject(resetempinfo);
			list = uiserLoginService.getResetLoginUsersInfo(jsonObj);
		} catch (Exception ex) {
			logger.error("getResetLoginUsersInfo Method Exception" + ex);
		}
		logger.info("getResetLoginUsersInfo Method end");
		return new ResponseEntity<List<ResetPasswordEmpEntity>>(list, HttpStatus.OK);

	}
	@CrossOrigin(maxAge = 3600)
	@GetMapping("getuserscreeninfo/{roleid}/{moduleid}/{tenantid}")
	public ResponseEntity<List<UserScreenMap>> getUserScreenInfo(@PathVariable("roleid") int roleid,@PathVariable("moduleid") String moduleid,@PathVariable("tenantid") String tenantid) {
		logger.info("getUserScreenInfo Method Start");
		List<UserScreenMap> list = null;
		try {
			list = uiserLoginService.getUserScreenInfo(roleid, moduleid, tenantid);

		} catch (Exception ex) {
			logger.error("getUserScreenInfo Method Exception" + ex);
		}
		logger.info("getUserScreenInfo Method end");
		return new ResponseEntity<List<UserScreenMap>>(list, HttpStatus.OK);

	}
	@CrossOrigin(maxAge = 3600)
	@GetMapping("getscreenidbyrole/{roleid}/{moduleid}/{tenantid}")
	public ResponseEntity<List<UserScreenMap>> getScreenIdByRole(@PathVariable("roleid") int roleid,@PathVariable("moduleid") String moduleid,@PathVariable("tenantid") String tenantid) {
		logger.info("getScreenIdByRole Method Start");
		List<UserScreenMap> list = null;
		try {
			list = uiserLoginService.getScreenIdByRole(roleid, moduleid, tenantid);

		} catch (Exception ex) {
			logger.error("getScreenIdByRole Method Exception" + ex);
		}
		logger.info("getScreenIdByRole Method end");
		return new ResponseEntity<List<UserScreenMap>>(list, HttpStatus.OK);

	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateuserrolemapping")
	public ResponseEntity<String> updateUserRoleMapping(@RequestParam("usermapinfo") String usermapinfo) {
		logger.info("updateUserRoleMapping Method Start");
		String returnMsg = "";
		try {
			JSONObject jsonObj = new JSONObject(usermapinfo);
			returnMsg = uiserLoginService.updateUserRoleMapping(jsonObj);
		} catch (Exception ex) {
			logger.error("updateUserRoleMapping Method Exception" + ex);
		}
		logger.info("updateUserRoleMapping Method end");
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);

	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("modulesinfo")
	public ResponseEntity<List<UIModuleMst>> getModules(@RequestParam("modules") String modules) {
		logger.info("getModules Method Start");
		List<UIModuleMst> list = null;
		try {
			JSONObject jsonObj = new JSONObject(modules);
			list = uiserLoginService.getModules(jsonObj);
		} catch (Exception ex) {
			logger.error("getModules Method Exception" + ex);
		}
		logger.info("getModules Method end");
		return new ResponseEntity<List<UIModuleMst>>(list, HttpStatus.OK);

	}
	
	//Login User Detail
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getUserLogginDetails")
	public ResponseEntity<List<LoggedInUserDetailsHdr>> getLoggedUserDetails(@RequestParam("getDetail") String modules) {
		logger.info("getLoggedUserDetails Method Start");
		List<LoggedInUserDetailsHdr> list = null;
		try {
			JSONObject jsonObj = new JSONObject(modules);
			list = uiserLoginService.getLoggedUserDetails(jsonObj);
		} catch (Exception ex) {
			logger.error("getLoggedUserDetails Method Exception" + ex);
		}
		logger.info("getLoggedUserDetails Method end");
		return new ResponseEntity<List<LoggedInUserDetailsHdr>>(list, HttpStatus.OK);

	}
	
	// User Detail
		@CrossOrigin(maxAge = 3600)
		@PostMapping("getuserinfo")
		public ResponseEntity<List<LoginUserInfoHdr>> getUserInfo(@RequestParam("getuserDetail") String modules) {
			logger.info("getUserInfo Method Start");
			List<LoginUserInfoHdr> list = null;
			try {
				JSONObject jsonObj = new JSONObject(modules);
				list = uiserLoginService.getUserInfo(jsonObj);
			} catch (Exception ex) {
				logger.error("getUserInfo Method Exception" + ex);
			}
			logger.info("getUserInfo Method end");
			return new ResponseEntity<List<LoginUserInfoHdr>>(list, HttpStatus.OK);

		}

		@CrossOrigin(maxAge = 3600)
		@PostMapping("getMenuByUserRole")
		public ResponseEntity<ResponseAsList> getMenuByUserRole(@RequestBody GetMenuByUserRoleRequest getMenuByUserRoleReq) {
			logger.debug("getMenuByUserRole Method Start");
			ResponseAsList list = null;
			try {
				list = uiserLoginService.getMenuByUserRole(getMenuByUserRoleReq);
			} catch (Exception ex) {
				logger.error("getMenuByUserRole Method Exception" + ex);
			}
			logger.debug("getMenuByUserRole Method end");
			return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);

		}
		@CrossOrigin(maxAge = 3600)
		@PostMapping("getlinkurlStatus")
		public ResponseEntity<ResponseAsMessage> getlinkurlStatus(@RequestBody GetlinkurlStatusRequest getlinkurlStatusRequest) {
			logger.debug("getlinkurlStatus Method Start");
			ResponseAsMessage list = null;
			try {
				list = uiserLoginService.getlinkurlStatus(getlinkurlStatusRequest);
			} catch (Exception ex) {
				logger.error("getlinkurlStatus Method Exception" + ex);
			}
			logger.debug("getlinkurlStatus Method end");
			return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);

		}
		@CrossOrigin(maxAge = 3600)
		@PostMapping("getImageUrl")
		public ResponseEntity<ResponseAsMessage> getImageUrl(@RequestBody TenantRequest tenantReq) {
			logger.debug("getImageUrl Method Start");
			ResponseAsMessage list = null;
			try {
				list = uiserLoginService.getImageUrl(tenantReq);
			} catch (Exception ex) {
				logger.error("getImageUrl Method Exception" + ex);
			}
			logger.debug("getImageUrl Method end");
			return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);

		}
}
