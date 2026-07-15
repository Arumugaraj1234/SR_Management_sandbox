package com.vmfg.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.request.GetMenuByUserRoleRequest;
import com.vmfg.general.request.GetlinkurlStatusRequest;
import com.vmfg.general.response.GetMenuByUserRoleResponse;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.security.config.WebSecurityConfig;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;
import com.vmfg.util.dao.IGetPropertyValueDAO;

@Transactional
@Repository
public class UserLoginDAO implements IUserLoginDAO, UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserLoginDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IGetPropertyValueDAO iGetPropertyValueService;

	public List<UIModuleMst> getUIModuleMstAndScreenMstData(int roleId, String tenantid) {
		logger.info("getUIModuleMstAndScreenMstData DAO method start");
		List<UIModuleMst> umm = null;
		List<UIScreenMst> usm = null;
		try {

			umm = this.jdbcTemplate.query("call UI_GET_MODULESBY_ROLEID(?,?)", new UIModuleMstRowMapper(), roleId,
					tenantid);

			if (umm != null && !umm.isEmpty() && umm.size() > 0) {
				for (int i = 0; i < umm.size(); i++) {
					usm = this.jdbcTemplate.query("call UI_GET_SCREENSBY_ROLEANDMDULEID(?,?,?)",
							new UIScreenMstRowMapper(), roleId, umm.get(i).getUiModuleMstID(), tenantid);
					if (usm != null) {
						umm.get(i).setUiScreenMstList(usm);
					} else {
						logger.info("<-----------Role-------Screens--------Empty----------------->");
					}

				}

			} else {
				logger.info("<-----------------Role Module Empty----------------------------->");
			}

		} catch (Exception e) {
			logger.error("getUIModuleMstAndScreenMstData DAO Method Exception" + e);
		}
		logger.info("getUIModuleMstAndScreenMstData DAO method end");
		return umm;
	}

	@Override
	public List<LoginUserRoles> getAllUserRoles(String tenantid) {
		logger.info(" getAllUserRoles DAO method Start");
		List<LoginUserRoles> list = null;
		try {

			logger.info("<----------------call---USER_GETALL_USERROLES-------proc--------------------------->");
			list = this.jdbcTemplate.query("call USER_GETALL_USERROLES(?)", new LoginUserRolesRowMapper(), tenantid);

		} catch (Exception e) {
			logger.error("getAllUserRoles DAO Method Exception" + e);
		}
		logger.info(" getAllUserRoles DAO method end");
		return list;
	}

	@Override
	public List<LoginUserDetails> getAllLoginUsers(String activeflag, String tenantid) {
		logger.info(" getAllLoginUsers DAO method Start");
		List<LoginUserDetails> list = null;
		try {
			logger.info("<----------------call---USER_GETALL_LOGINUSERS-------proc--------------------------->");

			list = this.jdbcTemplate.query("call USER_GETALL_LOGINUSERS(?,?)", new LoginUserDetailsRowMapper(),
					activeflag, tenantid);

		} catch (Exception e) {
			logger.error("getAllLoginUsers DAO Method Exception" + e);
		}
		logger.info(" getAllLoginUsers DAO method end");
		return list;
	}

	@Override
	public String insertLoginUsers(JSONObject jsonObj) {
		String returnString = "";
		logger.info("insertLoginUsers DAO method start");
		try {

			if (jsonObj.has("useritem")) {
				logger.info("<------------insertLoginUsers method--useritem Array Found-->");
				JSONArray useritems = jsonObj.getJSONArray("useritem");
				String insertqty = "INSERT INTO \r\n" + "user_login \r\n"
						+ "(userName,userPassword,isActive,EMPLOYEE_ID,TENANT_ID)\r\n" + "VALUES (?,?,?,?,?)";

				for (int i = 0; i < useritems.length(); i++) {

					JSONObject objects = useritems.getJSONObject(i);
					JSONArray keys = objects.names();

					String userName = "";
					String userPassword = "";
					String isActive = "";
					String employee_mst_id = "";
					String TENANT_ID = "";

					final String userName1;
					final String userPassword1;
					final String isActive1;
					final String employee_mst_id1;
					final String TENANT_ID1;

					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("userName")) {
							userName = value;
						} else if (key.equalsIgnoreCase("userPassword")) {
							userPassword = value;
						} else if (key.equalsIgnoreCase("isActive")) {
							isActive = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						} else if (key.equalsIgnoreCase("employee_mst_id")) {
							employee_mst_id = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						}
					}

					String configUserCount = GetPropertyValue.getPropValueByTenant(TENANT_ID, "LOGIN_USER_COUNT",
							this.jdbcTemplate);

					String userCountQty = "select count(*) AS COUNT from  user_login where TENANT_ID = ?";
					Map<String, Object> result = this.jdbcTemplate.queryForMap(userCountQty,TENANT_ID);
					Number usernumber  = Integer.parseInt(result.get("COUNT").toString());

					
					int dbUserCount = (usernumber != null ? usernumber.intValue() : 0);
					logger.info("ccount->" + configUserCount + ">-->ccount-->" + dbUserCount);
					if (Integer.parseInt(configUserCount) > dbUserCount) {
						userName1 = userName;
						userPassword1 = userPassword;
						isActive1 = isActive;
						employee_mst_id1 = employee_mst_id;
						TENANT_ID1 = TENANT_ID;
						String selecttqty = "select \r\n" + "count(*) AS COUNT \r\n" + "from \r\n" + "user_login as ul\r\n"
								+ "where\r\n" + "ul.EMPLOYEE_ID = ?\r\n"
								+ "and ul.TENANT_ID = ?\r\n" + "and ul.userName = ?";
						Map<String, Object> resultmap = this.jdbcTemplate.queryForMap(selecttqty,employee_mst_id,TENANT_ID,userName);
						Number number =Integer.parseInt(resultmap.get("COUNT").toString());
						
						int selectCount = (number != null ? number.intValue() : 0);
						if (selectCount != 0) {
							returnString = "E0013";
						} else {
							WebSecurityConfig ws = new WebSecurityConfig();
							String encPassword = ws.passwordEncoder().encode(userPassword1);
							KeyHolder holder = new GeneratedKeyHolder();
							jdbcTemplate.update(new PreparedStatementCreator() {
								@Override
								public PreparedStatement createPreparedStatement(Connection connection)
										throws SQLException {
									PreparedStatement ps = connection.prepareStatement(insertqty,
											Statement.RETURN_GENERATED_KEYS);
									ps.setString(1, userName1);
									ps.setString(2, encPassword);
									ps.setString(3, isActive1);
									ps.setObject(4, employee_mst_id1);
									ps.setString(5, TENANT_ID1);
									return ps;

								}
							}, holder);
							int userloginid = holder.getKey().intValue();
							logger.info("New User Login ID------>>>>", userloginid);

							// Inserted Successfully
							if (userloginid != 0) {
								returnString = "M0001";

								if (jsonObj.has("roleitem")) {
									JSONArray roleitem = jsonObj.getJSONArray("roleitem");
									String user_roleid = "";
									for (int k = 0; k < roleitem.length(); k++) {

										JSONObject objects1 = roleitem.getJSONObject(k);
										JSONArray keys1 = objects1.names();

										for (int l = 0; l < keys1.length(); ++l) {
											String key1 = keys1.getString(l);
											String value1 = objects1.getString(key1);
											if (key1.equalsIgnoreCase("user_roleid")) {
												user_roleid = value1;
											}
										}

										if (user_roleid != null && !user_roleid.trim().isEmpty()) {
											String[] roleArray = user_roleid.split(",");

											String insertQuery = "INSERT INTO user_role_mapping (USER_LOGINID,USER_ROLEID,IS_ACTIVE,TENANT_ID) VALUES (?,?,?,?)";
											for (int m = 0; m < roleArray.length; m++) {
												logger.info("<--------RoleID------------------->" + roleArray[m]);
												int insertres = jdbcTemplate.update(insertQuery, userloginid,
														roleArray[m], "1", TENANT_ID);
												if (insertres == 1) {
													returnString = "M0001";
													logger.info("<--------Successfully inserted--------->");
												} else {
													returnString = "E0001";
													logger.info("<--------Failed to insert------------->");
												}
											}
										}

									}

								}

							} else {
								returnString = "E0001";
							}
						}
					} else {
						returnString = "M0053";
					}
				}

			} else {
				logger.info("<------------insertLoginUsers method--useritem Array Not Found-->");
			}

		} catch (Exception e) {
			logger.error("insertLoginUsers DAO method Exception:" + e);
		}
		logger.info("insertLoginUsers DAO method end");
		return returnString;
	}

	@Override
	public String updateLoginUsers(JSONObject jsonObj) {
		String returnString = "";
		logger.info("updateLoginUsers DAO method start");
		try {
			if (jsonObj.has("useritem")) {
				logger.info("<------------updateLoginUsers method--useritem Array Found-->");
				JSONArray useritems = jsonObj.getJSONArray("useritem");

				String updateqty = "update user_login set isActive = ? where user_loginid = ? and userName = ? and EMPLOYEE_ID = ? and TENANT_ID = ?";

				for (int i = 0; i < useritems.length(); i++) {

					JSONObject objects = useritems.getJSONObject(i);
					JSONArray keys = objects.names();

					String user_loginid = "";
					String userName = "";
					String isActive = "";
					String employee_mst_id = "";
					String TENANT_ID = "";

					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("username")) {
							userName = value;
						} else if (key.equalsIgnoreCase("user_loginid")) {
							user_loginid = value;
						} else if (key.equalsIgnoreCase("isactive")) {
							isActive = value;
						} else if (key.equalsIgnoreCase("employeeid")) {
							employee_mst_id = value;
						} else if (key.equalsIgnoreCase("tenantID")) {
							TENANT_ID = value;
						}
					}

					logger.info("User Login ID------>>>>" + user_loginid);

					// Inserted Successfully
					if (user_loginid != "") {
						logger.info("isActive>>>>" + isActive + "<===user_loginid==>" + user_loginid + "<===userName==>"
								+ userName + "<===employee_mst_id==>" + employee_mst_id + "<===TENANT_ID==>"
								+ TENANT_ID);
						int updateRes = jdbcTemplate.update(updateqty, isActive, user_loginid, userName,
								employee_mst_id, TENANT_ID);
						if (updateRes == 1) {
							returnString = "M0002";
							logger.info("User Login Updated--->" + updateRes + "----" + returnString);
							String deleteQty = "delete from user_role_mapping where USER_LOGINID = ? and TENANT_ID = ?";
							int deleteRes = jdbcTemplate.update(deleteQty, user_loginid, TENANT_ID);
							logger.info("deleteRes----->>" + deleteRes);
							// if (deleteRes == 1) {
							if (jsonObj.has("roleitem")) {
								JSONArray roleitem = jsonObj.getJSONArray("roleitem");
								String user_roleid = "";
								for (int k = 0; k < roleitem.length(); k++) {

									JSONObject objects1 = roleitem.getJSONObject(k);
									JSONArray keys1 = objects1.names();

									for (int l = 0; l < keys1.length(); ++l) {
										String key1 = keys1.getString(l);
										String value1 = objects1.getString(key1);
										if (key1.equalsIgnoreCase("user_roleid")) {
											user_roleid = value1;
										}
									}

									if (user_roleid != null && !user_roleid.trim().isEmpty()) {
										String[] roleArray = user_roleid.split(",");

										String insertQuery = "INSERT INTO user_role_mapping (USER_LOGINID,USER_ROLEID,IS_ACTIVE,TENANT_ID) VALUES (?,?,?,?)";
										for (int m = 0; m < roleArray.length; m++) {
											logger.info("<--------RoleID------------------->" + roleArray[m]);
											int insertres = jdbcTemplate.update(insertQuery, user_loginid, roleArray[m],
													"1", TENANT_ID);
											if (insertres == 1) {
												returnString = "M0002";
												logger.info("<--------Successfully update--------->");
											} else {
												returnString = "E0002";
												logger.info("<--------Failed to update------------->");
											}
										}
									}

								}

							}
							// } else {
							// logger.info("<--------------------No Role Mapping
							// Availiable----------------------------->");
							// }

						} else {
							logger.info("User Login Not Updated--->" + updateRes);
						}

					} else {
						returnString = "E0002";
					}

				}
			} else {
				logger.info("<------------insertLoginUsers method--useritem Array Not Found-->");
			}
		} catch (Exception e) {
			logger.error("updateLoginUsers DAO method Exception:" + e);
		}
		logger.info("updateLoginUsers DAO method end");
		return returnString;
	}

	@Override
	public List<LoginUserDetails> getUserAndRoleinfo(String loginid, String empid, String tenantid) {
		logger.info(" getUserAndRoleinfo DAO method Start");
		List<LoginUserDetails> list = null;
		List<UserRoles> userRoles = null;
		try {

			logger.info("<----------------call---USER_GET_USERANDROLES-------proc--------------------------->");
			list = this.jdbcTemplate.query("call USER_GET_USERANDROLES(?,?,?)", new LoginUserDetailsRowMapper(),
					loginid, empid, tenantid);

			if (list != null && !list.isEmpty() && list.size() > 0) {

				logger.info("<--userLoginID----------->" + loginid);
				logger.info(
						"<----------------call---UI_GET_USERROLESBY_USERLOGINID-------proc--------------------------->");
				userRoles = this.jdbcTemplate.query("call UI_GET_USERROLESBY_USERLOGINID(?,?)",
						new UserRolesRowMapper(), loginid, tenantid);

				list.get(0).setUserRoles(userRoles);
			}

		} catch (Exception e) {
			logger.error("getUserAndRoleinfo DAO Method Exception--->" + e);
		}
		logger.info(" getUserAndRoleinfo DAO method end");
		return list;
	}

	@Override
	public String resetLoginUsersPassword(JSONObject jsonObj) {
		logger.info(" resetLoginUsersPassword DAO method Start");
		String returnMsg = "";
		try {

			if (jsonObj.has("useritem")) {
				logger.info("<------------resetLoginUsersPassword method--resetuseritem Array Found-->");
				JSONArray useritems = jsonObj.getJSONArray("useritem");

				String updateqty = "update user_login set userPassword = ? where user_loginid = ? and userName = ? and EMPLOYEE_ID = ? and TENANT_ID = ?";

				for (int i = 0; i < useritems.length(); i++) {

					JSONObject objects = useritems.getJSONObject(i);
					JSONArray keys = objects.names();

					String user_loginid = "";
					String userName = "";
					String EMPLOYEE_ID = "";
					String TENANT_ID = "";
					String userPassword = "";

					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("userName")) {
							userName = value;
						} else if (key.equalsIgnoreCase("user_loginid")) {
							user_loginid = value;
						} else if (key.equalsIgnoreCase("EMPLOYEE_ID")) {
							EMPLOYEE_ID = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						} else if (key.equalsIgnoreCase("userPassword")) {
							userPassword = value;
						}
					}

					WebSecurityConfig ws = new WebSecurityConfig();
					String encPassword = ws.passwordEncoder().encode(userPassword);

					logger.info("<----resetLoginUsersPassword DAO--Encrypet--Key-->" + encPassword);

					if (user_loginid != "") {
						logger.info("<===user_loginid==>" + user_loginid + "<===userName==>" + userName
								+ "<===EMPLOYEE_ID==>" + EMPLOYEE_ID + "<===TENANT_ID==>" + TENANT_ID);
						int updateRes = jdbcTemplate.update(updateqty, encPassword, user_loginid, userName, EMPLOYEE_ID,
								TENANT_ID);
						if (updateRes == 1) {
							returnMsg = "M0002";
						} else {
							returnMsg = "E0002";
						}

					}
				}
			} else {
				logger.info("<------------resetLoginUsersPassword method--resetuseritem Array Not Found-->");
			}

		} catch (Exception e) {
			logger.error("resetLoginUsersPassword DAO Method Exception--->" + e);
		}
		logger.info(" resetLoginUsersPassword DAO method End");
		return returnMsg;
	}

	@Override
	public List<ResetPasswordEmpEntity> getResetLoginUsersInfo(JSONObject jsonObj) {
		logger.info(" getResetLoginUsersInfo DAO method Start");
		List<ResetPasswordEmpEntity> list = null;
		try {

			if (jsonObj.has("useritem")) {
				logger.info("<------------getResetLoginUsersInfo method--resetpassworditem Array Found-->");
				JSONArray useritems = jsonObj.getJSONArray("useritem");

				String updateqty = "SELECT \r\n" + "    ul.user_loginid as user_loginid,\r\n"
						+ "    ul.userName as userName,\r\n" + "    ul.EMPLOYEE_ID as EMPLOYEE_ID,\r\n"
						+ "    ul.isActive as isActive,\r\n" + "    ul.TENANT_ID as TENANT_ID,\r\n"
						+ "    emp.EMPLOYEE_FIRSTNAME as EMPLOYEE_FIRSTNAME,\r\n"
						+ "    ed.DESIGNATION_NAME As DESIGNATION_NAME\r\n" + "FROM\r\n" + "    user_login AS ul,\r\n"
						+ "    employee_mst AS emp, employee_designation AS ed\r\n" + "WHERE\r\n"
						+ "    ul.EMPLOYEE_ID = emp.EMPLOYEE_ID\r\n"
						+ "        AND ul.TENANT_ID = emp.TENANT_ID  AND ed.DESIGNATION_CODE = emp.DESIGNATION_CODE\r\n"
						+ "        AND ul.userName = ?\r\n" + "        and ul.TENANT_ID = ?";

				for (int i = 0; i < useritems.length(); i++) {

					JSONObject objects = useritems.getJSONObject(i);
					JSONArray keys = objects.names();

					String userName = "";
					String TENANT_ID = "";

					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("userName")) {
							userName = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						}
					}

					if (userName != "") {
						logger.info("<===userName==>" + userName + "<===TENANT_ID==>" + TENANT_ID);
						RowMapper<ResetPasswordEmpEntity> rowMapper = new ResetPasswordEmpEntityRowMapper();
						list = this.jdbcTemplate.query(updateqty, rowMapper, userName, TENANT_ID);

					}
				}
			} else {
				logger.info("<------------getResetLoginUsersInfo method--resetpassworditem Array Not Found-->");
			}

		} catch (Exception e) {
			logger.error("getResetLoginUsersInfo DAO Method Exception--->" + e);
		}
		logger.info(" getResetLoginUsersInfo DAO method End");
		return list;
	}

	@Override
	public List<UserScreenMap> getUserScreenInfo(int roleid, String moduleid, String tenantid) {
		List<UserScreenMap> list = null;
		logger.info("getUserScreenInfo method start");
		try {

			String query = "SELECT                  \r\n" + "us.UI_SCREEN_MST_ID AS UI_SCREEN_MST_ID,\r\n"
					+ "us.DESCRIPTION AS SCREEN_DESCRIPTION,  ui.DESCRIPTION AS MODULE_DESCRIPTION,\r\n"
					+ "us.DISPLAY_NAME AS SCREEN_DISPLAY_NAME, us.IS_ACTIVE AS IS_ACTIVE,\r\n"
					+ "us.TENANT_ID AS TENANT_ID \r\n" + "FROM     \r\n" + "ui_module_mst AS ui,\r\n"
					+ "ui_screen_mst AS us\r\n" + "WHERE     \r\n" + "ui.UI_MODULE_MST_ID = us.UI_MODULE_MST_ID\r\n"
					+ "and ui.UI_MODULE_MST_ID = ?\r\n" + "AND ui.TENANT_ID = us.TENANT_ID\r\n"
					+ "AND us.IS_ACTIVE = 1 AND ui.TENANT_ID = ?   ORDER BY ui.DESCRIPTION";
			RowMapper<UserScreenMap> rowMapper = new UserScreenMapRowMapper();
			list = this.jdbcTemplate.query(query, rowMapper, moduleid, tenantid);

		} catch (Exception e) {
			logger.error("getUserAndRoleinfo DAO Method Exception--->" + e);
		}
		logger.info(" getUserAndRoleinfo DAO method end");
		return list;
	}

	@Override
	public List<UserScreenMap> getScreenIdByRole(int roleid, String moduleid, String tenantid) {
		List<UserScreenMap> list = null;
		logger.info("getScreenIdByRole methos start");
		try {
			String query = "SELECT \r\n" + "    role.UI_SCREEN_MST_ID, role.TENANT_ID\r\n" + "FROM\r\n"
					+ "    ui_role_screen_mapping role,\r\n" + "    ui_screen_mst scr\r\n" + "WHERE\r\n"
					+ "    USER_ROLE_ID = ? \r\n" + "        AND role.TENANT_ID = ? \r\n"
					+ "        AND scr.UI_MODULE_MST_ID = ? \r\n"
					+ "        AND role.UI_SCREEN_MST_ID = scr.UI_SCREEN_MST_ID\r\n"
					+ "        AND role.TENANT_ID=scr.TENANT_ID";
			RowMapper<UserScreenMap> rowMapper = new UserScreenByIdRowMapper();

			list = this.jdbcTemplate.query(query, rowMapper, roleid, tenantid, moduleid);
		} catch (Exception ex) {
			logger.error("getScreenIdByRole method exception " + ex);
		}
		logger.info("getScreenIdByRole method end");
		return list;
	}

	@Override
	public String updateUserRoleMapping(JSONObject jsonObj) {
		String returnMsg = "";
		logger.info("updateUserRoleMapping DAO mEthod start");
		try {

			if (jsonObj.has("userdeleteitem")) {
				JSONArray useritem = jsonObj.getJSONArray("userdeleteitem");
				for (int i = 0; i < useritem.length(); i++) {

					JSONObject objects = useritem.getJSONObject(i);
					JSONArray keys = objects.names();

					String UI_MODULE_MST_ID = "";
					String USER_ROLE_ID = "";
					String TENANT_ID = "";
					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("UI_MODULE_MST_ID")) {
							UI_MODULE_MST_ID = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						} else if (key.equalsIgnoreCase("USER_ROLE_ID")) {
							USER_ROLE_ID = value;
						}
					}
					String delete = "delete from  ui_role_screen_mapping where USER_ROLE_ID=? and UI_SCREEN_MST_ID in \r\n"
							+ " (select UI_SCREEN_MST_ID from ui_screen_mst where  UI_MODULE_MST_ID=? \r\n"
							+ " and ui_role_screen_mapping.UI_SCREEN_MST_ID= ui_screen_mst.UI_SCREEN_MST_ID  and ui_role_screen_mapping.TENANT_ID =?)";
					int deleteres = this.jdbcTemplate.update(delete,
							new Object[] { USER_ROLE_ID, UI_MODULE_MST_ID, TENANT_ID });

					if (deleteres == 1) {
						logger.info("DELETE ui_role_screen_mapping UI_MODULE_MST_ID -->" + UI_MODULE_MST_ID
								+ "-->deleted succesfully");
					} else {
						logger.info("DELETE ui_role_screen_mapping UI_MODULE_MST_ID -->" + UI_MODULE_MST_ID
								+ "-->delete failed");
					}
				}

			}
			if (jsonObj.has("useritems")) {
				JSONArray useritems = jsonObj.getJSONArray("useritems");

				for (int i = 0; i < useritems.length(); i++) {

					JSONObject objects = useritems.getJSONObject(i);
					JSONArray keys = objects.names();

					String USER_ROLE_ID = "";
					String UI_SCREEN_MST_ID = "";
					String TENANT_ID = "";
					for (int j = 0; j < keys.length(); ++j) {
						String key = keys.getString(j);
						String value = objects.getString(key);

						if (key.equalsIgnoreCase("USER_ROLE_ID")) {
							USER_ROLE_ID = value;
						} else if (key.equalsIgnoreCase("UI_SCREEN_MST_ID")) {
							UI_SCREEN_MST_ID = value;
						} else if (key.equalsIgnoreCase("TENANT_ID")) {
							TENANT_ID = value;
						}
					}

					String updatequery = "insert into ui_role_screen_mapping (USER_ROLE_ID,UI_SCREEN_MST_ID,TENANT_ID) values(?,?,?)";
					int res = this.jdbcTemplate.update(updatequery, USER_ROLE_ID, UI_SCREEN_MST_ID, TENANT_ID);

					if (res == 1) {
						returnMsg = "M0001";
					} else {
						returnMsg = "E0001";
					}
				}
			} else {
				returnMsg = "M0001";
			}

		} catch (Exception ex) {
			logger.error("updateUserRoleMapping DAO Exception-->" + ex);
		}
		logger.info("updateUserRoleMapping DAO method end");
		return returnMsg;
	}

	@Override
	public List<UIModuleMst> getModules(JSONObject jsonObj) {
		List<UIModuleMst> list = null;
		logger.info("getModules methos start");
		try {
			JSONArray useritems = jsonObj.getJSONArray("moduleitem");
			for (int i = 0; i < useritems.length(); i++) {

				JSONObject objects = useritems.getJSONObject(i);
				JSONArray keys = objects.names();

				String TENANT_ID = "";
				for (int j = 0; j < keys.length(); ++j) {
					String key = keys.getString(j);
					String value = objects.getString(key);

					if (key.equalsIgnoreCase("TENANT_ID")) {
						TENANT_ID = value;
					}
				}

				String query = "SELECT * FROM ui_module_mst where TENANT_ID=? and IS_ACTIVE=1";
				RowMapper<UIModuleMst> rowMapper = new UIModuleMstRowMapper();
				list = this.jdbcTemplate.query(query, rowMapper, TENANT_ID);
			}
		} catch (Exception ex) {
			logger.error("getModules method exception " + ex);
		}
		logger.info("getModules method end");
		return list;
	}

	@Override
	public List<LoggedInUserDetailsHdr> getLoggedUserDetails(JSONObject jsonObj) {
		logger.info("getLoggedUserDetails method Starts");
		List<LoggedInUserDetailsHdr> list = null;
		try {
			JSONArray useritems = jsonObj.getJSONArray("getArray");
			for (int i = 0; i < useritems.length(); i++) {

				JSONObject objects = useritems.getJSONObject(i);
				JSONArray keys = objects.names();

				String TENANT_ID = "", DATE = "", EMPLOYEE_ID = "";
				for (int j = 0; j < keys.length(); ++j) {
					String key = keys.getString(j);
					String value = objects.getString(key);

					if (key.equalsIgnoreCase("TENANT_ID")) {
						TENANT_ID = value;
					} else if (key.equalsIgnoreCase("DATE")) {
						DATE = value;
					} else if (key.equalsIgnoreCase("EMPLOYEE_ID")) {
						EMPLOYEE_ID = value;
					}
				}

				if (EMPLOYEE_ID.equalsIgnoreCase("")) {
					EMPLOYEE_ID = "%%";

				}
				String query = "select ld.EMPLOYEE_ID,COUNT(ld.EMPLOYEE_ID) AS COUNT_LOGGED_IN, em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_NAME\r\n"
						+ " from employee_mst em \r\n" + "inner join user_logged_in_dtl ld \r\n"
						+ "on em.EMPLOYEE_ID=ld.EMPLOYEE_ID \r\n"
						+ "where ld.TENANT_ID=?  AND DATE(LOGGED_IN_TIME)=? and ld.EMPLOYEE_ID like ?  and STATUS='1' \r\n"
						+ " and em.TENANT_ID = ld.TENANT_ID \r\n" + "GROUP BY ld.EMPLOYEE_ID ;";
				RowMapper<LoggedInUserDetailsHdr> rowMapper = new LoggedInUserDetailsRowMapper();
				list = this.jdbcTemplate.query(query, rowMapper, TENANT_ID, DATE, EMPLOYEE_ID);

			}
		} catch (Exception e) {
			logger.info("getLoggedUserDetails method exception" + e);
		}
		return list;
	}

	@Override
	public List<LoginUserInfoHdr> getUserInfo(JSONObject jsonObj) {
		logger.info("getUserInfo DAO Starts");
		List<LoginUserInfoHdr> list = null;
		try {
			JSONArray useritems = jsonObj.getJSONArray("getArray");
			for (int i = 0; i < useritems.length(); i++) {

				JSONObject objects = useritems.getJSONObject(i);
				JSONArray keys = objects.names();

				String EMPLOYEE_ID = "", DATE = "", TENANT_ID = "";
				for (int j = 0; j < keys.length(); ++j) {
					String key = keys.getString(j);
					String value = objects.getString(key);

					if (key.equalsIgnoreCase("EMPLOYEE_ID")) {
						EMPLOYEE_ID = value;
					} else if (key.equalsIgnoreCase("DATE")) {
						DATE = value;
					} else if (key.equalsIgnoreCase("TENANT_ID")) {
						TENANT_ID = value;
					}
				}

				if (EMPLOYEE_ID.equalsIgnoreCase("")) {
					EMPLOYEE_ID = "%%";

				}
				String query = "select LOGGED_IN_TIME,EMPLOYEE_FIRSTNAME,ul.EMPLOYEE_ID from user_logged_in_dtl ul\r\n"
						+ " inner join employee_mst em on em.EMPLOYEE_ID=ul.EMPLOYEE_ID\r\n"
						+ " where ul.EMPLOYEE_ID=? and date(LOGGED_IN_TIME)=? \r\n"
						+ " and ul.TENANT_ID = em.TENANT_ID \r\n" + "  and ul.TENANT_ID= ?";
				RowMapper<LoginUserInfoHdr> rowMapper = new LoginUserDtlRowMapper();
				list = this.jdbcTemplate.query(query, rowMapper, EMPLOYEE_ID, DATE, TENANT_ID);

			}

		} catch (Exception e) {
			logger.error("getUserInfo method Exception" + e);
		}
		return list;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String getLoginPwdQ = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN userPassword\r\n"
				+ "        ELSE 0\r\n" + "    END userPassword\r\n" + "FROM\r\n" + "    user_login\r\n" + "WHERE\r\n"
				+ "    userName =?";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(getLoginPwdQ,username);
		String loginPwd = resultMap.get("userPassword").toString();

		return new User(username, loginPwd, new ArrayList<>());

	}

	public ResponseAsList getUserDtlForAuth(String username, String password, String token) {
		logger.info(" getUserDtlForAuth DAO method Start");
		ResponseAsList list = new ResponseAsList();
		List<UserLogin> userLogin = new ArrayList<UserLogin>();
		try {
			String insertLogged = "insert into user_logged_in_dtl (USER_ID,LOGGED_IN_TIME,STATUS,TENANT_ID) values (?,?,?,?)";

			String userLoginDtlStr = "SELECT \r\n" + "    ul.USER_LOGIN_ID AS USERLOGINID,\r\n"
					+ "    ul.userName AS USERNAME,\r\n" + "    em.EMPLOYEE_ID AS EMPLOYEE_ID,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME,\r\n"
					+ "    em.EMPLOYEE_LASTNAME AS EMPLOYEE_LASTNAME,\r\n"
					+ "    ed.DESIGNATION_NAME AS DESIGNATION_NAME,\r\n" + "    em.TENANT_ID AS TENANT_ID,\r\n"
					+ "    em.DEPARTMENT_CODE AS DEPARTMENT_CODE,\r\n"
					+ "    em.DESIGNATION_CODE AS DESIGNATION_CODE,dep.DEPARTMENT_NAME As DEPT_NAME,\r\n"
					+ "    ur.USER_ROLE_ID AS USERROLEID,\r\n" + "    ur.role_code AS ROLECODE,\r\n"
					+ "    ur.role_name AS ROLENAME\r\n" + "FROM\r\n" + "    user_login ul,\r\n"
					+ "    employee_mst em\r\n" + "    INNER JOIN\r\n"
					+ "    employee_designation ed INNER JOIn department dep INNER JOIN\r\n"
					+ "    user_role_mapping AS urm INNER JOIN\r\n" + "    user_role AS ur\r\n" + "WHERE\r\n"
					+ "    ul.EMPLOYEE_ID = em.EMPLOYEE_ID\r\n" + "        AND ul.TENANT_ID = em.TENANT_ID\r\n"
					+ "        AND em.DESIGNATION_CODE = ed.DESIGNATION_CODE\r\n"
					+ "        AND em.TENANT_ID = ed.TENANT_ID AND dep.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n"
					+ "        AND ul.userName = ? \r\n" + "        AND urm.USER_LOGINID =  ul.USER_LOGIN_ID\r\n"
					+ "        AND  urm.USER_ROLEID = ur.USER_ROLE_ID\r\n" + "        AND ul.IS_ACTIVE = 1\r\n"
					+ "        AND em.EMPLOYMENT_STATUS_CODE = 'ES001'";
			userLogin = this.jdbcTemplate.query(userLoginDtlStr, new UserLoginRowMapper(), username);
			if (userLogin.size() > 0) {
				this.jdbcTemplate.update(insertLogged, username, CommonMethod.getCurrentDateTime(), "1",
						userLogin.get(0).getTenantID());
				userLogin.get(0).setJwtToken(token);
				list.setResponseData(userLogin);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				this.jdbcTemplate.update(insertLogged, username, CommonMethod.getCurrentDateTime(), "0", "");
				list.setResponseData(userLogin);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);

				userLogin.get(0).setStatus("0");

			}

		} catch (Exception ex) {
			logger.error("getUserLoginInfo DAO Method Exception" + ex);
		}
		logger.info("getUserLoginInfo DAO method end");
		return list;

	}

	@Override
	public List<UserLogin> getUserLoginInfo(String username, String password, String ipaddress, String hostname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseAsList getMenuByUserRole(GetMenuByUserRoleRequest getMenuByUserRoleReq) {

		ResponseAsList list = new ResponseAsList();
		List<GetMenuByUserRoleResponse> getMenuByUserRoleListResp = new ArrayList<GetMenuByUserRoleResponse>();
		try {
			GetMenuByUserRoleResponse getMenuByUserRoleResp = new GetMenuByUserRoleResponse();
			String userRoleListStr = "SELECT \r\n" + "    mm.UI_MODULE_MST_ID AS UI_MODULE_MST_ID,\r\n"
					+ "    mm.DESCRIPTION AS DESCRIPTION,\r\n" + "    mm.DISPLAY_NAME AS DISPLAY_NAME,\r\n"
					+ "    mm.ICON AS MATERIAL_ICON,\r\n" +
					// " mm.LINK_URL AS LINK_URL,\r\n" +
					"    mm.SEQ_NO AS SEQ_NO,\r\n" + "    mm.IS_ACTIVE AS IS_ACTIVE,\r\n"
					+ "    mm.TENANT_ID AS TENANT_ID\r\n" + "FROM\r\n" + "    ui_module_mst AS mm\r\n" + "WHERE\r\n"
					+ "    mm.IS_ACTIVE = 1 AND mm.TENANT_ID = ?\r\n"
					+ "        AND mm.UI_MODULE_MST_ID IN (SELECT DISTINCT\r\n"
					+ "            (sm.UI_MODULE_MST_ID)\r\n" + "        FROM\r\n"
					+ "            ui_screen_mst AS sm\r\n" + "        WHERE\r\n"
					+ "            sm.UI_SCREEN_MST_ID IN (SELECT \r\n" + "                    UI_SCREEN_MST_ID\r\n"
					+ "                FROM\r\n" + "                    ui_role_screen_mapping AS rsm\r\n"
					+ "                WHERE\r\n" + "                    rsm.USER_ROLE_ID = ?\r\n"
					+ "                        AND rsm.TENANT_ID = ?)\r\n" + "                AND sm.IS_ACTIVE = 1\r\n"
					+ "                AND sm.TENANT_ID = ?\r\n" + "        ORDER BY sm.SEQ_NO ASC)\r\n"
					+ "   ORDER BY mm.SEQ_NO ASC; ";
			List<UIModuleMst> userRoleList = this.jdbcTemplate.query(userRoleListStr, new UIModuleMstRowMapper(),
					getMenuByUserRoleReq.getTenantId(), getMenuByUserRoleReq.getUserRoleId(),
					getMenuByUserRoleReq.getTenantId(), getMenuByUserRoleReq.getTenantId());
			if (userRoleList.size() > 0) {
				for (int i = 0; i < userRoleList.size(); i++) {
					String uiscreenList = "SELECT \r\n" + "    sm.UI_MODULE_MST_ID AS UI_MODULE_MST_ID,\r\n"
							+ "    sm.UI_SCREEN_MST_ID AS UI_SCREEN_MST_ID,\r\n"
							+ "    sm.DESCRIPTION AS DESCRIPTION,\r\n" + "    sm.DISPLAY_NAME AS DISPLAY_NAME,\r\n"
							+ "    sm.MATERIAL_ICON AS MATERIAL_ICON,\r\n" +
							// " sm.LINK_URL AS LINK_URL,\r\n" +
							"    sm.SEQ_NO AS SEQ_NO,\r\n" + "    sm.IS_ACTIVE AS IS_ACTIVE,\r\n"
							+ "    sm.SUB_MODULE AS SUB_MODULE,\r\n" + "    sm.TENANT_ID AS TENANT_ID\r\n" + "FROM\r\n"
							+ "    ui_screen_mst AS sm\r\n" + "WHERE\r\n" + "    sm.UI_SCREEN_MST_ID IN (SELECT \r\n"
							+ "            UI_SCREEN_MST_ID\r\n" + "        FROM\r\n"
							+ "            ui_role_screen_mapping AS rsm\r\n" + "        WHERE\r\n"
							+ "            rsm.USER_ROLE_ID = ? \r\n" + "                AND rsm.TENANT_ID = ? )\r\n"
							+ "        AND sm.IS_ACTIVE = 1\r\n" + "        AND sm.TENANT_ID = ?\r\n"
							+ "        AND sm.UI_MODULE_MST_ID = ? \r\n"
							+ "ORDER BY sm.SUB_MODULE ASC,sm.DISPLAY_NAME ASC;";
					List<UIScreenMst> uiScreenMstlist = this.jdbcTemplate.query(uiscreenList,
							new UIScreenMstRowMapper(), getMenuByUserRoleReq.getUserRoleId(),
							getMenuByUserRoleReq.getTenantId(), getMenuByUserRoleReq.getTenantId(),
							userRoleList.get(i).getUiModuleMstID());
					userRoleList.get(i).setUiScreenMstList(uiScreenMstlist);

				}
				String landingPageStr = "SELECT \r\n"
						+ "    case when count(*)> 0 then LINK_URL else '' end As LINK_URL\r\n" + "FROM\r\n"
						+ "    user_login ul\r\n" + "        INNER JOIN\r\n"
						+ "    employee_mst em ON em.EMPLOYEE_ID = ul.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
						+ "    landing_page_config lpc ON lpc.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n"
						+ "        AND lpc.DESIGNATION_CODE = em.DESIGNATION_CODE\r\n" + "WHERE\r\n"
						+ "    ul.USER_LOGIN_ID = ? and ul.TENANT_ID =? ";
				
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(landingPageStr,getMenuByUserRoleReq.getLoginId(),getMenuByUserRoleReq.getTenantId());
				String landingPage = resultMap.get("LINK_URL").toString();
				
				getMenuByUserRoleResp.setUiModule(userRoleList);
				getMenuByUserRoleResp.setLandingPageUrl(landingPage);
				getMenuByUserRoleListResp.add(getMenuByUserRoleResp);
				list.setResponseData(getMenuByUserRoleListResp);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				list.setResponseData(getMenuByUserRoleListResp);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getMenuByUserRole DAO Method Exception" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage getlinkurlStatus(GetlinkurlStatusRequest getlinkurlStatusReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			String linkurlStatusStr = "SELECT \r\n" + "    count(*) AS COUNT\r\n" + "FROM\r\n" + "    ui_screen_mst usm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    ui_role_screen_mapping ursm ON ursm.UI_SCREEN_MST_ID = usm.UI_SCREEN_MST_ID\r\n"
					+ "WHERE\r\n" + "    usm.COMPONENT_DTL = ?'\r\n"
					+ "        AND ursm.USER_ROLE_ID =? \r\n"
					+ "        AND usm.IS_ACTIVE = 1 \r\n" + "        AND usm.TENANT_ID =? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(linkurlStatusStr,getlinkurlStatusReq.getLinkUrl(),
									getlinkurlStatusReq.getRoleId(),getlinkurlStatusReq.getTenantId());
			int linkurlStatus  = Integer.parseInt(result.get("COUNT").toString());

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
	public List<UIModuleMst> getuserRoleList(String roleId, String tenantId) {
		List<UIModuleMst> userRoleList = null;
		try {
			String userRoleListStr = "SELECT \r\n" + "    mm.UI_MODULE_MST_ID AS UI_MODULE_MST_ID,\r\n"
					+ "    mm.DESCRIPTION AS DESCRIPTION,\r\n" + "    mm.DISPLAY_NAME AS DISPLAY_NAME,\r\n"
					+ "    mm.ICON AS MATERIAL_ICON,\r\n" +
					// " mm.LINK_URL AS LINK_URL,\r\n" +
					"    mm.SEQ_NO AS SEQ_NO,\r\n" + "    mm.IS_ACTIVE AS IS_ACTIVE,\r\n"
					+ "    mm.TENANT_ID AS TENANT_ID\r\n" + "FROM\r\n" + "    ui_module_mst AS mm\r\n" + "WHERE\r\n"
					+ "    mm.IS_ACTIVE = 1 AND mm.TENANT_ID = ?\r\n"
					+ "        AND mm.UI_MODULE_MST_ID IN (SELECT DISTINCT\r\n"
					+ "            (sm.UI_MODULE_MST_ID)\r\n" + "        FROM\r\n"
					+ "            ui_screen_mst AS sm\r\n" + "        WHERE\r\n"
					+ "            sm.UI_SCREEN_MST_ID IN (SELECT \r\n" + "                    UI_SCREEN_MST_ID\r\n"
					+ "                FROM\r\n" + "                    ui_role_screen_mapping AS rsm\r\n"
					+ "                WHERE\r\n" + "                    rsm.USER_ROLE_ID = ?\r\n"
					+ "                        AND rsm.TENANT_ID = ?)\r\n" + "                AND sm.IS_ACTIVE = 1\r\n"
					+ "                AND sm.TENANT_ID = ?\r\n" + "        ORDER BY sm.SEQ_NO ASC)\r\n"
					+ "   ORDER BY mm.SEQ_NO ASC; ";
			userRoleList = this.jdbcTemplate.query(userRoleListStr, new UIModuleMstRowMapper(), tenantId, roleId,
					tenantId, tenantId);
		} catch (Exception ex) {
			logger.error("userRoleList Error " + ex);
		}
		return userRoleList;
	}

	@Override
	public List<UIScreenMst> getuiscreenList(String roleId, String tenantId, String moduleId) {
		List<UIScreenMst> uiScreenMstlist = null;
		try {
			String uiscreenList = "SELECT \r\n" + "    sm.UI_MODULE_MST_ID AS UI_MODULE_MST_ID,\r\n"
					+ "    sm.UI_SCREEN_MST_ID AS UI_SCREEN_MST_ID,\r\n" + "    sm.DESCRIPTION AS DESCRIPTION,\r\n"
					+ "    sm.DISPLAY_NAME AS DISPLAY_NAME,\r\n" + "    sm.MATERIAL_ICON AS MATERIAL_ICON,\r\n"
					+ "    sm.COMPONENT_DTL AS LINK_URL,\r\n" + "    sm.SEQ_NO AS SEQ_NO,\r\n"
					+ "    sm.IS_ACTIVE AS IS_ACTIVE,\r\n" + "    sm.SUB_MODULE AS SUB_MODULE,\r\n"
					+ "    sm.TENANT_ID AS TENANT_ID\r\n" + "FROM\r\n" + "    ui_screen_mst AS sm\r\n" + "WHERE\r\n"
					+ "    sm.UI_SCREEN_MST_ID IN (SELECT \r\n" + "            UI_SCREEN_MST_ID\r\n"
					+ "        FROM\r\n" + "            ui_role_screen_mapping AS rsm\r\n" + "        WHERE\r\n"
					+ "            rsm.USER_ROLE_ID = ? \r\n" + "                AND rsm.TENANT_ID = ? )\r\n"
					+ "        AND sm.IS_ACTIVE = 1\r\n" + "        AND sm.TENANT_ID = ?\r\n"
					+ "        AND sm.UI_MODULE_MST_ID = ? \r\n" + "ORDER BY sm.SEQ_NO;";
			uiScreenMstlist = this.jdbcTemplate.query(uiscreenList, new UIScreenMstRowMapper(), roleId, tenantId,
					tenantId, moduleId);
		} catch (Exception ex) {
			logger.error("getuiscreenList error " + ex);
		}
		return uiScreenMstlist;
	}

	@Override
	public String onloadLandingPage(String loginId, String tenantId) {
		String langingPage = "";
		try {
			String landingPageStr = "SELECT \r\n"
					+ "    case when count(*)> 0 then LINK_URL else '' end As LINK_URL\r\n" + "FROM\r\n"
					+ "    user_login ul\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst em ON em.EMPLOYEE_ID = ul.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    landing_page_config lpc ON lpc.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n"
					+ "       \r\n" + "WHERE\r\n"
					+ "    ul.USER_LOGIN_ID = ? and ul.TENANT_ID =? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(landingPageStr,loginId,tenantId);
			langingPage = resultMap.get("LINK_URL").toString();

		} catch (Exception ex) {
			logger.error("onloadLandingPage Error " + ex);
		}
		return langingPage;
	}

	@Override
	public int getActivelinkurlStatus(String linkUrl, String roleId, String tenantId) {
		int linkurlStatus = 0;
		try {
			String linkurlStatusStr = "SELECT \r\n" + "    count(*) as COUNT\r\n" + "FROM\r\n" + "    ui_screen_mst usm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    ui_role_screen_mapping ursm ON ursm.UI_SCREEN_MST_ID = usm.UI_SCREEN_MST_ID\r\n"
					+ "WHERE\r\n" + "    usm.COMPONENT_DTL = ?\r\n"
					+ "        AND ursm.USER_ROLE_ID = ? \r\n" + "        AND usm.IS_ACTIVE = 1 \r\n"
					+ "        AND usm.TENANT_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(linkurlStatusStr,linkUrl,roleId,tenantId);
			linkurlStatus = Integer.parseInt(result.get("COUNT").toString());


		} catch (Exception ex) {
			logger.error("getlinkurlStatus DAO Method Exception" + ex);
		}
		return linkurlStatus;
	}

	@Override
	public String onLoadCurrency(String tenantId) {
		String Curr = "";
		try {
			String linkurlStatusStr = "SELECT CURRENCY FROM organization_info where TENANT_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(linkurlStatusStr,tenantId);
			Curr = resultMap.get("CURRENCY").toString();
			
		} catch (Exception ex) {
			logger.error("getlinkurlStatus DAO Method Exception" + ex);
		}
		return Curr;
	}

	@Override
	public List<LinkUrlEntity> getUrlDtls(String userRoleId, String tenantId) {
		// TODO Auto-generated method stub
		
		List<LinkUrlEntity> list=null;
		
		try {
			
			String moduleIdQry="SELECT \r\n" + 
					"   group_concat( mm.UI_MODULE_MST_ID )AS UI_MODULE_MST_ID\r\n" + 
					"FROM\r\n" + 
					"    ui_module_mst AS mm\r\n" + 
					"WHERE\r\n" + 
					"    mm.IS_ACTIVE = 1 AND mm.TENANT_ID = ?\r\n" + 
					"        AND mm.UI_MODULE_MST_ID IN (SELECT DISTINCT\r\n" + 
					"            (sm.UI_MODULE_MST_ID)\r\n" + 
					"        FROM\r\n" + 
					"            ui_screen_mst AS sm\r\n" + 
					"        WHERE\r\n" + 
					"            sm.UI_SCREEN_MST_ID IN (SELECT \r\n" + 
					"                    UI_SCREEN_MST_ID\r\n" + 
					"                FROM\r\n" + 
					"                    ui_role_screen_mapping AS rsm\r\n" + 
					"                WHERE\r\n" + 
					"                    rsm.USER_ROLE_ID = ?\r\n" + 
					"                        AND rsm.TENANT_ID = ?)\r\n" + 
					"                AND sm.IS_ACTIVE = 1\r\n" + 
					"                AND sm.TENANT_ID = ?\r\n" + 
					"        ORDER BY sm.SEQ_NO ASC)\r\n" + 
					"ORDER BY mm.SEQ_NO ASC; ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(moduleIdQry,tenantId,userRoleId,tenantId,tenantId);
			String uiModuleId = resultMap.get("UI_MODULE_MST_ID").toString();
			
			String getListQry="SELECT \r\n" + 
					"     sm.COMPONENT_DTL AS LINK_URL\r\n" + 
					"FROM\r\n" + 
					"    ui_screen_mst AS sm\r\n" + 
					"WHERE\r\n" + 
					"    sm.UI_SCREEN_MST_ID IN (SELECT \r\n" + 
					"            UI_SCREEN_MST_ID\r\n" + 
					"        FROM\r\n" + 
					"            ui_role_screen_mapping AS rsm\r\n" + 
					"        WHERE\r\n" + 
					"            rsm.USER_ROLE_ID = '"+userRoleId+"'\r\n" + 
					"                AND rsm.TENANT_ID = '"+tenantId+"')\r\n" + 
					"        AND sm.IS_ACTIVE = 1\r\n" + 
					"        AND sm.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND find_in_set( sm.UI_MODULE_MST_ID ,'"+uiModuleId+"') \r\n" + 
					"ORDER BY sm.SEQ_NO;";
			RowMapper<LinkUrlEntity> rm=new LinkUrlRowMapper();
			list=this.jdbcTemplate.query(getListQry, rm);
		}catch(Exception ex) {
			logger.error("getUrlDtls error "+ ex);
		}
		
		return list;
	}

	@Override
	public List<ImageUrlEntity> getImageUrl(String url) {
		List<ImageUrlEntity> response=new ArrayList<>();
		ImageUrlEntity res=new ImageUrlEntity();

		String imageUrl = "";
		try {
			String[] urlSplit = url.split("/");
			String lastName = urlSplit[urlSplit.length - 1];
			String domain=urlSplit[2];

			String tenantId=iGetPropertyValueService.getTenantPropertyVal(domain, jdbcTemplate);
			
			if (lastName.equalsIgnoreCase("404")) {
				imageUrl = iGetPropertyValueService.getTenantPropertyVal("ERROR_IMAGE_URL", jdbcTemplate);
			} else if (lastName.equalsIgnoreCase("login")) {
				String orgURl = "select case when count(*)>0 and IMAGE_URL is not null  then IMAGE_URL else '' end As URL from organization_info where APP_URL = ? ";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(orgURl, url);
				imageUrl = resultMap.get("URL").toString();
			}
			if (imageUrl.equalsIgnoreCase("")) {
				imageUrl = iGetPropertyValueService.getTenantPropertyValByTenantId("DEFAULT_IMAGE_URL",tenantId, jdbcTemplate);
			}
			res.setImageUrl(imageUrl);
			res.setTenantId(tenantId);
			response.add(res);
		} catch (Exception ex) {
			logger.error("getImageUrl DAO Method Exception" + ex);
		}
		return response;
	}
}
