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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.finance.entity.PraStatusEntity;
import com.vmfg.finance.rowmapper.PraStatusRowMapper;
import com.vmfg.scm.dao.interfaces.IDebitNoteDAO;
import com.vmfg.scm.entity.DebitNoteDtlListEntity;
import com.vmfg.scm.entity.DebitNoteStatusEntity;
import com.vmfg.scm.entity.GetDebitNoteEntity;
import com.vmfg.scm.request.DebitNoteDtlRequest;
import com.vmfg.scm.request.DebitNoteHdrAndDtlRequest;
import com.vmfg.scm.rowmapper.DebitNoteDtlRowMapper;
import com.vmfg.scm.rowmapper.GetDebitNoteHdrRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class DebitNoteDAO implements IDebitNoteDAO {
	private static final Logger logger = LoggerFactory.getLogger(PoDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insertDebitNoteHdrAndDtl(DebitNoteHdrAndDtlRequest debitNoteRequest, String debitCode) {
		int insertRes = 0;
           try {
        	   String insertDnHdr = "INSERT INTO debit_note (PM_HDR_ID,PO_ID,VENDOR_CODE,DNR_ID,DN_VALUE,SEQUENCE_NO,SEQUENCE_STATUS,CREATED_BY,LAST_UPDATED_BY,LAST_UPDATED_DATETIME,TENANT_ID,DN_CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
   			KeyHolder holder = new GeneratedKeyHolder();

   			this.jdbcTemplate.update(new PreparedStatementCreator() {

   				@Override
   				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
   					PreparedStatement ps = con.prepareStatement(insertDnHdr, Statement.RETURN_GENERATED_KEYS);

   					ps.setString(1, debitNoteRequest.getPmHdrId());
   					ps.setString(2, debitNoteRequest.getPoId());
   					ps.setString(3, debitNoteRequest.getVendorCode());
   					ps.setString(4, debitNoteRequest.getDnrId());
   					ps.setString(5, debitNoteRequest.getDnValue());
   					ps.setString(6, debitNoteRequest.getSeq());
   					ps.setString(7, debitNoteRequest.getSeqStatus());
   					ps.setString(8, debitNoteRequest.getEmpId());
   					ps.setString(9, debitNoteRequest.getEmpId());
   					ps.setString(10, CommonMethod.getCurrentDateTime());
   					ps.setString(11, debitNoteRequest.getTenantId());
   					ps.setString(12, debitCode);
   					return ps;
   				}

   			}, holder);
   			insertRes = holder.getKey().intValue();
   			
   			if(insertRes > 0) {
   				
   				for(DebitNoteDtlRequest dtl : debitNoteRequest.getDebitNoteDtl()) {
   					
   				 String insertDnDtl = "INSERT INTO debit_note_dtl (DN_ID, PO_DTL_ID) VALUES(?,?);";	
   	   			KeyHolder holder1 = new GeneratedKeyHolder();
                String dnId = String.valueOf(insertRes);
   	   			    this.jdbcTemplate.update(new PreparedStatementCreator() {

   	   				     @Override
   	   				     public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
   	   					     PreparedStatement ps = con.prepareStatement(insertDnDtl, Statement.RETURN_GENERATED_KEYS);
   	   					        ps.setString(1, dnId);
   	   					        ps.setString(2, dtl.getPoDtlId());
   	   					        return ps;
   	   				     }

   	   			    }, holder1);
   	   			    
   	   			 holder1.getKey().intValue();
   	   			
   				}
   			}
   			
           }catch (Exception e) {
   			logger.error("insertDebitNoteHdrAndDtlDAO method Error" + e);
   		}
		return insertRes;
	}

	@Override
	public int updateIndentQtyByPoDtlQty(DebitNoteDtlRequest debitNoteDtlRequest, String poDtlQty) {

		int update = 0;
		String sql = "UPDATE indent_grp_dtl igd " +
				"JOIN indent_grp_scs igs ON igd.IG_HDR_ID = igs.IG_HDR_ID " +
				"JOIN po_hdr ph ON ph.IG_SCS_ID = igs.IG_SCS_ID " +
				"JOIN po_dtl pd ON pd.PO_ID = ph.PO_ID " +
				"   AND pd.indent_dtl_id = igd.INDENT_DTL_ID " +
				"SET igd.INVENTORY = igd.INVENTORY - ?, " +
				"    igd.QTY       = igd.QTY - ? " +
				"WHERE pd.po_dtl_id = ? " +
				"  AND igd.INVENTORY = ? " +
				"  AND igd.QTY = ?";

		try {
			BigDecimal qty = new BigDecimal(poDtlQty);
			String poDtlId = debitNoteDtlRequest.getPoDtlId();

			update =  jdbcTemplate.update(sql,
					qty,        // INVENTORY - qty
					qty,        // QTY - qty
					poDtlId,    // filter by po_dtl_id
					qty,        // INVENTORY >= qty
					qty         // QTY >= qty
			);
		} catch (Exception e) {
			logger.error("Error updating indent quantities for po_dtl_id: "
					+ debitNoteDtlRequest.getPoDtlId(), e);
		}
		return update;
	}

	@Override
	public int insertDebitNoteReason(String dnReason, String tenantId){
		int insertRes=0;
		try{
			String insertDnReason = "INSERT INTO debit_note_reason (DNR_REASON,IS_ACTIVE,TENANT_ID) VALUES(?,?,?);";
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertDnReason, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, dnReason);
					ps.setString(2, "1");
					ps.setString(3, tenantId);
					return ps;
				}
			}, holder);
			insertRes = holder.getKey().intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return insertRes;
	}

	@Override
	public int updateDebitNoteHdr(String seq, String seqStatus, String isLast, String currentDateTime, String empId,
			String dnId) {
		int res=0;
		try {
			if(isLast.equalsIgnoreCase("0")) {
			String qry="UPDATE `debit_note` SET `SEQUENCE_NO`= ? , `SEQUENCE_STATUS`= ?, `LAST_UPDATED_DATETIME` = ?, `LAST_UPDATED_BY` = ? WHERE `DN_ID`=? ";
			res = this.jdbcTemplate.update(qry,seq,seqStatus,currentDateTime,empId,dnId);
			}else {
				String qry="UPDATE `debit_note` SET `SEQUENCE_NO`= ?, `SEQUENCE_STATUS`=?, `IS_COMPLETED`= ? , `LAST_UPDATED_DATETIME`= ?, `LAST_UPDATED_BY` = ? WHERE `DN_ID`=?  ";
				res = this.jdbcTemplate.update(qry,seq,seqStatus,isLast,currentDateTime,empId,dnId);
			}
		}catch(Exception ex) {
			logger.error("updateDebitNoteHdr Method Exception "+ex);
		}
		return res;
	}

	@Override
	public int insertDebitNoteStatusDtl(String dnId, String seq, String seqStatus, String tenantId, String remarks,
			String empId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO debit_note_status_dtl ( DN_ID, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
			insertStatus = this.jdbcTemplate.update(qry, dnId, seq, seqStatus, remarks, empId, tenantId);

		} catch (Exception ex) {
			logger.error("insertPraStatus method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public List<GetDebitNoteEntity> getDebitNoteHdrListByPmHdrId(String pmHdrId, String poId, String tenantId) {
		List<GetDebitNoteEntity>list = new ArrayList<GetDebitNoteEntity>();
		try {
			String query = "SELECT \r\n" + 
					"    DN_ID,\r\n" + 
					"    DN_VALUE,\r\n" + 
					"    DNR_REASON,\r\n" + 
					"    PROJECT_CODE,\r\n" + 
					"    phdr.PROJECT_NAME,\r\n" +
					"    PO_CODE,\r\n" +
					"    hdr.PO_ID,\r\n" + 
					"    vm.VENDOR_NAME,\r\n" + 
					"    vm.VENDOR_CODE,\r\n" +
					"    hdr.CREATED_BY,\r\n" +
					"    hdr.TENANT_ID,\r\n" + 
					"    TOTAL_VALUE,\r\n" + 
					"    TOTAL_VALUE_FX, hdr.SEQUENCE_NO,\r\n" + 
					"    hdr.SEQUENCE_STATUS,\r\n" +
					"	 dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" +
					"	 poh.*\r\n" +
					"FROM\r\n" +
					"    debit_note hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    debit_note_reason dnr ON dnr.DNR_ID = hdr.DNR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_mst vm ON vm.VENDOR_CODE = hdr.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = hdr.SEQUENCE_STATUS\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON phdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_hdr poh ON poh.PO_ID = hdr.PO_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.PO_ID LIKE ?\r\n" + 
					"        AND hdr.PM_HDR_ID LIKE ?\r\n" +
					"        AND hdr.TENANT_ID = ?;";
			list = this.jdbcTemplate.query(query, new GetDebitNoteHdrRowMapper(), poId,pmHdrId,tenantId);
		
		}catch(Exception e){
			logger.error("getDebitNoteHdrListByPmHdrId Error" + e);
		}
		return list;
	}

	@Override
	public List<DebitNoteDtlListEntity> getDebitNoteSubList(String dnId) {
		// TODO Auto-generated method stub
		List<DebitNoteDtlListEntity> dtlList = new ArrayList<DebitNoteDtlListEntity>();
		try {
			String query = "SELECT \r\n" + 
					"    dtl.*, idtl.product_code\r\n" + 
					"FROM\r\n" + 
					"    debit_note_dtl dndtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_dtl dtl ON dndtl.PO_DTL_ID = dtl.PO_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl idtl ON idtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
					"WHERE\r\n" + 
					"    DN_ID = ? ";
			dtlList = this.jdbcTemplate.query(query, new DebitNoteDtlRowMapper(), dnId);
					
		}catch(Exception e) {
			logger.error("getDebitNoteSubList error" + e);
		}
		return dtlList;
	}
	
	@Override
	public List<DebitNoteStatusEntity> getDebitNoteStatusList(String dnId){
		List<DebitNoteStatusEntity> debitNoteStatusEntityList = new ArrayList<>();
		try{
			String qry = "SELECT \n" +
					"    dns.*, \n" +
					"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION, \n" +
					"    emp.EMPLOYEE_FIRSTNAME\n" +
					"FROM \n" +
					"    debit_note_status_dtl dns\n" +
					"INNER JOIN \n" +
					"    document_status_type_code doc ON dns.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\n" +
					"INNER JOIN \n" +
					"    employee_mst emp ON dns.UPDATED_BY = emp.EMPLOYEE_ID\n" +
					"WHERE \n" +
					"    dns.DN_ID = ?\n";
			debitNoteStatusEntityList = this.jdbcTemplate.query(qry, new DebitNoteStatusRowMapper(), dnId);
		}
		catch (Exception ex){
			logger.error("getPraStatusList Method Exception --->" + ex);
		}
		return  debitNoteStatusEntityList;
	}

	@Override
	public int getDmIdByLatestVerionForDebit(String refernceId, String docType, String tenantId) {
		int dmId = 0;
		try {
			String checkCountStr = "select count(*) DM_ID_COUNT from document_management where REFERENCE_ID=?"
					+ " and UPLOAD_DOC_TYPE='FC015' and DOC_TYPE_CODE=? and TENANT_ID=? order by VERSION desc limit 1;";
			Map<String, Object> results = this.jdbcTemplate.queryForMap(checkCountStr,refernceId,docType,tenantId);
			int checkCount =  Integer.parseInt(results.get("DM_ID_COUNT").toString());
			if(checkCount>0) {
				String getCount = "select  DM_ID AS DM_ID from document_management where REFERENCE_ID=?"
						+ " and UPLOAD_DOC_TYPE='FC015' and DOC_TYPE_CODE=? and TENANT_ID=? order by VERSION desc limit 1;";
				Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,refernceId,docType,tenantId);
				dmId = Integer.parseInt(result.get("DM_ID").toString());
			}
		} catch (Exception ex) {
			logger.error("getDmIdByLatestVerion Method Exception --->" + ex);

		}
		return dmId;
	}

	@Override
	public String getLastDebitCode() {
		String debitCode="";
		try {
			String qry="SELECT \r\n" +
					"    CASE \r\n" +
					"        WHEN COUNT(*) > 0 THEN (SELECT DN_CODE FROM debit_note ORDER BY DN_ID DESC LIMIT 1)\r\n" +
					"        ELSE 'DN001'\r\n" +
					"    END AS DN_CODE\r\n" +
					"FROM \r\n" +
					"    debit_note;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
			debitCode = resultMap.get("DN_CODE").toString();
		}catch(Exception ex) {
			logger.error("getLastDebitCode Method Exception "+ex);
		}
		return debitCode;
	}
}
