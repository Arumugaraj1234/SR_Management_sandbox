package com.vmfg.scm.dao.impl;

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

import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.design.rowmapper.IndentHdrDtlsRowMapper;
import com.vmfg.scm.dao.interfaces.IIndentManagementDAO;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentHdrDropDownEntity;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.entity.ProjectHdrDtlEntity;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.entity.ScmHdrEntity;
import com.vmfg.scm.request.IndentGrpRetRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;
import com.vmfg.scm.rowmapper.IndentGroupHdrAndDtlRowMapper;
import com.vmfg.scm.rowmapper.IndentHdrDropDownRowMapper;
import com.vmfg.scm.rowmapper.ProjectDtlsRowMapper;
import com.vmfg.scm.rowmapper.ScmHdrRowMapper;

@Transactional
@Repository
public class IndentManagementDAO implements IIndentManagementDAO {
	private static final Logger logger = LoggerFactory.getLogger(IndentManagementDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ProjectDtlsEntity> getIndentProjectDtlsByDate(String fromDate, String toDate, String tenantId) {
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
			if(!fromDate.equalsIgnoreCase("")&& !toDate.equalsIgnoreCase("")) {
			String qry = "SELECT \r\n"
					+ "    distinct(hdr.PROJECT_ID),ph.PROJECT_CODE,ph.CUSTOMER_NAME, ph.PROJECT_NAME\r\n" + "FROM\r\n"
					+ "    project_hdr ph\r\n" + "        INNER JOIN\r\n"
					+ "    indent_hdr hdr ON ph.PM_HDR_ID = hdr.PROJECT_ID\r\n" + "WHERE\r\n"
//					+ "    hdr.TENANT_ID = ?\r\n" + "        #AND date(hdr.CREATED_DATE) BETWEEN ? AND ?\r\n"
					+"      hdr.TENANT_ID = ?\r\n "
					+ "        AND hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n"
					+ "ORDER BY ph.PROJECT_CODE ASC;";
			list = this.jdbcTemplate.query(qry, new ProjectDtlsRowMapper(), tenantId);
			}else {
				String qry = "SELECT DISTINCT\n"
						+ "    (ph.PM_HDR_ID) AS PROJECT_ID,\n"
						+ "    ph.PROJECT_CODE,\n"
						+ "    ph.CUSTOMER_NAME,\n"
						+ "    ph.PROJECT_NAME\n"
						+ "FROM\n"
						+ "    project_hdr ph\n"
						+ "WHERE\n"
						+ "    ph.TENANT_ID = ? \n"
						+ "ORDER BY ph.PROJECT_CODE ASC";
				list = this.jdbcTemplate.query(qry, new ProjectDtlsRowMapper(), tenantId);
			}
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByDate Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentHdrDropDownEntity> indentHdrDropDownByProjectCode(String empId, String pmId, String projectId,
			String tenantId) {
		// By ProjectId
		String qry = "SELECT DISTINCT\r\n" + "    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n"
				+ "FROM\r\n" + "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
				+ "    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" + "WHERE\r\n" + "    ph.PM_HDR_ID = ? \r\n"
				+ "        AND hdr.TENANT_ID = ?\r\n"
				+ "        AND hdr.SEQUENCE_STATUS IN ('DS020')";
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();
		try {
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("indentHdrDropDownByProjectCode Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<IndentHdrDropDownEntity> getOnlyScmAcceptedIndents(String empId, String pmId, String projectId,
			String tenantId) {
		// By ProjectId
		String qry = "SELECT DISTINCT\r\n" + "    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n"
				+ "FROM\r\n" + "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
				+ "    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" + "WHERE\r\n" + "    ph.PM_HDR_ID = ? \r\n"
				+ "        AND hdr.TENANT_ID = ?\r\n" + "        AND hdr.SEQUENCE_STATUS IN ('DS070')";
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();
		try {
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("getOnlyScmAcceptedIndents Method Exception --->" + ex);

		}
		return list;
	}
	
	@Override
	public List<IndentHdrDropDownEntity> getOnlyScmVerifiedAndClosedIndents(String empId, String pmId, String projectId,
			String tenantId) {
		// By ProjectId
		String qry = "SELECT DISTINCT\r\n" + 
				"    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n" + 
				"FROM\r\n" + 
				"    indent_hdr hdr\r\n" + 
				"        INNER JOIN\r\n" + 
				"    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" + 
				"WHERE\r\n" + 
				"    ph.PM_HDR_ID = ?\r\n" + 
				"        AND hdr.TENANT_ID = ?\r\n" + 
				"        AND hdr.SEQUENCE_STATUS IN ('DS077' , 'DS020')";
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();
		try {
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("getOnlyScmAcceptedIndents Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<IndentHdrDropDownEntity> getIndentsExceptPmVerified(String empId, String pmId, String projectId,
			String tenantId) {
		// By ProjectId
		String qry = "SELECT DISTINCT\r\n" + "    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n"
				+ "FROM\r\n" + "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
				+ "    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" + "WHERE\r\n" + "    ph.PM_HDR_ID = ? \r\n"
				+ "        AND hdr.TENANT_ID = ?\r\n"
				+ "        AND hdr.SEQUENCE_STATUS IN ('DS019', 'DS020' , 'DS070', 'DS077')";
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();
		try {
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("getIndentsExceptPmVerified Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<IndentHdrDtlsEntity> getIndentHdrDtlsByIndentId(String indentId, String tenantId) {
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();
		try {
			String qry = "SELECT \r\n" + "    INDENT_CODE,\r\n" + "    INDENT_ID,\r\n" + "    hdr.CREATED_DATE,\r\n"
					+ "    EMPLOYEE_FIRSTNAME AS CREATED_BY,\r\n" + "    proj.PROJECT_NAME,\r\n"
					+ "    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    sbc.SBC_DESC,\r\n" + "    pk.PK_DESC,\r\n"
					+ "    psk.PSK_DESC,\r\n" + "    itm.INDENT_TYPE_DESC,\r\n"
					+ "    hdr.EXPECTED_DELIVERY_DATE,hdr.TARGET_VALUE,hdr.CLOSED_DATE AS CLOSED_DATE,hdr.REVISION_NO,hdr.REVISION_DATE\r\n" + "FROM\r\n" + "    indent_hdr hdr\r\n"
					+ "        INNER JOIN\r\n" + "    employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        INNER JOIN\r\n" + "         project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + "WHERE\r\n"
					+ "    hdr.TENANT_ID = ?\r\n"
					+ "        AND hdr.INDENT_ID = ? ORDER BY hdr.EXPECTED_DELIVERY_DATE,hdr.INDENT_ID";
			list = this.jdbcTemplate.query(qry, new IndentHdrDtlsRowMapper(), tenantId, indentId);

		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByIndentId Method Exception --->" + ex);

		}
		return list;

	}




	@Override
	public List<IndentHdrDtlsEntity> getAllIndentHdrDtls(String empId, String pmId, String projectId, String tenantId,
			String byProjectId) {
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();
		try {

			String qry = "SELECT \r\n" + "    INDENT_CODE,\r\n" + "    INDENT_ID,\r\n" + "    hdr.CREATED_DATE,\r\n"
					+ "    EMPLOYEE_FIRSTNAME AS CREATED_BY,\r\n" + "    proj.PROJECT_NAME,\r\n"
					+ "    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    sbc.SBC_DESC,\r\n" + "    pk.PK_DESC,\r\n"
					+ "    pskam.PSK_DESC,\r\n" + "    itm.INDENT_TYPE_DESC,\r\n"
					+ "    hdr.EXPECTED_DELIVERY_DATE,\r\n" + "    hdr.TARGET_VALUE,hdr.CLOSED_DATE AS CLOSED_DATE,hdr.REVISION_NO,hdr.REVISION_DATE \r\n" + "FROM\r\n"
					+ "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area pska ON hdr.PKSA_ID = pska.PKSA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area_mst pskam ON pskam.PSK_ID = pska.PSK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + "WHERE\r\n"
					+ "    hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077', 'DS075', 'DS076')\r\n"
					+ "    and hdr.PROJECT_ID = ?\r\n" + "        AND hdr.TENANT_ID = ?\r\n"
					+ "ORDER BY hdr.EXPECTED_DELIVERY_DATE , hdr.INDENT_ID";
			list = this.jdbcTemplate.query(qry, new IndentHdrDtlsRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("getAllIndentHdrDtls Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentGroupHdrAndDtlEntity> getIndentGrpNewProd(IndentGrpRetRequest indentGrpReq) {
		List<IndentGroupHdrAndDtlEntity> list = null;
		try {

			String getQ = "SELECT\n" +
					"    dtl.*,\n" +
					"    dtl.QTY AS INDENT_QTY,\n" +
					"    um.UOM_LONG_DESCRIPTION AS UOM,\n" +
					"    COALESCE(SUM(gdtl.QTY), 0) AS INDENT_GRP_QTY,\n" +
					"    (dtl.QTY - COALESCE(SUM(gdtl.QTY), 0)) AS DIFFERENCE_QTY\n" +
					"FROM indent_dtl dtl\n" +
					"LEFT JOIN indent_grp_dtl gdtl\n" +
					"    ON dtl.INDENT_DTL_ID = gdtl.INDENT_DTL_ID\n" +
					"INNER JOIN uom_mst um\n" +
					"    ON dtl.UNIT = um.UOM_CODE\n" +
					"INNER JOIN indent_assign_team iat\n" +
					"    ON iat.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n" +
					"WHERE\n" +
					"    dtl.INDENT_ID = ?\n" +
					"    AND dtl.TENANT_ID = ?\n" +
					"    AND iat.EMPLOYEE_ID = ?\n" +
					"GROUP BY\n" +
					"    dtl.INDENT_DTL_ID\n" +
					"HAVING\n" +
					"    SUM(gdtl.QTY) < dtl.QTY\n" +
					"    OR SUM(gdtl.QTY) IS NULL;\n";

			RowMapper<IndentGroupHdrAndDtlEntity> dtlrm = new IndentGroupHdrAndDtlRowMapper();

			list = this.jdbcTemplate.query(getQ, dtlrm, indentGrpReq.getIndentId(), indentGrpReq.getTenantId(),
					indentGrpReq.getEmpId());
		} catch (Exception ex) {
			logger.error("getIndentGrpNewProd Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<ScmHdrBasedDtlEntity> getScmHdrBasedDtl(ScmHdrBasedDtlRequest scmHdrBasedDtl) {
		List<ScmHdrEntity> list = new ArrayList<>();
		List<ScmHdrBasedDtlEntity> scmHdrBasedDtlList = new ArrayList<>();
		String getQ = "",datediff="";
		if(!scmHdrBasedDtl.getFromDate().equalsIgnoreCase("")) {
			datediff =  " sh.SCM_INITIATED_DATE BETWEEN '"+scmHdrBasedDtl.getFromDate()+"' and '"+scmHdrBasedDtl.getToDate()+"' and ";
		}
		
		try {
			if (scmHdrBasedDtl.getProjectId() == null || scmHdrBasedDtl.getProjectId().isEmpty()) {
				getQ = "SELECT \r\n" + "    @a:=@a + 1 serial_number,\r\n" + "    sh.DUE_DATE,\r\n"
						+ "    sh.SCM_HDR_ID,\r\n" + "    sh.SCM_INITIATED_DATE,\r\n" + "    sh.PM_HDR_ID,\r\n"
						+ "    sh.TRANSACTION_STATUS,\r\n" + "    sh.TRANSACTION_STATUS_SEQ,\r\n"
						+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    phdr.ENQUIRY_ID\r\n" + "FROM\r\n"
						+ "    (SELECT @a:=0) AS a,\r\n" + "    scm_hdr AS sh\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code AS dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = sh.TRANSACTION_STATUS\r\n"
						+ "        INNER JOIN\r\n" + "    process_assigned_team pa ON pa.MASTER_ID = sh.SCM_HDR_ID\r\n"
						+ "        INNER JOIN\r\n" + "    project_hdr phdr ON phdr.PM_HDR_ID = sh.PM_HDR_ID\r\n"
						+ "WHERE\r\n" +datediff +"  sh.TENANT_ID = '"
						+ scmHdrBasedDtl.getTenantId() + "' \r\n" + "        AND pa.ASSIGNED_EMP_ID = '"
						+ scmHdrBasedDtl.getEmpId() + "' \r\n" + "        AND pa.PM_ID = 5 AND pa.TENANT_ID = sh.TENANT_ID \r\n"
						+ "        AND pa.IS_ACTIVE = 1";

			} else {

				getQ = "SELECT \r\n" + "    @a:=@a + 1 serial_number,\r\n" + "    sh.DUE_DATE,\r\n"
						+ "    sh.SCM_HDR_ID,\r\n" + "    sh.SCM_INITIATED_DATE,\r\n" + "    sh.PM_HDR_ID,\r\n"
						+ "    sh.TRANSACTION_STATUS,\r\n" + "    sh.TRANSACTION_STATUS_SEQ,\r\n"
						+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION ,phdr.ENQUIRY_ID \r\n" + "FROM\r\n"
						+ "    (SELECT @a:=0) AS a,\r\n" + "    scm_hdr AS sh\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code AS dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = sh.TRANSACTION_STATUS\r\n"
						+ "        INNER JOIN\r\n"
						+ "    process_assigned_team pa ON pa.MASTER_ID = sh.SCM_HDR_ID inner join project_hdr phdr on phdr.PM_HDR_ID = sh.PM_HDR_ID \r\n"
						+ "WHERE\r\n" + "       sh.TENANT_ID = '"
						+ scmHdrBasedDtl.getTenantId() + "' \r\n" + "        AND sh.PM_HDR_ID = '"
						+ scmHdrBasedDtl.getProjectId() + "' \r\n" + "        AND pa.ASSIGNED_EMP_ID = '"
						+ scmHdrBasedDtl.getEmpId() + "' \r\n" + "        AND pa.PM_ID = 5\r\n"
						+ "        AND pa.IS_ACTIVE = 1";

			}
			list = this.jdbcTemplate.query(getQ, new ScmHdrRowMapper());

			for (ScmHdrEntity obj : list) {
				ScmHdrBasedDtlEntity scmHdrObj = new ScmHdrBasedDtlEntity();
				String projCountQry = "SELECT \r\n" + 
						"    COUNT(distinct dtl.INDENT_DTL_ID) AS INDENT_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl dtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr AS ih ON dtl.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"WHERE\r\n" + 
						"    ih.PROJECT_ID = ? \r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(projCountQry,obj.getPmHdrId() );
				int intentCount = Integer.parseInt(resultMap.get("INDENT_DTL_ID").toString());
				
				String intentCountQry = "SELECT \r\n" + 
						"    count(pod.PO_DTL_ID) as PO_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_hdr AS ih\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_hdr AS poh ON poh.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl pod ON poh.PO_ID = pod.PO_ID\r\n" + 
						"WHERE\r\n" + 
						"    ih.PROJECT_ID = ? AND poh.IS_APPROVED=1 AND IS_LATEST=1 and poh.SEQUENCE_STATUS !='DS100' ";

				
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(intentCountQry,obj.getPmHdrId() );
				int poCount = Integer.parseInt(resultMap1.get("PO_DTL_ID").toString());

				
				String inwardCountQry = "  SELECT \r\n" + 
						"    COUNT(mid.MI_DTL_ID) AS MI_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    scm_hdr hdr,\r\n" + 
						"    indent_hdr ihdr,\r\n" + 
						"    po_hdr po,\r\n" + 
						"    material_inward_hdr mih,\r\n" + 
						"    material_inward_dtl mid\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = ihdr.PROJECT_ID\r\n" + 
						"        AND ihdr.INDENT_ID = po.INDENT_ID\r\n" + 
						"        AND po.PO_ID = mih.PO_ID\r\n" + 
						"        And mih.MI_ID=mid.MI_ID\r\n" + 
						"        AND hdr.PM_HDR_ID = ? AND mih.IS_COMPLETED=1";

				Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(inwardCountQry,obj.getPmHdrId() );
				int inwardCount = Integer.parseInt(resultMap2.get("MI_DTL_ID").toString());

				String grnCountQry = "SELECT \r\n" + 
						"    COUNT(gdtl.GRN_DTL_ID) AS GRN_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    scm_hdr hdr,\r\n" + 
						"    indent_hdr ihdr,\r\n" + 
						"    po_hdr po,\r\n" + 
						"    material_inward_hdr mih,\r\n" + 
						"    grn_hdr ghdr,\r\n" + 
						"    grn_dtl gdtl\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = ihdr.PROJECT_ID\r\n" + 
						"        AND ihdr.INDENT_ID = po.INDENT_ID\r\n" + 
						"        AND po.PO_ID = mih.PO_ID\r\n" + 
						"        AND ghdr.MI_ID = mih.MI_ID\r\n" + 
						"        and ghdr.GRN_HDR_ID=gdtl.GRN_HDR_ID\r\n" + 
						"        AND hdr.PM_HDR_ID = ? ";
				
				Map<String, Object> resultMap3 = jdbcTemplate.queryForMap(grnCountQry,obj.getPmHdrId() );
				int grnCount = Integer.parseInt(resultMap3.get("GRN_DTL_ID").toString());
				String statusQry = "SELECT\r\n"
						+ "   prh.PROJECT_CODE,prh.TRANSACTION_NO,prh.CUSTOMER_NAME,prh.PROJECT_NAME\r\n" + "FROM \r\n"
						+ "   project_hdr AS prh \r\n" + "WHERE\r\n" + "    prh.PM_HDR_ID =?";
				
				ProjectHdrDtlEntity projectHdrDtlObj  = new ProjectHdrDtlEntity();
				
				Map<String, Object> resultMap4 = jdbcTemplate.queryForMap(statusQry,obj.getPmHdrId());
				projectHdrDtlObj.setProjectCode(resultMap4.get("PROJECT_CODE").toString());
				projectHdrDtlObj.setTransactionNo(resultMap4.get("TRANSACTION_NO").toString());
				projectHdrDtlObj.setCustomerName(resultMap4.get("CUSTOMER_NAME").toString());
				projectHdrDtlObj.setProjectName(resultMap4.get("PROJECT_NAME").toString());
				
				String intrlQry = " select IS_INTERNAL from project_hdr hdr inner join sales_enq_hdr enq\r\n" + 
						" on hdr.ENQUIRY_ID = enq.SE_ID where PM_HDR_ID=? ";
				Map<String, Object> resultMap5 = jdbcTemplate.queryForMap(intrlQry,obj.getPmHdrId());
				scmHdrObj.setIsInternal(resultMap5.get("IS_INTERNAL").toString());
				
				scmHdrObj.setPmHdrId(obj.getPmHdrId());
				scmHdrObj.setScmHdrId(obj.getScmHdrId());
				scmHdrObj.setScmInitiatedDate(obj.getScmInitiatedDate());
				scmHdrObj.setPoCount(String.valueOf(poCount));
				scmHdrObj.setIntentCount(String.valueOf(intentCount));
				scmHdrObj.setHdrStatusDesc(obj.getHdrStatusDesc());
				scmHdrObj.setProjectCode(projectHdrDtlObj.getProjectCode());
				scmHdrObj.setTransactionStatus(obj.getTransactionStatus());
				scmHdrObj.setTransactionNo(projectHdrDtlObj.getTransactionNo());
				scmHdrObj.setSNo(obj.getSNo());
				scmHdrObj.setDueDate(obj.getDueDate());
				scmHdrObj.setTransactionStatusSeq(obj.getTransactionStatusSeq());
				scmHdrObj.setCustomerName(projectHdrDtlObj.getCustomerName());
				scmHdrObj.setProjectName(projectHdrDtlObj.getProjectName());
				scmHdrObj.setInwardCount(String.valueOf(inwardCount));
				scmHdrObj.setGrnCount(String.valueOf(grnCount));
				scmHdrObj.setEnquiryId(obj.getEnquiryId());
				scmHdrBasedDtlList.add(scmHdrObj);
			}

		} catch (Exception ex) {
			logger.error("getScmHdrBasedDtl Method Exception --->" + ex);

		}
		return scmHdrBasedDtlList;

	}
	
	@Override
	public String getIndentClosedStatus(String indentId) {
		String status = "";
		try {
			String getCode = "select case when count(*)>0 then '1' else '0' end as COUNT from indent_hdr where INDENT_ID=? and SEQUENCE_STATUS='DS077'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCode,indentId);
			status = resultMap.get("COUNT").toString();

		} catch (Exception ex) {
			logger.error("getIndentClosedStatus method Error" + ex);
		}
		return status;

	}
	
	@Override
	public int indentVerCheck(String indentId, String tenantId) {
	    int value = 0;
	    try {
	        String qry = "SELECT \r\n" + 
	        		"CASE WHEN curr.SEQUENCE_NO < prev.SEQUENCE_NO THEN 1 ELSE 0 END AS IS_VALID \r\n" + 
	        		"FROM \r\n" + 
	        		"(SELECT CAST(s.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO FROM indent_status_dtl s \r\n" + 
	        		" INNER JOIN indent_hdr ih ON ih.INDENT_ID = s.REFERENCE_ID \r\n" + 
	        		" WHERE ih.INDENT_ID = ? AND ih.TENANT_ID = ?\r\n" + 
	        		" ORDER BY s.ISD_ID DESC LIMIT 1) curr, \r\n" + 
	        		"(SELECT CAST(s1.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO  FROM indent_status_dtl s1 \r\n" + 
	        		" INNER JOIN indent_hdr ih1 ON ih1.INDENT_ID = s1.REFERENCE_ID \r\n" + 
	        		"WHERE ih1.INDENT_ID = ? AND ih1.TENANT_ID = ?\r\n" + 
	        		"ORDER BY s1.ISD_ID DESC LIMIT 1 OFFSET 1) prev";

	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, indentId, tenantId, indentId, tenantId);
	        Object result = resultMap.get("IS_VALID");
	        if (result != null) {
	            value = Integer.parseInt(result.toString());
	        }
	    } catch (Exception ex) {
	        logger.error("indentVerCheck Method Exception ---> " + ex);
	    }
	    return value;
	}

	@Override
	public List<ProjectDtlsEntity> getIndentProjectDtlsByDateAndIndent(String fromDate, String toDate,
			String tenantId) {
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
			String qry = "SELECT DISTINCT\r\n" + 
					"    (ph.PM_HDR_ID) AS PROJECT_ID,\r\n" + 
					"    ph.PROJECT_CODE,\r\n" + 
					"    ph.CUSTOMER_NAME,\r\n" + 
					"    ph.PROJECT_NAME, dhr.DE_HDR_ID\r\n" + 
					"FROM\r\n" + 
					"    project_hdr ph\r\n" + 
					"        INNER JOIN\r\n" + 
					"  #  indent_hdr hdr ON ph.PM_HDR_ID = hdr.PROJECT_ID\r\n" + 
					"   #     INNER JOIN\r\n" + 
					"    sales_enq_hdr sale ON sale.SE_ID = ph.ENQUIRY_ID"
					+ " inner join \r\n" + 
					"   design_hdr dhr on dhr.PM_HDR_ID=ph.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    ph.TENANT_ID = ?\r\n" + 
					"        AND sale.IS_INTERNAL = '1'\r\n" + 
					"      #  AND DATE(ph.CREATED_DATE) BETWEEN ? AND ?\r\n" + 
					"ORDER BY ph.CREATED_DATE ASC";
			list = this.jdbcTemplate.query(qry, new ProjectDtlsRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByDateAndIndent Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<ProjectDtlsEntity> getIndentProjectDtlsByEmployee(String fromDate, String toDate, String tenantId,
			String empId) {
		// TODO Auto-generated method stub
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
		//	String Qry="";
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByEmployee Method Exception --->" + ex);
		}
		return list;
	}

	@Override
	public List<IndentHdrDropDownEntity> getIndentsBasedOnEmployee(String empId, String pmId, String projectId,
			String tenantId) {
		// TODO Auto-generated method stub
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();

		try {
			
			String qry = "SELECT DISTINCT\r\n" + 
					"    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
					"WHERE\r\n" + 
					"    ph.PM_HDR_ID = ?\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
					"        AND iat.EMPLOYEE_ID = ?\r\n" + 
					"        AND hdr.SEQUENCE_STATUS IN ('DS077' , 'DS020');";
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId,empId);
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByEmployee Method Exception --->" + ex);
		}
		return list;
	}


	@Override
	public List<ProjectDtlsEntity> getCapexIndentProjectDtlsByDate(String fromDate, String toDate, String tenantId) {
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
			if(!fromDate.equalsIgnoreCase("")&& !toDate.equalsIgnoreCase("")) {
				String qry = "SELECT DISTINCT(ph.PM_HDR_ID), \n" +
						"       ph.PROJECT_CODE, \n" +
						"       ph.CUSTOMER_NAME, \n" +
						"       ph.PROJECT_NAME\n" +
						"FROM project_hdr ph\n" +
						"INNER JOIN sales_enq_hdr seq ON seq.SE_ID = ph.ENQUIRY_ID\n" +
						"WHERE ph.TENANT_ID = ?  \n" +
						"  AND seq.IS_INTERNAL = 1\n" +
						"ORDER BY ph.PROJECT_CODE ASC;\n";
				list = this.jdbcTemplate.query(qry, new ProjectDtlsRowMapper(), tenantId);
			}else {
				String qry = "SELECT DISTINCT\n"
						+ "    (ph.PM_HDR_ID) AS PROJECT_ID,\n"
						+ "    ph.PROJECT_CODE,\n"
						+ "    ph.CUSTOMER_NAME,\n"
						+ "    ph.PROJECT_NAME\n"
						+ "FROM\n"
						+ "    project_hdr ph\n"
						+ "WHERE\n"
						+ "    ph.TENANT_ID = ? \n"
						+ "ORDER BY ph.PROJECT_CODE ASC";
				list = this.jdbcTemplate.query(qry, new ProjectDtlsRowMapper(), tenantId);
			}
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByDate Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentHdrDropDownEntity> getCapexIndentsBasedOnEmployee(String empId, String pmId, String projectId,
																		String tenantId) {
		// TODO Auto-generated method stub
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();

		try {

			String qry = "SELECT DISTINCT\r\n" +
					"    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n" +
					"FROM\r\n" +
					"    indent_hdr hdr\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" +
					"WHERE\r\n" +
					"    ph.PM_HDR_ID = ?\r\n" +
					"        AND hdr.TENANT_ID = ?\r\n" +
					"        AND iat.EMPLOYEE_ID = ?\r\n";
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId,empId);
		} catch (Exception ex) {
			logger.error("getCapexIndentProjectDtlsByEmployee Method Exception --->" + ex);
		}
		return list;
	}

	@Override
	public List<IndentHdrDropDownEntity> getIsInternalOneIndents(String empId, String pmId, String projectId,
			String tenantId) {
		// TODO Auto-generated method stub
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();

		try {

			String qry = "SELECT DISTINCT\r\n" +
					"    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n" +
					"FROM\r\n" +
					"    indent_hdr hdr\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" +
					"WHERE\r\n" +
					"    ph.PM_HDR_ID = ?\r\n" +
					"        AND hdr.TENANT_ID = ?\r\n" +
					"        AND iat.EMPLOYEE_ID = ?  AND hdr.SEQUENCE_STATUS IN ('DS070', 'DS077')\r\n";
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId,empId);
		} catch (Exception ex) {
			logger.error("getCapexIndentProjectDtlsByEmployee Method Exception --->" + ex);
		}
		return list;
	}
	@Override
	public List<IndentHdrDropDownEntity> getOnlyScmVerifiedIndents(String empId, String pmId, String projectId,
			String tenantId) {
		// TODO Auto-generated method stub
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();

		try {

			String qry = "SELECT DISTINCT\r\n" +
					"    (hdr.INDENT_CODE), hdr.INDENT_ID, EXPECTED_DELIVERY_DATE\r\n" +
					"FROM\r\n" +
					"    indent_hdr hdr\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    project_hdr ph ON hdr.PROJECT_ID = ph.PM_HDR_ID\r\n" +
					"        INNER JOIN\r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" +
					"WHERE\r\n" +
					"    ph.PM_HDR_ID = ?\r\n" +
					"        AND hdr.TENANT_ID = ?\r\n" +
					"        AND iat.EMPLOYEE_ID = ?  AND hdr.SEQUENCE_STATUS IN ('DS020')\r\n";
			list = this.jdbcTemplate.query(qry, new IndentHdrDropDownRowMapper(), projectId, tenantId,empId);
		} catch (Exception ex) {
			logger.error("getCapexIndentProjectDtlsByEmployee Method Exception --->" + ex);
		}
		return list;
	}

}
