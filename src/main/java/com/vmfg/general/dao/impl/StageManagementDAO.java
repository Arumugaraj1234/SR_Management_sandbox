package com.vmfg.general.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.interfaces.IStageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.GeneralEntity;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.general.entity.GetComponentDtls;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.general.entity.ProcessConfigEntity;
import com.vmfg.general.entity.ProjectDueDateEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.entity.StatusDtlEntity;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.rowmapper.DocTypeMstRowMapper;
import com.vmfg.general.rowmapper.DocumentStatusMstRowMapper;
import com.vmfg.general.rowmapper.GeneralEntityRowMapper;
import com.vmfg.general.rowmapper.GetComponentDtlsRowMapper;
import com.vmfg.general.rowmapper.GetstageprocessDtlRowMapper;
import com.vmfg.general.rowmapper.ProcessConfigRowMapper;
import com.vmfg.general.rowmapper.StatusDtlRowMapper;
import com.vmfg.general.rowmapper.WBSInitiationMstRowMapper;
import com.vmfg.project.dao.interfaces.IProjectDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class StageManagementDAO implements IStageManagementDAO {
	private static final Logger logger = LoggerFactory.getLogger(StageManagementDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IProjectDAO iProjectDAO;

	@Override
	public String getcurrentEnquiryStatus(String referenceId, String tenantId, String tableName) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STATUS_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STATUS_SEQ\r\n" + "FROM\r\n" + "    " + tableName + "\r\n" + "WHERE\r\n"
					+ "    MASTER_ID = '" + referenceId + "' AND TENANT_ID = '" + tenantId + "'";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);

		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public String getcurrentEnquiryStage(String referenceId, String tenantId) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STAGE_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STAGE_SEQ\r\n" + "FROM\r\n" + "    sales_enq_hdr \r\n" + "WHERE\r\n"
					+ "    SE_ID = '" + referenceId + "' AND TENANT_ID = '" + tenantId + "'";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public String getcurrentMstTblStage(String referenceId, String tenantId, String MstName, String mstColumnId) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STAGE_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STAGE_SEQ\r\n" + "FROM\r\n" + "    " + MstName + " \r\n" + "WHERE\r\n"
					+ "    " + mstColumnId + " = '" + referenceId + "' AND TENANT_ID = '" + tenantId + "'";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public String getcurrentMstTblStatus(String referenceId, String tenantId, String MstName, String mstColumnId) {
		String currentStatus = "";
		try {
			String currentStatusStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STATUS_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STATUS_SEQ\r\n" + "FROM\r\n" + "    " + MstName + " \r\n" + "WHERE\r\n"
					+ "    " + mstColumnId + " = '" + referenceId + "' AND TENANT_ID = '" + tenantId + "'";
			currentStatus = this.jdbcTemplate.queryForObject(currentStatusStr, String.class);
		} catch (Exception ex) {
			logger.error("getcurrentMstTblStatus Error" + ex);
		}
		return currentStatus;
	}

	@Override
	public String getcurrentStageSeqMstTable(String referenceId, String tableName, String slaveColumn) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN " + slaveColumn
					+ "\r\n" + "        ELSE ''\r\n" + "    END " + slaveColumn + " \r\n" + "FROM\r\n" + "    "
					+ tableName + " \r\n" + "WHERE\r\n" + "   MASTER_ID = '" + referenceId + "'";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);

		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public List<GetstageprocessDtlEntity> getprocessDtlBySeq(String proccessCode, String seq, String isVisiable,
			String tenantId) {
		List<GetstageprocessDtlEntity> list = new ArrayList<GetstageprocessDtlEntity>();
		try {
			String processDtlBySeqStr = "SELECT \r\n"
					+ "    pc.*, pm.PROCESS_NAME,sm.STG_DESC,dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    process_config pc\r\n" + "        INNER JOIN\r\n"
					+ "    process_mst pm ON pc.PM_ID = pm.PM_ID INNER JOIN\r\n"
					+ "	stg_master sm ON sm.STG_CODE = pc.STG_CODE LEFT JOIN document_status_type_code dstc on dstc.DOCUMENT_STATUS_TYPE_CODE =pc.MASTER_DOC_STATUS\r\n "
					+ "WHERE\r\n" + "    pc.PM_ID = ? AND pc.seq <= ? AND pc.ALWAYS_VISIBLE = ? and pc.TENANT_ID = ? \r\n"
					+ "        order by pc.SEQ ";
			list = this.jdbcTemplate.query(processDtlBySeqStr, new GetstageprocessDtlRowMapper(), proccessCode, seq,
					isVisiable,tenantId);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return list;
	}

	@Override
	public List<GetstageprocessDtlEntity> getVisibleAllprocessDtl(String proccessCode, String visibleAll,
			String tenantId) {
		List<GetstageprocessDtlEntity> list = new ArrayList<GetstageprocessDtlEntity>();
		try {
			String processDtlBySeqStr = "SELECT \r\n"
					+ "    pc.*, pm.PROCESS_NAME,sm.STG_DESC,dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    process_config pc\r\n" + "        INNER JOIN\r\n"
					+ "    process_mst pm ON pc.PM_ID = pm.PM_ID INNER JOIN stg_master sm ON sm.STG_CODE = pc.STG_CODE LEFT JOIN document_status_type_code dstc on dstc.DOCUMENT_STATUS_TYPE_CODE =pc.MASTER_DOC_STATUS \r\n"
					+ "WHERE\r\n" + "    pc.PM_ID = ? AND pc.ALWAYS_VISIBLE = ? \r\n" + "        AND pc.TENANT_ID = ? ";
			list = this.jdbcTemplate.query(processDtlBySeqStr, new GetstageprocessDtlRowMapper(), proccessCode,
					visibleAll, tenantId);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return list;
	}

	@Override
	public int getPreStgValCheck(String processCode, String seq,String tenantId) {
		int seqVal = 0;
		try {
			String preStgValStr = " SELECT \r\n" + "    PREVIOUS_STG_EDIT\r\n" + "FROM\r\n" + "    process_config\r\n"
					+ "WHERE\r\n" + "    PM_ID = '" + processCode + "' AND SEQ = '" + seq + "' AND TENANT_ID = '"+tenantId+"' ";
			int preStgVal = this.jdbcTemplate.queryForObject(preStgValStr, int.class);
			if (preStgVal > 0) {
				seqVal = 1;
			} else {
				seqVal = 0;
			}
		} catch (Exception ex) {
			logger.error("getPreStgValCheck Error" + ex);
		}
		return seqVal;
	}

	@Override
	public List<ProcessConfigEntity> getprocessDtlcurrentSeq(String proccessCode, String seq) {
		List<ProcessConfigEntity> list = new ArrayList<ProcessConfigEntity>();
		try {
			String processDtlBySeqStr = "SELECT \r\n"
					+ "    pc.*, pm.PROCESS_NAME,sm.STG_DESC,dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    process_config pc\r\n" + "        INNER JOIN\r\n"
					+ "    process_mst pm ON pc.PM_ID = pm.PM_ID INNER JOIN\r\n"
					+ "	stg_master sm ON sm.STG_CODE = pc.STG_CODE LEFT JOIN document_status_type_code dstc on dstc.DOCUMENT_STATUS_TYPE_CODE =pc.MASTER_DOC_STATUS  \r\n "
					+ "WHERE\r\n" + "    pc.PM_ID = ? AND pc.seq = ?  \r\n" + "     ";
			list = this.jdbcTemplate.query(processDtlBySeqStr, new ProcessConfigRowMapper(), proccessCode, seq);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return list;
	}

	@Override
	public List<DocumentStatusMstEntity> getDocDtlcurrentSeq(String referenceDoc, String seq, String tenantId) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return documentNextSeqList;
	}
	
	public List<DocumentStatusMstEntity> getAlldocLifeCyc(String referenceDoc, String type, String tenantId) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "   dsm.DOC_TYPE= ? and dsm.CANCEL_SEQ is not null and dsm.DOC_GROUP = ? and dsm.TENANT_ID = ? order by CURR_SEQUENCE +0  ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, type, tenantId);
		} catch (Exception ex) {
			logger.error("getAlldocLifeCyc Error" + ex);
		}
		return documentNextSeqList;
	}
	
	@Override
	public List<DocumentStatusMstEntity> getDocDtlcurrentSeqByDocGrp(String referenceDoc, String seq, String tenantId,String docGrp) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? and dsm.DOC_GROUP= ?";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId,docGrp);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return documentNextSeqList;
	}

	@Override
	public List<DocumentStatusMstEntity> getNextSeqbatchDtl(String referenceDoc, String seq, String tenantId) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.SEQ_BATCH = ? and dsm.TENANT_ID = ? ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId);
		} catch (Exception ex) {
			logger.error("getNextSeqbatchDtl Error" + ex);
		}
		return documentNextSeqList;
	}

	@Override
	public List<DocumentStatusMstEntity> getNextSeqbatchDtlByDesig(String referenceDoc, String seq, String tenantId,
			String Desig) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String apprdesi = "%" + Desig + "%";
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n"
					+ "     dsm.DOC_TYPE = ? and dsm.SEQ_BATCH = ? and dsm.TENANT_ID = ? and APPR_DESI like ? ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId, apprdesi);
		} catch (Exception ex) {
			logger.error("getNextSeqbatchDtl Error" + ex);
		}
		return documentNextSeqList;
	}

	@Override
	public List<DocumentStatusMstEntity> getDocCurrentSeqDtl(String referenceDoc, String seq, String tenantId) {
		List<DocumentStatusMstEntity> documentCurrSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String currSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? ";
			documentCurrSeqList = this.jdbcTemplate.query(currSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId);
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return documentCurrSeqList;
	}

	@Override
	public String getRefTableNameByDocTyp(String docType, String tenantId) {
		String refTableName = "";
		try {
			String refTableNameStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN REFERENCE_TABLE_NAME IS NOT NULL THEN REFERENCE_TABLE_NAME\r\n"
					+ "        ELSE ''\r\n" + "    END AS REFERENCE_TABLE_NAME\r\n" + "FROM\r\n"
					+ "    document_type_mst where DOCUMENT_TYPE_CODE = '" + docType + "' ";
			refTableName = this.jdbcTemplate.queryForObject(refTableNameStr, String.class);
		} catch (Exception ex) {
			logger.error("getRefTableNameByDocTyp Error " + ex);
		}
		return refTableName;

	}

	@Override
	public String getMstTableNameByDocTyp(String docType, String tenantId) {
		String mstTableName = "";
		try {
			String mstTableNameStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN MASTER_TABLE_NAME IS NOT NULL THEN MASTER_TABLE_NAME\r\n" + "        ELSE ''\r\n"
					+ "    END AS MASTER_TABLE_NAME\r\n" + "FROM\r\n"
					+ "    document_type_mst where DOCUMENT_TYPE_CODE = '" + docType + "' AND TENANT_ID = '" + tenantId
					+ "'";
			mstTableName = this.jdbcTemplate.queryForObject(mstTableNameStr, String.class);
		} catch (Exception ex) {
			logger.error("getMstTableNameByDocTyp Error " + ex);
		}
		return mstTableName;

	}

	@Override
	public String getStatusTableNameByDocTyp(String docType, String tenantId) {
		String mstTableName = "";
		try {
			String mstTableNameStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN STATUS_TABLE_NAME IS NOT NULL THEN STATUS_TABLE_NAME\r\n" + "        ELSE ''\r\n"
					+ "    END AS STATUS_TABLE_NAME\r\n" + "FROM\r\n"
					+ "    document_type_mst where DOCUMENT_TYPE_CODE = '" + docType + "' AND TENANT_ID = '" + tenantId
					+ "'";
			mstTableName = this.jdbcTemplate.queryForObject(mstTableNameStr, String.class);
		} catch (Exception ex) {
			logger.error("getStatusTableNameByDocTyp Error " + ex);
		}
		return mstTableName;

	}

	@Override
	public String getDocstsBydocseqAtype(String referenceDoc, String seq, String tenantId) {
		String docStatusCode = "";
		try {
			String docstatusCodeStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN DOC_STATUS\r\n"
					+ "        ELSE ''\r\n" + "    END AS Doc_status\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "    CURR_SEQUENCE = '" + seq + "' \r\n"
					+ "        AND DOC_TYPE = '" + referenceDoc + "'\r\n" + "        AND TENANT_ID = '" + tenantId
					+ "' ";
			docStatusCode = this.jdbcTemplate.queryForObject(docstatusCodeStr, String.class);
		} catch (Exception ex) {
			logger.error("getDocstsBydocseqAtype Error " + ex);
		}
		return docStatusCode;
	}

	@Override
	public int updateProcessDtlStsAndCode(String referenceId, String stsSeq, String stsCode, String refTableName,
			String refTableColumn) {
		int updateDtl = 0;
		try {
			String countCheckStr = "select count(*) from " + refTableName + " where " + refTableColumn + " = '"
					+ referenceId + "'";
			int countCheck = this.jdbcTemplate.queryForObject(countCheckStr, int.class);

			String updateDtlStr = "UPDATE " + refTableName
					+ " SET `TRANSACTION_STATUS`=?, `TRANSACTION_STATUS_SEQ`=? WHERE " + refTableColumn + "=? ";
			if (countCheck > 0) {
				updateDtl = this.jdbcTemplate.update(updateDtlStr, stsCode, stsSeq, referenceId);
			} else {
				String insertDtlStr = "INSERT INTO " + refTableName
						+ " (`MASTER_ID`, `TRANSACTION_STATUS`, `TRANSACTION_STATUS_SEQ`) VALUES (?,?,?) ";
				updateDtl = this.jdbcTemplate.update(insertDtlStr, referenceId, stsCode, stsSeq);
			}
		} catch (Exception ex) {
			logger.error("updateProcessDtlStsAndCode Error" + ex);
		}
		return updateDtl;
	}

	@Override
	public int updateProcessHdrStsAndCode(String referenceId, String stsSeq, String stsCode, String mstTableName,
			String mstCoulmnName,String empId) {
		int updateDtl = 0;
		try {
			String updateDtlStr = "UPDATE " + mstTableName
					+ " SET TRANSACTION_STATUS=?, TRANSACTION_STATUS_SEQ=?,LAST_UPDATED_DATETIME=?,LAST_UPDATED_BY=?  WHERE " + mstCoulmnName + "=? ";
			updateDtl = this.jdbcTemplate.update(updateDtlStr, stsCode, stsSeq,CommonMethod.getCurrentDateTime(),empId, referenceId);

		} catch (Exception ex) {
			logger.error("updateProcessHdrStsAndCode Error" + ex);
		}
		return updateDtl;
	}
	
	@Override
	public int updateMstTblIsCompleted(String mstId, String mstTableName,String mstColName) {
		int updateDtl = 0;
		try {
			String updateDtlStr = "UPDATE " + mstTableName +" SET IS_COMPLETED='1', COMPLETED_DATETIME=? WHERE "+ mstColName +" =?;";
			updateDtl = this.jdbcTemplate.update(updateDtlStr,CommonMethod.getCurrentDateTime(),mstId);

		} catch (Exception ex) {
			logger.error("updateMstTblIsCompleted Error" + ex);
		}
		return updateDtl;
	}

	@Override
	public int getMstCompletedVal(String docType, String seq, String tenantId) {
		int count = 0;
		try {
			String qry = "SELECT MASTER_COMPLETED FROM document_lifecycle_mst where DOC_TYPE='"+docType+"' AND CURR_SEQUENCE='"+seq+"' AND TENANT_ID='"+tenantId+"'";
			count = this.jdbcTemplate.queryForObject(qry, Integer.class);
		} catch (Exception ex) {
			logger.error("getMstCompletedVal Error " + ex);
		}
		return count;
	}
	
	@Override
	public int updateProcessHdrStgAndCode(String referenceId, int stgSeq, String stgCode, String mstTableName,
			String mstCoulmnName,String empId) {
		int updateDtl = 0;
		try {
			String updateDtlStr = "UPDATE " + mstTableName
					+ " SET TRANSACTION_STAGE = ? ,TRANSACTION_STAGE_SEQ = ?,LAST_UPDATED_DATETIME=?,LAST_UPDATED_BY=? WHERE " + mstCoulmnName + "=? ";
			updateDtl = this.jdbcTemplate.update(updateDtlStr, stgCode, stgSeq,CommonMethod.getCurrentDateTime(),empId, referenceId);

		} catch (Exception ex) {
			logger.error("updateProcessHdrStsAndCode Error" + ex);
		}
		return updateDtl;
	}

	@Override
	public String getNextStgDtl(String process, String seq, String tenantId) {
		String getNextStgDtl = "";
		try {
			String getNextStgDtlStr = "SELECT \r\n" + "    case when Count(*) >0 then PC_ID else  '' end AS PC_ID\r\n"
					+ "FROM\r\n" + "    process_config\r\n" + "WHERE\r\n" + "    PM_ID = '" + process
					+ "' AND ALWAYS_VISIBLE = 0 \r\n" + "        AND seq > '" + seq + "' AND TENANT_ID = '" + tenantId
					+ "' \r\n" + "ORDER BY seq + 0\r\n" + "LIMIT 1 ";
			getNextStgDtl = this.jdbcTemplate.queryForObject(getNextStgDtlStr, String.class);
		} catch (Exception ex) {
			logger.error("getDocstsBydocseqAtype Error " + ex);
		}
		return getNextStgDtl;
	}

	@Override
	public int updateProcesStatusDtl(String referenceId, String stsSeq, String stsCode, String statusTableName,
			String empId, String docType, String tenantId, String remarks) {
		int resp = 0;
		try {
			String updateStatusStr = "INSERT INTO " + statusTableName
					+ " (`REFERENCE_ID`, `REFERENCE_DOC`, `SEQUENCE_NO`, `SEQUENCE_STATUS`, `UPDATED_BY`, `UPDATED_ON`, `TENANT_ID`,`REMARKS`) VALUES (?,?,?,?,?,?,?,?) ";
			resp = this.jdbcTemplate.update(updateStatusStr, referenceId, docType, stsSeq, stsCode, empId,
					CommonMethod.getCurrentDateTime(), tenantId, remarks);
		} catch (Exception ex) {
			logger.error("UpdateProcessStatusDtl Error " + ex);
		}
		return resp;
	}

	@Override
	public List<ProcessConfigEntity> getNextprocessStaDtlBySeq(String proccessCode, String seq, String isVisiable,
			String tenantId) {
		List<ProcessConfigEntity> list = new ArrayList<ProcessConfigEntity>();
		try {
			String processDtlBySeqStr = "SELECT \r\n"
					+ "     pc.*, pm.PROCESS_NAME,sm.STG_DESC,dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "   process_config pc \r\n" + "					       INNER JOIN\r\n"
					+ "					    process_mst pm ON pc.PM_ID = pm.PM_ID INNER JOIN\r\n"
					+ "						stg_master sm ON sm.STG_CODE = pc.STG_CODE LEFT JOIN document_status_type_code dstc on dstc.DOCUMENT_STATUS_TYPE_CODE =pc.MASTER_DOC_STATUS\r\n"
					+ "WHERE\r\n" + "     pc.seq > '" + seq + "' AND  pc.ALWAYS_VISIBLE = '" + isVisiable + "'\r\n"
					+ "        AND  pc.PM_ID = '" + proccessCode + "' \r\n" + "        AND  pc.TENANT_ID = '" + tenantId
					+ "'\r\n" + "ORDER BY  pc.SEQ + 0 \r\n" + "LIMIT 1";
			list = this.jdbcTemplate.query(processDtlBySeqStr, new ProcessConfigRowMapper());
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return list;
	}

	@Override
	public String getProcessLifeCycleCurrSeq(String processCode, String status, String tenantId) {
		String getProcessLifeCycleCurrSeq = "";
		try {
			String getProcessLifeCycleCurrSeqStr = "SELECT \r\n"
					+ "  case when Count(*) >0 then CURRENT_SEQUENCE else  '' end  AS CURRENT_SEQUENCE  \r\n"
					+ "FROM\r\n" + "    process_lifecycle_mst\r\n" + "WHERE\r\n" + "    PM_ID = '" + processCode
					+ "' AND PL_STATUS = '" + status + "'\r\n" + "        AND TENANT_ID = '" + tenantId + "' ";
			getProcessLifeCycleCurrSeq = this.jdbcTemplate.queryForObject(getProcessLifeCycleCurrSeqStr, String.class);
		} catch (Exception ex) {
			logger.error("getDocstsBydocseqAtype Error " + ex);
		}
		return getProcessLifeCycleCurrSeq;
	}

	@Override
	public String getEnquiryDtlId(String referenceId) {
		String getEnquiryDtlId = "";
		try {
			String getEnquiryDtlIdStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN SE_DTL_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS SE_DTL_ID\r\n" + "FROM\r\n" + "    sales_enq_dtl\r\n"
					+ "WHERE\r\n" + "    SE_ID = '" + referenceId + "' ";
			getEnquiryDtlId = this.jdbcTemplate.queryForObject(getEnquiryDtlIdStr, String.class);
		} catch (Exception ex) {
			logger.error("getDocstsBydocseqAtype Error " + ex);
		}
		return getEnquiryDtlId;
	}

	@Override
	public List<DocumentTypeMstEntity> getDocTypeMstDtl(String docType, String pmId) {
		List<DocumentTypeMstEntity> documentTypeMstDtl = new ArrayList<DocumentTypeMstEntity>();
		try {
			String documentTypeMstDtlStr = "SELECT \r\n" + "    dtm.*,pm.PROCESS_NAME AS PM_DESC\r\n" + "FROM\r\n"
					+ "    document_type_mst dtm inner join process_mst pm on pm.PM_ID=dtm.PM_ID \r\n" + "WHERE\r\n"
					+ "    dtm.PM_ID = ? \r\n" + "        AND dtm.DOCUMENT_TYPE_CODE = ? ";
			documentTypeMstDtl = this.jdbcTemplate.query(documentTypeMstDtlStr, new DocTypeMstRowMapper(), pmId, docType);
		} catch (Exception ex) {

		}

		return documentTypeMstDtl;
	}

	@Override
	public List<DocumentTypeMstEntity> getDocTypeMstByDoc(String docType) {
		List<DocumentTypeMstEntity> documentTypeMstDtl = new ArrayList<DocumentTypeMstEntity>();
		try {
			String documentTypeMstDtlStr = "SELECT \r\n" + "    dtm.*,pm.PROCESS_NAME AS PM_DESC\r\n" + "FROM\r\n"
					+ "    document_type_mst dtm inner join process_mst pm on pm.PM_ID=dtm.PM_ID \r\n" + "WHERE\r\n"
					+ "      dtm.DOCUMENT_TYPE_CODE = ? ";
			documentTypeMstDtl = this.jdbcTemplate.query(documentTypeMstDtlStr, new DocTypeMstRowMapper(),
					docType);
		} catch (Exception ex) {

		}

		return documentTypeMstDtl;
	}

	@Override
	public List<DocumentTypeMstEntity> getDocTypeMstDtlByStage(String stgCode, String pmId, String tenantId) {
		List<DocumentTypeMstEntity> documentTypeMstDtl = new ArrayList<DocumentTypeMstEntity>();
		try {
			String documentTypeMstDtlStr = "SELECT \r\n" + "    dtm.*,pm.PROCESS_NAME AS PM_DESC\r\n" + "FROM\r\n"
					+ "    document_type_mst dtm inner join process_mst pm on pm.PM_ID=dtm.PM_ID \r\n" + "WHERE\r\n"
					+ "    dtm.PM_ID = ?  \r\n" + "        AND dtm.STG_CODE = ? ";
			documentTypeMstDtl = this.jdbcTemplate.query(documentTypeMstDtlStr, new DocTypeMstRowMapper(),
					Integer.parseInt(pmId), stgCode);
		} catch (Exception ex) {
			logger.error("getDocTypeMstDtlByStage Error" + ex);
		}

		return documentTypeMstDtl;
	}

	@Override
	public String getDistinctMstTableNameDtlId(String pmId, String tenantId) {
		String getEnquiryDtlId = "";
		try {
			String getEnquiryDtlIdStr = "SELECT DISTINCT\r\n"
					+ "    case when count(*)>0 then  (MASTER_TABLE_NAME) else '' end AS MASTER_TABLE_NAME \r\n"
					+ "FROM\r\n" + "    document_type_mst\r\n" + "WHERE\r\n" + "    PM_ID = '" + pmId
					+ "' \r\n" + "        AND MASTER_TABLE_NAME IS NOT NULL limit 1";
			getEnquiryDtlId = this.jdbcTemplate.queryForObject(getEnquiryDtlIdStr, String.class);
		} catch (Exception ex) {
			logger.error("getDistinctMstTableNameDtlId Error " + ex);
		}
		return getEnquiryDtlId;
	}

	@Override
	public String getDistinctMstTableId(String pmId, String tenantId) {
		String getEnquiryDtlId = "";
		try {
			String getEnquiryDtlIdStr = "SELECT DISTINCT\r\n"
					+ "    case when count(*)>0 then  (MASTER_TABLE_ID) else '' end AS MASTER_TABLE_ID \r\n"
					+ "FROM\r\n" + "    document_type_mst\r\n" + "WHERE\r\n" + "    PM_ID = '" + pmId
					+ "' \r\n" + "        AND MASTER_TABLE_ID IS NOT NULL limit 1";
			getEnquiryDtlId = this.jdbcTemplate.queryForObject(getEnquiryDtlIdStr, String.class);
		} catch (Exception ex) {
			logger.error("getDistinctMstTableId Error " + ex);
		}
		return getEnquiryDtlId;
	}

	public String getcurrentEnquiryStageSeqMstTable(String referenceId, String tenantId, String tableName,
			String tableColumnName) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STAGE_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STAGE_SEQ\r\n" + "FROM\r\n" + "    " + tableName + " \r\n" + "WHERE\r\n"
					+ "    " + tableColumnName + " = '" + referenceId + "' AND TENANT_ID = '" + tenantId + "'";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public String getEmpDesinationCode(String empId, String tenantId) {
		String getEmpDesinationCode = "";
		try {
			String getEmpDesinationCodeStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN DESIGNATION_CODE\r\n" + "        ELSE ''\r\n"
					+ "    END AS DESIGNATION_CODE\r\n" + "FROM\r\n" + "    employee_mst\r\n" + "WHERE\r\n"
					+ "    EMPLOYEE_ID = '" + empId + "'\r\n" + "        AND TENANT_ID = '" + tenantId + "'";
			getEmpDesinationCode = this.jdbcTemplate.queryForObject(getEmpDesinationCodeStr, String.class);
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return getEmpDesinationCode;
	}

	@Override
	public int empApproveCheck(String desiCode, String docType, String currSeq, String tenantId) {
		int empApproveCheck = 0;
		try {
			String empApproveCheckStr = "SELECT \r\n" + "    count(*)\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "    APPR_DESI LIKE '%" + desiCode + "%'\r\n"
					+ "        AND DOC_TYPE = '" + docType + "' \r\n" + " AND TENANT_ID='"+tenantId+"'   \r\n    AND CURR_SEQUENCE = '" + currSeq
					+ "' ";
			empApproveCheck = this.jdbcTemplate.queryForObject(empApproveCheckStr, int.class);
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return empApproveCheck;
	}

	@Override
	public String getSalesEnqDtlStatus(String slaveId, String refTableName) {
		String currentStage = "";
		try {
			String currentStageStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN TRANSACTION_STATUS_SEQ\r\n" + "        ELSE ''\r\n"
					+ "    END TRANSACTION_STATUS_SEQ\r\n" + "FROM\r\n" + "  " + refTableName + " \r\n" + "WHERE\r\n"
					+ "    MASTER_ID = '" + slaveId + "' ";
			currentStage = this.jdbcTemplate.queryForObject(currentStageStr, String.class);

		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public int getisEditableStatus(String currSeq, String docType, String pmId,String tenantId) {
		int isEditableStatus = 0;
		try {
			String getisEditableStr = "SELECT \r\n" + "    COUNT(*)\r\n" + "FROM\r\n" + "    document_lifecycle_mst\r\n"
					+ "WHERE\r\n" + "    DOC_TYPE = '" + docType + "' AND PROCESS_CODE = '" + pmId + "' \r\n"
					+ "        AND CURR_SEQUENCE = '" + currSeq + "' \r\n" + "        AND IS_EDITABLE = 1 AND TENANT_ID='"+tenantId +"'";
			isEditableStatus = this.jdbcTemplate.queryForObject(getisEditableStr, int.class);
		} catch (Exception ex) {
			logger.error("getisEditableStatus Error " + ex);
		}
		return isEditableStatus;
	}
	
	@Override
	public String getStageCodeForPmId(String pmId, String docDesc,String tenantId) {
		String stgCode = "";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN count(*) > 0 THEN STG_CODE\r\n" + 
					"        ELSE ''\r\n" + 
					"    END AS STG_CODE\r\n" + 
					"FROM\r\n" + 
					"    document_type_mst\r\n" + 
					"WHERE\r\n" + 
					"    PM_ID = '"+pmId+"'\r\n" + 
					"        AND DOCUMENT_TYPE_DESCRIPTION LIKE '"+docDesc+"%'\r\n" + 
					"        AND TENANT_ID = '"+tenantId+"';";
			stgCode = this.jdbcTemplate.queryForObject(qry, String.class);
		} catch (Exception ex) {
			logger.error("getStageCodeForPmId Error " + ex);
		}
		return stgCode;
	}

	@Override
	public List<GetComponentDtls> getComponentNameForPmId(String pmId, String stgCode,String tenantId) {
		List<GetComponentDtls> list=new ArrayList<GetComponentDtls>();
		try {
			String qry = "SELECT \r\n" + 
					"    pc.COMPONENT as COMPONENT, sm.STG_DESC as STG_DESC \r\n" + 
					"FROM\r\n" + 
					"    process_config pc\r\n" + 
					"        INNER JOIN\r\n" + 
					"    stg_master sm ON pc.STG_CODE = sm.STG_CODE\r\n" + 
					"WHERE\r\n" + 
					"    pc.STG_CODE = '"+stgCode+"'\r\n" + 
					"        AND pc.PM_ID = '"+pmId+"'\r\n" + 
					"        AND pc.TENANT_ID = '"+tenantId+"'";
			list = this.jdbcTemplate.query(qry,new GetComponentDtlsRowMapper());
		} catch (Exception ex) {
			logger.error("getComponentNameForPmId Error " + ex);
		}
		return list;
	}
	
	@Override
	public List<DocumentStatusMstEntity> getFirstOrLastSeqDocDtl(String referenceDoc, String Seqtype, String tenantId) {
		List<DocumentStatusMstEntity> getSeqDtl = new ArrayList<DocumentStatusMstEntity>();
		try {
			String getSeqDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.TENANT_ID = ? ORDER BY CURR_SEQUENCE + 0 "
					+ Seqtype + " limit 1 ";
			getSeqDtl = this.jdbcTemplate.query(getSeqDtlStr, new DocumentStatusMstRowMapper(), referenceDoc, tenantId);
		} catch (Exception ex) {
			logger.error("getNextSeqbatchDtl Error" + ex);
		}
		return getSeqDtl;
	}

	@Override
	public List<StatusDtlEntity> getStatusDtl(String docType, String referenceId, String tenantId, String tableName) {
		List<StatusDtlEntity> getStatusDtl = new ArrayList<StatusDtlEntity>();
		try {
			String getStatusStr = "SELECT \r\n" + "    dtl.REFERENCE_ID AS REFERENCE_ID,\r\n"
					+ "    dtl.REFERENCE_DOC AS REFERENCE_DOC,\r\n" + "    dtl.SEQUENCE_NO AS SEQUENCE_NO,\r\n"
					+ "    dtl.SEQUENCE_STATUS AS SEQUENCE_STATUS,\r\n" + "    dtl.UPDATED_BY AS UPDATED_BY,\r\n"
					+ "    dtl.UPDATED_ON AS UPDATED_ON,dtl.REMARKS AS REMARKS,\r\n"
					+ "    dtl.TENANT_ID As TENANT_ID,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
					+ "    dtm.DOCUMENT_TYPE_DESCRIPTION AS DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME As EMPLOYEE_FIRSTNAME\r\n" + "FROM\r\n" + "    " + tableName
					+ " dtl\r\n" + "        LEFT JOIN\r\n"
					+ "    document_type_mst dtm ON dtm.DOCUMENT_TYPE_CODE = dtl.REFERENCE_DOC\r\n"
					+ "        LEFT JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dtl.SEQUENCE_STATUS\r\n"
					+ "		LEFT JOIN\r\n" + "	employee_mst em on em.EMPLOYEE_ID = dtl.UPDATED_BY\r\n" + "WHERE\r\n"
					+ "    dtl.REFERENCE_ID = ? \r\n" + "        AND dtl.REFERENCE_DOC = ? \r\n"
					+ "        AND dtl.TENANT_ID = ? Order By dtl.UPDATED_ON  ";
			getStatusDtl = this.jdbcTemplate.query(getStatusStr, new StatusDtlRowMapper(), referenceId, docType,
					tenantId);
		} catch (Exception ex) {
			logger.error("getNextSeqbatchDtl Error" + ex);
		}
		return getStatusDtl;
	}

	@Override
	public List<ProjectWbsInitiationMst> getPMFromDept(InitiateProcessRequest initiateProcessReq) {
		logger.debug("getPMFromDept PM dept");
		List<ProjectWbsInitiationMst> getPMWBS = null;
		try {
			String getQ = "SELECT * FROM project_wbs_initiation_mst where DEPARTMENT_CODE = ? and TENANT_ID=?";
			getPMWBS = this.jdbcTemplate.query(getQ, new WBSInitiationMstRowMapper(), initiateProcessReq.getDeptCode(),
					initiateProcessReq.getTenantId());

		} catch (Exception ex) {
			logger.error("getPMFromDept Error" + ex);
		}
		return getPMWBS;
	}

	@Override
	public List<ProjectWbsInitiationMst> getPMFromPMID(String PMID, String tenantId) {
		logger.debug("getPMFromPMID  dept");
		List<ProjectWbsInitiationMst> getPMWBS = null;
		try {
			String getQ = "SELECT * FROM project_wbs_initiation_mst where PM_ID in ("+PMID+") and TENANT_ID=?";
			getPMWBS = this.jdbcTemplate.query(getQ, new WBSInitiationMstRowMapper(),tenantId);

		} catch (Exception ex) {
			logger.error("getPMFromPMID Error" + ex);
		}
		return getPMWBS;
	}

	@Override
	public String insertMasterInfo(String pmId, String enqId, String tenantID, String dueDate, String empID,
			String startDate,int getPmhdrIdFlag) {
		String resp = "", referenceId="";
		try {
			if(getPmhdrIdFlag==1) {
				referenceId=getPmHdrIdByEnqId(enqId); // from sales
			}else {
				referenceId=enqId;                    //from child process
			}
			
			final String refId=referenceId;
			
			List<ProcessConfigEntity> processDtl = getprocessDtlcurrentSeq(pmId, "1");

			if (pmId.equalsIgnoreCase("3")) {

				String getQ = "SELECT \r\n" + "    PROJECT_NAME AS KEY1,\r\n" + "    CUSTOMER_NAME AS KEY2,\r\n"
						+ "    PROJECT_DESCRIPTION AS KEY3,PRODUCT_DETAILS as KEY4,INDUSTRIAL_TYPE AS KEY5,SCOPE_OF_WORK AS KEY6 \r\n"
						+ "FROM\r\n" + "    sales_enq_hdr\r\n" + "WHERE\r\n" + "    SE_ID = ?";
				List<GeneralEntity> ge = this.jdbcTemplate.query(getQ, new GeneralEntityRowMapper(), enqId);

				String insertQ = "INSERT INTO project_hdr (PROJECT_NAME, ENQUIRY_ID, PROJECT_DESCRIPTION, CREATED_DATE, DUE_DATE,\r\n"
						+ " CUSTOMER_NAME, TRANSACTION_STATUS, TRANSACTION_STAGE, TRANSACTION_STATUS_SEQ, TRANSACTION_STAGE_SEQ,\r\n"
						+ " CREATED_DATETIME, LAST_UPDATED_DATETIME, TENANT_ID,PRODUCT_DETAILS,PROJECT_CODE,TRANSACTION_NO,FINANCIAL_YEAR_MST_ID,INDUSTRIAL_TYPE,SCOPE_OF_WORK,PLANNED_START_DATE,COST_FLOW_TYPE) \r\n"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, ge.get(0).getKey1());
						ps.setString(2, enqId);
						ps.setString(3, ge.get(0).getKey3());
						ps.setString(4, CommonMethod.getCurrentDate());
						ps.setString(5, dueDate);
						ps.setString(6, ge.get(0).getKey2());
						ps.setString(7, processDtl.get(0).getMasterDocStatus());
						ps.setString(8, processDtl.get(0).getStgCode());
						ps.setString(9, "1");
						ps.setString(10, "1");
						ps.setString(11, CommonMethod.getCurrentDateTime());
						ps.setString(12, CommonMethod.getCurrentDateTime());
						ps.setString(13, tenantID);
						ps.setString(14, ge.get(0).getKey4());
						ps.setString(15, null);
						ps.setInt(16, 0);
						ps.setString(17, null);
						ps.setString(18, ge.get(0).getKey5());
						ps.setString(19, ge.get(0).getKey6());
						ps.setString(20, startDate);
						ps.setString(21, "NEW");
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";

				String updateQ = "UPDATE document_management SET PROJECT_ID=? WHERE ENQUIRY_ID=? and DM_ID>0";
				this.jdbcTemplate.update(updateQ, resp, enqId);
				String updateHandOverDate="UPDATE `sales_enq_hdr` SET `PROJECT_HANDOVER_DATE`=? WHERE `SE_ID`=? ";
				this.jdbcTemplate.update(updateHandOverDate, startDate, enqId);
				
//				insert due_date in project_due_dates
				updateProjectDueDate(dueDate, empID, resp, "Initial", tenantID);
				
			} else if (pmId.equalsIgnoreCase("2")) {

				GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDate(), tenantID,
						"design_hdr", "2", jdbcTemplate, 1,0,null,1);

				String getQ = "SELECT \r\n" + "    PROJECT_NAME AS KEY1,\r\n" + "    CUSTOMER_NAME AS KEY2,\r\n"
						+ "    PROJECT_DESCRIPTION AS KEY3,PRODUCT_DETAILS as KEY4 \r\n" + "FROM\r\n"
						+ "    project_hdr\r\n" + "WHERE\r\n" + "    PM_HDR_ID = ?";
				List<GeneralEntity> ge = this.jdbcTemplate.query(getQ, new GeneralEntityRowMapper(), refId);

				String insertQ = "INSERT INTO design_hdr (PM_HDR_ID, PROJECT_NAME, CUSTOMER_NAME, PROJECT_DESCRIPTION, PRODUCT_DETAILS,\r\n"
						+ " REQUEST_DATE, REQUESTED_BY, PLANNED_START_DATE, PLANNED_END_DATE, START_INDENT_REQUEST,\r\n"
						+ " TRANSACTION_STATUS, TRANSACTION_STAGE, TRANSACTION_STATUS_SEQ, TRANSACTION_STAGE_SEQ, CREATED_DATETIME, LAST_UPDATED_DATETIME,\r\n"
						+ " TENANT_ID,DESIGN_CODE,TRANSACTION_NO,FINANCIAL_YEAR_MST_ID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, refId);
						ps.setString(2, ge.get(0).getKey1());
						ps.setString(3, ge.get(0).getKey2());
						ps.setString(4, ge.get(0).getKey3());
						ps.setString(5, ge.get(0).getKey4());
						ps.setString(6, CommonMethod.getCurrentDate());
						ps.setString(7, empID);
						ps.setString(8, startDate);
						ps.setString(9, dueDate);
						ps.setString(10, "1");
						ps.setString(11, processDtl.get(0).getMasterDocStatus());
						ps.setString(12, processDtl.get(0).getStgCode());
						ps.setString(13, "1");
						ps.setString(14, "1");
						ps.setString(15, CommonMethod.getCurrentDateTime());
						ps.setString(16, CommonMethod.getCurrentDateTime());
						ps.setString(17, tenantID);
						ps.setString(18, gen.getEnquiryCode());
						ps.setInt(19, gen.getSeq());
						ps.setString(20, gen.getFinainceId());
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";
			}  else if (pmId.equalsIgnoreCase("4")) {

				

				String getQ = "SELECT \r\n" + "    PROJECT_NAME AS KEY1,\r\n" + "    CUSTOMER_NAME AS KEY2,\r\n"
						+ "    PROJECT_DESCRIPTION AS KEY3,PRODUCT_DETAILS as KEY4 \r\n" + "FROM\r\n"
						+ "    project_hdr\r\n" + "WHERE\r\n" + "    PM_HDR_ID = ?";
				List<GeneralEntity> ge = this.jdbcTemplate.query(getQ, new GeneralEntityRowMapper(), refId);

				String insertQ = "INSERT INTO assy_hdr (PM_HDR_ID, PROJECT_NAME, CUSTOMER_NAME, PROJECT_DESCRIPTION, PRODUCT_DETAILS, REQUEST_DATE, REQUESTED_BY, \r\n" + 
						"PLANNED_START_DATE, PLANNED_END_DATE, ACTUAL_START_DATE, ACTUAL_END_DATE, START_MATERIAL_REQUEST, TRANSACTION_STATUS, \r\n" + 
						"TRANSACTION_STAGE, TRANSACTION_STATUS_SEQ, TRANSACTION_STAGE_SEQ, CREATED_DATETIME, LAST_UPDATED_DATETIME, TENANT_ID) \r\n" + 
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, refId);
						ps.setString(2, ge.get(0).getKey1());
						ps.setString(3, ge.get(0).getKey2());
						ps.setString(4, ge.get(0).getKey3());
						ps.setString(5, ge.get(0).getKey4());
						ps.setString(6, CommonMethod.getCurrentDate());
						ps.setString(7, empID);
						ps.setString(8, startDate);
						ps.setString(9, dueDate);
						ps.setString(10, null);
						ps.setString(11, null);
						ps.setString(12, "0");
						ps.setString(13,  processDtl.get(0).getMasterDocStatus());
						ps.setString(14, processDtl.get(0).getStgCode());
						ps.setString(15, "1");
						ps.setString(16, "1");
						ps.setString(17, CommonMethod.getCurrentDateTime());
						ps.setString(18, CommonMethod.getCurrentDateTime());
						ps.setString(19, tenantID);
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";
			} else if (pmId.equalsIgnoreCase("5")) {

				String insertQ = "INSERT INTO scm_hdr (SCM_INITIATED_DATE, PM_HDR_ID, DUE_DATE, TRANSACTION_STATUS, TRANSACTION_STAGE, TRANSACTION_STATUS_SEQ,\r\n"
						+ " TRANSACTION_STAGE_SEQ, CREATED_DATETIME, LAST_UPDATED_DATETIME, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, startDate);
						ps.setString(2, refId);
						ps.setString(3, dueDate);
						ps.setString(4, processDtl.get(0).getMasterDocStatus());
						ps.setString(5, processDtl.get(0).getStgCode());
						ps.setString(6, "1");
						ps.setString(7,  "1");
						ps.setString(8, CommonMethod.getCurrentDateTime());
						ps.setString(9, CommonMethod.getCurrentDateTime());
						ps.setString(10, tenantID);
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";

			}else if (pmId.equalsIgnoreCase("6")) {

				String insertQ = "INSERT INTO `quality_hdr` (`PM_HDR_ID`, `INTIATED_DATE`, `TRANSACTION_STATUS`, `TRANSACTION_STAGE`, `TRANSACTION_STATUS_SEQ`, `TRANSACTION_STAGE_SEQ`, `CREATED_DATETIME`, `LAST_UPDATED_DATETIME`, `TENANT_ID`) VALUES (?,?,?,?,?,?,?,?,?)";
				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, refId);
						ps.setString(2, CommonMethod.getCurrentDate());
						ps.setString(3, processDtl.get(0).getMasterDocStatus());
						ps.setString(4, processDtl.get(0).getStgCode());
						ps.setString(5, "1");
						ps.setString(6, "1");
						ps.setString(7,  CommonMethod.getCurrentDateTime());
						ps.setString(8, CommonMethod.getCurrentDateTime());
						ps.setString(9, tenantID);
						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";

			}else if (pmId.equalsIgnoreCase("7")) {
		//		String getQ = "SELECT \r\n" + "    PROJECT_NAME AS KEY1,\r\n" + "    CUSTOMER_NAME AS KEY2,\r\n"
		//				+ "    PROJECT_DESCRIPTION AS KEY3,PRODUCT_DETAILS as KEY4,INDUSTRIAL_TYPE AS KEY5,SCOPE_OF_WORK AS KEY6 \r\n"
		//				+ "FROM\r\n" + "    sales_enq_hdr\r\n" + "WHERE\r\n" + "    SE_ID = ?";
		//		List<GeneralEntity> ge = this.jdbcTemplate.query(getQ, new GeneralEntityRowMapper(), refId);
				
				String getQ = "SELECT \r\n" + "    PROJECT_NAME AS KEY1,\r\n" + "    CUSTOMER_NAME AS KEY2,\r\n"
						+ "    PROJECT_DESCRIPTION AS KEY3,PRODUCT_DETAILS as KEY4 \r\n" + "FROM\r\n"
						+ "    project_hdr\r\n" + "WHERE\r\n" + "    PM_HDR_ID = ?";
				List<GeneralEntity> ge = this.jdbcTemplate.query(getQ, new GeneralEntityRowMapper(), refId);

		//		String pmHdrIdStr ="select case when count(*)>0 then  PM_HDR_ID else '' end as pmHdrId from project_hdr where ENQUIRY_ID = '"+refId+"' ";
		//		String pmHdrId = this.jdbcTemplate.queryForObject(pmHdrIdStr, String.class);
				if(!refId.equalsIgnoreCase("")) {
				String insertQ = "INSERT INTO `finance_hdr` (`PM_HDR_ID`, `PROJECT_NAME`, `CUSTOMER_NAME`, `PROJECT_DESCRIPTION`, `PRODUCT_DETAILS`, `INITIATED_DATE`, `REQUESTED_BY`, `TRANSACTION_STATUS_SEQ`, `TRANSACTION_STATUS`, `TRANSACTION_STAGE_SEQ`, `TRANSACTION_STAGE`, `CREATED_DATETIME`, `LAST_UPDATED_DATETIME`,`TENANT_ID`) VALUES (?,?,?, ?,?,?,?,?, ?, ?, ?, ?, ?,?) ";
				KeyHolder holder = new GeneratedKeyHolder();

				
				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1,refId );
						ps.setString(2, ge.get(0).getKey1());
						ps.setString(3, ge.get(0).getKey2());
						ps.setString(4, ge.get(0).getKey3());
						ps.setString(5, ge.get(0).getKey4());
						ps.setString(6, startDate);
						ps.setString(7, empID);
						ps.setString(8, "1");
						ps.setString(9, processDtl.get(0).getMasterDocStatus());
						ps.setString(10, "1");
						ps.setString(11, processDtl.get(0).getStgCode());
						ps.setString(12,  CommonMethod.getCurrentDate());
						ps.setString(13, CommonMethod.getCurrentDateTime());
						ps.setString(14, tenantID);
						return ps;
					}

				}, holder);
				
				int insertRes = holder.getKey().intValue();
				resp = insertRes + "";
				}
			}
		} catch (Exception ex) {
			logger.error("getPMFromDept Error" + ex);
		}
		return resp;
	}

	@Override
	public String checkMasterInfo(String pmId, String refId, String tenantID,int getPmhdrIdFlag) {
		String resp = "";
		try {

			if (pmId.equalsIgnoreCase("3")) {

				String getQ = "select case when count(*)>0 then PM_HDR_ID else 0 end from project_hdr where ENQUIRY_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);

			} else if (pmId.equalsIgnoreCase("2")) {
				String getQ = "select case when count(*)>0 then DE_HDR_ID else 0 end from design_hdr where PM_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			} else if (pmId.equalsIgnoreCase("5")) {
				String getQ = "select case when count(*)>0 then SCM_HDR_ID else 0 end from scm_hdr where PM_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("4")) {
				String getQ = "select case when count(*)>0 then ASSY_HDR_ID else 0 end from assy_hdr where PM_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("6")) {
				String getQ = "select case when count(*)>0 then Q_HDR_ID else 0 end from quality_hdr where PM_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("7")) {
				String getQ ="";
				if(	getPmhdrIdFlag == 1) {
				 getQ = "select case when count(*)>0 then FE_HDR_ID else 0 end from finance_hdr hdr inner join project_hdr proj on proj.PM_HDR_ID = hdr.PM_HDR_ID where proj.ENQUIRY_ID='"
						+ refId + "' limit 1";
				}else {
					 getQ = "select case when count(*)>0 then FE_HDR_ID else 0 end from finance_hdr where PM_HDR_ID ='"
								+ refId + "' limit 1";
				}
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}
		} catch (Exception ex) {
			logger.error("getPMFromDept Error" + ex);
		}
		return resp;
	}

	@Override
	public String getMasterIdBySlaveId(String slaveId, String tableName, String tenantId, String tableColumnName) {
		String currentmasterId = "";
		try {
			String currentmasterIdStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN MASTER_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS MASTER_ID\r\n" + "FROM\r\n" + "    " + tableName + " \r\n"
					+ "WHERE\r\n" + "    " + tableColumnName + " ='" + slaveId + "' and TENANT_ID = '" + tenantId
					+ "' ";
			currentmasterId = this.jdbcTemplate.queryForObject(currentmasterIdStr, String.class);

		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentmasterId;
	}

	@Override
	public String getNextApprDesigByDocType(String docType, String currentSeq, String tenantId) {
		String nextApprDesig = "";
		try {
			String nextApprDesigStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n"
					+ "        ELSE ''\r\n" + "    END AS APPR_DESIG\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "    DOC_TYPE = '" + docType + "' \r\n"
					+ "  AND TENANT_ID='"+tenantId+"'     AND CURR_SEQUENCE = " + currentSeq + " + 1";
			nextApprDesig = this.jdbcTemplate.queryForObject(nextApprDesigStr, String.class);

		} catch (Exception ex) {
			logger.error("getNextApprDesigByDocType Error" + ex);
		}
		return nextApprDesig;
	}
	
	@Override
	public String getNextApprDesigByDocGrp(String docType, String currentSeq, String tenantId,String docGroup) {
		String nextApprDesig = "";
		try {
			String nextApprDesigStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n"
					+ "        ELSE ''\r\n" + "    END AS APPR_DESIG\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "  DOC_GROUP='"+docGroup+"' and  DOC_TYPE = '" + docType + "' \r\n"
					+ "  AND TENANT_ID='"+tenantId+"'     AND CURR_SEQUENCE = " + currentSeq + " + 1"  ;
			nextApprDesig = this.jdbcTemplate.queryForObject(nextApprDesigStr, String.class);

		} catch (Exception ex) {
			logger.error("getNextApprDesigByDocGrp Error" + ex);
		}
		return nextApprDesig;
	}
	
	@Override
	public String getPmHdrIdByEnqId(String enqId) {
		String pmHdrId = "";
		try {
			String qry = "select PM_HDR_ID from project_hdr where ENQUIRY_ID='"+enqId+"'";
			pmHdrId = this.jdbcTemplate.queryForObject(qry, String.class);

		} catch (Exception ex) {
			logger.error("getPmHdrIdByEnqId Error" + ex);
		}
		return pmHdrId;
	}
	
	@Override
	public String getNextApprDesigByDocTypeByDocGrp(String docType, String currentSeq, String tenantId,String docGrp) {
		String nextApprDesig = "";
		try {
			String nextApprDesigStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n"
					+ "        ELSE ''\r\n" + "    END AS APPR_DESIG\r\n" + "FROM\r\n"
					+ "    document_lifecycle_mst\r\n" + "WHERE\r\n" + "    DOC_TYPE = '" + docType + "' \r\n"
					+ "    AND TENANT_ID='"+tenantId+"'    AND CURR_SEQUENCE = " + currentSeq + " + 1 and DOC_GROUP = '"+docGrp+"'";
			nextApprDesig = this.jdbcTemplate.queryForObject(nextApprDesigStr, String.class);

		} catch (Exception ex) {
			logger.error("getNextApprDesigByDocType Error" + ex);
		}
		return nextApprDesig;
	}

	@Override
	public int GenerateAndUpdateProjectCode(String hdrId, String tenantId, String projectCode) {
		int updateStatus = 0;
		try {
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDate(), tenantId,
					"project_hdr", "3", jdbcTemplate, 0,0,null,1);
			String maxValue = iProjectDAO.getMinMaxDate("max", hdrId, "PLANNED_END_DATE");
				String minValue = iProjectDAO.getMinMaxDate("min", hdrId, "PLANNED_START_DATE");

			String qry = "UPDATE project_hdr SET PROJECT_CODE=?,TRANSACTION_NO=?,FINANCIAL_YEAR_MST_ID=?,PLANNED_START_DATE =?,PLANNED_END_DATE=? WHERE PM_HDR_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, projectCode, gen.getSeq(), gen.getFinainceId(),minValue,maxValue, hdrId);
//			updateStatus = gen.getEnquiryCode();
		} catch (Exception ex) {
			logger.error("GenerateAndUpdateProjectCode Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateInitiationDate(String initiationDate, String seqStatus, String seq, String pmHdrId) {
		int updateStatus = 0;
		try {

			String qry = "UPDATE project_hdr SET TRANSACTION_STATUS=?, TRANSACTION_STATUS_SEQ=?, INITIATION_DATE=? WHERE PM_HDR_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, seqStatus, seq, initiationDate, pmHdrId);

		} catch (Exception ex) {
			logger.error("updateInitiationDate Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateProjectDtl(String seqStatus, String seq, String pmHdrId) {
		int updateStatus = 0;
		try {

			String qry = "UPDATE project_dtl SET TRANSACTION_STATUS=?, TRANSACTION_STATUS_SEQ=? WHERE MASTER_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, seqStatus, seq, pmHdrId);

		} catch (Exception ex) {
			logger.error("updateProjectDtl Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public String getChildPMID(String pmId, String tenantId) {

		String getQ = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN CHILD_PROCESS\r\n"
				+ "        ELSE 0\r\n" + "    END CHILD_PROCESS\r\n" + "FROM\r\n" + "    process_mst\r\n" + "WHERE\r\n"
				+ "    PM_ID = '" + pmId + "'";
		return this.jdbcTemplate.queryForObject(getQ, String.class);

	}

	@Override
	public String getprojectCode(String pmId, String refId) {
		String resp = "";
		try {

			if (pmId.equalsIgnoreCase("1")) {

				String getQ = "select ENQUIRY_CODE from sales_enq_hdr where SE_ID ='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);

			} else if (pmId.equalsIgnoreCase("2")) {
				String getQ = "select prj.PROJECT_CODE from design_hdr dsg inner join project_hdr prj on dsg.PM_HDR_ID = prj.PM_HDR_ID where dsg.DE_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			} else if (pmId.equalsIgnoreCase("3")) {
				String getQ = "select PROJECT_CODE from project_hdr where PM_HDR_ID ='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("4")) {
				String getQ = "select prj.PROJECT_CODE from assy_hdr dsg inner join project_hdr prj on dsg.PM_HDR_ID = prj.PM_HDR_ID where dsg.ASSY_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("5")) {
				String getQ = "select prj.PROJECT_CODE from scm_hdr dsg inner join project_hdr prj on dsg.PM_HDR_ID = prj.PM_HDR_ID where dsg.SCM_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("6")) {
				String getQ = "select prj.PROJECT_CODE from quality_hdr dsg inner join project_hdr prj on dsg.PM_HDR_ID = prj.PM_HDR_ID where dsg.Q_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}else if (pmId.equalsIgnoreCase("7")) {
				String getQ = "select prj.PROJECT_CODE from finance_hdr dsg inner join project_hdr prj on dsg.PM_HDR_ID = prj.PM_HDR_ID where dsg.FE_HDR_ID='"
						+ refId + "'";
				resp = this.jdbcTemplate.queryForObject(getQ, String.class);
			}
		} catch (Exception ex) {
			logger.error("getprojectCode Error" + ex);
		}
		return resp;
	}

	@Override
	public int updateProjectDueDate(String dueDate, String empId, String pmHdrId, String reason, String tenantId) {
		int insertStatus = 0;
		try {

			String qry="INSERT INTO project_due_dates (PM_HDR_ID, DUE_DATE, REASON, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?)";
			insertStatus=this.jdbcTemplate.update(qry,pmHdrId,dueDate,"Initial",empId,CommonMethod.getCurrentDateTime(),tenantId);

		} catch (Exception ex) {
			logger.error("updateProjectDtl Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public List<ProjectDueDateEntity> getProjectDueDates(String pmHdrId, String tenantId) {
		List<ProjectDueDateEntity> list=new ArrayList<ProjectDueDateEntity>();
		try {
			String qry="SELECT \r\n" + 
					"    pd.DUE_DATE,\r\n" + 
					"    pd.PD_ID,\r\n" + 
					"    pd.PM_HDR_ID,\r\n" + 
					"    pd.REASON,\r\n" + 
					"    pd.TENANT_ID,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME as UPDATED_BY,\r\n" + 
					"    pd.UPDATED_ON\r\n" + 
					"FROM\r\n" + 
					"    project_due_dates pd\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst em ON pd.UPDATED_BY = em.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    pd.PM_HDR_ID = ? AND pd.TENANT_ID = ?";
			list=this.jdbcTemplate.query(qry, new ProjectDueDateRowMapper(),pmHdrId,tenantId);
			
		}catch (Exception e) {
			logger.error("getQIStatusDtls method exception: " +e);
		}
		return list;
	}

	@Override
	public void updatePmDueDate(String pmHdrId, String dueDate) {
		try {
			String qry="UPDATE project_hdr SET DUE_DATE=? , PLANNED_END_DATE = ?  WHERE PM_HDR_ID=?";
			this.jdbcTemplate.update(qry,dueDate,dueDate,pmHdrId);

		} catch (Exception ex) {
			logger.error("updatePmDueDate Error" + ex);
		}
	}


	@Override
	public int udpateDueDate(String tableName,String startDate, String dueDate, String tenantId,String pmHdrId) {
		int udpateDueDate = 0;
		try {

			String qry="update "+tableName+"  set DUE_DATE = ? , SCM_INITIATED_DATE = ? where PM_HDR_ID =  ? and TENANT_ID = ? ";
			udpateDueDate=this.jdbcTemplate.update(qry,dueDate,startDate,pmHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("udpateDueDate Error" + ex);
		}
		return udpateDueDate;
	}

	@Override
	public int udpatestartAndEndDate(String tableName,String startDate, String dueDate, String tenantId,String pmHdrId) {
		int udpateDueDate = 0;
		try {

			String qry="update "+tableName+"  set PLANNED_START_DATE = ? , PLANNED_END_DATE = ? where PM_HDR_ID =  ? and TENANT_ID = ? ";
			udpateDueDate=this.jdbcTemplate.update(qry,startDate,dueDate,pmHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("udpateDueDate Error" + ex);
		}
		return udpateDueDate;
	}

	@Override
	public String getSaleEnqRefId(String referenceId, String tenantId) {

		String getQ = "select MASTER_ID from sales_enq_dtl where SE_DTL_ID = '"+referenceId+"' and TENANT_ID = '"+tenantId+"';";
		return this.jdbcTemplate.queryForObject(getQ, String.class);

	}

	@Override
	public int getProjectCode(String projectCode, String tenantId) {
	    int count = 0;
	    try {
	        
	        String checkQuery = "SELECT COUNT(*) FROM project_hdr WHERE PROJECT_CODE = ? and TENANT_ID = ? ";
	        count = this.jdbcTemplate.queryForObject(checkQuery, Integer.class, projectCode, tenantId);

	    } catch (Exception ex) {
	        logger.error("getProjectCode Error: " + ex);
	    }
	    return count;
	}

	public List<DocumentStatusMstEntity> getDocDtlcurrentSeqByDocGrp(String referenceDoc, String seq, String tenantId,
			String docGrp, String processDoc) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? and dsm.DOC_GROUP= ? AND PROCESS_CODE=?";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId,docGrp,processDoc);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return documentNextSeqList;
	}

	public List<DocumentStatusMstEntity> getDocDtlcurrentSeqForBudgetExcess(String docType, String seqNo,
			String tenantID, String processDoc) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? and dsm.PROCESS_CODE = ? ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					docType, seqNo, tenantID, processDoc);
		} catch (Exception ex) {
			logger.error("getDocDtlcurrentSeqForBudgetExcess Error" + ex);
		}
		return documentNextSeqList;
	}

}
