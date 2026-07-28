package com.vmfg.quality.dao.impl;


import java.math.BigDecimal;
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

import com.vmfg.quality.RowMapper.QIStatusRowMapper;
import com.vmfg.quality.RowMapper.QiCaRowMapper;
import com.vmfg.quality.RowMapper.QualityInsRequestRowMapper;
import com.vmfg.quality.RowMapper.QualityInspectionDtlRowMapper;
import com.vmfg.quality.RowMapper.QualityInspectionHdrRowMapper;
import com.vmfg.quality.RowMapper.RetrieveQualitInspectionRowMapper;
import com.vmfg.quality.dao.interfaces.IQualityInspectionDAO;
import com.vmfg.quality.entity.QIStatusEntity;
import com.vmfg.quality.entity.QiCaEntity;
import com.vmfg.quality.entity.QualityInsReqEntity;
import com.vmfg.quality.entity.QualityInspectionDtlEntity;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.UpdateVendorRatingRequest;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class QualityInspectionDAO implements IQualityInspectionDAO{
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<QualityInspectionHdrEntity> getQIConfigHdrList(String qicName,String tenantId) {
		List<QualityInspectionHdrEntity> qiConfigList = new ArrayList<QualityInspectionHdrEntity>();
		try {
			String qry="SELECT \r\n" + 
					"    QIC_HDR_ID,\r\n" + 
					"    QIC_NAME,\r\n" + 
					"    QIC_CREATED_ON,\r\n" + 
					"    emp.EMPLOYEE_FIRSTNAME as QIC_CREATED_BY,\r\n" + 
					"    IS_ACTIVE,\r\n" + 
					"    qic.TENANT_ID\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_config_hdr qic\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON qic.QIC_CREATED_BY = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    QIC_NAME =? AND qic.IS_ACTIVE='1' and qic.TENANT_ID=?";
			qiConfigList=this.jdbcTemplate.query(qry, new QualityInspectionHdrRowMapper(),qicName,tenantId);
		}catch (Exception e) {
			logger.error("getQIConfigHdrList method exception: " +e);
		}
		return qiConfigList;
	}

	@Override
	public List<QualityInspectionDtlEntity> getQIConfigDtls(String qicHdrId) {
		List<QualityInspectionDtlEntity> qiConfigDtlList=new ArrayList<QualityInspectionDtlEntity>();
		try {
			String qry="select  @a:=@a + 1 AS S_NO,qi.* from (SELECT @a:=0) AS a,quality_inspection_config_dtl qi where QIC_HDR_ID=?";
			qiConfigDtlList=this.jdbcTemplate.query(qry, new QualityInspectionDtlRowMapper(),qicHdrId);
			
		}catch (Exception e) {
			logger.error("getQIConfigDtls method exception: " +e);
		}
		return qiConfigDtlList;
	}

	@Override
	public List<QualityInspectionHdrEntity> getQiHdrList(String qiId, String tenantId) {
		List<QualityInspectionHdrEntity> list=new ArrayList<QualityInspectionHdrEntity>();
		try {
			String qry="SELECT \r\n" + 
					"    QI_HDR_ID,\r\n" + 
					"    QI_ID,\r\n" + 
					"    PM_HDR_ID,\r\n" + 
					"    QUALITY_REF_NO,\r\n" + 
					"    INSPECTION_QTY,\r\n" + 
					"    OK_QTY,NOK_QTY,REWORK_QTY,OK_QTY_REMARKS,NOK_QTY_REMARKS,\r\n" + 
					"    CA_INTERNAL,\r\n" + 
					"    CA_VENDOR,\r\n" + 
					"    REVISION_DATE,\r\n" + 
					"    INSPECTION_TYPE,\r\n" + 
					"    REWORK_INTERNAL,\r\n" + 
					"    REWORK_VENDOR,\r\n" + 
					"    REJECTED_INTERNAL,\r\n" + 
					"    REJECTED_EXTERNAL,\r\n" + 
					"	 emp.EMPLOYEE_FIRSTNAME as INSPECTED_BY,\r\n" + 
					"    INSPECTED_ON,\r\n" + 
					"    SEQUENCE_NO,\r\n" + 
					"    SEQUENCE_STATUS,\r\n" + 
					"    IS_COMPLETED,NR_FLAG,\r\n" + 
					"    qi.TENANT_ID,qi.VENDOR_CODE,\r\n" + 
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,qi.QUALITY_RATING,"+
					"	qi.CONFIG_NAME,qi.REJECTED_INTERNAL_REMARKS,qi.REJECTED_EXTERNAL_REMARKS,\r\n" +
					"	qi.REWORK_INTERNAL_REMARKS,qi.REWORK_VENDOR_REMARKS "+
					"FROM\r\n" + 
					"    quality_inspection_hdr qi\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code doc ON qi.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON qi.INSPECTED_BY = emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    QI_ID = ? AND qi.TENANT_ID = ? AND qi.CANCEL_FLAG != 1 AND qi.IS_LATEST = 1";
			list=this.jdbcTemplate.query(qry, new QualityInspectionHdrRowMapper(),qiId,tenantId);
			
		}catch (Exception e) {
			logger.error("getQiHdrList method exception: " +e);
		}
		return list;
	}

	@Override
	public List<QualityInspectionHdrEntity> getQiHdrListtByQiHdrId(String qiHdrId, String tenantId) {
		List<QualityInspectionHdrEntity> list=new ArrayList<QualityInspectionHdrEntity>();
		try {
			String qry="select * from quality_inspection_hdr where QI_HDR_ID=? and TENANT_ID=?";
			list=this.jdbcTemplate.query(qry, new QualityInspectionHdrRowMapper(),qiHdrId,tenantId);
			
		}catch (Exception e) {
			logger.error("getQiHdrList method exception: " +e);
		}
		return list;
	}
	
	@Override
	public List<QualityInspectionDtlEntity> getQIDtlList(String qiHdrId) {
		List<QualityInspectionDtlEntity> list=new ArrayList<QualityInspectionDtlEntity>();
		try {
			String qry="select  @a:=@a + 1 AS S_NO,dtl.* from (SELECT @a:=0) AS a,quality_inspection_dtl dtl where QI_HDR_ID=? order by SERIAL_NO;";
			list=this.jdbcTemplate.query(qry, new QualityInspectionDtlRowMapper(),qiHdrId);
			
		}catch (Exception e) {
			logger.error("getQIDtlList method exception: " +e);
		}
		return list;
	}

	@Override
	public List<QIStatusEntity> getQIStatusDtls(String refId,String refDoc, String tenantId) {
		List<QIStatusEntity> list=new ArrayList<QIStatusEntity>();
		try {
			String qry="SELECT \r\n" + 
					"    QSD_ID,\r\n" + 
					"    REFERENCE_ID,\r\n" + 
					"    REFERENCE_DOC,\r\n" + 
					"    SEQUENCE_NO,\r\n" + 
					"    SEQUENCE_STATUS,\r\n" + 
					"    REMARKS,\r\n" + 
					"    emp.EMPLOYEE_FIRSTNAME AS UPDATED_BY,\r\n" + 
					"    UPDATED_ON,\r\n" + 
					"    qis.TENANT_ID,\r\n" + 
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"FROM \r\n" + 
					"    quality_inspection_status qis\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON qis.UPDATED_BY = emp.EMPLOYEE_ID\r\n" + 
					"    inner join document_status_type_code doc on qis.SEQUENCE_STATUS=doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
					"WHERE\r\n" + 
					"    REFERENCE_ID = ? AND REFERENCE_DOC =? AND qis.TENANT_ID = ?;";
			list=this.jdbcTemplate.query(qry, new QIStatusRowMapper(),refId,refDoc,tenantId);
			
		}catch (Exception e) {
			logger.error("getQIStatusDtls method exception: " +e);
		}
		return list;
	}

	@Override
	public int resetAndInsertQiHdr(QualityInspectionHdrEntity qiHdrDtlReq) {
		int qiHdrId = 0;
		try {
			// lock the parent request row for this QI_ID so a concurrent submission for the
			// same QI_ID can't interleave with the reset+insert below and leave two rows both
			// flagged IS_LATEST=1 (this class is @Transactional, so the lock is held for the
			// duration of this method and released on commit)
			jdbcTemplate.queryForObject(
					"SELECT QI_ID FROM quality_inspection_request WHERE QI_ID = ? FOR UPDATE",
					Integer.class, qiHdrDtlReq.getQiId());

			// no TENANT_ID filter here - a row inserted with a bad/blank TENANT_ID must still
			// be resettable, otherwise it stays IS_LATEST=1 forever
			jdbcTemplate.update("UPDATE quality_inspection_hdr SET IS_LATEST = 0 WHERE QI_ID = ?",
					qiHdrDtlReq.getQiId());

			qiHdrId = insertInQiHdr(qiHdrDtlReq);
		} catch (Exception ex) {
			logger.error("resetAndInsertQiHdr error: " + ex);
		}
		return qiHdrId;
	}

	@Override
	public int insertInQiHdr(QualityInspectionHdrEntity qiHdrDtlReq) {
		int qiHdrId = 0;
		try {

			String insertQry = " INSERT INTO quality_inspection_hdr(QI_ID, PM_HDR_ID, VENDOR_CODE, QUALITY_REF_NO, DRAWING_NO, INSPECTION_TYPE, CONFIG_NAME, INSPECTION_QTY, OK_QTY, OK_QTY_REMARKS, NOK_QTY, NOK_QTY_REMARKS, "
					+ "CA_INTERNAL, CA_VENDOR, REVISION_DATE, REWORK_INTERNAL, REWORK_VENDOR, REJECTED_INTERNAL, REJECTED_EXTERNAL, QUALITY_RATING, INSPECTED_BY, INSPECTED_ON,"
					+ " SEQUENCE_NO, SEQUENCE_STATUS, IS_COMPLETED, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, TENANT_ID,REJECTED_INTERNAL_REMARKS ,REJECTED_EXTERNAL_REMARKS,REWORK_INTERNAL_REMARKS,REWORK_VENDOR_REMARKS, CANCEL_FLAG) VALUES (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, qiHdrDtlReq.getQiId());
					ps.setString(2, qiHdrDtlReq.getPmHdrId());
					ps.setString(3, qiHdrDtlReq.getVendorCode());
					ps.setString(4, qiHdrDtlReq.getQualityRefNo());
					ps.setString(5,qiHdrDtlReq.getDrawingNo());
					ps.setString(6,qiHdrDtlReq.getInspectionType());
					ps.setString(7,qiHdrDtlReq.getConfigName());
					ps.setString(8, qiHdrDtlReq.getInspectionQty().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getInspectionQty());
					ps.setString(9, qiHdrDtlReq.getTotalOkQty().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalOkQty());
					ps.setString(10,qiHdrDtlReq.getOkRemarks());
					ps.setString(11, qiHdrDtlReq.getTotalNokQty().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalNokQty());
					ps.setString(12,qiHdrDtlReq.getNokRemarks());
					ps.setString(13, qiHdrDtlReq.getCaInternal().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getCaInternal());
					ps.setString(14, qiHdrDtlReq.getCaVendor().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getCaVendor());
					ps.setString(15,qiHdrDtlReq.getRevisionDate());
					ps.setString(16,qiHdrDtlReq.getTotalReworkInt().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalReworkInt());
					ps.setString(17,qiHdrDtlReq.getTotalReworkVen().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalReworkVen());
					ps.setString(18,qiHdrDtlReq.getTotalRejcInt().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalRejcInt());
					ps.setString(19,qiHdrDtlReq.getTotalRejcExt().equalsIgnoreCase("") ? "0" :qiHdrDtlReq.getTotalRejcExt());
					ps.setString(20,qiHdrDtlReq.getQualityRating());
					ps.setString(21,qiHdrDtlReq.getInspectedBy());
					ps.setString(22,qiHdrDtlReq.getInspectedOn());
					ps.setString(23,"1"); // seqNo
					ps.setString(24,qiHdrDtlReq.getSequenceStatus());
					ps.setString(25,"0");
					ps.setString(26,CommonMethod.getCurrentDateTime());
					ps.setString(27,qiHdrDtlReq.getEmpId());
					ps.setString(28, qiHdrDtlReq.getTenantId());
					ps.setString(29, qiHdrDtlReq.getRejectIntRemarks());
					ps.setString(30, qiHdrDtlReq.getRejectExtRemarks());
					ps.setString(31,qiHdrDtlReq.getReworkIntRemarks());
					ps.setString(32,qiHdrDtlReq.getReworkVenRemarks());
					ps.setString(33, qiHdrDtlReq.getCancelFlag());
					return ps;
				}

			}, holder);
			qiHdrId = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertInQiHdr error---> " + ex);
		}

		return qiHdrId;

	}

	@Override
	public int updateQIHdr(QualityInspectionHdrEntity qiHdrDtlReq) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE quality_inspection_hdr SET QUALITY_REF_NO=?,DRAWING_NO=?, INSPECTION_QTY=?, OK_QTY=?, CA_INTERNAL=?, CA_VENDOR=?, REVISION_DATE=?, INSPECTION_TYPE=?, REWORK_INTERNAL=?,"
					+ " REWORK_VENDOR=?, REJECTED_INTERNAL=?, REJECTED_EXTERNAL=?, INSPECTED_ON=?, QUALITY_RATING=?,OK_QTY_REMARKS=?,NOK_QTY=?,NOK_QTY_REMARKS=?,REJECTED_INTERNAL_REMARKS=? ,REJECTED_EXTERNAL_REMARKS=? , REWORK_INTERNAL_REMARKS=?,  REWORK_VENDOR_REMARKS=? WHERE QI_HDR_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, qiHdrDtlReq.getQualityRefNo(), qiHdrDtlReq.getDrawingNo(),
					qiHdrDtlReq.getInspectionQty(),
					qiHdrDtlReq.getTotalOkQty().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getTotalOkQty(),
					qiHdrDtlReq.getCaInternal().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getCaInternal(),
					qiHdrDtlReq.getCaVendor().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getCaVendor(),
					qiHdrDtlReq.getRevisionDate(),
					qiHdrDtlReq.getInspectionType(),
					qiHdrDtlReq.getTotalReworkInt().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getTotalReworkInt(),
					qiHdrDtlReq.getTotalReworkVen().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getTotalReworkVen(),
					qiHdrDtlReq.getTotalRejcInt().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getTotalRejcInt(),
					qiHdrDtlReq.getTotalRejcExt().equalsIgnoreCase("") ? "0" : qiHdrDtlReq.getTotalRejcExt(),
					qiHdrDtlReq.getInspectedOn(), qiHdrDtlReq.getQualityRating(),
					qiHdrDtlReq.getOkRemarks(),qiHdrDtlReq.getTotalNokQty(),qiHdrDtlReq.getNokRemarks(),
					qiHdrDtlReq.getRejectIntRemarks(),
					qiHdrDtlReq.getRejectExtRemarks(),
					qiHdrDtlReq.getReworkIntRemarks(),
					qiHdrDtlReq.getReworkVenRemarks(),
					qiHdrDtlReq.getQiHdrId());

		} catch (Exception ex) {
			logger.error("updateQIHdr method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int insertQiDtl(QualityInspectionDtlEntity req) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO quality_inspection_dtl (QI_HDR_ID, SERIAL_NO,DESCRIPTION, SPECIFICATION, INSPECTION_METHOD, MINIMUM, MAXIMUM, AVERAGE, INSPECTION_RESULT, TENANT_ID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insertStatus = this.jdbcTemplate.update(qry, req.getQiHdrId(),req.getSerialNumber(),req.getDescription(),req.getSpecification(),req.getInspectionMethod(),
					req.getMinimum()==null || req.getMinimum().equalsIgnoreCase("") ? "0" : req.getMinimum(),
					req.getMaximum()==null || req.getMaximum().equalsIgnoreCase("")  ? "0" : req.getMaximum(),
					req.getAverage()==null || req.getAverage().equalsIgnoreCase("")  ? "0" : req.getAverage(),
					req.getInspectionResult(),
					req.getTenantId());

		} catch (Exception ex) {
			logger.error("insertQiDtl method Error" + ex);
		}
		return insertStatus;
	}
	
	@Override
	public int updateQiDtl(QualityInspectionDtlEntity req) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE quality_inspection_dtl SET SERIAL_NO=?,DESCRIPTION=?, SPECIFICATION=?, INSPECTION_METHOD=?, MINIMUM=?, MAXIMUM=?, AVERAGE=?, INSPECTION_RESULT=? WHERE QI_DTL_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, req.getSerialNumber(),req.getDescription(),req.getSpecification(),req.getInspectionMethod(),
					req.getMinimum()==null || req.getMinimum().equalsIgnoreCase("") ? "0" : req.getMinimum(),
					req.getMaximum()==null || req.getMaximum().equalsIgnoreCase("")  ? "0" : req.getMaximum(),
					req.getAverage()==null || req.getAverage().equalsIgnoreCase("")  ? "0" : req.getAverage(),
					req.getInspectionResult(),
					req.getQiDtlId());

		} catch (Exception ex) {
			logger.error("updateQiDtl method Error" + ex);
		}
		return updateStatus;
	}
	
	@Override
	public int insertQiStatus(String refId, String refDoc, String seqNo, String seqStatus,
			String remarks, String empId, String tenantId) {
	int insertStatus=0;
		try {
			String qry = "INSERT INTO quality_inspection_status (REFERENCE_ID, REFERENCE_DOC, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
			insertStatus=this.jdbcTemplate.update(qry, refId,  refDoc,  seqNo,  seqStatus,remarks,  empId,  CommonMethod.getCurrentDateTime(),tenantId);

		} catch (Exception ex) {
			logger.error("insertQiStatus method Error" + ex);
		}
		return insertStatus;
		
	}
	
	@Override
	public int insertInQualityCa(String qiHdrId, String poId, String poCode, String poDtlId,
			String caType, String qty, String seqNo,String seqStatus,String  isApproved,String pmHdrId,String tenantId) {
	int qiCaId=0;
		try {
			String qry = "INSERT INTO quality_ca_dtl (QI_HDR_ID, PO_ID, PO_CODE, PO_DTL_ID, CA_TYPE, QTY, SEQUENCE_NO, SEQUENCE_STATUS, IS_APPROVED,PM_HDR_ID,TENANT_ID,REQUEST_RECEIEVED_DATETIME) VALUES (?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?);";
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, qiHdrId);
				ps.setString(2, poId);
				ps.setString(3, poCode);
				ps.setString(4, poDtlId);
				ps.setString(5, caType);
				ps.setString(6, qty);
				ps.setString(7, seqNo);
				ps.setString(8, seqStatus);
				ps.setString(9, isApproved);
				ps.setString(10, pmHdrId);
				ps.setString(11, tenantId);
				ps.setString(12, CommonMethod.getCurrentDateTime());
				return ps;
			}

		}, holder);
			qiCaId = holder.getKey().intValue();
			
		} catch (Exception ex) {
			logger.error("insertInQualityCa method Error" + ex);
		}
		return qiCaId;
		
	}
		@Override
		public int updateInspectedQty(String inspectedQty,String poDtlId,String nokQty,String reWorkQty) {
			int updateStatus=0;
			try {
				String qry=" UPDATE po_dtl SET INSPECTED_QTY = INSPECTED_QTY + ? , NOK_QTY = NOK_QTY + ?, REWORK_QTY = REWORK_QTY + ? WHERE PO_DTL_ID=? AND INSPECTED_QTY + ? <= QTY";
				updateStatus=this.jdbcTemplate.update(qry,inspectedQty,nokQty,reWorkQty,poDtlId,inspectedQty);
			}catch (Exception e) {
				logger.error("updateInspectedQty method Error" + e);
			}
			return updateStatus;
		}
		
		@Override
		public int updateQiReqQtyInspected(String okQty,String qiId) {
			int updateStatus=0;
			try {
				String qry="UPDATE quality_inspection_request SET QTY_INSPECTED= QTY_INSPECTED + ?, LAST_UPDATE_DATETIME=? WHERE QI_ID=?";
				updateStatus=this.jdbcTemplate.update(qry,okQty,CommonMethod.getCurrentDateTime(),qiId);
			}catch (Exception e) {
				logger.error("updateQiReqQtyInspected method Error" + e);
			}
			return updateStatus;
		}
		 
		@Override
		public List<RetrieveQualitInspectionEntity> getPoDtlsByQiHdrId(String qiId) {
				List<RetrieveQualitInspectionEntity> list = new ArrayList<>();
				try {
					String qry="SELECT \r\n" + 
							"    PO_ID,\r\n" + 
							"    PO_DTL_ID,\r\n" + 
							"    PO_CODE\r\n" + 
							"FROM\r\n" + 
							"    quality_inspection_request \r\n" + 
							"WHERE\r\n" + 
							"    QI_ID = '"+qiId+"'";
					list = this.jdbcTemplate.query(qry, new RetrieveQualitInspectionRowMapper());
			}catch (Exception e) {
				logger.error("getPoDtlsByQiHdrId method Error" + e);
			}
			return list;
		}

		@Override
		public int updateQISeqAndStatus(String currentseq, String docStatus, int isCompleted,String qiHdrId,String empId,String nrFLag) {
			int updateStatus = 0;
			try {
				String qry = "UPDATE quality_inspection_hdr SET SEQUENCE_NO=?, SEQUENCE_STATUS=?, IS_COMPLETED=?, NR_FLAG=?, LAST_UPDATED_DATETIME=? ,LAST_UPDATED_BY=? WHERE QI_HDR_ID=?";
				updateStatus = this.jdbcTemplate.update(qry, currentseq, docStatus, isCompleted,nrFLag,CommonMethod.getCurrentDateTime(),empId,qiHdrId);

			} catch (Exception ex) {
				logger.error("updateQISeqAndStatus method Error" + ex);
			}
			return updateStatus;

		}
		
		@Override
		public int updateQiCaSeqAndStatus(String currentseq, String docStatus, int isApproved,String qiCaDtlId,String empId) {
			int updateStatus = 0;
			try {
				String qry = "UPDATE quality_ca_dtl SET SEQUENCE_NO=?, SEQUENCE_STATUS=?, IS_APPROVED=?, LAST_UPDATED_BY=?, LAST_UPDATED_DATETIME=? WHERE QI_CA_DTL_ID=?";
				updateStatus = this.jdbcTemplate.update(qry, currentseq, docStatus, isApproved,empId,CommonMethod.getCurrentDateTime(),qiCaDtlId);

			} catch (Exception ex) {
				logger.error("updateQISeqAndStatus method Error" + ex);
			}
			return updateStatus;

		}

		@Override
		public List<QiCaEntity> getQiCaList(String pmHdrId, String tenantId, String fromDate, String toDate) {
			List<QiCaEntity> list = new ArrayList<>();
			try {
				String Date = fromDate != null ? "AND ca.REQUEST_RECEIEVED_DATETIME between '"+fromDate+"' and '"+toDate+"'" : "";
				String qry="SELECT \r\n" + 
						"       ca.QI_CA_DTL_ID,\r\n" + 
						"    ca.QI_HDR_ID,\r\n" + 
						"    ca.PM_HDR_ID, \r\n" + 
						"    PROJECT_CODE, PROJECT_NAME,\r\n" + 
						"    ca.PO_ID,\r\n" + 
						"    ca.PO_CODE,\r\n" + 
						"    ca.PO_DTL_ID,\r\n" + 
						"    ca.CA_TYPE,\r\n" + 
						"    ca.QTY,\r\n" + 
						"    ca.SEQUENCE_NO,\r\n" + 
						"    ca.SEQUENCE_STATUS,\r\n" + 
						"    ca.REWORK_INTERNAL,\r\n" + 
						"    ca.REWORK_VENDOR,\r\n" + 
						"    ca.REJECTED_INTERNAL,\r\n" + 
						"    ca.REJECTED_EXTERNAL,\r\n" + 
						"    ca.CA_QTY,\r\n" + 
						"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
						"    ca.IS_APPROVED,\r\n" + 
						"    ca.TENANT_ID,\r\n" + 
						"    po.INDENT_DTL_ID,\r\n" + 
						"    prod.PRODUCT_CODE,\r\n" + 
						"    prod.PRODUCT_DESCRIPTION, ca.REQUEST_RECEIEVED_DATETIME,\r\n" + 
						"    case when ca.IS_APPROVED = 1 then ca.LAST_UPDATED_DATETIME ELSE '' END as CA_APPROVED_DATE,\r\n" + 
						"    datediff(case when ca.IS_APPROVED = 1 then ca.LAST_UPDATED_DATETIME ELSE '' END,ca.REQUEST_RECEIEVED_DATETIME) as CA_DURATION,\r\n" + 
						"    (select mst.EMPLOYEE_FIRSTNAME from employee_mst mst where mst.EMPLOYEE_ID = qih.LAST_UPDATED_BY) as REQUESTED_BY, \r\n" + 
						"    case when ca.IS_APPROVED = 1 then \r\n" + 
						"    (select mst.EMPLOYEE_FIRSTNAME from employee_mst mst where mst.EMPLOYEE_ID =ca.LAST_UPDATED_BY) else '' end as APPROVED_BY, \r\n" + 
						"    uom.UOM_SHORT_DESCRIPTION,ca.CA_INTERNAL,ca.CA_VENDOR,ca.REMARKS,pohdr.VENDOR_NAME,qih.QI_ID " + 
						"FROM\r\n" + 
						"    quality_ca_dtl ca INNER JOIN quality_inspection_hdr qih on qih.QI_HDR_ID = ca.QI_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl po ON ca.PO_DTL_ID = po.PO_DTL_ID INNER JOIN po_hdr pohdr ON pohdr.PO_ID = ca.PO_ID \r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl id ON po.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    product_mst prod ON id.PRODUCT_CODE = prod.PRODUCT_CODE AND ca.PM_HDR_ID=prod.PM_HDR_ID \r\n" + 
						"        INNER JOIN\r\n" + 
						"    uom_mst uom ON prod.PRODUCT_UOM_CODE = uom.UOM_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    document_status_type_code doc ON ca.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
						"   INNER JOIN\r\n" + 
						"	project_hdr hdr ON hdr.PM_HDR_ID = ca.PM_HDR_ID \r\n"+
						"WHERE\r\n" + 
						"    ca.PM_HDR_ID LIKE '"+pmHdrId+"'\r\n" + 
						"        AND ca.TENANT_ID = '"+tenantId+"'  "+Date+"  "  +
					    "group by ca.QI_CA_DTL_ID;";
				list = this.jdbcTemplate.query(qry, new QiCaRowMapper());
		}catch (Exception e) {
			logger.error("getPoDtlsByQiHdrId method Error" + e);
		}
		return list;
	}
		
		@Override
		public List<QiCaEntity> getQiCaDtlsByQiCaId(String qiCaDtlId, String tenantId) {
			List<QiCaEntity> list = new ArrayList<>();
			try {
				String qry="SELECT \r\n" + 
						"    QI_CA_DTL_ID,\r\n" + 
						"    QI_HDR_ID,\r\n" + 
						"    ca.PM_HDR_ID,\r\n" + 
						"    ca.PO_ID,\r\n" + 
						"    ca.PO_CODE,\r\n" + 
						"    ca.PO_DTL_ID,\r\n" + 
						"    ca.CA_TYPE,\r\n" + 
						"    ca.QTY,\r\n" + 
						"    ca.SEQUENCE_NO,\r\n" + 
						"    ca.SEQUENCE_STATUS,\r\n" + 
						"    ca.REQUEST_RECEIEVED_DATETIME,\r\n" + 
						"    ca.REWORK_INTERNAL,\r\n" + 
						"    ca.REWORK_VENDOR,\r\n" + 
						"    ca.REJECTED_INTERNAL,\r\n" + 
						"    ca.REJECTED_EXTERNAL,\r\n" + 
						"    ca.CA_QTY,\r\n" + 
						"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
						"    ca.IS_APPROVED,\r\n" + 
						"    ca.TENANT_ID,\r\n" + 
						"    po.INDENT_DTL_ID,\r\n" + 
						"    prod.PRODUCT_CODE,\r\n" + 
						"    prod.PRODUCT_DESCRIPTION,\r\n" + 
						"    uom.UOM_SHORT_DESCRIPTION,\r\n" + 
						"    CA_INTERNAL,\r\n" + 
						"    CA_VENDOR,\r\n" + 
						"    ca.REMARKS,\r\n" + 
						"    pohdr.VENDOR_NAME,\r\n" + 
						"    (SELECT \r\n" + 
						"            DM_ID\r\n" + 
						"        FROM\r\n" + 
						"            document_management\r\n" + 
						"        WHERE\r\n" + 
						"            REFERENCE_ID = po.INDENT_DTL_ID\r\n" + 
						"                AND UPLOAD_DOC_TYPE = 'FC015'\r\n" + 
						"        ORDER BY VERSION DESC limit 1) AS DM_ID\r\n" + 
						"FROM\r\n" + 
						"    quality_ca_dtl ca\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl po ON ca.PO_DTL_ID = po.PO_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_hdr pohdr ON pohdr.PO_ID = ca.PO_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr ih ON ih.INDENT_ID = pohdr.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl id ON po.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    product_mst prod ON id.PRODUCT_CODE = prod.PRODUCT_CODE\r\n" + 
						"        AND prod.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    uom_mst uom ON prod.PRODUCT_UOM_CODE = uom.UOM_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    document_status_type_code doc ON ca.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" +
						"WHERE\r\n" + 
						"    ca.QI_CA_DTL_ID = '"+qiCaDtlId+"'\r\n" + 
						"        AND ca.TENANT_ID = '"+tenantId+"' group by prod.PRODUCT_CODE ";
				list = this.jdbcTemplate.query(qry, new QiCaRowMapper());
		}catch (Exception e) {
			logger.error("getQiCaDtlsByQiCaId method Error" + e);
		}
		return list;
	}
		
		
		@Override
		public List<QiCaEntity> getQiCaListByQiCaId(String qiCaId) {
			List<QiCaEntity> list = new ArrayList<>();
			try {
				String qry="SELECT \r\n" + 
						"    ca.*,id.PRODUCT_CODE,id.UNIT as UOM,id.QTY AS INDENT_QTY,id.INDENT_DTL_ID, qhdr.INSPECTION_QTY\r\n" + 
						"FROM\r\n" + 
						"    quality_ca_dtl ca\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl po ON ca.PO_DTL_ID = po.PO_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl id ON po.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
						"   INNER JOIN quality_inspection_hdr qhdr ON qhdr.QI_HDR_ID = ca.QI_HDR_ID \r\n" +
						"WHERE\r\n" + 
						"    ca.QI_CA_DTL_ID = '"+qiCaId+"'";
				list = this.jdbcTemplate.query(qry, new QiCaRowMapper());
		}catch (Exception e) {
			logger.error("getQiCaListByQiCaId method Error" + e);
		}
		return list;
	}
		
		@Override
		public int updateQiHdrValues(String caInternal,String caVendor,String reworkInternal,String reworkVendor,String rejectedInternal,
					String rejectedExternal ,String qiHdrId,String okQty, String nOkQty, String reworkQty,String empId, String qualityRating) {
			int updateStatus = 0;
			try {
				String qry = " UPDATE quality_inspection_hdr SET OK_QTY=OK_QTY + ?, NOK_QTY=?, REWORK_QTY=REWORK_QTY+?, CA_INTERNAL=?, CA_VENDOR=?, REWORK_INTERNAL=REWORK_INTERNAL+?, REWORK_VENDOR=REWORK_VENDOR+?, REJECTED_INTERNAL=REJECTED_INTERNAL + ?, REJECTED_EXTERNAL=REJECTED_EXTERNAL + ?, LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=?, QUALITY_RATING=QUALITY_RATING+? WHERE QI_HDR_ID=?";
					updateStatus = this.jdbcTemplate.update(qry,okQty,nOkQty,reworkQty,caInternal,caVendor,reworkInternal,reworkVendor,rejectedInternal,rejectedExternal,CommonMethod.getCurrentDateTime(),empId,qualityRating,qiHdrId);

			} catch (Exception ex) {
				logger.error("updateCaValues method Error" + ex);
			}
			return updateStatus;

		}
		
		@Override
		public int updateQiCaDtlValues(String caQty,String reworkInternal,String reworkVendor,String rejectedInternal,String rejectedExternal ,String qiCaDtlId,String caVendor,String caInternal,String remarks) {
			int updateStatus = 0;
			try {
				String qry = "UPDATE quality_ca_dtl SET REWORK_INTERNAL=?, REWORK_VENDOR=?, REJECTED_INTERNAL=?, REJECTED_EXTERNAL=?, CA_QTY=?,CA_VENDOR=?,CA_INTERNAL=?,REMARKS=? WHERE QI_CA_DTL_ID=?;";
				updateStatus = this.jdbcTemplate.update(qry,reworkInternal,reworkVendor,rejectedInternal,rejectedExternal,caQty,caVendor,caInternal,remarks,qiCaDtlId);

			} catch (Exception ex) {
				logger.error("updateQiCaDtlValues method Error" + ex);
			}
			return updateStatus;

		}
		
		@Override
		public String compareQtyAndInsQty(String poDtlId) {
			String value="";
			try {
				String getId = "select case when QTY < INSPECTED_QTY then QTY else 0 end as result  from po_dtl where PO_DTL_ID= ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(getId, poDtlId);
				value =  resultMap.get("result").toString();
			} catch (Exception ex) {
				logger.error("compareQtyAndInsQty Method Exception --->" + ex);

			}
			return value;
		}

		@Override
		public int resetPoInspectedQty(String receivedQty,String poDtlId) {
			int updateStatus=0;
			try {
				String qry=" UPDATE po_dtl SET INSPECTED_QTY = ? WHERE PO_DTL_ID=?";
				updateStatus=this.jdbcTemplate.update(qry,receivedQty,poDtlId);
			}catch (Exception e) {
				logger.error("resetPoInspectedQty method Error" + e);
			}
			return updateStatus;
		}
		
		@Override
		public void updateMiStatus(String miId,String empId) {
			try {
				String qry="UPDATE material_inward_hdr SET STATUS='DS086', IS_COMPLETED='1', LAST_UPDATED_ON=?, LAST_UPDATED_BY=? WHERE MI_ID=?";
				this.jdbcTemplate.update(qry,CommonMethod.getCurrentDateTime(),empId,miId);
			}catch (Exception e) {
				logger.error("resetPoInspectedQty method Error" + e);
			}
		}
		
		
		@Override
		public void updateMiQty(String miDtlID,String okQty,String nokQty,String reworkQty) {
			try {
				String qry="UPDATE material_inward_dtl SET INSPECTED_QTY=INSPECTED_QTY + ?, NOK_QTY=NOK_QTY + ?, REWORK_QTY= REWORK_QTY + ? WHERE MI_DTL_ID=? AND INSPECTED_QTY + ? <= RECEIVED_QTY";
				this.jdbcTemplate.update(qry,okQty,nokQty,reworkQty,miDtlID,okQty);
			}catch (Exception e) {
				logger.error("updateMiQty method Error" + e);
			}
		}
		
		@Override
		public List<Integer> getMiCompletedStatus(String miId) {
			List<Integer> list=new ArrayList<Integer>();
			try {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(RECEIVED_QTY) != SUM(INSPECTED_QTY) THEN 0\r\n" + 
						"        ELSE 1\r\n" + 
						"    END\r\n" + 
						"FROM\r\n" + 
						"    material_inward_dtl\r\n" + 
						"WHERE\r\n" + 
						"    MI_ID = '"+miId+"'\r\n" + 
						"GROUP BY MI_DTL_ID;";
				list=this.jdbcTemplate.queryForList(qry, Integer.class);
			}catch (Exception e) {
				logger.error("getMiCompletedStatus method Error" + e);
			}
			return list;
		}

		@Override
		public String getMiDtlIdByCaDtlId(String qiCaDtlId, String tenantId) {
			String miDtlId="";
			try {
				String qry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(*) > 0 THEN COALESCE(qir.MI_DTL_ID, '')\r\n" + 
						"        ELSE ''\r\n" + 
						"    END AS MI_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_request qir\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_hdr hdr ON qir.QI_ID = hdr.QI_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_ca_dtl ca ON hdr.QI_HDR_ID = ca.QI_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    ca.QI_CA_DTL_ID = ?\r\n" + 
						"        AND ca.TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, qiCaDtlId,tenantId);
				miDtlId = resultMap.get("MI_DTL_ID").toString();
			}catch (Exception e) {
				logger.error("getMiDtlIdByCaDtlId method Error" + e);
			}
			return miDtlId;
		}
		
		@Override
		public String getIndentHdrIdByQiHdrId(String qihdrId, String tenantId) {
			String indentDtlId="";
			try {
				String qry="SELECT \r\n" + 
						"    CASE WHEN qir.INDENT_DTL_ID IS NOT NULL THEN qir.INDENT_DTL_ID ELSE NULL END as INDENT_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_request qir\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_hdr hdr ON qir.QI_ID = hdr.QI_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.QI_HDR_ID = ?\r\n" + 
						"        AND hdr.TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, qihdrId,tenantId);
				indentDtlId = resultMap.get("INDENT_DTL_ID").toString();
			}catch (Exception e) {
				logger.error("getIndentHdrIdByQiHdrId method Error" + e);
			}
			return indentDtlId;
		}
		
		@Override
		public String getMiDtlIdByQiHdrId(String qihdrId, String tenantId) {
			String miDtlId="";
			try {
				String qry="SELECT \r\n" + 
						"    CASE WHEN qir.MI_DTL_ID IS NOT NULL THEN qir.MI_DTL_ID ELSE NULL END as MI_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_request qir\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_hdr hdr ON qir.QI_ID = hdr.QI_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.QI_HDR_ID = ?\r\n" + 
						"        AND hdr.TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, qihdrId,tenantId);
				miDtlId = resultMap.get("MI_DTL_ID").toString();
			}catch (Exception e) {
				logger.error("getMiDtlIdByQiHdrId method Error" + e);
			}
			return miDtlId;
		}
		@Override
		public String getMiIdByMiDtlId(String miDtlId, String tenantId) {
			String miId="";
			try {
				String qry="select MI_ID from material_inward_dtl where MI_DTL_ID=? and TENANT_ID=?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, miDtlId,tenantId);
				miId = resultMap.get("MI_ID").toString();			
				}catch (Exception e) {
				logger.error("getMiIdByMiDtlId method Error" + e);
			}
			return miId;
		}

		@Override
		public String getInventoryCodeByMiId(String miId) {
			String inventoryCode="";
			try {
				String qry="select INVENTORY_LOCATION_CODE from material_inward_hdr where MI_ID=?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, miId);
				inventoryCode = resultMap.get("INVENTORY_LOCATION_CODE").toString();
			}catch (Exception e) {
				logger.error("getInventoryCodeByMiId method Error" + e);
			}
			return inventoryCode;
		}

		@Override
		public List<QualityInsReqEntity> getGrnDtls(String qicHdrId, String tenantId) {
			List<QualityInsReqEntity> list= new ArrayList<QualityInsReqEntity>();
			try {
				String qry="SELECT \r\n" + 
						"    qir.PO_DTL_ID,\r\n" + 
						"    qir.INDENT_DTL_ID,\r\n" + 
						"    qir.MI_DTL_ID,\r\n" + 
						"    mi.MI_ID,\r\n" + 
						"    id.PRODUCT_CODE, mi.PRODUCT_ID,\r\n" + 
						"    mh.INVENTORY_LOCATION_CODE,id.UNIT as UOM,id.QTY\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_request qir\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_hdr hdr ON qir.QI_ID = hdr.QI_ID\r\n" + 
						"         INNER JOIN\r\n" + 
						"    indent_dtl id ON qir.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    material_inward_dtl mi ON qir.MI_DTL_ID = mi.MI_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    material_inward_hdr mh ON mh.MI_ID = mi.MI_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.QI_HDR_ID = '"+qicHdrId+"'\r\n" + 
						"        AND hdr.TENANT_ID = '"+tenantId+"';" + 
						"";
				list=this.jdbcTemplate.query(qry,new QualityInsRequestRowMapper());
			}catch (Exception e) {
				logger.error("getGrnDtls method Error" + e);
			}
			return list;
		}

		@Override
		public String getPoCode(String qiCaDtlId) {
			
			String poCode="";
			try {
				String poQry="SELECT \r\n" + 
						"    CASE\r\n" + 
						"        WHEN COUNT(*) > 0 THEN PO_CODE\r\n" + 
						"        ELSE ''\r\n" + 
						"    END PO_CODE\r\n" + 
						"FROM\r\n" + 
						"    quality_ca_dtl\r\n" + 
						"WHERE\r\n" + 
						"    QI_CA_DTL_ID = ?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(poQry, qiCaDtlId);
				poCode = resultMap.get("PO_CODE").toString();
			}catch(Exception ex) {
				logger.error("getPoCode method Error" + ex);
			}
			// TODO Auto-generated method stub
			return poCode;
		}

		@Override
		public String getEmpDepCode(String empId, String tenantId) {
			
			String depCode="";
			try {
				String depQry="select DEPARTMENT_CODE from employee_mst where EMPLOYEE_ID= ? and TENANT_ID=?;\r\n" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(depQry, empId,tenantId);
				depCode = resultMap.get("DEPARTMENT_CODE").toString();
			}catch(Exception ex) {
				logger.error("getEmpDepCode error "+ex);
			}
			
			return depCode;
		}

		@Override
		public String getQiIdByQiHdrId(String qiHdrId) {
			String qiId="";
			try {
				String qry="select QI_ID from quality_inspection_hdr where QI_HDR_ID= ?" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, qiHdrId);
				qiId = resultMap.get("QI_ID").toString();
			}catch(Exception ex) {
				logger.error("getQiIdByQiHdrId error "+ex);
			}
			
			return qiId;
		}

		@Override
		public String getLastSeq(String docType, String tenantId) {
			String lastSeq="";
			try {
				String qry="select CURR_SEQUENCE from document_lifecycle_mst where DOC_TYPE= ? and LAST_SEQ=1 AND TENANT_ID=?" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, docType,tenantId);
				lastSeq = resultMap.get("CURR_SEQUENCE").toString();
			}catch(Exception ex) {
				logger.error("getLastSeq error "+ex);
			}
			
			return lastSeq;
		}
		
		@Override
		public String getCanLastSeq(String docType, String tenantId) {
			String lastSeq="";
			try {
				String qry="select CURR_SEQUENCE from document_lifecycle_mst where DOC_TYPE= ? and LAST_SEQ=2 AND TENANT_ID=?" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, docType,tenantId);
				lastSeq = resultMap.get("CURR_SEQUENCE").toString();
			}catch(Exception ex) {
				logger.error("getLastSeq error "+ex);
			}
			
			return lastSeq;
		}

		@Override
		public String getPoDelDate(String qHdrId, String tenantId) {
			// TODO Auto-generated method stub
			String poDelDate="";
			try {
				String dateQry="SELECT \r\n" + 
						"    phr.DELIVERY_DATE\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_hdr qhr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_inspection_request qir ON qir.QI_ID = qhr.QI_ID\r\n" + 
						"		inner join\r\n" + 
						"	po_hdr phr on phr.PO_ID=qir.PO_ID\r\n" + 
						"where qhr.QI_HDR_ID= ? and qhr.TENANT_ID=?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(dateQry, qHdrId,tenantId);
				poDelDate = resultMap.get("DELIVERY_DATE").toString();
			}catch(Exception ex) {
				logger.error("getPoDelDate error "+ex);
			}
			return poDelDate;
		}

		@Override
		public String getInsDate(String qHdrId, String tenantId) {
			// TODO Auto-generated method stub
			String inspectDate="";
			try {
				String dateQry="SELECT \r\n" + 
						"    qhr.INSPECTED_ON\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_hdr qhr\r\n" + 
						"WHERE\r\n" + 
						"    qhr.QI_HDR_ID = ?\r\n" + 
						"        AND qhr.TENANT_ID = ?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(dateQry, qHdrId,tenantId);
				inspectDate = resultMap.get("INSPECTED_ON").toString();
			}catch(Exception ex) {
				logger.error("getInsDate error "+ex);
			}
			return inspectDate;
		}
		
		@Override
		public String getOldRelationshipRating(String qHdrId, String tenantId) {
			// TODO Auto-generated method stub
			String inspectDate="";
			try {
				String dateQry="SELECT \r\n" + 
						"    qhr.OLD_SUPPLIER_RATING AS OLD_SUPPLIER_RATING\r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_hdr qhr\r\n" + 
						"WHERE\r\n" + 
						"    qhr.QI_HDR_ID = ?\r\n" + 
						"        AND qhr.TENANT_ID = ?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(dateQry, qHdrId,tenantId);
				inspectDate = resultMap.get("OLD_SUPPLIER_RATING").toString();
			}catch(Exception ex) {
				logger.error("getInsDate error "+ex);
			}
			return inspectDate;
		}

		@Override
		public void updateInwardRating(String qHdrId, String tenantId,String poDelDate,String insDate) {
			// TODO Auto-generated method stub
			
			try {
				BigDecimal inwardRating = BigDecimal.ZERO;

				Date poDeliveryDate=new SimpleDateFormat("yyyy-MM-dd").parse(poDelDate); 
			    Date inspectDate = new SimpleDateFormat("yyyy-MM-dd").parse(insDate); 
			    
			    if (inspectDate.compareTo(poDeliveryDate) <= 0) {
		            inwardRating = new BigDecimal(20);
		        } else {
		            long diffInMillis = inspectDate.getTime() - poDeliveryDate.getTime();
		            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

		            if (diffInDays <= 7) {
		                inwardRating = new BigDecimal(15);
		            } else if (diffInDays <= 14) {
		                inwardRating = new BigDecimal(10);
		            } else {
		                inwardRating = new BigDecimal(5);
		            }
		        }
				System.out.println("inwardRating ---> "+inwardRating+qHdrId+tenantId);
				String updateQry="UPDATE `quality_inspection_hdr`\r\n" + 
						"SET `INWARD_RATING`=?\r\n" + 
						"WHERE `QI_HDR_ID` = ? and TENANT_ID=?;";
				this.jdbcTemplate.update(updateQry,inwardRating,qHdrId,tenantId);
			}catch(Exception ex) {
				logger.error("updateInwardRating error "+ex);
			}
		}

		@Override
		public String getInwardRating(String qHdrId, String tenantId,String poDelDate,String insDate) {
			// TODO Auto-generated method stub
			String resp ="";
			try {
				BigDecimal inwardRating = BigDecimal.ZERO;

				Date poDeliveryDate=new SimpleDateFormat("yyyy-MM-dd").parse(poDelDate); 
			    Date inspectDate = new SimpleDateFormat("yyyy-MM-dd").parse(insDate); 
			    
			    if (inspectDate.compareTo(poDeliveryDate) <= 0) {
		            inwardRating = new BigDecimal(20);
		        } else {
		            long diffInMillis = inspectDate.getTime() - poDeliveryDate.getTime();
		            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

		            if (diffInDays <= 7) {
		                inwardRating = new BigDecimal(15);
		            } else if (diffInDays <= 14) {
		                inwardRating = new BigDecimal(10);
		            } else {
		                inwardRating = new BigDecimal(5);
		            }
		        }
			    resp = inwardRating.toString();
				System.out.println("inwardRating ---> "+inwardRating+qHdrId+tenantId);
				
			}catch(Exception ex) {
				logger.error("getInwardRating error "+ex);
			}
			return resp;
		}
		@Override
		public int updateVendorRating(UpdateVendorRatingRequest updateVendorRatingRequest) {
			// TODO Auto-generated method stub
			int check=0;
			try {
				String updateQry="UPDATE `quality_inspection_hdr`\r\n" + 
						"SET `SUPPLIER_RATING`=?,`OLD_SUPPLIER_RATING`=?,`LAST_UPDATED_BY`=?\r\n" + 
						"WHERE `QI_HDR_ID` = ? and TENANT_ID=?;";
			 check=	this.jdbcTemplate.update(updateQry,updateVendorRatingRequest.getSupplierValue(),updateVendorRatingRequest.getSupplierValue(),updateVendorRatingRequest.getEmpId(),
					 updateVendorRatingRequest.getQiHdrId(),updateVendorRatingRequest.getTenantId());
			}catch(Exception ex) {
				logger.error("updateVendorRating error "+ex);

			}
			return check;
		}

		@Override
		public int udpatepreinspectionqty(String projectId, String productCode, String locationCode,
				BigDecimal transQty, String operation, String invTransType, String transRefId, String transferredBy,
				String transDateTime, String tenantId) {
			int check=0;
			try {
				CommonMethod.updateProductInvDtl(projectId, productCode, locationCode, transQty, "Subraction",
						invTransType, transRefId, transferredBy, transDateTime, tenantId, jdbcTemplate);
			}catch(Exception ex) {
				logger.error("udpatepreinspectionqty error "+ex);

			}
			return check;
		}

		@Override
		public int updateCancelInspQualityStatus(String cancelFlag, String empId, String qualityHdrId,
				String tenantId) {
			int check=0;
			try {
				String updateQry="UPDATE `quality_inspection_hdr` \r\n" + 
						"SET `CANCEL_FLAG`=?,`LAST_UPDATED_DATETIME`= NOW(),`LAST_UPDATED_BY`=?,`SEQUENCE_STATUS` =?, `IS_COMPLETED` = ?\r\n" + 
						"WHERE `QI_HDR_ID` =? and `TENANT_ID`=? ";
			 check=	this.jdbcTemplate.update(updateQry,cancelFlag, empId,"DS018", 1, qualityHdrId, tenantId);
			}catch(Exception ex) {
				logger.error("updateCancelInspQualityStatus error "+ex);

			}
			return check;
		}

		@Override
		public String getCurrSeqForQInspection(String refId, String type) {
			String seq="";
			try {
				if(type.equalsIgnoreCase("1")) {
					String dateQry="select case when count(SEQUENCE_NO)>=1 \r\n" + 
							"then SEQUENCE_NO else 0 end as SEQUENCE_NO\r\n" + 
							" from quality_inspection_hdr where QI_ID = ?";
					Map<String,Object> resultMap = jdbcTemplate.queryForMap(dateQry, refId);
					seq = resultMap.get("SEQUENCE_NO").toString();
				}else {
					String dateQry="SELECT \r\n" + 
							"    CASE WHEN COUNT(QI_HDR_ID) >= 1 THEN MAX(QI_HDR_ID) ELSE 0 \r\n" + 
							"    END AS QI_HDR_ID\r\n" + 
							"FROM quality_inspection_hdr WHERE QI_ID = ?\r\n" + 
							"ORDER BY QI_HDR_ID DESC\r\n" + 
							"LIMIT 1;";
					Map<String,Object> resultMap = jdbcTemplate.queryForMap(dateQry, refId);
					seq = resultMap.get("QI_HDR_ID").toString();
				}
				
			}catch(Exception ex) {
				logger.error("getCurrSeqForQInspection error "+ex);
			}
			return seq;
		}

		@Override
		public String getPoId(String projectId, String refId, String tENANT_ID) {
			String poId=""; int cnt = 0;
			try {
				
				String dateQry=" select Count(PO_ID) as PO_ID\r\n" + 
						" from quality_inspection_request \r\n" + 
//						"	po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID  INNER JOIN  \r\n" + 
//						"	po_hdr phdr ON phdr.PO_ID = pdl.PO_ID\r\n" + 
						"	WHERE PM_HDR_ID = ? AND QI_ID = ?\r\n" + 
						"		 AND TENANT_ID = ? ";
				Map<String,Object> result = jdbcTemplate.queryForMap(dateQry, projectId, refId, tENANT_ID);
				cnt = Integer.valueOf(result.get("PO_ID").toString());
				
				if(cnt>0) {
					String Qry=" select PO_ID \r\n" + 
							" from quality_inspection_request \r\n" + 
//							"	po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID  INNER JOIN  \r\n" + 
//							"	po_hdr phdr ON phdr.PO_ID = pdl.PO_ID\r\n" + 
							"	WHERE PM_HDR_ID = ? AND QI_ID = ?\r\n" + 
							"		 AND TENANT_ID = ? ";
					Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, projectId, refId, tENANT_ID);
					poId = resultMap.get("PO_ID").toString();
				}
				
			}catch(Exception ex) {
				logger.error("getPoId error "+ex);
			}
			return poId;
		}

		@Override
		public int getCompletedCountByQiId(String qiId, String excludeQiHdrId, String sequenceNo) {
			int count = 0;
			try {
				String qry = "SELECT COUNT(*) FROM quality_inspection_hdr WHERE QI_ID = ? AND IS_COMPLETED = 1 AND QI_HDR_ID != ? AND SEQUENCE_NO = ?";
				count = jdbcTemplate.queryForObject(qry, Integer.class, qiId, excludeQiHdrId, sequenceNo);
			} catch (Exception ex) {
				logger.error("getCompletedCountByQiId error " + ex);
			}
			return count;
		}

}
