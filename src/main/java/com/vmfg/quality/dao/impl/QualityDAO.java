package com.vmfg.quality.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.quality.RowMapper.GetInspTypeRowMapper;
import com.vmfg.quality.RowMapper.GetQtyDtlRowMapper;
import com.vmfg.quality.RowMapper.GetQtyInspectionHdrRowMapper;
import com.vmfg.quality.RowMapper.RetieveQCInspectionHdrRowMapper;
import com.vmfg.quality.RowMapper.RetrieveQualitInspectionRowMapper;
import com.vmfg.quality.dao.interfaces.IQualityDAO;
import com.vmfg.quality.entity.GetInspTypeEntity;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.quality.entity.GetQtyInspectionHdrEntity;
import com.vmfg.quality.entity.RetieveQCInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.GetqtyInspecDocDtlRequest;
import com.vmfg.sales.entity.ApprovedDocEntity;
import com.vmfg.sales.rowmapper.ApprovedDocRowMapper;

@Transactional
@Repository
public class QualityDAO implements IQualityDAO{

	private static final Logger logger = LoggerFactory.getLogger(QualityDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<GetQtyDtlEntity> getQtyDtl(String qHdrId, String empId, String fromDate, String toDate,
			String tenantId,String customerName,String pmId,String projectId) {
		List<GetQtyDtlEntity> qtyHdrList = new ArrayList<GetQtyDtlEntity>();
		try {
			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "AND qhdr.INTIATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			}
			String pro = " and qhdr.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and qhdr.PM_HDR_ID = '"+projectId+"'";
			}
			if (qHdrId.isEmpty()) {
				String custName = "%" + customerName + "%";
				String assyHdrStr = "SELECT \r\n" + 
						"    qhdr.*,qdtl.Q_DTL_ID,\r\n" + 
						"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC,\r\n" + 
						"    dstc2.STG_DESC AS STAGE_DESC,\r\n" + 
						"    hdr.PROJECT_CODE,\r\n" + 
						"    hdr.PROJECT_NAME,\r\n" + 
						"    hdr.PROJECT_DESCRIPTION,hdr.CUSTOMER_NAME,hdr.ENQUIRY_ID \r\n" + 
						"FROM\r\n" + 
						"    quality_hdr qhdr LEFT JOIN quality_dtl qdtl ON qdtl.MASTER_ID = qhdr.Q_HDR_ID,\r\n" + 
						"    project_hdr hdr,\r\n" + 
						"    document_status_type_code dstc,\r\n" + 
						"    stg_master dstc2,\r\n" + 
						"    process_assigned_team pat\r\n" + 
						"WHERE\r\n" +
						"    hdr.PM_HDR_ID = qhdr.PM_HDR_ID AND hdr.TENANT_ID = qhdr.TENANT_ID AND pat.TENANT_ID = qhdr.TENANT_ID \r\n" +
						"        AND dstc.DOCUMENT_STATUS_TYPE_CODE = qhdr.TRANSACTION_STATUS\r\n" + 
						"        AND dstc2.STG_CODE = qhdr.TRANSACTION_STAGE\r\n" + datediff+
						"        AND hdr.CUSTOMER_NAME like ?\r\n" + 
						"     AND pat.MASTER_ID = qhdr.Q_HDR_ID    AND pat.PM_ID = ? \r\n" + 
						"        AND pat.ASSIGNED_EMP_ID = ? \r\n" + 
						"        AND qhdr.TENANT_ID = ?  AND pat.IS_ACTIVE = 1"+pro;
				qtyHdrList = this.jdbcTemplate.query(assyHdrStr, new GetQtyDtlRowMapper(),custName,pmId,empId,
						tenantId);
			} else {
				String assyHdrStr = "SELECT \r\n" + 
						"    qhdr.*,qdtl.Q_DTL_ID,\r\n" + 
						"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC ,\r\n" + 
						"    dstc2.STG_DESC AS STAGE_DESC,\r\n" + 
						"    hdr.PROJECT_CODE,\r\n" + 
						"    hdr.PROJECT_NAME,\r\n" + 
						"    hdr.PROJECT_DESCRIPTION,hdr.CUSTOMER_NAME\r\n" + 
						"FROM\r\n" + 
						"    quality_hdr qhdr LEFT JOIN quality_dtl qdtl ON qdtl.MASTER_ID = qhdr.Q_HDR_ID,\r\n" + 
						"    project_hdr hdr,\r\n" + 
						"    document_status_type_code dstc,\r\n" + 
						"    stg_master dstc2,\r\n" + 
						"    process_assigned_team pat\r\n" + 
						"WHERE\r\n" +
						"    hdr.PM_HDR_ID = qhdr.PM_HDR_ID AND hdr.TENANT_ID = qhdr.TENANT_ID\r\n" +
						"        AND dstc.DOCUMENT_STATUS_TYPE_CODE = qhdr.TRANSACTION_STATUS\r\n" +
						"        AND dstc2.STG_CODE = qhdr.TRANSACTION_STAGE\r\n" +
						"        AND qhdr.Q_HDR_ID = ? \r\n" +
						"       AND pat.MASTER_ID = qhdr.Q_HDR_ID  AND pat.PM_ID =  ? \r\n" + 
						"        AND pat.ASSIGNED_EMP_ID = ? \r\n" + 
						"        AND qhdr.TENANT_ID = ?";
				qtyHdrList = this.jdbcTemplate.query(assyHdrStr, new GetQtyDtlRowMapper(), qHdrId,pmId,empId, tenantId);
			}
		} catch (Exception ex) {
			logger.error("getQtyDtl Error" + ex);
		}
		return qtyHdrList;
	}

	@Override
	public String getCountofinsp(String pmHdrId,String tenantId) {
		String  response="";
		try {
			String insQry = "SELECT \r\n" + 
					"    COUNT(*) as COUNT\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request qir\r\n" + 
					"LEFT JOIN\r\n" + 
					"    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID AND qi.IS_LATEST=1 "
					+ "INNER JOIN  po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID   "
					+ "INNER JOIN  po_hdr phdr ON phdr.PO_ID = pdl.PO_ID and phdr.SEQUENCE_NO != 3 \r\n" + 
					"WHERE\r\n" + 
					"    qir.PM_HDR_ID LIKE ? AND qir.TENANT_ID = ? AND qir.IS_LATEST=1 ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(insQry,pmHdrId,tenantId);
			response = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("getQtyDtl Error" + ex);
		}
		return response;
	}

	@Override
	public String getQtyinspCompleted(String pmHdrId, String tenantId) {
		String  response="";
		try {
			String insQry = "SELECT \n"
					+ "    COUNT(*) as COUNT\n"
					+ "FROM\n"
					+ "     quality_inspection_request qi "
					+ "LEFT join  quality_inspection_hdr hdr  on qi.QI_ID = hdr.QI_ID and hdr.IS_LATEST=1 "
					+ " and qi.IS_LATEST=1 inner join "
					+ "po_dtl pdl ON qi.PO_DTL_ID = pdl.PO_DTL_ID   "
					+ "INNER JOIN  po_hdr phdr ON phdr.PO_ID = pdl.PO_ID and phdr.SEQUENCE_NO != 3 \n"
					+ "WHERE\n"
					+ "    qi.PM_HDR_ID LIKE ? \n"
					+ "        AND hdr.IS_COMPLETED = 1 and qi.TENANT_ID=? AND qi.IS_LATEST=1";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(insQry,pmHdrId,tenantId);
			response = resultMap.get("COUNT").toString();
		} catch (Exception ex) {
			logger.error("getQtyDtl Error" + ex);
		}
		return response;
	}

	@Override
	public List<RetrieveQualitInspectionEntity> retrieveQualitInspectionReq(String projectId, String tenantId) {
		List<RetrieveQualitInspectionEntity> inspectQuality = new ArrayList<>();
		try {

			String Qry = "SELECT \r\n" + 
					"    qir.PO_CODE,\r\n" + 
					"    qir.QI_CODE,\r\n" + 
					"    qir.FINANCIAL_YEAR_MST_ID,\r\n" + 
					"    qir.TRANSACTION_NO,\r\n" + 
					"    qir.INDENT_DTL_ID,\r\n" + 
					"    qir.PM_HDR_ID,\r\n" + 
					"    qir.QI_ID,\r\n" + 
					"    qir.PO_ID,\r\n" + 
					"    qir.QTY_INSPECTED,\r\n" + 
					"    qir.QTY_TO_BE_INSPECTED,\r\n" + 
					"    qir.PO_DTL_ID,\r\n" + 
					"    qir.INSPECTION_REQUESTED_DATE,\r\n" + 
					"    idl.PRODUCT_CODE,\r\n" + 
					"    idl.DESCRIPTION,\r\n" + 
					"    qir.REQUEST_FROM,\r\n" +
					"    um.UOM_LONG_DESCRIPTION,\r\n" + 
					"    qir.INSPECTION_REQUESTED_DATE,\r\n" + 
					"    ABS(qi.OK_QTY - (qi.CA_INTERNAL + qi.CA_VENDOR)) AS OK_QTY,\r\n" + 
					"    qi.CA_INTERNAL + qi.CA_VENDOR AS CONDITIONAL_APPROVED_CNT,\r\n" + 
					"    qi.REJECTED_INTERNAL + qi.REJECTED_EXTERNAL AS NOK_CNT,\r\n" + 
					"    qi.REWORK_INTERNAL + qi.REWORK_VENDOR AS REWORK_CNT,\r\n" + 
					"    qi.INSPECTED_ON,\r\n" + 
					"    qi.QI_HDR_ID,\r\n" + 
					"    qi.NR_FLAG,\r\n" + 
					"    qi.CANCEL_FLAG,\r\n" +
					"    qi.QUALITY_RATING,\r\n" + 
					"    qir.REQUEST_FROM, emst.EMPLOYEE_FIRSTNAME as INSPECTION_REQUESTED_BY,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME AS INSPECTED_BY,qir.IS_REWORK,phdr.VENDOR_CODE AS VENDOR_CODE, phdr.VENDOR_NAME, \r\n" +
					"    IFNULL(doc.DOCUMENT_STATUS_TYPE_DESCRIPTION, 'Yet To Inspect') AS STATUS,\r\n" + 
					"	 IF(doc.DOCUMENT_STATUS_TYPE_DESCRIPTION IS NULL, 0, 1) AS ISFLAG\r\n" +
					"FROM\r\n" + 
					"    quality_inspection_request qir\r\n" +
				    "     LEFT JOIN\r\n" + 
				    "    employee_mst emst ON qir.INSPECTION_REQUESTED_BY = emst.EMPLOYEE_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl idl ON qir.INDENT_DTL_ID = idl.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID   INNER JOIN  po_hdr phdr ON phdr.PO_ID = pdl.PO_ID and phdr.SEQUENCE_NO != 3 \r\n" + 
					"        INNER JOIN\r\n" + 
					"    uom_mst um ON pdl.UOM_CODE = um.UOM_CODE\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID AND qi.IS_LATEST = 1 AND qi.TENANT_ID = qir.TENANT_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    employee_mst em ON qi.INSPECTED_BY = em.EMPLOYEE_ID\r\n" + 
					"        LEFT JOIN \r\n" + 
					"	 document_status_type_code doc ON qi.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" +
					"WHERE\r\n" + 
					"    qir.PM_HDR_ID = '"+projectId+"'\r\n" +
					"	 AND qir.IS_LATEST=1\r\n" +
					"        AND qir.TENANT_ID = '"+tenantId+"'";
			inspectQuality = this.jdbcTemplate.query(Qry, new RetrieveQualitInspectionRowMapper());


		} catch (Exception ex) {
			logger.error("retrieveQualitInspectionReq Error" + ex);
		}
		return inspectQuality;
	}

	@Override
	public List<RetieveQCInspectionHdrEntity> getQiCountsByPmHdrId(String pmHdrId, String tenantId) {
		List<RetieveQCInspectionHdrEntity> inspectQuality = new ArrayList<>();
		try {

			String Qry = "SELECT \r\n" + 
					" case when count(*)>0 then SUM(QTY_TO_BE_INSPECTED) else 0 end as QTY_TO_BE_INSPECTED ,\r\n" + 
					"case when count(*)>0 then sum(INSPECTION_QTY) else 0 end as INSPECTION_QTY , case when count(*)>0 then ABS(SUM(qi.OK_QTY)-SUM(qi.CA_INTERNAL + qi.CA_VENDOR)) else 0 end as OK_QTY ,  \r\n" + 
					" case when count(*)>0 then SUM(qi.CA_INTERNAL + qi.CA_VENDOR) else 0 end  AS CONDITIONAL_APPROVED_CNT,  \r\n" + 
					" case when count(*)>0 then SUM(qi.REJECTED_INTERNAL + qi.REJECTED_EXTERNAL)  else 0 end AS NOK_CNT,  \r\n" + 
					" case when count(*)>0 then SUM(qi.REWORK_INTERNAL + qi.REWORK_VENDOR)  else 0 end AS REWORK_CNT \r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request qir\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID\r\n" + 
					"WHERE\r\n" + 
					"    qir.PM_HDR_ID = '"+pmHdrId+"' AND qir.IS_LATEST=1 AND qir.TENANT_ID='"+tenantId+"'";
			inspectQuality = this.jdbcTemplate.query(Qry, new RetieveQCInspectionHdrRowMapper());


		} catch (Exception ex) {
			logger.error("getQiCountsByPmHdrId Error" + ex);
		}
		return inspectQuality;
	}

	@Override
	public List<RetieveQCInspectionHdrEntity> retieveQCInspectionHdr(String qiId, String tenantId) {
		List<RetieveQCInspectionHdrEntity> inspectQuality = new ArrayList<>();
		try {

			String Qry = "SELECT \r\n" + 
					"    phr.VENDOR_NAME,\r\n" + 
					"    phr.VENDOR_CODE,\r\n" + 
					"    lm.LOCATION_CITY AS LOCATION_REFERENCENAME,\r\n" + 
					"    idl.DESCRIPTION,\r\n" + 
					"    idl.PRODUCT_CODE,\r\n" + 
					"    idl.INDENT_DTL_ID,\r\n" + 
					"    qir.QTY_INSPECTED, qir.QTY_TO_BE_INSPECTED,\r\n" + 
					"    idl.TENANT_ID,\r\n" + 
					"    qir.INSPECTION_REQUESTED_DATE,\r\n" + 
					"    qi.OK_QTY,\r\n" + 
					"    qi.CA_INTERNAL + qi.CA_VENDOR AS CONDITIONAL_APPROVED_CNT,\r\n" + 
					"    qi.REJECTED_INTERNAL + qi.REJECTED_EXTERNAL AS NOK_CNT,\r\n" + 
					"    qi.REWORK_INTERNAL + qi.REWORK_VENDOR AS REWORK_CNT,\r\n" + 
					"    qi.QUALITY_REF_NO,\r\n" + 
					"    qi.DRAWING_NO,\r\n" + 
					"    qi.CONFIG_NAME,\r\n" + 
					"    qi.INSPECTED_ON\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request qir\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_hdr phr ON qir.PO_ID = phr.PO_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_mst vm ON phr.VENDOR_CODE = vm.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    location_mst lm ON vm.LOCATION_ID = lm.LOCATION_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl idl ON qir.INDENT_DTL_ID = idl.INDENT_DTL_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID and qi.IS_LATEST = 1 and qi.CANCEL_FLAG != 1\r\n" + 
					"WHERE\r\n" + 
					"    qir.QI_ID = '"+qiId+"'\r\n" + 
					"        AND qir.TENANT_ID = '"+tenantId+"'";
			inspectQuality = this.jdbcTemplate.query(Qry, new RetieveQCInspectionHdrRowMapper());


		} catch (Exception ex) {
			logger.error("retieveQCInspectionHdr Error" + ex);
		}
		return inspectQuality;
	}
	@Override
	public List<GetInspTypeEntity> getInspType(String tenantId) {
		List<GetInspTypeEntity> inspectType = new ArrayList<>();
		String isActive="1";
		try {
			String Qry = "SELECT DISTINCT\r\n"
					+ "    INSPECTION_TYPE\r\n"
					+ "FROM\r\n"
					+ "    quality_inspection_config_dtl\r\n"
					+ "WHERE\r\n"
					+ "    IS_ACTIVE = '"+isActive+"' AND TENANT_ID = '"+tenantId+"'";
			inspectType = this.jdbcTemplate.query(Qry,new GetInspTypeRowMapper());
		} catch (Exception ex) {
			logger.error("getInspType Error" + ex);
		}
		return inspectType;
	}

	@Override
	public List<String> getConfigName(String tenantId) {
		List<String> inspectType = new ArrayList<>();
		String isActive="1";
		try {
			String Qry = "SELECT DISTINCT\r\n"
					+ "    QIC_NAME\r\n"
					+ "FROM\r\n"
					+ "    quality_inspection_config_hdr\r\n"
					+ "WHERE\r\n"
					+ "    IS_ACTIVE = '"+isActive+"'\r\n"
					+ "        AND TENANT_ID = '"+tenantId+"'";
			inspectType = this.jdbcTemplate.queryForList(Qry,String.class);
		} catch (Exception ex) {
			logger.error("getConfigName Error" + ex);
		}
		return inspectType;
	}

	@Override
	public String getConfigNameByQiId(String qiId, String tenantId) {
		String configName ="";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN CONFIG_NAME\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END as CONFIG_NAME\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_hdr\r\n" + 
					"WHERE\r\n" + 
					"    QI_ID = ? AND TENANT_ID = ? and IS_LATEST =1 AND CANCEL_FLAG != 1";
            Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,qiId,tenantId);
            configName = resultMap.get("CONFIG_NAME").toString();
		} catch (Exception ex) {
			logger.error("getConfigNameByQiId error---> " + ex);
		}
		return configName;
	}

	@Override

		public List<GetQtyInspectionHdrEntity> getInspectionDtlList(String fromDate, String toDate, String vendorCode,

				String tenantId) {

			List<GetQtyInspectionHdrEntity> inspectType = new ArrayList<GetQtyInspectionHdrEntity>();

			try {

				String Qry = "SELECT \r\n" + 
						"    @a:=@a + 1 S_NO,\r\n" + 
						"    hdr.*,\r\n" + 
						"    CA_INTERNAL + CA_VENDOR AS CA_TOTAL,\r\n" + 
						"    REWORK_INTERNAL + REWORK_VENDOR AS REWORK_TOTAL,\r\n" + 
						"    REJECTED_INTERNAL + REJECTED_EXTERNAL AS REJECTED_TOTAL,\r\n" + 
						"    prj.CUSTOMER_NAME,\r\n" + 
						"    prj.PROJECT_CODE,\r\n" + 
						"    prj.PROJECT_DESCRIPTION,\r\n" + 
						"    qi.PO_CODE,\r\n" + 
						"    indDtl.PRODUCT_CODE,\r\n" + 
						"    indDtl.DESCRIPTION AS DESCRIPTION,\r\n" + 
						"    uom.UOM_LONG_DESCRIPTION,\r\n" + 
						"    vm.VENDOR_NAME,\r\n" + 
						"    hdr.INWARD_RATING,\r\n" + 
						"    hdr.SUPPLIER_RATING,\r\n" + 
						"    hdr.LAST_UPDATED_BY,\r\n" + 
						"    emp.EMPLOYEE_FIRSTNAME\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_hdr prj ON hdr.PM_HDR_ID = prj.PM_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_request qi ON qi.QI_ID = hdr.QI_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl podtl ON podtl.PO_DTL_ID = qi.PO_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl indDtl ON indDtl.INDENT_DTL_ID = podtl.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    uom_mst uom ON uom.UOM_CODE = podtl.UOM_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    employee_mst emp ON emp.EMPLOYEE_ID = hdr.LAST_UPDATED_BY\r\n" + 
						"        INNER JOIN\r\n" + 
						"    vendor_mst vm ON vm.VENDOR_CODE = hdr.VENDOR_CODE,\r\n" + 
						"    (SELECT @a:=0) AS a\r\n" + 
						" WHERE \r\n"
						+ " hdr.INSPECTED_ON between ? and ? and hdr.VENDOR_CODE like ? and hdr.TENANT_ID =? and hdr.NR_FLAG = 0 and CANCEL_FLAG = 0 ";

				inspectType = this.jdbcTemplate.query(Qry,new GetQtyInspectionHdrRowMapper(),fromDate,toDate,"%"+vendorCode+"%",tenantId);

			} catch (Exception ex) {

				logger.error("getInspectionDtlList Error" + ex);

			}

			return inspectType;

		}

	@Override
	public String checkInsCount(String qiId) {
		String status ="";
		try {
			String qry = "SELECT \r\n" + 
					"    case when count(*)>0 then doc.DOCUMENT_STATUS_TYPE_DESCRIPTION else 'NA' end as STATUS\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_hdr qi\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code doc ON qi.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"WHERE\r\n" + 
					"    QI_ID = ?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,qiId);
			status = resultMap.get("STATUS").toString();
		} catch (Exception ex) {
			logger.error("getConfigNameByQiId error---> " + ex);
		}
		return status;
	}

	@Override
	public List<ApprovedDocEntity> getqtyInspecDocDtl(GetqtyInspecDocDtlRequest getqtyInspecDocDtlReq) {
		List<ApprovedDocEntity> list = new ArrayList<ApprovedDocEntity>();
		String getDocList = "";
		try {
				getDocList = "SELECT \n"
						+ "    fc.DOCUMENT_TYPE_CODE,\n"
						+ "    mst.DOCUMENT_TYPE_DESCRIPTION,\n"
						+ "    dm.DM_ID,\n"
						+ "    dm.UPLOAD_DOC_TYPE,\n"
						+ "    fc.DESCRIPTION AS UPLOAD_DOCUMENT,\n"
						+ "    dm.VERSION,\n"
						+ "    emp.EMPLOYEE_FIRSTNAME,\n"
						+ "    dm.DOCUMENT_NAME,\n"
						+ "    dm.REMARKS,\n"
						+ "    fim.FILE_CREATED_DATE,\n"
						+ "    dm.REFERENCE_ID,\n"
						+ "    dm.STAGE_CODE,fim.FILE_ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME,"
						+ "( CASE\r\n" + 
						"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
						"        ELSE 0\r\n" + 
						"    END)AS IS_PDF  \n"
						+ "FROM\n"
						+ "    document_management dm\n"
						+ "        LEFT JOIN\n"
						+ "    file_upload_config fc ON dm.UPLOAD_DOC_TYPE = fc.FU_CODE\n"
						+ "        LEFT JOIN\n"
						+ "    document_type_mst mst ON fc.DOCUMENT_TYPE_CODE = mst.DOCUMENT_TYPE_CODE\n"
						+ "        LEFT JOIN\n"
						+ "    file_manager fim ON dm.DM_ID = fim.REFERNCE_ID\n"
						+ "        LEFT JOIN\n"
						+ "    employee_mst emp ON fim.FILE_CREATED_BY = emp.EMPLOYEE_ID where dm.ENQUIRY_ID = ? and dm.PROJECT_ID =?  and dm.UPLOAD_DOC_TYPE like ? and dm.DOC_TYPE_CODE = ? and dm.REFERENCE_ID = ? and dm.TENANT_ID = ? and dm.LATEST_VERSION = 1 and dm.APPROVED =1";

		

			list = this.jdbcTemplate.query(getDocList, new ApprovedDocRowMapper(), getqtyInspecDocDtlReq.getEnqId(), getqtyInspecDocDtlReq.getPmHdrId(),"%"+getqtyInspecDocDtlReq.getUploadDocType()+"%",getqtyInspecDocDtlReq.getDocTypeCode(),getqtyInspecDocDtlReq.getRefId(),getqtyInspecDocDtlReq.getTenantId());

		} catch (Exception ex) {
			logger.error("getApprovedDocDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int checkScmEmp(String empId, String tenantId, String dept) {
		// TODO Auto-generated method stub
		int isScmEmp=0;
		try {
			String empDepQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN COUNT(*)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END SCM_DEPT_EMP\r\n" + 
					"FROM\r\n" + 
					"    employee_mst em\r\n" + 
					"WHERE\r\n" + 
					"    em.EMPLOYEE_ID = ?\r\n" + 
					"        AND em.DEPARTMENT_CODE = ? \r\n" + 
					"        AND TENANT_ID = ?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(empDepQry,empId,dept,tenantId);
			isScmEmp = ((Long) resultMap.get("SCM_DEPT_EMP")).intValue();
		}catch(Exception ex) {
			logger.error("checkScmEmp Error" + ex);
		}
		return isScmEmp;
	}
}
