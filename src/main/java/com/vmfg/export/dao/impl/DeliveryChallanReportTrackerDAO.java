package com.vmfg.export.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.vmfg.export.dao.interfaces.IDeliveryChallanReportTrackerDAO;
import com.vmfg.export.entity.DcRequestDtlEntity;
import com.vmfg.export.entity.DcRequestHdrEntity;
import com.vmfg.export.rowmapper.DcRequestDtlRowMapper;
import com.vmfg.export.rowmapper.DcRequestHdrRowMapper;

@Transactional
@Repository
public class DeliveryChallanReportTrackerDAO implements IDeliveryChallanReportTrackerDAO {
	private static final Logger logger = LoggerFactory.getLogger(DeliveryChallanReportTrackerDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getPropValueByTenant(String tenantId, String propertyName) {
		String propertyValue = "";
		try {
			String qty = "select PROPERTY_VALUE from tenant_property_mst where TENANT_ID =?  and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,tenantId,propertyName);
			String propValue = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValue;
	}

	@Override
	public String getCurrentDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	@Override
	public String getProjectTrackerPath(String tenantId, String key, String isDestination) {
		String reportPathQry = "SELECT \r\n" + "    path\r\n" + "FROM\r\n" + "    document_path_property\r\n"
				+ "WHERE\r\n" + "    key_value = ? AND tenant_id = ?\r\n"
				+ "        AND is_destination = ?";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(reportPathQry,key,tenantId,isDestination);
		String reportPath = resultMap.get("path").toString();
		return reportPath;
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("getConnection Method Exception " + e);
			e.printStackTrace();
		}
		return connection;

	}

	@Override
	public String getDcCodeCode(String tenantId, String dcId) {
		String dcCode = "";
		try {
			String dcCodeQry = "select DC_CODE from dc_hdr where DC_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(dcCodeQry,dcId);
			dcCode = resultMap.get("DC_CODE").toString();

		} catch (Exception ex) {
			logger.error("getDcCodeCode Method Exception " + ex);
			ex.printStackTrace();
		}
		return dcCode;
	}

	@Override
	public String insertdcReqHdr(String reqOn, String reqBy, String pmHdrId, String remarks, String isComplested,
			String tenantId,String mrHdrId) {
		// TODO Auto-generated method stub
		String insertRes = null;
		try {
			String insertQ = "insert into dc_request_hdr (REQUESTED_ON,REQUESTED_BY,PM_HDR_ID,REMARKS,IS_COMPLETED,TENANT_ID,MR_HDR_ID) values (?,?,?,?,?,?,?) ;";
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, reqOn);
					ps.setString(2, reqBy);
					ps.setString(3, pmHdrId);
					ps.setString(4, remarks);
					ps.setString(5, isComplested);
					ps.setString(6, tenantId);
					ps.setString(7, mrHdrId);
					return ps;
				}
			}, holder);
			insertRes = holder.getKey().toString();
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("insertdcReqHdr Dao  method exception" + e);
		}
		return insertRes;
	}

	@Override
	public String insertdcReqDtl(String dcrId, String productId, String descofGoods, String Qty, String closedQty,
			String tenantid) {
		// TODO Auto-generated method stub
		int res = 0;
		String res1 = "";
		try {
			String query = "insert into dc_request_dtl (DCR_ID,PRODUCT_ID,DESC_OF_GOODS,QTY,CLOSED_QTY,TENANT_ID) values (?,?,?,?,?,?) ;";
			res = this.jdbcTemplate.update(query, dcrId, productId, descofGoods, Qty, closedQty, tenantid);
			res1 = String.valueOf(res);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("insertdcReqDtl Dao method exception" + e);
		}
		return res1;
	}

	@Override
	public List<DcRequestHdrEntity> getDcreqHdr(String pmHdrId, String tenantId, String locTypeCode) {

		List<DcRequestHdrEntity> list = new ArrayList<>();
		try {
			DcRequestHdrRowMapper rowMapper = new DcRequestHdrRowMapper();
			String hdr = "SELECT \r\n" + 
					"    DCR_ID,\r\n" + 
					"    hdr.REQUESTED_ON,\r\n" + 
					"    mst.EMPLOYEE_FIRSTNAME AS REQUESTED_BY,\r\n" + 
					"    hdr.PM_HDR_ID,\r\n" + 
					"    mst.EMPLOYEE_FIRSTNAME AS REMARKS,\r\n" + 
					"    hdr.IS_COMPLETED,\r\n" + 
					"    mr.MR_CODE\r\n" + 
					"FROM\r\n" + 
					"    dc_request_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON mst.EMPLOYEE_ID = hdr.REQUESTED_BY\r\n" + 
					"        INNER JOIN\r\n" + 
					"    material_request_hdr mr ON hdr.MR_HDR_ID = mr.MR_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    (\r\n" + 
					"        SELECT DISTINCT MR_HDR_ID, INVENTORY_LOCATION_CODE\r\n" + 
					"        FROM material_request_dtl\r\n" + 
					"        WHERE INVENTORY_LOCATION_CODE = '"+locTypeCode+"'\r\n" + 
					"    ) dtl ON mr.MR_HDR_ID = dtl.MR_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_location_mst ilm ON ilm.INVENTORY_LOCATION_CODE = dtl.INVENTORY_LOCATION_CODE\r\n" + 
					"WHERE\r\n" + 
					"    hdr.PM_HDR_ID = '"+pmHdrId+"'\r\n" + 
					"    AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"    AND hdr.IS_COMPLETED = '0' and mr.IS_CANCELLED = 0";
			list = this.jdbcTemplate.query(hdr, rowMapper);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getDcreqHdr Dao method exception" + e);
		}
		return list;
	}

	@Override
	public List<DcRequestDtlEntity> getDcreqDtl(String dcrId, String tenantId) {

		List<DcRequestDtlEntity> list = new ArrayList<>();
		try {
			DcRequestDtlRowMapper rowMapper = new DcRequestDtlRowMapper();
			String dtlq = "select dtl.DC_DTL_ID,dtl.DCR_ID,dtl.PRODUCT_ID,dtl.DESC_OF_GOODS,dtl.QTY, \r\n" + 
					"					                            (dtl.QTY - dtl.CLOSED_QTY) as PENDING_QTY,dtl.CLOSED_QTY, hdr.MR_HDR_ID , pmst.PRODUCT_CODE\r\n" + 
					"					                            from dc_request_dtl dtl inner join dc_request_hdr hdr \r\n" + 
					"					                            on  hdr.DCR_ID = dtl.DCR_ID inner join product_mst pmst \r\n" + 
					"                                                on dtl.PRODUCT_ID = pmst.PRODUCT_ID\r\n" + 
					"					                            where dtl.DCR_ID = '"+dcrId+"' and dtl.TENANT_ID = '"+tenantId+"' and (dtl.QTY - dtl.CLOSED_QTY) != '0';";
			list = this.jdbcTemplate.query(dtlq, rowMapper);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getDcreqDtl Dao method exception" + e);
		}
		return list;
	}

	@Override
	public int updateDcqty(String dtlId, String qty) {
		// TODO Auto-generated method stub
		int update=0;
		try {
		String query = "update dc_request_dtl set CLOSED_QTY=CLOSED_QTY+? where DC_DTL_ID = ?;";
		update = this.jdbcTemplate.update(query,qty,dtlId);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("updateDcqty DAO method exception"+e);
		}
		
		return update;
	}

	@Override
	public int getdcrId(String dtlId) {
		// TODO Auto-generated method stub
		int dcrId = 0;
		try {
			String query  = "SELECT DCR_ID FROM dc_request_dtl WHERE DC_DTL_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(query,dtlId);
			dcrId = Integer.parseInt(result.get("DCR_ID").toString());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("getdcrId DAO method exception"+e);
		}
		return dcrId;
	}
	
	@Override
	public int getcheckqty(int dcrId) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			String query = "select count(*) as COUNT from dc_request_dtl where dcr_id = ? and qty != closed_qty;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(query,dcrId);
			count = Integer.parseInt(result.get("COUNT").toString());
		}catch (Exception e) {
			logger.error("getcheckqty DAO method exception"+e);
		}
		return count;
	}

	@Override
	public void updateDcrequest(int dcrid) {
		// TODO Auto-generated method stub
		try {
			String update = "update dc_request_hdr set IS_COMPLETED = '1' where DCR_ID =?";
			this.jdbcTemplate.update(update,dcrid);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("updateDcrequest DAO method exception"+e);
		}
		
	}

	@Override
	public String getMrHdrId(String dcrId) {
		String id = "";
			try {
				String query = "select MR_HDR_ID from dc_request_hdr where DCR_ID=?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,dcrId);
				id = resultMap.get("MR_HDR_ID").toString();
			}catch (Exception e) {
				logger.error("getMrHdrId DAO method exception"+e);
			}
			return id;
		}

	@Override
	public void updateMRIscompletedStatus(String mrHdrId) {
		try {
			String update = "UPDATE material_request_hdr SET IS_COMPLETED = 1 WHERE MR_HDR_ID = ?";
			this.jdbcTemplate.update(update, mrHdrId);
		}catch (Exception e) {
			logger.error("updateMRIscompletedStatus DAO method exception"+e);
		}
		
	}

	@Override
	public String getLocTypeCode(String name, String tenantId) {
		String invLocType = "";
		try {
			String invLocTypeStr = "select case when count(*) >0 then INVENTORY_LOC_TYPE else '' end AS LOC_TYPE from organization_location_dtl where LOCATION_REFERENCENAME = ? and TENANT_ID =? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(invLocTypeStr,name,tenantId);
			invLocType = resultMap.get("LOC_TYPE").toString();

		} catch (Exception e) {
			logger.error("getLocTypeCode method Error" + e);
		}
		return invLocType;
	}
	
}
