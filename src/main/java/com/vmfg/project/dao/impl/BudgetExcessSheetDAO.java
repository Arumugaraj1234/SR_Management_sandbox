package com.vmfg.project.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.rowmapper.DocLifeCycleMstTableRowMapper;
import com.vmfg.general.rowmapper.DocumentStatusMstRowMapper;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.entity.BudgetExcessAssSubAssEntity;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.GetIndentBudgetDtlsEntity;
import com.vmfg.project.entity.RetriveBudgetExcessStatusDtlEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.project.response.BudgetExcessBasedIndentHdrDtl;
import com.vmfg.project.rowmapper.BudgetExcessAssSubAssRowMapper;
import com.vmfg.project.rowmapper.BudgetExcessStatusRowMapper;
import com.vmfg.project.rowmapper.GetIndentBudgetDtlsRowMapper;
import com.vmfg.project.rowmapper.RetriveBudgetExcessSheetDtlRowMapper;
import com.vmfg.project.rowmapper.SalesCategoryDtlRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class BudgetExcessSheetDAO implements IBudgetExcessSheetDAO {
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessSheetDAO.class);
	@Autowired
	private StageManagementDAO stageManagementDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	private IndentGroupDAO indentGroupDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDao;
	@Override
	public int updateBudgetExcessSheetDtl(updateBudgetExcessSheetRequest updateudget) {
		logger.debug("updateBudgetExcessSheetDtl   method Start");
		int resp = 0;
		
		try {
//			String docStatsCode=indentUploadDao.getStatusCodebySeqAndDocType(updateudget.getSequenceNo(), updateudget.getTenantId(), "DC039");
		
			if (updateudget.getSequenceNo().isEmpty()) {
				String insertBudgetQ = "UPDATE budget_excess_dtl \r\n" + "SET \r\n" + "  REASON = ?,"
						+ "    ROOT_CAUSE = ?, "  
					    + "    ACTION = ?,   RESPONSIBLE = ?, "
					    + "    UPDATED_BY = ?,UPDATED_ON = ? "
					    + "WHERE\r\n" + "    BE_HDR_ID = ?";

			int insertBudgetCount =	this.jdbcTemplate.update(insertBudgetQ, updateudget.getReason(), updateudget.getRootCase(), updateudget.getAction(), 
					updateudget.getResponseDept(), updateudget.getEmpId(), CommonMethod.getCurrentDateTime(), updateudget.getBeHdrId());
				if (insertBudgetCount > 0) {
					resp = 200;
				} else {
					resp = 0;
				}
			} else {
//				int currBudgetExcDtlSeq= jdbcTemplate.queryForObject("Select Count(*) from budget_excess_dtl where  BE_HDR_ID = '"+updateudget.getBeHdrId()+"' and SEQUENCE_NO = 1 ", int.class);
//				String insertBudgetQ = "UPDATE budget_excess_dtl \r\n" + "SET \r\n" + "    REASON = '"
//						+ updateudget.getReason() + "',\r\n" + "    ROOT_CAUSE = '" + updateudget.getRootCase()
//						+ "',\r\n" + "    ACTION = '" + updateudget.getAction() + "',\r\n" + "    RESPONSIBLE = '"
//						+ updateudget.getResponseDept() + "',\r\n" + "    SEQUENCE_STATUS = '"
//						+ updateudget.getApprovingStatus() + "',\r\n" + "    SEQUENCE_NO = '"
//						+ updateudget.getSequenceNo() + "',\r\n" + "    UPDATED_BY = '" + updateudget.getEmpId()  +"',UPDATED_ON = '"+CommonMethod.getCurrentDateTime()+"' "
//						+ "\r\n" + "WHERE\r\n" + "    BE_HDR_ID = '" + updateudget.getBeHdrId() + "'";
//
//				int budegetRes = this.jdbcTemplate.update(insertBudgetQ);
//				if (budegetRes > 0) {
//					if(currBudgetExcDtlSeq>0 && updateudget.getSequenceNo().equalsIgnoreCase("6")) {
//						String indGrpId= jdbcTemplate.queryForObject("Select IG_SCS_ID from budget_excess_dtl where  BE_HDR_ID = '"+updateudget.getBeHdrId()+"' ", String.class);
//						List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC038","1", updateudget.getTenantId());
//						if(currSeqDocLifeCycleMstList.size()>0) {
//							indentGroupDAO.updateScpSeqAndStatus(indGrpId,currSeqDocLifeCycleMstList.get(0).getCurrSequence(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),updateudget.getEmpId());
//						}
//						String indentId= jdbcTemplate.queryForObject("Select INDENT_ID from budget_excess_dtl where  BE_HDR_ID = '"+updateudget.getBeHdrId()+"' ", String.class);
//						String budIndentCode = GetPropertyValue.getPropValueByTenant(updateudget.getTenantId(), "BUDGET_INDENT_REJECTION_CODE",
//								this.jdbcTemplate);
//						List<DocumentStatusMstEntity> currSeqDocindentCode = stageManagementDAO.getDocDtlcurrentSeq("DC018",budIndentCode, updateudget.getTenantId());
//						if(currSeqDocindentCode.size()>0) {
//							String nextApprDesig = stageManagementDAO.getNextApprDesigByDocType("DC018", currSeqDocindentCode.get(0).getCurrSequence(), updateudget.getTenantId());
//							indentUploadDao.updateIndentHdrStatusAndDesig(indentId, currSeqDocindentCode.get(0).getCurrSequence(), currSeqDocindentCode.get(0).getDocStatus(), nextApprDesig,updateudget.getEmpId());
//						}
//					}
//					List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC039",updateudget.getSequenceNo(), updateudget.getTenantId());
//					if(currSeqDocLifeCycleMstList.size()>0) {
//							if (currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
//								updateBudgetSheetExcessApproved(updateudget.getBeHdrId());
//							}
//					}
//					String insertBudgetStatusQ = "INSERT INTO budget_excess_status_dtl (BE_HDR_ID, SEQUENCE_NO,SEQUENCE_STATUS,REMARKS,UPDATED_BY,UPDATED_ON,TENANT_ID ) "
//							+ "VALUES (?,?,?,?,?,NOW(),?)";
//
//					int budegetStatusRes = this.jdbcTemplate.update(insertBudgetStatusQ, updateudget.getBeHdrId(),
//							updateudget.getSequenceNo(), updateudget.getApprovingStatus(), updateudget.getRemarks(),
//							updateudget.getEmpId(), updateudget.getTenantId());
//
//					if (budegetStatusRes > 0) {
//						resp = 200;
//					} else {
//						resp = 0;
//					}
//
//				} else {
//					resp = 0;
//				}
			}

		} catch (Exception ex) {
			logger.error("updateBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("updateBudgetExcessSheetDtl   method end");
		return resp;

	}
	
	@Override
	public int UpdateIndentAndScsStatus(String currSeq,String beHdrId,String tenantId,String empId, String docGrp, String processCode) {
		int excessValue =0;
		try {
			logger.debug("UpdateIndentAndScsStatus   method Start");
			
			Map<String,Object> resultData = jdbcTemplate.queryForMap("Select Count(*) AS COUNT from budget_excess_dtl where  BE_HDR_ID = ? and SEQUENCE_NO = 1 ", beHdrId);
			int currBudgetExcDtlSeq = Integer.parseInt(resultData.get("COUNT").toString());
			String budIndentCode ="";
			if(processCode.equalsIgnoreCase("3")) {
				budIndentCode = GetPropertyValue.getPropValueByTenant(tenantId, "BUDGET_INDENT_REJECTION_CODE",
						this.jdbcTemplate);
			}else {
			   budIndentCode = GetPropertyValue.getPropValueByTenant(tenantId, "CAPEX_BUDGET_INDENT_REJECTION_CODE",
						this.jdbcTemplate);
			}
			
			
				if(currBudgetExcDtlSeq>0 && currSeq.equalsIgnoreCase(budIndentCode)) {
					
//					Map<String,Object> resultMap = jdbcTemplate.queryForMap("Select IG_SCS_ID from budget_excess_dtl where  BE_HDR_ID = ? ", beHdrId);
//					String indGrpId= resultMap.get("IG_SCS_ID").toString();
							
//					List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC038","1",tenantId);
//					if(currSeqDocLifeCycleMstList.size()>0) {
//						indentGroupDAO.updateScpSeqAndStatus(indGrpId,currSeqDocLifeCycleMstList.get(0).getCurrSequence(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),empId);
//					}
					
					
					Map<String,Object> resultID = jdbcTemplate.queryForMap("Select INDENT_ID from budget_excess_dtl where  BE_HDR_ID = ? ", beHdrId);
							String indentId= resultID.get("INDENT_ID").toString();
							
					Map<String,Object> resultCode = jdbcTemplate.queryForMap("Select INDENT_TYPE_CODE from indent_hdr where  INDENT_ID = ? ", indentId);
							String indentType=resultCode.get("INDENT_TYPE_CODE").toString();
							
					Map<String,Object> result = jdbcTemplate.queryForMap("\r\n" + 
							"SELECT \r\n" + 
							"    BUDGET_EXCESS_SEQ\r\n" + 
							"FROM\r\n" + 
							"    indent_hdr hdr\r\n" + 
							"        INNER JOIN\r\n" + 
							"    indent_type_mst mst ON hdr.INDENT_TYPE_CODE = mst.INDENT_TYPE_CODE\r\n" + 
							"WHERE\r\n" + 
							"    INDENT_ID = ? and hdr.TENANT_ID=? ", indentId,tenantId);
							String indentSeq=result.get("BUDGET_EXCESS_SEQ").toString();		
//					String budIndentCode = GetPropertyValue.getPropValueByTenant(tenantId, "BUDGET_INDENT_REJECTION_CODE",
//							this.jdbcTemplate);
					
//					List<DocumentStatusMstEntity> currSeqDocindentCode = stageManagementDAO.getDocDtlcurrentSeq("DC018",budIndentCode, tenantId);
					List<DocumentStatusMstEntity> currSeqDocindentCode = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC018",indentSeq, tenantId, indentType);
					
					if(currSeqDocindentCode.size()>0) {
						String nextApprDesig = stageManagementDAO.getNextApprDesigByDocGrp("DC018", currSeqDocindentCode.get(0).getCurrSequence(), tenantId,indentType);
						indentUploadDao.updateIndentHdrStatusAndDesig(indentId, currSeqDocindentCode.get(0).getCurrSequence(), currSeqDocindentCode.get(0).getDocStatus(), nextApprDesig, empId, tenantId);
					}
				}
				
//				List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC039",currSeq,tenantId);
				List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC039", currSeq, tenantId, docGrp, processCode);
				if(currSeqDocLifeCycleMstList.size()>0) {
						if (currSeqDocLifeCycleMstList.get(0).getLastSeq()!=null && currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
							updateBudgetSheetExcessApproved(beHdrId);
						}
				}
		} catch (Exception ex) {
			logger.error("UpdateIndentAndScsStatus Error" + ex);
		}
		logger.debug("UpdateIndentAndScsStatus   method Start");
		return excessValue;
	}


	@Override
	public List<BudgetExcessSheetEntity> retriveBudgetExcessSheetDtl(BudgetExcessStatusDtlReq statusDtlReq) {
		logger.debug("retriveBudgetExcessSheetDtl   method Start");
		List<BudgetExcessSheetEntity> totalList = new ArrayList<>();
		try {
			if(statusDtlReq.getPmHdrId() == null) {
				statusDtlReq.setPmHdrId(indentUploadDao.getprojIdByEnqId(statusDtlReq.getEnqId()));
			}

			String totalListQ = "SELECT \r\n" + "    bed.*,\r\n" + "    em.EMPLOYEE_FIRSTNAME,\r\n"
					+ "    ih.INDENT_CODE,\r\n" + "    ph.PROJECT_CODE,\r\n" + "    ph.PROJECT_NAME,\r\n" +" bed.IS_COMPLETED,\r\n"
					+ "    vm.VENDOR_NAME,\r\n" + "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,dp.DEPARTMENT_NAME\r\n" + "FROM\r\n"
					+ "    budget_excess_dtl AS bed\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst AS em ON bed.UPDATED_BY = em.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    indent_hdr AS ih ON ih.INDENT_ID = bed.INDENT_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr AS ph ON ph.PM_HDR_ID = bed.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    vendor_mst AS vm ON vm.VENDOR_CODE = bed.VENDOR_CODE\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code AS dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = bed.SEQUENCE_STATUS LEFT JOIN department dp ON dp.DEPARTMENT_CODE = bed.RESPONSIBLE \r\n"
					+ "WHERE\r\n" + "    bed.PM_HDR_ID = '" + statusDtlReq.getPmHdrId() + "'\r\n"
					+ "        AND bed.TENANT_ID = '" + statusDtlReq.getTenantId() + "'";

			totalList = this.jdbcTemplate.query(totalListQ, new RetriveBudgetExcessSheetDtlRowMapper());

		} catch (Exception ex) {
			logger.error("retriveBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("retriveBudgetExcessSheetDtl   method end");
		return totalList;

	}
	
	@Override
	public List<DocumentStatusMstEntity> getDocDtlcurrentSeqByDocGrp(String referenceDoc, String seq, String tenantId,String docGroup) {
		List<DocumentStatusMstEntity> documentNextSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String nextSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "    where\r\n" + "     dsm.DOC_TYPE = ? and dsm.CURR_SEQUENCE = ? and dsm.TENANT_ID = ? and DOC_GROUP=? ";
			documentNextSeqList = this.jdbcTemplate.query(nextSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, seq, tenantId,docGroup);
		} catch (Exception ex) {
			logger.error("getDocDtlcurrentSeqByDocGrp Error" + ex);
		}
		return documentNextSeqList;
	}
	
	@Override
	public List<DocumentStatusMstEntity> getNextSeqandStatusByDocGrp(int currentSeq, String docType, String tenantId, String docGroup) {
		String NEXT_SEQ = "";
		List<DocumentStatusMstEntity> getLi = null;
		try {
			String qry = "select NEXT_SEQ from document_lifecycle_mst where DOC_TYPE= ? and TENANT_ID= ?\r\n"
					 + " and CURR_SEQUENCE = ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(qry, docType, tenantId, currentSeq);
			NEXT_SEQ = resultData.get("NEXT_SEQ").toString();

			qry = "select * from document_lifecycle_mst where DOC_TYPE='" + docType + "' and TENANT_ID='" + tenantId
					+ "' and CURR_SEQUENCE ='" + NEXT_SEQ + "' and DOC_GROUP='"+docGroup+"' ";
			getLi = this.jdbcTemplate.query(qry, new DocLifeCycleMstTableRowMapper());

		} catch (Exception ex) {
			logger.error("checkForNextSeq Error" + ex);
		}
		return getLi;

	}

	@Override
	public List<RetriveBudgetExcessStatusDtlEntity> getBudegetStatusDtl(String brHdrId, String tenantId) {
		List<RetriveBudgetExcessStatusDtlEntity> statusList = new ArrayList<>();
		try {
			// StatusList
			String statusListQ = "SELECT DISTINCT\r\n"
					+ "    besd.*, dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS DOCUMENT_STATUS_TYPE_DESCRIPTION,em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME\r\n"
					+ "FROM\r\n" + "\r\n" + "   \r\n" + "    budget_excess_status_dtl AS besd\r\n"
					+ "        LEFT JOIN\r\n"
					+ "    document_lifecycle_mst AS dlm ON besd.SEQUENCE_STATUS = dlm.DOC_STATUS\r\n"
					+ "        LEFT JOIN\r\n"
					+ "    document_status_type_code AS dstc ON dlm.DOC_STATUS = dstc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "      LEFT JOIN\r\n"
					+ "   employee_mst AS em ON besd.UPDATED_BY = em.EMPLOYEE_ID where BE_HDR_ID='" + brHdrId + "' AND dlm.TENANT_ID='"+tenantId+"' order by UPDATED_ON DESC";

			statusList = this.jdbcTemplate.query(statusListQ, new BudgetExcessStatusRowMapper());
		} catch (Exception ex) {
			logger.error("getBudegetStatusDtl  method  exception" + ex);
		}
		logger.debug("getBudegetStatusDtl   method end");
		return statusList;
	}

	@Override
	public BudgetExcessBasedIndentHdrDtl getIndentHdrDetail(String indentId) {
		logger.debug("BudgetExcessBasedIndentHdrDtl   method Start");
		BudgetExcessBasedIndentHdrDtl hdrDtl = new BudgetExcessBasedIndentHdrDtl();
		try {

			String query = "SELECT \r\n" + " INDENT_ID,PROJECT_ID,TARGET_VALUE,SCM_BUDGET_ALLOCATED,PKSA_ID\r\n"
					+ "FROM\r\n" + "  indent_hdr\r\n" + "WHERE\r\n" + "INDENT_ID = ?";

			Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(query, indentId);
			hdrDtl.setIndentId(resultMap.get("INDENT_ID").toString());
			hdrDtl.setPmHdrId(resultMap.get("PROJECT_ID").toString());
			hdrDtl.setScmBudAllocatedValue(resultMap.get("SCM_BUDGET_ALLOCATED").toString());
			hdrDtl.setDskId(resultMap.get("PKSA_ID").toString());
			hdrDtl.setTargetValue(resultMap.get("TARGET_VALUE").toString());
			
		} catch (Exception ex) {
			logger.error("BudgetExcessBasedIndentHdrDtl  method  exception" + ex);
		}
		logger.debug("BudgetExcessBasedIndentHdrDtl   method end");
		return hdrDtl;
	}

	@Override
	public List<DocumentStatusMstEntity> getDocCurrentSeqDtl(String docType, String seqNo, String tenantID, String processDoc) {
		logger.debug("getDocCurrentSeqDtl   method Start");
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<>();
		try {
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqForBudgetExcess(docType, seqNo, tenantID, processDoc);

		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl  method  exception" + ex);
		}
		logger.debug("getDocCurrentSeqDtl   method end");
		return currSeqDocLifeCycleMstList;
	}

	@Override
	public int insertBudgetExcessSheetDtl(String indentId, String pmHdrId, String budgetValue,
			String scmBudAllocatedValue, String excess, String vendor, String reason, String rootCase, String action,
			String responsible, String seqNo, String docStatus, String updatedBy, String tenantID, String dskId,String igScsId , String scsActualCost) {
		logger.debug("insertBudgetExcessSheetDtl   method Start");
		int insertRes = 0;
		try {

			// Guarded as a single atomic INSERT...SELECT...WHERE NOT EXISTS instead of a
			// separate check-then-insert, to close the race window where two near-simultaneous
			// "Raise Budget Excess" clicks (or the legacy auto-create path) could both pass a
			// prior getBudgetExcessDtlCount() check before either commits, creating duplicate
			// entries for the same IG_SCS_ID. Mirrors getBudgetExcessDtlCount's exact condition
			// (SEQUENCE_NO != 6 doesn't count as "already raised") so existing behavior for
			// re-raising after a resolved/superseded entry is unchanged. If the NOT EXISTS check
			// fails, zero rows are inserted and holder.getKey() throws, which the existing
			// catch-and-return-0 below already handles as a no-op failure.
			String insertBudgetQ = "INSERT INTO budget_excess_dtl (INDENT_ID, PM_HDR_ID, BUDGET_COST, ACTUAL_COST,EXCESS,  VENDOR_CODE,REASON,"
					+ " ROOT_CAUSE, ACTION, RESPONSIBLE, SEQUENCE_NO,SEQUENCE_STATUS, UPDATED_BY, UPDATED_ON, TENANT_ID,PKSA_ID,IG_SCS_ID, ACTUAL_EXCESS) "
					+ "SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,? FROM DUAL "
					+ "WHERE NOT EXISTS (SELECT 1 FROM budget_excess_dtl WHERE IG_SCS_ID = ? AND SEQUENCE_NO != 6)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertBudgetQ, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, indentId);
					ps.setString(2, pmHdrId);
					ps.setString(3, budgetValue);
					ps.setString(4, scmBudAllocatedValue);
					ps.setString(5, excess);
					ps.setString(6, vendor);
					ps.setString(7, reason);
					ps.setString(8, rootCase);
					ps.setString(9, action);
					ps.setString(10, responsible);
					ps.setString(11, seqNo);
					ps.setString(12, docStatus);
					ps.setString(13, updatedBy);
					ps.setString(14, tenantID);
					ps.setString(15, dskId);
					ps.setString(16,igScsId );
					ps.setString(17,scsActualCost );
					ps.setString(18, igScsId);

					return ps;
				}
			}, holder);
			insertRes = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("insertBudgetExcessSheetDtl   method end");
		return insertRes;

	}

	@Override
	public int insertBudgetExcessStatus(int beHdrId, String seqNo, String docStatus, String remarks, String updatedBy,
			String tenantID) {
		logger.debug("insertBudgetExcessStatus   method Start");
		int insertBudgetSheetStatus = 0;
		try {

			String insertBudgetStatusQ = "INSERT INTO budget_excess_status_dtl (BE_HDR_ID, SEQUENCE_NO,SEQUENCE_STATUS,REMARKS,UPDATED_BY,UPDATED_ON,TENANT_ID ) "
					+ "VALUES (?,?,?,?,?,NOW(),?)";

			insertBudgetSheetStatus = this.jdbcTemplate.update(insertBudgetStatusQ, beHdrId, seqNo, docStatus, remarks,
					updatedBy, tenantID);

		} catch (Exception ex) {
			logger.error("insertBudgetExcessStatus  method  exception" + ex);
		}
		logger.debug("insertBudgetExcessStatus   method end");
		return insertBudgetSheetStatus;
	}

	@Override
	public List<SalesCategoryDtlEntity> getSalesCatagoryDtl(IndentBudgetDtlReq indentBudgetDtl) {
		logger.debug("getIndentBudgetDtl   method Start");

		List<SalesCategoryDtlEntity> sbcCodeList = new ArrayList<>();
		try {

			String sbcQuery = "SELECT \r\n" + "   sbc.SBC_CODE, sbc.SBC_DESC,\r\n" + "    SUM(ih.BUDGET_VALUE) AS BUDGET_VALUE,\r\n"
					+ "    SUM(ih.SCM_BUDGET_ALLOCATED) AS SCM_BUDGET_ALLOCATED\r\n" + "    \r\n" + "FROM\r\n"
					+ "    indent_hdr AS ih\r\n"
					+ "   INNER  JOIN sales_budget_category AS sbc ON sbc.SBC_CODE=ih.SBC_CODE\r\n" + "    \r\n"
					+ "WHERE\r\n" + "   ih. PROJECT_ID = '" + indentBudgetDtl.getPmHdrId() + "' AND ih.TENANT_ID = '"
					+ indentBudgetDtl.getTenantId() + "'\r\n" + "GROUP BY sbc.SBC_CODE";
			sbcCodeList = jdbcTemplate.query(sbcQuery, new SalesCategoryDtlRowMapper());

		} catch (Exception ex) {
			logger.error("getSalesCatagoryDtl  method  exception" + ex);
		}
		logger.debug("getSalesCatagoryDtl   method end");
		return sbcCodeList;
	}

	@Override
	public List<GetIndentBudgetDtlsEntity> getIndentBudgetDtl(String sbcCode, String pmHdrId) {
		logger.debug("getIndentBudgetDtl   method Start");
		List<GetIndentBudgetDtlsEntity> list = new ArrayList<>();
		try {

			String query = "SELECT DISTINCT\r\n" + 
					"    hdr.INDENT_CODE,\r\n" + 
					"     pk.PK_DESC,\r\n" + 
					"    pskam.PSK_DESC,\r\n" + 
					"    hdr.CREATED_DATE\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr AS hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area pska ON hdr.PKSA_ID = pska.PKSA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area_mst pskam ON pskam.PSK_ID = pska.PSK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_mst AS pkam ON pka.PK_ID = pkam.PK_ID\r\n" + 
					"WHERE\r\n" + 
					"    SBC_CODE = ?\r\n" + 
					"        AND PROJECT_ID = ?";
			list = this.jdbcTemplate.query(query, new GetIndentBudgetDtlsRowMapper());
		} catch (Exception ex) {
			logger.error("getIndentBudgetDtl  method  exception" + ex);
		}
		logger.debug("getIndentBudgetDtl   method end");
		return list;
	}

	@Override
	public int updateBudgetSheetExcessSeqAndStatus(String budegtSheetDtlId, String currentseq, String docStatus,String tenantId,String empId) {
		int updateStatus = 0;
		try {
			
			
			String qry = "UPDATE budget_excess_dtl SET SEQUENCE_NO=?, SEQUENCE_STATUS=?, UPDATED_BY=?, UPDATED_ON=? WHERE BE_HDR_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, currentseq, docStatus,empId,CommonMethod.getCurrentDateTime(), budegtSheetDtlId);

		} catch (Exception ex) {
			logger.error("updateBudgetSheetExcessSeqAndStatus method Error" + ex);
		}
		return updateStatus;

	}


	@Override
	public int insertBudgetSheetExcessStatusStatusDtl(String budegtSheetDtlId, String currentseq, String docStatus,
			String tenantId, String remarks, String empId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO budget_excess_status_dtl (BE_HDR_ID, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
			insertStatus = this.jdbcTemplate.update(qry, budegtSheetDtlId, currentseq, docStatus, remarks, empId, tenantId);

		} catch (Exception ex) {
			logger.error("insertBudgetSheRetExcessStatusStatusDtl method Error" + ex);
		}
		return insertStatus;
	}

	
	@Override
	public void updateBudgetSheetExcessApproved(String budegtSheetDtlId) {
		try {
			String qry = "UPDATE budget_excess_dtl SET IS_COMPLETED='1' WHERE BE_HDR_ID=?";
			this.jdbcTemplate.update(qry, budegtSheetDtlId);

		} catch (Exception ex) {
			logger.error("updateBudgetSheetExcessApproved method Error" + ex);
		}

	}
	
	@Override
	public List<String> getDistinctDocGroup(String docType,String tenantId, String processCode) {
		List<String> docGrp = new ArrayList<>();
		try {
			String Qry = "select distinct(DOC_GROUP) from document_lifecycle_mst where DOC_TYPE='"+docType+"'"
					+ " AND TENANT_ID='"+tenantId+"' AND PROCESS_CODE = '"+processCode+"' "
					+ " order by DOC_GROUP +1 ";
			docGrp = this.jdbcTemplate.queryForList(Qry,String.class);
		} catch (Exception ex) {
			logger.error("getDistinctDocGroup Error" + ex);
		}
		return docGrp;
	}

	@Override
	public String getBudgetExcessValueByHdrId(String budegtSheetDtlId) {
		String excessValue ="";
		try {
			String Qry = "select EXCESS from budget_excess_dtl where BE_HDR_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,budegtSheetDtlId);
			excessValue =resultMap.get("EXCESS").toString();
		} catch (Exception ex) {
			logger.error("getBudgetExcessValueByHdrId Error" + ex);
		}
		return excessValue;
	}

	@Override
	public String getIndentIdByBehdrID(String hdrId) {
		String indentId="";
		try {
			String Qry = "select INDENT_ID from budget_excess_dtl where BE_HDR_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,hdrId);
			indentId = resultMap.get("INDENT_ID").toString();		
			} catch (Exception ex) {
			logger.error("getIndentIdByBehdrID Error" + ex);
		}
		return indentId;
	}

	@Override
	public String getAssemblyDes(String tenantId, String indentId) {
		String desc="";
		List<BudgetExcessAssSubAssEntity> list = new ArrayList<BudgetExcessAssSubAssEntity>();
		try {
			String qry="SELECT \r\n" + 
					"    mst.PK_ID as id , mst.PK_DESC as Des\r\n" + 
					"FROM\r\n" + 
					"    project_key_area_mst mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area area ON area.PK_ID = mst.PK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr hdr ON hdr.PKA_ID = area.PKA_ID\r\n" + 
					"WHERE\r\n" + 
					"    mst.IS_ACTIVE = '1'\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
					"        AND hdr.INDENT_ID = ? ";
			list = this.jdbcTemplate.query(qry, new BudgetExcessAssSubAssRowMapper(),tenantId,indentId);
			
			desc = Optional.ofNullable(list)
	                   .flatMap(l -> l.stream().findFirst()) // Get the first element if list is not null
	                   .map(BudgetExcessAssSubAssEntity::getDesc) // Extract description
	                   .orElse("");
			
		}catch(Exception ex) {
			logger.error("getAssemblyDes Error" + ex);
		}
		
		return desc;
	}

	@Override
	public String getSubAssemblyDes(String tenantId, String indentId) {
		List<BudgetExcessAssSubAssEntity> list = new ArrayList<BudgetExcessAssSubAssEntity>();
		String desc="";
		try {
			String qry="SELECT \r\n" + 
					"    mst.PSK_ID as id , mst.PSK_DESC as Des\r\n" + 
					"FROM\r\n" + 
					"    project_key_sub_area_mst mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area area ON area.PSK_ID = mst.PSK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr hdr ON hdr.PKA_ID = area.PKA_ID\r\n" + 
					"        AND hdr.PKSA_ID = area.PKSA_ID\r\n" + 
					"WHERE\r\n" + 
					"    mst.IS_ACTIVE = '1'\r\n" + 
					"        AND mst.TENANT_ID = ?\r\n" + 
					"        AND hdr.INDENT_ID = ? ";
			list = this.jdbcTemplate.query(qry, new BudgetExcessAssSubAssRowMapper(),tenantId,indentId);
			desc = Optional.ofNullable(list)
	                   .flatMap(l -> l.stream().findFirst()) // Get the first element if list is not null
	                   .map(BudgetExcessAssSubAssEntity::getDesc) // Extract description
	                   .orElse("");
		}catch(Exception ex) {
			logger.error("getSubAssemblyDes Error" + ex);
		}
		return desc;
	}
	
	@Override
	public String getBudgetCodeByIndentId(String indentId) {
		String budgetCost="";
		try {
			String Qry = "select case when sum(BUDGET_VALUE) > 0 then sum(BUDGET_VALUE) else 0 end  AS BUDGET_VALUE from indent_budget_dtl where INDENT_ID=?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,indentId);
			budgetCost = resultMap.get("BUDGET_VALUE").toString();		
			} catch (Exception ex) {
			logger.error("getBudgetCodeByIndentId Error" + ex);
		}
		return budgetCost;
	}

	@Override
	public String getExcessValue(String refCode) {
		String excess="";
		try {
			String Qry = "select EXCESS from budget_excess_dtl where BE_HDR_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,refCode);
			excess = resultMap.get("EXCESS").toString();		
			} catch (Exception ex) {
			logger.error("getExcessValue Error" + ex);
		}
		return excess;
	}

	@Override
	public int getCurrSeqForBudgetExcess(String refCode) {
		int curSeq=0; int cnt = 0;
		try {
				String qry = "select Count(SEQUENCE_NO) as SEQUENCE_NO from budget_excess_dtl where BE_HDR_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,refCode);
				cnt = Integer.valueOf(resultMap.get("SEQUENCE_NO").toString());
			
			    if(cnt>0) {
			       String Qry = "select SEQUENCE_NO from budget_excess_dtl where BE_HDR_ID = ?";
			       Map<String,Object> result = jdbcTemplate.queryForMap(Qry,refCode);
			       curSeq = Integer.valueOf(result.get("SEQUENCE_NO").toString());	
			    }
			    
			} catch (Exception ex) {
			logger.error("getCurrSeqForBudgetExcess Error" + ex);
		}
		return curSeq;
	}

	@Override
	public String getActualValue(String refCode) {
		String actual="";
		try {
			String Qry = "select ACTUAL_COST from budget_excess_dtl where BE_HDR_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,refCode);
			actual = resultMap.get("ACTUAL_COST").toString();		
			} catch (Exception ex) {
			logger.error("getActualValue Error" + ex);
		}
		return actual;
	}

	@Override
	public String getTargetValue(String refCode) {
		String target="";
		try {
			String Qry = "select BUDGET_COST from budget_excess_dtl where BE_HDR_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry,refCode);
			target = resultMap.get("BUDGET_COST").toString();		
			} catch (Exception ex) {
			logger.error("getTargetValue Error" + ex);
		}
		return target;
	}

	@Override
	public String getVerChechForBudgetExcessByBeHdrId(String beHdrId, String tenantId) {
		String verCheck="0";
		try {
//			String Qry = "SELECT  \n" +
//					"    COUNT(*) AS VER_CHECK\n" +
//					"FROM (\n" +
//					"    SELECT  \n" +
//					"        COUNT(*) AS SEQ_COUNT \n" +
//					"    FROM  \n" +
//					"        budget_excess_status_dtl s\n" +
//					"    INNER JOIN budget_excess_dtl hdr ON hdr.BE_HDR_ID = s.BE_HDR_ID\n" +
//					"    WHERE  \n" +
//					"        hdr.BE_HDR_ID = ? AND hdr.TENANT_ID = ?\n" +
//					"    GROUP BY s.SEQUENCE_NO\n" +
//					"    HAVING COUNT(*) > 1  -- Apply the condition directly on COUNT(*)\n" +
//					") AS subquery;\n";
			String Qry = "SELECT \n" +
					"    CASE \n" +
					"        WHEN curr.SEQUENCE_NO < prev.SEQUENCE_NO THEN 1 \n" +
					"        ELSE 0 \n" +
					"    END AS IS_VALID \n" +
					"FROM \n" +
					"    (SELECT CAST(b1.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO \n" +
					"     FROM budget_excess_status_dtl b1 \n" +
					"     INNER JOIN budget_excess_dtl dtl ON dtl.BE_HDR_ID = b1.BE_HDR_ID \n" +
					"     WHERE dtl.BE_HDR_ID = ? \n" +
					"     ORDER BY b1.BES_ID DESC \n" +
					"     LIMIT 1) AS curr, \n" +
					"    (SELECT  CAST(b.SEQUENCE_NO AS UNSIGNED) AS SEQUENCE_NO \n" +
					"     FROM budget_excess_status_dtl b \n" +
					"     INNER JOIN budget_excess_dtl dtl1 ON dtl1.BE_HDR_ID = b.BE_HDR_ID \n" +
					"     WHERE dtl1.BE_HDR_ID = ? \n" +
					"     ORDER BY b.BES_ID DESC \n" +
					"     LIMIT 1 OFFSET 1) AS prev;";

			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(Qry, beHdrId, beHdrId);
	        if (!resultList.isEmpty()) {
	            Object isValid = resultList.get(0).get("IS_VALID");
	            verCheck = isValid != null ? isValid.toString() : "0";
	        }
	    } catch (Exception ex) {
	        logger.error("getVerChechForBudgetExcessByBeHdrId Error", ex);
	    }
	    return verCheck;
	}

	public String checkIndentExcessCount(String indentId, String tenantId) {
		String check = "0";	
		try {	
			String ChckQuery ="SELECT \r\n" + 
				"    CASE \r\n" + 
				"        WHEN COUNT(*) > 0 THEN SUM(ACTUAL_EXCESS)\r\n" + 
				"        ELSE 0\r\n" + 
				"    END AS ACTUAL_EXCESS\r\n" + 
				"FROM budget_excess_dtl\r\n" + 
				"WHERE INDENT_ID = ?\r\n" + 
				"  AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChckQuery, indentId,tenantId);
			check = resultMap.get("ACTUAL_EXCESS").toString();
			}catch (Exception ex) {
			logger.error("checkIndentExcessCount method Error" + ex);
		}	
		return check;
	}
}
