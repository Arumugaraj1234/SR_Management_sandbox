package com.vmfg.scm.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.rowmapper.IndentDtlTblRowMapper;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.scm.entity.IndentGroupDetailsEntity;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenEntity;
import com.vmfg.scm.entity.IndentGrpScpVenPtEntity;
import com.vmfg.scm.entity.IndentGrpScsStatusEntity;
import com.vmfg.scm.entity.IndentInsertGrpDtlRequest;
import com.vmfg.scm.entity.ScpDtlsEntity;
import com.vmfg.scm.request.IndentGrpDelRequest;
import com.vmfg.scm.request.IndentInsertGrpRequest;
import com.vmfg.scm.request.IndentTemplateNameRequest;
import com.vmfg.scm.rowmapper.IndentGroupDetailsRowMapper;
import com.vmfg.scm.rowmapper.IndentGroupHdrAndDtlRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScpDtlRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScpRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScpVenDtlRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScpVenPtRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScpVenRowMapper;
import com.vmfg.scm.rowmapper.IndentGrpScsStatusRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class IndentGroupDAO implements IIndentGroupDAO {
	private static final Logger logger = LoggerFactory.getLogger(IndentGroupDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<IndentGroupDetailsEntity> getIndentGroupRetrieve(String tenantId, String fromdate, String todate,
			String indentId,String projectId,String empId) {
		String byIndentId="";
		List<IndentGroupDetailsEntity> returnList = new ArrayList<IndentGroupDetailsEntity>();
		try {
			if(indentId.equalsIgnoreCase("getAll")) {
				 byIndentId="";
			}else {
				byIndentId="AND ind.INDENT_ID ='"+indentId+"' ";
			}

			String retQry = "SELECT DISTINCT \n" +
					"    hdr.IG_HDR_ID, \n" +
					"    hdr.CREATED_BY, \n" +
					"    mst.EMPLOYEE_FIRSTNAME,\n" +
					"    inh.INDENT_ID, \n" +
					"    inh.INDENT_CODE, \n" +
					"    hdr.GROUP_NAME, \n" +
					"    inh.SBC_CODE, \n" +
					"    sb.SBC_DESC, \n" +
					"    inh.EXPECTED_DELIVERY_DATE, \n" +
					"    inh.PKA_ID, \n" +
					"    pkam.PK_DESC, \n" +
					"    inh.PKSA_ID, \n" +
					"    pksam.PSK_DESC, \n" +
					"    inh.TARGET_VALUE, \n" +
					"    hdr.IS_INVENTORY\n" +
					"FROM \n" +
					"    indent_grp_hdr hdr\n" +
					"INNER JOIN \n" +
					"    employee_mst mst ON hdr.CREATED_BY = mst.EMPLOYEE_ID\n" +
					"INNER JOIN \n" +
					"    indent_grp_dtl dtl ON dtl.IG_HDR_ID = hdr.IG_HDR_ID\n" +
					"INNER JOIN \n" +
					"    indent_dtl ind ON ind.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n" +
					"INNER JOIN \n" +
					"    indent_hdr inh ON inh.INDENT_ID = ind.INDENT_ID\n" +
					"INNER JOIN \n" +
					"    sales_budget_category sb ON sb.SBC_CODE = inh.SBC_CODE\n" +
					"INNER JOIN \n" +
					"    project_hdr ph ON ph.PM_HDR_ID = inh.PROJECT_ID\n" +
					"INNER JOIN \n" +
					"    project_key_area pka ON pka.PKA_ID = inh.PKA_ID\n" +
					"INNER JOIN \n" +
					"    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID\n" +
					"INNER JOIN \n" +
					"    project_key_sub_area pksa ON pksa.PKSA_ID = inh.PKSA_ID\n" +
					"INNER JOIN \n" +
					"    project_key_sub_area_mst pksam ON pksam.PSK_ID = pksa.PSK_ID\n" +
					"INNER JOIN \n" +
					"    indent_assign_team iat ON ind.INDENT_DTL_ID = iat.INDENT_DTL_ID\n" +
					"WHERE \n" +
					"    inh.PROJECT_ID = ?\n" +
					"    AND hdr.TENANT_ID = ?\n" +
					"    AND inh.SEQUENCE_STATUS IN ('DS020', 'DS077', 'DS070')\n" +
					"    AND iat.EMPLOYEE_ID = ?\n" +
					"    " + byIndentId + ";";

//			String retQry = "SELECT DISTINCT \n" +
//					"    hdr.IG_HDR_ID, \n" +
//					"    mst.EMPLOYEE_FIRSTNAME,\n" +
//					"    inh.INDENT_ID, \n" +
//					"    inh.INDENT_CODE, \n" +
//					"    hdr.GROUP_NAME, \n" +
//					"    inh.SBC_CODE, \n" +
//					"    sb.SBC_DESC, \n" +
//					"    inh.EXPECTED_DELIVERY_DATE, \n" +
//					"    inh.PKA_ID, \n" +
//					"    pkam.PK_DESC, \n" +
//					"    inh.PKSA_ID, \n" +
//					"    pksam.PSK_DESC, \n" +
//					"    inh.TARGET_VALUE, \n" +
//					"    hdr.IS_INVENTORY\n" +
//					"FROM \n" +
//					"    indent_grp_hdr hdr\n" +
//					"INNER JOIN \n" +
//					"\temployee_mst mst ON hdr.CREATED_BY = mst.EMPLOYEE_ID\n" +
//					"INNER JOIN \n" +
//					"    indent_grp_dtl dtl ON dtl.IG_HDR_ID = hdr.IG_HDR_ID\n" +
//					"INNER JOIN \n" +
//					"    indent_dtl ind ON ind.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n" +
//					"INNER JOIN \n" +
//					"    indent_hdr inh ON inh.INDENT_ID = ind.INDENT_ID\n" +
//					"INNER JOIN \n" +
//					"    sales_budget_category sb ON sb.SBC_CODE = inh.SBC_CODE\n" +
//					"INNER JOIN \n" +
//					"    project_hdr ph ON ph.PM_HDR_ID = inh.PROJECT_ID\n" +
//					"INNER JOIN \n" +
//					"    project_key_area pka ON pka.PKA_ID = inh.PKA_ID\n" +
//					"INNER JOIN \n" +
//					"    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID\n" +
//					"INNER JOIN \n" +
//					"    project_key_sub_area pksa ON pksa.PKSA_ID = inh.PKSA_ID\n" +
//					"INNER JOIN \n" +
//					"    project_key_sub_area_mst pksam ON pksam.PSK_ID = pksa.PSK_ID\n" +
//					"INNER JOIN \n" +
//					"    indent_assign_team iat ON ind.INDENT_DTL_ID = iat.INDENT_DTL_ID\n" +
//					"WHERE \n" +
////					"    DATE(hdr.CREATED_DATETIME) BETWEEN ? AND ? AND\n" +
//					"     inh.PROJECT_ID = ?\n" +
//					"    AND hdr.TENANT_ID = ?\n" +
//					"    AND inh.SEQUENCE_STATUS IN ('DS020', 'DS077')\n" +
//					"    AND iat.EMPLOYEE_ID = ?;";

			RowMapper<IndentGroupDetailsEntity> dtlrm = new IndentGroupDetailsRowMapper();

//			returnList = this.jdbcTemplate.query(retQry, dtlrm,fromdate,todate,projectId,tenantId,empId);
			returnList = this.jdbcTemplate.query(retQry, dtlrm,projectId,tenantId,empId);

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return returnList;
	}

	@Override
	public int delIndentGrpDtl(IndentGrpDelRequest indentGrpDtlReq) {
		int res = 0;
		try {
			String delQ = "delete from indent_grp_dtl where IG_DTL_ID = ? and TENANT_ID=?";
			res = this.jdbcTemplate.update(delQ,indentGrpDtlReq.getIgDtlId(),indentGrpDtlReq.getTenantId());

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return res;
	}

	@Override
	public int delIndentGrp(IndentGrpDelRequest indentGrpDtlReq) {
		int res = 0;
		try {
			String delQ = "delete from indent_grp_dtl where IG_HDR_ID = ? and TENANT_ID=?";
			res = this.jdbcTemplate.update(delQ,indentGrpDtlReq.getIgDtlId(),indentGrpDtlReq.getTenantId());

			delQ = "delete from indent_grp_hdr where IG_HDR_ID=? and TENANT_ID=?";
			res = this.jdbcTemplate.update(delQ,indentGrpDtlReq.getIgDtlId(),indentGrpDtlReq.getTenantId());

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return res;
	}

	@Override
	public List<IndentGroupHdrAndDtlEntity> getIndentGroupHdrAndDtl(IndentGrpDelRequest indentGrpDtlReq) {
		List<IndentGroupHdrAndDtlEntity> returnList = new ArrayList<IndentGroupHdrAndDtlEntity>();
		try {
			String retQry = "SELECT \r\n" + 
					"    idtl.PRODUCT_CODE,\r\n" + 
					"    idtl.DESCRIPTION,\r\n" + 
					"    idtl.SPECIFICATION,\r\n" + 
					"    idtl.MAKE,\r\n" + 
					"    idtl.WEIGHT,\r\n" + 
					"    idtl.MATERIAL,\r\n" + 
					"    idtl.REMARKS,\r\n" + 
					"    dtl.QTY AS INDENT_GRP_QTY,\r\n" + 
					"    idtl.QTY AS INDENT_QTY,\r\n" + 
//					"    idtl.qty - dtl.QTY AS RESP_QTY,\r\n" + 
					"    dtl.IG_DTL_ID,idtl.INDENT_DTL_ID,\r\n" + 
					"    uom.UOM_SHORT_DESCRIPTION AS UOM\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_dtl dtl,\r\n" + 
					"    indent_dtl idtl,\r\n" + 
					"    uom_mst uom\r\n" + 
					"WHERE\r\n" + 
					"    dtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" + 
					"        AND idtl.UNIT = uom.UOM_CODE\r\n" + 
					"        AND dtl.IG_HDR_ID = ?\r\n" + 
					"        AND dtl.TENANT_ID = ?";
			RowMapper<IndentGroupHdrAndDtlEntity> dtlrm = new IndentGroupHdrAndDtlRowMapper();

			returnList = this.jdbcTemplate.query(retQry, dtlrm, indentGrpDtlReq.getIgDtlId(),
					indentGrpDtlReq.getTenantId());
			
			

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return returnList;
	}

	@Override
	public int checkTemplateName(IndentTemplateNameRequest indentTempName) {
		int res = 0;
		try {
			String getQ = "select count(*) as COUNT from indent_grp_hdr where GROUP_NAME = ? and TENANT_ID= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,indentTempName.getTempName(),indentTempName.getTenantId());
			res = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return res;
	}

	@Override
	public int insertTempGrup(IndentInsertGrpRequest indentTempName) {
		int insertRes = 0;
		try {

			String insertQ = "INSERT INTO indent_grp_hdr (GROUP_NAME,IS_INVENTORY ,CREATED_DATETIME, CREATED_BY, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, TENANT_ID) VALUES (?, ?,?, ?, ?, ?, ?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, indentTempName.getGroupName());
					ps.setString(2, indentTempName.getIsInventory());
					ps.setString(3, CommonMethod.getCurrentDateTime());
					ps.setString(4, indentTempName.getCreatedBy());
					ps.setString(5, CommonMethod.getCurrentDateTime());
					ps.setString(6, indentTempName.getCreatedBy());
					ps.setString(7, indentTempName.getTenantId());
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

			// TODO Auto-generated method stub

		} catch (Exception ex) {
			logger.error("insertTempGrup error---> " + ex);
		}
		return insertRes;

	}

	@Override
	public void insertTempGrpDtl(int hdrId, IndentInsertGrpDtlRequest grpDtl) {

		try {
			String insertQ = "INSERT INTO indent_grp_dtl (IG_HDR_ID, INDENT_DTL_ID, INVENTORY, QTY, TENANT_ID) VALUES (?, ?, ?, ?, ?)";
			this.jdbcTemplate.update(insertQ, hdrId, grpDtl.getIndentDtlId(), grpDtl.getInventory(), grpDtl.getQty(),
					grpDtl.getTenantId());
		} catch (Exception ex) {
			logger.error("insertTempGrpDtl error---> " + ex);
		}

	}

	@Override
	public BigDecimal getsumOfGrpQty(String indentDtl, String tenantId) {
		BigDecimal totalQty = BigDecimal.ZERO;
		try {
			String QtyStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN SUM(QTY) > 0 THEN SUM(QTY)\r\n"
					+ "        ELSE 0\r\n" + "    END Total_qty\r\n" + "FROM\r\n" + "    indent_grp_dtl\r\n"
					+ "WHERE\r\n" + "    INDENT_DTL_ID = ? AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(QtyStr,indentDtl,tenantId);
			totalQty = new BigDecimal(resultMap.get("Total_qty").toString());

		} catch (Exception ex) {
			logger.error("getsumOfGrpQty ERROR  " + ex);
		}
		return totalQty;
	}

	public List<ScpDtlsEntity> getScpDtlsByIgHdrId(String hdrId, String tenantId) {
		List<ScpDtlsEntity> list = new ArrayList<ScpDtlsEntity>();
		try {
			String qry = "SELECT \r\n" + "    IG_SCS_ID,\r\n" + "    IG_HDR_ID,INDENT_ID,\r\n" + "    TECHNICAL_COMPASSION,\r\n"
					+ "    TECHNICAL_RECOMMENDATION,\r\n" + "    VENDOR_EVALUATED,\r\n" + "    VENDOR_SHORTLISTED,\r\n"
					+ "    JUSTIFICATION,\r\n" + "    VENDOR_QUALIFIED,\r\n" + "    CUSTOMER_APPROVAL,\r\n"
					+ "    CREATED_DATETIME,\r\n" + "    emp.EMPLOYEE_FIRSTNAME as CREATED_BY,\r\n"
					+ "    SEQUENCE_NO,\r\n" + "    SEQUENCE_STATUS,\r\n" + "    IS_APPROVED,igs.TYPE\r\n" + "FROM\r\n"
					+ "    indent_grp_scs igs\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst emp ON igs.CREATED_BY = emp.EMPLOYEE_ID\r\n" + "WHERE\r\n"
					+ "    igs.IG_HDR_ID = ?\r\n" + "	AND igs.TENANT_ID = ?;";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpRowMapper(), hdrId, tenantId);

		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentGrpScpDtlEntity> getScpDtlList(String igScpId) {
		List<IndentGrpScpDtlEntity> list = new ArrayList<IndentGrpScpDtlEntity>();
		try {
			String qry = "SELECT       @a:=@a + 1 AS S_NO,      igs.*,      dtl.PRODUCT_CODE,\r\n" +
					"					     dtl.DESCRIPTION,      igd.QTY,      uom.UOM_SHORT_DESCRIPTION as UOM,dtl.SPECIFICATION AS SPECIFICATION, dtl.WEIGHT, dtl.MATERIAL\r\n" +
					"                         FROM\r\n" +
					"					     (SELECT @a:=0) AS a,      indent_grp_scs_dtl igs          INNER JOIN\r\n" +
					"					     indent_grp_dtl igd ON igs.IG_DTL_ID = igd.IG_DTL_ID          INNER JOIN\r\n" +
					"					     indent_dtl dtl ON igd.INDENT_DTL_ID = dtl.INDENT_DTL_ID          INNER JOIN\r\n" +
					"					     uom_mst uom ON dtl.UNIT = uom.UOM_CODE\r\n" +
					"                         WHERE      IG_SCS_ID = ?;";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpDtlRowMapper(), igScpId);

		} catch (Exception ex) {
			logger.error("getScpDtlList Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentGrpScpVenEntity> getScpVendorList(String igScpId) {
		List<IndentGrpScpVenEntity> list = new ArrayList<IndentGrpScpVenEntity>();
		try {
//			String qry = "SELECT * FROM indent_grp_scs_ven where IG_SCS_ID=?";
//			String qry = "SELECT \n" +
//					"  igscv.*,\n" +
//					"  \n" +
//					"  loc1.LOCATION_COUNTRY_CODE AS L1_VENDOR_COUNTRY,\n" +
//					"\n" +
//					"  loc2.LOCATION_COUNTRY_CODE AS L2_VENDOR_COUNTRY,\n" +
//					"\n" +
//					"  loc3.LOCATION_COUNTRY_CODE AS L3_VENDOR_COUNTRY\n" +
//					"\n" +
//					"FROM indent_grp_scs_ven igscv\n" +
//					"LEFT JOIN vendor_mst vm1 ON igscv.L1_VENDOR_CODE = vm1.VENDOR_CODE\n" +
//					"LEFT JOIN location_mst loc1 ON loc1.LOCATION_ID = vm1.LOCATION_ID\n" +
//					"\n" +
//					"LEFT JOIN vendor_mst vm2 ON igscv.L2_VENDOR_CODE = vm2.VENDOR_CODE\n" +
//					"LEFT JOIN location_mst loc2 ON loc2.LOCATION_ID = vm2.LOCATION_ID\n" +
//					"\n" +
//					"LEFT JOIN vendor_mst vm3 ON igscv.L3_VENDOR_CODE = vm3.VENDOR_CODE\n" +
//					"LEFT JOIN location_mst loc3 ON loc3.LOCATION_ID = vm3.LOCATION_ID\n" +
//					"\n" +
//					"WHERE igscv.IG_SCS_ID = ?";
			String qry = "SELECT \n" +
					"  igscv.*,\n" +
					"\n" +
					"  loc1.LOCATION_COUNTRY_CODE AS L1_VENDOR_COUNTRY,\n" +
					"  vm1.CURRENCY_TYPE AS L1_VENDOR_CURRENCY,\n" +
					"\n" +
					"  loc2.LOCATION_COUNTRY_CODE AS L2_VENDOR_COUNTRY,\n" +
					"  vm2.CURRENCY_TYPE AS L2_VENDOR_CURRENCY,\n" +
					"\n" +
					"  loc3.LOCATION_COUNTRY_CODE AS L3_VENDOR_COUNTRY,\n" +
					"  vm3.CURRENCY_TYPE AS L3_VENDOR_CURRENCY\n" +
					"\n" +
					"FROM indent_grp_scs_ven igscv\n" +
					"LEFT JOIN vendor_mst vm1 ON igscv.L1_VENDOR_CODE = vm1.VENDOR_CODE\n" +
					"LEFT JOIN location_mst loc1 ON loc1.LOCATION_ID = vm1.LOCATION_ID\n" +
					"\n" +
					"LEFT JOIN vendor_mst vm2 ON igscv.L2_VENDOR_CODE = vm2.VENDOR_CODE\n" +
					"LEFT JOIN location_mst loc2 ON loc2.LOCATION_ID = vm2.LOCATION_ID\n" +
					"\n" +
					"LEFT JOIN vendor_mst vm3 ON igscv.L3_VENDOR_CODE = vm3.VENDOR_CODE\n" +
					"LEFT JOIN location_mst loc3 ON loc3.LOCATION_ID = vm3.LOCATION_ID\n" +
					"\n" +
					"WHERE igscv.IG_SCS_ID = ?;\n";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpVenRowMapper(), igScpId);

		} catch (Exception ex) {
			logger.error("getScpVendorList Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentGrpScpVenDtlEntity> getScpVendorDtlList(String igScpId) {
		List<IndentGrpScpVenDtlEntity> list = new ArrayList<IndentGrpScpVenDtlEntity>();
		try {
			String qry = "SELECT * FROM indent_grp_scs_ven_dtl where IG_SCS_ID=?";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpVenDtlRowMapper(), igScpId);

		} catch (Exception ex) {
			logger.error("getScpVendorDtlList Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentGrpScpVenPtEntity> getScpvendorPtList(String igScpId) {
		List<IndentGrpScpVenPtEntity> list = new ArrayList<IndentGrpScpVenPtEntity>();
		try {
			String qry = "SELECT \r\n" + "    IG_SCS_VPT, IG_SCS_ID, LEVEL, TERM, PERCENTAGE,REMARKS\r\n" + "FROM\r\n"
					+ "    indent_grp_scs_ven_pt\r\n" + "WHERE\r\n" + "    IG_SCS_ID = ?";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpVenPtRowMapper(), igScpId);

		} catch (Exception ex) {
			logger.error("getScpvendorPtList Method Exception --->" + ex);

		}
		return list;

	}
	
	@Override
	public List<IndentGrpScpVenPtEntity> getScpPtListForLevel(String igScpId,String level) {
		List<IndentGrpScpVenPtEntity> list = new ArrayList<IndentGrpScpVenPtEntity>();
		try {
			String qry = "SELECT \r\n" + "    IG_SCS_VPT, IG_SCS_ID, LEVEL, TERM, PERCENTAGE,REMARKS\r\n" + "FROM\r\n"
					+ "    indent_grp_scs_ven_pt\r\n" + "WHERE\r\n" + "    IG_SCS_ID = ? and LEVEL=?";
			list = this.jdbcTemplate.query(qry, new IndentGrpScpVenPtRowMapper(), igScpId,level);

		} catch (Exception ex) {
			logger.error("getScpPtListForL1 Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public int insertInScp(String igHdrId, String TechnicalCompassion, String technicalRecommendation,
			String vendorEvaluated, String vendorShortListed, String justification, String vendorQualified,
			String customerApproval, String createdDate, String createdBy, String seqNo, String seqStatus,
			String tenantId,String indentId,String type) {
		int scpId = 0;
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO indent_grp_scs ( IG_HDR_ID,INDENT_ID, TECHNICAL_COMPASSION, TECHNICAL_RECOMMENDATION, VENDOR_EVALUATED, VENDOR_SHORTLISTED, JUSTIFICATION, VENDOR_QUALIFIED, CUSTOMER_APPROVAL, CREATED_DATETIME, CREATED_BY, SEQUENCE_NO, SEQUENCE_STATUS, IS_APPROVED, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, TENANT_ID,TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, igHdrId);
					ps.setString(2, indentId);  // Add indentId here
					ps.setString(3, TechnicalCompassion);
					ps.setString(4, technicalRecommendation);
					ps.setString(5, vendorEvaluated);
					ps.setString(6, vendorShortListed);
					ps.setString(7, justification);
					ps.setString(8, vendorQualified);
					ps.setString(9, customerApproval);
					ps.setString(10, CommonMethod.getCurrentDateTime());
					ps.setString(11, createdBy);
					ps.setString(12, seqNo);
					ps.setString(13, seqStatus);
					ps.setString(14, "0");
					ps.setString(15, CommonMethod.getCurrentDateTime());
					ps.setString(16, createdBy);
					ps.setString(17, tenantId); 
					ps.setString(18, type);
					return ps;
				}
			}, holder);

			scpId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertInScp method Error" + ex);
		}
		return scpId;

	}

	@Override
	public int updateInScp(String igScpId, String TechnicalCompassion, String technicalRecommendation,
			String vendorEvaluated, String vendorShortListed, String justification, String vendorQualified,
			String customerApproval, String createdBy,String type) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_grp_scs SET TECHNICAL_COMPASSION=?, TECHNICAL_RECOMMENDATION=?, VENDOR_EVALUATED=?, VENDOR_SHORTLISTED=?, JUSTIFICATION=?, VENDOR_QUALIFIED=?, CUSTOMER_APPROVAL=?, LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=?,TYPE =? WHERE IG_SCS_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, TechnicalCompassion, technicalRecommendation, vendorEvaluated,
					vendorShortListed, justification, vendorQualified, customerApproval,
					CommonMethod.getCurrentDateTime(), createdBy,type, igScpId);

		} catch (Exception ex) {
			logger.error("updateInScp method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int insertInScpDtl(String igScpDItld, String igScpId, String igDtlId, String l1CurrencyType, String l1ExchangeRate,  String l1UnitPrice,
			String l1ExtendedPrice, String l1UnitPriceFx, String l1ExtendedPriceFx, String l2CurrencyType, String l2ExchangeRate, String l2UnitPrice, String l2ExtendedPrice, String l2UnitPriceFx, String l2ExtendedPriceFx, String l3CurrencyType, String l3ExchangeRate, String l3UnitPrice,
			String l3ExtendedPrice, String l3UnitPriceFx, String l3ExtendedPriceFx, String finalL1UnitPrice,String finall1ExtnPrice, String finalL1UnitPriceFx, String finalL1ExtnPriceFx, String finalL2UnitPrice,String finall2ExtnPrice, String finalL2UnitPriceFx, String finalL2ExtnPriceFx, String finalL3UnitPrice,String finall3ExtnPrice, String finalL3UnitPriceFx, String finalL3ExtnPriceFx, String tenantId,String indentDtlId) {
		int insertStatus = 0;
		try {
			if (igScpDItld.equalsIgnoreCase("")) {
//				String qry = "INSERT INTO indent_grp_scs_dtl (IG_SCS_ID, IG_DTL_ID,INDENT_DTL_ID, L1_CURRENCY_TYPE, L1_EXCHANGE_RATE, L1_UNIT_PRICE_FX, L1_UNIT_PRICE, L1_EXTENDED_PRICE, L2_CURRENCY_TYPE, L2_EXCHANGE_RATE, L2_UNIT_PRICE_FX,  L2_UNIT_PRICE, L2_EXTENDED_PRICE, L3_CURRENCY_TYPE, L3_EXCHANGE_RATE, L3_UNIT_PRICE_FX, L3_UNIT_PRICE, L3_EXTENDED_PRICE, FINAL_L1_FX_UNIT_PRICE, FINAL_L1_UNIT_PRICE, FINAL_L1_EXTENDED_PRICE, FINAL_L2_FX_UNIT_PRICE, FINAL_L2_UNIT_PRICE, FINAL_L2_EXTENDED_PRICE, FINAL_L3_FX_UNIT_PRICE, FINAL_L3_UNIT_PRICE, FINAL_L3_EXTENDED_PRICE, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
				String qry = "INSERT INTO indent_grp_scs_dtl (IG_SCS_ID, IG_DTL_ID,INDENT_DTL_ID, L1_CURRENCY_TYPE, L1_EXCHANGE_RATE, L1_UNIT_PRICE_FX, L1_EXTENDED_PRICE, L1_UNIT_PRICE, L1_FX_EXTENDED_PRICE,\n" +
						" L2_CURRENCY_TYPE, L2_EXCHANGE_RATE, L2_UNIT_PRICE_FX, L2_FX_EXTENDED_PRICE,  L2_UNIT_PRICE, L2_EXTENDED_PRICE,\n" +
						" L3_CURRENCY_TYPE, L3_EXCHANGE_RATE, L3_UNIT_PRICE_FX, L3_FX_EXTENDED_PRICE, L3_UNIT_PRICE, L3_EXTENDED_PRICE,\n" +
						" FINAL_L1_FX_UNIT_PRICE, FINAL_L1_FX_EXTENDED_PRICE,  FINAL_L1_UNIT_PRICE, FINAL_L1_EXTENDED_PRICE, \n" +
						" FINAL_L2_FX_UNIT_PRICE, FINAL_L2_FX_EXTENDED_PRICE, FINAL_L2_UNIT_PRICE, FINAL_L2_EXTENDED_PRICE,\n" +
						" FINAL_L3_FX_UNIT_PRICE, FINAL_L3_FX_EXTENDED_PRICE, FINAL_L3_UNIT_PRICE, FINAL_L3_EXTENDED_PRICE, TENANT_ID)\n" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				insertStatus = this.jdbcTemplate.update(qry,
						igScpId,
						igDtlId,
						indentDtlId,
						CommonMethod.safeStr(l1CurrencyType),
						CommonMethod.safeStrOrDefault(l1ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l1UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l1ExtendedPrice, "0"),
						CommonMethod.safeStrOrDefault(l1UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l1ExtendedPriceFx, "0"),
						CommonMethod.safeStr(l2CurrencyType),
						CommonMethod.safeStrOrDefault(l2ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l2UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l2ExtendedPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l2UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l2ExtendedPrice, "0"),
						CommonMethod.safeStr(l3CurrencyType),
						CommonMethod.safeStrOrDefault(l3ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l3UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l3ExtendedPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l3UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l3ExtendedPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL1UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL1ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL1UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall1ExtnPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL2UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL2ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL2UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall2ExtnPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL3UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL3ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL3UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall3ExtnPrice, "0"),
						CommonMethod.safeStr(tenantId)
				);
			} else {
				String qry = "UPDATE indent_grp_scs_dtl SET " +
						"L1_CURRENCY_TYPE=?, L1_EXCHANGE_RATE=?, L1_UNIT_PRICE_FX=?, L1_FX_EXTENDED_PRICE=?, L1_UNIT_PRICE=?, L1_EXTENDED_PRICE=?, " +
						"L2_CURRENCY_TYPE=?, L2_EXCHANGE_RATE=?, L2_UNIT_PRICE_FX=?, L2_FX_EXTENDED_PRICE=?, L2_UNIT_PRICE=?, L2_EXTENDED_PRICE=?, " +
						"L3_CURRENCY_TYPE=?, L3_EXCHANGE_RATE=?, L3_UNIT_PRICE_FX=?, L3_FX_EXTENDED_PRICE=?, L3_UNIT_PRICE=?, L3_EXTENDED_PRICE=?, " +
						"FINAL_L1_FX_UNIT_PRICE=?, FINAL_L1_FX_EXTENDED_PRICE=?, FINAL_L1_UNIT_PRICE=?, FINAL_L1_EXTENDED_PRICE=?, " +
						"FINAL_L2_FX_UNIT_PRICE=?, FINAL_L2_FX_EXTENDED_PRICE=?, FINAL_L2_UNIT_PRICE=?, FINAL_L2_EXTENDED_PRICE=?, " +
						"FINAL_L3_FX_UNIT_PRICE=?, FINAL_L3_FX_EXTENDED_PRICE=?, FINAL_L3_UNIT_PRICE=?, FINAL_L3_EXTENDED_PRICE=?, " +
						"TENANT_ID=? WHERE IG_SCSD_ID=?;";

				insertStatus = this.jdbcTemplate.update(qry,
						CommonMethod.safeStr(l1CurrencyType),
						CommonMethod.safeStrOrDefault(l1ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l1UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l1ExtendedPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l1UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l1ExtendedPrice, "0"),
						CommonMethod.safeStr(l2CurrencyType),
						CommonMethod.safeStrOrDefault(l2ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l2UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l2ExtendedPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l2UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l2ExtendedPrice, "0"),
						CommonMethod.safeStr(l3CurrencyType),
						CommonMethod.safeStrOrDefault(l3ExchangeRate, "0"),
						CommonMethod.safeStrOrDefault(l3UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l3ExtendedPriceFx, "0"),
						CommonMethod.safeStrOrDefault(l3UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(l3ExtendedPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL1UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL1ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL1UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall1ExtnPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL2UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL2ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL2UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall2ExtnPrice, "0"),
						CommonMethod.safeStrOrDefault(finalL3UnitPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL3ExtnPriceFx, "0"),
						CommonMethod.safeStrOrDefault(finalL3UnitPrice, "0"),
						CommonMethod.safeStrOrDefault(finall3ExtnPrice, "0"),
						CommonMethod.safeStr(tenantId),
						CommonMethod.safeStr(igScpDItld)
				);

			}

			} catch (Exception ex) {
			logger.error("insertInScpDtl method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int insertInVen(String igScpVid, String igScpId, String l1VendorCode, String l2VendorCode,
			String l3VendorCode, String level, String createdBy,String l1Gst,String l2Gst,String l3Gst) {
		int insertStatus = 0;
		try {
			if (igScpVid.equalsIgnoreCase("")) {
				String qry = "INSERT INTO indent_grp_scs_ven (IG_SCS_ID, L1_VENDOR_CODE, L2_VENDOR_CODE, L3_VENDOR_CODE, LAST_UPDATED_DATETIME, LAST_UPDATED_BY,L1_GST,L2_GST,L3_GST) VALUES ( ?, ?, ?, ?, ?, ?,?,?,?);";
				insertStatus = this.jdbcTemplate.update(qry, igScpId, l1VendorCode, l2VendorCode, l3VendorCode, 
						CommonMethod.getCurrentDateTime(), createdBy,l1Gst,l2Gst,l3Gst);
			} else {
				String qry = "UPDATE indent_grp_scs_ven SET L1_VENDOR_CODE=?, L2_VENDOR_CODE=?, L3_VENDOR_CODE=?, LEVEL=?,L1_GST = ?,L2_GST = ?,L3_GST =? WHERE IG_SCS_VID=?";
				insertStatus = this.jdbcTemplate.update(qry, l1VendorCode, l2VendorCode, l3VendorCode, level,l1Gst,l2Gst,l3Gst, igScpVid);
			}

		} catch (Exception ex) {
			logger.error("insertInVen method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int insertInVenDtl(IndentGrpScpVenDtlEntity indentGrpScpVenDtl, String createdBy, String scpId) {
		int insertStatus = 0;
		try {
			if (indentGrpScpVenDtl.getIgScpVendtlId().equalsIgnoreCase("")) {
				String qry = "INSERT INTO indent_grp_scs_ven_dtl (" +
						"IG_SCS_ID, IG_DTL_ID, INDENT_DTL_ID, " +

						"L1_WARRENTY, L1_DELIVERY_DATE, L1_LD, L1_INTIAL_QUOTED_DATE, L1_INTIAL_QUOTED_REF, " +
						"L1_FINAL_QUOTED_DATE, L1_FINAL_QUOTED_REF, L1_FX_UNIT_INI_BASIC_TOTAL, L1_UNIT_INI_BASIC_TOTAL, " +
						"L1_FX_EXTN_INI_BASIC_TOTAL, L1_EXTN_INI_BASIC_TOTAL, L1_TRANSPORT_CHARGES, L1_TRANSPORT_CHARGES_FX, L1_P_F, L1_P_F_FX, L1_SUB_TOTAL, " +
						"L1_SUB_TOTAL_FX, L1_GST_VALUE, L1_GST_VALUE_FX, L1_TOTAL_COST, L1_TOTAL_COST_FX, L1_FX_UNIT_FINAL_BASIC_TOTAL, L1_UNIT_FINAL_BASIC_TOTAL, " +
						"L1_FX_EXTN_FINAL_BASIC_TOTAL, L1_EXTN_FINAL_BASIC_TOTAL, L1_FINAL_TRANSPORT_CHARGES, L1_FX_FINAL_TRANSPORT_CHARGES, L1_FINAL_P_F, L1_FX_FINAL_P_F, " +
						"L1_FINAL_SUB_TOTAL, L1_FINAL_SUB_TOTAL_FX, L1_FINAL_GST_VALUE, L1_FX_FINAL_GST_VALUE, L1_FINAL_TOTAL_COST, L1_FX_FINAL_TOTAL_COST, " +

						"L2_WARRENTY, L2_DELIVERY_DATE, L2_LD, L2_INTIAL_QUOTED_DATE, L2_INTIAL_QUOTED_REF, L2_FINAL_QUOTED_DATE, " +
						"L2_FINAL_QUOTED_REF, L2_FX_UNIT_INI_BASIC_TOTAL, L2_UNIT_INI_BASIC_TOTAL, L2_FX_EXTN_INI_BASIC_TOTAL, " +
						"L2_EXTN_INI_BASIC_TOTAL, L2_TRANSPORT_CHARGES, L2_TRANSPORT_CHARGES_FX, L2_P_F, L2_P_F_FX, L2_SUB_TOTAL, " +
						"L2_SUB_TOTAL_FX, L2_GST_VALUE, L2_GST_VALUE_FX, L2_TOTAL_COST, L2_TOTAL_COST_FX, L2_FX_UNIT_FINAL_BASIC_TOTAL, L2_UNIT_FINAL_BASIC_TOTAL, " +
						"L2_FX_EXTN_FINAL_BASIC_TOTAL, L2_EXTN_FINAL_BASIC_TOTAL, L2_FINAL_TRANSPORT_CHARGES, L2_FX_FINAL_TRANSPORT_CHARGES, L2_FINAL_P_F, L2_FX_FINAL_P_F, L2_FINAL_SUB_TOTAL, " +
						"L2_FINAL_SUB_TOTAL_FX, L2_FINAL_GST_VALUE, L2_FX_FINAL_GST_VALUE, L2_FINAL_TOTAL_COST, L2_FX_FINAL_TOTAL_COST, " +

						"L3_WARRENTY, L3_DELIVERY_DATE, L3_LD, L3_INTIAL_QUOTED_DATE, L3_INTIAL_QUOTED_REF, " +
						"L3_FINAL_QUOTED_DATE, L3_FINAL_QUOTED_REF, L3_FX_UNIT_INI_BASIC_TOTAL, L3_UNIT_INI_BASIC_TOTAL, " +
						"L3_FX_EXTN_INI_BASIC_TOTAL, L3_EXTN_INI_BASIC_TOTAL, L3_TRANSPORT_CHARGES, L3_TRANSPORT_CHARGES_FX, L3_P_F, L3_P_F_FX, L3_SUB_TOTAL, " +
						"L3_SUB_TOTAL_FX, L3_GST_VALUE, L3_GST_VALUE_FX, L3_TOTAL_COST, L3_TOTAL_COST_FX, L3_FX_UNIT_FINAL_BASIC_TOTAL, L3_UNIT_FINAL_BASIC_TOTAL, " +
						"L3_FX_EXTN_FINAL_BASIC_TOTAL, L3_EXTN_FINAL_BASIC_TOTAL, L3_FINAL_TRANSPORT_CHARGES, L3_FX_FINAL_TRANSPORT_CHARGES, L3_FINAL_P_F, L3_FX_FINAL_P_F, " +
						"L3_FINAL_SUB_TOTAL, L3_FINAL_SUB_TOTAL_FX, L3_FINAL_GST_VALUE, L3_FX_FINAL_GST_VALUE, L3_FINAL_TOTAL_COST, L3_FX_FINAL_TOTAL_COST, " +

						"LAST_UPDATED_DATETIME, LAST_UPDATED_BY" +
						") VALUES (" +
						"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
						")";

				insertStatus = this.jdbcTemplate.update(qry,
						scpId,
						indentGrpScpVenDtl.getIgDtlId(),
						indentGrpScpVenDtl.getIndentDtlId(),

						// L1
						getValue(indentGrpScpVenDtl.getL1Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL1DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL1Ld()),
						getDateValue(indentGrpScpVenDtl.getL1InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL1InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL1FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL1FinalQuotedRef()),
						getValue(indentGrpScpVenDtl.getL1UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL1TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL1PF()),
						getNumber(indentGrpScpVenDtl.getL1PFFx()),
						getNumber(indentGrpScpVenDtl.getL1SubTotal()),
						getNumber(indentGrpScpVenDtl.getL1SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1GstValue()),
						getNumber(indentGrpScpVenDtl.getL1GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL1TotalCost()),
						getNumber(indentGrpScpVenDtl.getL1TotalCostFx()),
						getValue(indentGrpScpVenDtl.getL1UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL1FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalPF()),
						getNumber(indentGrpScpVenDtl.getL1FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL1FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL1FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL1FinalTotalCostFx()),

						// L2
						getValue(indentGrpScpVenDtl.getL2Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL2DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL2Ld()),
						getDateValue(indentGrpScpVenDtl.getL2InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL2InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL2FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL2FinalQuotedRef()),
						getValue(indentGrpScpVenDtl.getL2UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL2TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL2PF()),
						getNumber(indentGrpScpVenDtl.getL2PFFx()),
						getNumber(indentGrpScpVenDtl.getL2SubTotal()),
						getNumber(indentGrpScpVenDtl.getL2SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2GstValue()),
						getNumber(indentGrpScpVenDtl.getL2GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL2TotalCost()),
						getNumber(indentGrpScpVenDtl.getL2TotalCostFx()),
						getValue(indentGrpScpVenDtl.getL2UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL2FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalPF()),
						getNumber(indentGrpScpVenDtl.getL2FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL2FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL2FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL2FinalTotalCostFx()),

						// L3
						getValue(indentGrpScpVenDtl.getL3Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL3DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL3Ld()),
						getDateValue(indentGrpScpVenDtl.getL3InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL3InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL3FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL3FinalQuotedRef()),
						getNumber(indentGrpScpVenDtl.getL3UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL3TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL3PF()),
						getNumber(indentGrpScpVenDtl.getL3PFFx()),
						getNumber(indentGrpScpVenDtl.getL3SubTotal()),
						getNumber(indentGrpScpVenDtl.getL3SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3GstValue()),
						getNumber(indentGrpScpVenDtl.getL3GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL3TotalCost()),
						getNumber(indentGrpScpVenDtl.getL3TotalCostFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL3FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalPF()),
						getNumber(indentGrpScpVenDtl.getL3FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL3FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL3FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL3FinalTotalCostFx()),

						// Meta
						CommonMethod.getCurrentDateTime(),
						createdBy
				);

			} else {
				String qry = "UPDATE indent_grp_scs_ven_dtl SET " +
						"L1_WARRENTY=?, L1_DELIVERY_DATE=?, L1_LD=?, L1_INTIAL_QUOTED_DATE=?, L1_INTIAL_QUOTED_REF=?, " +
						"L1_FINAL_QUOTED_DATE=?, L1_FINAL_QUOTED_REF=?, L1_FX_UNIT_INI_BASIC_TOTAL=?, L1_UNIT_INI_BASIC_TOTAL=?, " +
						"L1_FX_EXTN_INI_BASIC_TOTAL=?, L1_EXTN_INI_BASIC_TOTAL=?, L1_TRANSPORT_CHARGES=?, L1_TRANSPORT_CHARGES_FX=?, L1_P_F=?, L1_P_F_FX=?, L1_SUB_TOTAL=?, " +
						"L1_SUB_TOTAL_FX=?, L1_GST_VALUE=?, L1_GST_VALUE_FX=?, L1_TOTAL_COST=?, L1_TOTAL_COST_FX=?, L1_FX_UNIT_FINAL_BASIC_TOTAL=?, L1_UNIT_FINAL_BASIC_TOTAL=?, " +
						"L1_FX_EXTN_FINAL_BASIC_TOTAL=?, L1_EXTN_FINAL_BASIC_TOTAL=?, L1_FINAL_TRANSPORT_CHARGES=?, L1_FX_FINAL_TRANSPORT_CHARGES=?, L1_FINAL_P_F=?, L1_FX_FINAL_P_F=?, " +
						"L1_FINAL_SUB_TOTAL=?, L1_FINAL_SUB_TOTAL_FX=?, L1_FINAL_GST_VALUE=?, L1_FX_FINAL_GST_VALUE=?, L1_FINAL_TOTAL_COST=?, L1_FX_FINAL_TOTAL_COST=?, " +
						"L2_WARRENTY=?, L2_DELIVERY_DATE=?, L2_LD=?, L2_INTIAL_QUOTED_DATE=?, L2_INTIAL_QUOTED_REF=?, " +
						"L2_FINAL_QUOTED_DATE=?, L2_FINAL_QUOTED_REF=?, L2_FX_UNIT_INI_BASIC_TOTAL=?, L2_UNIT_INI_BASIC_TOTAL=?, " +
						"L2_FX_EXTN_INI_BASIC_TOTAL=?, L2_EXTN_INI_BASIC_TOTAL=?, L2_TRANSPORT_CHARGES=?, L2_TRANSPORT_CHARGES_FX=?, L2_P_F=?, L2_P_F_FX=?, L2_SUB_TOTAL=?, " +
						"L2_SUB_TOTAL_FX=?, L2_GST_VALUE=?, L2_GST_VALUE_FX=?, L2_TOTAL_COST=?, L2_TOTAL_COST_FX=?, L2_FX_UNIT_FINAL_BASIC_TOTAL=?, L2_UNIT_FINAL_BASIC_TOTAL=?, " +
						"L2_FX_EXTN_FINAL_BASIC_TOTAL=?, L2_EXTN_FINAL_BASIC_TOTAL=?, L2_FINAL_TRANSPORT_CHARGES=?, L2_FX_FINAL_TRANSPORT_CHARGES=?, L2_FINAL_P_F=?, L2_FX_FINAL_P_F=?, " +
						"L2_FINAL_SUB_TOTAL=?, L2_FINAL_SUB_TOTAL_FX=?, L2_FINAL_GST_VALUE=?, L2_FX_FINAL_GST_VALUE=?, L2_FINAL_TOTAL_COST=?, L2_FX_FINAL_TOTAL_COST=?, " +
						"L3_WARRENTY=?, L3_DELIVERY_DATE=?, L3_LD=?, L3_INTIAL_QUOTED_DATE=?, L3_INTIAL_QUOTED_REF=?, " +
						"L3_FINAL_QUOTED_DATE=?, L3_FINAL_QUOTED_REF=?, L3_FX_UNIT_INI_BASIC_TOTAL=?, L3_UNIT_INI_BASIC_TOTAL=?, " +
						"L3_FX_EXTN_INI_BASIC_TOTAL=?, L3_EXTN_INI_BASIC_TOTAL=?, L3_TRANSPORT_CHARGES=?, L3_TRANSPORT_CHARGES_FX=?, L3_P_F=?, L3_P_F_FX=?, L3_SUB_TOTAL=?, " +
						"L3_SUB_TOTAL_FX=?, L3_GST_VALUE=?, L3_GST_VALUE_FX=?, L3_TOTAL_COST=?, L3_TOTAL_COST_FX=?, L3_FX_UNIT_FINAL_BASIC_TOTAL=?, L3_UNIT_FINAL_BASIC_TOTAL=?, " +
						"L3_FX_EXTN_FINAL_BASIC_TOTAL=?, L3_EXTN_FINAL_BASIC_TOTAL=?, L3_FINAL_TRANSPORT_CHARGES=?, L3_FX_FINAL_TRANSPORT_CHARGES=?, L3_FINAL_P_F=?, L3_FX_FINAL_P_F=?, " +
						"L3_FINAL_SUB_TOTAL=?, L3_FINAL_SUB_TOTAL_FX=?, L3_FINAL_GST_VALUE=?, L3_FX_FINAL_GST_VALUE=?, L3_FINAL_TOTAL_COST=?, L3_FX_FINAL_TOTAL_COST=?, " +
						"LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=? " +
						"WHERE IG_SCS_VDID=?";

				insertStatus = this.jdbcTemplate.update(qry,

						// ---------- L1 ----------
						getValue(indentGrpScpVenDtl.getL1Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL1DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL1Ld()),
						getDateValue(indentGrpScpVenDtl.getL1InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL1InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL1FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL1FinalQuotedRef()),
						getValue(indentGrpScpVenDtl.getL1UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL1TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL1PF()),
						getNumber(indentGrpScpVenDtl.getL1PFFx()),
						getNumber(indentGrpScpVenDtl.getL1SubTotal()),
						getNumber(indentGrpScpVenDtl.getL1SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1GstValue()),
						getNumber(indentGrpScpVenDtl.getL1GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL1TotalCost()),
						getNumber(indentGrpScpVenDtl.getL1TotalCostFx()),
						getValue(indentGrpScpVenDtl.getL1UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL1FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL1FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalPF()),
						getNumber(indentGrpScpVenDtl.getL1FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL1FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL1FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL1FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL1FinalTotalCostFx()),

						// ---------- L2 ----------
						getValue(indentGrpScpVenDtl.getL2Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL2DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL2Ld()),
						getDateValue(indentGrpScpVenDtl.getL2InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL2InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL2FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL2FinalQuotedRef()),
						getValue(indentGrpScpVenDtl.getL2UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL2TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL2PF()),
						getNumber(indentGrpScpVenDtl.getL2PFFx()),
						getNumber(indentGrpScpVenDtl.getL2SubTotal()),
						getNumber(indentGrpScpVenDtl.getL2SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2GstValue()),
						getNumber(indentGrpScpVenDtl.getL2GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL2TotalCost()),
						getNumber(indentGrpScpVenDtl.getL2TotalCostFx()),
						getValue(indentGrpScpVenDtl.getL2UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL2FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL2FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalPF()),
						getNumber(indentGrpScpVenDtl.getL2FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL2FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL2FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL2FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL2FinalTotalCostFx()),

						// ---------- L3 ----------
						getValue(indentGrpScpVenDtl.getL3Warrenty()),
						getDateValue(indentGrpScpVenDtl.getL3DeliveryDate()),
						getValue(indentGrpScpVenDtl.getL3Ld()),
						getDateValue(indentGrpScpVenDtl.getL3InitialQuotedDate()),
						getValue(indentGrpScpVenDtl.getL3InitialQuotedRef()),
						getDateValue(indentGrpScpVenDtl.getL3FinalQuotedDate()),
						getValue(indentGrpScpVenDtl.getL3FinalQuotedRef()),
						getNumber(indentGrpScpVenDtl.getL3UnitIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3ExtnIniBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3ExtnIniBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3TransportCharges()),
						getNumber(indentGrpScpVenDtl.getL3TransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL3PF()),
						getNumber(indentGrpScpVenDtl.getL3PFFx()),
						getNumber(indentGrpScpVenDtl.getL3SubTotal()),
						getNumber(indentGrpScpVenDtl.getL3SubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3GstValue()),
						getNumber(indentGrpScpVenDtl.getL3GstValueFx()),
						getNumber(indentGrpScpVenDtl.getL3TotalCost()),
						getNumber(indentGrpScpVenDtl.getL3TotalCostFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3UnitFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3ExtnFinalBasicTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3ExtnFinalBasicTotal()),
						getNumber(indentGrpScpVenDtl.getL3FinalTransportCharges()),
						getNumber(indentGrpScpVenDtl.getL3FinalTransportChargesFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalPF()),
						getNumber(indentGrpScpVenDtl.getL3FinalPFFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalSubTotal()),
						getNumber(indentGrpScpVenDtl.getL3FinalSubTotalFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalGSTValue()),
						getNumber(indentGrpScpVenDtl.getL3FinalGSTValueFx()),
						getNumber(indentGrpScpVenDtl.getL3FinalTotalCost()),
						getNumber(indentGrpScpVenDtl.getL3FinalTotalCostFx()),

						// ---------- Common ----------
						CommonMethod.getCurrentDateTime(),   // LAST_UPDATED_DATETIME
						indentGrpScpVenDtl.getLastUpdatedBy(),  // LAST_UPDATED_BY
						indentGrpScpVenDtl.getIgScpVendtlId()       // WHERE IG_SCS_VDID = ?
				);

			}

		} catch (Exception ex) {
			logger.error("insertInVenDtl method Error" + ex);
		}
		return insertStatus;

	}
	private String getValue(String val) {
		return val == null ? "" : val;
	}

	private String getDateValue(String date) {
		return (date == null || date.equalsIgnoreCase("") || date.equalsIgnoreCase("Invalid date")) ? null : date;
	}

	private String getNumber(String val) {
		return (val == null || val.equalsIgnoreCase("")) ? "0" : val;
	}


	@Override
	public int insertInVenPt(IndentGrpScpVenPtEntity indentGrpScpVenPt, String tenantId, String scpId,
			String createdBy,String remarks) {
		int insertStatus = 0;
		try {
				String qry = "INSERT INTO indent_grp_scs_ven_pt (IG_SCS_ID, LEVEL, TERM, PERCENTAGE, REMARKS,LAST_UPDATED_DATETIME, LAST_UPDATED_BY) VALUES (?,?,?, ?, ?, ?, ?);";
				insertStatus = this.jdbcTemplate.update(qry, scpId, indentGrpScpVenPt.getLevel(),
						indentGrpScpVenPt.getTerm(), indentGrpScpVenPt.getPercentage()!=""? indentGrpScpVenPt.getPercentage():"0",remarks,
						CommonMethod.getCurrentDateTime(), createdBy);

		} catch (Exception ex) {
			logger.error("insertInVenPt method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int insertInScpStatus(String igScpId, String seqNo, String SeqStatus, String remarks, String createdBy,
			String tenantId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO indent_grp_scs_status (IG_SCS_ID, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
			insertStatus = this.jdbcTemplate.update(qry, igScpId, seqNo, SeqStatus, remarks, createdBy,
					CommonMethod.getCurrentDateTime(), tenantId);

		} catch (Exception ex) {
			logger.error("insertInScpStatus method Error" + ex);
		}
		return insertStatus;

	}
	public int updateindentGrpHdr(String scpId) {
		int updateStatus = 0;
		try {
			String getidgIdStr = "select case when count(*)>0 then IG_HDR_ID else 0 end AS IG_HDR_ID from indent_grp_scs where IG_SCS_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getidgIdStr,scpId);
			int getidgId = Integer.parseInt(resultMap.get("IG_HDR_ID").toString());
			String qry = "UPDATE `indent_grp_hdr` SET `LAST_UPDATED_DATETIME`= ? WHERE `IG_HDR_ID`= ? ";
			updateStatus = this.jdbcTemplate.update(qry,CommonMethod.getCurrentDateTime(), getidgId);

		} catch (Exception ex) {
			logger.error("updateindentGrpHdr method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int updateScpSeqAndStatus(String scpId, String currentseq, String docStatus,String empId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_grp_scs SET SEQUENCE_NO=?, SEQUENCE_STATUS=?, LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=? WHERE IG_SCS_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, currentseq, docStatus,CommonMethod.getCurrentDateTime(),empId, scpId);

		} catch (Exception ex) {
			logger.error("updateScpSeqAndStatus method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int updateScpApproved(String scpId,String isApproved) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_grp_scs SET IS_APPROVED=? WHERE IG_SCS_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, isApproved,scpId);

		} catch (Exception ex) {
			logger.error("updateScpApproved method Error" + ex);
		}
		return updateStatus;

	}
	
	@Override
	public String getIndentIdByIndentDtlId(String indentDtlId) {
		String id = "";
		try {
			String getId = "SELECT case when count(*)>0 then INDENT_ID else 0 end as ID  from indent_dtl where INDENT_DTL_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getId,indentDtlId);
			id = resultMap.get("ID").toString();

		} catch (Exception ex) {
			logger.error("getIndentIdByIndentDtlId Method Exception --->" + ex);

		}
		return id;
	}
	
	@Override
	public String getVendorQualified(String scsId) {
		String value = "";
		try {
			String qry = "select VENDOR_QUALIFIED from  indent_grp_scs where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scsId);
			value = resultMap.get("VENDOR_QUALIFIED").toString();

		} catch (Exception ex) {
			logger.error("getVendorQualified Method Exception --->" + ex);

		}
		return value;
	}
	
	public String getindentTypeCode(String scsId) {
		String value = "";
		try {
			String qry = "select INDENT_TYPE_CODE AS INDENT_TYPE_CODE from  indent_hdr  where INDENT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scsId);
			value = resultMap.get("INDENT_TYPE_CODE").toString();

		} catch (Exception ex) {
			logger.error("getindentTypeCode Method Exception --->" + ex);

		}
		return value;
	}
	

	@Override
	public String getIndentDtlIdByIgDtlId(String igDtlId) {
		String id = "";
		try {
			String getId = "SELECT case when count(*)>0 then INDENT_DTL_ID else 0 end as ID FROM indent_grp_dtl WHERE IG_DTL_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getId,igDtlId);
			id = resultMap.get("ID").toString();

		} catch (Exception ex) {
			logger.error("getIndentDtlIdByIgDtlId Method Exception --->" + ex);

		}
		return id;
	}

	@Override
	public int getIndentgrpDtlCount(String igDtlId) {
		int  count=0;
		try {
			String getCount = "select count(*) as COUNT from indent_grp_dtl grpDtl inner join indent_grp_scs grpscs on grpDtl.IG_HDR_ID = grpscs.IG_HDR_ID where grpDtl.IG_DTL_ID =?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igDtlId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getIndentgrpDtlCount Method Exception --->" + ex);

		}
		return count;
	}
	
	@Override
	public int getIndentgrpScsCountByIgHdrId(String igHdrId) {
		int  count=0;
		try {
			String getCount = "select count(*) as COUNT from indent_grp_scs where IG_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igHdrId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getIndentgrpDtlCountByIgHdrId Method Exception --->" + ex);

		}
		return count;
	}
	
	@Override
	public int getPoCountByIgScsId(String igScsId) {
		int  count=0;
		try {
			String getCount = "select case when count(*)>0 then PO_ID else 0 end as PO_ID from po_hdr where IG_SCS_ID=? and IS_LATEST='1' and SEQUENCE_NO !=3 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igScsId);
			count = Integer.parseInt(resultMap.get("PO_ID").toString());

		} catch (Exception ex) {
			logger.error("getPoCountByIgScsId Method Exception --->" + ex);

		}
		return count;
	}
	@Override
	public int checkForCancelledPo(String igScsId, String tenantId) {
		int  count=0;
		try {
			String getCount = "select count(*) as COUNT from po_hdr where IG_SCS_ID=? and IS_LATEST=0 AND TENANT_ID=? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igScsId, tenantId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getPoCountByIgScsId Method Exception --->" + ex);

		}
		return count;
	}
	
	@Override
	public List<IndentDtlTblEntity> getIndentDtlsByScsId(String scsId, String tenantId) {
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
		try {
			String getDtlList = "SELECT \r\n" + 
					"  dtl.INDENT_DTL_ID\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_scs scs\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_dtl dtl ON scs.IG_HDR_ID = dtl.IG_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    scs.IG_SCS_ID=? and scs.TENANT_ID=?";
			list = this.jdbcTemplate.query(getDtlList, new IndentDtlTblRowMapper(), scsId, tenantId);
		} catch (Exception ex) {
			logger.error("getIndentDtlsByScsId method Error" + ex);
		}
		return list;
	}
	
	@Override
	public List<IndentDtlTblEntity> getIndentDtlsByPoId(String poId, String tenantId) {
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
		try {
			String getDtlList = "SELECT \r\n" + 
					"    dtl.INDENT_DTL_ID\r\n" + 
					"FROM\r\n" + 
					"    po_dtl dtl inner join po_hdr hdr on dtl.PO_ID=hdr.PO_ID\r\n" + 
					"WHERE\r\n" + 
					"    dtl.PO_ID = ? AND hdr.TENANT_ID = ? ";
			list = this.jdbcTemplate.query(getDtlList, new IndentDtlTblRowMapper(), poId, tenantId);
		} catch (Exception ex) {
			logger.error("getIndentDtlsByPoId method Error" + ex);
		}
		return list;
	}


	@Override
	public String getPoRevisionNoByIgScsId(String igScsId) {
		String  count="";
		try {
			String getCount = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN REVISION + 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END as COUNT \r\n" + 
					"FROM\r\n" + 
					"    po_hdr\r\n" + 
					"WHERE\r\n" + 
					"    IG_SCS_ID = ? AND SEQUENCE_NO = 3\r\n" + 
					"ORDER BY REVISION DESC";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igScsId);
			count = resultMap.get("COUNT").toString();

		} catch (Exception ex) {
			logger.error("getPoRevisionNoByIgScsId Method Exception --->" + ex);

		}
		return count;
	}
	
	@Override
	public String getScsIdByIgHdrId(String igHdrId) {
		String  scsId="";
		try {
			String getCount = "select IG_SCS_ID from indent_grp_scs where IG_HDR_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igHdrId);
			scsId = resultMap.get("IG_SCS_ID").toString();

		} catch (Exception ex) {
			logger.error("getScsIdByIgHdrId Method Exception --->" + ex);

		}
		return scsId;
	}
	
	
	@Override
	public int getIndentGrpDtlCountByIgHdrId(String igHdrId) {
		int  count=0;
		try {
			String getCount = "SELECT COUNT(*) as COUNT FROM indent_grp_dtl WHERE IG_HDR_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,igHdrId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getIndentGrpDtlCountByIgHdrId Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public String getScmBudgetValue(String indentId,String col) {
		String value="";
		try {
			if(col.equalsIgnoreCase("L1_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L1_FINAL_SUB_TOTAL) is not null THEN SUM(L1_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs igs\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl ven ON igs.IG_SCS_ID = ven.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    INDENT_ID = ?';";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
				value = resultMap.get("VALUE").toString();
				
				
			}else if(col.equalsIgnoreCase("L2_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L2_FINAL_SUB_TOTAL) is not null THEN SUM(L2_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs igs\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl ven ON igs.IG_SCS_ID = ven.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    INDENT_ID = ?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
				value = resultMap.get("VALUE").toString();
				
			}else if(col.equalsIgnoreCase("L3_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L3_FINAL_SUB_TOTAL) is not null THEN SUM(L3_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs igs\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl ven ON igs.IG_SCS_ID = ven.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    INDENT_ID = ?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
				value = resultMap.get("VALUE").toString();
			}
//			
//			String qry="SELECT \r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN  SUM("+col+") is not null THEN SUM("+col+")\r\n" + 
//					"        ELSE 0\r\n" + 
//					"    END\r\n" + 
//					"FROM\r\n" + 
//					"    indent_grp_scs igs\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_grp_scs_ven_dtl ven ON igs.IG_SCS_ID = ven.IG_SCS_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    INDENT_ID = '"+indentId+"';";
//			 
			
		}catch (Exception ex) {
			logger.error("getScsIdList Method Exception --->" + ex);

		}
		return value;
	}
	
	public String getindentScmVal(String indentId) {
		String value="";
		try {
			String qry="select case when SCM_BUDGET_ALLOCATED is null then 0 else SCM_BUDGET_ALLOCATED end AS VAL from indent_hdr where INDENT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			value = resultMap.get("VAL").toString();
			
		}catch (Exception ex) {
			logger.error("getScsIdList Method Exception --->" + ex);

		}
		return value;
	}
	
	
	
	public String getScmBudgetValueByigscsId(String igscsid,String col) {
		String value="";
		try {
			
			if(col.equalsIgnoreCase("L1_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L1_FINAL_SUB_TOTAL) is not null THEN SUM(L1_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs_ven_dtl \r\n" + 
						"WHERE\r\n" + 
						"    IG_SCS_ID = ?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igscsid);
				value = resultMap.get("VALUE").toString();
				
				
			}else if(col.equalsIgnoreCase("L2_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L2_FINAL_SUB_TOTAL) is not null THEN SUM(L2_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs_ven_dtl \r\n" + 
						"WHERE\r\n" + 
						"    IG_SCS_ID = ?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igscsid);
				value = resultMap.get("VALUE").toString();
				
			}else if(col.equalsIgnoreCase("L3_FINAL_SUB_TOTAL")) {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN  SUM(L3_FINAL_SUB_TOTAL) is not null THEN SUM(L3_FINAL_SUB_TOTAL)\r\n" + 
						"        ELSE 0\r\n" + 
						"    END as VALUE\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_scs_ven_dtl \r\n" + 
						"WHERE\r\n" + 
						"    IG_SCS_ID = ?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igscsid);
				value = resultMap.get("VALUE").toString();
			}
			
//			String qry="SELECT \r\n" + 
//					"    CASE\r\n" + 
//					"        WHEN  SUM("+col+") is not null THEN SUM("+col+")\r\n" + 
//					"        ELSE 0\r\n" + 
//					"    END\r\n" + 
//					"FROM\r\n" + 
//					"    indent_grp_scs_ven_dtl \r\n" + 
//					"WHERE\r\n" + 
//					"    IG_SCS_ID = '"+igscsid+"';";
			
		}catch (Exception ex) {
			logger.error("getScmBudgetValueByigscsId Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public String getIndentTargetValue(String indentId) {
		String targetVal="";
		try {
			String qry="select TARGET_VALUE from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			targetVal = resultMap.get("TARGET_VALUE").toString();
		} catch (Exception ex) {
			logger.error("getIndentTargetValue error " + ex.getMessage());
		}
		return targetVal;
	}

	@Override
	public String getBudgetExcessValue(String indentId) {
		String excessVal="0";
		try {
			String chk = "select count(*) > 0 as cnt from budget_excess_dtl where INDENT_ID = ? and IS_COMPLETED=1";
			Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(chk,indentId);
			 int cnt = Integer.valueOf(resultMap1.get("cnt").toString());
			 if(cnt > 0) {
				  String qry="select case when EXCESS > 0 then EXCESS ELSE 0 END AS EXCESS from budget_excess_dtl where INDENT_ID = ? and IS_COMPLETED=1 ORDER BY BE_HDR_ID DESC LIMIT 1";
				  Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
				  excessVal = resultMap.get("EXCESS").toString(); 
			 }
			
		} catch (Exception ex) {
			logger.error("getBudgetExcessValue error " + ex.getMessage());
		}
		return excessVal;
	}
	
	@Override
	public String getVendorNameByVendorCode(String vendorCode) {
		String vendorName="";
		try {
			String qry="select VENDOR_NAME from vendor_mst where VENDOR_CODE=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,vendorCode);
			vendorName = resultMap.get("VENDOR_NAME").toString();
		} catch (Exception ex) {
			logger.error("getVendorNameByVendorCode error " + ex.getMessage());
		}
		return vendorName;
	}
	@Override
	public String getVendorUniqueCodeByVendorCode(String vendorCode) {
		String vendorName="";
		try {
			String qry="select VENDOR_UNIQUE_CODE from vendor_mst where VENDOR_CODE=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,vendorCode);
			vendorName = resultMap.get("VENDOR_UNIQUE_CODE").toString();
		} catch (Exception ex) {
			logger.error("getVendorUniqueCodeByVendorCode error " + ex.getMessage());
		}
		return vendorName;
	}

	@Override
	public List<IndentGrpScsStatusEntity> getScsStatusList(String igScpId) {
		List<IndentGrpScsStatusEntity> list = new ArrayList<IndentGrpScsStatusEntity>();
		try {
			String qry = "SELECT \r\n" + 
					"    igs.*, doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,emp.EMPLOYEE_FIRSTNAME\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_scs_status igs\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code doc ON igs.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON igs.UPDATED_BY = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    igs.IG_SCS_ID = ?";
			list = this.jdbcTemplate.query(qry, new IndentGrpScsStatusRowMapper(), igScpId);

		} catch (Exception ex) {
			logger.error("getScsStatusList Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public String getScsStatusDesc(String igHdrId) {
		String statusDesc="";
		try {
			String qry="SELECT \r\n" + 
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_scs igs\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code doc ON igs.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"WHERE\r\n" + 
					"    IG_HDR_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igHdrId);
			statusDesc = resultMap.get("DOCUMENT_STATUS_TYPE_DESCRIPTION").toString();
		}catch (Exception e) {
				logger.error("getScsStatusDesc Method Exception --->" + e);
		}
		return statusDesc;
	}
	
	@Override
	public String getPoStatusDesc(String igScsId, String tenantId) {
		String statusDesc="";
		try {
			String qry="SELECT \r\n" + 
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    po_hdr po\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code doc ON po.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"WHERE\r\n" + 
					"    po.IG_SCS_ID = ? and SEQUENCE_STATUS not in ('DS100') and po.TENANT_ID = ? and IS_LATEST ='1' limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igScsId, tenantId);
			statusDesc = resultMap.get("DOCUMENT_STATUS_TYPE_DESCRIPTION").toString();
		}catch (Exception e) {
				logger.error("getPoStatusDesc Method Exception --->" + e);
		}
		return statusDesc;
	}

	@Override
	public int getBudgetExcessIsCompleted(String scpId) {
		int isCompleted=0;
		try {
			String qry="select count(*) as COUNT from budget_excess_dtl where IG_SCS_ID=? and IS_COMPLETED='1' and SEQUENCE_NO !=6";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scpId);
			isCompleted = Integer.parseInt(resultMap.get("COUNT").toString());
		}catch (Exception e) {
			logger.error("getBudgetExcessIsCompleted Method Exception --->" + e);
		}
		return isCompleted;
	}

	@Override
	public int getBudgetExcessDtlCount(String scpId) {
		int isCompleted=0;
		try {
			String qry="select count(*) as COUNT  from budget_excess_dtl where IG_SCS_ID=?and SEQUENCE_NO !=6";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scpId);
			isCompleted = Integer.parseInt(resultMap.get("COUNT").toString());
		}catch (Exception e) {
			logger.error("getBudgetExcessDtlCount Method Exception --->" + e);
		}
		return isCompleted;
	}

	@Override
	public int getScsPtCount(String scpID) {
		int count=0;
		try {
			String qry = "select count(*) as COUNT from indent_grp_scs_ven_pt where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scpID);
			count = Integer.parseInt(resultMap.get("COUNT").toString());
			
		} catch (Exception e) {
			logger.error("getScsPtCount method Error" + e);
		}
		return count;
	}


	@Override
	public void deleteScsPt(String scpID) {
		try {
			
			String deleteQuery = "delete from indent_grp_scs_ven_pt where IG_SCS_ID=?";
			
			this.jdbcTemplate.update(deleteQuery,scpID);
			
		} catch (Exception e) {
			logger.error("deleteScsPt method Error" + e);
		}
	}

	@Override
	public int poScsCheck(String indScpId) {
		int count=0;
		try {
			String qry = "select count(*) as COUNT from po_hdr where IG_SCS_ID =? and IS_LATEST = 1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indScpId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());
			
		} catch (Exception e) {
			logger.error("poScsCheck method Error" + e);
		}
		return count;
	}

	@Override
	public void deleteScpId(String indScpId, String tableName) {
		
		try {
			String qry = "delete from  "+tableName+"  where IG_SCS_ID=?";
			 this.jdbcTemplate.update(qry,indScpId);		
		} catch (Exception e) {
			logger.error("deleteScpId method Error" + e);
		}
	}

	@Override
	public void deletePoScsId(String indScpId, String tenantId) {
		
		try {
			String qry = "DELETE FROM po_dtl\r\n" + 
					"WHERE PO_ID IN (SELECT PO_ID FROM po_hdr WHERE IG_SCS_ID = ? and TENANT_ID = ?)";
			 this.jdbcTemplate.update(qry,indScpId, tenantId);	
			 
			String Qry = "DELETE FROM po_hdr WHERE IG_SCS_ID = ? and TENANT_ID = ?";
				 this.jdbcTemplate.update(Qry,indScpId, tenantId);
		} catch (Exception e) {
			logger.error("deletePoScsId method Error" + e);
		}
	}
	
	@Override
	public String getTenantPropertyVal(String propertyName,String tenantId) {
		String seq="";
		try {
			 seq=GetPropertyValue.getPropValue(propertyName, tenantId, jdbcTemplate);
		}catch (Exception e) {
				logger.error("getTenantPropertyVal Method Exception --->" + e);
		}
		return seq;
	}
	
	@Override
	public String getVendorCodeByScsId(String scsId,String vendorCodeColumn) {
		String vendorCode="";
		try {
			String qry ="select "+vendorCodeColumn+" AS L_VENDOR_CODE from indent_grp_scs_ven where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,scsId);
			vendorCode = resultMap.get("L_VENDOR_CODE").toString();
		}catch (Exception e) {
				logger.error("getVendorCodeByScsId Method Exception --->" + e);
		}
		return vendorCode;
	}

	@Override
	public List<IndentGroupDetailsEntity> getIndentGroupDtlsForSCS(String pmHdrId, String tenantId) {
		List<IndentGroupDetailsEntity> returnList = new ArrayList<IndentGroupDetailsEntity>();
		try {
			String retQry = "SELECT \r\n" + 
					"    hdr.IG_HDR_ID,\r\n" + 
					"    inh.INDENT_ID,\r\n" + 
					"    inh.INDENT_CODE,\r\n" + 
					"    hdr.GROUP_NAME,\r\n" + 
					"    inh.SBC_CODE,\r\n" + 
					"    sb.SBC_DESC,\r\n" + 
					"    EXPECTED_DELIVERY_DATE,\r\n" + 
					"    inh.PKA_ID,\r\n" + 
					"    pkam.PK_DESC,\r\n" + 
					"    inh.PKSA_ID,\r\n" + 
					"    pksam.PSK_DESC,\r\n" + 
					"    inh.TARGET_VALUE,\r\n" + 
					"    hdr.IS_INVENTORY,\r\n" + 
					"    scs.IG_SCS_ID\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_dtl dtl ON dtl.IG_HDR_ID = hdr.IG_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl ind ON ind.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr inh ON inh.INDENT_ID = ind.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    sales_budget_category sb ON sb.SBC_CODE = inh.SBC_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr ph ON ph.PM_HDR_ID = inh.PROJECT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area pka ON pka.PKA_ID = inh.PKA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area pksa ON pksa.PKSA_ID = inh.PKSA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area_mst pksam ON pksam.PSK_ID = pksa.PSK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_scs scs ON hdr.IG_HDR_ID = scs.IG_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    inh.PROJECT_ID = ?\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
					"        AND inh.SEQUENCE_STATUS IN ('DS020' , 'DS070', 'DS077', 'DS076')\r\n" + 
					"        AND hdr.IS_INVENTORY = '0' group by hdr.IG_HDR_ID; ";

			returnList = this.jdbcTemplate.query(retQry,new IndentGroupDetailsRowMapper(), pmHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("getIndentGroupRetrieve error---> " + ex);
		}
		return returnList;
	}
	@Override
	public String venDtlBasicCost(String igHdrId,String basicTotalCol) {
		String venDtlBasicCost="";
		try {
			if(basicTotalCol.equalsIgnoreCase("L1_FINAL_SUB_TOTAL")) {
				String venDtlBasicCostStr ="SELECT \r\n" + 
						"case when count(*)>0 then   grpven.L1_FINAL_SUB_TOTAL else 0 end as cost\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_hdr grphdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = grpscs.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    grphdr.IG_HDR_ID = ? limit 1";

				Map<String, Object> resultMap = jdbcTemplate.queryForMap(venDtlBasicCostStr,igHdrId);
				venDtlBasicCost = resultMap.get("cost").toString();
				
			}else if(basicTotalCol.equalsIgnoreCase("L2_FINAL_SUB_TOTAL")) {
				String venDtlBasicCostStr ="SELECT \r\n" + 
						"case when count(*)>0 then   grpven.L2_FINAL_SUB_TOTAL else 0 end as cost\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_hdr grphdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = grpscs.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    grphdr.IG_HDR_ID = ? limit 1";
				
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(venDtlBasicCostStr,igHdrId);
				venDtlBasicCost = resultMap.get("cost").toString();
				
			}else if(basicTotalCol.equalsIgnoreCase("L3_FINAL_SUB_TOTAL")) {
				String venDtlBasicCostStr ="SELECT \r\n" + 
						"case when count(*)>0 then   grpven.L3_FINAL_SUB_TOTAL else 0 end as cost\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_hdr grphdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = grpscs.IG_SCS_ID\r\n" + 
						"WHERE\r\n" + 
						"    grphdr.IG_HDR_ID = ? limit 1";
				
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(venDtlBasicCostStr,igHdrId);
				venDtlBasicCost = resultMap.get("cost").toString();
				
			}
//			String venDtlBasicCostStr ="SELECT \r\n" + 
//					"case when count(*)>0 then   grpven."+basicTotalCol+" else 0 end as cost\r\n" + 
//					"FROM\r\n" + 
//					"    indent_grp_hdr grphdr\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = grpscs.IG_SCS_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    grphdr.IG_HDR_ID = '"+igHdrId+"' limit 1";
			
			
			
		}catch (Exception e) {
				logger.error("venDtlBasicCost Method Exception --->" + e);
		}
		return venDtlBasicCost;
	}

	@Override
	public String venDtlBasicCostFx(String igHdrId, String basicTotalCol) {
		String venDtlBasicCostFx = "0";
		try {
			String columnName = "";
			if (basicTotalCol.equalsIgnoreCase("L1_FINAL_SUB_TOTAL")) {
				columnName = "L1_FINAL_SUB_TOTAL_FX";
			} else if (basicTotalCol.equalsIgnoreCase("L2_FINAL_SUB_TOTAL")) {
				columnName = "L2_FINAL_SUB_TOTAL_FX";
			} else if (basicTotalCol.equalsIgnoreCase("L3_FINAL_SUB_TOTAL")) {
				columnName = "L3_FINAL_SUB_TOTAL_FX";
			}

			if (!columnName.isEmpty()) {
				String sql = "SELECT " +
						"CASE WHEN COUNT(*) > 0 THEN grpven." + columnName + " ELSE 0 END AS cost " +
						"FROM indent_grp_hdr grphdr " +
						"INNER JOIN indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID " +
						"INNER JOIN indent_grp_scs_ven_dtl grpven ON grpven.IG_SCS_ID = grpscs.IG_SCS_ID " +
						"WHERE grphdr.IG_HDR_ID = ? LIMIT 1";

				Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, igHdrId);
				Object costValue = resultMap.get("cost");
				venDtlBasicCostFx = (costValue != null) ? costValue.toString() : "0";
			}

		} catch (Exception e) {
			logger.error("venDtlBasicCostFx Method Exception --->", e);
		}
		return venDtlBasicCostFx;
	}

	@Override
	public String indentGroupSCSType(String igHdrId) {
		String indentGroupSCSType="";
		try {
			String indentGroupSCSTypeStr ="SELECT \r\n" + 
					"   case when count(*)>0 and grpscs.TYPE is not null then grpscs.TYPE else '' end AS type\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_hdr grphdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_scs grpscs ON grphdr.IG_HDR_ID = grpscs.IG_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    grphdr.IG_HDR_ID = ? limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(indentGroupSCSTypeStr,igHdrId);
			indentGroupSCSType = resultMap.get("type").toString();
		}catch (Exception e) {
				logger.error("indentGroupSCSType Method Exception --->" + e);
		}
		return indentGroupSCSType;
	}

	@Override
	public String getindentCode(String scsId) {
		String indentCode="";
		try {
			String indentQry="SELECT \r\n" + 
					"    INDENT_CODE\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_scs scs\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr hd ON hd.INDENT_ID = scs.INDENT_ID\r\n" + 
					"WHERE\r\n" + 
					"    scs.IG_SCS_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(indentQry,scsId);
			indentCode = resultMap.get("INDENT_CODE").toString();
		}catch(Exception ex) {
			logger.error("getindentCode error "+ex);
		}
		return indentCode;
	}

	@Override
	public String getindentIdBygrpScd(String igScsId) {
		String indentId="";
		try {
			String indentQry="select INDENT_ID from  indent_grp_scs where IG_SCS_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(indentQry,igScsId);
			indentId = resultMap.get("INDENT_ID").toString();
		}catch(Exception ex) {
			logger.error("getindentIdBygrpScd error "+ex);
		}
		return indentId;
	}

	@Override
	public List<String> getDistinctScsDocGroup(String docType,String tenantId, String processDoc) {
		List<String> docGrp = new ArrayList<>();
		try {
			String Qry = "select distinct(DOC_GROUP) from document_lifecycle_mst where DOC_TYPE='"+docType+"' and PROCESS_CODE = '"+processDoc+"' and TENANT_ID = '"+tenantId+"' order by DOC_GROUP +1 ";
			docGrp = this.jdbcTemplate.queryForList(Qry,String.class);
		} catch (Exception ex) {
			logger.error("getDistinctScsDocGroup Error" + ex);
		}
		return docGrp;
	}

	@Override
	public String getVenQualifiedByIgHdrId(String igHdrId) {
		String value = "";
		try {
			String qry = "select case when count(*)>0 then VENDOR_QUALIFIED else 'NA' end as VAL from  indent_grp_scs where IG_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igHdrId);
			value = resultMap.get("VAL").toString();

		} catch (Exception ex) {
			logger.error("getVenQualifiedByIgHdrId Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public int pjsversioncheck(String igScsId) {
	    int value = 0;
	    try {
	        String qry = "SELECT \r\n" + 
	        		"    CASE\r\n" + 
	        		"        WHEN curr.SEQUENCE_NO < prev.SEQUENCE_NO THEN 1\r\n" + 
	        		"        ELSE 0\r\n" + 
	        		"    END AS IS_VALID\r\n" + 
	        		"FROM\r\n" + 
	        		"    (SELECT \r\n" + 
	        		"        CAST(s1.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO\r\n" + 
	        		"    FROM\r\n" + 
	        		"        indent_grp_scs_status s1 inner join \r\n" + 
	        		"        indent_grp_scs scs1 on scs1.IG_SCS_ID = s1.IG_SCS_ID inner join\r\n" + 
	        		"        indent_grp_hdr hdr1 on hdr1.IG_HDR_ID = scs1.IG_HDR_ID\r\n" + 
	        		"    WHERE\r\n" + 
	        		"        hdr1.IG_HDR_ID = ?\r\n" + 
	        		"    ORDER BY s1.IG_SCSS_ID DESC\r\n" + 
	        		"    LIMIT 1) AS curr,\r\n" + 
	        		"    (SELECT \r\n" + 
	        		"        CAST(s.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO\r\n" + 
	        		"    FROM\r\n" + 
	        		"        indent_grp_scs_status s inner join \r\n" + 
	        		"        indent_grp_scs scs on scs.IG_SCS_ID = s.IG_SCS_ID inner join\r\n" + 
	        		"        indent_grp_hdr hdr on hdr.IG_HDR_ID = scs.IG_HDR_ID\r\n" + 
	        		"    WHERE\r\n" + 
	        		"        hdr.IG_HDR_ID = ?\r\n" + 
	        		"    ORDER BY s.IG_SCSS_ID DESC\r\n" + 
	        		"    LIMIT 1 OFFSET 1) AS prev;";

	        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(qry, igScsId, igScsId);

	        if (!resultList.isEmpty()) {
	            value = Integer.parseInt(resultList.get(0).get("IS_VALID").toString());
	        }

	    } catch (Exception ex) {
	        logger.error("pjsversioncheck Method Exception --->" + ex);
	    }
	    return value;
	}
	
	@Override
	public String getLastUpdatedDateTime(String indentDtlId, String tenantId,String type) {
		String dateTime = null;
		try {
			String qry="";
			if (type.equalsIgnoreCase("PO")) {
				
				 qry = "SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(dtl.QTY) >= indtl.QTY THEN MAX(hdr.LAST_UPDATED_DATETIME)\r\n" + 
						"        ELSE ''\r\n" + 
						"    END AS LAST_UPDATED_DATETIME\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl indtl\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    po_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_hdr hdr ON dtl.PO_ID = hdr.PO_ID\r\n" + 
						"WHERE\r\n" + 
						"    indtl.INDENT_DTL_ID = ?\r\n" + 
						"        AND indtl.TENANT_ID = ?\r\n" + 
						"        AND hdr.IS_APPROVED = '1'\r\n" + 
						"        AND hdr.IS_LATEST = 1\r\n" + 
						"        AND hdr.SEQUENCE_NO != 3\r\n" + 
						"        AND hdr.PO_ID IS NOT NULL\r\n" + 
						"ORDER BY hdr.LAST_UPDATED_DATETIME DESC\r\n" + 
						"LIMIT 1";
			
			}else if (type.equalsIgnoreCase("QI_REQUEST")) {

				 qry = "SELECT \r\n" + 
				 		"    CASE\r\n" + 
				 		"        WHEN SUM(qir.QTY_TO_BE_INSPECTED) >= id.QTY THEN MAX(INSPECTION_REQUESTED_DATE)\r\n" + 
				 		"        ELSE ''\r\n" + 
				 		"    END AS LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    quality_inspection_request qir\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_dtl id ON qir.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
				 		"        LEFT JOIN\r\n" + 
				 		"    quality_inspection_hdr qih ON qir.QI_ID = qih.QI_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    qir.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND qir.TENANT_ID = ? AND qir.IS_LATEST = 1 \r\n" + 
				 		"        AND (CANCEL_FLAG =0 OR CANCEL_FLAG is NULL)\r\n" + 
				 		"ORDER BY qih.INSPECTED_ON DESC\r\n" + 
				 		"LIMIT 1;";
				
			}else if (type.equalsIgnoreCase("QI")) {

				 qry = "SELECT \r\n" + 
				 		"    CASE\r\n" + 
				 		"        WHEN SUM(qih.CA_VENDOR + qih.CA_INTERNAL + qih.OK_QTY) >= id.QTY THEN MAX(INSPECTION_REQUESTED_DATE)\r\n" + 
				 		"        ELSE ''\r\n" + 
				 		"    END AS LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    quality_inspection_hdr qih\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    quality_inspection_request qir ON qih.QI_ID = qir.QI_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_dtl id ON qir.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    qir.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND qir.TENANT_ID = ?\r\n" + 
				 		"        AND qih.IS_COMPLETED = 1 and qih.IS_LATEST=1\r\n" + 
				 		"        AND qih.CANCEL_FLAG = 0\r\n" + 
				 		"ORDER BY qih.INSPECTED_ON DESC\r\n" + 
				 		"LIMIT 1;";
				
			}else if (type.equalsIgnoreCase("MI")) {

				 qry = "SELECT \r\n" + 
				 		"    case when SUM(indtl.RECEIVED_QTY)>= dtl.QTY then MAX(hdr.CREATED_ON) else '' end LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    indent_dtl dtl\r\n" + 
				 		"        LEFT JOIN\r\n" + 
				 		"    material_inward_dtl indtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    material_inward_hdr hdr ON hdr.MI_ID = indtl.MI_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    dtl.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND dtl.TENANT_ID = ?\r\n" + 
				 		"        AND hdr.IS_LATEST = 1\r\n" + 
				 		"        AND hdr.MI_ID IS NOT NULL\r\n" + 
				 		"ORDER BY hdr.CREATED_ON DESC\r\n" + 
				 		"LIMIT 1;";
				
			}else if (type.equalsIgnoreCase("GRN")) {

				 qry = "SELECT \r\n" + 
				 		"    CASE\r\n" + 
				 		"        WHEN SUM(gdtl.RECEIVED_QTY) >= idtl.QTY THEN MAX(ghdr.CREATED_ON)\r\n" + 
				 		"        ELSE ''\r\n" + 
				 		"    END AS LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    grn_dtl gdtl\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_dtl idtl ON idtl.INDENT_DTL_ID = gdtl.INDENT_DTL_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    grn_hdr ghdr ON ghdr.GRN_HDR_ID = gdtl.GRN_HDR_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    gdtl.INDENT_DTL_ID = ? AND ghdr.IS_LATEST=1 \r\n" + 
				 		"        AND gdtl.TENANT_ID = ?\r\n" + 
				 		"        AND ghdr.GRN_HDR_ID IS NOT NULL\r\n" + 
				 		"ORDER BY ghdr.CREATED_ON DESC\r\n" + 
				 		"LIMIT 1;";

			}else if (type.equalsIgnoreCase("PRA_REQUEST")) {

				 qry = "SELECT \r\n" + 
				 		"    MAX(PRA_DATE) AS LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    pra_hdr hdr\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    po_dtl pdtl ON hdr.PO_ID = pdtl.PO_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_dtl idtl ON pdtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    po_payment_term pterm ON pterm.PO_ID = pdtl.PO_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    pdtl.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND (IS_COMPLETED = 0 or IS_COMPLETED is null)\r\n" + 
				 		"        AND hdr.TENANT_ID = ?";

			}else if (type.equalsIgnoreCase("PRA")) {

				 qry = "SELECT \r\n" + 
				 		"    MAX(COMPLETED_DATETIME) AS LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    pra_hdr hdr\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    po_dtl pdtl ON hdr.PO_ID = pdtl.PO_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_dtl idtl ON pdtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    po_payment_term pterm ON pterm.PO_ID = pdtl.PO_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    pdtl.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND IS_COMPLETED = 1\r\n" + 
				 		"        AND hdr.TENANT_ID = ?;";

			}if (type.equalsIgnoreCase("PJS")) {

				 qry = "SELECT \r\n" + 
				 		"    CASE\r\n" + 
				 		"        WHEN SUM(igd.QTY) >= dtl.QTY THEN MAX(scs.LAST_UPDATED_DATETIME)\r\n" + 
				 		"        ELSE ''\r\n" + 
				 		"    END LAST_UPDATED_DATETIME\r\n" + 
				 		"FROM\r\n" + 
				 		"    indent_grp_dtl igd\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_grp_hdr hdr ON hdr.IG_HDR_ID = igd.IG_HDR_ID\r\n" + 
				 		"        INNER JOIN\r\n" + 
				 		"    indent_grp_scs scs ON scs.IG_HDR_ID = hdr.IG_HDR_ID\r\n" + 
				 		"        JOIN\r\n" + 
				 		"    indent_dtl dtl ON igd.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
				 		"WHERE\r\n" + 
				 		"    dtl.INDENT_DTL_ID = ?\r\n" + 
				 		"        AND hdr.TENANT_ID = ?\r\n" + 
				 		"        AND hdr.IG_HDR_ID IS NOT NULL\r\n" + 
				 		"        AND scs.IS_APPROVED = '1'\r\n" + 
				 		"ORDER BY hdr.CREATED_DATETIME DESC\r\n" + 
				 		"LIMIT 1;";

			}
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, indentDtlId, tenantId);
			dateTime = resultMap.get("LAST_UPDATED_DATETIME").toString();
		} catch (Exception ex) {
			logger.error("indentPoDtlDateTime Method Exception --->" + ex);

		}
		return dateTime;
	}
	
	@Override
	public int updateLastUpdatedDateTime(String indentDtlId, String colName,String datetime,String tenantId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_dtl SET "+colName+"=? WHERE INDENT_DTL_ID=? and TENANT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,datetime, indentDtlId,tenantId);

		} catch (Exception ex) {
			logger.error("updateLastUpdatedDateTime method Error" + ex);
		}
		return updateStatus;

	}
	
	@Override
	public int reUpdatedDateTimeIndentDtl(String indentDtlId, String colName, String tenantId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_dtl SET "+colName+"= null WHERE INDENT_DTL_ID=? and TENANT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, indentDtlId,tenantId);

		} catch (Exception ex) {
			logger.error("updateLastUpdatedDateTime method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String getIndentIdByIgDtlId(String igDtlId) {
		String value = "";
		try {
			String qry = "SELECT \n"
					+ "    dtl.INDENT_ID AS INDENT_ID \n"
					+ "FROM\n"
					+ "    indent_grp_dtl gdtl\n"
					+ "        INNER JOIN\n"
					+ "    indent_dtl dtl ON gdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n"
					+ "WHERE\n"
					+ "    gdtl.IG_DTL_ID = ? \n"
					+ "LIMIT 1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igDtlId);
			value =resultMap.get("INDENT_ID").toString() ;

		} catch (Exception ex) {
			logger.error("getIndentIdByIgDtlId Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public String getIndentIdByIgHdrId(String igHdrId) {
		String value ="";
		try {
			String qry = "SELECT \n"
					+ "    dtl.INDENT_ID AS INDENT_ID \n"
					+ "FROM\n"
					+ "    indent_grp_dtl gdtl\n"
					+ "        INNER JOIN\n"
					+ "    indent_dtl dtl ON gdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n"
					+ "WHERE\n"
					+ "    IG_HDR_ID = ? \n"
					+ "LIMIT 1 ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igHdrId);
			value =resultMap.get("INDENT_ID").toString() ;

		} catch (Exception ex) {
			logger.error("getIndentIdByIgHdrId Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public int lastIndentGrpDtlCheck(String indentDtlId) {
		int value = 0;
		try {
			String qry = "SELECT \n"
					+ "    COUNT(*) AS COUNT \n"
					+ "FROM\n"
					+ "    indent_grp_dtl grpDtl\n"
					+ "WHERE\n"
					+ "    grpDtl.IG_HDR_ID IN (SELECT \n"
					+ "            dtl.IG_HDR_ID\n"
					+ "        FROM\n"
					+ "            indent_grp_dtl dtl\n"
					+ "        WHERE\n"
					+ "            dtl.IG_DTL_ID = ? )";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId);
			value =Integer.parseInt(resultMap.get("COUNT").toString()) ;

		} catch (Exception ex) {
			logger.error("lastIndentGrpDtlCheck Method Exception --->" + ex);

		}
		return value;
	}

	public String getIgHdrId(String refId, String tENANT_ID) {
		String value ="";
		try {
			String qry = "select IG_HDR_ID from indent_grp_scs where IG_SCS_ID = ? and TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, refId, tENANT_ID);
			value =resultMap.get("IG_HDR_ID").toString();

		} catch (Exception ex) {
			logger.error("getIgHdrId Method Exception --->" + ex);
		}
		return value;
	}

	@Override
	public String getIsPdfOrNot(String dmId) {
		String value ="";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS IS_PDF\r\n" + 
					"FROM\r\n" + 
					"    file_manager\r\n" + 
					"WHERE\r\n" + 
					"    REFERNCE_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, dmId);
			value =resultMap.get("IS_PDF").toString();

		} catch (Exception ex) {
			logger.error("getIsPdfOrNot Method Exception --->" + ex);
		}
		return value;
	}

	@Override
	public String getScsGrpType(String scsId) {
		String type="";
		try {
			String indentQry="select case when type='Cash Voucher' then 1 else 0 end as type from indent_grp_scs where IG_SCS_ID= ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(indentQry,scsId);
			type = resultMap.get("type").toString();
		}catch(Exception ex) {
			logger.error("getindentCode error "+ex);
		}
		return type;
	}

	@Override
	public void updateInvStockYesOrNo(String type, String igHdrId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_grp_hdr SET IS_INVENTORY=? WHERE IG_HDR_ID=? ";
			updateStatus = this.jdbcTemplate.update(qry,type, igHdrId);

		} catch (Exception ex) {
			logger.error("updateInvStockYesOrNo method Error" + ex);
		}
	}

	@Override
	public int getPendingUngroupedItemCount(String indentId, String tenantId) {
		int count = 0;
		try {
			String qry = "SELECT COUNT(*) FROM (" +
					"    SELECT dtl.INDENT_DTL_ID" +
					"    FROM indent_dtl dtl" +
					"    LEFT JOIN indent_grp_dtl gdtl ON dtl.INDENT_DTL_ID = gdtl.INDENT_DTL_ID" +
					"    WHERE dtl.INDENT_ID = ?" +
					"    AND dtl.TENANT_ID = ?" +
					"    GROUP BY dtl.INDENT_DTL_ID, dtl.QTY" +
					"    HAVING COALESCE(SUM(gdtl.QTY), 0) < dtl.QTY" +
					") AS pending";
			count = this.jdbcTemplate.queryForObject(qry, Integer.class, indentId, tenantId);
		} catch (Exception ex) {
			logger.error("getPendingUngroupedItemCount method Error" + ex);
		}
		return count;
	}

	@Override
	public String getOtherCommittedScsTotalByPkaId(String pkaId, String excludeIndentId, String minSeqNo) {
		String totalVal = "0";
		try {
			String qry = "SELECT CASE WHEN COUNT(*) > 0 THEN SUM(ih.SCM_BUDGET_ALLOCATED) ELSE 0 END AS VAL " +
					"FROM indent_grp_scs scs " +
					"INNER JOIN indent_hdr ih ON ih.INDENT_ID = scs.INDENT_ID " +
					"WHERE ih.PKA_ID = ? AND scs.INDENT_ID <> ? AND scs.SEQUENCE_NO >= ? " +
					"AND NOT EXISTS (" +
					"    SELECT 1 FROM po_hdr ph WHERE ph.INDENT_ID = scs.INDENT_ID AND ph.IS_LATEST = 1 AND ph.IS_APPROVED = 1" +
					")";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pkaId, excludeIndentId, minSeqNo);
			totalVal = resultMap.get("VAL").toString();
		} catch (Exception ex) {
			logger.error("getOtherCommittedScsTotalByPkaId method Error" + ex);
		}
		return totalVal;
	}

}