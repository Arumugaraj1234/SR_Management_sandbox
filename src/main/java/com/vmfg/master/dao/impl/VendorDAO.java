package com.vmfg.master.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.IVendorDAO;
import com.vmfg.master.entity.CustomerMstEntity;
import com.vmfg.master.entity.VendorCategoryEntity;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.master.entity.VendorRatingEntity;
import com.vmfg.master.rowmapper.CustomerMasterDtlsRowMapper;
import com.vmfg.master.rowmapper.VendorCategoryRowMapper;
import com.vmfg.master.rowmapper.VendorDtlsRowMapper;
import com.vmfg.master.rowmapper.VendorRatingRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class VendorDAO implements IVendorDAO {
	private static final Logger logger = LoggerFactory.getLogger(VendorDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<VendorMstEntity> getApprVendorDtls(String approved, String tenantId) {
		List<VendorMstEntity> list = new ArrayList<VendorMstEntity>();
		try {
			String qry = "SELECT \r\n" + 
					"    VENDOR_CODE,\r\n" + 
					"    VENDOR_NAME,\r\n" + 
					"    VENDOR_UNIQUE_CODE,\r\n" +
					"    ven.LOCATION_ID,\r\n" + 
					"    BRANCH_CODE,\r\n" + 
					"    GST,\r\n" + 
					"    PAN,\r\n" + 
					"    ARN,\r\n" + 
					"    EMAIL_ID,\r\n" + 
					"    VENDOR_STATUS,\r\n" + 
					"    PO_TYPE,\r\n" + 
					"    CONTACT_NO,\r\n" + 
					"    VENDOR_TYPE,\r\n" + 
					"    VENDOR_CATEGORY,\r\n" + 
					"    GST_TYPE,\r\n" +
					"    CURRENCY_TYPE,\r\n" +
					"    case when INSPECTION_RAISED is not null then INSPECTION_RAISED else 0 end as INSPECTION_RAISED,\r\n" +
					"    case when REINSPECTION_DATE is not null then REINSPECTION_DATE else '' end as REINSPECTION_DATE,\r\n" +
					"    (SELECT INSPECTION_RATING FROM vendor_dtl WHERE VENDOR_CODE = ven.VENDOR_CODE AND IS_LATEST = 1 ORDER BY VDTL_ID DESC LIMIT 1) AS LATEST_INSPECTION_RATING,\r\n" +
					"    (SELECT INSPECTION_DATE FROM vendor_dtl WHERE VENDOR_CODE = ven.VENDOR_CODE AND IS_LATEST = 1 ORDER BY VDTL_ID DESC LIMIT 1) AS LATEST_INSPECTED_DATE,\r\n" +
					"    (SELECT INSPECTION_DATE FROM vendor_dtl WHERE VENDOR_CODE = ven.VENDOR_CODE ORDER BY VDTL_ID ASC LIMIT 1) AS FIRST_INSPECTION_DATE,\r\n" +
					"    ven.TENANT_ID,\r\n" + 
					"    loc.LOCATION_ADDRESSLINE,\r\n" + 
					"    loc.LOCATION_CITY,\r\n" + 
					"    loc.LOCATION_COUNTRY_CODE,\r\n" + 
					"    loc.LOCATION_PINCODE,\r\n" + 
					"    loc.LOCATION_REFERENCENAME,\r\n" + 
					"    loc.LOCATION_STATE\r\n" + 
					"FROM\r\n" + 
					"    vendor_mst ven\r\n" + 
					"        INNER JOIN\r\n" + 
					"    location_mst loc ON ven.LOCATION_ID = loc.LOCATION_ID\r\n" + 
					"WHERE\r\n" + 
					"    VENDOR_STATUS = ?\r\n" + 
					"        AND ven.TENANT_ID = ?";
			list = this.jdbcTemplate.query(qry, new VendorDtlsRowMapper(), approved, tenantId);

		} catch (Exception ex) {
			logger.error("getApprVendorDtls Method Exception --->" + ex);

			return null;
		}
		return list;

	}

	@Override
	public int insertLocDtls(String tenantId, String locReferName, String locAddLine, String city, String state,
			String countryCode, String pinCode) {

		int resp = 0;
		try {
//			String Checkquery = "SELECT * FROM    location_mst\r\n" + "			WHERE    TENANT_ID = '" + tenantId
//					+ "'  AND " + "           LOCATION_REFERENCENAME = '" + locReferName + "'\r\n"
//					+ "           AND LOCATION_ADDRESSLINE = '" + locAddLine + "'\r\n"
//					+ "		    AND LOCATION_CITY = '" + city + "' AND LOCATION_STATE = '" + state + "' "
//					+ "           AND LOCATION_COUNTRY_CODE = '" + countryCode + "' AND LOCATION_PINCODE = '" + pinCode
//					+ "'";
//			List<LocationMstEntity> locationVal = this.jdbcTemplate.query(Checkquery, new LocationMstRowMapper());

//			if (locationVal.size() > 0) {
//				resp = Integer.parseInt(locationVal.get(0).getLocationId());
//			}
//			
//			if (resp == 0) {

				String inquery = "INSERT INTO location_mst (LOCATION_REFERENCENAME,"
						+ "     LOCATION_ADDRESSLINE, LOCATION_CITY,\r\n"
						+ "     LOCATION_STATE, LOCATION_COUNTRY_CODE, LOCATION_PINCODE, TENANT_ID) "
						+ "     VALUES (?,?,?,?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();
				this.jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, locReferName);
						ps.setString(2, locAddLine);
						ps.setString(3, city);
						ps.setString(4, state);
						ps.setString(5, countryCode);
						ps.setString(6, pinCode);
						ps.setString(7, tenantId);

						return ps;
					}
				}, holder);
				int LocId = holder.getKey().intValue();
				resp = LocId;
//			}
		} catch (Exception ex) {
			logger.error("insertLocDtls is method is error" + ex);
		}
		return resp;
	}

	@Override
	public int insertVendorDtls(String vendorName, String gst, String pan, String arn, String email,
			String vendorStatus, String poType, String tenantId, int locId, String contactNo, String vendorType,
			String vendorCategory, String gstType, String currencyType) {

		int resp = 0;
		try {
			String mstCodeQry = "select VENDOR_CODE from vendor_mst order by VENDOR_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(mstCodeQry);
			String mstCodeval = resultMap.get("VENDOR_CODE").toString();
			String vendorCode = CommonMethod.getUomNewCode("BN", mstCodeval, "BN0001");
			String reInsDate=LocalDate.now().minusDays(1).toString();
		    
			
			String mstCodeQry01 = "select COUNT(*) as count from vendor_mst where TENANT_ID = ?  order by VENDOR_CODE desc limit 1";
			Map<String, Object> resultMap01 = jdbcTemplate.queryForMap(mstCodeQry01, tenantId);
			Integer mstCodeval01 = ((Number) resultMap01.get("count")).intValue(); 
			String vendorCode1 ="";
			if(mstCodeval01>0) {
				
				String mstCodeQry1 = "select VENDOR_UNIQUE_CODE from vendor_mst where TENANT_ID = ?  order by VENDOR_UNIQUE_CODE desc limit 1";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(mstCodeQry1,tenantId);
				String mstCodeval1 = resultMap1.get("VENDOR_UNIQUE_CODE").toString();
				vendorCode1 = CommonMethod.getUomNewCode("BN", mstCodeval1, "BN0001");
				
			}else {
				vendorCode1 = "BN0001";
			}
			
			String insVendarMst = "INSERT INTO vendor_mst (VENDOR_CODE, VENDOR_NAME, VENDOR_UNIQUE_CODE, LOCATION_ID,\r\n"
					+ "         GST, PAN, ARN, EMAIL_ID, VENDOR_STATUS, PO_TYPE, TENANT_ID,CONTACT_NO,VENDOR_TYPE,VENDOR_CATEGORY,REINSPECTION_DATE,GST_TYPE,CURRENCY_TYPE)\r\n"
					+ "         values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int insert = this.jdbcTemplate.update(insVendarMst, vendorCode, vendorName,vendorCode1, locId, gst, pan, arn, email,
					vendorStatus, poType, tenantId, contactNo, vendorType, vendorCategory,reInsDate,gstType,currencyType);
			if (insert == 1) {
				resp = 1;
			}
		} catch (Exception ex) {
			logger.error("insertVendorDtls is method is error" + ex);
		}
		return resp;
	}

	@Override
	public int updateVendorDtls(String vendorCode, String vendorName, String gst, String pan, String arn, String email,
			String vendorStatus, String poType, String tenantId, String locationId, String contactNo, String vendorType,
			String vendorCategory, String gstType, String currencyType) {

		int resp = 0;
		try {

			String updateVendor = "UPDATE vendor_mst \n" +
					"SET \n" +
					"    VENDOR_NAME = ?,\n" +
					"    LOCATION_ID = ?,\n" +
					"    GST = ?,\n" +
					"    PAN = ?,\n" +
					"    ARN = ?,\n" +
					"    EMAIL_ID = ?,\n" +
					"    VENDOR_STATUS = ?,\n" +
					"    PO_TYPE = ?,\n" +
					"    VENDOR_CATEGORY = ?,\n" +
					"    GST_TYPE = ?,\n" +
					"    CURRENCY_TYPE=?,\n" +
					"    TENANT_ID = ?,\n" +
					"    CONTACT_NO = ?,\n" +
					"    VENDOR_TYPE = ?\n" +
					"WHERE\n" +
					"    VENDOR_CODE = ?;\n";

			int update = this.jdbcTemplate.update(updateVendor, vendorName, locationId, gst, pan, arn, email,
					vendorStatus, poType, vendorCategory,gstType, currencyType, tenantId, contactNo, vendorType, vendorCode);
			if (update == 1) {
				resp = 1;
			}
		} catch (Exception ex) {
			logger.info("updateVendorDtls is method is error" + ex);
		}

		return resp;
	}

	@Override
	public int updateLocDtls(String locReferName, String locAddLine, String city, String state, String countryCode,
			String pinCode, String locationId, String tenantId) {
		int resp = 0;
		try {
			int updateloc = 0;
			if (tenantId != null && !tenantId.isEmpty()) {
				String updateLoc = " UPDATE location_mst SET LOCATION_REFERENCENAME=?, "
						+ " LOCATION_ADDRESSLINE=?, LOCATION_CITY=?, "
						+ " LOCATION_STATE=?, LOCATION_COUNTRY_CODE=?, "
						+ " LOCATION_PINCODE=?, TENANT_ID=? "
						+ " WHERE LOCATION_ID=?;";
				updateloc = this.jdbcTemplate.update(updateLoc, locReferName, locAddLine, city, state,
						countryCode, pinCode, tenantId, locationId);
			} else {
				String updateLoc = " UPDATE location_mst SET LOCATION_REFERENCENAME=?, "
						+ " LOCATION_ADDRESSLINE=?, LOCATION_CITY=?, "
						+ " LOCATION_STATE=?, LOCATION_COUNTRY_CODE=?, "
						+ " LOCATION_PINCODE=? "
						+ " WHERE LOCATION_ID=?;";
				updateloc = this.jdbcTemplate.update(updateLoc, locReferName, locAddLine, city, state,
						countryCode, pinCode, locationId);
			}
			if (updateloc == 1) {
				resp = 1;
			}
		} catch (Exception ex) {
			logger.info("updateLocDtls this method is error" + ex);
		}
		return resp;
	}

	@Override
	public List<VendorMstEntity> getAllVendorDtls(String vendorCode, String tenantId) {

		List<VendorMstEntity> list = new ArrayList<VendorMstEntity>();
		try {
			String qry = "	SELECT \r\n" + "		ven.*,\r\n" + "		lm.LOCATION_REFERENCENAME, \r\n"
					+ "        lm.LOCATION_ADDRESSLINE, \r\n" + "        lm.LOCATION_CITY, \r\n"
					+ "        lm.LOCATION_STATE, \r\n" + "		lm.LOCATION_COUNTRY_CODE, \r\n"
					+ "        lm.LOCATION_PINCODE\r\n" + "	FROM\r\n" + "		vendor_mst ven\r\n"
					+ "			INNER JOIN\r\n" + "		location_mst lm ON lm.LOCATION_ID = ven.LOCATION_ID\r\n"
					+ "	WHERE\r\n" + "		ven.VENDOR_CODE = ?\r\n" + "			AND ven.TENANT_ID = ?\r\n";
			list = this.jdbcTemplate.query(qry, new VendorDtlsRowMapper(), vendorCode, tenantId);

		} catch (Exception ex) {
			logger.error("getApprVendorDtls Method Exception --->" + ex);
			return null;
		}
		return list;
	}

	@Override
	public List<CustomerMstEntity> getAllCustomerDtl(String tenantID) {
		List<CustomerMstEntity> list = new ArrayList<CustomerMstEntity>();
		try {
			String qry = "SELECT \n" + "    CUST_CODE,CUST_NAME,GST,PAN,ADDRESS,CITY,STATE,CONTACT_NO,COUNTRY,PINCODE\n"
					+ "FROM\n" + "    customer_mst\n" + "WHERE\n" + "    TENANT_ID = ? ";
			list = this.jdbcTemplate.query(qry, new CustomerMasterDtlsRowMapper(), tenantID);
		} catch (Exception ex) {
			logger.error("getAllCustomerDtl Method Exception --->" + ex);
		}

		return list;
	}

	@Override
	public List<VendorCategoryEntity> getVendorCategory(String tenantID) {
		List<VendorCategoryEntity> list = new ArrayList<VendorCategoryEntity>();
		try {
			String qry = "SELECT \n" + "    VC_ID,VC,TENANT_ID\n" + "FROM\n" + "    vendor_category\n" + "WHERE\n"
					+ "    TENANT_ID = ? ";
			list = this.jdbcTemplate.query(qry, new VendorCategoryRowMapper(), tenantID);
		} catch (Exception ex) {
			logger.error("getVendorCategory Method Exception --->" + ex);
		}

		return list;
	}

	@Override
	public String checkSupplyCatCodeExist(String tenantid, String categoryDesc) {
		String count = "";
		try {
			String qry = "select case when count(*)>0 then VC else 'NA' end as count from vendor_category where VC = ? "
					+ " and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, categoryDesc, tenantid);
            count = resultMap.get("count").toString();
		} catch (Exception ex) {
			logger.error("checkSupplyCatCodeExist error " + ex);
		}
		return count;
	}

	@Override
	public String insertVendorCategory(String vendor, String tenantId) {
		// TODO Auto-generated method stub
		String insertRes = "";
		try {

			String inquery = "INSERT INTO `vendor_category`\r\n" + "(\r\n" + "`VC`,\r\n" + "`TENANT_ID`)\r\n"
					+ "VALUES\r\n" + "(?,?);";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, vendor);
					ps.setString(2, tenantId);

					return ps;
				}

			}, holder);
			insertRes = (String) holder.getKeys().get("VC");

		} catch (Exception ex) {
			logger.error("insertVendorCategory error " + ex);
		}
		return insertRes;
	}

	@Override
	public List<VendorRatingEntity> getVendorInspRatingDtls(String vendorCode) {
		List<VendorRatingEntity> list = new ArrayList<VendorRatingEntity>();
		try {
			String qry = "SELECT \r\n" + "    VDTL_ID,\r\n" + "    VENDOR_CODE,\r\n" + "    INSPECTION_DATE,\r\n"
					+ "    INSPECTION_RATING,\r\n" + "    INSPECTED_ON,\r\n"
					+ "    mst.EMPLOYEE_FIRSTNAME as INSPECTED_BY\r\n" + "FROM\r\n" + "    vendor_dtl dtl\r\n"
					+ "        INNER JOIN\r\n" + "    employee_mst mst ON dtl.INSPECTED_BY = mst.EMPLOYEE_ID\r\n"
					+ "WHERE\r\n" + "   VENDOR_CODE  = '" + vendorCode + "'";
			list = this.jdbcTemplate.query(qry, new VendorRatingRowMapper());

		} catch (Exception ex) {
			logger.error("getApprVendorDtls Method Exception --->" + ex);

			return null;
		}
		return list;

	}

	@Override
	public int updateInspectionRaised(String vendorCode) {
		int resp = 0;
		try {
			String updateVendor = "UPDATE vendor_mst SET INSPECTION_RAISED='1' , SEQUENCE_NO='2' WHERE VENDOR_CODE=?;";

			int update = this.jdbcTemplate.update(updateVendor, vendorCode);
			if (update == 1) {
				resp = 1;
			}
		} catch (Exception ex) {
			logger.info("updateVendorDtls is method is error" + ex);
		}

		return resp;
	}

	@Override
	public int vendorDtlInsert(String vendorCode, String inspDate, String inspRating, String empId) {
		int id = 0;
		try {
			String insrtQry = "INSERT INTO vendor_dtl (VENDOR_CODE, INSPECTION_DATE, INSPECTION_RATING, INSPECTED_ON, INSPECTED_BY,IS_LATEST) VALUES (?,?, ?, ?, ?, ?);";

			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, vendorCode);
					ps.setString(2, inspDate);
					ps.setString(3, inspRating);
					ps.setString(4, CommonMethod.getCurrentDateTime());
					ps.setString(5, empId);
					ps.setString(6, "1");
					return ps;
				}
			}, holder);

			id = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("vendorDtlInsert Error" + ex);
		}
		return id;
	}

	@Override
	public int updateInspectionDate(String vendorCode, String inspDate) {
		int resp = 0;
		try {
			String updateVendor = "UPDATE vendor_mst SET REINSPECTION_DATE=? , INSPECTION_RAISED='0' , SEQUENCE_NO='1' WHERE VENDOR_CODE=?";
			resp = this.jdbcTemplate.update(updateVendor, inspDate, vendorCode);

		} catch (Exception ex) {
			logger.info("updateInspectionDate is method is error" + ex);
		}

		return resp;
	}
	
	@Override
	public int updateVendorIsLatest(String vendorCode) {
		int resp = 0;
		try {
			String updateVendor = "UPDATE vendor_dtl SET IS_LATEST='0' WHERE VENDOR_CODE=?";
			resp = this.jdbcTemplate.update(updateVendor, vendorCode);

		} catch (Exception ex) {
			logger.info("updateVendorIsLatest is method is error" + ex);
		}

		return resp;
	}

	@Override
	public String getDmId(String refId, String docType, String uploadDocType) {
		String dmId = "";
		try {
			String getCount = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN DM_ID\r\n"
					+ "        ELSE 0\r\n" + "    END AS DM_ID\r\n" + "FROM\r\n" + "    document_management\r\n"
					+ "WHERE\r\n" + "    REFERENCE_ID = ? AND DOC_TYPE_CODE = ?\r\n"
					+ "        AND UPLOAD_DOC_TYPE = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount, refId, docType, uploadDocType);
			dmId = resultMap.get("DM_ID").toString();

		} catch (Exception ex) {
			logger.error("getDmIdByLatestVerion Method Exception --->" + ex);

		}
		return dmId;
	}

	@Override
	public int getInspectionRaisedVal(String vendorCode, String tenantId) {
		int val = 0;
		try {
			String qry = "SELECT  case when INSPECTION_RAISED is null then 0  else CAST(INSPECTION_RAISED AS UNSIGNED) end as INSPECTION_RAISED FROM vendor_mst WHERE VENDOR_CODE= ? "  
					+ " AND TENANT_ID= ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode, tenantId);
			val = Integer.parseInt(resultMap.get("INSPECTION_RAISED").toString());

		} catch (Exception ex) {
			logger.error("getInspectionRaisedVal error " + ex);
		}
		return val;
	}

	@Override
	public int checkInspectionDate(String vendorCode, String tenantId) {
		int val = 0;
		try {
			String qry = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN REINSPECTION_DATE is null or DATE_SUB(REINSPECTION_DATE, INTERVAL 10 DAY) < CURRENT_DATE THEN 1\r\n"
					+ "        ELSE 0\r\n" + "    END AS date\r\n" + "FROM\r\n" + "    vendor_mst\r\n" + "WHERE\r\n"
					+ "    VENDOR_CODE = ? \r\n" + "        AND TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode, tenantId);
			val = Integer.parseInt(resultMap.get("date").toString());

		} catch (Exception ex) {
			logger.error("checkInspectionDate error " + ex);
		}
		return val;
	}

	@Override
	public String getNextInspDate(String vendorCode, String tenantId) {
		String date = "";
		try {
			String qry = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN REINSPECTION_DATE IS NOT NULL THEN REINSPECTION_DATE \r\n" + "        ELSE ''\r\n"
					+ "    END AS date\r\n" + "FROM\r\n" + "    vendor_mst\r\n" + "WHERE\r\n" + "    VENDOR_CODE = ? "
				    + "        AND TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode, tenantId);
             date = resultMap.get("date").toString();
		} catch (Exception ex) {
			logger.error("getNextInspDate error " + ex);
		}
		return date;
	}

	@Override
	public int getInspRaisedBtn(String vendorCode, String tenantId) {
		int resp = 0;
		try {
			String qry = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN INSPECTION_RAISED IS NOT NULL THEN INSPECTION_RAISED \r\n" + "        ELSE 0 \r\n"
					+ "    END AS date\r\n" + "FROM\r\n" + "    vendor_mst\r\n" + "WHERE\r\n" + "    VENDOR_CODE = ?"
				    + "        AND TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode, tenantId);
			resp = Integer.parseInt(resultMap.get("date").toString());

		} catch (Exception ex) {
			logger.error("getInspRaisedBtn error " + ex);
		}
		return resp;
	}

	@Override
	public int getVendorCurrSeq(String vendorCode) {
		int resp = 0;
		try {
			String qry = "select case when SEQUENCE_NO is not null then SEQUENCE_NO else 1 end as SEQUENCE_NO from vendor_mst where VENDOR_CODE= ? ;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode);
            resp = Integer.parseInt(resultMap.get("SEQUENCE_NO").toString());
		} catch (Exception ex) {
			logger.error("getVendorCurrSeq error " + ex);
		}
		return resp;
	}

	@Override
	public String getLatestVenDtlId(String vendorCode) {
		String dtlId = "";
		try {
			String qry = "select VDTL_ID from vendor_dtl where IS_LATEST='1' and VENDOR_CODE= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorCode);
			dtlId = resultMap.get("VDTL_ID").toString();

		} catch (Exception ex) {
			logger.error("getNextInspDate error " + ex);
		}
		return dtlId;
	}

	@Override
	public List<VendorMstEntity> getVendorRatingDtls(String approved, String tenantId) {
		List<VendorMstEntity> abList = new ArrayList<VendorMstEntity>();
		List<VendorMstEntity> cList = new ArrayList<VendorMstEntity>();
		List<VendorMstEntity> inspNotReqList = new ArrayList<VendorMstEntity>();
		try {
			String abQry = "SELECT \r\n" + 
					"    ven.VENDOR_CODE,\r\n" + 
					"    ven.VENDOR_NAME,\r\n" + 
					"    ven.LOCATION_ID,\r\n" + 
					"    ven.BRANCH_CODE,\r\n" + 
					"    ven.GST,\r\n" + 
					"    ven.PAN,\r\n" + 
					"    ven.ARN,\r\n" + 
					"    ven.EMAIL_ID,\r\n" + 
					"    ven.VENDOR_STATUS,\r\n" + 
					"    ven.PO_TYPE,\r\n" + 
					"    ven.CONTACT_NO,\r\n" + 
					"    ven.VENDOR_TYPE,\r\n" + 
					"    ven.VENDOR_CATEGORY,\r\n" +
					"    ven.CURRENCY_TYPE,\r\n" +
					"    CASE\r\n" + 
					"        WHEN INSPECTION_RAISED IS NOT NULL THEN INSPECTION_RAISED\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS INSPECTION_RAISED,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN REINSPECTION_DATE IS NOT NULL THEN REINSPECTION_DATE\r\n" + 
					"        ELSE ''\r\n" + 
					"    END AS REINSPECTION_DATE,\r\n" + 
					"    ven.TENANT_ID,\r\n" + 
					"    loc.LOCATION_ADDRESSLINE,\r\n" + 
					"    loc.LOCATION_CITY,\r\n" + 
					"    loc.LOCATION_COUNTRY_CODE,\r\n" + 
					"    loc.LOCATION_PINCODE,\r\n" + 
					"    loc.LOCATION_REFERENCENAME,\r\n" + 
					"    loc.LOCATION_STATE,ven.VENDOR_UNIQUE_CODE\r\n" + 
					"FROM\r\n" + 
					"    vendor_mst ven\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_dtl dtl ON ven.VENDOR_CODE = dtl.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    location_mst loc ON ven.LOCATION_ID = loc.LOCATION_ID\r\n" + 
					"WHERE\r\n" + 
					"    VENDOR_STATUS = ? AND ven.VENDOR_TYPE =1 AND ven.VENDOR_CATEGORY != 'General' \r\n" + 
					"        AND ven.TENANT_ID = ?\r\n" + 
					"        AND dtl.IS_LATEST = 1\r\n" + 
					"        AND ven.REINSPECTION_DATE > CURDATE()\r\n" + 
					"        AND dtl.INSPECTION_RATING In( 'A','B') ;";
			abList = this.jdbcTemplate.query(abQry, new VendorDtlsRowMapper(), approved, tenantId);
			
			String inspNotReq =  "SELECT \r\n" + 
					"    ven.VENDOR_CODE,\r\n" + 
					"    ven.VENDOR_NAME,\r\n" + 
					"    ven.LOCATION_ID,\r\n" + 
					"    ven.BRANCH_CODE,\r\n" + 
					"    ven.GST,\r\n" + 
					"    ven.PAN,\r\n" + 
					"    ven.ARN,\r\n" + 
					"    ven.EMAIL_ID,\r\n" + 
					"    ven.VENDOR_STATUS,\r\n" + 
					"    ven.PO_TYPE,\r\n" + 
					"    ven.CONTACT_NO,\r\n" + 
					"    ven.VENDOR_TYPE,\r\n" + 
					"    ven.VENDOR_CATEGORY,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN INSPECTION_RAISED IS NOT NULL THEN INSPECTION_RAISED\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS INSPECTION_RAISED,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN REINSPECTION_DATE IS NOT NULL THEN REINSPECTION_DATE\r\n" + 
					"        ELSE ''\r\n" + 
					"    END AS REINSPECTION_DATE,\r\n" + 
					"    ven.TENANT_ID,\r\n" + 
					"    loc.LOCATION_ADDRESSLINE,\r\n" + 
					"    loc.LOCATION_CITY,\r\n" + 
					"    loc.LOCATION_COUNTRY_CODE,\r\n" + 
					"    loc.LOCATION_PINCODE,\r\n" + 
					"    loc.LOCATION_REFERENCENAME,\r\n" + 
					"    loc.LOCATION_STATE,ven.VENDOR_UNIQUE_CODE\r\n" + 
					"FROM\r\n" + 
					"    vendor_mst ven\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_dtl dtl ON ven.VENDOR_CODE = dtl.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    location_mst loc ON ven.LOCATION_ID = loc.LOCATION_ID\r\n" + 
					"WHERE\r\n" + 
					"    VENDOR_STATUS = ? AND ven.VENDOR_TYPE =1 AND ven.VENDOR_CATEGORY = 'General' \r\n" + 
					"        AND ven.TENANT_ID = ? ";
					
			inspNotReqList =  this.jdbcTemplate.query(inspNotReq, new VendorDtlsRowMapper(), approved, tenantId);
			String inspDate = "select INSPECTION_DATE from vendor_dtl where INSPECTION_RATING = 'C' order by INSPECTION_DATE ASC LIMIT 1;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(inspDate);
			inspDate = resultMap.get("INSPECTION_DATE").toString();
			
//			String cQry = "SELECT \r\n" + 
//					"  ven.VENDOR_CODE,\r\n" + 
//					"    ven.VENDOR_NAME,\r\n" + 
//					"    ven.LOCATION_ID,\r\n" + 
//					"    ven.BRANCH_CODE,\r\n" + 
//					"    ven.GST,\r\n" + 
//					"    ven.PAN,\r\n" + 
//					"    ven.ARN,\r\n" + 
//					"    ven.EMAIL_ID,\r\n" + 
//					"    ven.VENDOR_STATUS,\r\n" + 
//					"    ven.PO_TYPE,\r\n" + 
//					"    ven.CONTACT_NO,\r\n" + 
//					"    ven.VENDOR_TYPE,\r\n" + 
//					"    ven.VENDOR_CATEGORY,\r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN INSPECTION_RAISED IS NOT NULL THEN INSPECTION_RAISED\r\n" + 
//					"        ELSE 0\r\n" + 
//					"    END AS INSPECTION_RAISED,\r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN REINSPECTION_DATE IS NOT NULL THEN REINSPECTION_DATE\r\n" + 
//					"        ELSE ''\r\n" + 
//					"    END AS REINSPECTION_DATE,\r\n" + 
//					"    ven.TENANT_ID,\r\n" + 
//					"    loc.LOCATION_ADDRESSLINE,\r\n" + 
//					"    loc.LOCATION_CITY,\r\n" + 
//					"    loc.LOCATION_COUNTRY_CODE,\r\n" + 
//					"    loc.LOCATION_PINCODE,\r\n" + 
//					"    loc.LOCATION_REFERENCENAME,\r\n" + 
//					"    loc.LOCATION_STATE,ven.VENDOR_UNIQUE_CODE\r\n" + 
//					"FROM\r\n" + 
//					"    vendor_mst ven\r\n" + 
//					"        LEFT JOIN\r\n" + 
//					"    po_hdr po ON ven.VENDOR_CODE = po.VENDOR_CODE\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    vendor_dtl dtl ON ven.VENDOR_CODE = dtl.VENDOR_CODE\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    location_mst loc ON ven.LOCATION_ID = loc.LOCATION_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    VENDOR_STATUS = ? AND ven.VENDOR_TYPE =1 AND ven.VENDOR_CATEGORY != 'General' \r\n" + 
//					"        AND ven.TENANT_ID = ?\r\n" + 
//					"        AND dtl.IS_LATEST = 1\r\n" + 
//					"        AND ven.REINSPECTION_DATE > CURDATE()\r\n" + 
//					"        AND dtl.INSPECTION_RATING = 'C'\r\n" + 
//					"        AND (po.IS_APPROVED = 1 or po.IS_APPROVED  is null or po.IS_APPROVED = 0 ) and (po.IS_LATEST=1  or po.IS_LATEST is null) \r\n" + 
//					"        AND (po.DATE BETWEEN (SELECT case when count(*) < 0 then \r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN INSPECTION_RATING = 'C' THEN INSPECTION_DATE\r\n" + 
//					"        ELSE CURDATE() end else ?\r\n" + 
//					"    END AS INSPECTION_DATE\r\n" + 
//					"FROM\r\n" + 
//					"    vendor_dtl\r\n" + 
//					"WHERE\r\n" + 
//					"    VENDOR_CODE = ven.VENDOR_CODE\r\n" + 
//					"        AND IS_LATEST = '0'\r\n" + 
//					"ORDER BY VDTL_ID DESC\r\n" + 
//					"LIMIT 1) AND CURDATE() or po.DATE is null)\r\n" +
//					"GROUP BY ven.VENDOR_CODE having count(ven.VENDOR_CODE) < 5;";
			String cQry ="SELECT \r\n" + 
					"    ven.VENDOR_CODE,\r\n" + 
					"    ven.VENDOR_NAME,\r\n" + 
					"    ven.LOCATION_ID,\r\n" + 
					"    ven.BRANCH_CODE,\r\n" + 
					"    ven.GST,\r\n" + 
					"    ven.PAN,\r\n" + 
					"    ven.ARN,\r\n" + 
					"    ven.EMAIL_ID,\r\n" + 
					"    ven.VENDOR_STATUS,\r\n" + 
					"    ven.PO_TYPE,\r\n" + 
					"    ven.CONTACT_NO,\r\n" + 
					"    ven.VENDOR_TYPE,\r\n" + 
					"    ven.VENDOR_CATEGORY,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN INSPECTION_RAISED IS NOT NULL THEN INSPECTION_RAISED\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS INSPECTION_RAISED,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN REINSPECTION_DATE IS NOT NULL THEN REINSPECTION_DATE\r\n" + 
					"        ELSE ''\r\n" + 
					"    END AS REINSPECTION_DATE,\r\n" + 
					"    ven.TENANT_ID,\r\n" + 
					"    loc.LOCATION_ADDRESSLINE,\r\n" + 
					"    loc.LOCATION_CITY,\r\n" + 
					"    loc.LOCATION_COUNTRY_CODE,\r\n" + 
					"    loc.LOCATION_PINCODE,\r\n" + 
					"    loc.LOCATION_REFERENCENAME,\r\n" + 
					"    loc.LOCATION_STATE,\r\n" + 
					"    ven.VENDOR_UNIQUE_CODE\r\n" + 
					"FROM\r\n" + 
					"    vendor_mst ven\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    po_hdr po ON ven.VENDOR_CODE = po.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_dtl dtl ON ven.VENDOR_CODE = dtl.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    location_mst loc ON ven.LOCATION_ID = loc.LOCATION_ID\r\n" + 
					"WHERE\r\n" + 
					"    VENDOR_STATUS = ?\r\n" + 
					"        AND ven.VENDOR_TYPE = 1\r\n" + 
					"        AND ven.VENDOR_CATEGORY != 'General'\r\n" + 
					"        AND ven.TENANT_ID = ?\r\n" + 
					"        AND dtl.IS_LATEST = 1\r\n" + 
					"        AND ven.REINSPECTION_DATE > CURDATE()\r\n" + 
					"        AND dtl.INSPECTION_RATING = 'C'\r\n" + 
					"        AND (po.IS_APPROVED = 1\r\n" + 
					"        OR po.IS_APPROVED IS NULL\r\n" + 
					"        OR po.IS_APPROVED = 0)\r\n" + 
					"        AND (po.IS_LATEST = 1 OR po.IS_LATEST IS NULL)\r\n" + 
					"        AND (SELECT \r\n" + 
					"            COUNT(*)\r\n" + 
					"        FROM\r\n" + 
					"            po_hdr p\r\n" + 
					"        WHERE\r\n" + 
					"            p.VENDOR_CODE = ven.VENDOR_CODE\r\n" + 
					"                AND ((p.DATE BETWEEN (SELECT \r\n" + 
					"                    COALESCE((SELECT \r\n" + 
					"                                        INSPECTION_DATE\r\n" + 
					"                                    FROM\r\n" + 
					"                                        vendor_dtl\r\n" + 
					"                                    WHERE\r\n" + 
					"                                        VENDOR_CODE = ven.VENDOR_CODE\r\n" + 
					"                                            AND IS_LATEST = 0\r\n" + 
					"                                            AND INSPECTION_RATING = 'C'\r\n" + 
					"                                    ORDER BY INSPECTION_DATE DESC\r\n" + 
					"                                    LIMIT 1),\r\n" + 
					"                                (SELECT \r\n" + 
					"                                        INSPECTION_DATE\r\n" + 
					"                                    FROM\r\n" + 
					"                                        vendor_dtl\r\n" + 
					"                                    WHERE\r\n" + 
					"                                        VENDOR_CODE = ven.VENDOR_CODE\r\n" + 
					"                                            AND IS_LATEST = 1\r\n" + 
					"                                            AND INSPECTION_RATING = 'C'\r\n" + 
					"                                    LIMIT 1),\r\n" + 
					"                                CURDATE())\r\n" + 
					"                ) AND CURDATE())\r\n" + 
					"                OR p.DATE IS NULL)\r\n" + 
					"                AND (p.IS_APPROVED = 1\r\n" + 
					"                OR p.IS_APPROVED IS NULL\r\n" + 
					"                OR p.IS_APPROVED = 0)\r\n" + 
					"                AND (p.IS_LATEST = 1 OR p.IS_LATEST IS NULL)) < 5\r\n" + 
					"GROUP BY ven.VENDOR_CODE;";
			cList = this.jdbcTemplate.query(cQry, new VendorDtlsRowMapper(), approved, tenantId);
	if(cList.size()>0) {
		abList.addAll(cList);
	}
	if(inspNotReqList.size()>0) {
		abList.addAll(inspNotReqList);
	}

		} catch (Exception ex) {
			logger.error("getApprVendorDtls Method Exception --->" + ex);

			return null;
		}
		return abList;

	}

	@Override
	public int checkVendorname(String vendorName, String tenantId) {
		int checkVendorname = 0;
		try {
			String qry = "select count(*) AS VENDOR_COUNT from vendor_mst where VENDOR_NAME= ? and TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, vendorName, tenantId);
			checkVendorname =Integer.parseInt(resultMap.get("VENDOR_COUNT").toString()) ;
		} catch (Exception ex) {
			logger.error("checkVendorname error " + ex);
		}
		return checkVendorname;
	}

	@Override
	public int updateQtyInspectionRating(String qiHdrId, String tenantId,String qtyRating,String inwardRating,String relationshipRating,String customerComplain) {
		int updateQtyInspectionRating = 0;
		try {
			String qry = "UPDATE `quality_inspection_hdr` SET `QUALITY_RATING`= ? , `INWARD_RATING`= ? , `SUPPLIER_RATING`= ? ,`CUSTOMER_COMPLAINT` = ?  WHERE `QI_HDR_ID`= ? ";
			updateQtyInspectionRating = this.jdbcTemplate.update(qry, qtyRating,inwardRating,relationshipRating,customerComplain,qiHdrId);
		} catch (Exception ex) {
			logger.error("updateQtyInspectionRating error " + ex);
		}
		return updateQtyInspectionRating;
	}

	@Override
	public String getvDtlId() {
		// TODO Auto-generated method stub
		String id ="";
		String qry = "SELECT VDTL_ID+1 as VDTL_ID FROM vendor_dtl ORDER BY VDTL_ID DESC LIMIT 1";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
		id = resultMap.get("VDTL_ID").toString();
		
		return id;
	}

	@Override
	public int updateCusMasDtl(CustomerMstEntity updateCus) {
		String query = "UPDATE customer_mst SET GST = ?, PAN = ?, ADDRESS = ?, CONTACT_NO = ? WHERE CUST_CODE = ? ";
		int updated = this.jdbcTemplate.update(query, updateCus.getGstNumber(), updateCus.getPanNumber(), updateCus.getAddress(), updateCus.getContactNumber(), updateCus.getCustomerCode() );
		return updated ;
	}
}
