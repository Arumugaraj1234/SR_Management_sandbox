package com.vmfg.design.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.dao.interfaces.IDesignDAO;
import com.vmfg.design.entity.GetKeySubAreaDtlEntity;
import com.vmfg.design.entity.GetPlanAndActualEntity;
import com.vmfg.design.entity.GetTasKTemplateHdrEntity;
import com.vmfg.design.entity.ProductBasedInventoryDtlEntity;
import com.vmfg.design.entity.ProductBasedPoDtlEntity;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.entity.ProductMstEntity;
import com.vmfg.design.entity.ProjectKeyAreaMstEntity;
import com.vmfg.design.entity.getPoDetailByIndentDtlEntity;
import com.vmfg.design.request.GetKeySubAreaByPKIdRequest;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.design.response.KeyArea_ID;
import com.vmfg.design.rowmapper.DesignHdrRowMapper;
import com.vmfg.design.rowmapper.DesignSubAreaIDRowMapper;
import com.vmfg.design.rowmapper.GetKeySubAreaDtlRowMapper;
import com.vmfg.design.rowmapper.GetPlanAndActualRowMapper;
import com.vmfg.design.rowmapper.GetTasKTemplateHdrRowMapper;
import com.vmfg.design.rowmapper.ProductBasedInventoryDtlRowMapper;
import com.vmfg.design.rowmapper.ProductBasedPoDtlRowMapper;
import com.vmfg.design.rowmapper.ProductMstRowMapper;
import com.vmfg.design.rowmapper.ProjectKeyAreaMstRowMapper;
import com.vmfg.design.rowmapper.getPoDetailByIndentDtlRowMapper;
import com.vmfg.design.rowmapper.productMstDropDownRowMapper;
import com.vmfg.project.entity.ProjectSubAreaExtnEntity;
import com.vmfg.project.rowmapper.ProjectSubAreaExtnRowMapper;

@Repository
public class DesignDAO implements IDesignDAO {
	private static final Logger logger = LoggerFactory.getLogger(DesignDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	@Override
	public List<DesignHdr> getDesignHdr(String fromDate, String toDate, String customer, String processId, String empId,
			String tenantID, String designID,String projectId) {
		logger.debug("getDesignHdr DAO method start");
		List<DesignHdr> hdr = null;
		try {
			String custname = "";
			if (customer.equalsIgnoreCase("")) {
				custname = "%%";
			} else {
				custname = "%" + customer + "%";
			}

			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "  hdr.REQUEST_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND";
			}
			String pro = " and hdr.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and hdr.PM_HDR_ID = '"+projectId+"'";
			}
			
			if (designID.isEmpty()) {
				String getQ = "SELECT \r\n" + "    hdr.*,\r\n" + "    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
						+ "    proj.ENQUIRY_ID,\r\n" + "    proj.PROJECT_CODE,\r\n" + "    em.EMPLOYEE_FIRSTNAME,\r\n"
						+ "    stg.STG_DESC, IS_INTERNAL\r\n" + "FROM\r\n" + "    design_hdr hdr,\r\n"
						+ "    document_status_type_code ds,\r\n" + "    employee_mst em,\r\n"
						+ "    project_hdr proj,\r\n" + "    process_assigned_team pat, stg_master stg, sales_enq_hdr enq \r\n"
						+ "WHERE\r\n" + datediff  + "     hdr.TENANT_ID = ?\r\n"
						+ "        and stg.STG_CODE = hdr.TRANSACTION_STAGE \r\n"
						+ "        and proj.ENQUIRY_ID = enq.SE_ID\r\n"
						+ "        AND hdr.TRANSACTION_STATUS = ds.DOCUMENT_STATUS_TYPE_CODE AND pat.TENANT_ID = hdr.TENANT_ID \r\n"
						+ "        AND proj.PM_HDR_ID = hdr.PM_HDR_ID\r\n"
						+ "        AND hdr.DE_HDR_ID = pat.MASTER_ID\r\n" + "        AND pat.PM_ID = ?\r\n"
						+ "        AND ASSIGNED_EMP_ID = ?\r\n" + "        AND hdr.CUSTOMER_NAME LIKE ?\r\n"
						+ "        AND hdr.REQUESTED_BY = em.EMPLOYEE_ID\r\n" + "        AND pat.IS_ACTIVE = 1"+pro;

				RowMapper<DesignHdr> rowmapper = new DesignHdrRowMapper();
				hdr = this.jdbcTemplate.query(getQ, rowmapper, tenantID, processId, empId, custname);
			} else {
				String getQ = "SELECT  \n" +
						"    hdr.*,  \n" +
						"    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION,  \n" +
						"    proj.ENQUIRY_ID,  \n" +
						"    proj.PROJECT_CODE,  \n" +
						"    em.EMPLOYEE_FIRSTNAME,  \n" +
						"    stg.STG_DESC , IS_INTERNAL\n" +
						"FROM  \n" +
						"    design_hdr hdr,  \n" +
						"    document_status_type_code ds,  \n" +
						"    employee_mst em,  \n" +
						"    project_hdr proj,  \n" +
						"    stg_master stg ,\n" +
						"    sales_enq_hdr enq\n" +
						"WHERE  \n" +
						"    hdr.TENANT_ID = ?  \n" +
						"    AND hdr.DE_HDR_ID = ?  \n" +
						"    AND stg.STG_CODE = hdr.TRANSACTION_STAGE  \n" +
						"    AND hdr.TRANSACTION_STATUS = ds.DOCUMENT_STATUS_TYPE_CODE  \n" +
						"    AND proj.PM_HDR_ID = hdr.PM_HDR_ID\n" +
						"    and proj.ENQUIRY_ID = enq.SE_ID\n" +
						"    AND hdr.REQUESTED_BY = em.EMPLOYEE_ID;";

				RowMapper<DesignHdr> rowmapper = new DesignHdrRowMapper();
				hdr = this.jdbcTemplate.query(getQ, rowmapper, tenantID, designID);
			}

		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}

		logger.debug("getDesignHdr DAO method end");
		return hdr;
	}

	@Override
	public String getTaskPlanned(String designID, String dept, String tenantID, String completionStatus) {

		String retrnMsg = "";
		try {
			String getQ = "SELECT \r\n" + "    count(*) as COUNT\r\n" + "FROM\r\n" + "    task_entry_hdr hdr,\r\n"
					+ "    task_entry_dtl dtl\r\n" + "WHERE\r\n" + "    hdr.TE_HDR_ID = dtl.TE_HDR_ID and MASTER_ID = ?"
					+ " and DEPARTMENT_CODE = ? and hdr.TENANT_ID=?"
					+ " and IS_COMPLETED like ? And DEPENDENT_TE_HDR_ID is null ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,designID,dept,tenantID,completionStatus);
			retrnMsg = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return retrnMsg;

	}

	@Override
	public String getIndentPlanned(String designID, String dept, String tenantID, String completionStatus) {
		String retrnMsg = "";
		try {
			String getQ = " SELECT \r\n" + 
					"    COUNT(*) AS COUNT\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr\r\n" + 
					"WHERE\r\n" + 
					"     MASTER_ID = ? AND DEPARTMENT_CODE = ? AND TENANT_ID = ? AND IS_COMPLETED like ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,designID,dept,tenantID,completionStatus);
			retrnMsg = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return retrnMsg;
	}

	public List<GetPlanAndActualEntity> getTaskPlannedGroupList(String designID, String dept, String tenantID, String completionStatus) {
		List<GetPlanAndActualEntity> retrnMsg = new ArrayList<GetPlanAndActualEntity>();
		try {
			String getQ = "SELECT \r\n" + "    SUM(CASE\n"
					+ "        WHEN hdr.TE_HDR_ID IS NOT NULL THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) AS PLAN_COUNT,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN IS_COMPLETED = 1 THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) ACTUAL_COUNT \r\n" + "FROM\r\n" + "    task_entry_hdr hdr,\r\n"
					+ "    task_entry_dtl dtl\r\n" + "WHERE\r\n" + "    hdr.TE_HDR_ID = dtl.TE_HDR_ID and MASTER_ID = '"
					+ designID + "' and DEPARTMENT_CODE = '" + dept + "' and hdr.TENANT_ID='" + tenantID
					+ "'  And DEPENDENT_TE_HDR_ID is null";
			RowMapper<GetPlanAndActualEntity> rowmapper = new GetPlanAndActualRowMapper();
			retrnMsg = this.jdbcTemplate.query(getQ, rowmapper);
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return retrnMsg;
	}
	public List<GetPlanAndActualEntity> getIndentPlannedGroupList(String designID, String dept, String tenantID, String completionStatus) {
		List<GetPlanAndActualEntity> retrnMsg = new ArrayList<GetPlanAndActualEntity>();
		try {
			String getQ =  "SELECT \r\n" + 
					"    SUM(CASE\n"
					+ "        WHEN hdr.INDENT_ID IS NOT NULL THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) AS PLAN_COUNT,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN hdr.IS_COMPLETED = 1 THEN 1\n"
					+ "        ELSE 0\n"
					+ "    END) ACTUAL_COUNT \r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr \r\n" + 
					"WHERE\r\n" + 
					"     MASTER_ID = '"+designID+"'AND DEPARTMENT_CODE = '"+dept+"'AND TENANT_ID = '"+tenantID+"' ";
			RowMapper<GetPlanAndActualEntity> rowmapper = new GetPlanAndActualRowMapper();
			retrnMsg = this.jdbcTemplate.query(getQ, rowmapper);
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return retrnMsg;
	}
	@Override
	public String getIndentPlannedByProject(String projId, String dept, String tenantID, String completionStatus) {
		String retrnMsg = "";
		try {
			String getQ = "SELECT \r\n" + "    COUNT(*) AS COUNT\r\n" + "FROM\r\n" + "    indent_hdr ihdr,\r\n"
					+ "    design_hdr dhdr\r\n" + "WHERE\r\n" + "    ihdr.MASTER_ID = dhdr.DE_HDR_ID\r\n"
					+ "        AND dhdr.PM_HDR_ID = ?  AND ihdr.DEPARTMENT_CODE =?  AND ihdr.TENANT_ID = ?"
					+ "        AND ihdr.IS_COMPLETED = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,projId,dept,tenantID,completionStatus);
			retrnMsg = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return retrnMsg;
	}

	@Override
	public String getIndentPlannedByProjectAndsts(String projId, String tenantID, String completionStatus) {
		String retrnMsg = "";
		try {
			String getQ = "SELECT \r\n" + "    COUNT(*) as COUNT\r\n" + "FROM\r\n" + "    indent_hdr ihdr,\r\n"
					+ "    design_hdr dhdr\r\n" + "WHERE\r\n" + "    ihdr.MASTER_ID = dhdr.DE_HDR_ID\r\n"
					+ "        AND dhdr.PM_HDR_ID = ?" + "     \r\n"
					+ "        AND ihdr.TENANT_ID = ?" + "        AND ihdr.IS_COMPLETED =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ,projId,tenantID,completionStatus);
			retrnMsg = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("Error with method getIndentPlannedByProjectAndsts " + ex.getMessage());
		}
		return retrnMsg;
	}

	@Override
	public List<ProjectKeyAreaMstEntity> getKeyArea(ProductDtlDropDownRequest tentReq) {
		List<ProjectKeyAreaMstEntity> rl = null;
		RowMapper<ProjectKeyAreaMstEntity> rowmapper = new ProjectKeyAreaMstRowMapper();

		try {
			if (tentReq.getPmHdrId().equalsIgnoreCase("")) {
				String getQ = "select * from project_key_area_mst where TENANT_ID=? group by PK_DESC ";

				rl = this.jdbcTemplate.query(getQ, rowmapper, tentReq.getTenantId());
			} else {
				String getQ = "SELECT \r\n" + "    pka.*,ka.PKA_ID\r\n" + "FROM\r\n"
						+ "    project_key_area_mst pka\r\n" + "        INNER JOIN\r\n"
						+ "    project_key_area ka ON pka.PK_ID = ka.PK_ID\r\n" + "WHERE\r\n"
						+ "    ka.PM_HDR_ID = ? and pka.TENANT_ID =? ";
				rl = this.jdbcTemplate.query(getQ, rowmapper, tentReq.getPmHdrId(), tentReq.getTenantId());
			}
		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<ProjectKeyAreaMstEntity> getKeySubArea(ProductDtlDropDownRequest tentReq) {
		List<ProjectKeyAreaMstEntity> rl = null;
		RowMapper<ProjectKeyAreaMstEntity> rowmapper = new ProjectKeyAreaMstRowMapper();

		try {
			if (tentReq.getPmHdrId().equalsIgnoreCase("")) {
				String getQ = "select * from project_key_sub_area_mst where TENANT_ID=? group by PSK_DESC ";
				rl = this.jdbcTemplate.query(getQ, rowmapper, tentReq.getTenantId());
			} else {
				String getQ = "SELECT \r\n" + "    pka.*,ka.PKSA_ID\r\n" + "FROM\r\n"
						+ "    project_key_sub_area_mst pka\r\n" + "        INNER JOIN\r\n"
						+ "    project_key_sub_area ka ON pka.PSK_ID = ka.PSK_ID\r\n" + "WHERE\r\n"
						+ "    ka.PM_HDR_ID = ? and pka.TENANT_ID = ? ";

				rl = this.jdbcTemplate.query(getQ, rowmapper, tentReq.getPmHdrId(), tentReq.getTenantId());
			}

		} catch (Exception ex) {
			logger.error("Error with method getDesignHdr " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<ProductMstDropDownEntity> getAllProductsByPmHdrId(String pmHdrId, String tenantId, String isQuantity) {
		List<ProductMstDropDownEntity> rl = null;
		String getQ = "";
		try {
			if (isQuantity != null && isQuantity.equals("1") && !isQuantity.isEmpty()) {

				getQ = "SELECT  pm.*, um.UOM_LONG_DESCRIPTION, um.UOM_SHORT_DESCRIPTION FROM\r\n"
						+ "					  product_mst AS pm INNER JOIN\r\n"
						+ "				  uom_mst AS um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE WHERE\r\n"
						+ "					   pm.PM_HDR_ID = '" + pmHdrId + "' AND pm.TENANT_ID = '" + tenantId
						+ "' AND pm.QTY>0  order by  pm.PKA_ID ,pm.PSKA_ID";

			} else {
				getQ = "SELECT \r\n" + "    pm.*, um.UOM_LONG_DESCRIPTION, um.UOM_SHORT_DESCRIPTION\r\n" + "FROM\r\n"
						+ "    product_mst AS pm\r\n" + "        INNER JOIN\r\n"
						+ "    uom_mst AS um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + "WHERE\r\n"
						+ "    pm.PM_HDR_ID = '" + pmHdrId + "' AND pm.TENANT_ID = '" + tenantId
						+ "'  order by  pm.PKA_ID ,pm.PSKA_ID";
			}
			rl = this.jdbcTemplate.query(getQ, new productMstDropDownRowMapper());
		} catch (Exception ex) {
			logger.error("Error with method getAllProductsByPmHdrId " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public int insertDesignSubKey(String pmHdrId, String pskId, String tenantId, String pkaId) {
		int insertDesignSubKey = 0;
		try {
			String checkCount = "select count(*) as COUNT from project_key_sub_area where PM_HDR_ID=? "
					+ " and PSK_ID=? and TENANT_ID=? and PKA_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(checkCount,pmHdrId,pskId,tenantId,pkaId);
			int count = Integer.parseInt(result.get("COUNT").toString());
			if (count == 0) {
				String insertDesignSubKeyStr = "INSERT INTO `project_key_sub_area` (`PM_HDR_ID`, `PSK_ID`, `TENANT_ID`,`PKA_ID`) VALUES (?,?,?,?)";
				insertDesignSubKey = this.jdbcTemplate.update(insertDesignSubKeyStr, pmHdrId, pskId, tenantId, pkaId);
			} else {
				insertDesignSubKey = 1;
			}
		} catch (Exception ex) {
			logger.error("insertDesignSubKey Error " + ex);
		}
		return insertDesignSubKey;
	}

	@Override
	public int updateDesignSubKey(String PKSA_ID, String pmHdrId, String pskId, String pkaId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE project_key_sub_area SET PM_HDR_ID=?, PSK_ID=?,PKA_ID=? WHERE PKSA_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, pmHdrId, pskId, pkaId, PKSA_ID);
		} catch (Exception ex) {
			logger.error("updateDesignSubKey Error " + ex);
		}
		return updateStatus;
	}

	@Override
	public int deleteDesignSunkey(String DskId, String tenantId) {
		int deleteDesignSunkey = 0;
		try {
			String deleteDesignSunkeyStr = "DELETE FROM `project_key_sub_area` WHERE `PKSA_ID`=? and `TENANT_ID` = ? ";
			deleteDesignSunkey = this.jdbcTemplate.update(deleteDesignSunkeyStr, DskId, tenantId);

		} catch (Exception ex) {
			logger.error("insertDesignSubKey Error " + ex);
		}
		return deleteDesignSunkey;
	}

	@Override
	public List<KeyArea_ID> getProjectSubExtnByProjSubId(String dskId) {
		List<KeyArea_ID> rl = null;
		try {
			String getQ = "select * from project_key_sub_area_extn where PKSA_ID='" + dskId + "'";
			rl = this.jdbcTemplate.query(getQ, new DesignSubAreaIDRowMapper());
		} catch (Exception ex) {
			logger.error("Error with method getProjectSubExtnByProjSubId " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<ProjectSubAreaExtnEntity> getProjectExtnByProjSubId(String pkaId) {
		List<ProjectSubAreaExtnEntity> rl = null;
		try {
			String getQ = "select * from project_key_area_extn where PKA_ID='" + pkaId + "'";
			rl = this.jdbcTemplate.query(getQ, new ProjectSubAreaExtnRowMapper());
		} catch (Exception ex) {
			logger.error("Error with method getProjectExtnByProjSubId " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<ProjectKeyAreaMstEntity> getKeySubAreaByPKId(GetKeySubAreaByPKIdRequest getKeySubAreaByPKIdReq) {
		List<ProjectKeyAreaMstEntity> rl = null;
		RowMapper<ProjectKeyAreaMstEntity> rowmapper = new ProjectKeyAreaMstRowMapper();

		try {

			String getQ = "SELECT \r\n" + "    pka.*,ka.PKSA_ID\r\n" + "FROM\r\n"
					+ "    project_key_sub_area_mst pka\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area ka ON pka.PSK_ID = ka.PSK_ID\r\n" + "WHERE\r\n"
					+ "    ka.PM_HDR_ID = ? and pka.TENANT_ID = ? and ka.PKA_ID = ?  ";

			rl = this.jdbcTemplate.query(getQ, rowmapper, getKeySubAreaByPKIdReq.getPmHdrId(),
					getKeySubAreaByPKIdReq.getTenantId(), getKeySubAreaByPKIdReq.getPkaId());

		} catch (Exception ex) {
			logger.error("Error with method getKeySubAreaByPKId " + ex.getMessage());
		}
		return rl;
	}
	@Override
	public List<ProjectKeyAreaMstEntity> getKeySubAreaByIsdefault() {
		List<ProjectKeyAreaMstEntity> rl = null;
		RowMapper<ProjectKeyAreaMstEntity> rowmapper = new ProjectKeyAreaMstRowMapper();

		try {

			String getQ = "select * from .project_key_sub_area_mst where IS_ACTIVE = 1 and IS_DEFAULT = 1 ";

			rl = this.jdbcTemplate.query(getQ, rowmapper);

		} catch (Exception ex) {
			logger.error("Error with method getKeySubAreaByIsdefault " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<GetKeySubAreaDtlEntity> getKeySubAreaDtl(String pmHdrId, String tenantId) {
		List<GetKeySubAreaDtlEntity> rl = null;
		RowMapper<GetKeySubAreaDtlEntity> rowmapper = new GetKeySubAreaDtlRowMapper();

		try {

			String getQ = "SELECT \r\n" 
			        + "    pksa.*,pksam.PSK_DESC,pkam.PK_DESC, pksam.CODE as PSK_CODE, pkam.CODE as PK_CODE\r\n" 
			        + "FROM\r\n"
					+ "    project_key_sub_area pksa\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area_mst pksam ON pksam.PSK_ID = pksa.PSK_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_area pka ON pka.PKA_ID = pksa.PKA_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID where pksa.PM_HDR_ID=? and pksa.TENANT_ID = ? ";

			rl = this.jdbcTemplate.query(getQ, rowmapper, pmHdrId, tenantId);

		} catch (Exception ex) {
			logger.error("Error with method getKeySubAreaDtl " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public int indentpksaIdCheck(String pksaid) {
		int indentpksaIdCheck = 0;
		try {
			String indentpksaIdCheckStr = "select count(*) as COUNT from indent_hdr where PKSA_ID = ?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(indentpksaIdCheckStr,pksaid);
			indentpksaIdCheck = Integer.parseInt(result.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("indentpksaIdCheck Error " + ex);
		}
		return indentpksaIdCheck;
	}

	@Override
	public List<ProductBasedInventoryDtlEntity> getProductBasedInventoryDtl(String productId, String tenantId) {
		List<ProductBasedInventoryDtlEntity> rl = null;
		RowMapper<ProductBasedInventoryDtlEntity> rowmapper = new ProductBasedInventoryDtlRowMapper();

		try {

			String getQ = "SELECT \r\n" + "    ipdl.INVENTORY_LOCATION_CODE,\r\n"
					+ "    ipdl.PRODUCT_QUANTITY_ON_HAND,\r\n" + "    ilm.INVENTORY_LOCATION_DESCRIPTION,\r\n"
					+ "    ilm.INVENTORY_LOCATION_TYPE,\r\n" + "    ilm.INVENTORY_LOCATION_PARENT_CODE\r\n" + "FROM\r\n"
					+ "    inventory_product_dtl AS ipdl\r\n" + "        INNER JOIN\r\n"
					+ "    inventory_location_mst AS ilm ON ipdl.INVENTORY_LOCATION_CODE = ilm.INVENTORY_LOCATION_CODE\r\n"
					+ "WHERE\r\n" + "    ipdl.PRODUCT_ID = ?\r\n" + "        AND ipdl.TENANT_ID = ?";

			rl = this.jdbcTemplate.query(getQ, rowmapper, productId, tenantId);

		} catch (Exception ex) {
			logger.error("Error with method getProductBasedInventoryDtl " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<ProductBasedPoDtlEntity> getProductBasedPoDtl(String productId, String pmHdrId,
			String tenantId) {
		List<ProductBasedPoDtlEntity> rl = null;
		RowMapper<ProductBasedPoDtlEntity> rowmapper = new ProductBasedPoDtlRowMapper();

		try {

			String getQ = "SELECT \r\n" +
					"    phdr.PO_CODE, pdtl.QTY, CASE WHEN mdtl.RECEIVED_QTY IS NOT NULL THEN mdtl.RECEIVED_QTY ELSE 0 END as RECEIVED_QTY, CASE WHEN mdtl.INSPECTED_QTY IS NOT NULL THEN mdtl.INSPECTED_QTY ELSE 0 END as INWARD_QTY, \r\n" +
					"     pdtl.UNITE_RATE, pdtl.TOTAL_VALUE\r\n" +
					"FROM\r\n" +
					"    indent_dtl dtl \r\n" +
					"       INNER JOIN \r\n" +
					"	 indent_hdr ihdr ON ihdr.INDENT_ID = dtl.INDENT_ID  \r\n" +
					"		INNER JOIN \r\n" +
					"	 product_mst mst ON mst.PRODUCT_CODE = dtl.PRODUCT_CODE AND ihdr.PROJECT_ID = mst.PM_HDR_ID\r\n" +
					"		 INNER JOIN" +
					"    po_dtl pdtl ON pdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    po_hdr phdr ON phdr.PO_ID = pdtl.PO_ID\r\n" +
					"        LEFT JOIN\r\n" +
//					"    indent_hdr ihdr ON ihdr.INDENT_ID = dtl.INDENT_ID\r\n" +
//				    "        INNER JOIN\r\n" +
					"	material_inward_dtl mdtl ON mdtl.PO_DTL_ID = pdtl.PO_DTL_ID and mst.PRODUCT_ID = mdtl.PRODUCT_ID " +
					"        LEFT JOIN\r\n" +
					"    material_inward_hdr mihdr ON mihdr.MI_ID = mdtl.MI_ID\r\n" +
					"WHERE\r\n" +
					"     mst.PRODUCT_ID = ? AND phdr.IS_LATEST=1\r\n" +
					"        AND ihdr.PROJECT_ID = ?\r\n" +
					"        AND ihdr.TENANT_ID = ?\r\n" +
					"        AND (mihdr.MI_ID IS NULL OR mihdr.IS_LATEST = 1)";

			rl = this.jdbcTemplate.query(getQ, rowmapper, productId, pmHdrId, tenantId);

		} catch (Exception ex) {
			logger.error("Error with method getProductBasedPoDtl " + ex.getMessage());
		}
		return rl;
	}
	
	@Override
	public List<ProductMstEntity> getAllProductsByPmHdrId(String pmHdrId, String tenantId) {
		List<ProductMstEntity> rl = null;
		String getQ = "";
		String productCategory="SE";
		try {
 
			getQ = "SELECT \r\n" + 
					"    @a:=@a + 1 SERIAL_NUMBER,\r\n" + 
					"    idh.INDENT_CODE,\r\n" + 
					"    sbc.SBC_DESC,\r\n" + 
					"    pksam.PSK_DESC,\r\n" + 
					"    pkam.PK_DESC,\r\n" + 
					"    pm.*,\r\n" + 
					"    um.UOM_LONG_DESCRIPTION, \r\n" + 
					"    um.UOM_SHORT_DESCRIPTION, idt.MATERIAL as UPDATED_MATERIAL, idt.DESCRIPTION as UPDATED_DESCRIPTION,\r\n" + 
					"    proj.PROJECT_CODE, idt.WEIGHT AS UPDATED_WEIGHT, idt.QTY AS UPDATED_QTY,\r\n" + 
					"    idt.INDENT_DTL_ID, idt.REMARKS, idt.MAKE as UPDATED_MAKE, idt.SPECIFICATION as UPDATED_SPECIFICATION,\r\n" +
//					"COALESCE((select DM_ID from document_management where REFERENCE_ID= idt.INDENT_DTL_ID\r\n" + 
//					"					 and UPLOAD_DOC_TYPE='FC015' and TENANT_ID=? order by VERSION desc limit 1),0) as DM_ID,\r\n" +
					"    fm.FILE_NAME_EXTN,\r\n" +
					"    CASE WHEN fm.FILE_NAME_EXTN = 'pdf' THEN 1 ELSE 0 END AS IS_PDF,\r\n" +
						"(SELECT  \r\n" +
						"					    ph.PO_CODE \r\n" +
						"					FROM \r\n" +
						"					    po_dtl pd \r\n" +
						"					        INNER JOIN \r\n" +
						"					    po_hdr ph ON ph.po_id = pd.po_id \r\n" +
						"					WHERE \r\n" +
						"					    pd.INDENT_DTL_ID =   idt.INDENT_DTL_ID\r\n" +
						"					        AND ph.TENANT_ID = ? ORDER BY ph.po_id DESC limit 1) as po_code,\r\n" +
						"						(SELECT  \r\n" +
						"					    ph.VENDOR_NAME \r\n" +
						"					FROM \r\n" +
						"					    po_dtl pd \r\n" +
						"					        INNER JOIN \r\n" +
						"					    po_hdr ph ON ph.po_id = pd.po_id \r\n" +
						"					WHERE \r\n" +
						"					    pd.INDENT_DTL_ID =   idt.INDENT_DTL_ID\r\n" +
						"					        AND ph.TENANT_ID = ? ORDER BY ph.po_id DESC limit 1) as VENDOR_NAME,\r\n" +
						"						(SELECT  \r\n" + 
						"					    pd.UNITE_RATE \r\n" + 
						"					FROM \r\n" + 
						"					    po_dtl pd \r\n" + 
						"					        INNER JOIN \r\n" + 
						"					    po_hdr ph ON ph.po_id = pd.po_id \r\n" + 
						"					WHERE \r\n" + 
						"					    pd.INDENT_DTL_ID =   idt.INDENT_DTL_ID\r\n" + 
						"					        AND ph.TENANT_ID = ? ORDER BY ph.po_id DESC limit 1) as UNITE_RATE,\r\n" + 
						"						(SELECT  \r\n" + 
						"					    ph.TOTAL_VALUE \r\n" + 
						"					FROM \r\n" + 
						"					    po_dtl pd \r\n" + 
						"					        INNER JOIN \r\n" + 
						"					    po_hdr ph ON ph.po_id = pd.po_id \r\n" + 
						"					WHERE \r\n" + 
						"					    pd.INDENT_DTL_ID =   idt.INDENT_DTL_ID\r\n" + 
						"					        AND ph.TENANT_ID = ? ORDER BY ph.po_id DESC limit 1) as TOTAL_VALUE "	+
					"FROM\r\n" + 
					"    (SELECT @a:=0) AS a,\r\n" + 
//					"    product_mst AS pm\r\n" + 
//					"        INNER JOIN\r\n" + 
					"    indent_dtl idt \r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr idh ON idh.INDENT_ID = idt.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"     product_mst pm ON pm.PRODUCT_CODE = idt.PRODUCT_CODE AND idh.PROJECT_ID = pm.PM_HDR_ID" +
					"        INNER JOIN"  +
					"    uom_mst AS um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area pka ON pka.PKA_ID = idh.PKA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_mst pkam ON pkam.PK_ID = pka.PK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area pksa ON pksa.PKSA_ID = idh.PKSA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area_mst pksam ON pksa.PSK_ID = pksam.PSK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    sales_budget_category sbc ON sbc.SBC_CODE = idh.SBC_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr proj ON pm.PM_HDR_ID = proj.PM_HDR_ID and idh.PROJECT_ID=proj.PM_HDR_ID\r\n" + 
					"        LEFT JOIN file_manager fm ON fm.REFERNCE_ID = idt.INDENT_DTL_ID\r\n" +
					"WHERE\r\n" + 
					"    pm.PM_HDR_ID LIKE ? \r\n" + 
					"        AND pm.TENANT_ID = ? \r\n" +
					"        AND idh.SEQUENCE_N0 >= 4 AND idh.SEQUENCE_N0 !=5\r\n" + 
					"        AND pm.PRODUCT_CATEGORY != '"+productCategory+"'\r\n" + 
					"  group by idt.INDENT_DTL_ID " +
					"ORDER BY idh.INDENT_CODE, pm.PKA_ID , pm.PSKA_ID";
 
// 			rl = this.jdbcTemplate.query(getQ, new ProductMstRowMapper(),tenantId,pmHdrId,tenantId);
			rl = this.jdbcTemplate.query(getQ, new ProductMstRowMapper(),tenantId,tenantId,tenantId,tenantId,pmHdrId,tenantId);
 
		} catch (Exception ex) {
			logger.error("Error with method getAllProductsByPmHdrId " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<GetTasKTemplateHdrEntity> getTasKTemplateHdr(String ttCode, String tcCode, String tenantId) {
		List<GetTasKTemplateHdrEntity> rl = null;
		try {
			String getQ = "SELECT \n"
					+ "    hdr.*, ttm.TT_DESC, tcm.TC_DESC, d.DEPARTMENT_NAME,em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME \n"
					+ "FROM\n"
					+ "    task_template_hdr hdr\n"
					+ "        LEFT JOIN\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    department d ON d.DEPARTMENT_CODE = hdr.TT_DEPARTMENT_CODE LEFT JOIN employee_mst em on em.EMPLOYEE_ID = hdr.TT_CREATED_BY \n"
					+ "WHERE\n"
					+ "    hdr.TASK_TYPE_CODE = ? \n"
					+ "        AND hdr.TASK_CATEGORY_CODE = ? \n"
					+ "        AND hdr.TENANT_ID = ? ";
			rl = this.jdbcTemplate.query(getQ, new GetTasKTemplateHdrRowMapper(),ttCode,tcCode,tenantId);
		} catch (Exception ex) {
			logger.error("Error with method getTasKTemplateHdr " + ex.getMessage());
		}
		return rl;
	}

	@Override
	public List<getPoDetailByIndentDtlEntity> getPoDetailByIndentDtlRequest(String indentDtlId, String tenantId) {
		List<getPoDetailByIndentDtlEntity > rl = null;
		try {
			String getQ = "SELECT \n\r"
		             + "    hdr.PO_CODE, hdr.DATE, hdr.VENDOR_NAME, hdr.TENANT_ID, \n\r"
		             + "    dtl.QTY, dtl.TOTAL_VALUE, dtl.UNITE_RATE, dtl.INDENT_DTL_ID  \n\r"
		             + "FROM \n\r"
		             + "    po_hdr hdr \n\r"
		             + "INNER JOIN \n"
		             + "    po_dtl dtl ON dtl.PO_ID = hdr.PO_ID \n\r"
		             + "WHERE \n\r"
		             + "    hdr.TENANT_ID = ? \n\r"
		             + "    AND dtl.INDENT_DTL_ID = ? ORDER BY hdr.po_id";
			rl = this.jdbcTemplate.query(getQ, new getPoDetailByIndentDtlRowMapper(),tenantId,indentDtlId);
		} catch (Exception ex) {
			logger.error("Error with method getPoDetailByIndentDtlRequest " + ex.getMessage());
		}
		return rl;
	}
	
}
